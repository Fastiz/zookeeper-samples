package com.example.zookeepertest.common.observable

import java.util.UUID

typealias UnregisterCallback = () -> Unit

open class Observable<T> {
    protected val observers = mutableMapOf<UUID, Observer<T>>()

    fun register(observer: Observer<T>): UnregisterCallback {
        val id = UUID.randomUUID()
        observers[id] = observer
        return {
            observers.remove(id)
        }
    }
}

