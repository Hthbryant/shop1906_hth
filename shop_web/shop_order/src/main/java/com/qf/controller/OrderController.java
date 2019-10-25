package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Order;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.IOrderDetailsService;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String insert(User user,Integer[] checkone){

        //将这些id号的购物车添加至订单
        Order order = orderService.insertOrder(user,checkone);
        //将这些id号的购物车添加至订单详情
        for (Integer shopc : checkone) {
//            orderDetailsService.insertOrderDetail(user,shopc);
        }
        //将这些id号的购物车从购物车里面删除


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

}
