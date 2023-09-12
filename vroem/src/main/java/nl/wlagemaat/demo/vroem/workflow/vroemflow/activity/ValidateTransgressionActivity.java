package nl.wlagemaat.demo.vroem.workflow.vroemflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;

@ActivityInterface
public interface ValidateTransgressionActivity extends VroemActivityMarker {
	
	@ActivityMethod(name = "Retrieve additional data from other systems for bulk migration")
    FineProcessingResult validateFine(FineDto transgression);
	
	@ActivityMethod(name = "Return enrichment failed result for bulk migration")
    FineProcessingResult recover(FineDto transgression, String exceptionType, String msg);
}