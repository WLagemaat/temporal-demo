package nl.wlagemaat.demo.mass.workflow.manualtasks.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskFinishedActivityImpl implements TaskFinishedActivity {

	@Override
	public boolean finishTask(InsuranceCaseDto insuranceCaseDto) {
		return true;
	}
}