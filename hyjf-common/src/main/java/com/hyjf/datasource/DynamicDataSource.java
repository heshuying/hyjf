/*
 * Copyright(c) 2012-2014 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.datasource;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年3月16日
 * @see 上午9:25:44
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public static String[] dataSourceArray;

    public static String randomInt;

    public static final Integer MAX_INTEGER = 99999;

    public static final Random random = new Random();

    private static String readDataSourcePollPattern;

    private static Integer slaveCount;

    private AtomicInteger counter = new AtomicInteger(-1);

    static {
        ResourceBundle rb = ResourceBundle.getBundle("datasource");

        randomInt = rb.getString("datasource.read.randomInt");

        dataSourceArray = rb.getString("datasource.read").split(",");

        readDataSourcePollPattern = rb.getString("datasource.read.pattern");

        slaveCount = dataSourceArray.length;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        if("slave".equals(DatabaseContextHolder.getCustomerType())){
            if (readDataSourcePollPattern.equals("0")) {
                return getRandomReadDataSource();
            }else{
                Integer index = counter.incrementAndGet() % slaveCount;
                if (counter.get() > MAX_INTEGER) {
                    counter.set(-1);
                }
                return dataSourceArray[index];
            }
        }else{
            return null;
        }
    }

    public static String getRandomReadDataSource() {
        return dataSourceArray[random.nextInt(Integer.parseInt(randomInt))];
    }

}
