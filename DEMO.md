# Getting Started

https://docs.temporal.io/

### What does Temporal in short do and look like?
![Temporal_introduction.png](docs%2FTemporal_introduction.png)

## Use Case: Insurance Case intake process 
### result: possible different execution paths
![Demo_insurance_case_flow.png](docs%2FDemo_insurance_case_flow.png)

## Design
### Plotted flows and Activities
![Design_flows_insurance_case.png](docs%2FDesign_flows_insurance_case.png)
How the Applications are connected to the flows
![Design_insurance_case_with_applications.png](docs%2FDesign_insurance_case_with_applications.png)

## Demo:
### Prerequisites
- Temporial stack is running
- Applications: CLAP / MASS / ~~RDW~~ / IOT / DOG / EXE(b) 

### Start the demo

- Feed the application with a new case

[start-insurance-flow.http](docker%2Fstart-insurance-flow.http)

Result: Activity is stuck, lets see in [Web-UI](http://localhost:8080)
#### Show usage of the search label we put with the admin tool
> InsuranceCaseState = 'validated'

After we turn on the RDW container, the activity will be picked up and the flow will continue.
But alas, the flow stops again..

Change container of EXE  (`exe-bug` -> `exe-1` ) inside [.env](docker%2F.env) and update the docker-compose

Watch the magic happen in the [Web-UI](http://localhost:8080)

extra: [Versioning support](https://docs.temporal.io/dev-guide/java/versioning)


## The Code

- setup: [TemporalService.java](clap%2Fsrc%2Fmain%2Fjava%2Fnl%2Fwlagemaat%2Fdemo%2Fclap%2Fworkflow%2FTemporalService.java) 
- Endpoint where it starts: [IntakeController.java](clap%2Fsrc%2Fmain%2Fjava%2Fnl%2Fwlagemaat%2Fdemo%2Fclap%2Fcontroller%2FIntakeController.java)
- The flow: [InsuranceCaseWorkflowImpl.java](clap%2Fsrc%2Fmain%2Fjava%2Fnl%2Fwlagemaat%2Fdemo%2Fclap%2Fworkflow%2Finsuranceflow%2FInsuranceCaseWorkflowImpl.java)
- The first childFlow: [IntakeWorkflowImpl.java](clap%2Fsrc%2Fmain%2Fjava%2Fnl%2Fwlagemaat%2Fdemo%2Fclap%2Fworkflow%2Fintakeflow%2FIntakeWorkflowImpl.java)

