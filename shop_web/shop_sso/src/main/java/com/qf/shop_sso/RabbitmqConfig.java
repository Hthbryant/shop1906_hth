package com.qf.shop_sso;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean("mail_queue")
    public Queue getQueue(){
        return new Queue("mail_queue");
    }
    @Bean("mail_exchange")
    public FanoutExchange getExchange(){
        return new FanoutExchange("mail_exchange");
    }
    @Bean
    public Binding getBinding(FanoutExchange mail_exchange,Queue mail_queue){
        return BindingBuilder.bind(mail_queue).to(mail_exchange);
    }
}
