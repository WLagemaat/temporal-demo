package nl.wlagemaat.demo.vroem.mq;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MQRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final MQReceiver MQReceiver;

    @Value("${mq.topic.exchange}")
    private String topicExchangeName;

    @Value("${mq.routing.key}")
    private String routingKey;

    public MQRunner(MQReceiver MQReceiver, RabbitTemplate rabbitTemplate) {
        this.MQReceiver = MQReceiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, "Hello from RabbitMQ!");
    }

}