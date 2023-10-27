package nl.wlagemaat.demo.vroem.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowStub;
import lombok.RequiredArgsConstructor;
import lombok.val;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.model.RdwResponseSignal;
import nl.wlagemaat.demo.vroem.workflow.TemporalService;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FineIntakeWorkflowService {

    private final TemporalService temporalService;

    @WithSpan
    public void intake(@Nonnull FineDto transgression) {
        val workFlowClient = temporalService.runnableParentWorkFlow();
        WorkflowClient.start(workFlowClient::processTransgression, transgression);
    }

    @WithSpan
    public void rdwSignal(@Nonnull RdwResponseSignal signal) {
        val workFlowClient = temporalService.getWorkflowClient();
        WorkflowExecution workflowExecution =
                WorkflowExecution.newBuilder().setWorkflowId(signal.workflowId()).build();
        WorkflowStub workflow = workFlowClient.newUntypedWorkflowStub(workflowExecution, Optional.empty());
        workflow.signal("driverByRDWSignal", signal.driver());
    }
}
