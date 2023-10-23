package nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.service.rdw.RdwService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateAndEnrichActivityImpl implements ValidateAndEnrichActivity {
	
	private final RdwService rdwService;

	@Override
	public FineProcessingResult determineLicenseplate(FineDto input) {
		return rdwService.determineLicenseplate(input);
	}
	

}