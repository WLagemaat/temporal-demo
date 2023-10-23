package nl.wlagemaat.demo.vroem.workflow.intakeflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.service.validate.ValidateFineService;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateTransgressionActivityImpl implements ValidateTransgressionActivity {
	
	private final ValidateFineService validateFineService;


	@Override
	public FineProcessingResult validateFine(FineDto input) {
		return validateFineService.validate(input);
	}
	
	public FineProcessingResult recover(FineDto input, String exceptionType, String msg) {
		return FineProcessingResult.builder()
				.succeeded(false)
				.transgressionNumber(input.transgressionNumber())
				.errorMessage(exceptionType + ": "+msg)
				.build();
	}
}