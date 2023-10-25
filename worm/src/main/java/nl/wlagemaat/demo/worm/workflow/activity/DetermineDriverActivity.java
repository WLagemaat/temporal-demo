package nl.wlagemaat.demo.worm.workflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.FineDto;

@ActivityInterface
public interface DetermineDriverActivity extends DetermineDriverActivityMarker {
	
	@ActivityMethod(name = "create the task to determine the driver")
    void createDeterminationTask(FineDto transgression);

}