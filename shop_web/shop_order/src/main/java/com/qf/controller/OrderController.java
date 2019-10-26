package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Orders;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.IOrderDetailsService;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IAddressService addressService;


    @Autowired
    private IOrderDetailsService orderDetailsService;

    @IsLogin(mustLogin = true)
    @RequestMapping("/insert")
    @Transactional
    public String insert(User user,Integer[] shopcartid,Integer sendwho){

        System.out.println("选中的购物车的商品的id为：");
        System.out.println(Arrays.toString(shopcartid));
        System.out.println("收货地址id为");
        System.out.println(sendwho);

        //将这些id号的购物车添加至订单
        Orders order = orderService.insertOrder(user,shopcartid,sendwho);
        System.out.println(order);
        System.out.println(order.getId());
        //将这些id号的购物车添加至订单详情
        //将这些id号的购物车从购物车里面删除
        orderDetailsService.insertOrderDetails(user,order.getId(),shopcartid,sendwho);
        return "insertSuccess";
    }

    /**
     * 获得用户的收货地址列表
     * @param uid
     * @return
     */
    @RequestMapping("/getUserAddress")
    @ResponseBody
    public List<Address> getUserAddress(@RequestParam Integer uid){
        return addressService.getUserAddress(uid);
    }

    /**
     * 用户添加收货地址
     * @param user
     * @param address
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/addAddress")
    @ResponseBody
    public int addAddress(User user,Address address){
        System.out.println("进来了添加收货地址");
       /* System.out.println(address);
        address.setUid(user.getId());
        int result =  addressService.addAddress(address);
        if(result>0){
            System.out.println("添加收货地址成功");
        }*/
       int result = 0;
        return result;
    }

    @IsLogin(mustLogin = true)
    @RequestMapping("/showOrder")
    public String showOrder(User user, ModelMap map){
        List<Orders> orderList =  orderService.getOrder(user.getId());
        System.out.println("orderlist的长度："+orderList.size());
        map.put("orderList",orderList);
        return "orderList";
    }

}
