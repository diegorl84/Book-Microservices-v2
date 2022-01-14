package microservices.book.multiplication.configuration;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures RabbitMQ via AMQP abstraction to use events in our application.
 */
@Configuration
public class AMQPConfiguration {

  /**
   * We make the topic durable, so it’ll remain in the broker after RabbitMQ restarts. Also, we
   * declare it as a topic exchange since that’s the solution we envisioned in our event-driven
   * system. The name is picked up from configuration thanks to the already known @Value annotation.
   *
   * @param exchangeName
   * @return
   */
  @Bean
  public TopicExchange challengesTopicExchange(
      @Value("${amqp.exchange.attempts}") final String exchangeName) {
    return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
  }

  /**
   * By injecting a bean of type Jackson2JsonMessageConverter, we’re overriding the default Java
   * object serializer by a JSON object serializer. We do this to avoid various pitfalls of the Java
   * object serialization.
   *
   * @return
   */
  @Bean
  public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
