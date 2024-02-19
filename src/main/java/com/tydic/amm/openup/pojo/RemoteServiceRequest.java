package com.tydic.amm.openup.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class RemoteServiceRequest implements Serializable {
    private String url;
    private String xml;
    private Map<String,String> headers;
}
