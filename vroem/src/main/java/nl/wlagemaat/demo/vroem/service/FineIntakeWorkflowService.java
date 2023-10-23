package nl.wlagemaat.demo.vroem.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.temporal.client.WorkflowClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.workflow.TemporalService;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
public class FineIntakeWorkflowService {

    private final TemporalService temporalService;

    @WithSpan
    public void intake(@Nonnull FineDto transgression) {
        val workFlowClient = temporalService.runnableParentWorkFlow();
        WorkflowClient.start(workFlowClient::processTransgression, transgression);
    }


}
