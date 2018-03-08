package com.diyiliu.support.config;

import com.diyiliu.support.model.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Constant
 * Author: DIYILIU
 * Update: 2018-03-07 15:24
 */
public class Constant {

    public static Map<String, Pair>  insideCacheProvider = new ConcurrentHashMap();
    public static Map<String, Pair>  outsideCacheProvider = new ConcurrentHashMap();
}
