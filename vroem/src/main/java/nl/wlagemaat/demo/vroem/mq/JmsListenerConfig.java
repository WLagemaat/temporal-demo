package nl.wlagemaat.demo.vroem.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import nl.wlagemaat.demo.vroem.endpoint.jms.IntakeHandler;
import nl.wlagemaat.demo.vroem.util.JacksonConvertingMessageHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JmsListenerConfig {
    
    private final IntakeHandler intakeHandler;
    private final RabbitTemplate rabbitTemplate;
    
    @RabbitListener(queues = "#{@vroemQueue}", containerFactory = "rabbitListenerContainerFactory")
    public void listenForPhxOnboardingResults(Message msg) {
        handleMessage(intakeHandler, msg,
                configProperties.getRabbitmq().getPhxOnboardingResultQueue(),
                configProperties.getRabbitmq().getPhxOnboardingResultDLQueue());
    }
    

    private void handleMessage(JacksonConvertingMessageHandler<?> handler, Message amqpMessage, String queue, String dlq) {
        try {
            log.debug("Received Message from queue {}: {}", queue, amqpMessage.getBody());
            handler.handle(new String(amqpMessage.getBody(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Unable to process OnboardingResultMessage", e);
            rabbitTemplate.send(dlq, amqpMessage);
        }
    }
}