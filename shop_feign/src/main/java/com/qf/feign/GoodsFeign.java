package com.qf.feign;

import com.qf.entity.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Component
@FeignClient("web-goods")
public interface GoodsFeign {

    @RequestMapping("/goods/list")
    List<Goods> queryAllGoods();
}
