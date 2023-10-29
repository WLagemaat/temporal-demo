package nl.wlagemaat.demo.clap.controller;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.service.CaseIntakeWorkflowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IntakeController {

    private final CaseIntakeWorkflowService caseIntakeWorkflowService;

     @PostMapping("/insurance-intake")
     public void startInsuranceCase(@RequestBody InsuranceCaseDto insuranceCaseDto) {
         caseIntakeWorkflowService.intake(insuranceCaseDto);
     }

}
