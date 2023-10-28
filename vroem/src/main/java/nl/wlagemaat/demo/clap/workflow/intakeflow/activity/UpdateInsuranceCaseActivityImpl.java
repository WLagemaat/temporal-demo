package nl.wlagemaat.demo.clap.workflow.intakeflow.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.repository.InsuranceCaseRepository;
import nl.wlagemaat.demo.clap.repository.entities.InsuranceCase;
import nl.wlagemaat.demo.clap.service.ValidateCaseInputService;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateInsuranceCaseActivityImpl implements UpdateInsuranceCaseActivity {
	
	private final InsuranceCaseRepository insuranceCaseRepository;

	@Override
	public void updateDriverInsuranceCase(String insuranceNumber, String driver) {
		Optional<InsuranceCase> insuranceCaseResult = insuranceCaseRepository.findByInsuranceCaseNumber(insuranceNumber);
		if(insuranceCaseResult.isEmpty()){
			log.error("Insurance Case not found for number {}", insuranceNumber);
			throw new RuntimeException("Insurance Case not found for number " + insuranceNumber);
		}
		InsuranceCase insuranceCase = insuranceCaseResult.get();
		insuranceCase.setDriver(driver);
		insuranceCaseRepository.save(insuranceCase);
	}

	@Override
	public void updateInsuranceTierInsuranceCase(String insuranceNumber, Integer tierlevel) {
		Optional<InsuranceCase> insuranceCaseResult = insuranceCaseRepository.findByInsuranceCaseNumber(insuranceNumber);
		if(insuranceCaseResult.isEmpty()){
			log.error("Insurance Case not found for number {}", insuranceNumber);
			throw new RuntimeException("Insurance Case not found for number " + insuranceNumber);
		}
		InsuranceCase insuranceCase = insuranceCaseResult.get();
		insuranceCase.setInsuranceTier(tierlevel);
		insuranceCaseRepository.save(insuranceCase);
	}
}