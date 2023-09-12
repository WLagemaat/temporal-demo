package nl.wlagemaat.demo.vroem.workflow.vroemflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.vroem.intake.FineIntakeService;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateTransgressionActivityImpl implements ValidateTransgressionActivity {
	
	private final FineIntakeService fineIntakeService;


	@Override
	public FineProcessingResult validateFine(FineDto input) {
		return fineIntakeService.validate(input);
	}
	
	public FineProcessingResult recover(FineDto input, String exceptionType, String msg) {
		return FineProcessingResult.builder()
				.succeeded(false)
				.transgressionNumber(input.transgressionNumber())
				.errorMessage(exceptionType + ": "+msg)
				.build();
	}
}