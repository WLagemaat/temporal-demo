package nl.wlagemaat.demo.worm.workflow.activity;

import io.temporal.workflow.SignalMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.FineDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetermineDriverActivityImpl implements DetermineDriverActivity {
	
	@Override
	public void createDeterminationTask(FineDto input) {
		log.info("Waiting for signal that task is finished");
	}

	@SignalMethod
	public void signal(String signal) {
		log.info("Received signal: {}", signal);

	}
	

}