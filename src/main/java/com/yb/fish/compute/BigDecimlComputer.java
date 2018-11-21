package com.yb.fish.compute;

import com.yb.fish.constant.FishContants;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
/**
* 精度运算模板工具
* @author bing
* @create 2018/5/23
* @version 1.0
**/
public class BigDecimlComputer {
    public static final char DIVIDE = '/';
    private static final char MULTIPLY = '*';
    public static final char PLUS = '+';
    public static final char SUBTRACT = '-';
    public static final char LEFT_SIGN = '(';
    public static final char RIGHT_SIGN = ')';

    public static void main(String[] args) {
        BigDecimal[] bigDecimals = {BigDecimal.valueOf(2.1), BigDecimal.valueOf(2), BigDecimal.valueOf(3), BigDecimal.valueOf(1)};
        String rex = "?*?/(?+?)";
        BigDecimal decimal = computerBigDecimalByExpression(rex, bigDecimals);
        System.out.println(decimal);
    }

    /**
     * 根据入参计算结果集
     *
     * @param expression 表达式：?*?/(?+?)
     * @param bigDecimals 占位符赋值
     * @return BigDecimal
     */
    public static BigDecimal computerBigDecimalByExpression(String expression, BigDecimal... bigDecimals) {
        Queue expressionQueue = buildExpressionQueue(expression, bigDecimals);
        BigDecimal decimal = postfix(expressionQueue);
        return decimal;
    }

    /**
     * 根据占位符拼装
     *
     * @param expression
     * @param bigDecimals
     * @return
     */
    private static Queue buildExpressionQueue(String expression, BigDecimal... bigDecimals) {
        char[] chars = expression.toCharArray();
        Queue expressionQueue = new ArrayDeque();
        int index = FishContants.ZERO;
        for (char _char : chars) {
            switch (_char) {
                case MULTIPLY:
                    expressionQueue.add(MULTIPLY);
                    break;
                case DIVIDE:
                    expressionQueue.add(DIVIDE);
                    break;
                case PLUS:
                    expressionQueue.add(PLUS);
                    break;
                case SUBTRACT:
                    expressionQueue.add(SUBTRACT);
                    break;
                case LEFT_SIGN:
                    expressionQueue.add(LEFT_SIGN);
                    break;
                case RIGHT_SIGN:
                    expressionQueue.add(RIGHT_SIGN);
                    break;
                default:
                    expressionQueue.add(bigDecimals[index]);
                    index++;
            }

        }
        return expressionQueue;
    }

    /**
     * 中缀表达转后缀表达式
     *
     * @param expressionQueue
     * @return
     */
    private static BigDecimal postfix(Queue expressionQueue) {
        Stack<BigDecimal> nums = new Stack<>(); // 保存数字
        Stack<Character> opes = new Stack<>(); // 保存操作符
        while (!expressionQueue.isEmpty()) {
            Object data = expressionQueue.poll();
            BigDecimal dataDecimal = null;
            char dataOperators = FishContants.ZERO;
            if (data instanceof BigDecimal) {
                dataDecimal = (BigDecimal) data;
                nums.push(dataDecimal);
                continue;
            }
            if (data instanceof Character) {
                dataOperators = (Character) data;
            }
            //括号
            if (dataOperators == LEFT_SIGN) {
                opes.push(dataOperators);
                continue;
            } else if (dataOperators == RIGHT_SIGN) {
                while (opes.peek() != LEFT_SIGN) { // 括号里面运算完
                    BigDecimal ret = computerData(nums.pop(), nums.pop(), opes.pop());
                    nums.push(ret);
                    continue;
                }
                opes.pop();
                //运算
            } else if (isLevelType(dataOperators) > FishContants.ZERO) {
                // 栈为空直接入栈
                if (opes.isEmpty()) {
                    opes.push(dataOperators);
                    continue;
                } else {
                    // 若栈顶元素优先级大于或等于要入栈的元素,将栈顶元素弹出并计算,然后入栈
                    if (isLevelType(opes.peek()) >= isLevelType(dataOperators)) {
                        BigDecimal ret = computerData(nums.pop(), nums.pop(), opes.pop());
                        nums.push(ret);
                    }
                    opes.push(dataOperators);
                }
            }
        }
        // 最后一个字符若是数字,未入栈
        while (!opes.isEmpty()) {
            BigDecimal ret = computerData(nums.pop(), nums.pop(), opes.pop());
            nums.push(ret);
        }
        return nums.pop();
    }

    /**
     * 返回的是运算符的优先级,数字和()不需要考虑
     *
     * @param operators
     * @return
     */
    private static int isLevelType(char operators) {
        if (operators == PLUS || operators == SUBTRACT) {
            return 1;
        } else if (operators == MULTIPLY || operators == DIVIDE) {
            return 2;
        } else {
            return FishContants.ZERO;
        }
    }

    /**
     * 运算次序是反的,跟入栈出栈次序有关
     *
     * @param prevData
     * @param nextData
     * @param operators
     * @return
     */
    private static BigDecimal computerData(BigDecimal prevData, BigDecimal nextData, char operators) {
        BigDecimal sum = null;
        if (operators == PLUS) {
            sum = prevData.add(nextData);
        } else if (operators == SUBTRACT) {
            sum = prevData.subtract(nextData);
        } else if (operators == MULTIPLY) {
            sum = prevData.multiply(nextData);
        } else if (operators == DIVIDE) {
            sum = prevData.divide(nextData);
        }
        return sum;
    }
}