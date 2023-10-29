package nl.wlagemaat.demo.dog.workflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetermineDriverActivityImpl implements DetermineDriverActivity {
	
	@Override
	public void createDeterminationTask(InsuranceCaseDto input) {
		log.info("DOG-Task gets finished");
	}

}