package nl.wlagemaat.demo.iot.controller;

import com.google.gson.Gson;
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
public class InsuranceTierController {

    Logger log = LoggerFactory.getLogger(InsuranceTierController.class);

    @Value("${callback.url}")
    private String callbackUrl;

    @PostMapping(value = "/tier-level", consumes = "application/json", produces = "application/json")
    public ResponseEntity createPerson(@RequestBody InsuranceTierRequest iotRequest) {
        // Async action
        determineTierAsync(iotRequest);
        // let the requester know we recieved the request and processed it
        return ResponseEntity.accepted().build();
    }

    private void determineTierAsync(InsuranceTierRequest request) {
        new Thread(() -> {
            try {
                Thread.sleep(1500);

                sendResponse(new InsuranceTierResponse(tierLevel(), request.workflowId()));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "RDW-Determine-Holder").start();
    }

    private void sendResponse(InsuranceTierResponse response){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(response);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonStr, headers);
        log.info("Sending response to {}: {}",callbackUrl, request.getBody());

        restTemplate.postForEntity(callbackUrl, request, String.class);
    }

    private int tierLevel(){
        return ThreadLocalRandom.current().nextInt(100);
    }
}
