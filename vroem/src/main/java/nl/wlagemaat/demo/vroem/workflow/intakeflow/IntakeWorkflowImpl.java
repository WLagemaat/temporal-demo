package nl.wlagemaat.demo.vroem.workflow.intakeflow;

import io.temporal.common.SearchAttributeKey;
import io.temporal.common.SearchAttributeUpdate;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.ManualTaskWorkFlow;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.clients.model.TransgressionValidationEnrichmentResult;
import nl.wlagemaat.demo.clients.options.FlowOptions;
import nl.wlagemaat.demo.vroem.model.FineProcessingResult;
import nl.wlagemaat.demo.vroem.util.VroemUtilities;
import nl.wlagemaat.demo.vroem.workflow.intakeflow.activity.CheckRDWActivity;
import nl.wlagemaat.demo.vroem.workflow.intakeflow.activity.CreateTransgressionActivity;
import nl.wlagemaat.demo.vroem.workflow.intakeflow.activity.ValidateTransgressionActivity;

import java.util.Optional;

import static nl.wlagemaat.demo.clients.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.clients.options.FlowOptions.getActivity;

@Slf4j
public class IntakeWorkflowImpl implements IntakeWorkflow {

    private final ValidateTransgressionActivity validateTransgressionActivity = getActivity(ValidateTransgressionActivity.class, defaultRetryOptions());
    private final CreateTransgressionActivity createTransgressionActivity = getActivity(CreateTransgressionActivity.class, defaultRetryOptions());
    private final CheckRDWActivity checkRDWActivity = getActivity(CheckRDWActivity.class, defaultRetryOptions());

    private Optional<FineProcessingResult> rdwResult = Optional.empty();

    @Override
    public TransgressionValidationEnrichmentResult transgressionIntake(FineDto fine) {
        updateTransgressionState("init");
        // validate
        var validationResult = validateTransgressionActivity.validateFine(fine);
        if(!validationResult.succeeded()) {
            updateTransgressionState("rejected");
            return TransgressionValidationEnrichmentResult.builder()
                    .isValid(false)
                    .transgressionNumber(fine.transgressionNumber())
                    .errorMessage(validationResult.errorMessage())
                    .build();
        }
        updateTransgressionState("validated");

        String transgressionNumber = Workflow.sideEffect(String.class, VroemUtilities::generateTransgressionNumber);

        FineDto enrichedFine = getCreatedTransgression(fine, transgressionNumber);
        // store the initial transgression
        createTransgressionActivity.createTransgression(enrichedFine);
        // check rdw with the licenplate to determine the initial possible driver
        checkRDWActivity.determineDriver(enrichedFine, Workflow.getInfo().getWorkflowId());
        // wait for rdw result
        Workflow.await(() -> rdwResult.isPresent());

        var enrichmentResult = rdwResult.get();
        updateTransgressionState("driver enriched");

        // check if manual task needs to be created based on the outcome of the rdw check
        if(enrichmentResult.isManualTask()){
            ManualTaskWorkFlow manualTaskWorkFlow = Workflow.newChildWorkflowStub(ManualTaskWorkFlow.class, FlowOptions.getOptions());
            Promise<TaskProcessingResult> taskResult = Async.function(manualTaskWorkFlow::processTask, enrichedFine);
            updateTransgressionState("manual-task-created");
            var taskFinished = taskResult.get();
            enrichmentResult = FineProcessingResult.builder()
                    .transgressionNumber(taskFinished.transgressionNumber())
                    .succeeded(taskFinished.succeeded())
                    .build();
        }

        // check if it should go to mulder or worm based on the fineDto value
        if(enrichmentResult.succeeded()) {
            updateTransgressionState("enriched");
            return TransgressionValidationEnrichmentResult.builder()
                    .isValid(true)
                    .transgressionNumber(fine.transgressionNumber())
                    .isMulder(fine.isMulder())
                    .build();
        } else {
            updateTransgressionState("failed unexpected");
            return TransgressionValidationEnrichmentResult.builder()
                    .isValid(false)
                    .transgressionNumber(fine.transgressionNumber())
                    .errorMessage("unexpected endstate")
                    .build();
        }
    }

    @Override
    public void driverByRDW(String driver) {
        log.info("Driver by RDW: {}", driver);
        var result = FineProcessingResult.builder().succeeded(true);
        if(driver.equalsIgnoreCase("unknown")){
            rdwResult = Optional.of(result.isManualTask(true).build());
        } else {
            rdwResult = Optional.of(result.isManualTask(false).value(driver).build());
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

    private void updateTransgressionState(String state) {
        log.info("Updating transgression state to: {}", state);
        Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("TransgressionState"), state));
    }
}
