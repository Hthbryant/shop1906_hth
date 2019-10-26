package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.OrderDetailsMapper;
import com.qf.entity.OrderDetails;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.feign.CartFeign;
import com.qf.feign.GoodsFeign;
import com.qf.service.IOrderDetailsService;
import com.qf.util.ShopMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderDetailsServiceImpl implements IOrderDetailsService {

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Override
    public List<OrderDetails> queryDetailsByOid(Integer oid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("oid",oid);
        queryWrapper.orderByDesc("create_time");
        return orderDetailsMapper.selectList(queryWrapper);
    }

    @Override
    public int insertOrderDetails(User user, Integer oid, Integer[] shopcartids, Integer addressid) {
        System.out.println("要添加到订单详情的id为：");
        System.out.println(Arrays.toString(shopcartids));
        //通过购物车id数组拿到购物车对象集合
        List<Shopcart> shopcarts = cartFeign.getCartByIds(user.getId(),shopcartids);
        System.out.println("集合长度为："+shopcarts.size());
        for (Shopcart sh : shopcarts) {
//            s.setGoods(goodsFeign.queryById(s.getGid()));
            sh = ShopMoney.countPrice(sh);
            OrderDetails orderDetails = new OrderDetails().setGid(sh.getGid())
                    .setUid(user.getId())
                    .setOid(oid)
                    .setNumber(sh.getNumber())
                    .setDetailPrice(sh.getShopPrice())
                    .setSubject(sh.getGoods().getSubject())
                    .setPrice(sh.getCartPrice())
                    .setGoods(sh.getGoods());
            //将订单添加到数据库
            orderDetailsMapper.insert(orderDetails);
            //将该商品从购物车数据库删除
            cartFeign.deleteById(sh.getId());
        }

        return 0;
    }

    @Override
    public int paySuccess(Integer orderid) {
        //这这个订单号下面的订单详情的状态改为1
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("oid",orderid);
        List<OrderDetails> orderDetails = orderDetailsMapper.selectList(queryWrapper);
        if(orderDetails!=null){
            for (OrderDetails o : orderDetails) {
                o.setStatus(1);
                orderDetailsMapper.updateById(o);
            }
        }
        return 1;
    }
}
