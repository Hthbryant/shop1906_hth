package com.qf.controller;

import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.ISsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sso")
public class SSOController {


    @Autowired
    private ISsoService ssoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 登录
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "";
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
