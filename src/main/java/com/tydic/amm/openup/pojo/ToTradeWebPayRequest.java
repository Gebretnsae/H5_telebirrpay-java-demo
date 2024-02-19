package com.tydic.amm.openup.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ToTradeWebPayRequest implements Serializable {

    private String appId;
    private String notifyUrl;
    private String returnUrl;
    private String subject;
    private String outTradeNo;
    private String timeoutExpress;
    private String totalAmount;
    private String shortCode;
    private String receiveName;
    private String timestamp;
    private String nonce;

}
