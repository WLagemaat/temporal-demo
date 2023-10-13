package nl.wlagemaat.demo.vroem.mq;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.intake.FineIntakeService;
import nl.wlagemaat.demo.vroem.model.MQResponseDto;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.rdw.RdwService;
import nl.wlagemaat.demo.vroem.sven.SvenService;
import nl.wlagemaat.demo.vroem.util.JsonMapper;
import nl.wlagemaat.demo.vroem.workflow.FineIntakeWorkflowService;
import nl.wlagemaat.demo.vroem.worm.WormService;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Component
@RequiredArgsConstructor
public class MQReceiver {

    private final RdwService rdwService;
    private final WormService wormService;
    private final SvenService svenService;
    private final FineIntakeService fineIntakeService;
    private final FineIntakeWorkflowService fineIntakeWorkflowService;
    private final JsonMapper jsonMapper;

    public void receiveMessage(MQResponseDto input) {
        System.out.println("Received MQResponseDto <" + input + ">");

        switch(input.party()){
            case BAS -> {
                rdwService.finishBasTask(input.transgressionNumber(), input.message());
            }
            case WORM -> {
                wormService.finishWormTask(input.transgressionNumber(), input.message());
            }
            case SVEN -> {
                if(input.message().toLowerCase().contains("worm")){
                    svenService.backToWorm(input.transgressionNumber());
                } else {
                    svenService.finishSvenTask(input.transgressionNumber(), input.message());
                }
            }
        }
    }

    public void receiveMessage(FineDto input) {
        System.out.println("Received TransgressionDto <" + input + ">");
        fineIntakeWorkflowService.intake(input);
    }

    public void receiveMessage(Object input) {
        System.out.println("Received Object <" + input + ">");
    }

    public void receiveMessage(byte[] input) {
        var stringInput = new String(input).replaceAll("\\\\", "");
        System.out.println("Received Byte[] <" + stringInput + ">");
        FineDto fine = jsonMapper.parseJson(stringInput, FineDto.class);
        fineIntakeWorkflowService.intake(fine);
    }

    public void receiveMessage(String message) {
        System.out.println("Received Message <" + message + ">");

        FineDto fine = jsonMapper.parseJson(message, FineDto.class);
        fineIntakeWorkflowService.intake(fine);
    }
    public void receiveMessage(Message message) {
        String jsonValue = new String(message.getBody(), UTF_8);
        System.out.println("Received Message <" + jsonValue + ">");

        FineDto fine = jsonMapper.parseJson(jsonValue, FineDto.class);
        fineIntakeWorkflowService.intake(fine);
    }
}