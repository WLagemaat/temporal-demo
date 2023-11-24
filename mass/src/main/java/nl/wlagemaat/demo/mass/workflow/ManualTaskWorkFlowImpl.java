package nl.wlagemaat.demo.mass.workflow;

import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.mass.workflow.manualtasks.activity.CreateTaskForManualFixActivity;
import nl.wlagemaat.demo.clients.ManualTaskWorkFlow;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.mass.workflow.manualtasks.activity.TaskFinishedActivity;
import nl.wlagemaat.demo.mass.workflow.manualtasks.activity.TaskInProgressActivity;

import java.time.Duration;

import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.getActivity;

@Slf4j
public class ManualTaskWorkFlowImpl implements ManualTaskWorkFlow {

    private final CreateTaskForManualFixActivity createTaskForManualFixActivity = getActivity(CreateTaskForManualFixActivity.class, defaultRetryOptions());
    private final TaskInProgressActivity taskInProgressActivity = getActivity(TaskInProgressActivity.class, defaultRetryOptions());
    private final TaskFinishedActivity taskFinishedActivity = getActivity(TaskFinishedActivity.class, defaultRetryOptions());

    @Override
    public TaskProcessingResult processTask(InsuranceCaseDto insuranceCaseDto) {
        log.info("Processing manual task for transgression: {}", insuranceCaseDto.insuranceNumber());
        createTaskForManualFixActivity.correctData(insuranceCaseDto);
        Workflow.sleep(Duration.ofMillis(2_000));
        taskInProgressActivity.taskInProgress(insuranceCaseDto);
        Workflow.sleep(Duration.ofMillis(4_000));
        var result = taskFinishedActivity.finishTask(insuranceCaseDto);
        Workflow.sleep(Duration.ofMillis(1_500));

        log.info("Finished manual task for transgression: {}", insuranceCaseDto.insuranceNumber());

        return TaskProcessingResult.builder()
                .insuranceCaseNumber(insuranceCaseDto.insuranceNumber())
                .value("Hulk")
                .succeeded(result).build();
    }
}
