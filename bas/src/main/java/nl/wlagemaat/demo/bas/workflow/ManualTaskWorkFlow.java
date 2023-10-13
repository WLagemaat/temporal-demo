package nl.wlagemaat.demo.bas.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.bas.model.FineDto;
import nl.wlagemaat.demo.bas.model.TaskProcessingResult;

@WorkflowInterface
public interface ManualTaskWorkFlow {

    @WorkflowMethod
    TaskProcessingResult processTask(FineDto fine);
}
