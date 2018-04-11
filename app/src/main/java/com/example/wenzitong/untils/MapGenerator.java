package com.example.wenzitong.untils;

import java.util.HashMap;

/**
 * Created by 邹特强 on 2018/2/11.
 * 利用builder设计模式快速生成Map的工具类
 * 目前只是为了方便增加数据
 * @author 邹特强
 */

public class MapGenerator extends HashMap<String,String> {
    public static MapGenerator generate(){
        return new MapGenerator();
    }
    public MapGenerator add(String key, String value) {
        this.put(key,value);
        return this;
    }
}
