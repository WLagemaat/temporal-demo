package nl.wlagemaat.demo.vroem.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;

@ActivityInterface
public interface CheckRDWActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "Retrieve licenseplate data from RDW system")
    FineProcessingResult determineLicenseplate(FineDto transgression);
}