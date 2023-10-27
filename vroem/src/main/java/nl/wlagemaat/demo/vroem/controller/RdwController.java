package nl.wlagemaat.demo.vroem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.vroem.model.RdwResponseSignal;
import nl.wlagemaat.demo.vroem.service.FineIntakeWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RdwController {
    private final FineIntakeWorkflowService fineIntakeWorkflowService;

    @PostMapping(value = "/licenseplate-holder-response", consumes = "application/json", produces = "application/json")
    public ResponseEntity createPerson(@RequestBody RdwResponseSignal driver) {
        // Async action
        log.info("Received response to determine driver for license plate: {}", driver);

        // fire signal to workflow
        fineIntakeWorkflowService.rdwSignal(driver);

        return ResponseEntity.accepted().build();
    }

}
