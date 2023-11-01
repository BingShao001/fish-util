package com.yb.fish.utils;

import com.alibaba.fastjson.JSON;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * YbHttpUtils
 *
 * @author bing
 * @version 1.0
 * @create 2023/8/9
 **/
@Slf4j
public class YbHttpUtils {


    private static RestTemplate client = new RestTemplate();

    public static String HEARD_PRE = "Basic ";

    /**
     * 请求封装
     * @param requestProperty 请求元素
     * @return http接口返回值
     */
    public static Map<String, Object> doRequest(RequestProperty requestProperty) {
        return doRequest(requestProperty, Map.class);
    }

    /**
     * 请求封装
     * @param requestProperty 请求参数
     * @param responseClass   用于接收返回数据的实体
     * @return 返回实体
     */
    public static <T> T doRequest(RequestProperty requestProperty, Class<T> responseClass) {
        String jsonStr = executeMethod(requestProperty.getUrl(), requestProperty.getMethod(),
                requestProperty.getHttpHeaders(), requestProperty.getParamMap());
        try {
            return JSON.parseObject(jsonStr, responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行请求
     *
     * @param url   请求URL
     * @param param 请求参数 key:value
     * @return string
     */
    private static String executeMethod(String url, HttpMethod method, HttpHeaders headers,
                                        Map<String, Object> param) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 将请求头部和参数合成一个请求
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(param, headers);
        // 执行HTTP请求，将返回的结构使用String类格式化
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        log.info("调用接口返回值：{}", response.getBody());
        return response.getBody();
    }

    /**
     * 请求封装
     * @param requestProperty 请求参数
     * @param responseClass   用于接收返回数据的实体
     * @param contentType 媒体类型
     * @param <T> 接收返回实体
     */
    public static <T> T doRequestWithFormData(RequestProperty requestProperty,
                                  Class<T> responseClass, MediaType contentType) {
        String jsonStr = executeMethodWithFormData(requestProperty.getUrl(),
                requestProperty.getHttpHeaders(), requestProperty.getParamMap(), contentType);
        try {
            return JSON.parseObject(jsonStr, responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String executeMethodWithFormData(String url,
                                                    HttpHeaders headers,
                                                    Map<String, Object> param,
                                                    MediaType contentType) {
        headers.setContentType(contentType);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        Iterator<Map.Entry<String, Object>> iterator = param.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            map.add(key, value);
        }
        // 将请求头部和参数合成一个请求
        //构造实体对象
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        // 执行HTTP请求，将返回的结构使用String类格式化
        ResponseEntity<String> response = client.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }
}
