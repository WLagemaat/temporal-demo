package nl.wlagemaat.demo.clients.options;

import io.temporal.workflow.ChildWorkflowOptions;

import static nl.wlagemaat.demo.clients.ManualTaskWorkFlow.MANUAL_TASK_QUEUE;
import static nl.wlagemaat.demo.clients.ManualTaskWorkFlow.NAMESPACE_MANUAL;

public class ManualTaskFlowOptions {

    public static ChildWorkflowOptions getOptions() {
        return ChildWorkflowOptions.newBuilder()
                .setTaskQueue(MANUAL_TASK_QUEUE)
                .setNamespace(NAMESPACE_MANUAL)
                .build();
    }
}
