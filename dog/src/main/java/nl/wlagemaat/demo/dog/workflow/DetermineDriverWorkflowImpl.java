package nl.wlagemaat.demo.dog.workflow;

import nl.wlagemaat.demo.clients.DetermineDriverWorkflow;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.dog.workflow.activity.DetermineDriverActivity;

import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.getActivity;

public class DetermineDriverWorkflowImpl implements DetermineDriverWorkflow {

    private final DetermineDriverActivity determineDriverActivity = getActivity(DetermineDriverActivity.class, defaultRetryOptions());

    @Override
    public TaskProcessingResult processInsuranceCase(InsuranceCaseDto insuranceCaseDto) {
        determineDriverActivity.createDeterminationTask(insuranceCaseDto);
        return TaskProcessingResult.builder().insuranceCaseNumber(insuranceCaseDto.insuranceNumber()).succeeded(true).build();

    }
}
