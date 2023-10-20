package nl.wlagemaat.demo.vroem.workflow.vroemflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.model.ValidatedFineDto;
import nl.wlagemaat.demo.vroem.rdw.RdwService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckRDWActivityImpl implements CheckRDWActivity {
	
	private final RdwService rdwService;

	@Override
	public FineProcessingResult determineLicenseplate(ValidatedFineDto input) {
		return rdwService.determineLicenseplate(input);
	}
	

}