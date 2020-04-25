/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author PC-LIUSHOUYI
 * @version BeanToStringUtil, v0.1 2018/11/22 20:46
 */
public class BeanToStringUtil {

    /**
     * 实体bean转带分隔符的String字符串(默认忽略serialVersionUID字段)
     *
     * @param model    实体bean
     * @param delimit  分隔符
     * @param delEnter 是否去除回车换行
     * @return
     */
    public static String beanToString(Object model, String delimit, boolean delEnter) {
        try {
            String result = "";
            // 获取实体类的所有属性，返回Field数组
            Field[] field = model.getClass().getDeclaredFields();
            // 获取属性的名字
            String[] modelName = new String[field.length];
            String[] modelType = new String[field.length];
            for (int i = 0; i < field.length; i++) {
                // 获取属性的名字
                String name = field[i].getName();
                modelName[i] = name;
                // 获取属性类型
                String type = field[i].getGenericType().toString();
                modelType[i] = type;

                //关键。。。可访问私有变量
                field[i].setAccessible(true);

                // 将属性的首字母大写
                name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                        .toUpperCase());

                // 忽略serialVersionUID字段
                if ("SerialVersionUID".equals(name)) {
                    continue;
                } else {
                    if (i != 0) {
                        // 拼接分割字符串
                        result = result.concat(delimit);
                    }
                    // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = model.getClass().getMethod("get" + name);
                    // 调用getter方法获取属性值
                    if (null != m.invoke(model)) {
                        // 拼接各个字段的值，空的情况只拼接分隔符
                        result = result.concat(m.invoke(model).toString());
                    }
                }
            }

            // 是否去掉行数据中多余的回车换行
            if (delEnter) {
                result = result.replace(StringPool.ASCII_TABLE[13], "");
                result = result.replace(StringPool.ASCII_TABLE[10], "");
            }
            // 拼接成功返回字符串
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
