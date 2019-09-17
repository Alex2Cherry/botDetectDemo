package com.anc.botdetectdemo.json;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectBuilder {
        private JSONObject mJson;

        public JsonObjectBuilder() {
            this(new JSONObject());
        }

        public JsonObjectBuilder(JSONObject json) {
            mJson = json;
        }

        public <T> JsonObjectBuilder add(String key, T value) {
            try {
                mJson.put(key, value);
            } catch (JSONException e) {
            }

            return this;
        }

        public JsonObjectBuilder addFrom(JSONObject from) {
            JsonHelper.mergeFrom(mJson, from);
            return this;
        }

        public JSONObject toJson() {
            return mJson;
        }
    }