package nl.wlagemaat.demo.vroem.endpoint.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.wlagemaat.demo.vroem.model.MQResponseDto;
import nl.wlagemaat.demo.vroem.sven.SvenService;
import nl.wlagemaat.demo.vroem.util.JacksonConvertingMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class SvenHandler extends JacksonConvertingMessageHandler<MQResponseDto> {

	private final SvenService svenService;

	public SvenHandler(ObjectMapper objectMapper, SvenService svenService) {
		super(objectMapper);
		this.svenService = svenService;
	}
	
	public void handle(MQResponseDto input) {
		if(input.message().toLowerCase().contains("worm")){
			svenService.backToWorm(input.transgressionNumber());
		} else {
			svenService.finishSvenTask(input.transgressionNumber(), input.message());
		}
	}
}
