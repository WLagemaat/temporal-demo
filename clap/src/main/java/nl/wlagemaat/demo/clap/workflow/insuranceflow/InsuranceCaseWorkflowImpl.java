package nl.wlagemaat.demo.clap.workflow.insuranceflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import nl.wlagemaat.demo.clients.DetermineDriverWorkflow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.InsuranceCaseWorkflow;
import nl.wlagemaat.demo.clients.activity.SendToExecutionActivity;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.InsuranceCaseResult;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.InsuranceCaseValidationEnrichmentResult;

import static nl.wlagemaat.demo.clients.DetermineDriverWorkflow.*;
import static nl.wlagemaat.demo.clients.IntakeWorkflow.CLAP_TASK_QUEUE;
import static nl.wlagemaat.demo.clients.model.Department.AUDITING_CASE_EXPERTS;
import static nl.wlagemaat.demo.clients.model.Department.INSTANT_PAYOUT;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.getActivity;

public class InsuranceCaseWorkflowImpl implements InsuranceCaseWorkflow {

    private final SendToExecutionActivity sendToExecutionActivity = getActivity(SendToExecutionActivity.class, defaultRetryOptions());

    /**
     * This workflow mainly controls child workflows which can be distributed among teams and applications
     * @param insuranceCaseDto the actual input
     */
    @Override
    public InsuranceCaseResult processInsuranceCase(InsuranceCaseDto insuranceCaseDto) {

        //create a childflow for CLAP, which CLAP-Worker will process
        IntakeWorkflow intakeWorkflow = Workflow.newChildWorkflowStub(IntakeWorkflow.class, ChildWorkflowOptions.newBuilder()
                .setWorkflowId("INTAKE-"+ extractWorkflowIdSuffix())
                .setTaskQueue(CLAP_TASK_QUEUE).build());
        Promise<InsuranceCaseValidationEnrichmentResult> validationEnrichmentResult = Async.function(intakeWorkflow::insuranceCaseIntake, insuranceCaseDto);
        var validationResult = validationEnrichmentResult.get();

        if (!validationResult.isValid()) {
            // return to the caller the error message in case of validation error
            return InsuranceCaseResult.builder()
                    .insuranceCaseNumber(validationResult.insuranceCaseNumber())
                    .errorMessage("Failed with reason:" + validationResult.errorMessage())
                    .build();
        }
        InsuranceCaseResult.InsuranceCaseResultBuilder insuranceCaseResult = InsuranceCaseResult.builder()
                .insuranceCaseNumber(validationResult.insuranceCaseNumber());

        if(validationResult.isMinorSeverity()){
            // INSTANT_PAYOUT
            var insuranceCase = InsuranceCaseDto.builder()
                    .insuranceNumber(validationResult.insuranceCaseNumber())
                    .instantPayoutTechnicalErrorOdds(insuranceCaseDto.instantPayoutTechnicalErrorOdds())
                    .build();
            sendToExecutionActivity.send(insuranceCase, Workflow.getInfo().getWorkflowId());
            insuranceCaseResult.department(INSTANT_PAYOUT);
        } else {
            // EXPERT_FLOW

            // Determine Owner Goods
            DetermineDriverWorkflow determineDriverWorkflow = Workflow.newChildWorkflowStub(DetermineDriverWorkflow.class, ChildWorkflowOptions.newBuilder()
                    .setWorkflowId("DRIVER-"+ extractWorkflowIdSuffix())
                    .setNamespace(NAMESPACE_MANUAL)
                    .setTaskQueue(DRIVER_TASK_QUEUE).build());
            Promise<TaskProcessingResult> taskProcessingResultPromise = Async.function(determineDriverWorkflow::processInsuranceCase, insuranceCaseDto);
            taskProcessingResultPromise.get();

            // hand over to the experts department
            var insuranceCase = InsuranceCaseDto.builder()
                    .insuranceNumber(validationResult.insuranceCaseNumber())
                    .instantPayoutTechnicalErrorOdds(insuranceCaseDto.instantPayoutTechnicalErrorOdds())
                    .build();
            sendToExecutionActivity.send(insuranceCase, Workflow.getInfo().getWorkflowId());
            insuranceCaseResult.department(AUDITING_CASE_EXPERTS);
        }
        insuranceCaseResult.isProcessed(true);
        return insuranceCaseResult.build();
    }

    /**
     * This method is used to extract the workflow id suffix from the workflow id
     * This to create a visible correlation between parent and child flows
     *
     * @return the workflow id suffix
     */
    private String extractWorkflowIdSuffix() {
        return Workflow.getInfo().getWorkflowId().replaceAll("INSURANCE_CASE-","");
    }
}
