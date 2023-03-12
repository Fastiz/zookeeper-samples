package com.example.zookeepertest.sample.barrier.runner

import com.example.zookeepertest.concurrency.Barrier
import java.util.logging.Logger

class BarrierCloseRunner : BarrierRunner {
    private val logger = Logger.getLogger(javaClass.name)

    override suspend fun run(clientName: String, barrier: Barrier) {
        logger.info("($clientName): starting close runner")

        logger.info("($clientName): closing barrier")

        barrier.close()

        logger.info("($clientName): closed barrier")

        logger.info("($clientName): entering barrier")

        barrier.enter()

        logger.info("($clientName): entered barrier")
    }
}