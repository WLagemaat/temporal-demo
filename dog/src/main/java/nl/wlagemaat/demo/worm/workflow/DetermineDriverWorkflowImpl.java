package nl.wlagemaat.demo.worm.workflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import nl.wlagemaat.demo.clients.DetermineDriverWorkflow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;
import nl.wlagemaat.demo.worm.workflow.activity.DetermineDriverActivity;

import static nl.wlagemaat.demo.clients.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.clients.options.FlowOptions.getActivity;

public class DetermineDriverWorkflowImpl implements DetermineDriverWorkflow {

    private final DetermineDriverActivity determineDriverActivity = getActivity(DetermineDriverActivity.class, defaultRetryOptions());

    @Override
    public TaskProcessingResult processTransgression(FineDto fine) {
        determineDriverActivity.createDeterminationTask(fine);
        return TaskProcessingResult.builder().transgressionNumber(fine.transgressionNumber()).succeeded(true).build();

    }
}
