package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.GoodsImagesMapper;
import com.qf.dao.GoodsMapper;
import com.qf.dao.GoodsMiaoshaMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.entity.GoodsMiaosha;
import com.qf.feign.ItemFeign;
import com.qf.feign.SearchFeign;
import com.qf.service.IGoodsService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsImagesMapper goodsImagesMapper;

    @Autowired
    private SearchFeign searchFeign;

    @Autowired
    private GoodsMiaoshaMapper goodsMiaoshaMapper;

    @Autowired
    private ItemFeign itemFeign;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Goods> queryAllGoods() {
        return goodsMapper.queryGoodsList();
    }

    @Override
    @Transactional
    public int insertGoods(Goods goods) {

        int result = goodsMapper.insert(goods);

        System.out.println("添加商品的结果为："+result);
        GoodsImages fengmian = new GoodsImages();
        fengmian.setGid(goods.getId());
        fengmian.setUrl(goods.getFengmian());
        fengmian.setIsDefault(1);
        goodsImagesMapper.insert(fengmian);

        List<String> otherImg = goods.getOtherImg();
        System.out.println(otherImg);
        if(otherImg!=null){
            for (String s : otherImg) {
                GoodsImages otherImage = new GoodsImages();
                otherImage.setGid(goods.getId());
                otherImage.setUrl(s);
                otherImage.setIsDefault(0);
                goodsImagesMapper.insert(otherImage);
            }
        }


        //添加秒杀商品信息
        GoodsMiaosha goodsMiaosha = goods.getGoodsMiaosha();
        System.out.println(goodsMiaosha);
        if(goodsMiaosha!=null){
            System.out.println("秒杀商品不为空");
            goodsMiaosha.setGid(goods.getId());
            goodsMiaoshaMapper.insert(goodsMiaosha);
        }


        //将商品信息放到rabbitmq中
        rabbitTemplate.convertAndSend("goods_exchange","",goods);

        return result;
    }

    @Override
    public Goods queryById(Integer gid) {
        Goods goods = goodsMapper.selectById(gid);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("gid",gid);
        queryWrapper.eq("is_default",1);
        goods.setFengmian(goodsImagesMapper.selectOne(queryWrapper).getUrl());
        return goods;
    }

    @Override
    public List<Map<String,Object>> queryMiaoshaList() {
        List<Map<String,Object>> miaoshaList = new ArrayList<>();

        //查询这个时间段的秒杀商品
        List<Goods> goodsList = goodsMiaoshaMapper.queryListNow();
        System.out.println(goodsList);
        Map<String,Object> nowMap = null;
        if(goodsList!=null && goodsList.size()>0){
            nowMap = new HashMap<>();
            nowMap.put("startTime",goodsList.get(0).getGoodsMiaosha().getStartTime());
            nowMap.put("endTime",goodsList.get(0).getGoodsMiaosha().getEndTime());
            nowMap.put("goods",goodsList);
        }
        miaoshaList.add(nowMap);

        //获得下一时间段的秒杀商品
        List<Goods> nextGoodsList = goodsMiaoshaMapper.queryListNext();
        Map<String,Object> nextMap = null;
        if(nextGoodsList!=null && nextGoodsList.size()>0){
            nextMap = new HashMap<>();
            nextMap.put("startTime",nextGoodsList.get(0).getGoodsMiaosha().getStartTime());
            nextMap.put("endTime",nextGoodsList.get(0).getGoodsMiaosha().getEndTime());
            nextMap.put("goods",nextGoodsList);
        }
        miaoshaList.add(nextMap);

        return miaoshaList;
    }
}
