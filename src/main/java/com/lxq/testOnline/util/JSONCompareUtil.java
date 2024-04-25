package com.lxq.testOnline.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JSONCompareUtil {

    /**
     * 比较两个JSON对象是否相等，包括嵌套的JSONObject和 JSONArray。
     *
     * @param json1 第一个JSON对象
     * @param json2 第二个JSON对象
     * @return 如果两个JSON对象相等，返回true；否则返回false。
     */
    public static boolean compareJSON(JSONObject json1, JSONObject json2) {
        // 确保两个对象都有相同的键数量
        if (json1.keySet().size() != json2.keySet().size()) {
            return false;
        }

        // 遍历第一个JSON对象的所有键
        for (String key : json1.keySet()) {
            if (!json2.containsKey(key)) {
                return false; // 如果第二个对象没有这个键，直接返回false
            }
            Object value1 = json1.get(key);
            Object value2 = json2.get(key);

            // 如果键对应的值是JSON对象或数组，则递归比较
            if ((value1 instanceof JSONObject) && (value2 instanceof JSONObject)) {
                if (!compareJSON((JSONObject) value1, (JSONObject) value2)) {
                    return false;
                }
            } else if ((value1 instanceof JSONArray) && (value2 instanceof JSONArray)) {
                if (!compareJSONArrays((JSONArray) value1, (JSONArray) value2)) {
                    return false;
                }
            } else if (!isEqual(value1, value2)) {
                // 如果不是JSON对象或数组，直接比较值
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个JSON数组是否相等，不考虑元素顺序。
     *
     * @param array1 第一个JSON数组
     * @param array2 第二个JSON数组
     * @return 如果两个JSON数组相等，返回true；否则返回false。
     */
    public static boolean compareJSONArrays(JSONArray array1, JSONArray array2) {
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();

        // 将数组中的所有元素添加到列表中
        for (int i = 0; i < array1.size(); i++) {
            list1.add(array1.get(i));
        }
        for (int i = 0; i < array2.size(); i++) {
            list2.add(array2.get(i));
        }

        // 比较两个列表的大小
        if (list1.size() != list2.size()) {
            return false;
        }

        // 比较列表中的每个元素
        for (Object obj1 : list1) {
            boolean found = false;
            if (obj1 instanceof JSONObject) {
                for (Object obj2 : list2) {
                    if (obj2 instanceof JSONObject && compareJSON((JSONObject) obj1, (JSONObject) obj2)) {
                        found = true;
                        list2.remove(obj2);
                        break;
                    }
                }
            } else if (obj1 instanceof JSONArray) {
                for (Object obj2 : list2) {
                    if (obj2 instanceof JSONArray && compareJSONArrays((JSONArray) obj1, (JSONArray) obj2)) {
                        found = true;
                        list2.remove(obj2);
                        break;
                    }
                }
            } else {
                // 比较基础类型或复杂类型（如日期）的值
                if (list2.contains(obj1)) {
                    found = true;
                    list2.remove(obj1);
                }
            }
            if (!found) {
                return false; // 如果没有找到匹配的元素，返回false
            }
        }
        return true; // 所有元素都找到了匹配项，返回true
    }

    /**
     * 比较两个对象是否相等，处理FastJSON中的特殊类型。
     *
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @return 如果两个对象相等，返回true；否则返回false。
     */
    private static boolean isEqual(Object obj1, Object obj2) {
        // 如果是JSON数组或JSON对象，使用相应的比较方法
        if (obj1 instanceof JSONArray && obj2 instanceof JSONArray) {
            return compareJSONArrays((JSONArray) obj1, (JSONArray) obj2);
        }
        if (obj1 instanceof JSONObject && obj2 instanceof JSONObject) {
            return compareJSON((JSONObject) obj1, (JSONObject) obj2);
        }

        // 对于其他类型，使用equals方法比较
        return obj1.equals(obj2);
    }



}
