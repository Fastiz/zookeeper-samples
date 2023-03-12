package com.example.zookeepertest.sample.doublebarrier.runner

import com.example.zookeepertest.concurrency.doublebarrier.DoubleBarrier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import java.util.logging.Logger

class DoubleBarrierRunner {
    private val logger = Logger.getLogger(javaClass.name)

    suspend fun run(doubleBarrier: DoubleBarrier, waitTime: Long) {
        logger.info("starting runner")

        logger.info("entering barrier")

        doubleBarrier.enter()

        logger.info("entered barrier")

        withContext(Dispatchers.IO) {
            sleep(waitTime)
        }

        logger.info("leaving barrier")

        doubleBarrier.leave()

        logger.info("left barrier")
    }
}