package com.qf.service;

import com.qf.entity.OrderDetails;
import com.qf.entity.User;

import java.util.List;

public interface IOrderDetailsService {

    List<OrderDetails> queryDetailsByOid(Integer oid);

    int insertOrderDetails(User user, Integer oid, Integer[] shopcartids, Integer addressid);
}
