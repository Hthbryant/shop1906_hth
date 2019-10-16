package com.qf.listener;

import com.qf.entity.Goods;
import com.qf.service.IItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @Autowired
    private IItemService itemService;

    @RabbitListener(queues = "item_queue")
    public void goodsMsgHandler(Goods goods){
        System.out.println("item收到了消息，正在执行创建页面");
        itemService.createHtml(goods);
    }
}
