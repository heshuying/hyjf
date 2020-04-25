/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringContextUtil提供手动对bean的操作
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月18日
 * @see 下午1:19:32
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        SpringContextUtil.applicationContext = arg0;
    }

    /**
     * 
     * 获取applicationContext对象
     * @author renxingchen
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 
     * 根据bean的id来查找对象
     * @author renxingchen
     * @param id
     * @return
     */
    public static Object getBeanById(String id) {
        return applicationContext.getBean(id);
    }

    /**
     * 
     * 根据bean的name来查找对象
     * @author renxingchen
     * @param name
     * @return
     */
    public static Object getBeanByName(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 
     * 根据bean的class来查找对象
     * @author renxingchen
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getBeanByClass(Class c) {
        return applicationContext.getBean(c);
    }

}
