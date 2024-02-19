package com.tydic.amm.openup.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class HttpRequest {

    private final RestTemplate restTemplate;


    /**
     * Fill the placeholder above the url
     * http://baidu.com/{key} -> http://baodu.com/value
     */
    private final Map<String, String> uriVars = new HashMap<>();
    /**
     * parameter
     * http://baidu.com/query?A=a&B=b1&B=b2
     */
    private final MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    
    private final HttpHeaders headers = new HttpHeaders();
    
    private boolean debug = false;
    
    private String url;
    
    private HttpMethod method = HttpMethod.GET;

    
    private Object requestBody;

    public HttpRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpRequest debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public HttpRequest url(String url) {
        this.url = url;
        return this;
    }

    public HttpRequest method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpRequest method(String method) {
        return this.method(HttpMethod.resolve(method));
    }

    public HttpRequest uriVars(Map<String, String> uriVars) {
        this.uriVars.clear();
        if (CollUtil.isEmpty(uriVars)) {
            return this;
        }
        this.uriVars.putAll(uriVars);
        return this;
    }

    public HttpRequest uriVar(String key, String value) {
        if (StrUtil.isBlank(key) || StrUtil.isBlank(value)) {
            return this;
        }
        this.uriVars.put(key, value);
        return this;
    }

    public HttpRequest parameters(MultiValueMap<String, Object> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
        return this;
    }


    public HttpRequest parameter(String key, List<Object> values) {
        if (StrUtil.isBlank(key)) {
            return this;
        }
        this.parameters.put(key, values);
        return this;
    }

    public HttpRequest parameter(String key, Object... values) {
        return this.parameter(key, CollUtil.newArrayList(values));
    }

    public HttpRequest headers(HttpHeaders headers) {
        this.headers.clear();
        this.headers.putAll(headers);
        return this;
    }


    public HttpRequest header(String key, List<String> values) {
        if (StrUtil.isBlank(key)) {
            return this;
        }
        this.headers.put(key, values);
        return this;
    }

    public HttpRequest header(String key, String... values) {
        return this.header(key, CollUtil.newArrayList(values));
    }

    public HttpRequest requestBody(Object requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    private String toParameterStr() {
        return parameters.entrySet()
                .stream()
                .filter(entry -> StrUtil.isNotBlank(entry.getKey()))
                .flatMap(entry -> {
                    String key = entry.getKey();
                    return entry.getValue()
                            .stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .filter(StrUtil::isNotBlank)
                            .map(value -> key + "=" + value);
                }).collect(Collectors.joining("&"));
    }

    public HttpResponse execute() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(this.requestBody, this.headers);
        String parameterStr = toParameterStr();
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                this.url + "?" + parameterStr
                , this.method
                , httpEntity
                , String.class
                , this.uriVars
        );
        HttpResponse httpResponse = new HttpResponse(responseEntity);
//        if (debug) {
//            log.debug("HttpRequest {} {}        uriVars : {}     parameters : {}        headers : {}    requestBody : {} responseStatus : {}   responseBody : {}", this.method, this.url, this.uriVars, parameterStr, this.headers, this.requestBody, httpResponse.getResponseEntity().getStatusCode(), httpResponse.getBody().replaceAll("\r","").replaceAll("\n",""));
//        }
        return httpResponse;
    }

    public HttpResponse get() {
        return this.method(HttpMethod.GET).execute();
    }

    public HttpResponse post() {
        return this.method(HttpMethod.POST).execute();
    }

    public HttpResponse delete() {
        return this.method(HttpMethod.DELETE).execute();
    }

    public HttpResponse put() {
        return this.method(HttpMethod.PUT).execute();
    }
}
