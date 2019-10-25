package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.OrderMapper;
import com.qf.entity.Order;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.feign.CartFeign;
import com.qf.service.IOrderService;
import com.qf.util.ShopMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartFeign cartFeign;

    @Override
    public List<Order> queryOrderByUid(Integer uid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",uid);
        queryWrapper.orderByDesc("create_time");
        return orderMapper.selectList(queryWrapper);
    }

    /**
     * 添加订单
     * @param user
     * @param checkone
     * @return
     */
    @Override
    public Order insertOrder(User user, Integer[] checkone) {
        List<Shopcart> shopcartList = cartFeign.queryByIds(checkone);
        if(shopcartList!=null){
            shopcartList = ShopMoney.countMoney(shopcartList);
        }
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Shopcart shopcart : shopcartList) {
            sum.add(shopcart.getShopPrice());
        }
        //用UUID生成一个唯一订单号
        String orderid = UUID.randomUUID().toString();
        Order order = new Order().setOid(orderid)
                .setUid(user.getId())
                .setOrderPrice(sum);
        return order;
    }


}
