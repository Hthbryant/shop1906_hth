package com.qf.shop_goods;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

   /* @Bean("search_queue")
    public Queue getSearchQueue(){
        return new Queue("search_queue");
    }
    @Bean("item_queue")
    public Queue getItemQueue(){
        return new Queue("item_queue");
    }
    @Bean("goods_exchange")
    public FanoutExchange getFanoutExchange(){
        return new FanoutExchange("goods_exchange");
    }
    @Bean
    public Binding setBinding(Queue search_queue,FanoutExchange goods_exchange){
        return BindingBuilder.bind(search_queue).to(goods_exchange);
    }
    @Bean
    public Binding setBinding2(Queue item_queue,FanoutExchange goods_exchange){
        return BindingBuilder.bind(item_queue).to(goods_exchange);
    }*/
   @Bean("goods_exchange")
   public DirectExchange getDirectExchange1(){
       return new DirectExchange("goods_exchange");
   }
}
