package nl.wlagemaat.demo.clap.workflow.intakeflow;

import io.temporal.common.SearchAttributeKey;
import io.temporal.common.SearchAttributeUpdate;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clap.model.CaseProcessingResult;
import nl.wlagemaat.demo.clap.util.VroemUtilities;
import nl.wlagemaat.demo.clap.workflow.intakeflow.activity.*;
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.ManualTaskWorkFlow;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;
import nl.wlagemaat.demo.clients.model.InsuranceCaseValidationEnrichmentResult;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;
import nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions;

import java.util.Optional;

import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.commons.temporal.util.options.FlowOptions.getActivity;

@Slf4j
public class IntakeWorkflowImpl implements IntakeWorkflow {

    private final ValidateInsuranceCaseActivity validateInsuranceCaseActivity = getActivity(ValidateInsuranceCaseActivity.class, defaultRetryOptions());
    private final CreateInsuranceCaseActivity createInsuranceCaseActivity = getActivity(CreateInsuranceCaseActivity.class, defaultRetryOptions());
    private final UpdateInsuranceCaseActivity updateInsuranceCaseActivity = getActivity(UpdateInsuranceCaseActivity.class, defaultRetryOptions());
    private final CheckRDWActivity checkRDWActivity = getActivity(CheckRDWActivity.class, defaultRetryOptions());
    private final CheckInsuranceTierActivity checkInsuranceTierActivity = getActivity(CheckInsuranceTierActivity.class, defaultRetryOptions());

    private Optional<CaseProcessingResult> rdwResult = Optional.empty();
    private Optional<Integer> insuranceTier = Optional.empty();

    @Override
    public InsuranceCaseValidationEnrichmentResult insuranceCaseIntake(InsuranceCaseDto insuranceCase) {
        updateInsuranceCaseState("init");
        // validate
        var validationResult = validateInsuranceCaseActivity.validateCase(insuranceCase);
        if (!validationResult.succeeded()) {
            updateInsuranceCaseState("rejected");
            return InsuranceCaseValidationEnrichmentResult.builder()
                    .isValid(false)
                    .insuranceCaseNumber(insuranceCase.insuranceNumber())
                    .errorMessage(validationResult.errorMessage())
                    .build();
        }
        updateInsuranceCaseState("validated");

        // create the insurance case number as a Marker/SideEffect.
        // if the workflow will restart from an earlier point, the same number will be re-used
        String insuranceCaseNumber = Workflow.sideEffect(String.class, VroemUtilities::generateInsuranceCaseNumber);

        InsuranceCaseDto enrichedCase = getCreatedInsuranceCase(insuranceCase, insuranceCaseNumber);
        // store the initial transgression
        createInsuranceCaseActivity.createInsuranceCase(enrichedCase);

        // check rdw with the licenplate to determine the initial possible driver
        checkRDWActivity.determineDriver(enrichedCase, Workflow.getInfo().getWorkflowId());
        // wait for rdw result
        Workflow.await(() -> rdwResult.isPresent());

        var enrichmentResult = rdwResult.get();

        if (enrichmentResult.isManualTask()) {
            enrichmentResult = createManualTask(enrichedCase);
        }

        updateInsuranceCaseActivity.updateDriverInsuranceCase(insuranceCaseNumber, enrichmentResult.value());
        updateInsuranceCaseState("initial-driver");

        checkInsuranceTierActivity.determineTier(enrichedCase, Workflow.getInfo().getWorkflowId());
        // wait for IOT result
        Workflow.await(() -> insuranceTier.isPresent());

        if (enrichmentResult.isManualTask()) {
            enrichmentResult = createManualTask(enrichedCase);
        }

        updateInsuranceCaseActivity.updateInsuranceTierInsuranceCase(insuranceCaseNumber, insuranceTier.get());
        updateInsuranceCaseState("tier-level");

        // check if the case is correctly enriched
        if (enrichmentResult.succeeded()) {
            updateInsuranceCaseState("enriched");
            return InsuranceCaseValidationEnrichmentResult.builder()
                    .isValid(true)
                    .insuranceCaseNumber(insuranceCase.insuranceNumber())
                    .isMinorSeverity(insuranceCase.isMinorSeverity())
                    .build();
        } else {
            updateInsuranceCaseState("failed unexpected");
            return InsuranceCaseValidationEnrichmentResult.builder()
                    .isValid(false)
                    .insuranceCaseNumber(insuranceCase.insuranceNumber())
                    .errorMessage("unexpected endstate")
                    .build();
        }
    }

    @Override
    public void driverByRDW(String driver) {
        log.info("Driver by RDW: {}", driver);
        var result = CaseProcessingResult.builder().succeeded(true);
        if (driver.equalsIgnoreCase("unknown")) {
            rdwResult = Optional.of(result.isManualTask(true).build());
        } else {
            rdwResult = Optional.of(result.isManualTask(false).value(driver).build());
        }
    }

    @Override
    public void insuranceTier(int tierLevel) {
        log.info("Insurance tier: {}", tierLevel);
        insuranceTier = Optional.of(tierLevel);
    }

    private static InsuranceCaseDto getCreatedInsuranceCase(InsuranceCaseDto insuranceCase, String insuranceCaseNumber) {
        return InsuranceCaseDto.builder()
                .insuranceNumber(insuranceCaseNumber)
                .isMinorSeverity(insuranceCase.isMinorSeverity())
                .validOdds(insuranceCase.validOdds())
                .auditorTechnicalErrorOdds(insuranceCase.auditorTechnicalErrorOdds())
                .instantPayoutTechnicalErrorOdds(insuranceCase.instantPayoutTechnicalErrorOdds())
                .dogTechnicalErrorOdds(insuranceCase.dogTechnicalErrorOdds())
                .catTechnicalErrorOdds(insuranceCase.catTechnicalErrorOdds())
                .rdwTechnicalErrorOdds(insuranceCase.rdwTechnicalErrorOdds())
                .rdwOdds(insuranceCase.rdwOdds())
                .build();
    }

    private CaseProcessingResult createManualTask(InsuranceCaseDto enrichedCase){
        // check if manual task needs to be created based on the outcome of the rdw check

        ManualTaskWorkFlow manualTaskWorkFlow = Workflow.newChildWorkflowStub(ManualTaskWorkFlow.class, FlowOptions.getOptions());
        Promise<TaskProcessingResult> taskResult = Async.function(manualTaskWorkFlow::processTask, enrichedCase);
        updateInsuranceCaseState("manual-task-created");
        var taskFinished = taskResult.get();
        return CaseProcessingResult.builder()
                .insuranceCaseNumber(taskFinished.insuranceCaseNumber())
                .succeeded(taskFinished.succeeded())
                .build();

    }
    private void updateInsuranceCaseState(String state) {
        log.info("Updating transgression state to: {}", state);
        Workflow.upsertTypedSearchAttributes(SearchAttributeUpdate.valueSet(SearchAttributeKey.forKeyword("InsuranceCaseState"), state));
    }
}
