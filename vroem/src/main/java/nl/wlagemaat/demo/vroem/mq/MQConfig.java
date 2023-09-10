package nl.wlagemaat.demo.vroem.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class MQConfig {

    @Value("${mq.topic.exchange}")
    private String topicExchangeName;

    @Value("${mq.queue.vroem}")
    private String vroemQueueValue;

    @Value("${mq.queue.bas}")
    private String basQueueValue;

    @Value("${mq.routing.key.pre-intake}")
    private String routingKey;

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String password;
    @Bean
    Queue vroemQueue() {
        return new Queue(vroemQueueValue, false);
    }

    @Bean
    Queue BasQueue() {
        return new Queue(basQueueValue, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue vroemQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vroemQueue).to(exchange).with(routingKey);
    }

    @Bean
    @Primary
    public CachingConnectionFactory rabbitMQConnectionFactory(ConnectionFactory connectionFactory) {
        log.info("Creating connectionFactory for {}@{}:{}", connectionFactory.getUsername(), connectionFactory.getHost(), connectionFactory.getPort());
        var factory = new CachingConnectionFactory(connectionFactory.getHost(), connectionFactory.getPort());
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setVirtualHost(connectionFactory.getVirtualHost());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitMQConnectionFactory, ObjectMapper objectMapper) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitMQConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }
}
