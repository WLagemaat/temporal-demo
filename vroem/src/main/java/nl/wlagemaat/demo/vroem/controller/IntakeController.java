package nl.wlagemaat.demo.vroem.controller;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.workflow.FineIntakeWorkflowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IntakeController {

    private final FineIntakeWorkflowService fineIntakeWorkflowService;

     @PostMapping("/fine-intake")
     public void createFine(@RequestBody FineDto fineDto) {
         fineIntakeWorkflowService.intake(fineDto);
     }


}
