package com.qf.service;

import com.qf.entity.OrderDetails;

import java.util.List;

public interface IOrderDetailsService {

    List<OrderDetails> queryDetailsByOid(Integer oid);
}
