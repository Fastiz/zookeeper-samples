package com.example.zookeepertest.common.observable

class Subject<T> : Observable<T>, Observer<T> {
    private val basicObservable = BasicObservable<T>()

    override fun register(observer: Observer<T>): UnregisterCallback {
        return basicObservable.register(observer)
    }

    override fun next(value: T) {
        basicObservable.getObserversIterator()
            .forEach { it.next(value) }
    }

    override fun complete() {
        basicObservable.getObserversIterator()
            .forEach { it.complete() }
    }

    override fun error() {
        basicObservable.getObserversIterator()
            .forEach { it.error() }
    }
}