package nl.wlagemaat.demo.mass.workflow.manualtasks.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface CorrectDataActivity extends ManualTaskActivityMarker {
	
	@ActivityMethod(name = "correct the data of the transgression")
    boolean correctData(InsuranceCaseDto insuranceCaseDto);

}