package com.hyjf.wechat.util;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.web.servlet.tags.Param;

public class ParameterNameUtils {
    /**
     * 获取指定方法的参数名
     * 
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表
     */
    public static String[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        String[] parameterNames = new String[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    parameterNames[i++] = param.getName();
                }
            }
        }
        return parameterNames;
    }
}
