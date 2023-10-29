package nl.wlagemaat.demo.clap.service;

import io.temporal.client.WorkflowClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import nl.wlagemaat.demo.clap.model.signal.IotResponseSignal;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clap.model.signal.RdwResponseSignal;
import nl.wlagemaat.demo.clap.workflow.TemporalService;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
public class CaseIntakeWorkflowService {

    private final TemporalService temporalService;

    /**
     * Start a new workflow with the given insurance case
     * @param insuranceCaseDto
     */
    public void intake(@Nonnull InsuranceCaseDto insuranceCaseDto) {
        val workFlowClient = temporalService.runnableParentWorkFlow();
        WorkflowClient.start(workFlowClient::processInsuranceCase, insuranceCaseDto);
    }

    /**
     * async input to continue to flow
     * @param signal
     */
    public void rdwSignal(@Nonnull RdwResponseSignal signal) {
        temporalService.runningIntakeFlow(signal.workflowId(), "driverByRDWSignal", signal.driver());
    }

    /**
     * async input to continue to flow
     * @param signal
     */
    public void iotSignal(@Nonnull IotResponseSignal signal) {
        temporalService.runningIntakeFlow(signal.workflowId(), "tierLevelByIOTSignal", signal.tier());
    }
}
