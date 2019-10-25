package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CartMapper;
import com.qf.entity.Address;
import com.qf.entity.Goods;
import com.qf.entity.Shopcart;
import com.qf.entity.User;
import com.qf.feign.GoodsFeign;
import com.qf.feign.OrderFeign;
import com.qf.service.ICartService;
import com.qf.util.ShopMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderFeign orderFeign;

    @Override
    public String insertCart(String cartToken, Integer gid, Integer gnumber, User user) {

        //根据商品id查询商品信息
        Goods goods = goodsFeign.queryById(gid);


        Shopcart shopcart = new Shopcart(
                gid,
                user!=null?  user.getId() : null,
                gnumber,
                goods.getPrice(),
                goods,
                null
        );
        //对购物车里面的小计进行赋值
        shopcart = ShopMoney.countPrice(shopcart);
        //打印单个购物车的信息
        System.out.println("该购物车信息为：");
        System.out.println(shopcart);

        if(user!=null){
            //判断该用户是否添加过该产品，如果添加过就在之前数量上增加
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid",user.getId());
            queryWrapper.eq("gid",gid);
            Shopcart shopcart1 = cartMapper.selectOne(queryWrapper);
            if(shopcart1!=null){
                //添加过该商品
                System.out.println("有相同商品");
                shopcart1.setNumber(shopcart1.getNumber()+gnumber);
                shopcart1 = ShopMoney.countPrice(shopcart1);
                cartMapper.updateById(shopcart1);
            }else{
                //未添加过该商品
                cartMapper.insert(shopcart);
            }
            /*cartMapper.insert(shopcart);*/
            return null;

        }else{
            cartToken = cartToken==null?UUID.randomUUID().toString() : cartToken;
            redisTemplate.opsForList().leftPush(cartToken,shopcart);
            return cartToken;
        }
    }

    @Override
    public List<Shopcart> getCart(String cartToken, User user) {
        if(user!=null){
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

    @Override
    public void merge(String cartToken, User user) {
        //从redis中取出购物车的信息
        Long size = redisTemplate.opsForList().size(cartToken);
        List<Shopcart> shopcartList = redisTemplate.opsForList().range(cartToken,0, size);
        if(shopcartList!=null){
            for (Shopcart shopcart : shopcartList) {
                shopcart.setUid(user.getId());
                //判断用户的购物车是否包含相同商品
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("uid",user.getId());
                queryWrapper.eq("gid",shopcart.getGid());
                Shopcart shopcart1 = cartMapper.selectOne(queryWrapper);
                if(shopcart1!=null){
                    //如果已经存在，就覆盖
                    shopcart1 = shopcart;
                    cartMapper.updateById(shopcart1);
                }else{
                    //不存在直接添加
                    cartMapper.insert(shopcart);
                }
            }
        }

    }

    @Override
    public List<Address> getUserAddress(Integer uid) {

        return orderFeign.getUserAddress(uid);
    }

    @Override
    public List<Shopcart> getCartByIds(Integer uid, Integer[] ids) {
        List<Shopcart> shopcartList = cartMapper.queryCartByIds(uid, ids);
        if(shopcartList!=null){
            //为goods赋值
            for (Shopcart shopcart : shopcartList) {
                shopcart.setGoods(goodsFeign.queryById(shopcart.getGid()));
            }
        }
        return shopcartList;
    }

    @Override
    public int deleteById(Integer id) {
        return cartMapper.deleteById(id);
    }
}
