package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.service.ValidateCaseInputService;
import nl.wlagemaat.demo.clap.model.CaseProcessingResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateInsuranceCaseActivityImpl implements ValidateInsuranceCaseActivity {
	
	private final ValidateCaseInputService validateCaseInputService;

	@Override
	public CaseProcessingResult validateCase(InsuranceCaseDto input) {
		return validateCaseInputService.validate(input);
	}

}