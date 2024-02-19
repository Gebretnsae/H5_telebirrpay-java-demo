package com.tydic.amm.openup.utils;

import java.util.Map;
import java.util.stream.Stream;

/**
 * 
 * <p>
 * 2020/11/19 10:47
 *
 * @author lizhian
 */
public class CollUtil extends cn.hutool.core.collection.CollUtil {

    public static Map<String, String> zip(String keys, String values) {
        if (StrUtil.isBlank(keys))
            return newHashMap();
        return keys.contains(",")
                ? zip(keys, values, ",", false)
                : zip(keys, values, ";", false);
    }


    public static <T> Stream<T> stream(T[] array) {
        return CollUtil.newArrayList(array).stream();
    }
}
