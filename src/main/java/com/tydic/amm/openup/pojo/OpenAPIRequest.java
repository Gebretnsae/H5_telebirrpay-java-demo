package com.tydic.amm.openup.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OpenAPIRequest implements Serializable {
    private String appid;
    private String sign;
    private String ussd;
}
