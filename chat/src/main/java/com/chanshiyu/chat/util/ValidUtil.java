package com.chanshiyu.chat.util;

/**
 * @author SHIYU
 * @description
 * @since 2021/1/1 16:04
 */
public class ValidUtil {

    private static final String nameOrPWReg = "^[a-zA-Z0-9._-]{3,12}$";

    private static final String contentReg = "^[a-zA-Z0-9._-\\u4e00-\\u9fa5]{1,12}$";

    public static boolean validNameOrPW(String str) {
        return str.matches(nameOrPWReg);
    }

    public static boolean validContent(String str) {
        return str.matches(contentReg);
    }

}
