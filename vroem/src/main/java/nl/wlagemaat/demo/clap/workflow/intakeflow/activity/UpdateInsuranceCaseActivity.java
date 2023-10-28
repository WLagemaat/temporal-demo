package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface UpdateInsuranceCaseActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "update and store the driver")
    void updateDriverInsuranceCase(String insuranceNumber, String driver);

    @ActivityMethod(name = "update and store the insurance tier level")
    void updateInsuranceTierInsuranceCase(String insuranceNumber, Integer tierlevel);
}