package nl.wlagemaat.demo.vroem.mq;

import nl.wlagemaat.demo.vroem.model.Party;
import nl.wlagemaat.demo.vroem.model.FineDto;
import nl.wlagemaat.demo.vroem.util.JsonMapper;
import org.junit.jupiter.api.Test;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static nl.wlagemaat.demo.vroem.util.VroemUtilities.doesPass;
import static nl.wlagemaat.demo.vroem.util.VroemUtilities.generateTransgressionNumber;

@SpringBootTest
public class MessageRabbitMQTest {

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.queue.vroem}")
    private String queueName;

    @Test
    public void test() {
        try {
            String json =jsonMapper.toJson(generateDefaultFine().build());
            rabbitTemplate.convertAndSend(queueName, json);
        } catch (AmqpConnectException e) {
            // ignore - rabbit is not running
        }
    }

    @Test
    public void testString() {
        try {
            rabbitTemplate.convertAndSend(queueName, "Message test");
        } catch (AmqpConnectException e) {
            // ignore - rabbit is not running
        }
    }



    private FineDto.FineDtoBuilder generateDefaultFine(){
        return FineDto.builder()
                .party(Party.HANS)
                .isMulder(doesPass(70))
                .validOdds(5)
                .transgressionNumber(generateTransgressionNumber())
                .cvomTechnicalErrorOdds(5)
                .mulderTechnicalErrorOdds(5)
                .wormTechnicalErrorOdds(5)
                .svenTechnicalErrorOdds(5)
                .rdwOdds(10)
                .rdwTechnicalErrorOdds(3);

    }

}