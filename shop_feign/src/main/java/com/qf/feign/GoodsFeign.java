package com.qf.feign;

import com.qf.entity.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient("web-goods")
public interface GoodsFeign {

    @RequestMapping("/goods/list")
    List<Goods> queryAllGoods();

    @RequestMapping("/goods/insert")
    boolean insert(@RequestBody Goods goods);

    @RequestMapping("/goods/queryById")
    Goods queryById(@RequestParam Integer gid);
}
