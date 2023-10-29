package nl.wlagemaat.demo.clap.workflow.insuranceflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.service.RdwService;
import nl.wlagemaat.demo.clap.workflow.intakeflow.activity.CheckRDWActivity;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.stereotype.Component;

import static nl.wlagemaat.demo.clap.util.VroemUtilities.doesPass;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendToExecutionActivityImpl implements SendToExecutionActivity {
	
	@Override
	public void send(InsuranceCaseDto insuranceCaseDto, String workflowId) {
		if(!doesPass("isExecutionAvailable" , insuranceCaseDto.instantPayoutTechnicalErrorOdds())){
			throw new RuntimeException("Execution Department is not available");
		}
	}
}