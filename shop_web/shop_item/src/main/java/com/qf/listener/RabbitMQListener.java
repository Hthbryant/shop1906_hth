package com.qf.listener;

import com.qf.entity.Goods;
import com.qf.service.IItemService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @Autowired
    private IItemService itemService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "item_queue",durable = "true"),
            exchange = @Exchange(name = "goods_exchange",durable = "true",type = "direct"),
            key = "normal"
    ))
    public void goodsMsgHandler(Goods goods){
        System.out.println("item收到了消息，正在执行创建页面");
        itemService.createHtml(goods);
    }
}
