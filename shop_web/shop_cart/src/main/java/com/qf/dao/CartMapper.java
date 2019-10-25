package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Shopcart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper extends BaseMapper<Shopcart> {

    List<Shopcart> queryCartByIds(@Param("uid") Integer uid,@Param("ids") Integer[] ids);
}
