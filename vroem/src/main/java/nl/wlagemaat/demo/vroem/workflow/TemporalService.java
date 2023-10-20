package nl.wlagemaat.demo.vroem.workflow;

import com.uber.m3.tally.RootScopeBuilder;
import com.uber.m3.tally.Scope;
import com.uber.m3.tally.StatsReporter;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.common.reporter.MicrometerClientStatsReporter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.workflow.util.TemporalDataConverterHelper;
import nl.wlagemaat.demo.vroem.workflow.vroemflow.activity.VroemActivityMarker;
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
    public static final String PRE_INTAKE_QUEUE = "PRE_INTAKE_QUEUE";
    public static final String VROEM_TASK_QUEUE = "VROEM_TASK_QUEUE";

    private final List<VroemActivityMarker> vroemActivityImplementations;

    @Value("${app.temporal.host}")
    private String temporalHost;

    /**
     * Start to listen to Workflow tasks after all beans are set
     */
    @Override
    public void afterPropertiesSet() {

        WorkerFactory factory = WorkerFactory.newInstance(getWorkflowClient());

        // Specify the name of the Task Queue that this Worker should poll
        Worker worker = factory.newWorker(VROEM_TASK_QUEUE);

        // Specify which Workflow implementations this Worker will support
        worker.registerWorkflowImplementationTypes(VroemWorkflowImpl.class);
        worker.registerActivitiesImplementations(vroemActivityImplementations.toArray());

        // Begin running the Worker
        factory.start();

        log.info("{} started for task queue: {}", worker.getClass().getName(), VROEM_TASK_QUEUE);
    }

    private WorkflowClient getWorkflowClient() {
       // PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
       // StatsReporter reporter = new MicrometerClientStatsReporter(registry);
        // set up a new scope, report every 10 seconds
       // Scope scope = new RootScopeBuilder()
       //         .reporter(reporter)
       //         .reportEvery(com.uber.m3.util.Duration.ofSeconds(10));

        var stubOptions = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalHost)
       //         .setMetricsScope(scope)
                .build();
        var clientOptions = WorkflowClientOptions.newBuilder()
        //        .setInterceptors(new OpenTracingClientInterceptor())
                .setDataConverter(TemporalDataConverterHelper.createOmniscientJsonDataConverter())
                .setNamespace(PRE_INTAKE_NAMESPACE)
                .build();



        return WorkflowClient.newInstance(WorkflowServiceStubs.newServiceStubs(stubOptions), clientOptions);
    }

    public VroemWorkflow runnableVroemWorkFlow() {
        return getWorkflowClient()
                .newWorkflowStub(VroemWorkflow.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(VROEM_TASK_QUEUE)
                        .setWorkflowId("PRE_INTAKE-"+ UUID.randomUUID())
//                        .setRetryOptions() <-- for the whole flow, not an activity
                        .build());
    }

    public void startVroemWorkflow(VroemWorkflow workflow, FineDto fineDto) {
        log.info("{} started async flow ", workflow.getClass().getName());
        WorkflowClient.start(workflow::processTransgression, fineDto);
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
