package com.example.zookeepertest.configuration

import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ZooKeeperConfiguration(
    val watcher: Watcher,
    @param:Value("\${zookeeper.connectString}")
    val connectString: String,
    @param:Value("\${zookeeper.sessionTimeout}")
    val sessionTimeout: Int
) {

    @Bean
    fun zooKeeper(): ZooKeeper {
        return ZooKeeper(connectString, sessionTimeout, watcher)
    }
}
