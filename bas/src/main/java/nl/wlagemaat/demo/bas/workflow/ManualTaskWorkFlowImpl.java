package nl.wlagemaat.demo.bas.workflow;

import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.bas.workflow.manualtasks.activity.CorrectDataActivity;
import nl.wlagemaat.demo.clients.ManualTaskWorkFlow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;

import java.time.Duration;

import static nl.wlagemaat.demo.clients.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.clients.options.FlowOptions.getActivity;

@Slf4j
public class ManualTaskWorkFlowImpl implements ManualTaskWorkFlow {

    private final CorrectDataActivity correctDataActivity = getActivity(CorrectDataActivity.class, defaultRetryOptions());

    @Override
    public TaskProcessingResult processTask(FineDto fine) {
        log.info("Processing manual task for transgression: {}", fine.transgressionNumber());
        var result  = correctDataActivity.correctData(fine);
        Workflow.sleep(Duration.ofMillis(100_000));
        log.info("Finished manual task for transgression: {}", fine.transgressionNumber());

        return TaskProcessingResult.builder()
                .transgressionNumber(fine.transgressionNumber())
                .succeeded(result).build();
    }
}
