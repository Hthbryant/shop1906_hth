package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.OrderDetailsMapper;
import com.qf.entity.OrderDetails;
import com.qf.service.IOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsServiceImpl implements IOrderDetailsService {

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Override
    public List<OrderDetails> queryDetailsByOid(Integer oid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("oid",oid);
        queryWrapper.orderByDesc("create_time");
        return orderDetailsMapper.selectList(queryWrapper);
    }
}
