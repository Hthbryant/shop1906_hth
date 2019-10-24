package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CartMapper;
import com.qf.entity.Goods;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.feign.GoodsFeign;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String insertCart(String cartToken, Integer gid, Integer gnumber, User user) {

        System.out.println(user);
        Goods goods = goodsFeign.queryById(gid);

        Shopcart shopcart = new Shopcart(
                gid,
                user!=null?  user.getId() : null,
                gnumber,
                null,
                goods
        );

        if(user!=null){
            cartMapper.insert(shopcart);
        }else{

            cartToken=  cartToken==null? UUID.randomUUID().toString() :  cartToken;

            redisTemplate.opsForList().leftPush(cartToken, shopcart);
            redisTemplate.expire(cartToken,365, TimeUnit.DAYS);

            return cartToken;
        }

        return null;
    }

    @Override
    public List<Shopcart> getCart(String cartToken, User user) {
        if(user!=null){
            System.out.println("查询了数据库");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid",user.getId());
            queryWrapper.orderByDesc("create_time");
            List<Shopcart> shopcartList = cartMapper.selectList(queryWrapper);
            //对shopcartList的goods对象赋值
            for (Shopcart shopcart : shopcartList) {
                shopcart.setGoods(goodsFeign.queryById(shopcart.getGid()));
            }
            return shopcartList;
        } else{
           cartToken = cartToken==null ? UUID.randomUUID().toString() : cartToken;
            System.out.println("查询了redis");
            Long size = redisTemplate.opsForList().size(cartToken);
            List<Shopcart> shopcartList =  redisTemplate.opsForList().range(cartToken,0,size);
            return shopcartList;
        }

    }
}
