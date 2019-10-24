package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;


    @IsLogin
    @RequestMapping("/showCart")
    public String showCart(@CookieValue(value = "cart_token",required =false)String cartToken, User user, ModelMap map){

        List<Shopcart> shopcartList =  cartService.getCart(cartToken,user);
        System.out.println(shopcartList);
        map.put("shopcartList",shopcartList);
        return "cartList";
    }

    @IsLogin
    @RequestMapping("/insert")
    public String insert(@CookieValue(value = "cart_token",required = false) String cartToken,
                         Integer gid,
                         Integer gnumber,
                         User user,
                         HttpServletResponse response){

        cartToken = cartService.insertCart(cartToken, gid, gnumber, user);

        if(cartToken!=null){
            Cookie cookie = new Cookie("cart_token",cartToken);
            cookie.setMaxAge(60*60*24*365);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "insertSuccess";
    }
    @IsLogin
    @RequestMapping("/getCart")
    @ResponseBody
    public List<Shopcart> getCart(@CookieValue(value = "cart_token",required = false)String cartToken, User user){
        List<Shopcart> shopcartList = cartService.getCart(cartToken, user);
        System.out.println(shopcartList);
        if(shopcartList!=null){
            System.out.println("购物车不为空");
        }
        return shopcartList;
    }

}
