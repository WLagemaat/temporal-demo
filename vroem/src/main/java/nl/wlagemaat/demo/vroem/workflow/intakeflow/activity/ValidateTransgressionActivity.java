package nl.wlagemaat.demo.vroem.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;

@ActivityInterface
public interface ValidateTransgressionActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "Validate the Data of the received transgression")
    FineProcessingResult validateFine(FineDto transgression);
	
	@ActivityMethod(name = "Validate the Data of the received transgression from errorstate")
    FineProcessingResult recover(FineDto transgression, String exceptionType, String msg);
}