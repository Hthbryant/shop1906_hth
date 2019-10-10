package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.IBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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
        map.put("goodsList",goodsList);
        return "goodsList";
    }

    @RequestMapping("/uploader")
    public void imgUpload(MultipartFile file){
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

    }
}
