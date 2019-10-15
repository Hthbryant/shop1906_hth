package com.qf.service.impl;

import com.qf.entity.Goods;
import com.qf.service.IItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
public class ItemServiceImpl implements IItemService {

    @Autowired
    private Configuration configuration;

    @Override
    public boolean createHtml(Goods goods) {

        String path = ItemServiceImpl.class.getResource("/static/html").getPath();
        System.out.println("创建静态页面的路径为："+path);

        try(
                Writer out = new FileWriter(path+"/"+goods.getId()+".html");
         ) {
            Template template = configuration.getTemplate("goods.ftl");
            Map<String,Object> map = new HashMap<>();
            map.put("goods",goods);
            template.process(map,out);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
