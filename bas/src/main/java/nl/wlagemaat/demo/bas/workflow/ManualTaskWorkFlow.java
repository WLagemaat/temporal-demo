package nl.wlagemaat.demo.bas.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import nl.wlagemaat.demo.clients.model.FineDto;
import nl.wlagemaat.demo.clients.model.TaskProcessingResult;

@WorkflowInterface
public interface ManualTaskWorkFlow {

    @WorkflowMethod
    TaskProcessingResult processTask(FineDto fine);
}
