package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.service.ValidateCaseInputService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateInsuranceCaseActivityImpl implements CreateInsuranceCaseActivity {
	
	private final ValidateCaseInputService validateCaseInputService;

	@Override
	public void createInsuranceCase(InsuranceCaseDto insuranceCaseDto) {
		validateCaseInputService.saveInsuranceCase(insuranceCaseDto);
	}
}