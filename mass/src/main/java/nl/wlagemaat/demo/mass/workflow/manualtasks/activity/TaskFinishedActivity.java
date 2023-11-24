package nl.wlagemaat.demo.mass.workflow.manualtasks.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface TaskFinishedActivity extends ManualTaskActivityMarker {
	
	@ActivityMethod(name = "Finish the task")
    boolean finishTask(InsuranceCaseDto insuranceCaseDto);

}