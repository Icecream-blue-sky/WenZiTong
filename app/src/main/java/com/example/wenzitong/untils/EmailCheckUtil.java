package com.example.wenzitong.untils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 邹特强 on 2018/2/5.
 * 检查邮箱地址是否有效的工具类
 */

public class EmailCheckUtil {
    private static Pattern EMAILPATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    public static boolean checkEmail(String emailAddress) {
        if (null == emailAddress || "".equals(emailAddress)) {
            return false;
        }
        Matcher m = EMAILPATTERN.matcher(emailAddress);
        return m.matches();
    }
}