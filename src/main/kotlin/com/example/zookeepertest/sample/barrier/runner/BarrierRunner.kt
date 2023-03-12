package com.example.zookeepertest.sample.barrier.runner

import com.example.zookeepertest.concurrency.barrier.Barrier

interface BarrierRunner {
    suspend fun run(clientName: String, barrier: Barrier)
}