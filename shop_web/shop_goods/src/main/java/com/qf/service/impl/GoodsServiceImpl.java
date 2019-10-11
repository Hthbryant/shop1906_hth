package com.qf.service.impl;

import com.qf.dao.GoodsImagesMapper;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.service.IGoodsService;
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

    @Override
    public List<Goods> queryAllGoods() {
        return goodsMapper.queryGoodsList();
    }

    @Override
    @Transactional
    public int insertGoods(Goods goods) {
        int result = goodsMapper.insert(goods);
        GoodsImages fengmian = new GoodsImages();
        fengmian.setGid(goods.getId());
        fengmian.setUrl(goods.getFengmian());
        fengmian.setIsDefault(1);
        goodsImagesMapper.insert(fengmian);

        List<String> otherImg = goods.getOtherImg();
        System.out.println(otherImg);
        for (String s : otherImg) {
            GoodsImages otherImage = new GoodsImages();
            otherImage.setGid(goods.getId());
            otherImage.setUrl(s);
            otherImage.setIsDefault(0);
            goodsImagesMapper.insert(otherImage);
        }
        return result;
    }
}
