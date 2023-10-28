package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface CheckRDWActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "Retrieve license plate from RDW system")
    void determineDriver(InsuranceCaseDto insuranceCaseDto, String workflowId);
}