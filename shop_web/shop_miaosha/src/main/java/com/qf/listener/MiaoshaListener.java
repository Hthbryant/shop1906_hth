package com.qf.listener;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
public class MiaoshaListener {

//    @Autowired
//    private IMiaoshaService miaoshaService;
    @Autowired
    private Configuration configuration;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "miaosha_queue"),
            exchange = @Exchange(name = "goods_exchange",durable = "true",type = "direct"),
            key = "miaosha"
    ))
    public void miaoshaHandler(Goods goods){
        System.out.println("生成秒杀静态页");
        System.out.println("goods"+goods);

        //调用秒杀接口实现创建秒杀静态页
//        miaoshaService.createMiaosha(goods);
        String path = MiaoshaListener.class.getResource("/static/miaosha").getPath();
        System.out.println("创建静态页面的路径为："+path);

        try(
                Writer out = new FileWriter(path+"/"+goods.getId()+".html");
        ) {
            Template template = configuration.getTemplate("miaosha.ftl");
            Map<String,Object> map = new HashMap<>();
            map.put("goods",goods);
            template.process(map,out);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
