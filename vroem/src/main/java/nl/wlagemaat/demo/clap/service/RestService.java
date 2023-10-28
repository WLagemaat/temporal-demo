package nl.wlagemaat.demo.clap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.exception.InvalidRequestError;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendRequestForRDWDriver(String url, Object requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        try {
            String jsonStr = mapper.writeValueAsString(requestDto);
            HttpEntity<String> request = new HttpEntity<>(jsonStr, headers);
            log.info("Sending request to {}: {}",url, request.getBody());
            restTemplate.postForEntity(url, request, String.class);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestError("Invalid json request object",e);
        }

    }

}
