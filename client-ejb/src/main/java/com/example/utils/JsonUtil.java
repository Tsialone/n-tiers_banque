package com.example.utils;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class JsonUtil {

    private JsonUtil() {
    }

  
    public static JsonObject error(String message) {
        return Json.createObjectBuilder()
                .add("error", message)
                .build();
    }

  
    public static JsonObject create(String key, String value) {
        return Json.createObjectBuilder()
                .add(key, value)
                .build();
    }
}
