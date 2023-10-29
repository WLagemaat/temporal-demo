package nl.wlagemaat.demo.commons.temporal.util.options;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static nl.wlagemaat.demo.clients.ManualTaskWorkFlow.MANUAL_TASK_QUEUE;
import static nl.wlagemaat.demo.clients.ManualTaskWorkFlow.NAMESPACE_MANUAL;

public class FlowOptions {

    public static ChildWorkflowOptions getOptions() {
        return ChildWorkflowOptions.newBuilder()
                .setTaskQueue(MANUAL_TASK_QUEUE)
                .setNamespace(NAMESPACE_MANUAL)
                .build();
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
