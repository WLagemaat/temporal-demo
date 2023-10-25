package nl.wlagemaat.demo.vroem.workflow.transgressionflow;

import nl.wlagemaat.demo.clients.TransgressionWorkflow;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity.DetermineDriverActivity;
import nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity.ValidateAndEnrichActivity;

import static nl.wlagemaat.demo.clients.options.FlowOptions.defaultRetryOptions;
import static nl.wlagemaat.demo.clients.options.FlowOptions.getActivity;


public class TransgressionWorkflowImpl implements TransgressionWorkflow {

    private final ValidateAndEnrichActivity validateAndEnrichActivity = getActivity(ValidateAndEnrichActivity.class, defaultRetryOptions());
    private final DetermineDriverActivity determineDriverActivity = getActivity(DetermineDriverActivity.class, defaultRetryOptions());

    @Override
    public void processTransgression(FineDto fine) {

        // Call Vroem and create a childflow for Vroem
        var validationResult = validateAndEnrichActivity.validateAndEnrich(fine);

        if (!validationResult.isValid()) {
            // return to the caller the error message in case of validation error
            // throw an exception in the weird case something else happened
            return;
        }

        determineDriverActivity.determineDriver(fine);

        if(validationResult.isMulder()){
            // go mulder flow
        } else {
            // go OM-Afdoening flow
        }

    }
}
