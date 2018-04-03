package com.yitaqi.common;

import com.yitaqi.common.json.JsonWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-14 11:07
 */
public class JsonUtil {

    public static String toJson(Object object) {

        Map<String, Object> item = new HashMap<String, Object>(2);
        item.put("TYPE", false);
        item.put(JsonWriter.SKIP_NULL_FIELDS, true);
        String json = JsonWriter.objectToJson(object, item);
        return json;
    }
}
