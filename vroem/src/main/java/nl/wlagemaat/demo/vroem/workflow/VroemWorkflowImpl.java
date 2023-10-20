package nl.wlagemaat.demo.vroem.workflow;

import io.temporal.activity.ActivityOptions;
import lombok.val;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.ValidatedFineDto;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.CheckRDWActivity;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.ValidateTransgressionActivity;

import java.time.Duration;

import static nl.wlagemaat.demo.vroem.workflow.TemporalService.defaultRetryOptions;
import static nl.wlagemaat.demo.vroem.workflow.TemporalService.getActivity;

public class VroemWorkflowImpl implements VroemWorkflow {

    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .build();

    private final ValidateTransgressionActivity validateTransgressionActivity = getActivity(ValidateTransgressionActivity.class, defaultRetryOptions());
    private final CheckRDWActivity checkRDWActivity = getActivity(CheckRDWActivity.class, defaultRetryOptions());


    @Override
    public void processTransgression(FineDto fine) {
        val result = validateTransgressionActivity.validateFine(fine);
        if(result.succeeded()){
            val validatedFine = ValidatedFineDto.builder()
                    .fineInput(fine)
                    .transgressionNumber(result.transgressionNumber())
                    .build();

            checkRDWActivity.determineLicenseplate(validatedFine);
        }

    }
}
