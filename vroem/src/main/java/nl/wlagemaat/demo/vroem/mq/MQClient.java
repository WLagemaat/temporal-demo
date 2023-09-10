package nl.wlagemaat.demo.vroem.mq;

import lombok.RequiredArgsConstructor;
import nl.wlagemaat.demo.vroem.model.TransgressionDto;
import nl.wlagemaat.demo.vroem.repository.entities.Transgression;
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

    public void sendBasTask(String transgressionNumber){
        rabbitTemplate.convertAndSend(topicExchangeName, routingBas, transgressionNumber);
    }

    public void sendWormTask(String transgressionNumber){
        rabbitTemplate.convertAndSend(topicExchangeName, routingWorm, transgressionNumber);
    }

    public void sendSvenTask(Transgression transgression){
        rabbitTemplate.convertAndSend(topicExchangeName, routingSven, transgression);
    }


}
