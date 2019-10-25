package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.util.ShopMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
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


    /**
     * 去准备支付页面
     * @param user
     * @param checkone
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/toReadyPay")
    public String toReadyPay(User user,Integer[] checkone,ModelMap map){

        System.out.println("用户勾选的商品id：");
        System.out.println(Arrays.toString(checkone));
        //通过用户信息拿到用户的所有收货地址
        List<Address> addressList = cartService.getUserAddress(user.getId());
        map.put("addressList",addressList);
        //查询出用户已经选择过的id的商品
        List<Shopcart> shopcartList =   cartService.getCartByIds(user.getId(),checkone);
        //打印选中的商品
        System.out.println("打印选中的商品并对小计赋值");
        BigDecimal shopSum = BigDecimal.valueOf(0);
        if(shopcartList!=null){
            for (Shopcart shopcart : shopcartList) {
                shopcart = ShopMoney.countPrice(shopcart);
                shopSum = shopSum.add(shopcart.getShopPrice());
                System.out.println(shopcart);
            }

        }
        //将总价存在map
        double sum = shopSum.doubleValue();
        map.put("shopSum",sum);
        map.put("shopcartList",shopcartList);

        return "readyPay";
    }

    /**
     * 通过id数组获得购物车集合
     * @param uid
     * @param ids
     * @return
     */

    @RequestMapping("/getCartByIds")
    @ResponseBody
    public List<Shopcart> getCartByIds(@RequestParam("uid") Integer uid,@RequestParam("ids") Integer[] ids){
        return cartService.getCartByIds(uid,ids);
    }



    @RequestMapping("/deleteById")
    @ResponseBody
    public int deleteById(@RequestParam("id") Integer id){
        return cartService.deleteById(id);
    }

}
