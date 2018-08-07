package com.example.wenzitong.untils;

import com.example.wenzitong.R;
import com.example.wenzitong.application.MyApp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 邹特强 on 2018/3/26.
 * 检测密码是否为6到16位,并且是否由数字和密码组成的工具类
 * 算法：检测数字，字母，其他符号三种字符的数量，必须包含数字和字母，可包含字符
 */

public class PasswordCheckUtil {
    private static final int MAX_PASSWORD_LENGTH = 16;
    private static final int MIN_PASSWORD_LENGTH = 6;

    public static boolean checkPassword(String password) {
        /**
         * 如果密码包含空格，直接显示不合格
         */
        if (password.length() <MIN_PASSWORD_LENGTH && password.length() > MAX_PASSWORD_LENGTH) {
            ToastUtil.ToastShortShow(MyApp.getGlobalContext().getText(R.string.input_6to16_password).toString(), MyApp.getGlobalContext());
            return false;
        } else if (password.contains(" ")) {
            ToastUtil.ToastShortShow("密码不能包含空格！", MyApp.getGlobalContext());
            return false;
        } else {
            int numLength = 0;
            int letterLength = 0;
            /**
             * 检测数字出现的个数
             */
            String sift = "[0-9]";
            Pattern pattern = Pattern.compile(sift);
            Matcher matcher = pattern.matcher(password);
            while (matcher.find()) {
                numLength++;
            }
            /**
             * 检测字母出现的个数
             */+
            sift = "[A-Za-z]";
            pattern = Pattern.compile(sift);
            matcher = pattern.matcher(password);
            while (matcher.find()){
                letterLength ++;
            }
            if(numLength>0&&letterLength>0){
                return true;
            }else {
                ToastUtil.ToastShortShow("密码必须包含数字和字母！",MyApp.getGlobalContext());
                return false;
            }

        }

    }
}
