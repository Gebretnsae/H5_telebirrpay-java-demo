package com.tydic.amm.openup.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openup")
@Data
public class OpenupConfig {
    private String appid;
    private String appkey;
    private String publickey;
    private String notifyUrl;
    private String returnUrl;
    private String timeoutExpress;
    private String shortCode;
    private String receiveName;
    private String toTradeWebPay;
}
