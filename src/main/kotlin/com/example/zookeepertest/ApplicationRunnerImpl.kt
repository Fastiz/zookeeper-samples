package com.example.zookeepertest

import com.example.zookeepertest.sample.barrier.BarrierSample
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApplicationRunnerImpl(
    val barrierSample: BarrierSample
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        barrierSample.run(args.sourceArgs.toList())
    }
}
