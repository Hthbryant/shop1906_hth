package com.qf.service;

import com.qf.entity.Shopcart;
import com.qf.entity.User;

import java.util.List;

public interface ICartService {

   

    String insertCart(String cartToken, Integer gid, Integer gnumber, User user);

    List<Shopcart> getCart(String cartToken, User user);

}
