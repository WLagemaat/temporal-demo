package nl.wlagemaat.demo.vroem.service.rdw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.exception.TechnicalRdwError;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
//import nl.wlagemaat.demo.vroem.mq.MQClient;
import nl.wlagemaat.demo.vroem.repository.TransgressionRepository;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.generateLicensePlate;
import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;

@Service
@RequiredArgsConstructor
@Slf4j
public class RdwService {

    private final TransgressionRepository transgressionRepository;

    @Value("${rdw.request.url}")
    private String rdwRequestUrl;

    /**
     * The inital request to the RDW to determine the license plate holder
     * This is an async request
     *
     * The answer will come by a Temporal Signal
     *
     * @param fineDto
     */
    public void sendDeterminationRequestToRdw(final FineDto fineDto, String workflowId){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(fineDto.transgressionNumber());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(
                "{\"licensePlate\": \""+transgression.getLicensePlate()+"\", \"odds\": "+fineDto.rdwOdds()+", \"workflowId\": \""+workflowId+"\"}",
                headers);
        log.info("Sending RDW request to {}: {}",rdwRequestUrl, request.getBody());

        restTemplate.postForEntity(rdwRequestUrl, request, String.class);
    }


    public FineProcessingResult determineDriver(final FineDto fineDto){
        var resultaat = FineProcessingResult.builder().transgressionNumber(fineDto.transgressionNumber());
        if(doesPass("autoLicensePlateDetermined", fineDto.rdwOdds())){
            resultaat.succeeded(true);
            saveLicensePlate(fineDto);
        } else {
            resultaat.isManualTask(true);
            resultaat.succeeded(true);
        }
        if(doesPass("RDWTechnicalError", fineDto.rdwTechnicalErrorOdds())){
            throw new TechnicalRdwError("RDW not reachable!");
        }
        return resultaat.build();
    }

    private void saveLicensePlate(final FineDto fineDto){
        Transgression transgression = transgressionRepository.findByTransgressionNumber(fineDto.transgressionNumber());
        if(transgression == null){
            throw new IllegalArgumentException("Transgression not found");
        }

        transgressionRepository.save(transgression);
    }
}