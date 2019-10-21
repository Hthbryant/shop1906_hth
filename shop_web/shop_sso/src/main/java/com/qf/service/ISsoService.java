package com.qf.service;

import com.qf.entity.ResultData;
import com.qf.entity.User;

public interface ISsoService {

    User queryUserByName(String username);

    ResultData sendEmail(String username);

    int updatePassword(User user);

    int register(User user);

    User loginByNamePwd(String username, String password);
}
