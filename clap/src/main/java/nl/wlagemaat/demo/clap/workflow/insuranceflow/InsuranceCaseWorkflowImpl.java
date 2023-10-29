package nl.wlagemaat.demo.clap.workflow.insuranceflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import nl.wlagemaat.demo.clap.workflow.intakeflow.activity.ValidateInsuranceCaseActivity;
import nl.wlagemaat.demo.clients.DetermineDriverWorkflow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.InsuranceCaseWorkflow;
import nl.wlagemaat.demo.clients.activity.SendToExecutionActivity;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.InsuranceCaseValidationEnrichmentResult;

import java.util.UUID;

import static nl.wlagemaat.demo.clients.DetermineDriverWorkflow.MANUAL_TASK_QUEUE;
import static nl.wlagemaat.demo.clients.DetermineDriverWorkflow.NAMESPACE_MANUAL;
import static nl.wlagemaat.demo.clients.IntakeWorkflow.CLAP_TASK_QUEUE;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.getActivity;

public class InsuranceCaseWorkflowImpl implements InsuranceCaseWorkflow {

    private final SendToExecutionActivity sendToExecutionActivity = getActivity(SendToExecutionActivity.class, defaultRetryOptions());

    /**
     * This workflow mainly controls child workflows which can be distributed among teams and applications
     * @param insuranceCaseDto the actual input
     */
    @Override
    public void processInsuranceCase(InsuranceCaseDto insuranceCaseDto) {

        //create a childflow for CLAP, which CLAP-Worker will process
        IntakeWorkflow intakeWorkflow = Workflow.newChildWorkflowStub(IntakeWorkflow.class, ChildWorkflowOptions.newBuilder()
                .setWorkflowId("INTAKE-"+ UUID.randomUUID())
                .setTaskQueue(CLAP_TASK_QUEUE).build());
        Promise<InsuranceCaseValidationEnrichmentResult> validationEnrichmentResult = Async.function(intakeWorkflow::insuranceCaseIntake, insuranceCaseDto);
        var validationResult = validationEnrichmentResult.get();

        if (!validationResult.isValid()) {
            // return to the caller the error message in case of validation error
            // throw an exception in the weird case something else happened
            return;
        }

        if(validationResult.isMinorSeverity()){
            // go instant payount execution
            var insuranceCase = InsuranceCaseDto.builder().insuranceNumber(validationResult.insuranceCaseNumber()).build();
            sendToExecutionActivity.send(insuranceCase, Workflow.getInfo().getWorkflowId());
        } else {
            // go OM-Afdoening flow
            DetermineDriverWorkflow determineDriverWorkflow = Workflow.newChildWorkflowStub(DetermineDriverWorkflow.class, ChildWorkflowOptions.newBuilder()
                    .setWorkflowId("DRIVER-"+ UUID.randomUUID())
                    .setNamespace(NAMESPACE_MANUAL)
                    .setTaskQueue(MANUAL_TASK_QUEUE).build());
            Promise<TaskProcessingResult> taskProcessingResultPromise = Async.function(determineDriverWorkflow::processInsuranceCase, insuranceCaseDto);
            taskProcessingResultPromise.get();

        }

    }
}
