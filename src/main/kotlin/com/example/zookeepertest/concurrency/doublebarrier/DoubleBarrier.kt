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
    suspend fun enter() {
        val childrenPrefixPath = "$doubleBarrierName/child-"

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

            zooKeeper.create(childrenPrefixPath, byteArrayOf(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL)

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
        TODO()
    }
}
