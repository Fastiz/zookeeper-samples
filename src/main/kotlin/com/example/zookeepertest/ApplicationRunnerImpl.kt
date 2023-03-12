package com.example.zookeepertest

import com.example.zookeepertest.sample.barrier.BarrierSample
import com.example.zookeepertest.sample.barrier.BarrierSampleRunMode
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApplicationRunnerImpl(
    val barrierSample: BarrierSample
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val (clientName, runnerModeDto) = args.sourceArgs

        val runnerMode = when (runnerModeDto) {
            "open" -> BarrierSampleRunMode.Open
            "close" -> BarrierSampleRunMode.Close
            "enter" -> BarrierSampleRunMode.Enter
            else -> throw IllegalArgumentException("Invalid runner mode $runnerModeDto")
        }

        barrierSample.run(
            clientName = clientName,
            barrierSampleRunMode = runnerMode
        )
    }
}
