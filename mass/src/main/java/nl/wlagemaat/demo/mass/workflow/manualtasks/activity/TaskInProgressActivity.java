package nl.wlagemaat.demo.mass.workflow.manualtasks.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface TaskInProgressActivity extends ManualTaskActivityMarker {
	
	@ActivityMethod(name = "task is in progress")
    boolean taskInProgress(InsuranceCaseDto insuranceCaseDto);

}