package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.GoodsImagesMapper;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.feign.ItemFeign;
import com.qf.feign.SearchFeign;
import com.qf.service.IGoodsService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsImagesMapper goodsImagesMapper;

    @Autowired
    private SearchFeign searchFeign;

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


        //创建静态页面
//        if(!itemFeign.createHtml(goods)){
//            throw new RuntimeException("创建商品详情静态页面失败");
//        }


        //将商品信息添加到索引库
//        if(!searchFeign.insertSolr(goods)){
//            throw new RuntimeException("添加商品到索引库失败");
//        }

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
}
