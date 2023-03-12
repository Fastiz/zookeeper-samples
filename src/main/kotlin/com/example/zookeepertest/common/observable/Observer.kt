package com.example.zookeepertest.common.observable

interface Observer<in T> {
    fun next(value: T)
    fun complete()
    fun error()
}