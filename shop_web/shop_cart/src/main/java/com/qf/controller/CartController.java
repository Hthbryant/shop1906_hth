package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {


    @IsLogin(mustLogin = true)
    @RequestMapping("/insert")
    public String insert(Integer gid, Integer gnumber, User user){
        System.out.println("商品id为："+gid);
        System.out.println("购买数量为："+gnumber);
        return "insertSuccess";
    }

}
