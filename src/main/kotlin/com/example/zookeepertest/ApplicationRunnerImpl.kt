package com.example.zookeepertest

import com.example.zookeepertest.sample.barrier.BarrierSample
import com.example.zookeepertest.sample.doublebarrier.DoubleBarrierSample
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApplicationRunnerImpl(
    val barrierSample: BarrierSample,
    val doubleBarrierSample: DoubleBarrierSample,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val argumentList = args.sourceArgs.toList()

        val (sampleName) = argumentList

        val sampleRunner = when (sampleName) {
            "barrier" -> barrierSample
            "double_barrier" -> doubleBarrierSample
            else -> throw IllegalArgumentException("The sample name does not exist $sampleName")
        }

        sampleRunner.run(argumentList)
    }
}
