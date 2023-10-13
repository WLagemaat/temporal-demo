package nl.wlagemaat.demo.bas.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.bas.workflow.manualtasks.activity.ManualTaskActivityMarker;
import nl.wlagemaat.demo.bas.workflow.util.TemporalDataConverterHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemporalService implements InitializingBean {

    public static final String PRE_INTAKE_NAMESPACE = "PRE_INTAKE";
    public static final String MANUAL_TASK_QUEUE = "MANUAL_TASK_QUEUE";

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
                .setNamespace(PRE_INTAKE_NAMESPACE)
                .build();
        return WorkflowClient.newInstance(WorkflowServiceStubs.newServiceStubs(stubOptions), clientOptions);
    }

    public static <T> T getActivity(Class<T> activityInterface, RetryOptions.Builder retryOptions) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setStartToCloseTimeout(ofMinutes(10))
                .setRetryOptions(retryOptions.validateBuildWithDefaults())
                .build());
    }

    public static RetryOptions.Builder defaultRetryOptions() {
        return RetryOptions.newBuilder()
                .setInitialInterval(ofSeconds(10))
                .setBackoffCoefficient(1.2)
                .setMaximumInterval(ofMinutes(5))
                .setMaximumAttempts(0);
    }
}
