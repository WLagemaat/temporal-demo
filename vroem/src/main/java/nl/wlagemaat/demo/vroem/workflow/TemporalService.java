package nl.wlagemaat.demo.vroem.workflow;

import com.uber.m3.tally.RootScopeBuilder;
import com.uber.m3.tally.Scope;
import com.uber.m3.tally.StatsReporter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
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
import nl.wlagemaat.demo.clients.IntakeWorkflow;
import nl.wlagemaat.demo.clients.TransgressionWorkflow;
import nl.wlagemaat.demo.commons.temporal.util.TemporalDataConverterHelper;
import nl.wlagemaat.demo.vroem.workflow.intakeflow.IntakeWorkflowImpl;
import nl.wlagemaat.demo.vroem.workflow.intakeflow.activity.IntakeActivityMarker;
import nl.wlagemaat.demo.vroem.workflow.transgressionflow.TransgressionWorkflowImpl;
import nl.wlagemaat.demo.vroem.workflow.transgressionflow.activity.TransgressionActivityMarker;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static nl.wlagemaat.demo.clients.IntakeWorkflow.PRE_INTAKE_NAMESPACE;
import static nl.wlagemaat.demo.clients.TransgressionWorkflow.TRANSGRESSION_TASK_QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemporalService implements InitializingBean {

    /**
     * For Demo purposes all the applications have a similar TemporalService class
     * this class is responsible for starting the worker that listens to the workflow tasks
     * <p>
     * The worker will start a workflow when a task is received
     */

    private final List<IntakeActivityMarker> intakeActivityImplementations;
    private final List<TransgressionActivityMarker> transgressionActivityImplementations;

    @Value("${app.temporal.host}")
    private String temporalHost;

    /**
     * Start to listen to Workflow tasks after all beans are set and create 2 workers
     * worker 1: The parent flow that will start the intake flow -> vroem -> rdw etc etc
     * worker 2: the actual Vroem.part1 flow - validation and enrichment of the transgression
     */
    @Override
    public void afterPropertiesSet() {

        WorkerFactory factory = WorkerFactory.newInstance(getWorkflowClient());

        // Worker #1
        // Specify the name of the Task Queue that this Worker should poll
        Worker transgressionWorker = factory.newWorker(TRANSGRESSION_TASK_QUEUE);

        // Specify which Workflow implementations this Worker will support
        transgressionWorker.registerWorkflowImplementationTypes(TransgressionWorkflowImpl.class,
                                                                IntakeWorkflowImpl.class);
        transgressionWorker.registerActivitiesImplementations(transgressionActivityImplementations.toArray());
        transgressionWorker.registerActivitiesImplementations(intakeActivityImplementations.toArray());

        // Begin running the Workers
        factory.start();

        log.info("{} started for task queue: {}", transgressionWorker.getClass().getName(), TRANSGRESSION_TASK_QUEUE);
    }

    private WorkflowClient getWorkflowClient() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        StatsReporter reporter = new MicrometerClientStatsReporter(registry);

        Scope scope = new RootScopeBuilder()
                .reporter(reporter)
                .reportEvery(com.uber.m3.util.Duration.ofSeconds(10));

        var stubOptions = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalHost)
                .setMetricsScope(scope)
                .build();
        var clientOptions = WorkflowClientOptions.newBuilder()
                .setDataConverter(TemporalDataConverterHelper.createOmniscientJsonDataConverter())
                .setNamespace(PRE_INTAKE_NAMESPACE)
                .build();

        return WorkflowClient.newInstance(WorkflowServiceStubs.newServiceStubs(stubOptions), clientOptions);
    }

    /**
     * A startable workflow that will start the actual intake flow
     * @return a runnable workflow
     */
    public TransgressionWorkflow runnableParentWorkFlow() {
        return getWorkflowClient()
                .newWorkflowStub(TransgressionWorkflow.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(TRANSGRESSION_TASK_QUEUE)
                        .setWorkflowId("PRE_INTAKE-"+ UUID.randomUUID())
                        .build());
    }
}
