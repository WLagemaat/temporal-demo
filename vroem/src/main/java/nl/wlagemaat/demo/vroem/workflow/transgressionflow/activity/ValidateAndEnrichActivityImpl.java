package nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateAndEnrichActivityImpl implements ValidateAndEnrichActivity {
	
	@Override
	public TransgressionValidationEnrichmentResult validateAndEnrich(FineDto input) {
		// start child flow to validate and enrich
		// this should and could be done within a service class (want to keep the demo plain & simple)

		// tell me how to call temporal childflow from within an activity

		IntakeWorkflow intakeWorkflow = Workflow.newChildWorkflowStub(IntakeWorkflow.class);
		Promise<TransgressionValidationEnrichmentResult> validationEnrichmentResult = Async.function(intakeWorkflow::transgressionIntake, input);
		return validationEnrichmentResult.get();
	}
	

}