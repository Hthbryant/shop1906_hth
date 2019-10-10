package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.IBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goodsManager")
public class GoodsManagerController {

    @Autowired
    private IBackService backService;

    @RequestMapping("/list")
    public String queryAllGoods(ModelMap map){
        System.out.println("进来这goodsManager/list 方法");
        List<Goods> goodsList = backService.queryAllGoods();
        System.out.println("商品集合为：");
        System.out.println(goodsList);
        map.put("goodsList",goodsList);
        return "goodsList";
    }
}
