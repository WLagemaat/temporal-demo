package nl.wlagemaat.demo.clap.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.model.signal.RdwResponseSignal;
import nl.wlagemaat.demo.clap.service.CaseIntakeWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RdwController {
    private final CaseIntakeWorkflowService caseIntakeWorkflowService;

    @PostMapping(value = "/licenseplate-holder-response", consumes = "application/json", produces = "application/json")
    public ResponseEntity updateDriver(@RequestBody RdwResponseSignal driver) {
        // Async action
        log.info("Received response to determine driver for license plate: {}", driver);

        // fire signal to workflow
        caseIntakeWorkflowService.rdwSignal(driver);

        return ResponseEntity.accepted().build();
    }

}
