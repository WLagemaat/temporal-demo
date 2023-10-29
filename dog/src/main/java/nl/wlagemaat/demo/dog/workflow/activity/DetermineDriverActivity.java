package nl.wlagemaat.demo.dog.workflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface DetermineDriverActivity extends DetermineDriverActivityMarker {
	
	@ActivityMethod(name = "create the task to determine the driver")
    void createDeterminationTask(InsuranceCaseDto insuranceCaseDto);

}