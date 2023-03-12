package com.example.zookeepertest.presentation

import com.example.zookeepertest.common.observable.Observable
import com.example.zookeepertest.common.observable.ObservableBuilder.filter
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher.Event.EventType

object WatchedEventObservableBuilder {
    fun Observable<WatchedEvent>.filterByPathAndEvent(path: String, eventType: EventType): Observable<WatchedEvent> {
        return this.filter { value ->
            if (value.path != path) {
                return@filter false
            }

            if (value.type != eventType) {
                return@filter false
            }

            return@filter true
        }
    }
}