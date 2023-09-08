package nl.wlagemaat.demo.vroem.mq;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.repository.entiteiten.Transgression;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MQClient {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.topic.exchange}")
    private String topicExchangeName;

    @Value("${mq.routing.key.bas}")
    private String routingBas;

    @Value("${mq.routing.key.worm}")
    private String routingWorm;

    @Value("${mq.routing.key.sven}")
    private String routingSven;

    public void sendBasTask(String overtredingsnummer){
        rabbitTemplate.convertAndSend(topicExchangeName, routingBas, overtredingsnummer);
    }

    public void sendWormTask(Transgression transgression){
        rabbitTemplate.convertAndSend(topicExchangeName, routingWorm, transgression.getKenteken());
    }

    public void sendSvenTask(Transgression transgression){
        rabbitTemplate.convertAndSend(topicExchangeName, routingSven, transgression);
    }


}
