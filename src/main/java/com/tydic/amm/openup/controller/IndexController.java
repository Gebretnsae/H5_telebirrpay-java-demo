package com.tydic.amm.openup.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tydic.amm.openup.config.OpenupConfig;
import com.tydic.amm.openup.pojo.OpenAPIRequest;
import com.tydic.amm.openup.pojo.R;
import com.tydic.amm.openup.pojo.ToTradeWebPayRequest;
import com.tydic.amm.openup.pojo.ToTradeWebPayResponse;
import com.tydic.amm.openup.utils.DigestUtil;
import com.tydic.amm.openup.utils.HttpServletRequestReader;
import com.tydic.amm.openup.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    private HttpServletResponse response;
    private static final String APP_KEY = "appKey";
    @Autowired
    private OpenupConfig config;

    @GetMapping(value = "")
    public String index() {
        return "/toTradeWebPay";
    }

    RestTemplate restTemplate = new RestTemplate();

    @PostMapping(value = "/submit")
    @ResponseBody
    public void submit(String receiveName, String outTradeNo, String shortCode, String subject,
                       String totalAmount,
                       String notifyUrl,
                       String returnUrl,
                       String appid,
                       String appKey,
                       String publickey,
                       String timeoutExpress,
                       String nonce,
                       String timestamp) {

        String threadId = String.valueOf(System.currentTimeMillis());
        ToTradeWebPayRequest trade = new ToTradeWebPayRequest();
        trade.setTotalAmount(totalAmount);
        if (StringUtils.isEmpty(appid)) {// The actual development and configuration items are here to be compatible with the demo operation
            appid = config.getAppid();
        }
        trade.setAppId(appid);
        trade.setNonce(nonce);
        if (StringUtils.isEmpty(notifyUrl)) {// The actual development and configuration items are here to be compatible with the demo operation
            trade.setNotifyUrl(config.getNotifyUrl());
        } else {
            trade.setNotifyUrl(notifyUrl);
        }
        if (StringUtils.isEmpty(returnUrl)) {// The actual development and configuration items are here to be compatible with the demo operation
            trade.setReturnUrl(config.getReturnUrl());
        } else {
            trade.setReturnUrl(returnUrl);
        }
        trade.setOutTradeNo(outTradeNo);
        trade.setSubject(subject);
        trade.setReceiveName(receiveName);

        trade.setShortCode(shortCode);
        trade.setTimeoutExpress(timeoutExpress);
        trade.setTimestamp(timestamp);

        String data = JSON.toJSONString(trade);
        SortedMap<String, String> sortedMap = JSON.parseObject(data, new TypeReference<TreeMap<String, String>>() {});
        if (StringUtils.isEmpty(appKey)) {// The actual development and configuration items are here to be compatible with the demo operation
            sortedMap.put(APP_KEY, config.getAppkey());
        } else {
            sortedMap.put(APP_KEY, appKey);
        }
        if (StringUtils.isEmpty(publickey)) {// The actual development and configuration items are here to be compatible with the demo operation
            publickey = config.getPublickey();
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (!StringUtils.isEmpty(sb.toString())) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        log.debug("serial number： {},String to be signed(StringA)：{}", threadId, sb.toString());
        String sgin = DigestUtil.toSha256(sb.toString());
        log.debug("serial number： {}, signature：{}", threadId, sgin);
        log.debug("serial number： {},Parameters before encryption：{}", threadId, data);
        String ussd = RSAUtils.encryptByPublicKey(data, publickey);
        log.debug("serial number： {},Encrypted parameters(ussd)：{}", threadId, ussd);
        OpenAPIRequest request = new OpenAPIRequest();
        request.setAppid(trade.getAppId());
        request.setUssd(ussd);
        request.setSign(sgin);
        log.debug("serial number： {},request parameter：{}", threadId, JSON.toJSONString(request));
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(config.getToTradeWebPay(), request, String.class);
        String url = null;
        PrintWriter out = null;
        try {
            R<ToTradeWebPayResponse> r = JSON.parseObject(responseEntity.getBody(), new TypeReference<R<ToTradeWebPayResponse>>() {});
            log.debug("serial number： {},response parameter：{}", threadId, JSON.toJSONString(r));
            response.setContentType("text/html; charset=utf-8;");
            out = response.getWriter();
            if (r.getCode() == 200) {
                url = r.getData().getToPayUrl();
                out.print("<script>window.location.href='" + url + "'</script>");
            } else {
                out.print("<script>alert('" + r.getMessage() + "');history.go(-1);</script>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {

            }
        }
    }


    @PostMapping(value = "/getData")
    @ResponseBody
    public Map<String, String> getData(String receiveName, String outTradeNo, String shortCode, String subject,
                                       String totalAmount,
                                       String notifyUrl,
                                       String returnUrl,
                                       String appid,
                                       String timeoutExpress,
                                       String nonce,
                                       String timestamp) {
        String threadId = String.valueOf(System.currentTimeMillis());
        ToTradeWebPayRequest trade = new ToTradeWebPayRequest();
        trade.setTotalAmount(totalAmount);
        trade.setAppId(appid);
        trade.setNonce(nonce);
        if (StringUtils.isEmpty(notifyUrl)) {
            trade.setNotifyUrl(config.getNotifyUrl());
        } else {
            trade.setNotifyUrl(notifyUrl);
        }
        if (StringUtils.isEmpty(returnUrl)) {
            trade.setReturnUrl(config.getReturnUrl());
        } else {
            trade.setReturnUrl(returnUrl);
        }
        trade.setOutTradeNo(outTradeNo);
        trade.setSubject(subject);
        trade.setReceiveName(receiveName);

        trade.setShortCode(shortCode);
        trade.setTimeoutExpress(timeoutExpress);
        trade.setTimestamp(timestamp);
        String data = JSON.toJSONString(trade);
        SortedMap<String, String> sortedMap = JSON.parseObject(data, new TypeReference<TreeMap<String, String>>() {});
        sortedMap.put(APP_KEY, config.getAppkey());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (!StringUtils.isEmpty(sb.toString())) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        log.debug("serial number： {},String to be signed(StringA)：{}", threadId, sb.toString());
        String sgin = DigestUtil.toSha256(sb.toString());
        log.debug("serial number： {}, signature：{}", threadId, sgin);
        log.debug("serial number： {},Parameters before encryption：{}", threadId, data);
        String ussd = RSAUtils.encryptByPublicKey(data, config.getPublickey());
        log.debug("serial number： {},Encrypted parameters(ussd)：{}", threadId, ussd);
        OpenAPIRequest request = new OpenAPIRequest();
        request.setAppid(config.getAppid());
        request.setUssd(ussd);
        request.setSign(sgin);
        log.debug("serial number： {},request parameter：{}", threadId, JSON.toJSONString(request));
        Map<String, String> result = new HashMap<>();
        result.put("sign", sb.toString());
        result.put("ussd", data);
        result.put("encode", JSON.toJSONString(request));
        return result;
    }


    /**
     * Payment Notify
     *
     * @return
     */
    @PostMapping("/payNotify")
    @ResponseBody
    public R asyncNotify() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String reqeustNotifyData = HttpServletRequestReader.readInputStream();
        log.info("Received asynchronous notification (ciphertext)：{}", reqeustNotifyData);
        log.info("Received asynchronous notification (plaintext)：{}", RSAUtils.decryptByPublicKey(reqeustNotifyData, config.getPublickey()));
        R result = new R();
        result.setCode(0);
        result.setMessage("success");
        return result;
    }

    private String getNonce() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String getOutTradeNo() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {

    }
}
