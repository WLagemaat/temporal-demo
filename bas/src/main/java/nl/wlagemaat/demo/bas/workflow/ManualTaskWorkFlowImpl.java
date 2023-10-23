package nl.wlagemaat.demo.bas.workflow;

import nl.wlagemaat.demo.bas.model.FineDto;
import nl.wlagemaat.demo.bas.model.TaskProcessingResult;
import nl.wlagemaat.demo.bas.workflow.manualtasks.activity.CorrectDataActivity;

import static nl.wlagemaat.demo.bas.workflow.TemporalService.defaultRetryOptions;
import static nl.wlagemaat.demo.bas.workflow.TemporalService.getActivity;

public class ManualTaskWorkFlowImpl implements ManualTaskWorkFlow {

    private final CorrectDataActivity correctDataActivity = getActivity(CorrectDataActivity.class, defaultRetryOptions());

    @Override
    public TaskProcessingResult processTask(FineDto fine) {
        var result  = correctDataActivity.correctData(fine);
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return TaskProcessingResult.builder()
                .transgressionNumber(fine.transgressionNumber())
                .succeeded(result).build();
    }
}
