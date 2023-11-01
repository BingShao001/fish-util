package com.yb.fish.exception;

import com.alibaba.fastjson.JSON;
import com.yb.fish.constant.FishContants;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
* 断言扩展自定义
* @author bing
* @create 2018/5/23
* @version 1.0
**/
public class OriginalAssert extends Assert {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(OriginalAssert.class);



    private static OspMsgModel getOspMsgModel(String ospMsg) throws BusinessException {
        OspMsgModel ospMsgModel = OspMsgLoad.getOspMsgModel(ospMsg);
        if (null == ospMsgModel) {
            throw new BusinessException(FishContants.FILE_ERROR, FishContants.FILE_ERROR_MSG);
        }
        return ospMsgModel;
    }

    private static void throwBusinessException(OspMsgModel ospMsgModel) throws BusinessException {
        throw new BusinessException(ospMsgModel.getErrorCode(), ospMsgModel.getErrorMsg());
    }
    /***
     * 当出入的逻辑判断为true时抛出异常符合中国人思维
     * @param expression 逻辑表达
     * @param ospMsg 提示properties对应的key
     * @throws BusinessException 提示异常标识
     */
    public static void isRealTrue(boolean expression, String ospMsg) throws BusinessException {
        OspMsgModel ospMsgModel =  getOspMsgModel(ospMsg);
        if (expression) {
            throwBusinessException(ospMsgModel);
        }
    }
    /***
     * 当出入的逻辑判断为true时抛出异常符合中国人思维
     * @param expression 逻辑表达
     * @param msg 提示properties对应的key
     * @throws BusinessException 提示异常标识
     */
    public static void isRealTrueThrows(boolean expression,int errorCode ,String msg) throws BusinessException {
        if (expression) {
            throw new BusinessException(errorCode,msg);
        }
    }
    /***
     * 当出入的逻辑判断为false时抛出异常符合中国人思维
     * @param expression 逻辑表达
     * @param ospMsg 提示properties对应的key
     * @throws BusinessException 提示异常标识
     */
    public static void isRealFalse(boolean expression,String ospMsg) throws BusinessException {
        OspMsgModel ospMsgModel =  getOspMsgModel(ospMsg);
        if (!expression) {
            throwBusinessException(ospMsgModel);
        }
    }

    /***
     * 当出入的对象判断为null时抛出异常符合中国人思维
     * @param object 对象类型
     * @param ospMsg 提示properties对应的key
     * @throws BusinessException 提示异常标识
     */
    public static void isRealNull(Object object,String ospMsg) throws BusinessException {
        OspMsgModel ospMsgModel =  getOspMsgModel(ospMsg);
        if (object == null) {
            throwBusinessException(ospMsgModel);
        }
    }

    /***
     * 当出入的集合判断为null时抛出异常符合中国人思维
     * @param collection 集合框架
     * @param ospMsg 提示properties对应的key
     * @throws BusinessException 提示异常标识
     */
    public static void isEmpty(Collection<?> collection,String ospMsg) throws BusinessException {
        OspMsgModel ospMsgModel =  getOspMsgModel(ospMsg);
        if (CollectionUtils.isEmpty(collection)) {
            throwBusinessException(ospMsgModel);
        }
    }

    /**
     * 当出入的集合判断为null时抛出异常符合中国人思维
     *
     * @param map 集合框架
     * @param msg 提示properties对应的key
     * @throws BusinessException 提示异常标识
     */
    public static void isEmpty(Map map, String msg) throws BusinessException {
        OspMsgModel ospMsgModel =  getOspMsgModel(msg);
        if (CollectionUtils.isEmpty(map)) {
            throwBusinessException(ospMsgModel);
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param string 字符串
     * @param ospMsg 提示properties对应的key
     * @throws BusinessException 提示异常标识
     */
    public static void isStringEmpty(String string,String ospMsg) throws BusinessException {
        OspMsgModel ospMsgModel =  getOspMsgModel(ospMsg);
        if (StringUtils.isBlank(string)) {
            throwBusinessException(ospMsgModel);
        }
    }

    /**
     * 记录日志并抛出业务异常
     *
     * @param functionName 方法名称
     * @param e 异常对象
     * @throws BusinessException 提示异常标识
     */
    public static void throwBusinessException(String functionName, Exception e) throws BusinessException {
        logger.info(functionName + FishContants.LOG_SUFFIX, JSON.toJSON(e.getMessage()));
        if (e instanceof BusinessException){
            throw new BusinessException(((BusinessException) e).getErrorCode(),e.getMessage());
        }
        throw new BusinessException(ErrorMsg.SYSTEM_ERROR.getErrorCode(), ErrorMsg.SYSTEM_ERROR.getErrorMsg());
    }
}

