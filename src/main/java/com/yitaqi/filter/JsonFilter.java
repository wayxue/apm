package com.yitaqi.filter;

import com.yitaqi.common.JsonUtil;
import com.yitaqi.interfaces.IFilter;

import java.io.Serializable;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-15 8:39
 */
public class JsonFilter implements IFilter {

    @Override
    public Object doFilter(Object value) {

        if (value == null) {
            return null;
        } else if (!(value instanceof Serializable)) {
            return value;
        }
        String json = JsonUtil.toJson(value);
        return json;
    }
}
