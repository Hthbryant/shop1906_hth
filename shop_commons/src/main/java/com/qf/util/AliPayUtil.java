package com.qf.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

public class AliPayUtil {

    private static AlipayClient  alipayClient = null;
    private static String APP_ID ="2016101200666958";
    private static String APP_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKU/x1bJ/v8uO5x+GdUzX640t4YjMpXWQdWVp64H1APDNf3kVANNxgaCh1QVtEzKVPb1hzhVQxikpB8dgOsxqg9Kv1J18pO6IIygxECxLeuTahDbTOxge+UtjcZGGLaymSedO8em3unN2iZQgebDaNNtpEP4toALKWnWizzM8zhvAgMBAAECgYEAgUANbni4FmfXAGQxWGzCXcOl+Cj3m/Lls2VGrfw5fGfnhxpcC+l9BgJ6Tu5bA+UN58vLY+hsfo3oQN4Ww98on2Jy5meCVAfd1jnwT5aUqtkxGjBvnK9VvzynLnlwhaUhSNz08aNEz9ddSo/4PesN3LYfX5GeIZHfYLNlMlgcbkECQQDf5SCZr2/tDYNiNt+fj0UKU0IVnJAqCzrFS/YnM6WpgQ2UoW/PC7CN8xexp0WvQ/LRao7vqEpKLOpiixYIrespAkEAvPHXwQKymk3046qEc/F3YafSI/1I0hXmL2le+F1uGSQEjC3/RAvIJCteD6XXdV8HyNAQAPn6TbJaFUMasb0R1wJAT0dswb6TXVcZnJ9dk7xBcCSHE+v6SEjtqIr7QGpTUK/3xRdc5e13uOvJCgj27dablneq4UWv+I/q9rXVOYGL8QJBAJSaQO50iLQDjA9jFTl7OEF+FDBqDNPulVPEtATytpzfTxRROOrzuT15PklgTczqKN0vhua9tLdXBrKiTGRuJ5ECQQDC8x61WeUvs/DXA5Srrq70dufKNnI7OXwxv8GHl+Lht1hdHH+I+GaErMhwL/qPn1GO99EQ70n9poGXgCNhIu2l";
    private static String FORMAT = "json";
    private static String CHARSET = "UTF-8";
    private static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";
    private static String SIGN_TYPE = "RSA";


    static {
        alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                APP_ID,
                APP_PRIVATE_KEY,
                FORMAT,
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGN_TYPE); //获得初始化的AlipayClient
    }

    public static AlipayClient getAliPayClient(){
        return alipayClient;
    }
}
