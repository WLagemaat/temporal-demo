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
public class CreateTransgressionActivityImpl implements CreateTransgressionActivity {
	
	private final ValidateFineService validateFineService;

	@Override
	public FineProcessingResult createTransgression(FineDto transgression) {
		return validateFineService.saveTransgression(transgression);
	}
}