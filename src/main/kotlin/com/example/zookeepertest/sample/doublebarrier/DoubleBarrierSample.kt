package com.example.zookeepertest.sample.doublebarrier

import com.example.zookeepertest.concurrency.doublebarrier.DoubleBarrierFactory
import com.example.zookeepertest.sample.SampleRunner
import com.example.zookeepertest.sample.doublebarrier.runner.DoubleBarrierRunner
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class DoubleBarrierSample(
    private val doubleBarrierFactory: DoubleBarrierFactory
) : SampleRunner {
    override fun run(args: List<String>) {
        val (_, barrierNumberDto, waitTimeDto) = args
        val barrierNumber = barrierNumberDto.toInt()
        val waitTime = waitTimeDto.toLong()
        val doubleBarrier = doubleBarrierFactory.create(
            doubleBarrierName = "/DoubleBarrierSample::double_barrier",
            barrierNumber = barrierNumber
        )

        val runner = DoubleBarrierRunner()

        runBlocking {
            runner.run(doubleBarrier, waitTime)
        }
    }
}
