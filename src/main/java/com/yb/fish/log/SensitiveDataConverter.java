package com.yb.fish.log;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * 配置
 *     <conversionRule conversionWord="m" converterClass="com.yb.fish.log.SensitiveDataConverter">
 *     </conversionRule>
 */
public class SensitiveDataConverter extends MessageConverter {

    /**
     * 日志脱敏关键字
     */
    private static volatile String sensitiveDataKeys;

    /**
     * 默认值大一点，其他结合情况自行修改
     */
    private static volatile int logMaxLength = 10000;

    private static final String LOG_ALARM_MSG = "******日志长度超过阈值，可修改logMaxLength配置******";

    public static int getLogMaxLength() {
        return logMaxLength;
    }

    public static void setLogMaxLength(int logMaxLength) {
        logMaxLength = logMaxLength;
    }

    public static String getSensitiveDataKeys() {
        return sensitiveDataKeys;
    }

    /**
     * 外界注入待脱敏字段
     * @param sensitiveDataKeys
     */
    public static void setSensitiveDataKeys(String sensitiveDataKeys) {
        SensitiveDataConverter.sensitiveDataKeys = sensitiveDataKeys;
    }

    @Override
    public String convert(ILoggingEvent event) {
        // 获取原始日志
        String oriLogMsg = event.getFormattedMessage();
        if (StringUtils.isBlank(oriLogMsg)) {
            return oriLogMsg;
        }
        if (oriLogMsg.length() > logMaxLength) {
            return LOG_ALARM_MSG;
        }
        try {
            // 获取脱敏后的日志
            String sensitiveData = invokeMsg(oriLogMsg);
            return sensitiveData;
        } catch (Exception e) {

        }
        return oriLogMsg;
    }

    /**
     * 处理日志字符串，返回脱敏后的字符串
     *
     * @return
     * @param: msg
     */
    public String invokeMsg(final String oriMsg) {
        if (StringUtils.isBlank(sensitiveDataKeys) || StringUtils.isBlank(oriMsg)) {
            return oriMsg;
        }
        String tempMsg = oriMsg;
        String[] keysArray = sensitiveDataKeys.split(",");
        for (String key : keysArray) {
            int index = -1;
            do {
                index = tempMsg.indexOf(key, index + 1);
                if (index != -1) {
                    // 寻找值的开始位置
                    int valueStart = getValueStartIndex(tempMsg, index + key.length());
                    int valueEnd = getValueEndEIndex(tempMsg, valueStart);
                    // 对获取的值进行脱敏
                    String subStr = tempMsg.substring(valueStart, valueEnd);
                    subStr = tuoMin(subStr, key);
                    ///
                    tempMsg = tempMsg.substring(0, valueStart) + subStr + tempMsg.substring(valueEnd);
                }
            } while (index != -1);
        }
        return tempMsg;
    }

    /**
     * 获取value值的开始位置
     *
     * @param msg        要查找的字符串
     * @param valueStart 查找的开始位置
     * @return
     */
    private static int getValueStartIndex(String msg, int valueStart) {
        do {
            char ch = msg.charAt(valueStart);
            if (ch == ':' || ch == '=') { // key与 value的分隔符
                valueStart++;
                ch = msg.charAt(valueStart);
                if (ch == '"') {
                    valueStart++;
                }
                break; // 找到值的开始位置
            } else {
                valueStart++;
            }
        } while (true);
        return valueStart;
    }

    /**
     * 获取value值的结束位置
     *
     * @return
     */
    private static int getValueEndEIndex(String msg, int valueEnd) {
        do {
            if (valueEnd == msg.length()) {
                break;
            }
            char ch = msg.charAt(valueEnd);

            if (ch == '"') { // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
                if (valueEnd + 1 == msg.length()) {
                    break;
                }
                char nextCh = msg.charAt(valueEnd + 1);
                if (nextCh == ';' || nextCh == ',') {
                    while (valueEnd > 0) {
                        char preCh = msg.charAt(valueEnd - 1);
                        if (preCh != '\\') {
                            break;
                        }
                        valueEnd--;
                    }
                    break;
                } else {
                    valueEnd++;
                }
            } else if (ch == ';' || ch == ',' || ch == '}') {
                break;
            } else {
                valueEnd++;
            }

        } while (true);
        return valueEnd;
    }

    /**
     * 调用脱敏工具类进行字段日志处理
     *
     * @param subMsg
     * @param key    后续可以根据key进行不同策略脱敏
     * @return
     */
    private static String tuoMin(String subMsg, String key) {
        if ("password".equals(key)) {
            return "****";
        }
        return mask(subMsg);
    }

    private static final String MASK = "****";

    public static String mask(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        int len = value.length();
        if (len == 1) {
            return MASK;
        }
        if (len < MASK.length()) {
            return StringUtils.left(value, 1) + MASK;
        }
        int endLen = (len - MASK.length()) / 2;
        if (endLen == 0) {
            endLen = 1;
        }
        if (endLen > 4) {
            endLen = 4;
        }
        return StringUtils.left(value, endLen) + MASK + StringUtils.right(value, endLen);
    }

}
