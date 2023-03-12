package com.example.zookeepertest.sample.barrier.runner

import com.example.zookeepertest.concurrency.Barrier
import java.util.logging.Logger

class BarrierEnterRunner : BarrierRunner {
    private val logger = Logger.getLogger(javaClass.name)

    override suspend fun run(clientName: String, barrier: Barrier) {
        logger.info("($clientName): starting enter runner")

        logger.info("($clientName): attempting to enter barrier")

        barrier.enter()

        logger.info("($clientName): entered barrier")
    }
}