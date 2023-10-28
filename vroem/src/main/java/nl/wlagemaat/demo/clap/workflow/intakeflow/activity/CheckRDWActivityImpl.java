package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.service.RdwService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckRDWActivityImpl implements CheckRDWActivity {
	
	private final RdwService rdwService;

	@Override
	public void determineDriver(InsuranceCaseDto input, String workflowId) {
		rdwService.sendDriverRequestToRdw(input, workflowId);
	}
	

}