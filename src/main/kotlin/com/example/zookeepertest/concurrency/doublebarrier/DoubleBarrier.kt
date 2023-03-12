package com.example.zookeepertest.concurrency.doublebarrier

import com.example.zookeepertest.common.observable.ObserverBuilder
import com.example.zookeepertest.presentation.WatchedEventObservable
import com.example.zookeepertest.presentation.WatchedEventObservableBuilder.filterByPathAndEvent
import kotlinx.coroutines.CompletableDeferred
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.KeeperException
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper

class DoubleBarrier(
    private val barrierNumber: Int,
    private val zooKeeper: ZooKeeper,
    private val watchedEventObservable: WatchedEventObservable,
    private val doubleBarrierName: String
) {
    lateinit var childName: String
    private val childPrefixPath = "$doubleBarrierName/child-"

    suspend fun enter() {
        try {
            zooKeeper.create(doubleBarrierName, byteArrayOf(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
        } catch (keeperException: KeeperException) {
            if (keeperException.code() != KeeperException.Code.NODEEXISTS) {
                throw keeperException
            }
        }

        val readyPath = "$doubleBarrierName/ready"

        val existsWatcher = CompletableDeferred<Unit>()

        val existsWatchObserver = ObserverBuilder.buildObserverFromLambda<WatchedEvent> {
            existsWatcher.complete(Unit)
        }

        val unsubscriptionCallback = watchedEventObservable
            .filterByPathAndEvent(
                path = readyPath,
                eventType = Watcher.Event.EventType.NodeCreated
            )
            .register(existsWatchObserver)

        try {
            val exists = zooKeeper.exists(readyPath, true)

            if (exists != null) {
                return
            }

            val childPath = zooKeeper.create(
                childPrefixPath,
                byteArrayOf(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
            )

            childName = childPath.removePrefix("$doubleBarrierName/")

            val children = zooKeeper.getChildren(doubleBarrierName, false)

            if (children.size < barrierNumber) {
                existsWatcher.await()
            }

            try {
                zooKeeper.create(readyPath, byteArrayOf(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
            } catch (keeperException: KeeperException) {
                if (keeperException.code() != KeeperException.Code.NODEEXISTS) {
                    throw keeperException
                }
            }
        } finally {
            unsubscriptionCallback.invoke()
        }
    }

    suspend fun leave() {
        try {
            zooKeeper.delete("$doubleBarrierName/ready", -1)
        } catch (keeperException: KeeperException) {
            if (keeperException.code() != KeeperException.Code.NONODE) {
                throw keeperException
            }
        }

        val children = zooKeeper.getChildren(doubleBarrierName, false)
            .filterNot { it == "ready" }

        if (children.isEmpty()) {
            return
        }

        if (children.size == 1 && children[0] == childName) {
            zooKeeper.delete("$doubleBarrierName/$childName", -1)
            return
        }

        val lowestChildren = children.minBy {
            val childNumber = it.substringAfter("child-").trim('0').toInt()
            childNumber
        }

        val waitOnPath = if (childName == lowestChildren) {
            val highestChildren = children.maxBy {
                val childNumber = it.substringAfter("child-").trim('0').toInt()
                childNumber
            }
            highestChildren
        } else {
            zooKeeper.delete("$doubleBarrierName/$childName", -1)
            lowestChildren
        }

        val existsWatcher = CompletableDeferred<Unit>()

        val existsWatchObserver = ObserverBuilder.buildObserverFromLambda<WatchedEvent> {
            existsWatcher.complete(Unit)
        }

        val unsubscriptionCallback = watchedEventObservable
            .filterByPathAndEvent(
                path = "$doubleBarrierName/$waitOnPath",
                eventType = Watcher.Event.EventType.NodeDeleted
            )
            .register(existsWatchObserver)

        val waitOnPathExists = zooKeeper.exists("$doubleBarrierName/$waitOnPath", true)

        try {
            if (waitOnPathExists != null) {
                existsWatcher.await()
            }

            return leave()
        } finally {
            unsubscriptionCallback.invoke()
        }
    }
}
