package nl.wlagemaat.demo.vroem.workflow.transgressionflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import nl.wlagemaat.demo.clients.DetermineDriverWorkflow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.TransgressionWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;

public class TransgressionWorkflowImpl implements TransgressionWorkflow {

    /**
     * This workflow mainly controls child workflows which can be distributed among teams and applications
     * @param fine the actual input
     */
    @Override
    public void processTransgression(FineDto fine) {


        // Call Vroem and create a childflow for Vroem
        IntakeWorkflow intakeWorkflow = Workflow.newChildWorkflowStub(IntakeWorkflow.class);
        Promise<TransgressionValidationEnrichmentResult> validationEnrichmentResult = Async.function(intakeWorkflow::transgressionIntake, fine);
        var validationResult = validationEnrichmentResult.get();


        if (!validationResult.isValid()) {
            // return to the caller the error message in case of validation error
            // throw an exception in the weird case something else happened
            return;
        }

        if(validationResult.isMulder()){
            // go mulder flow

        } else {
            // go OM-Afdoening flow
            DetermineDriverWorkflow determineDriverWorkflow = Workflow.newChildWorkflowStub(DetermineDriverWorkflow.class);
            Promise<TaskProcessingResult> taskProcessingResultPromise = Async.function(determineDriverWorkflow::processTransgression, fine);
            taskProcessingResultPromise.get();

        }

    }
}
