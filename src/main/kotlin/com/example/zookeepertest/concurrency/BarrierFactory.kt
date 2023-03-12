package com.example.zookeepertest.concurrency

import com.example.zookeepertest.presentation.WatchedEventObservable
import org.apache.zookeeper.ZooKeeper
import org.springframework.stereotype.Component

@Component
class BarrierFactory(
    val zooKeeper: ZooKeeper,
    val watchedEventObservable: WatchedEventObservable
) {
    fun create(barrierName: String): Barrier {
        return Barrier(
            zooKeeper = zooKeeper,
            watchedEventObservable = watchedEventObservable,
            barrierName = barrierName
        )
    }
}