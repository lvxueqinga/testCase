package com.lxq.testOnline.util;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClientUtil {
    //MIME部分文件类型对照表
    private static final Map<String, String> FILE_TYPE = new HashMap<>();

    static {
        FILE_TYPE.put(".jpeg", "image/jpeg");
        FILE_TYPE.put(".jpg", "image/jpg");
        FILE_TYPE.put(".png", "image/png");
        FILE_TYPE.put(".bmp", "image/bmp");
        FILE_TYPE.put(".gif", "image/gif");
        FILE_TYPE.put(".mp4", "video/mp4");
        FILE_TYPE.put(".txt", "text/plain");
        FILE_TYPE.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        FILE_TYPE.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        FILE_TYPE.put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        FILE_TYPE.put(".pdf", "application/pdf");
    }

    /**
     * GET请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String doGet(String url, Map<String, String> params, Map<String, String> headers) {
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

    /**
     * POST请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String doPost(String url, Map<String, String> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            //组装请求头
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //组装请求体
            List<NameValuePair> paramList = new ArrayList<>();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
            httpPost.setEntity(requestEntity);

            //发送请求
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

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

    public static String post(String url, String jsonString) throws IOException, ParseException {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity((HttpEntity) new StringEntity(jsonString, org.apache.http.entity.ContentType.APPLICATION_JSON));


        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        response.close();
        httpClient.close();
        return result;
    }

    /**
     * GET请求下载文件
     *
     * @param url
     * @param params
     * @param headers
     * @param filePath
     */
    public static void doGetDownload(String url, Map<String, String> params, Map<String, String> headers, String filePath) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        BufferedInputStream inputStream = null;
        FileOutputStream outputStream = null;
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
            String contentDisposition = response.getHeader("Content-Disposition").getValue();
            String regex = "attachment; filename=(.+\\.\\w+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(contentDisposition);
            if (matcher.find()) {
                String fileName = matcher.group(1);
                File file = new File(filePath + "\\" + fileName);
                outputStream = new FileOutputStream(file);
                inputStream = new BufferedInputStream(response.getEntity().getContent());
                int n;
                while ((n = inputStream.read()) != -1) {
                    outputStream.write(n);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
            response.close();
            httpClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("输出流关闭失败");
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("输入流关闭失败");
                }
            }
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
    }

    /**
     * POST请求上传文件
     *
     * @param url
     * @param fileUrl
     * @param params
     * @param headers
     * @return
     */
    public static String doPostUpload(String url, String fileUrl, Map<String, String> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            File file = new File(fileUrl);
            //组装请求头
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //组装请求体
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addBinaryBody("file", file, ContentType.create(FILE_TYPE.get(fileExtension)), fileName);
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    entityBuilder.addTextBody(entry.getKey(), entry.getValue());
                }
            }
            HttpEntity requestEntity = entityBuilder.build();
            httpPost.setEntity(requestEntity);

            //发送请求
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);

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

    /**
     * 从返回头中获取登录token
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String getToken(String url, Map<String, String> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig config = RequestConfig.custom().setRedirectsEnabled(false).build();
            httpPost.setConfig(config);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            List<NameValuePair> paramList = new ArrayList<>();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(paramList);
            httpPost.setEntity(requestEntity);

            //发送请求，重定向到返回头中的Location
            response = httpClient.execute(httpPost);
            String location = response.getHeader("Location").getValue();

            //请求Location，获取返回头中的所有Set-Cookie
            HttpGet httpGet = new HttpGet(location);
            httpGet.setConfig(config);
            response = httpClient.execute(httpGet);
            Header[] cookies = response.getHeaders("Set-Cookie");
            for (Header cookie : cookies) {
                if (cookie.getValue().contains("token-test=")) {
                    return cookie.getValue();
                }
            }
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
}
