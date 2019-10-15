package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private IItemService itemService;

    @RequestMapping("/create")
    @ResponseBody
    public boolean createHtml(@RequestBody Goods goods){

        return itemService.createHtml(goods);
    }
}
