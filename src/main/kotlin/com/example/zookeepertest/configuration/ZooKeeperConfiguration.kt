package com.example.zookeepertest.configuration

import com.example.zookeepertest.presentation.WatchedEventObservable
import org.apache.zookeeper.ZooKeeper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ZooKeeperConfiguration(
    @param:Value("\${zookeeper.connectString}")
    val connectString: String,
    @param:Value("\${zookeeper.sessionTimeout}")
    val sessionTimeout: Int
) {

    @Bean
    fun zooKeeper(watcher: WatchedEventObservable): ZooKeeper {
        return ZooKeeper(connectString, sessionTimeout, watcher)
    }
}
