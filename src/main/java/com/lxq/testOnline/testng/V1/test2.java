package com.lxq.testOnline.testng.V1;

import com.alibaba.fastjson.JSONObject;
import com.lxq.testOnline.util.JSONCompareUtil;

public class test2 {
    public static void main(String[] args){

        JSONObject obj1 = JSONObject.parseObject("{\n" +
                "  \"name\": \"cwp\",\n" +
                "  \"courses\": [\n" +
                "    {\n" +
                "      \"name\": \"语文\",\n" +
                "      \"detail\": {\n" +
                "        \"score\": 80,\n" +
                "        \"grade\": \"A\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"数学\",\n" +
                "      \"detail\": {\n" +
                "        \"score\": 80,\n" +
                "        \"grade\": \"B\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "} ");

        JSONObject obj2 = JSONObject.parseObject("{\n" +
                "  \"name\": \"cwp\",\n" +
                "  \"courses\": [\n" +
                "    {\n" +
                "      \"name\": \"语文\",\n" +
                "      \"detail\": {\n" +
                "        \"score\": 80,\n" +
                "        \"grade\": \"A\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"数学\",\n" +
                "      \"detail\": {\n" +
                "        \"score\": 80,\n" +
                "        \"grade\": \"B\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}");
//
        //==============
//        JSONObject obj1 = JSONObject.parseObject("{\n" +
//                "  \"name\": \"cwp\",\n" +
//                "  \"scores\": [\"80\", \"90\", \"100\"]\n" +
//                "}");
//
//        JSONObject obj2 = JSONObject.parseObject("{\n" +
//                "  \"name\": \"cwp\",\n" +
//                "  \"scores\": [\"100\", \"90\", \"80\"]\n" +
//                "}");

//        JSONObject obj1 = JSONObject.parseObject("{\n" +
//                "  \"name\": \"cwp\",\n" +
//                "  \"course\": {\n" +
//                "    \"语文\": {\n" +
//                "      \"score\": \"80\",\n" +
//                "      \"grade\": \"A\"\n" +
//                "    },\n" +
//                "    \"数学\": {\n" +
//                "      \"score\": \"80\",\n" +
//                "      \"grade\": \"A\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}");
//
//        JSONObject obj2 = JSONObject.parseObject("{\n" +
//                "  \"name\": \"cwp\",\n" +
//                "  \"course\": {\n" +
//                "    \"数学\": {\n" +
//                "      \"score\": \"80\",\n" +
//                "      \"grade\": \"A\"\n" +
//                "    },\n" +
//                "    \"语文\": {\n" +
//                "      \"score\": \"80\",\n" +
//                "      \"grade\": \"A\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}");

//        JSONObject obj1 = JSONObject.parseObject("{\n" +
//                "  \"name\": \"cwp\",\n" +
//                "  \"courses\": [\n" +
//                "    {\n" +
//                "      \"subject\": \"语文\",\n" +
//                "      \"scores\": [\"80\", \"85\", \"90\"]\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"subject\": \"数学\",\n" +
//                "      \"scores\": [\"80\", \"85\", \"95\"]\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}");
//
//        JSONObject obj2 = JSONObject.parseObject("{\n" +
//                "  \"name\": \"cwp\",\n" +
//                "  \"courses\": [\n" +
////                "    {\n" +
////                "      \"subject\": \"语文\",\n" +
////                "      \"scores\": [\"80\", \"85\", \"90\"]\n" +
////                "    },\n" +
//                "    {\n" +
//                "      \"subject\": \"数学\",\n" +
//                "      \"scores\": [\"80\", \"95\", \"85\"]\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"subject\": \"语文\",\n" +
//                "      \"scores\": [\"80\", \"85\", \"90\"]\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}");
//
//        JSONObject obj1 = JSONObject.parseObject("");
//
//        JSONObject obj2 = JSONObject.parseObject("");

        System.out.println(JSONCompareUtil.compareJSON(obj1,obj2));;

    }

}

