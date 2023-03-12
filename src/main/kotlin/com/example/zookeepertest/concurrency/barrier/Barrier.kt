package com.example.zookeepertest.concurrency.barrier

import com.example.zookeepertest.common.observable.ObserverBuilder
import com.example.zookeepertest.concurrency.WatchedEventObservableBuilder.filterByPathAndEvent
import com.example.zookeepertest.presentation.WatchedEventObservable
import kotlinx.coroutines.CompletableDeferred
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE
import org.apache.zookeeper.ZooKeeper

class Barrier(
    private val zooKeeper: ZooKeeper,
    private val watchedEventObservable: WatchedEventObservable,
    private val barrierName: String
) {
    suspend fun enter() {
        val existsWatcher = CompletableDeferred<Unit>()

        val existsWatchObserver = ObserverBuilder.buildObserverFromLambda<WatchedEvent> {
            existsWatcher.complete(Unit)
        }

        val unsubscriptionCallback = watchedEventObservable
            .filterByPathAndEvent(
                path = barrierName,
                eventType = Watcher.Event.EventType.NodeDeleted
            )
            .register(existsWatchObserver)

        val exists = zooKeeper.exists(barrierName, true)

        if (exists != null) {
            existsWatcher.await()
        }

        unsubscriptionCallback.invoke()
    }

    fun close() {
        zooKeeper.create(barrierName, byteArrayOf(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL)
    }

    fun open() {
        zooKeeper.delete(barrierName, -1)
    }
}