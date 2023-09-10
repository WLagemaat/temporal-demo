package nl.wlagemaat.demo.vroem.endpoint.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.wlagemaat.demo.vroem.model.MQResponseDto;
import nl.wlagemaat.demo.vroem.rdw.RdwService;
import nl.wlagemaat.demo.vroem.util.JacksonConvertingMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class BasHandler extends JacksonConvertingMessageHandler<MQResponseDto> {

	private final RdwService rdwService;

	public BasHandler(ObjectMapper objectMapper, RdwService rdwService) {
		super(objectMapper);
		this.rdwService = rdwService;
	}
	
	public void handle(MQResponseDto input) {
		rdwService.finishBasTask(input.transgressionNumber(), input.message());
	}
}
