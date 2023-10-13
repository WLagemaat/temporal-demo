package nl.wlagemaat.demo.bas.workflow.manualtasks.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.bas.model.FineDto;

@ActivityInterface
public interface CorrectDataActivity extends ManualTaskActivityMarker {
	
	@ActivityMethod(name = "correct the data of the transgression")
    boolean correctData(FineDto transgression);

}