package com.tydic.amm.openup.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ToTradeWebPayResponse implements Serializable {
    private String toPayUrl;
}
