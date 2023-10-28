package nl.wlagemaat.demo.clients;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.InsuranceCaseValidationEnrichmentResult;

@WorkflowInterface
public interface IntakeWorkflow {

    String PRE_INTAKE_NAMESPACE = "PRE_INTAKE";
    String VROEM_TASK_QUEUE = "VROEM_TASK_QUEUE";

    @WorkflowMethod
    InsuranceCaseValidationEnrichmentResult insuranceCaseIntake(InsuranceCaseDto fine);

    @SignalMethod(name = "driverByRDWSignal")
    void driverByRDW(String driver);

    @SignalMethod(name = "tierLevelByIOTSignal")
    void insuranceTier(int tierLevel);
}
