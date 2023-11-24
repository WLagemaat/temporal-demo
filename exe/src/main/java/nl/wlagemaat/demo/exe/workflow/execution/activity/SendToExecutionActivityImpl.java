package nl.wlagemaat.demo.exe.workflow.execution.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.activity.SendToExecutionActivity;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendToExecutionActivityImpl implements SendToExecutionActivity {
	
	@Override
	public void send(InsuranceCaseDto insuranceCaseDto, String workflowId) {
		if(doesPass(insuranceCaseDto.instantPayoutTechnicalErrorOdds())){
			throw new RuntimeException("Execution Department is not available");
		}
	}

	private boolean doesPass(Integer odds){
		int dice = ThreadLocalRandom.current().nextInt(100);
		log.info("isExecutionAvailable odds:{}, diced: {} ", odds, dice);
		return dice <= odds;
	}
}