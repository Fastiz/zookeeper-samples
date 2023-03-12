package com.example.zookeepertest.concurrency

import com.example.zookeepertest.common.observable.ObserverBuilder
import com.example.zookeepertest.presentation.WatchedEventObservable
import kotlinx.coroutines.CompletableDeferred
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper

class Barrier(
    private val zooKeeper: ZooKeeper,
    private val watchedEventObservable: WatchedEventObservable,
    private val barrierName: String
) {
    suspend fun enter() {
        val existsWatcher = CompletableDeferred<Unit>()

        val existsWatchObserver = ObserverBuilder.buildObserverFromLambda<WatchedEvent> { value ->
            if (value.path != barrierName) {
                return@buildObserverFromLambda
            }

            if (value.type != Watcher.Event.EventType.NodeDeleted) {
                return@buildObserverFromLambda
            }

            existsWatcher.complete(Unit)
        }

        val unsubscriptionCallback = watchedEventObservable.register(existsWatchObserver)

        val exists = zooKeeper.exists(barrierName, true)

        if (exists != null) {
            existsWatcher.await()
        }

        unsubscriptionCallback.invoke()
    }

    fun close() {
        zooKeeper.create(barrierName, byteArrayOf(), emptyList(), CreateMode.EPHEMERAL)
    }

    fun open() {
        zooKeeper.delete(barrierName, -1)
    }
}