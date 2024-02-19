package com.tydic.amm.openup.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class HttpServletRequestReader {
    public static String readInputStream(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        BufferedReader buf = null;
        try {
            buf = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while (null != (line = buf.readLine())) {
                sb.append(line);
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        } catch (Exception e) {
            log.error("MM notify message parse request error.", e);
            return null;
        } finally {
            try {
                buf.close();
            } catch (Exception e) {
                log.error("MM notify message parse request error.", e);
            }
        }
        return null;
    }
}
