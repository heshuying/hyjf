/*
 * Copyright(c) 2012-2014 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.datasource.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import com.hyjf.datasource.DatabaseContextHolder;
import com.hyjf.datasource.DynamicDataSource;

/**
 * 
 * 动态进行数据源的切换
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月16日
 * @see 上午9:58:22
 */
@Component("beforeRead")
public class BeforeReadAdvice implements MethodBeforeAdvice {

    /**
     * 
     * 动态进行数据源的切换
     * @author 任星臣
     * @param method
     * @param args
     * @param target
     * @throws Throwable
     * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method,
     *      java.lang.Object[], java.lang.Object)
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        //String dataSourceName = DynamicDataSource.getRandomReadDataSource();
        DatabaseContextHolder.markSlave();
    }

}
