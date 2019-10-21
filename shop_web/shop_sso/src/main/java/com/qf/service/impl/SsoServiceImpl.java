package com.qf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.SSOMapper;
import com.qf.entity.Email;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.ISsoService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SsoServiceImpl implements ISsoService {

    @Autowired
    private SSOMapper ssoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public User queryUserByName(String username) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);
        return ssoMapper.selectOne(queryWrapper);
    }

    @Override
    public ResultData sendEmail(String username) {

        User user = this.queryUserByName(username);
        if(user == null){
            return new ResultData().setCode("1000").setMsg("该用户不存在");
        }

        String updatePasswordId = UUID.randomUUID().toString();
        String updateUrl="http://localhsot:16666/sso/toUpdatePassword?token="+updatePasswordId;
        stringRedisTemplate.opsForValue().set(updatePasswordId,username);
        stringRedisTemplate.expire(updatePasswordId,5, TimeUnit.MINUTES);


        System.out.println("将邮件队形上传到rabbitmq");


        //将邮件对象上传到rabbitmq
        Email email = new Email().setTo(user.getEmail()).setSubject("凌易管理系统找回密码").setContent("您的账号申请了密码找回，如果是本人操作，修改密码请点击：<a href='"+updateUrl+"'>"+updateUrl+"</a>");
        rabbitTemplate.convertAndSend("mail_exchange","",email);



        String hideMessage = user.getEmail().substring(3, user.getEmail().indexOf("@"));
        System.out.println("要隐藏的字符为："+hideMessage);
        String showEmailMsg = user.getEmail().replace(hideMessage, "*******");
        System.out.println("替换之后的邮箱为："+showEmailMsg);

        return new ResultData().setCode("0000").setMsg(showEmailMsg);
    }

    @Override
    public int updatePassword(User user) {
        return ssoMapper.updateById(user);
    }

    @Override
    public int register(User user) {
        return ssoMapper.insert(user);
    }

    @Override
    public User loginByNamePwd(String username, String password) {
        User user = this.queryUserByName(username);
        if(user!=null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
}
