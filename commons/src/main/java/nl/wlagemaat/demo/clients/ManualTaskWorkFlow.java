package nl.wlagemaat.demo.clients;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;

@WorkflowInterface
public interface ManualTaskWorkFlow {
    String MANUAL_TASK_QUEUE = "MANUAL_TASK_QUEUE";
    String DRIVER_TASK_QUEUE = "DRIVER_TASK_QUEUE";
    String NAMESPACE_MANUAL = "MANUAL_FLOWS";

    @WorkflowMethod
    TaskProcessingResult processTask(InsuranceCaseDto insuranceCaseDto);
}
