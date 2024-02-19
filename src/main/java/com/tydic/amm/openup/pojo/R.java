package com.tydic.amm.openup.pojo;

import lombok.Data;
import org.springframework.util.MultiValueMap;
import java.io.Serializable;
import java.util.Map;

@Data
public class R<T> implements Serializable {
    private int code;
    private T data;
    private String message;
}
