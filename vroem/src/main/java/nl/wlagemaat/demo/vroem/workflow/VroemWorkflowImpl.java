package nl.wlagemaat.demo.vroem.workflow;

import io.temporal.common.SearchAttributeKey;
import io.temporal.common.SearchAttributeUpdate;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import nl.wlagemaat.demo.clients.ManualTaskWorkFlow;
import nl.wlagemaat.demo.clients.VroemWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.options.ManualTaskFlowOptions;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.util.VroemUtilities;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.CheckRDWActivity;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.CreateTransgressionActivity;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.ValidateTransgressionActivity;

import static nl.wlagemaat.demo.vroem.workflow.TemporalService.defaultRetryOptions;
import static nl.wlagemaat.demo.vroem.workflow.TemporalService.getActivity;

public class VroemWorkflowImpl implements VroemWorkflow {

    private final ValidateTransgressionActivity validateTransgressionActivity = getActivity(ValidateTransgressionActivity.class, defaultRetryOptions());
    private final CreateTransgressionActivity createTransgressionActivity = getActivity(CreateTransgressionActivity.class, defaultRetryOptions());
    private final CheckRDWActivity checkRDWActivity = getActivity(CheckRDWActivity.class, defaultRetryOptions());
//    private final

    @Override
    public void processTransgression(FineDto fine) {
        Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("TransgressionState"), "init"));
        // validate
        var result = validateTransgressionActivity.validateFine(fine);
        if(!result.succeeded()) {
            return;
        }
        Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("TransgressionState"), "validated"));

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
            Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("TransgressionState"), "manual-task"));
            var taskFinished = taskResult.get();
            result = FineProcessingResult.builder()
                    .transgressionNumber(taskFinished.transgressionNumber())
                    .succeeded(taskFinished.succeeded())
                    .build();
        }

        // check if it should go to mulder or worm based on the fineDto value
        if(result.succeeded()) {
            if (fine.isMulder()) {
                // send to mulder
                Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("TransgressionState"), "to-mulder"));
                // send to mulder
            } else {
                // send to worm
                Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("TransgressionState"), "to-worm"));
                // send to worm
            }
        }


        if(!result.succeeded()) {
            Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("TransgressionState"), "failed unexpected"));
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
