package com.qf.service;

import com.qf.entity.Orders;
import com.qf.entity.User;

import java.util.List;

public interface IOrderService {

    List<Orders> queryOrderByUid(Integer uid);


    Orders insertOrder(User user, Integer[] shopcartid, Integer addressid);

    List<Orders> getOrder(Integer id);

    int paySuccess(Integer orderid);

    Orders queryOrderByOid(Integer id);

    int updateOrderByOid(String out_trade_no);
}
