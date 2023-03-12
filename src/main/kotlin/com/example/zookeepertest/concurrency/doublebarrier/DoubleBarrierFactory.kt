package com.example.zookeepertest.concurrency.doublebarrier

import com.example.zookeepertest.presentation.WatchedEventObservable
import org.apache.zookeeper.ZooKeeper
import org.springframework.stereotype.Component

@Component
class DoubleBarrierFactory(
    val zooKeeper: ZooKeeper,
    val watchedEventObservable: WatchedEventObservable
) {
    fun create(doubleBarrierName: String, barrierNumber: Int): DoubleBarrier {
        return DoubleBarrier(
            barrierNumber = barrierNumber,
            zooKeeper = zooKeeper,
            watchedEventObservable = watchedEventObservable,
            doubleBarrierName = doubleBarrierName
        )
    }
}