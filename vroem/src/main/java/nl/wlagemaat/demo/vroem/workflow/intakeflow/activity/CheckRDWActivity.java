package nl.wlagemaat.demo.vroem.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.FineDto;

@ActivityInterface
public interface CheckRDWActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "Retrieve licenseplate data from RDW system")
    void determineDriver(FineDto transgression, String workflowId);
}