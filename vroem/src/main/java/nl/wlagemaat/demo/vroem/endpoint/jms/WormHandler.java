package nl.wlagemaat.demo.vroem.endpoint.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.wlagemaat.demo.vroem.model.MQResponseDto;
import nl.wlagemaat.demo.vroem.rdw.RdwService;
import nl.wlagemaat.demo.vroem.util.JacksonConvertingMessageHandler;
import nl.wlagemaat.demo.vroem.worm.WormService;
import org.springframework.stereotype.Component;

@Component
public class WormHandler extends JacksonConvertingMessageHandler<MQResponseDto> {

	private final WormService wormService;

	public WormHandler(ObjectMapper objectMapper, WormService wormService) {
		super(objectMapper);
		this.wormService = wormService;
	}
	
	public void handle(MQResponseDto input) {
		wormService.finishWormTask(input.transgressionNumber(), input.message());
	}
}
