package com.sunjet.utils.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by lhj on 16/10/19.
 */
public class GsonHelper {

    /**
     * Bean转Json
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static String bean2Json(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * Json转Bean
     *
     * @param jsonStr
     * @param objClass
     * @param <T>
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, objClass);
    }
}
