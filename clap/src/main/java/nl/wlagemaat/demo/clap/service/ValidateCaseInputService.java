package nl.wlagemaat.demo.clap.service;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.clap.repository.entities.InsuranceCase;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.model.CaseProcessingResult;
import nl.wlagemaat.demo.clap.repository.InsuranceCaseRepository;
import org.springframework.stereotype.Service;

import static nl.wlagemaat.demo.clap.util.VroemUtilities.doesPass;
import static nl.wlagemaat.demo.clap.util.VroemUtilities.generateLicensePlate;

@Service
@RequiredArgsConstructor
public class ValidateCaseInputService {

    private final InsuranceCaseRepository insuranceCaseRepository;

    /**
     * Based on the supplied odds, determines if the insurance case is valid
     */
    public CaseProcessingResult validate(InsuranceCaseDto insuranceCaseDto){
        var result = CaseProcessingResult.builder();
        if(doesPass("isValidFine" , insuranceCaseDto.validOdds())){
            result.succeeded(true);
        } else {
            result.succeeded(false).errorMessage("Invalid Input, please check your Insurance Case");
        }
        return result.build();
    }

    /**
     * Stores the transgression in the database
     */
    public void saveInsuranceCase(InsuranceCaseDto insuranceCaseDto){
        InsuranceCase insuranceCase = insuranceCaseRepository.findByInsuranceCaseNumber(insuranceCaseDto.insuranceNumber()).orElse( new InsuranceCase());
        insuranceCase.setInsuranceCaseNumber(insuranceCaseDto.insuranceNumber());
        insuranceCase.setMinorSeverity(insuranceCaseDto.isMinorSeverity());
        insuranceCase.setLicensePlate(generateLicensePlate());
        insuranceCaseRepository.save(insuranceCase);
    }
}
