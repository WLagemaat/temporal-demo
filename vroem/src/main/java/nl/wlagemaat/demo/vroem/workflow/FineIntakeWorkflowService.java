package nl.wlagemaat.demo.vroem.workflow;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nl.wlagemaat.demo.vroem.model.FineDto;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
public class FineIntakeWorkflowService {

    private final TemporalService temporalService;

    public void intake(@Nonnull FineDto transgression) {
        val workFlowClient = temporalService.runnableVroemWorkFlow();
        temporalService.startVroemWorkflow(workFlowClient, transgression);
    }
}
