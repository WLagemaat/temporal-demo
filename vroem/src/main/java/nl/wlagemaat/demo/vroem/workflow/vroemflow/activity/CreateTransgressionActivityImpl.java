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
public class CreateTransgressionActivityImpl implements CreateTransgressionActivity {
	
	private final FineIntakeService fineIntakeService;

	@Override
	public FineProcessingResult createTransgression(FineDto transgression) {
		return fineIntakeService.saveTransgression(transgression);
	}
}