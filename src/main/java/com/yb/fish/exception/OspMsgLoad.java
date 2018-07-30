package com.yb.fish.exception;

import com.alibaba.fastjson.JSONObject;
import com.yb.fish.constant.FishContants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * load properties to model
 *
 * @author bing
 * @version 1.0
 * @create 2018/5/17
 **/
public class OspMsgLoad {

    private static Properties properties = null;


    static {
        InputStream ips = Thread.currentThread().getContextClassLoader().getResourceAsStream(FishContants.OSP_MSG_PROPERTIES);
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(ips, FishContants.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ips.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getOspMsgModel
     *
     * @param msgKey 文案key
     * @return OspMsgModel
     */
    protected static OspMsgModel getOspMsgModel(String msgKey) {
        String ospMsg = properties.getProperty(msgKey);
        OspMsgModel ospMsgModel = JSONObject.parseObject(ospMsg, OspMsgModel.class);
        return ospMsgModel;
    }

}
