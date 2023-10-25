package nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.DetermineDriverWorkflow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetermineDrvierActivityImpl implements DetermineDriverActivity {
	
	@Override
	public TaskProcessingResult determineDriver(FineDto fineDto) {
		// start child flow to validate and enrich
		// this should and could be done within a service class (want to keep the demo plain & simple)
		DetermineDriverWorkflow determineDriverWorkflow = Workflow.newChildWorkflowStub(DetermineDriverWorkflow.class);
		Promise<TaskProcessingResult> taskProcessingResultPromise = Async.function(determineDriverWorkflow::processTransgression, fineDto);
		return taskProcessingResultPromise.get();
	}
	

}