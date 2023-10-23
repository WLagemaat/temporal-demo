package nl.wlagemaat.demo.vroem.workflow;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.model.TaskProcessingResult;
import nl.wlagemaat.demo.vroem.util.VroemUtilities;
import nl.wlagemaat.demo.vroem.workflow.externalflows.ManualTaskFlowOptions;
import nl.wlagemaat.demo.vroem.workflow.externalflows.ManualTaskWorkFlow;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.CheckRDWActivity;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.CreateTransgressionActivity;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.ValidateTransgressionActivity;

import static nl.wlagemaat.demo.vroem.workflow.TemporalService.defaultRetryOptions;
import static nl.wlagemaat.demo.vroem.workflow.TemporalService.getActivity;

@Slf4j
public class VroemWorkflowImpl implements VroemWorkflow {

    private final ValidateTransgressionActivity validateTransgressionActivity = getActivity(ValidateTransgressionActivity.class, defaultRetryOptions());
    private final CreateTransgressionActivity createTransgressionActivity = getActivity(CreateTransgressionActivity.class, defaultRetryOptions());
    private final CheckRDWActivity checkRDWActivity = getActivity(CheckRDWActivity.class, defaultRetryOptions());


    @Override
    public void processTransgression(FineDto fine) {
        // validate
        var result = validateTransgressionActivity.validateFine(fine);
        if(!result.succeeded()) {
            return;
        }
        String transgressionNumber = Workflow.sideEffect(String.class, VroemUtilities::generateTransgressionNumber);
        FineDto enrichedFine = getCreatedTransgression(fine, transgressionNumber);
        // store
        createTransgressionActivity.createTransgression(enrichedFine);
        // check rdw
        result = checkRDWActivity.determineLicenseplate(enrichedFine);

        // check if manual task needs to be created based on the outcome of the rdw check
        if(result.isManualTask()){
            ManualTaskWorkFlow manualTaskWorkFlow = Workflow.newChildWorkflowStub(ManualTaskWorkFlow.class, ManualTaskFlowOptions.getOptions());
            Promise<TaskProcessingResult> taskResult = Async.function(manualTaskWorkFlow::processTask, enrichedFine);
            TaskProcessingResult taskProcessResult = taskResult.get();
            log.info(" Manual Task for {} did succeed:{}",taskProcessResult.transgressionNumber(), taskProcessResult.succeeded());
        }


    }

    private static FineDto getCreatedTransgression(FineDto fine, String transgressionNumber) {
        return FineDto.builder()
                .transgressionNumber(transgressionNumber)
                .isMulder(fine.isMulder())
                .party(fine.party())
                .validOdds(fine.validOdds())
                .cvomTechnicalErrorOdds(fine.cvomTechnicalErrorOdds())
                .mulderTechnicalErrorOdds(fine.mulderTechnicalErrorOdds())
                .wormTechnicalErrorOdds(fine.wormTechnicalErrorOdds())
                .svenTechnicalErrorOdds(fine.svenTechnicalErrorOdds())
                .rdwTechnicalErrorOdds(fine.rdwTechnicalErrorOdds())
                .rdwOdds(fine.rdwOdds())
                .build();
    }
}
