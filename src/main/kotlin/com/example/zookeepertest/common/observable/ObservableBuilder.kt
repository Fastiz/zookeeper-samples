package com.example.zookeepertest.common.observable

object ObservableBuilder {
    fun <T> Observable<T>.filter(predicate: (input: T) -> Boolean): Observable<T> {
        val subject = Subject<T>()

        register(
            object : Observer<T> {
                override fun next(value: T) {
                    if (!predicate(value)) {
                        return
                    }

                    subject.next(value)
                }
            }
        )

        return subject
    }
}