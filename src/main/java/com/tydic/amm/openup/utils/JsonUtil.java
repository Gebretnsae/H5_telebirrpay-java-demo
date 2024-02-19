package com.tydic.amm.openup.utils;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;


/**
 * Json
 * <p>
 * 2020/11/19 10:51
 *
 * @author lizhian
 */
public class JsonUtil {
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final String EMPTY_STRING = "";
    private static volatile ObjectMapper MAPPER = new ObjectMapper();

    public static void setMapper(ObjectMapper mapper) {
        MAPPER = mapper;
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    @SneakyThrows
    public static String toJsonString(Object object) {
        if (object == null) {
            return EMPTY_STRING;
        }
        if (object instanceof CharSequence) {
            return object.toString();
        }
        String string = MAPPER.writeValueAsString(object);
        return StrUtil.subIfSurround(string, "\"");
    }


    @SneakyThrows
    public static byte[] toJSONBytes(Object object) {
        if (object == null) {
            return EMPTY_ARRAY;
        }
        if (object instanceof CharSequence) {
            return object.toString().getBytes();
        }
        return MAPPER.writeValueAsBytes(object);
    }

    public static JSON parse(Object object) {
        return JSONUtil.parse(toJsonString(object));
    }

    public static JSONObject parse(byte[] bytes) {
        return JSONUtil.parseObj(bytes);
    }

    @SneakyThrows
    public static <T> T parse(String jsonString, Class<T> clazz) {
        if (clazz.equals(String.class)) {
            return (T) jsonString;
        }
        String s = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sel=\"http://services.multichoice.co.za/SelfCare\" xmlns:sel1=\"http://datacontracts.multichoice.co.za/SelfCare\" xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\"><soapenv:Header/><soapenv:Body><sel:GetSmartQuote><sel:smartQuoteRequest><sel1:Country>Ethiopia</sel1:Country><sel1:BasketId></sel1:BasketId><sel1:DeviceNumber>7029856682</sel1:DeviceNumber><sel1:ProductUserKey><arr:string>?</arr:string></sel1:ProductUserKey><sel1:InvoicePeriod>1</sel1:InvoicePeriod><sel1:QuotePeriod>1</sel1:QuotePeriod><sel1:City>?</sel1:City><sel1:BoxOffice>false</sel1:BoxOffice><sel1:InterfaceType>TeleBirr</sel1:InterfaceType><sel1:BusinessUnit>DStv</sel1:BusinessUnit></sel:smartQuoteRequest><sel:vendorCode>Telebirr_Dstv</sel:vendorCode><sel:language>Eng</sel:language><sel:ipAddress>?</sel:ipAddress></sel:GetSmartQuote></soapenv:Body></soapenv:Envelope>";
    return MAPPER.readValue(jsonString, clazz);
    }

    @SneakyThrows
    public static <T> T parse(String jsonString, TypeReference<T> typeReference) {
        return MAPPER.readValue(jsonString, typeReference);
    }

    @SneakyThrows
    public static <T> T parse(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        return MAPPER.readValue(bytes, clazz);
    }

    public static <T> T transform(Object object, Class<T> clazz) {
        return parse(toJsonString(object), clazz);
    }

    @SneakyThrows
    public static JsonNode readTree(Object object) {
        return MAPPER.readTree(toJsonString(object));
    }


}
