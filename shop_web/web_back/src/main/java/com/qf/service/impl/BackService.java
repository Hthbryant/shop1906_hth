package com.qf.service.impl;

import com.qf.dao.BGoodsMapper;
import com.qf.entity.Goods;
import com.qf.feign.GoodsFeign;
import com.qf.service.IBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackService implements IBackService {

    @Autowired
    private BGoodsMapper bGoodsMapper;

    @Autowired
    private GoodsFeign goodsFeign;


    @Override
    public List<Goods> queryAllGoods() {
        return goodsFeign.queryAllGoods();
    }
}
