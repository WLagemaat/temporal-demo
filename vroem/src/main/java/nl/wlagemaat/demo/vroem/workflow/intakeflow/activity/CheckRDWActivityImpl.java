package nl.wlagemaat.demo.vroem.workflow.intakeflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.service.rdw.RdwService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckRDWActivityImpl implements CheckRDWActivity {
	
	private final RdwService rdwService;

	@Override
	public void determineDriver(FineDto input, String workflowId) {
		rdwService.sendDeterminationRequestToRdw(input, workflowId);
	}
	

}