package nl.wlagemaat.demo.vroem.workflow.transgressionflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.TransgressionWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;

public class TransgressionWorkflowImpl implements TransgressionWorkflow {

    @Override
    public void processTransgression(FineDto fine) {

        IntakeWorkflow intakeWorkflow = Workflow.newChildWorkflowStub(IntakeWorkflow.class);
        Promise<TransgressionValidationEnrichmentResult> validationEnrichmentResult = Async.function(intakeWorkflow::transgressionIntake, fine);
        var validationResult = validationEnrichmentResult.get();

        if (!validationResult.isValid()) {
            // throw exception
            // log error

            return;
        }

        if(validationResult.isMulder()){
            // go mulder flow
        } else {
            // go OM-Afdoening flow
        }

    }
}
