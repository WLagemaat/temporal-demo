package nl.wlagemaat.demo.bas.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.bas.workflow.manualtasks.activity.ManualTaskActivityMarker;
import nl.wlagemaat.demo.commons.temporal.util.TemporalDataConverterHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static nl.wlagemaat.demo.clients.ManualTaskWorkFlow.MANUAL_TASK_QUEUE;
import static nl.wlagemaat.demo.clients.ManualTaskWorkFlow.NAMESPACE_MANUAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemporalService implements InitializingBean {

    private final List<ManualTaskActivityMarker> manualTaskActivityImplementations;

    @Value("${app.temporal.host}")
    private String temporalHost;

    /**
     * Start to listen to Workflow tasks after all beans are set
     */
    @Override
    public void afterPropertiesSet() {

        WorkerFactory factory = WorkerFactory.newInstance(getWorkflowClient());

        // Specify the name of the Task Queue that this Worker should poll
        Worker worker = factory.newWorker(MANUAL_TASK_QUEUE);

        // Specify which Workflow implementations this Worker will support
        worker.registerWorkflowImplementationTypes(ManualTaskWorkFlowImpl.class);
        worker.registerActivitiesImplementations(manualTaskActivityImplementations.toArray());

        // Begin running the Worker
        factory.start();

        log.info("{} started for task queue: {}", worker.getClass().getName(), MANUAL_TASK_QUEUE);
    }

    private WorkflowClient getWorkflowClient() {
        var stubOptions = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalHost)
                .build();
        var clientOptions = WorkflowClientOptions.newBuilder()
                .setDataConverter(TemporalDataConverterHelper.createOmniscientJsonDataConverter())
                .setNamespace(NAMESPACE_MANUAL)
                .build();
        return WorkflowClient.newInstance(WorkflowServiceStubs.newServiceStubs(stubOptions), clientOptions);
    }
}
