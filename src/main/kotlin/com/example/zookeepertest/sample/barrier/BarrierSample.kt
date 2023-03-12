package com.example.zookeepertest.sample.barrier

import com.example.zookeepertest.concurrency.BarrierFactory
import com.example.zookeepertest.sample.barrier.runner.BarrierSampleRunnerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import java.lang.Thread.sleep

@Component
class BarrierSample(
    val barrierFactory: BarrierFactory,
    val barrierSampleRunnerFactory: BarrierSampleRunnerFactory,
) {
    fun run(clientName: String, barrierSampleRunMode: BarrierSampleRunMode) {
        val barrier = barrierFactory.create("/BarrierSampleDispatcher::barrier")
        val runner = barrierSampleRunnerFactory.create(
            barrierSampleRunMode = barrierSampleRunMode
        )
        runBlocking {
            withContext(Dispatchers.IO) {
                sleep(1000)
                runner.run(clientName = clientName, barrier = barrier)
            }
        }
    }
}