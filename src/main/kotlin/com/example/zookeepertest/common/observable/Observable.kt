package com.example.zookeepertest.common.observable

typealias UnregisterCallback = () -> Unit

interface Observable<T> {
    fun register(observer: Observer<T>): UnregisterCallback
}

