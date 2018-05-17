package cn.zemic.hy.display.unmannedstoragedisplay.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fxs
 */
public class GsonUtils {


    private static class GsonHandler {
        private static Gson gson = new Gson();
    }

    private static Gson getInstance() {
        return GsonHandler.gson;
    }

    /**
     * 将object对象转成json字符串
     *
     * @param object object
     * @return json
     */
    public static String jsonToString(Object object) {
        return getInstance().toJson(object);
    }


    /**
     * 将jsonString转成泛型bean
     *
     * @param str jsonString
     * @param cls bean类
     * @return 泛型
     */
    public static <T> T jsonToBean(String str, Class<T> cls) {
        return getInstance().fromJson(str, cls);
    }


    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     *
     * @param str 字符串
     * @return list集合
     */
    public static <T> List<T> jsonToList(String str) {
        return getInstance().fromJson(str, new TypeToken<List<T>>() {
        }.getType());
    }


    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json 字符串
     * @param cls  bean类
     * @param <T>  list集合
     * @return list集合
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = getInstance();
        List<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param str 字符串
     * @return list中有map的
     */
    public static <T> List<Map<String, T>> jsonToListMaps(String str) {
        return getInstance().fromJson(str,
                new TypeToken<List<Map<String, T>>>() {
                }.getType());
    }


    /**
     * 转成map的
     *
     * @param str jsonString
     * @return map
     */
    public static <T> Map<String, T> jsonToMaps(String str) {
        return getInstance().fromJson(str, new TypeToken<Map<String, T>>() {
        }.getType());
    }
}

