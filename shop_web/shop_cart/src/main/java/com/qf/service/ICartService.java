package com.qf.service;

import com.qf.entity.Address;
import com.qf.entity.Shopcart;
import com.qf.entity.User;

import java.util.List;

public interface ICartService {

   

    String insertCart(String cartToken, Integer gid, Integer gnumber, User user);

    List<Shopcart> getCart(String cartToken, User user);

    void merge(String cartToken, User user);

    List<Address> getUserAddress(Integer uid);

    List<Shopcart> getCartByIds(Integer uid,Integer[] checkone);

    int deleteById(Integer id);
}
