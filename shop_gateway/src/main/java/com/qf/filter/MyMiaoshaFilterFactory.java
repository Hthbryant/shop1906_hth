package com.qf.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class MyMiaoshaFilterFactory extends AbstractGatewayFilterFactory {

    @Autowired
    private MyMiaoShaFilter myMiaoShaFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return myMiaoShaFilter;
    }

    @Override
    public String name() {
        return "myMiaosha";
    }
}
