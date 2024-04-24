package com.lxq.testOnline.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * 用于HTTP请求，使用池化设置。
 *
 *
 */

@Slf4j
public class HttpHelper {

    private static final String UTF8 = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";

    private static final int maxPerRoute = 300;
    private static final int maxTotal = 300;
    private static final int connectionTimeoutMillis = 3000;
    private static final int socketTimeoutMillis = 20000;
//    private static final int socketTimeoutMillis = 40000;
    private static final int maxRetry = 3;

    private static final String user = null;
    private static final String password = null;

    private static CloseableHttpClient httpClient;

    static {

        // 池化管理
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(maxPerRoute);

        // 失败重试策略
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // 如果已经重试了5次，就放弃
                if (executionCount >= maxRetry) {
                    return false;
                }
                // 连接被拒绝
                if (exception instanceof ConnectTimeoutException) {
                    return true;
                }
                // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                // socket time out
                if (exception instanceof SocketTimeoutException) {
                    return true;
                }
                // 连接被拒绝
                if (exception instanceof SocketException) {
                    return true;
                }
                // 不要重试SSL握手异常
                if (exception instanceof SSLHandshakeException) {
                    return false;
                }
                // 超时
                if (exception instanceof InterruptedIOException) {
                    return false;
                }
                // 目标服务器不可达
                if (exception instanceof UnknownHostException) {
                    return false;
                }
                // ssl握手异常
                if (exception instanceof SSLException) {
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                return !(request instanceof HttpEntityEnclosingRequest);
            }
        };

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectionTimeoutMillis)
                .setSocketTimeout(socketTimeoutMillis)
                .build();
        if (StringUtils.isNoneEmpty(user) && StringUtils.isNoneEmpty(password)) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials
                    = new UsernamePasswordCredentials(user, password);
            provider.setCredentials(AuthScope.ANY, credentials);
            httpClient = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultCredentialsProvider(provider)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(httpRequestRetryHandler)
                    .build();
        } else {
            httpClient = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(httpRequestRetryHandler)
                    .build();
        }
    }

    /**
     * head请求
     */
    public static Result head(String url) {
        log.info("@post request url = {}", url);
        HttpHead httpHead = new HttpHead(url);
        Result result = execute(httpHead);
        log.info("@post response code = {}, result = {}", result.getStatusCode(), result.getContent());
        return result;
    }

    /**
     * post请求
     */
    public static Result post(Map<String, String> headers, String url, String jsonString) {
        log.info("@post request url = {}, jsonStr = {}, headers = {}", url, jsonString, headers.toString());
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        if (null != headers) {
            headers.forEach((k, v) -> httpPost.addHeader(k, v));
        }
        Result result = execute(httpPost);
        return result;
    }

    /**
     * post请求JSON
     */
    public static Result post(String url, String jsonString) {
        log.info("@post request url = {}, jsonStr = {}", url, jsonString);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        Result result = execute(httpPost);
        log.info("@post response code = {}, result = {}", result.getStatusCode(), result.getContent());
        return result;
    }
    /**
     * put请求
     */
    public static Result put(Map<String, String> headers, String url, String jsonString) {
        log.info("@put request url = {}, jsonStr = {}, headers = {}", url, jsonString, headers.toString());
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        if (null != headers) {
            headers.forEach((k, v) -> httpPut.addHeader(k, v));
        }
        Result result = execute(httpPut);
        return result;
    }


    /**
     * put请求JSON
     */
    public static Result put(String url, String jsonString) {
        log.info("@put request url = {}, jsonStr = {}", url, jsonString);
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        Result result = execute(httpPut);
        log.info("@put response code = {}, result = {}", result.getStatusCode(), result.getContent());
        return result;
    }

    /**
     * post请求表单
     */
    public static Result post(String url, Map<String, String> form) {
        log.info("@post request url = {}, jsonStr = {}", url, JSON.toJSONString(form));
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<>(form.size());
        for (Map.Entry<String, String> entry : form.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        Result result = execute(httpPost);
        log.info("@post response code = {}, result = {}", result.getStatusCode(), result.getContent());
        return result;
    }

    /**
     * get请求
     */
    public static Result get(String url, Map<String, String> headers) {
        log.info("@get request url = {}", url);
        HttpGet httpGet = new HttpGet(url);
        if (null != headers) {
            headers.forEach((k, v) -> httpGet.addHeader(k, v));
        }
        return execute(httpGet);
    }

    /**
     * get 拼接url
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


    public static Result uploadFile(String url, String filePath, Map<String, String> header, String type) throws IOException {
        log.info("@post request url = {}, filepath = {}, type={}, headers = {}", url, filePath, type, header.toString());
        HttpPost httpPost = new HttpPost(url);

        for (String key : header.keySet()) {
            httpPost.addHeader(key, header.get(key));
        }
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // 把文件加到HTTP的post请求中
        File pictureAFile = new File(filePath);
        builder.addBinaryBody(
                "file",
                new FileInputStream(pictureAFile),
                ContentType.APPLICATION_OCTET_STREAM,
                pictureAFile.getName()
        );
        builder.addTextBody("type", type, ContentType.MULTIPART_FORM_DATA);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        return execute(httpPost);
    }


    public static Result compareImageRequest(String url, String picAURL, String picBPath ) throws IOException {
        log.info("@post request url = {}, picA = {}, picB={}", url, picAURL, picBPath);
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("session_token", "123456");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // 把文件加到HTTP的post请求中
        File pictureAFile = new File(picAURL);
        builder.addBinaryBody(
                "pictureA",
                new FileInputStream(pictureAFile),
                ContentType.APPLICATION_OCTET_STREAM,
                pictureAFile.getName()
        );
        File pictureBFile = new File(picBPath);
        builder.addBinaryBody(
                "pictureB",
                new FileInputStream(pictureBFile),
                ContentType.APPLICATION_OCTET_STREAM,
                pictureBFile.getName()
        );
        builder.addTextBody("isImageA", "true", ContentType.MULTIPART_FORM_DATA);
        builder.addTextBody("isImageB", "false", ContentType.MULTIPART_FORM_DATA);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        return execute(httpPost);
    }

    /**
     *
     * 分隔，公共与私有
     * 以下为私有方法
     *
     */
    private static Result execute(HttpUriRequest request) {
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            Result result = processHttpResponse(response, UTF8);
            if (null == result) {
                throw new RuntimeException("net exception : http result is null");
            }
            return result;
        } catch (SocketTimeoutException e) {
            throw new RuntimeException("SocketTimeoutException");
        } catch (ConnectTimeoutException e) {
            throw new RuntimeException("ConnectTimeoutException");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Result processHttpResponse(CloseableHttpResponse response, String encoding) {
        Header[] contentType = response.getHeaders(CONTENT_TYPE);
        String contentTypeEncoding = encoding;
        if (contentType != null && contentType.length > 0) {
            String[] arr = contentType[0].getValue().split("charset=");
            if (arr.length > 1) {
                contentTypeEncoding = arr[1];
            }
        }
        HttpEntity respEntity = response.getEntity();
        try {
            Result result = new Result();
            if (respEntity != null && respEntity.getContent() != null) {
                result.content = IOUtils.toString(respEntity.getContent(), getCharset(contentTypeEncoding));
            }
            result.headers = response.getAllHeaders();
            result.statusLine = response.getStatusLine();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            EntityUtils.consumeQuietly(respEntity);
            IOUtils.closeQuietly(response);
        }
    }

    private static Charset getCharset(String encoding) {
        Charset charset;
        try {
            charset = Charset.forName(encoding);
        } catch (Exception e) {
            charset = Charset.forName(UTF8);
        }
        return charset;
    }

    public static class Result {
        private Header[] headers;
        private String content;
        private StatusLine statusLine;

        public Header[] getHeaders() {
            return headers;
        }

        public String getContent() {
            return content;
        }

        public int getStatusCode() {
            return statusLine.getStatusCode();
        }

        public String getReasonPhrase() {
            return statusLine.getReasonPhrase();
        }

        public boolean isSuccess() {
            return getStatusCode() == 200;
        }
    }
}
