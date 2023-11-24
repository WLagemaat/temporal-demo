package nl.wlagemaat.demo.clap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.exception.TechnicalRdwError;
import nl.wlagemaat.demo.clap.exception.UnknownInsuranceCaseError;
import nl.wlagemaat.demo.clap.model.request.RdwRequest;
import nl.wlagemaat.demo.clap.repository.InsuranceCaseRepository;
import nl.wlagemaat.demo.clap.repository.entities.InsuranceCase;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static nl.wlagemaat.demo.clap.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
@Slf4j
public class RdwService {

    private final InsuranceCaseRepository insuranceCaseRepository;
    private final RestService restService;

    @Value("${rdw.request.url}")
    private String rdwRequestUrl;

    /**
     * The inital request to the RDW to determine the license plate holder
     * This is an async request
     * <p>
     * The answer will come by a Temporal Signal
     */
    public void sendDriverRequestToRdw(final InsuranceCaseDto insuranceCaseDto, String workflowId) {
        Optional<InsuranceCase> insuranceCase = insuranceCaseRepository.findByInsuranceCaseNumber(insuranceCaseDto.insuranceNumber());

        if (insuranceCase.isEmpty()) {
            throw new UnknownInsuranceCaseError("Insurance Case not found!");
        }
        if (doesPass("RDWTechnicalError", insuranceCaseDto.rdwTechnicalErrorOdds())) {
            throw new TechnicalRdwError("RDW not reachable!");
        }
        RdwRequest requestDto = new RdwRequest(insuranceCase.get().getLicensePlate(), insuranceCaseDto.rdwOdds(), workflowId);
        restService.sendPostRequest(rdwRequestUrl, requestDto);
    }
}