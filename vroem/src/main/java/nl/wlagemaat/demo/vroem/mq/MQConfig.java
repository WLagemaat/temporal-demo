package nl.wlagemaat.demo.vroem.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(vroemQueueValue);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MQReceiver MQReceiver) {
        return new MessageListenerAdapter(MQReceiver, "receiveMessage");
    }
}
