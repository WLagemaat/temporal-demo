package nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;

@ActivityInterface
public interface ValidateAndEnrichActivity extends TransgressionActivityMarker {
	
	@ActivityMethod(name = "Validate the fine input and enrich from external systems")
    TransgressionValidationEnrichmentResult validateAndEnrich(FineDto transgression);
}