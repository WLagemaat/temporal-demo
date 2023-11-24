package nl.wlagemaat.demo.mass.workflow.manualtasks.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface CreateTaskForManualFixActivity extends ManualTaskActivityMarker {
	
	@ActivityMethod(name = "create a task for manual fix")
    boolean correctData(InsuranceCaseDto insuranceCaseDto);

}