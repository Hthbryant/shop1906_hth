package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.util.ShopMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;


    //去到购物车列表
    @IsLogin
    @RequestMapping("/showCart")
    public String showCart(@CookieValue(value = "cart_token",required =false)String cartToken, User user, ModelMap map){

        List<Shopcart> shopcartList =  cartService.getCart(cartToken,user);
        //如果购物车不为空就对购物车里面的小计进行赋值
        if(shopcartList!=null){
            shopcartList = ShopMoney.countMoney(shopcartList);
        }
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

    /**
     * 通过ajax异步请求，来获取cookie或者用户购物车里面的商品
     * @param cartToken
     * @param user
     * @return
     */
    @IsLogin
    @RequestMapping("/getCart")
    @ResponseBody
    public List<Shopcart> getCart(@CookieValue(value = "cart_token",required = false)String cartToken, User user){
        List<Shopcart> shopcartList = cartService.getCart(cartToken, user);
        //如果购物车不为空就对购物车里面的小计进行赋值
        if(shopcartList!=null){
            shopcartList = ShopMoney.countMoney(shopcartList);
        }
        System.out.println(shopcartList);

        return shopcartList;
    }

    /**
     * 合并购物车
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/merge")
    public String merge(String returnUrl ,
                        @CookieValue(value = "cart_token",required = false)String cartToken,
                        User user,
                        HttpServletResponse response){

        if(cartToken!=null){
            cartService.merge(cartToken,user);
            Cookie cookie = new Cookie("cart_token",cartToken);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            //从redis中删除cartToken
            redisTemplate.delete(cartToken);
        }

        return "redirect:"+returnUrl;
    }

}
