package com.anc.botdetectdemo.json;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayBuilder {
        private List<Object> mValues;

        public JsonArrayBuilder() {
            mValues = new ArrayList<>();
        }

        public <T> JsonArrayBuilder add(T value) {
            mValues.add(value);
            return this;
        }

        public int size() {
            return mValues.size();
        }

        public JSONArray toJson() {
            return new JSONArray(mValues);
        }
    }