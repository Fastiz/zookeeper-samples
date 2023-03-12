package com.example.zookeepertest.presentation

import com.example.zookeepertest.common.observable.Observer
import com.example.zookeepertest.common.observable.Subject
import com.example.zookeepertest.common.observable.UnregisterCallback
import org.apache.zookeeper.WatchedEvent
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class WatchedEventObservableImpl : WatchedEventObservable {
    private val logger = Logger.getLogger(javaClass.name)

    private val eventSubject = Subject<WatchedEvent>()

    override fun process(event: WatchedEvent) {
        logger.info("Received an event: $event")
        eventSubject.next(event)
    }

    override fun register(observer: Observer<WatchedEvent>): UnregisterCallback {
        return eventSubject.register(observer)
    }
}
