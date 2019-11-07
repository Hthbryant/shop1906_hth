package com.qf.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.qf.feign.GoodsFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询此刻的秒杀商品列表，以及下一个点的待秒杀列表
     * @return
     */
    @RequestMapping("/queryListByNow")
    @ResponseBody
    public List<Map<String,Object>> queryListByNow(){
        System.out.println("进来了这个查询秒杀的列表");
        List<Map<String,Object>> miaoshaList = goodsFeign.queryMiaoshaList();
        System.out.println(miaoshaList);
        return miaoshaList;
    }
    @RequestMapping("/getTimeNow")
    @ResponseBody
    public Date getTimeNow(){
        return new Date();
    }
    @RequestMapping("/qiangGou")
    public String qiangGou(String code,Integer gid){
        System.out.println("进来了抢购方法");
        System.out.println("gid = " + gid);
        System.out.println("code = " + code);
        return "qgSuccess";
    }
    @RequestMapping("/checkCode")
    public String checkCode(@CookieValue(value = "code_token")String codeToken, String code,Integer gid){
        System.out.println("用户输入的code为:"+code);
        //去redis中查询是否与验证码一致
        if(codeToken!=null && codeToken!=""){
            String sCode = stringRedisTemplate.opsForValue().get(codeToken);
            if(code.equals(sCode)){
                System.out.println("验证码正确");
                return "redirect:http://localhost:16666/miaosha/qiangGou?gid="+gid;
            }else{
                System.out.println("验证码错误");
                return "error";
            }
        }else{
            System.out.println("验证码失效");
            return "error";//验证码失效
        }

    }
    @RequestMapping("/getCode")
    @ResponseBody
    public void getCode(@CookieValue(value = "code_token",required = false)String codeToken, HttpServletResponse response){
        if(codeToken==null || codeToken==""){
            String codeUUID = UUID.randomUUID().toString();
            codeToken=codeUUID;
            Cookie cookie = new Cookie("code_token",codeUUID);
            cookie.setMaxAge(60*5);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        //验证码的字符串
        String text = defaultKaptcha.createText();
        System.out.println("code:"+text);
        //将验证码存到redis
        stringRedisTemplate.opsForValue().set(codeToken,text);
        //设置超时时间
        stringRedisTemplate.expire(codeToken,5, TimeUnit.MINUTES);

        BufferedImage image = defaultKaptcha.createImage(text);
        Properties properties = new Properties();

        defaultKaptcha.setConfig(new Config(properties));

        try {
            ImageIO.write(image,"jpg",response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
