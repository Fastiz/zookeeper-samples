package com.example.zookeepertest.sample.barrier

import com.example.zookeepertest.concurrency.BarrierFactory
import com.example.zookeepertest.sample.barrier.runner.BarrierSampleRunnerFactory
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class BarrierSample(
    val barrierFactory: BarrierFactory,
    val barrierSampleRunnerFactory: BarrierSampleRunnerFactory,
) {
    fun run(args: List<String>) {
        val (clientName, runnerModeDto) = args

        val runnerMode = when (runnerModeDto) {
            "open" -> BarrierSampleRunMode.Open
            "close" -> BarrierSampleRunMode.Close
            "enter" -> BarrierSampleRunMode.Enter
            else -> throw IllegalArgumentException("Invalid runner mode $runnerModeDto")
        }

        val barrier = barrierFactory.create("/BarrierSampleDispatcher::barrier")
        val runner = barrierSampleRunnerFactory.create(
            barrierSampleRunMode = runnerMode
        )

        runBlocking {
            runner.run(clientName = clientName, barrier = barrier)
        }
    }
}