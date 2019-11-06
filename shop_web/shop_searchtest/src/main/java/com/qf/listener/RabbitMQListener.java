package com.qf.listener;

import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @Autowired
    private ISearchService searchService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "search_queue" ,durable = "true"),
            exchange = @Exchange(name = "goods_exchange",durable = "true",type = "direct"),
            key = {"miaosha","normal"}))
    public void goodsMsgHandler(Goods goods){
       searchService.insertSolr(goods);
    }
}
