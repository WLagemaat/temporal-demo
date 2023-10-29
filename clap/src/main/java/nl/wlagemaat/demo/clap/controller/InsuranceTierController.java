package nl.wlagemaat.demo.clap.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.model.signal.IotResponseSignal;
import nl.wlagemaat.demo.clap.service.CaseIntakeWorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InsuranceTierController {

    private final CaseIntakeWorkflowService caseIntakeWorkflowService;

    @PostMapping(value = "/insurance-tier-response", consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpStatus> createPerson(@RequestBody IotResponseSignal tier) {
        // Async action
        log.info("Received response to determine driver insurance tier: {}", tier);

        // fire signal to workflow
        caseIntakeWorkflowService.iotSignal(tier);

        return ResponseEntity.accepted().build();
    }

}
