package cn._42pay.simplepay.framework.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn._42pay.simplepay.framework.log.constant.LogParamType;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import ch.qos.logback.classic.Level;

/**
 * Created by kevin on 2017/12/4.
 * 只针对logback的方式，log4j不支持
 *
 */
public  final class Log {
    private static ch.qos.logback.classic.Logger innerLogger;

    private static final String FQCN = Log.class.getName();
    private static final String SIMPLEPAY_LOG="[simple-pay]";

    private static Method filterAndLog_0_Or3PlusInvoke;
    private static Method filterAndLog_1Invoke;
    private static Method filterAndLog_2Invoke;

    static {
        innerLogger= (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Log.class);
        try {
            filterAndLog_0_Or3PlusInvoke = innerLogger.getClass().getDeclaredMethod("filterAndLog_0_Or3Plus", String.class, Marker.class, Level.class, String.class, Object[].class, Throwable.class);
            filterAndLog_0_Or3PlusInvoke.setAccessible(true);

            filterAndLog_1Invoke=innerLogger.getClass().getDeclaredMethod("filterAndLog_1", String.class, Marker.class, Level.class, String.class, Object.class, Throwable.class);
            filterAndLog_1Invoke.setAccessible(true);

            filterAndLog_2Invoke=innerLogger.getClass().getDeclaredMethod("filterAndLog_2", String.class, Marker.class, Level.class, String.class, Object.class,Object.class, Throwable.class);
            filterAndLog_2Invoke.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            innerLogger.error("load failed, Exception:" + e);
        }
    }

    /**
     *  错误日志打印1
     * @param logScene 日志场景
     * @param methodName  方法名
     * @param logParamType 日志参数类型
     * @param index  索引 订单号，就诊人卡号
     * @param message  消息内容
     */
    public static void e(LogScene logScene, String methodName, LogParamType logParamType, String index, String message){
        String msg=buildMsg(logScene,methodName,logParamType,index,message);
        error(msg);
    }

    /**
     * 错误日志打印2
     * @param logScene 日志场景
     * @param methodName  方法名
     * @param logParamType 日志参数类型
     * @param index  索引 订单号，就诊人卡号
     * @param message  消息内容
     * @param argArray
     */
    public static void e(LogScene logScene, String methodName, LogParamType logParamType, String index, String message, Object... argArray){
        String msg=buildMsg(logScene,methodName,logParamType,index,message);
        error(msg,argArray);
    }

    /**
     * 错误日志打印3
     * @param logScene 日志场景
     * @param methodName  方法名
     * @param logParamType 日志参数类型
     * @param index  索引 订单号，就诊人卡号
     * @param message  消息内容
     * @param t
     */
    public static void e(LogScene logScene, String methodName, LogParamType logParamType, String index, String message, Throwable t){
        String msg=buildMsg(logScene,methodName,logParamType,index,message);
        error(msg,t);
    }

    /**
     * 错误日志打印4
     * @param logScene 日志场景
     * @param message  消息内容
     */
    public static void e(LogScene logScene, String message){
        String msg=buildMsg(logScene,message);
        error(msg);
    }

    /**
     * 错误日志打印5
     * @param logScene 日志场景
     * @param message  消息内容
     * @param argArray
     */
    public static void e(LogScene logScene, String message, Object... argArray){
        String msg=buildMsg(logScene,message);
        error(msg,argArray);
    }

    /**
     * 错误日志打印6
     * @param logScene 日志场景
     * @param message  消息内容
     * @param t
     */
    public static void e(LogScene logScene, String message, Throwable t){
        String msg=buildMsg(logScene,message);
        error(msg,t);
    }

    /**
     * 错误日志打印7
     * @param logScene
     * @param index
     * @param message
     * @param t
     */
    public static void e(LogScene logScene, String index, String message, Throwable t){
        String msg=buildMsg(logScene,index,message);
        error(msg,t);
    }

    /**
     * 正常日志打印1
     * @param logScene 日志场景
     * @param methodName  方法名
     * @param logParamType 日志参数类型
     * @param index  索引 订单号，就诊人卡号
     * @param message  消息内容
     */
    public static void i(LogScene logScene, String methodName, LogParamType logParamType, String index, String message){
        String msg=buildMsg(logScene,methodName,logParamType,index,message);
        info(msg);
    }

    /**
     * 正常日志打印2
     * @param logScene 日志场景
     * @param methodName  方法名
     * @param logParamType 日志参数类型
     * @param index  索引
     * @param message  消息内容
     * @param argArray
     */
    public static void i(LogScene logScene, String methodName, LogParamType logParamType, String index, String message, Object... argArray){
        String msg=buildMsg(logScene,methodName,logParamType,index,message);
        info(msg,argArray);
    }

    /**
     * 正常日志打印3
     * @param logScene 日志场景
     * @param message  消息内容
     */
    public static void i(LogScene logScene, String message){
        String msg=buildMsg(logScene,message);
        info(msg);
    }

    /**
     * 正常日志打印4
     * @param logScene 日志场景
     * @param message  消息内容
     * @param argArray
     */
    public static void i(LogScene logScene, String message, Object... argArray){
        String msg=buildMsg(logScene,message);
        info(msg,argArray);
    }

    /**
     * 正常日志打印
     * @param logScene
     * @param index
     * @param message
     */
    public static void i(LogScene logScene, String index, String message){
        String msg=buildMsg(logScene,index,message);
        info(msg);
    }

    /**
     *  正常日志打印
     * @param logScene
     * @param index
     * @param message
     * @param argArray
     */
    public static void i(LogScene logScene, String index, String message, Object... argArray){
        String msg=buildMsg(logScene,index,message);
        info(msg,argArray);
    }

    /**
     * 记录网关的日志
     * @param logScene
     * @param methodName
     * @param logParamType
     * @param time
     * @param message
     */
    public static void i(LogScene logScene, String methodName, LogParamType logParamType,String index, long time, String message){
        String msg=buildMsg(logScene,methodName,logParamType,index,time,message);
        info(msg);
    }

    public static void i(LogScene logScene, String methodName, LogParamType logParamType, long time, String message){
        String msg=buildMsg(logScene,methodName,logParamType,time,message);
        info(msg);
    }

    private static void error(String msg) {
        filterAndLog_0_Or3Plus(FQCN, null, Level.ERROR, msg, null, null);
    }

    private static void error(String format, Object arg) {
            filterAndLog_1(FQCN, null, Level.ERROR, format, arg, null);
    }

    private static void error(String format, Object arg1, Object arg2) {
        filterAndLog_2(FQCN, null, Level.ERROR, format, arg1, arg2, null);
    }

    private static void error(String format, Object... argArray) {
        filterAndLog_0_Or3Plus(FQCN, null, Level.ERROR, format, argArray, null);
    }

    private static void error(String msg, Throwable t) {
        filterAndLog_0_Or3Plus(FQCN, null, Level.ERROR, msg, null, t);
    }

    private static void info(String msg) {
        filterAndLog_0_Or3Plus(FQCN, null, Level.INFO, msg, null, null);
    }

    private static void info(String format, Object arg) {
        filterAndLog_1(FQCN, null, Level.INFO, format, arg, null);
    }

    private static void info(String format, Object arg1, Object arg2) {
        filterAndLog_2(FQCN, null, Level.INFO, format, arg1, arg2, null);
    }

    private static void info(String format, Object... argArray) {
        filterAndLog_0_Or3Plus(FQCN, null, Level.INFO, format, argArray, null);
    }

    private static void info(String msg, Throwable t) {
        filterAndLog_0_Or3Plus(FQCN, null, Level.INFO, msg, null, t);
    }

    private static void filterAndLog_0_Or3Plus(String localFQCN, Marker marker, Level level, String msg, Object[] params, Throwable t) {
        try {
            filterAndLog_0_Or3PlusInvoke.invoke(innerLogger, localFQCN, marker, level, msg, params, t);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            innerLogger.error("execute filterAndLog_0_Or3PlusInvoke failed, Exception:" + e);
        }
    }

    private static void filterAndLog_1(String localFQCN, Marker marker, Level level, String msg, Object param, Throwable t) {
        try {
            filterAndLog_1Invoke.invoke(innerLogger, localFQCN, marker, level, msg, param, t);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            innerLogger.error("execute filterAndLog_1 failed, Exception:" + e);
        }
    }

    private static void filterAndLog_2(String localFQCN, Marker marker, Level level, String msg, Object param1, Object param2, Throwable t) {
        try {
            filterAndLog_2Invoke.invoke(innerLogger, localFQCN, marker, level, msg, param1,param2, t);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            innerLogger.error("execute filterAndLog_2 failed, Exception:" + e);
        }
    }

    private static String buildMsg(LogScene logScene, String index, String message){
        return buildMsg(logScene,"",null,index,-1,message);
    }

    private static String buildMsg(LogScene logScene, String message){
        return buildMsg(logScene,"",null,"",-1,message);
    }

    private static String buildMsg(LogScene logScene, String methodName, LogParamType logParamType, String index, String message){
        return buildMsg(logScene,methodName,logParamType,index,-1,message);
    }

    private static String buildMsg(LogScene logScene, String methodName, LogParamType logParamType, long time, String message){
        return buildMsg(logScene,methodName,logParamType,"",time,message);
    }

    private static String buildMsg(LogScene logScene, String methodName, LogParamType logParamType, String index, long time, String message){
        StringBuilder sb=new StringBuilder();
        sb.append(SIMPLEPAY_LOG);
        sb.append("[").append(logScene.getDesc()).append("]");
        sb.append("[").append(methodName).append("]");

        if(logParamType!=null){
            sb.append("[").append(logParamType.getDesc()).append("]");
        }else{
            sb.append("[").append("").append("]");
        }
        if(time>=0){
            sb.append("[").append("耗时:").append(time).append("ms").append("]");
        }
        sb.append("(").append(index).append(")");
        sb.append("->");
        sb.append(message);
        return sb.toString();
    }
}
