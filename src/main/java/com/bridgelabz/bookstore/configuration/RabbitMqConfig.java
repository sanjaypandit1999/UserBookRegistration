package com.bridgelabz.bookstore.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bridgelabz.bookstore.util.IMessageListener;

@Configuration
public class RabbitMqConfig {

 public static final String ROUTING_KEY = "my.queue.key";
	 

	 /**
	  * purpose to create queue object in IOC container
	  *
	  */
	 @Bean
	 Queue queue() {
	  return new Queue(ROUTING_KEY, true);
	 }

	 /**
	  * purpose to exchange object store in IOC container
	  *
	  */
	 @Bean
	 TopicExchange exchange() {
	  return new TopicExchange("my_queue_exchange");
	 }

	 /**
	  * purpose to binding queue argument with exchange argument
	  * 
	  * @param queue object and exchange object
	  *
	  */
	 @Bean
	 Binding binding(Queue queue, TopicExchange exchange) {
	  return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	 }
	 
	 
	 /**
	  * construct listener for rabitMq using SimpleMessageListenerContainer class
	  * 
	  *@param connectionFactory and listenerAdapter object
	  */
	 @Bean
	 SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
	 MessageListenerAdapter listenerAdapter) {
	  SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	  container.setConnectionFactory((org.springframework.amqp.rabbit.connection.ConnectionFactory) connectionFactory);
	  container.setQueueNames(ROUTING_KEY);
	  container.setMessageListener(listenerAdapter);
	  return container;	
	 }

	 /**
	  * purpose to the handling of messages to target listener methods via reflection
	  * 
	  * @param listener object
	  *
	  */
	 @Bean
	 MessageListenerAdapter myQueueListener(IMessageListener listener) {
	  return new MessageListenerAdapter(listener, "onMessage");
	 }
}
