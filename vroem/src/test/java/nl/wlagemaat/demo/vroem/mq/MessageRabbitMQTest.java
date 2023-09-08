package nl.wlagemaat.demo.vroem.mq;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MessageRabbitMQTest {

    @MockBean
    private MQRunner runner;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MQReceiver MQReceiver;

    @Value("${mq.queue}")
    private String queueName;

    @Test
    public void test() {
        try {
            rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ!");
        } catch (AmqpConnectException e) {
            // ignore - rabbit is not running
        }
    }

}