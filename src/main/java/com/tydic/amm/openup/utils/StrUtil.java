package com.tydic.amm.openup.utils;

/**
 * 
 * <p>
 * 2020/11/19 10:51
 *
 * @author lizhian
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {


    public static String subIfSurround(CharSequence str, CharSequence chars) {
        if (isSurround(str, chars, chars)) {
            int charsLength = chars.length();
            return sub(str, charsLength, str.length() - charsLength);
        }
        return str.toString();
    }
    public static boolean hasText(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            int strLen = str.length();
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    public static String reverse(String str, int index) {
        if (isEmpty(str) && str.length() < index) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str.substring(0, index));
        stringBuffer.append(reverse(str.substring(index)));
        return stringBuffer.toString();
    }

}
