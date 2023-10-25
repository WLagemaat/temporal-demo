package nl.wlagemaat.demo.clients;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.FineDto;

@WorkflowInterface
public interface PrepareMulderWorkflow {

    // This is for demo purpose the same as the ManualTaskWorkFlow
    // In a real world scenario this would be a different workflow, or they would extend from the same interface that would declare the constants
    String PRE_INTAKE_NAMESPACE = "PRE_INTAKE";
    String VROEM_TASK_QUEUE = "MULDER_TASK_QUEUE";

    @WorkflowMethod
    void processTransgression(FineDto fine);
}
