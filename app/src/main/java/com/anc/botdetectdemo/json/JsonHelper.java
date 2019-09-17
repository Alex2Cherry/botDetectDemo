package com.anc.botdetectdemo.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JsonHelper {
    public static final JSONObject EMPTY_OBJECT = new JSONObject();

    public static JSONObject mergeFrom(JSONObject to, JSONObject from) {
        Iterator<String> iterator = from.keys();

        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                to.put(key, from.get(key));
            } catch (JSONException e) {
            }
        }

        return to;
    }
}
