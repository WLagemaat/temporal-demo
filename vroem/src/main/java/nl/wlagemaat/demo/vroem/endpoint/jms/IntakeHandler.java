package nl.wlagemaat.demo.vroem.endpoint.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.wlagemaat.demo.vroem.intake.FineDeliveryService;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.util.JacksonConvertingMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class IntakeHandler extends JacksonConvertingMessageHandler<TransgressionDto> {

	private final FineDeliveryService fineDeliveryService;

	public IntakeHandler(ObjectMapper objectMapper, FineDeliveryService fineDeliveryService) {
		super(objectMapper);
		this.fineDeliveryService = fineDeliveryService;
	}
	
	public void handle(TransgressionDto input) {
		fineDeliveryService.validate(input);
	}
}
