package nl.wlagemaat.demo.clap.workflow.insuranceflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import nl.wlagemaat.demo.clients.DetermineDriverWorkflow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.InsuranceCaseWorkflow;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.InsuranceCaseValidationEnrichmentResult;

public class InsuranceCaseWorkflowImpl implements InsuranceCaseWorkflow {

    /**
     * This workflow mainly controls child workflows which can be distributed among teams and applications
     * @param insuranceCaseDto the actual input
     */
    @Override
    public void processInsuranceCase(InsuranceCaseDto insuranceCaseDto) {

        //create a childflow for CLAP, which CLAP-Worker will process
        IntakeWorkflow intakeWorkflow = Workflow.newChildWorkflowStub(IntakeWorkflow.class);
        Promise<InsuranceCaseValidationEnrichmentResult> validationEnrichmentResult = Async.function(intakeWorkflow::insuranceCaseIntake, insuranceCaseDto);
        var validationResult = validationEnrichmentResult.get();

        if (!validationResult.isValid()) {
            // return to the caller the error message in case of validation error
            // throw an exception in the weird case something else happened
            return;
        }

        if(validationResult.isMinorSeverity()){
            // go mulder flow

        } else {
            // go OM-Afdoening flow
            DetermineDriverWorkflow determineDriverWorkflow = Workflow.newChildWorkflowStub(DetermineDriverWorkflow.class);
            Promise<TaskProcessingResult> taskProcessingResultPromise = Async.function(determineDriverWorkflow::processInsuranceCase, insuranceCaseDto);
            taskProcessingResultPromise.get();

        }

    }
}
