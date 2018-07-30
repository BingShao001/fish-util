package com.yb.fish.compute;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
* 入参为表达式，占位符的map集合，保留位数
* @author bing
* @create 2018/3/29
* @version 1.0
**/
public class DataComputer {

    private DataComputer(){}

    /**
     * 利用FelEngine做运算
     * @param expression
     * @param param
     * @return
     */
    private static Object computeByParam(String expression, Map<String, Object> param) {

        FelEngine fel = new FelEngineImpl();
        FelContext ctx = fel.getContext();
        Iterator<String> keySet = param.keySet().iterator();
        while (keySet.hasNext()) {
            String key = keySet.next();
            ctx.set(key, param.get(key));
        }
        Expression exp = fel.compile(expression, ctx);

        return exp.eval(ctx);

    }

    /**
     * 运算结果集四舍五入保留point
     * @param data
     * @param point
     * @return String
     */
    private static String doRound(Object data, int point) {
        String ret = null;
        if (data instanceof Double) {
            BigDecimal decimal = BigDecimal.valueOf((double) data);
            ret = decimal.setScale(point, BigDecimal.ROUND_HALF_UP).toPlainString();
        }else {
            ret = Integer.toString((int)data);
        }
        return ret;

    }

    /**
     * 取四舍五入结果集
     * @param expression 运算表达式，eg:单价*(数量+运费)
     * @param param 占位数
     * @param point 保留point
     * @return String
     */
    public static String getRound(String expression, Map<String, Object> param, int point){
       Object retObj = computeByParam(expression,param);
       String ret = doRound(retObj,point);
        return ret;
    }

    /**
     * 运算结果集向上取整
     * @param data
     * @param point
     * @return String
     */
    private static String doCeil(Object data,int point) {
        String ret = null;
        if (data instanceof Double) {
            BigDecimal decimal = BigDecimal.valueOf((double) data);
            ret = decimal.setScale(point, BigDecimal.ROUND_UP).toPlainString();
        }else {
            ret = Integer.toString((int)data);
        }
        return ret;

    }
    /**
     * 向上取整结果集
     * @param expression 运算表达式，eg:单价*(数量+运费)
     * @param param 占位数
     * @param point 保留point
     * @return String
     */
    public static String getCeil(String expression, Map<String, Object> param, int point){
        Object retObj = computeByParam(expression,param);
        String ret = doCeil(retObj,point);
        return ret;
    }

//    public static String expression = "单价*(数量+运费)";
//    public static void main(String[] args) {
//        Map<String, Object> param = new HashMap<>();
//        param.put("单价", 2.0);
//        param.put("数量", 2.111);
//        param.put("运费", 1);
//        System.out.println(getRound(expression,param,2));
//
//    }
}