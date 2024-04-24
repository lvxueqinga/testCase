package com.lxq.testOnline.testng.commonCase;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxq.testOnline.testng.commonDataStructure.Api;
import com.lxq.testOnline.testng.commonDataStructure.LogMine;
import com.lxq.testOnline.util.HttpHelper;
import com.lxq.testOnline.util.StatusCode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TestCaseCommon {

    public LogMine logger = new LogMine(LoggerFactory.getLogger(this.getClass()));
    public String response = "";
    public String authorization = "";
    public Map<String, String> HEADERS = new ConcurrentHashMap<>();

    private final String AUTHORIZATION = "Authorization";


    private boolean DEBUG = Boolean.parseBoolean(System.getProperty("DEBUG", "true"));



    public TestCaseCommon() {

    }

    @AfterSuite
    public void afterSuite() {



    }

    @BeforeSuite
    public void beforeSuite() {

        initialDB();
    }

    public void initialDB() {


    }

    public void initHttpConfig(){
        HEADERS.put("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        HEADERS.put("Content-Type","application/json;charset=UTF-8");

    }


    public String httpPost(String port, String path, String requestBody) throws Exception {
        Api api = new Api.Builder().method("POST").ipPort(port).path(path).requestBody(requestBody).build();
//        return execute(api, checkCode, hasToken);
        return  getResponse(api);
    }
    public String httpPut(String port, String path, String requestBody) throws Exception {
        Api api = new Api.Builder().method("PUT").ipPort(port).path(path).requestBody(requestBody).build();
//        return execute(api, checkCode, hasToken);
        return  getResponse(api);
    }
    public String httpGET(String port, String path, String requestBody) throws Exception {
        Api api = new Api.Builder().method("GET").ipPort(port).path(path).requestBody(requestBody).build();
        return  getResponse(api);
    }

    public String httpGETpara(String url, Map<String, String> params, Map<String, String> headers){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            //1、拼接url
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            URI testUrl = uriBuilder.build();

            //2、组装请求头
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(testUrl);
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //3、发送请求
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            response.close();
            httpClient.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    System.out.println("response close failed");
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    System.out.println("httpClient close failed");
                }
            }
        }
        return null;
    }

    private String execute(Api api, boolean checkCode, boolean hasToken) throws Exception {
        long start = System.currentTimeMillis();
        getResponse(api);
        if (checkCode) {
            checkCode(response, StatusCode.SUCCESS, api.getPath());
        }
        if (hasToken) {
            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            updateAuthorization();
            logger.info("authorization:" + HEADERS.get(AUTHORIZATION));
        }
        logger.info("{} time used {} ms", api.getPath(), System.currentTimeMillis() - start);

        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {


        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                logger.info("info-----" + message + " expect code: " + expect + ",actual: " + code);
                throw new Exception(message + " expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + "，path：" + path);
        }
    }

    private String getResponse(@NotNull Api api) {
        HEADERS.put("Accept-Language","zh-CN");
        HEADERS.put("Content-Type","application/json");

        if (api.getMethod().equals("POST")) {
            response = HttpHelper.post(HEADERS, api.getUrl(), api.getRequestBody()).getContent();

        } else if (api.getMethod().equals("GET")) {
            response = HttpHelper.get(api.getUrl(), HEADERS).getContent();
        }
        else if (api.getMethod().equals("PUT")) {
            response = HttpHelper.put(HEADERS, api.getUrl(), api.getRequestBody()).getContent();
        }
        else {
            throw new RuntimeException("未指定请求方式");
        }
        logger.info("response：{}", response);
        return response;

    }

    private void updateAuthorization() {
        HEADERS.put(AUTHORIZATION, authorization);
    }





}
