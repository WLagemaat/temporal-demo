package nl.wlagemaat.demo.vroem.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.vroem.model.FineDto;

@WorkflowInterface
public interface VroemWorkflow {

    @WorkflowMethod
    void processTransgression(FineDto fine);
}
