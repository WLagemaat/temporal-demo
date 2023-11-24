package nl.wlagemaat.demo.clients;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;

@WorkflowInterface
public interface DetermineDriverWorkflow {

    // This is for demo purpose the same as the ManualTaskWorkFlow
    // In a real world scenario this could be a different task_queue, or they would extend from the same interface that would declare the constants
    String NAMESPACE_MANUAL = "MANUAL_FLOWS";
    String DRIVER_TASK_QUEUE = "DRIVER_TASK_QUEUE";

    @WorkflowMethod
    TaskProcessingResult processInsuranceCase(InsuranceCaseDto insuranceCaseDto);
}
