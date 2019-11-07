package com.qf.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
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
public class MyCodeFilter implements GatewayFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("验证码过滤器触发。。。。");
        ServerHttpRequest request = exchange.getRequest();
        //先获取cookie
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie code_token = cookies.getFirst("code_token");
        System.out.println("code_token = " + code_token);
        String codeToken="";
        if(code_token!=null && code_token.getValue()!=null){
             codeToken= code_token.getValue();
        }
        String code = request.getQueryParams().getFirst("code");
        System.out.println("code = " + code);
        if(codeToken!=""){
            String sCode = stringRedisTemplate.opsForValue().get(codeToken);
            System.out.println("sCode = " + sCode);
            if(code!=null && !code.equals("") && code.equals(sCode)){
                //验证码正确，删除redis中的验证码，放行
                stringRedisTemplate.delete(codeToken);
                return chain.filter(exchange);
            }
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        String msg = "验证码错误";
        try {
            msg=URLEncoder.encode(msg,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.getHeaders().set("Location","/info/error?msg=" + msg);
        return response.setComplete();
    }

}
