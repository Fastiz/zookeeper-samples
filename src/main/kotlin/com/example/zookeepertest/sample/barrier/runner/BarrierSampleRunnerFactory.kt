package com.example.zookeepertest.sample.barrier.runner

import com.example.zookeepertest.sample.barrier.BarrierSampleRunMode
import org.springframework.stereotype.Component

@Component
class BarrierSampleRunnerFactory {
    fun create(barrierSampleRunMode: BarrierSampleRunMode): BarrierRunner {
        return when (barrierSampleRunMode) {
            BarrierSampleRunMode.Enter -> BarrierEnterRunner()
            BarrierSampleRunMode.Close -> BarrierCloseRunner()
            BarrierSampleRunMode.Open -> BarrierOpenRunner()
        }
    }
}
