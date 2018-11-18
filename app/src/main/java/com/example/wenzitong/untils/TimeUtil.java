package com.example.wenzitong.untils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 邹特强 on 2018/11/4.
 *
 */

public class TimeUtil {
    /**
     * 获取当前系统日期时间，格式为：20181104
     */
    public static String getCurTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmdd");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

}
