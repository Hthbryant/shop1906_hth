package com.qf.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Orders;
import com.qf.service.IOrderService;
import com.qf.util.AliPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("/aliPay")
    public  void aliPay(Integer oid, HttpServletResponse response){

        //根据订单id查询订单信息
        Orders orders =  orderService.queryOrderByOid(oid);
        System.out.println("该订单为：");
        System.out.println(orders);

        //获得支付宝核心对象
        AlipayClient alipayClient = AliPayUtil.getAliPayClient();//获得初始化的AlipayClient

        //调用支付接口完成支付
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://localhost:16666/order/showOrder");
        alipayRequest.setNotifyUrl("http://274w65v220.qicp.vip/pay/payCallBack");//在公共参数中设置回跳和通知地址

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
    }

    @RequestMapping("/payCallBack")
    @ResponseBody
    public String payCallBack(
            String charset,
            String out_trade_no,
            String trade_status,
            String sign_type,
            HttpServletRequest request){


        System.out.println("进来了这个方法");
        System.out.println("charset = " + charset);
        System.out.println("out_trade_no = " + out_trade_no);
        System.out.println("trade_status = " + trade_status);
        System.out.println("sign_type = " + sign_type);

        System.out.println("*********************************");
        Map<String ,String> map = new HashMap<>();
        Map<String ,String[]> requestMap  =  request.getParameterMap();
        for (Map.Entry<String, String[]> m : requestMap.entrySet()) {
            System.out.println(m.getKey()+" : "+ Arrays.toString(m.getValue()));
            map.put(m.getKey(),m.getValue()[0]);
        }
//        map.remove("sign");
//        map.remove("sign_type");

        //业务判断 验签
        try {
            boolean flag = AlipaySignature.rsaCheckV1(
                    map,
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB",
                    charset,
                    sign_type
            );
            System.out.println("验证的结果："+flag);
            if(flag){
                // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
                //对该订单的状态进行修改
                System.out.println("验签成功");
                if(trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED")){
                    int result =  orderService.updateOrderByOid(out_trade_no);
                    if(result>0){
                        System.out.println("订单状态已修改");
                        return "success";
                    }
                }

            }else{
                System.out.println("验签失败");
                // TODO 验签失败则记录异常日志，并在response中返回failure.
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "failure";
    }
}
