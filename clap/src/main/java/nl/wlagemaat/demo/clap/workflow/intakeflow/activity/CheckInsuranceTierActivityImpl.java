package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.service.IotService;
import nl.wlagemaat.demo.clap.service.RdwService;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckInsuranceTierActivityImpl implements CheckInsuranceTierActivity {
	
	private final IotService iotService;

	@Override
	public void determineTier(InsuranceCaseDto insuranceCaseDto, String workflowId) {
		iotService.sendTierDeterminationRequestToIot(insuranceCaseDto, workflowId);
	}
}