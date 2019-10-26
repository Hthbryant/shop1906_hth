package com.qf.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Orders;
import com.qf.service.IOrderService;
import com.qf.util.AliPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("/aliPay")
    public  String aliPay(Integer oid, HttpServletResponse response){

        //根据订单id查询订单信息
        Orders orders =  orderService.queryOrderByOid(oid);
        System.out.println("该订单为：");
        System.out.println(orders);

        //获得支付宝核心对象
        AlipayClient alipayClient = AliPayUtil.getAliPayClient();//获得初始化的AlipayClient

        //调用支付接口完成支付
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://www.baidu.com");
        alipayRequest.setNotifyUrl("http://www.baidu.com");//在公共参数中设置回跳和通知地址

        //支付宝的请求参数
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orders.getOid()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+orders.getOrderPrice().doubleValue()+"," +
                "    \"subject\":\""+orders.getOrderDetails()+"\"," +
                "    \"body\":\""+orders.getOrderDetails()+"\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
