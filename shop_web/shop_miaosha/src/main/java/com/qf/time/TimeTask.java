package com.qf.time;

import com.qf.util.ConstantUtil;
import com.qf.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimeTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 0 0/1 * * *")
    public void timeTest(){
        System.out.println("心态崩了！");
        //从将此刻的值存入redis
        String nowTimeStr = TimeUtil.getNowTimeStr();
        stringRedisTemplate.opsForValue().set(ConstantUtil.miaosha_now_time,nowTimeStr);

        //将上一秒杀时间段的商品删除
        String nextTime = TimeUtil.getNextTime(-1);
        stringRedisTemplate.delete(ConstantUtil.miaosha_startTime+"_"+nextTime);

        //删除缓存
        stringRedisTemplate.delete("miaosha::list");
    }
}
