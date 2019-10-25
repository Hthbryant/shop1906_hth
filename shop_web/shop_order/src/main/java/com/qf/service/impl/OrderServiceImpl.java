package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.AddressMapper;
import com.qf.dao.OrderMapper;
import com.qf.entity.Address;
import com.qf.entity.Orders;
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

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Orders> queryOrderByUid(Integer uid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",uid);
        queryWrapper.orderByDesc("create_time");
        return orderMapper.selectList(queryWrapper);
    }

    /**
     * 添加订单
     * @param user
     * @param shopcartid
     * @return
     */
    @Override
    public Orders insertOrder(User user, Integer[] shopcartid, Integer addressid) {

        //查询address对象
        Address address = addressMapper.selectById(addressid);

        List<Shopcart> shopcartList = cartFeign.getCartByIds(user.getId(),shopcartid);

        String subj = null;
        StringBuffer details = new StringBuffer();
        if(shopcartList!=null){
            shopcartList = ShopMoney.countMoney(shopcartList);
            int i = 0;
            for (Shopcart shopcart : shopcartList) {
                if(i==0){
                    subj=shopcart.getGoods().getSubject();
                    details.append(shopcart.getGoods().getInfo());
                }
            }
        }
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Shopcart shopcart : shopcartList) {
            sum = sum.add(shopcart.getShopPrice());
        }
        //用UUID生成一个唯一订单号
        System.out.println("添加订单");
        String orderid = UUID.randomUUID().toString();
        Orders order = new Orders().setOid(orderid)
                .setUid(user.getId())
                .setAddress(address.getAddress())
                .setCode(address.getCode())
                .setPerson(address.getPerson())
                .setPhone(address.getPhone())
                .setOrderDetails(subj)
                .setOrderPrice(sum);
        orderMapper.insert(order);
        return order;
    }


}
