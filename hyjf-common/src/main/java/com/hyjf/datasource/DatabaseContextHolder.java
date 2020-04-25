/*
 * Copyright(c) 2012-2014 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.datasource;

/**
 * 
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月16日
 * @see 上午9:25:54
 */
public class DatabaseContextHolder {

    private static final String MASTER = "master";

    private static final String SLAVE = "slave";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setCustomerType(String customerType) {
        contextHolder.set(customerType);
    }

    public static String getCustomerType() {
        return contextHolder.get();
    }

    public static void clearCustomerType() {
        contextHolder.remove();
    }

    public static void markMaster(){
        setCustomerType(MASTER);
    }

    public static void markSlave(){
        setCustomerType(SLAVE);
    }
}
