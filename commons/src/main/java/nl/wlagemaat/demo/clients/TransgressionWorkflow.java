package nl.wlagemaat.demo.clients;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.FineDto;

@WorkflowInterface
public interface TransgressionWorkflow {

    String PRE_INTAKE_NAMESPACE = "PRE_INTAKE";
    String TRANSGRESSION_TASK_QUEUE = "TRANSGRESSION_TASK_QUEUE";

    @WorkflowMethod
    void processTransgression(FineDto fine);
}
