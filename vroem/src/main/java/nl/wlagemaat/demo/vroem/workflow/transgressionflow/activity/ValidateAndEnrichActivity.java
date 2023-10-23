package nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.workflow.intakeflow.activity.IntakeActivityMarker;

@ActivityInterface
public interface ValidateAndEnrichActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "Validate the fine input and enrich from external systems")
    FineProcessingResult determineLicenseplate(FineDto transgression);
}