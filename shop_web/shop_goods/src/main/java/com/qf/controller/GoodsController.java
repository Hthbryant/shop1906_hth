package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;


    @RequestMapping("/list")
    @ResponseBody
    public List<Goods> queryAllGoods(){
        System.out.println("调用了goods里面的list方法");
        return goodsService.queryAllGoods();
    }

    @RequestMapping("/insert")
    @ResponseBody
    public boolean insert(@RequestBody Goods goods){
        //添加商品信息
        int result = goodsService.insertGoods(goods);

        return result>0;
    }
    @RequestMapping("/queryById")
    @ResponseBody
    public Goods queryById(@RequestParam Integer gid){
        return goodsService.queryById(gid);
    }

    @RequestMapping("/queryMiaoshaNow")
    @ResponseBody
    public List<Map<String,Object>> queryMiaoshaList(){
        return goodsService.queryMiaoshaList();
    }
}
