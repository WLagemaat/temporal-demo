package nl.wlagemaat.demo.vroem.mq;

import lombok.Getter;
import nl.wlagemaat.demo.vroem.model.MQResponse;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MQReceiver {

    public void receiveMessage(MQResponse message) {
        System.out.println("Received <" + message.getParty() + ">");
    }

}