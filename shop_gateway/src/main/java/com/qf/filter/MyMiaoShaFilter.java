package com.qf.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class MyMiaoShaFilter implements GatewayFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("商品判断过滤器触发。。。");

        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String gid = queryParams.getFirst("gid");
        System.out.println("gid = " + gid);
        //去redis中查询该id是否在秒杀列表里面
        //查询redis中的当前年月日时
        String miaosha_now_time = "NOW_TIME";
        String s = stringRedisTemplate.opsForValue().get(miaosha_now_time);
        String miaosha_startTime = "MIAOSHA_START_TIME";
        String temp = miaosha_startTime + "_" + s;
        System.out.println("temp = " + temp);

        Boolean flag = stringRedisTemplate.opsForSet().isMember(temp, gid + "");

        if(flag){
            //在里面，是秒杀商品
            return chain.filter(exchange);
        }
        String msg = "该商品还没开抢";
        try {
            msg=URLEncoder.encode(msg,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location","/info/error?msg="+msg);
        return response.setComplete();
    }
}
