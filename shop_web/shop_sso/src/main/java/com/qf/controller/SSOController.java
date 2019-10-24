package com.qf.controller;

import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.ISsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SSOController {


    @Autowired
    private ISsoService ssoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/isLogin")
    @ResponseBody
    public ResultData<User> isLogin(@CookieValue(value = "login_token",required = false) String loginToken){
        if(loginToken!=null){
            User user = (User) redisTemplate.opsForValue().get(loginToken);
            if(user!=null){
                return new ResultData<User>().setCode("0000").setMsg("已登录").setData(user);
            }
        }
        return new ResultData().setCode("1000").setMsg("未登录");
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping("/login")
    public String login(String username, String password,String returnUrl, HttpServletResponse response){
        System.out.println("进来了这里");
        System.out.println(username+","+password);
        System.out.println("returnUrl:"+returnUrl);
        User user  = ssoService.loginByNamePwd(username,password);

        if( returnUrl == null || returnUrl== ""){
            returnUrl="http://localhost:16666/";
        }

        if(user!=null){

            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(token,user);
            redisTemplate.expire(token,7, TimeUnit.DAYS);

            Cookie cookie = new Cookie("login_token",token);
            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/");
            response.addCookie(cookie);

            System.out.println("最终的url:"+returnUrl);

            return "redirect:"+returnUrl;
        }
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(@CookieValue(value = "login_token",required = false) String loginToken,HttpServletResponse response){

        if(loginToken!=null){
            Cookie cookie = new Cookie("login_token",loginToken);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            redisTemplate.delete(loginToken);
        }

        return "login";
    }

    /**
     * 去登陆界面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 去注册界面
     * @return
     */
    @RequestMapping("/toRegister")
    public String toResister(){
        return "register";
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public String register(User user){
        int result = ssoService.register(user);
        if(result>0){
            return "login";
        }
        return "register";
    }

    /**
     * 跳转到修改密码之前确认用户页面
     * @param username
     * @return
     */
    @RequestMapping("/toGetEmail")
    @ResponseBody
    public ResultData toGetEmail(String username){

       return ssoService.sendEmail(username);

    }

    /**
     * 跳到忘记密码页面
     * @return
     */
    @RequestMapping("/forgetPassword")
    public String forgetPassword(){

        return "forgetPassword";
    }

    /**
     * 跳到修改密码页面
     * @return
     */
    @RequestMapping("/toUpdatePassword")
    public String toUpdatePassword(String token, ModelMap map){
        map.put("token",token);
        return "updatePassword";
    }

    /**
     * 修改密码
     * @param token
     * @param password
     * @return
     */
    @RequestMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(String token,String password){
        String username = stringRedisTemplate.opsForValue().get(token);
        System.out.println("username:"+username);
        User user = ssoService.queryUserByName(username);
        user.setPassword(password);
        int result = ssoService.updatePassword(user);
        if(result>0){
            stringRedisTemplate.delete(token);
            return "<script>location.href='/sso/toLogin';</script>";
        }else{
            return "<script>alert('修改密码失败');location.href='/sso/toLogin';</script>";
        }
    }
}
