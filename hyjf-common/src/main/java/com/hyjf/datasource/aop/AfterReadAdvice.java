/*
 * Copyright(c) 2012-2014 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.datasource.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

import com.hyjf.datasource.DatabaseContextHolder;

/**
 * 
 * 清除当前绑定的数据源
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月16日
 * @see 上午9:58:01
 */
@Component("afterRead")
public class AfterReadAdvice implements AfterReturningAdvice {

    /**
     * 
     * 方法执行完毕清除当前绑定的数据源
     * @author 任星臣
     * @param returnValue
     * @param method
     * @param args
     * @param target
     * @throws Throwable
     * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        DatabaseContextHolder.clearCustomerType();
    }

}
