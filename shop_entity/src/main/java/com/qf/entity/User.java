package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    protected Integer id;
    protected Integer status =0;
    protected Date createTime = new Date();
    private String username;
    private String password;
    private String email;
    private String phone;
    private String nickname;


}
