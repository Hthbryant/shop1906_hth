package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private ISearchService searchService;


    @RequestMapping("/insert")
    @ResponseBody
    public boolean insertSolr(@RequestBody Goods goods){

        return searchService.insertSolr(goods);
    }
    @RequestMapping("/keyword")
    public String searchByKeyword(String keyword, ModelMap map){

//        System.out.println("搜索的商品为："+keyword);

        List<Goods> goodsList = searchService.query(keyword);


        map.put("goodsList",goodsList);
        return "searchList";
    }
}
