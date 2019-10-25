package com.qf.service;

import com.qf.entity.Order;
import com.qf.entity.User;

import java.util.List;

public interface IOrderService {

    List<Order> queryOrderByUid(Integer uid);


    Order insertOrder(User user, Integer[] checkone);
}
