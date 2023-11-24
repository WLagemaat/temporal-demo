package nl.wlagemaat.demo.clap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.exception.UnknownInsuranceCaseError;
import nl.wlagemaat.demo.clap.model.request.IotRequest;
import nl.wlagemaat.demo.clap.repository.InsuranceCaseRepository;
import nl.wlagemaat.demo.clap.repository.entities.InsuranceCase;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IotService {

    private final InsuranceCaseRepository insuranceCaseRepository;
    private final RestService restService;

    @Value("${iot.request.url}")
    private String iotRequestUrl;

    /**
     * The inital request to the IOT to request the insurance tier level
     * This is an async request
     * <p>
     * The answer will come by a Temporal Signal
     */
    public void sendTierDeterminationRequestToIot(final InsuranceCaseDto insuranceCaseDto, String workflowId){
        Optional<InsuranceCase> insuranceCase = insuranceCaseRepository.findByInsuranceCaseNumber(insuranceCaseDto.insuranceNumber());

        if(insuranceCase.isEmpty()){
            throw new UnknownInsuranceCaseError("Insurance Case not found!");
        }

        IotRequest requestDto = new IotRequest(insuranceCase.get().getDriver(), workflowId);
        restService.sendPostRequest(iotRequestUrl, requestDto);
    }
}