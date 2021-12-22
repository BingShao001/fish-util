package com.yb.fish.ability.component;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * httpclient工具类
 *
 * @author huangwei 2019/07/01
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 编码格式。发送编码格式统一用UTF-8
     */
    private static final String ENCODING = "UTF-8";

    /**
     * 设置连接超时时间，单位毫秒。
     */
    private static final int CONNECT_TIMEOUT = 10000;

    /**
     * 设置连接超时时间，单位毫秒。
     */
    private static final int GET_CONNECT_QEQ_TIMEOUT = 10000;

    /**
     * 请求获取数据的超时时间(即响应时间)，单位毫秒。
     */
    private static final int SOCKET_TIMEOUT = 10000;

    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * 发送get请求；带请求参数
     *
     * @param url    请求地址
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> params){
        try {
            return doGet(url, null, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        httpGet.setConfig(getRequestConfig());

        // 设置请求头
        packageHeader(headers, httpGet);

        // 执行请求并获得响应结果
        return getString(httpClient, httpGet);

    }

    /**
     * 发送post请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doPost(String url) throws Exception {
        return doPost(url, null, null);
    }

    /**
     * 发送post请求；带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> params) throws Exception {
        return doPost(url, null, params);
    }

    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        httpPost.setConfig(getRequestConfig());
        // 设置请求头
        /*
         * httpPost.setHeader("Cookie", "");
         * httpPost.setHeader("Connection", "keep-alive");
         * httpPost.setHeader("Accept", "application/json");
         * httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
         * httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
         * httpPost.setHeader("User-Agent",
         * "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
         * );
         */
        packageHeader(headers, httpPost);

        // 封装请求参数
        packageParam(params, httpPost);

        // 执行请求并获得响应结果
        return getString(httpClient, httpPost);

    }

    /**
     * 发送put请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     */
    public static String doPut(String url) {
        try {
            return doPut(url, null);
        } catch (Exception e) {
            logger.error("http doPut请求异常,e:{}", e);
        }
        return null;
    }

    /**
     * 发送put请求；带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doPut(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(getRequestConfig());

        packageParam(params, httpPut);

        return getString(httpClient, httpPut);

    }

    /**
     * 发送delete请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     */
    public static String doDelete(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(getRequestConfig());

        return getString(httpClient, httpDelete);

    }

    /**
     * 发送delete请求；带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doDelete(String url, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }

        params.put("_method", "delete");
        return doPost(url, params);
    }

    /**
     * Description: 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    private static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    private static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    private static String getString(CloseableHttpClient httpClient, HttpRequestBase httpMethod) {
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpMethod);

            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                String content = "";
                if (httpResponse.getEntity() != null) {
                    content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
                }
                return content;
            }
            return null;
        } catch (Exception e) {
            logger.error("http请求异常,e:{}", e);
            return null;
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    private static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        try {
            // 释放资源
            if (httpResponse != null) {
                httpResponse.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            logger.error("释放异常,e:{}", e);
        }
    }

    /**
     * 发送post请求，下载文件
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static InputStream doDownloadByGet(String url, Map<String, String> params, Map<String, String> headers)
            throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setConfig(getRequestConfig());
        // 设置请求头
        packageHeader(headers, httpGet);

        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpGet);
            // 获取返回结果
            if (httpResponse != null && httpResponse.getStatusLine() != null) {
                if (httpResponse.getEntity() != null) {
                    return httpResponse.getEntity().getContent();
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("http请求异常,e:{}", e);
            return null;
        }
    }

    /**
     * oss上传文件请求
     *
     * @param uri
     * @param header
     * @param is
     * @param fileName
     * @param params
     * @return
     */
    public static String multipartPost(String uri, Map<String, String> header, InputStream is, String fileName,
                                       Map<String, Object> params) {
        logger.info("开始上传文件，params:{},{},{},{},{} ", uri, header, is, fileName, params);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        try {
            //构建URI
            HttpPost request = new HttpPost(URI.create(uri));
            //构建请求config
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20 * 1000)
                    .setSocketTimeout(20 * 1000).build();
            //构建请求头
            request.setConfig(requestConfig);
            for (Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }

            //设置传输参数
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            multipartEntity.addBinaryBody("file", is, ContentType.DEFAULT_BINARY, fileName);

            HttpEntity httpEntity = multipartEntity.build();
            request.setEntity(httpEntity);

            httpResponse = httpClient.execute(request);
            // 状态码
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            String result = getResultContent(httpResponse, null);
            logger.info("上传文件结果 -> {} ", result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getResultContent(HttpResponse httpResponse, Charset CHARSET)
            throws ParseException, IOException {
        if (CHARSET == null) {
            CHARSET = Consts.UTF_8;
        }
        HttpEntity entity = httpResponse.getEntity();
        String resultString = null;
        if (entity != null) {
            resultString = EntityUtils.toString(entity, CHARSET);
            EntityUtils.consume(entity);
        }
        return resultString;
    }

    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(GET_CONNECT_QEQ_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();
    }
}
