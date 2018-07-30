package com.yb.fish.collection;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/**
* ArrayUtil
* @author bing(maintenance)
* @create 2018/5/23
* @version 1.0
**/
public final class ArrayUtil {

    private ArrayUtil() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 按入参size拆分集合
     *
     * @param datas 被裁分list
     * @param splitSize 份数
     * @param <T> any
     * @return List splitSize份list结果
     */
    public static <T> List<List<T>> arrayChunk(List<T> datas, int splitSize) {
        if (datas == null || splitSize < 1) {
            return Collections.emptyList();
        }
        int totalSize = datas.size();
        int count = (totalSize % splitSize == 0) ? (totalSize / splitSize) : (totalSize / splitSize + 1);

        List<List<T>> rows = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            List<T> cols = datas.subList(i * splitSize, (i == count - 1) ? totalSize : splitSize * (i + 1));
            rows.add(cols);
        }
        return rows;
    }


    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(JSONObject obj) {
        return obj == null || obj.isEmpty();
    }

}