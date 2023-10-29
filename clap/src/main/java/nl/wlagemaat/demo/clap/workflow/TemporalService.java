package nl.wlagemaat.demo.clap.workflow;

import com.uber.m3.tally.RootScopeBuilder;
import com.uber.m3.tally.Scope;
import com.uber.m3.tally.StatsReporter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.common.reporter.MicrometerClientStatsReporter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.wlagemaat.demo.clients.InsuranceCaseWorkflow;
import nl.wlagemaat.demo.commons.temporal.util.TemporalDataConverterHelper;
import nl.wlagemaat.demo.clap.workflow.intakeflow.IntakeWorkflowImpl;
import nl.wlagemaat.demo.clap.workflow.intakeflow.activity.IntakeActivityMarker;
import nl.wlagemaat.demo.clap.workflow.insuranceflow.InsuranceCaseWorkflowImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nl.wlagemaat.demo.clients.IntakeWorkflow.CLAP_TASK_QUEUE;
import static nl.wlagemaat.demo.clients.IntakeWorkflow.PRE_INTAKE_NAMESPACE;
import static nl.wlagemaat.demo.clients.InsuranceCaseWorkflow.INSURANCE_CASE_TASK_QUEUE;

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

    @Value("${app.temporal.host}")
    private String temporalHost;

    /**
     * Start to listen to Workflow tasks after all beans are set and create 1 worker that wil listen to 2 flows
     */
    @Override
    public void afterPropertiesSet() {

        // Worker #1 - main Insurance Case workflow
        createInsuranceCaseWorkFlowWorker();
        // Worker #2 - Intake workflow
        createIntakeWorkFlowWorker();
    }

    private void createInsuranceCaseWorkFlowWorker() {
        WorkerFactory factory = WorkerFactory.newInstance(getWorkflowClient());

        // Worker #1
        // Specify the name of the Task Queue that this Worker should poll
        Worker insuranceCaseWorker = factory.newWorker(INSURANCE_CASE_TASK_QUEUE);

        // Specify which Workflow implementations this Worker will support
        insuranceCaseWorker.registerWorkflowImplementationTypes(InsuranceCaseWorkflowImpl.class);

        // Begin running the Workers
        factory.start();

        log.info("{} started for task queue: {}", insuranceCaseWorker.getClass().getName(), INSURANCE_CASE_TASK_QUEUE);
    }

    private void createIntakeWorkFlowWorker() {
        WorkerFactory factory = WorkerFactory.newInstance(getWorkflowClient());

        // Worker #2
        // Specify the name of the Task Queue that this Worker should poll
        Worker insuranceCaseWorker = factory.newWorker(CLAP_TASK_QUEUE);

        // Specify which Workflow implementations this Worker will support
        insuranceCaseWorker.registerWorkflowImplementationTypes(IntakeWorkflowImpl.class);
        insuranceCaseWorker.registerActivitiesImplementations(intakeActivityImplementations.toArray());

        // Begin running the Workers
        factory.start();

        log.info("{} started for task queue: {}", insuranceCaseWorker.getClass().getName(), CLAP_TASK_QUEUE);
    }


    /**
     * A runnable workflow that will start the actual intake flow
     * @return a runnable workflow
     */
    public InsuranceCaseWorkflow runnableParentWorkFlow() {
        return getWorkflowClient()
                .newWorkflowStub(InsuranceCaseWorkflow.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(INSURANCE_CASE_TASK_QUEUE)
                        .setWorkflowId("INSURANCE_CASE-"+ UUID.randomUUID())
                        .build());
    }

    /**
     * get the 'current' workflow that is running and give it a signal
     */
    public void runningIntakeFlow(String workflowId, String signalName, Object signalValue) {
        WorkflowExecution workflowExecution = WorkflowExecution.newBuilder().setWorkflowId(workflowId).build();
        WorkflowStub workflow = getWorkflowClient().newUntypedWorkflowStub(workflowExecution, Optional.of("IntakeWorkflow-RDW"));
        workflow.signal(signalName, signalValue);
    }

    /**
     * Create a workflow client that will be used to start the workflow
     * <p>
     * Potential boilerplate code that could go to commons
     * <p>
     * @return a workflow client
     */
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
}