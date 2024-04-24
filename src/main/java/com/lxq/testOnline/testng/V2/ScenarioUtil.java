package com.lxq.testOnline.testng.V2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxq.testOnline.testng.commonCase.TestCaseCommon;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ScenarioUtil extends TestCaseCommon {
    private static ScenarioUtil instance = null;


    public String ip = "http://127.0.0.1:8888";
//    public String ip = "http://localhost:8899"; //mock

    //单例
    public static ScenarioUtil getInstance(){
        if(instance == null){
            synchronized (ScenarioUtil.class){
                instance = new ScenarioUtil();
            }
        }
        return instance;
    }


    public JSONObject getbook(int page, int size){
        String url = ip+ "/book/search";
        String res = null;
        Map <String,String> map = new ConcurrentHashMap<>();
        map.put("page","1");
        map.put("size","20");
        try {
//        JSONObject obj = new JSONObject();
//        obj.put("page",page);
//        obj.put("size",size);


            res = httpGETpara(url,map,null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return JSONObject.parseObject(res);
    }


    public JSONObject editBook(String pic ,String name, String id) throws Exception {
        String url = "/book/edit";
        JSONObject obj = new JSONObject();
        obj.put("pic",pic);
        obj.put("name",name);
        obj.put("id",id);


        String res = httpPost(ip,url,obj.toString());
        System.out.println("res ---" + res);

        return JSONObject.parseObject(res);

    }
}
