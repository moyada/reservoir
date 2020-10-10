package com.moyada.reservoir.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Slf4j
public class JsonUtil {

    public static String toJson(Object data) {
        if (data instanceof String) {
            return (String) data;
        }

        String value;
        try {
            value = JSONObject.toJSONString(data);
        } catch (JSONException e) {
            log.error("JSON format error, data: " + data, e);
            return null;
        }
        return value;
    }

    public static <T> T toObject(String data, Class<T> clz) {
        try {
            return JSONObject.parseObject(data, clz);
        } catch (Exception e) {
            log.error("JSON parse error, data: " + data, e);
            return null;
        }
    }

    public static <T> List<T> toList(String data, Class<T> type) {
        List<T> value;
        try {
            value = JSONObject.parseArray(data, type);
        } catch (JSONException e) {
            log.error("JSON array parse error, data: " + data, e);
            return null;
        }
        return value;
    }
}
