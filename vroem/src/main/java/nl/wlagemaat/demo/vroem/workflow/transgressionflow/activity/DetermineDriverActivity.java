package nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;

@ActivityInterface
public interface DetermineDriverActivity extends TransgressionActivityMarker {
	
	@ActivityMethod(name = "Determine the driver of the transgression")
    TaskProcessingResult determineDriver(FineDto transgression);
}