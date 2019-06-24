package com.zbcm.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbcm on 2017-09-14.
 */
public class Utils {
    private Utils() {
        throw new AssertionError();
    }

    public static JSONArray getObject(String jsonStr,String content) {
        JSONObject object = JSONObject.fromObject(jsonStr);
        JSONArray array = object.getJSONArray(content);
        return array;
    }
}
