package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.model.CaseProcessingResult;

@ActivityInterface
public interface ValidateInsuranceCaseActivity extends IntakeActivityMarker {
	
	@ActivityMethod(name = "Validate the Data of the received insurance case")
    CaseProcessingResult validateCase(InsuranceCaseDto insuranceCaseDto);

}