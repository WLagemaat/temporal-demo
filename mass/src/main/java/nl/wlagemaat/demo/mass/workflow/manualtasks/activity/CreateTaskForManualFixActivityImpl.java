package nl.wlagemaat.demo.mass.workflow.manualtasks.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTaskForManualFixActivityImpl implements CreateTaskForManualFixActivity {

	@Override
	public boolean correctData(InsuranceCaseDto insuranceCaseDto) {
		return true;
	}
}