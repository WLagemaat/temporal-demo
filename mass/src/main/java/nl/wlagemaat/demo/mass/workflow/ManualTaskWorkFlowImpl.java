package nl.wlagemaat.demo.mass.workflow;

import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.mass.workflow.manualtasks.activity.CorrectDataActivity;
import nl.wlagemaat.demo.clients.ManualTaskWorkFlow;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;

import java.time.Duration;

import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.getActivity;

@Slf4j
public class ManualTaskWorkFlowImpl implements ManualTaskWorkFlow {

    private final CorrectDataActivity correctDataActivity = getActivity(CorrectDataActivity.class, defaultRetryOptions());

    @Override
    public TaskProcessingResult processTask(InsuranceCaseDto insuranceCaseDto) {
        log.info("Processing manual task for transgression: {}", insuranceCaseDto.insuranceNumber());
        var result  = correctDataActivity.correctData(insuranceCaseDto);
        Workflow.sleep(Duration.ofMillis(100_000));
        log.info("Finished manual task for transgression: {}", insuranceCaseDto.insuranceNumber());

        return TaskProcessingResult.builder()
                .insuranceCaseNumber(insuranceCaseDto.insuranceNumber())
                .value("Hulk")
                .succeeded(result).build();
    }
}
