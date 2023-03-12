package com.example.zookeepertest.sample.barrier.runner

import com.example.zookeepertest.concurrency.barrier.Barrier
import java.util.logging.Logger

class BarrierOpenRunner : BarrierRunner {
    private val logger = Logger.getLogger(javaClass.name)

    override suspend fun run(clientName: String, barrier: Barrier) {
        logger.info("($clientName): starting open runner")

        logger.info("($clientName): opening barrier")

        barrier.open()

        logger.info("($clientName): opened barrier")
    }
}