package com.qf.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class MyCodeFilterFactory extends AbstractGatewayFilterFactory {

    @Autowired
    private MyCodeFilter myCodeFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return myCodeFilter;
    }

    @Override
    public String name() {
        return "myCode";
    }
}
