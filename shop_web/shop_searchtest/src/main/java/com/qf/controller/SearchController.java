package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
