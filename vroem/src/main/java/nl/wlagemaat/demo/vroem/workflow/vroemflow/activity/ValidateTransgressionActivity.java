package nl.wlagemaat.demo.vroem.workflow.vroemflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;

@ActivityInterface
public interface ValidateTransgressionActivity extends VroemActivityMarker {
	
	@ActivityMethod(name = "Validate the transgression for completeness")
    FineProcessingResult validateFine(FineDto transgression);
	
	@ActivityMethod(name = "Return validation failed for the fine")
    FineProcessingResult recover(FineDto transgression, String exceptionType, String msg);
}