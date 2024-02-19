package com.tydic.amm.openup.utils;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HttpResponse {
    @Getter
    private final ResponseEntity<String> responseEntity;

    public boolean is2xxSuccessful() {
        return Optional.ofNullable(responseEntity)
                .map(ResponseEntity::getStatusCode)
                .map(HttpStatus::is2xxSuccessful)
                .orElse(false);
    }

    public String getBody() {
        return Optional.ofNullable(responseEntity)
                .map(ResponseEntity::getBody)
                .orElse("");
    }


    public <T> T bodyToBean(Class<T> clazz) {
        String body = this.getBody();
        if (StrUtil.isBlank(body)) {
            return null;
        }
        return JsonUtil.parse(responseEntity.getBody(), clazz);
    }

    public JSON bodyToJSON() {
        String body = this.getBody();
        if (StrUtil.isBlank(body)) {
            return new JSONObject();
        }
        return JSONUtil.parse(body);
    }


    public JSONObject bodyToJSONObject() {
        String body = this.getBody();
        if (StrUtil.isBlank(body)) {
            return new JSONObject();
        }
        return JSONUtil.parseObj(body);
    }

    public JSONArray bodyToJSONArray() {
        String body = this.getBody();
        if (StrUtil.isBlank(body)) {
            return new JSONArray();
        }
        return JSONUtil.parseArray(body);
    }

    public Map<String, Object> xmlToMap() {
        String body = this.getBody();
        if (StrUtil.isBlank(body)) {
            return new HashMap<>();
        }
        return XmlUtil.xmlToMap(body);
    }

    public JSONObject xmlToJSONObject() {
        String body = this.getBody();
        if (StrUtil.isBlank(body)) {
            return new JSONObject();
        }
        return JSONUtil.xmlToJson(body);
    }

    public <T> T xmlToBean(Class<T> clazz) {
        JSONObject jsonObject = this.xmlToJSONObject();
        if (jsonObject == null) {
            return null;
        }
        return JsonUtil.parse(jsonObject.toString(), clazz);
    }


    public <T> T bodyToBeanIfSuccessful(Class<T> clazz) {
        if (!this.is2xxSuccessful()) {
            return null;
        }
        return this.bodyToBean(clazz);
    }


    public JSON bodyToJSONIfSuccessful() {
        if (!this.is2xxSuccessful()) {
            return null;
        }
        return this.bodyToJSON();
    }


    public JSONObject bodyToJSONObjectIfSuccessful() {
        if (!this.is2xxSuccessful()) {
            return null;
        }
        return this.bodyToJSONObject();
    }

    public JSONArray bodyToJSONArrayIfSuccessful() {
        if (!this.is2xxSuccessful()) {
            return null;
        }
        return this.bodyToJSONArray();
    }

    //响应体为xml
    public Map<String, Object> xmlToMapIfSuccessful() {
        if (!this.is2xxSuccessful()) {
            return null;
        }
        return this.xmlToMap();
    }

    public JSONObject xmlToJSONObjectIfSuccessful() {
        if (!this.is2xxSuccessful()) {
            return null;
        }
        return this.xmlToJSONObject();
    }

    public <T> T xmlToBeanIfSuccessful(Class<T> clazz) {
        if (!this.is2xxSuccessful()) {
            return null;
        }
        return this.xmlToBean(clazz);
    }

}
