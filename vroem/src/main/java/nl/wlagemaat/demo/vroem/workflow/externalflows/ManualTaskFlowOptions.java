package nl.wlagemaat.demo.vroem.workflow.externalflows;

import io.temporal.workflow.ChildWorkflowOptions;

import static nl.wlagemaat.demo.vroem.workflow.externalflows.ManualTaskWorkFlow.MANUAL_TASK_QUEUE;

public class ManualTaskFlowOptions {

    public static ChildWorkflowOptions getOptions() {
        return ChildWorkflowOptions.newBuilder()
//                .setWorkflowRunTimeout(java.time.Duration.ofMinutes(50))
//                .setWorkflowTaskTimeout(java.time.Duration.ofMinutes(20))
                .setTaskQueue(MANUAL_TASK_QUEUE)
                .build();
    }
}
