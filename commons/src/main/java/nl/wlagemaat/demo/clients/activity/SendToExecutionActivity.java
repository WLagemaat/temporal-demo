package nl.wlagemaat.demo.clients.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import nl.wlagemaat.demo.clients.model.InsuranceCaseDto;

@ActivityInterface
public interface SendToExecutionActivity {
	
	@ActivityMethod(name = "Send the Insurance Case to the Execution Department")
    void send(InsuranceCaseDto insuranceCaseDto, String workflowId);


}