package com.example.zookeepertest.common.observable

class Subject<T> : Observable<T>(), Observer<T> {

    override fun next(value: T) {
        observers.forEach { it.value.next(value) }
    }

    override fun complete() {
        observers.forEach { it.value.complete() }
    }

    override fun error() {
        observers.forEach { it.value.error() }
    }
}