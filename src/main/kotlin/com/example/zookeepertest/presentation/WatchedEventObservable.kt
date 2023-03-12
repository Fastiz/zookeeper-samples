package com.example.zookeepertest.presentation

import com.example.zookeepertest.common.observable.Observable
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher

interface WatchedEventObservable : Watcher, Observable<WatchedEvent>