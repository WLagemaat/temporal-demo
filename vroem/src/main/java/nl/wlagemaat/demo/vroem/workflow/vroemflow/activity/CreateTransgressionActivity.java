package nl.wlagemaat.demo.vroem.workflow.vroemflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;

@ActivityInterface
public interface CreateTransgressionActivity extends VroemActivityMarker {
	
	@ActivityMethod(name = "create and store the transgression")
    FineProcessingResult createTransgression(FineDto transgression);

}