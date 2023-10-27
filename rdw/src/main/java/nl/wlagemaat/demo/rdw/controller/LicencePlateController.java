package nl.wlagemaat.demo.rdw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class LicencePlateController {

    Logger log = LoggerFactory.getLogger(LicencePlateController.class);

    @Value("${callback.url}")
    private String callbackUrl;

    @PostMapping(value = "/licenseplate-holder", consumes = "application/json", produces = "application/json")
    public ResponseEntity createPerson(@RequestBody LicensePlateRequest licensePlate) {
        // Async action
        determineDriverAsync(licensePlate);
        // let the requester know we recieved the request and processed it
        return ResponseEntity.accepted().build();
    }

    private void determineDriverAsync(LicensePlateRequest request) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);

                if(doesPass(request.odds())){
                    sendResponse("Spiderman", request.workflowId());
                } else {
                    sendResponse("Unknown", request.workflowId());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "RDW-Determine-Holder").start();
    }

    private void sendResponse(String driver, String workflowId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"driver\": \""+driver+"\", \"workflowId\": \""+workflowId+"\"}",headers);
        log.info("Sending response to {}: {}",callbackUrl, request.getBody());

        restTemplate.postForEntity(callbackUrl, request, String.class);
    }

    private boolean doesPass(Integer odds){
        int dice = ThreadLocalRandom.current().nextInt(100);
        return dice <= odds;
    }
}
