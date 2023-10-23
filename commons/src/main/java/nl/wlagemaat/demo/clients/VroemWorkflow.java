package nl.wlagemaat.demo.clients;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.FineDto;

@WorkflowInterface
public interface VroemWorkflow {

    String PRE_INTAKE_NAMESPACE = "PRE_INTAKE";
    String VROEM_TASK_QUEUE = "VROEM_TASK_QUEUE";

    @WorkflowMethod
    void processTransgression(FineDto fine);
}
