package com.qf.shop_goods;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopGoodsApplicationTests {

    @Test
    public void contextLoads() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.98.172");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/admin");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
//        channel.queueDeclare("mail_queue",false,false,false,null);
//        channel.queueDeclare("item_queue",false,false,false,null);
//        channel.queueDeclare("search_queue",false,false,false,null);
        channel.exchangeDeclare("goods_exchange","");
    }

}
