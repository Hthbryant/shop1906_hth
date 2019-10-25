package com.qf.feign;

import com.qf.entity.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("web-order")
public interface OrderFeign {

    @RequestMapping("/order/getUserAddress")
    List<Address> getUserAddress(@RequestParam Integer uid);
}
