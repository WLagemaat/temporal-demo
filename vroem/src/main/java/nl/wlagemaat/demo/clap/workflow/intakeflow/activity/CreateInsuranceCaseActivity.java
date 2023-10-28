package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.model.CaseProcessingResult;

@ActivityInterface
public interface CreateInsuranceCaseActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "create and store the insurance case")
    void createInsuranceCase(InsuranceCaseDto insuranceCaseDto);

}