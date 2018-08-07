package com.example.wenzitong.untils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
       /**
        * SharePreference封装
        *
        * @author Kevin
        * @date 2015-10-17
        */
public class DbUtil {
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context ctx, String key, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void setInt(Context ctx, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(Context ctx, String key, int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    /**
     * 保存序列化对象到本地文件的方法
     */
    public static void setObject(Context ctx, String key, Object object) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        /**
         * 创建字节输出流
         */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /**
         * 创建字节对象输出流
         */
        ObjectOutputStream out = null;
        try {
            /**
             * 然后通过将字对象进行64转码，写入key值为key的sp中
             */
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从本地文件获取序列化对象方法
     */
    public static Object getObject(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 删除指定键值对的方法
     */
    public static void delete(Context ctx,String key){
        SharedPreferences sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        if(sp.contains(key)){
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.apply();
        }
    }
}

