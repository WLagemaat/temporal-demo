https://temporal.io/blog/workflow-engine-principles







![workflow-sequence-basic.png](images%2Fworkflow-sequence-basic.png)


![workflow-sequence-engine.png](images%2Fworkflow-sequence-engine.png)

```
If you run atop a workflow engine that doesn't have transactions across all these components, you will run into all sorts of race conditions:

if you update state, and put tasks in task queue, and the state update goes through, but the task queue update fails, your system ends up in a state where it thinks there is a task outstanding but the task is not.
if you put tasks in a task queue first and then update state, then If your update is slow, the task can be delivered and processed by the time that goes through so it will be inconsistent and need to deal with edge cases
If all these updates are transactional, then all these race conditions disappear. The system becomes simpler and application level code doesn't see all these edge cases and it simplifies the programming model.

This doesn't just apply to the workflow engine implementation. Most engineers don't use workflow-like orchestrators or workflow-engines to write their services. They use queues, databases and other data sources to create hodgepodge architectures with no transactions across them:


Our point about lack of transactions leading to race conditions applies to the majority of ad hoc systems built.

This is very important to understand: If you are building a system from these components you're guaranteed to have edge cases in race conditions. 
So that's why having a robust engine with something like Temporal's underlying architecture will simplify your life tremendously.
```

### Workflow management: 
Temporal.io offers a powerful workflow management framework that allows developers to orchestrate and manage complex workflows across microservices. 
It provides capabilities like coordination, synchronization, and error handling, resulting in more reliable and deterministic application behavior.

- overview schema of temporal server orchestrating workflows and actions
- schema of how parent/child workflows interact
- example of one workflow starting another workflow

### Simplified development: 
Temporal.io simplifies microservice development by providing a programming model that allows developers to focus on business logic without 
worrying about the complexities of distributed systems. It provides abstractions for handling asynchronous communication, retries, 
timeouts, and failure handling, which reduces the amount of boilerplate code developers need to write.

- schema of temporal layer inside an application
- example of how temporal handles retries and timeouts
- example of how temporal handles failures
- example of how temporal handles asynchronous communication
- example of how temporal handles scheduling

### Demo
- flow (with bugged pod)
- flow with fixed pod
- flow with pod down
- flow with all up

