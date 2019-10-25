package com.qf.aop;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Aspect
@Component
public class LoginAop {

    @Autowired
    private RedisTemplate redisTemplate;


    @Around("@annotation(IsLogin)")
    public Object loginAop(ProceedingJoinPoint joinPoint){

        //获得cookie
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String loginToken = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("login_token")){
                    loginToken = cookie.getValue();
                    break;
                }
            }
        }

        User user =null;
        if(loginToken!=null){
            user = (User) redisTemplate.opsForValue().get(loginToken);
        }


        if(user==null){
            //未登录
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            IsLogin isLogin = method.getAnnotation(IsLogin.class);
            boolean flag = isLogin.mustLogin();
            if(flag){
                String url = "http://localhost:16666" + request.getRequestURI().toString();
                String params = request.getQueryString();
                if(params!=null && params!=""){
                    url=url+"?"+params;
                }
                try {
                    url = URLEncoder.encode(url,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "redirect:http://localhost:16666/sso/toLogin?returnUrl="+url;
            }

        }
        //替换目标参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if( args[i]!=null && args[i].getClass() == User.class){
                System.out.println(user);
                args[i]=user;
                break;
            }
        }

        Object result = null;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        return result;
    }
}
