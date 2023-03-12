package com.example.zookeepertest.sample.barrier.runner

import com.example.zookeepertest.concurrency.Barrier

interface BarrierRunner {
    suspend fun run(clientName: String, barrier: Barrier)
}