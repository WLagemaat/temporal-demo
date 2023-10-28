package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface CheckInsuranceTierActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "Retrieve insurance tier from external service")
    void determineTier(InsuranceCaseDto insuranceCaseDto, String workflowId);
}