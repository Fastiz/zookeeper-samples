package com.example.zookeepertest.presentation

import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class WatcherImpl : Watcher {
    val logger = Logger.getLogger(javaClass.name)

    override fun process(event: WatchedEvent) {
        logger.info("Received an event: $event")
    }
}
