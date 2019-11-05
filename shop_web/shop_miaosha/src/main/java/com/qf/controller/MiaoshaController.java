package com.qf.controller;

import com.qf.feign.GoodsFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsFeign goodsFeign;

    /**
     * 查询此刻的秒杀商品列表，以及下一个点的待秒杀列表
     * @return
     */
    @RequestMapping("/queryListByNow")
    @ResponseBody
    public List<Map<String,Object>> queryListByNow(){
        System.out.println("进来了这个查询秒杀的列表");
        List<Map<String,Object>> miaoshaList = goodsFeign.queryMiaoshaList();
        System.out.println(miaoshaList);
        return miaoshaList;
    }

}
