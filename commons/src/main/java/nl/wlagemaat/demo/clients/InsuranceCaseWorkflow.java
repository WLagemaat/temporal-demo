package nl.wlagemaat.demo.clients;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@WorkflowInterface
public interface InsuranceCaseWorkflow {

    String PRE_INTAKE_NAMESPACE = "PRE_INTAKE";
    String INSURANCE_CASE_TASK_QUEUE = "INSURANCE_CASE_TASK_QUEUE";

    @WorkflowMethod
    void processInsuranceCase(InsuranceCaseDto fine);
}
