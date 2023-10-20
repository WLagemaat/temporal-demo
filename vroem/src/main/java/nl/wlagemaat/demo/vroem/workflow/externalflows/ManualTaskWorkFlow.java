package nl.wlagemaat.demo.vroem.workflow.externalflows;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.TaskProcessingResult;

@WorkflowInterface
public interface ManualTaskWorkFlow {
    String MANUAL_TASK_QUEUE = "MANUAL_TASK_QUEUE";

    @WorkflowMethod
    TaskProcessingResult processTask(FineDto fine);
}
