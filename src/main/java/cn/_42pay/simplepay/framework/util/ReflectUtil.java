package cn._42pay.simplepay.framework.util;

import java.lang.reflect.Method;

/**
 * Created by kevin on 2018/5/20.
 */
public class ReflectUtil {
    /**
     * 根据方法名找出对应的方法
     * @param methodName
     * @param ownerClass
     * @return
     */
    public static Method findMethod(String methodName, Class ownerClass ){
        Method method=null;
        for (Method m : ownerClass.getDeclaredMethods()) {
            if (m.getName().equalsIgnoreCase(methodName)) {
                method = m;
                break;
            }
        }
        return method;
    }
}
