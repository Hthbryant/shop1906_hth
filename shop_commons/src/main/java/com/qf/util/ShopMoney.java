package com.qf.util;

import com.qf.entity.Shopcart;

import java.math.BigDecimal;
import java.util.List;

public class ShopMoney {

    public static List<Shopcart> countMoney(List<Shopcart> shopcarts){
        if(shopcarts!=null){
            for (Shopcart shopcart : shopcarts) {
                BigDecimal num = BigDecimal.valueOf(shopcart.getNumber());
                BigDecimal danPrice = shopcart.getGoods().getPrice();
                BigDecimal shopMoney = num.multiply(danPrice);
                shopcart.setShopPrice(shopMoney);
            }
        }
        return shopcarts;
    }
    public static Shopcart countPrice(Shopcart shopcart){

                BigDecimal num = BigDecimal.valueOf(shopcart.getNumber());
                BigDecimal danPrice = shopcart.getCartPrice();
                BigDecimal shopMoney = num.multiply(danPrice);
                shopcart.setShopPrice(shopMoney);

        return shopcart;
    }

}
