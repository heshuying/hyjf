/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月28日 下午2:51:13
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.htl.listener;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Product;

/**
 * @author 李深强
 */

public class ProductUtils {

	private static Product product = null;

	/**
	 * @param product
	 */

	public ProductUtils(Product product) {
		ProductUtils.product = product;
	}

	public static Product getProduct() {
		return product;
	}
	
	
    /**
     * 新版汇天利利息算法 2016-04-15 上线
     * 汇天利利息计算    {本金 * (0.06 / 360)} 保留两位小数
     * @param amount 本金
     * @param investDate 出借时间 yyyy-MM-dd
     */
    public static BigDecimal getInterest(BigDecimal amount,String investDate){
    	BigDecimal interestAmount = BigDecimal.ZERO;
    	if(amount == null || amount.compareTo(BigDecimal.ZERO) == 0 ){
    		return interestAmount;
    	}
    	if(StringUtils.isEmpty(investDate)){
    		return interestAmount;
    	}
    	try {
	    	//出借时间
	    	int idate = Integer.parseInt(GetDate.get10Time(investDate));
	    	//修改利率日期
	    	int mdate = Integer.parseInt(GetDate.get10Time(ProductConstants.UPDATE_DATE));
	    	//第二次修改利率日期
	    	int twodate = Integer.parseInt(GetDate.get10Time(ProductConstants.UPDATE_DATE_TWO));
	    	//出借时间在二次修改之后 现利率计算
	    	if(idate > twodate){
	    		int validDays = GetDate.daysBetween(investDate, GetDate.getDate("yyyy-MM-dd"));
		    	if(validDays > 0){
		    		interestAmount = amount.multiply(ProductConstants.INTEREST_RATE)
		    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
					        .multiply(new BigDecimal(validDays));
		    		interestAmount = interestAmount.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN);
		    	}
	    	}else if(idate <= twodate && idate > mdate){ //出借时间在第一次修改和第二次修改之间
	    		int validDays1 = GetDate.daysBetween(investDate, ProductConstants.UPDATE_DATE_TWO);
	    		int validDays2 = GetDate.daysBetween(ProductConstants.UPDATE_DATE_TWO, GetDate.getDate("yyyy-MM-dd"));
	    		BigDecimal interestAmount1 = amount.multiply(ProductConstants.OLD_TWO_INTEREST_RATE)
	    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
	    				.multiply(new BigDecimal(validDays1));
	    		BigDecimal interestAmount2 = amount.multiply(ProductConstants.INTEREST_RATE)
	    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
	    				.multiply(new BigDecimal(validDays2));
	    		interestAmount = interestAmount1.add(interestAmount2);
	    		interestAmount = interestAmount.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN);
	    		
	    	}else { //在第一次修改之前
	    		int validDays1 = GetDate.daysBetween(investDate, ProductConstants.UPDATE_DATE);
	    		int validDays2 = GetDate.daysBetween(ProductConstants.UPDATE_DATE,ProductConstants.UPDATE_DATE_TWO);
	    		int validDays3 = GetDate.daysBetween(ProductConstants.UPDATE_DATE_TWO, GetDate.getDate("yyyy-MM-dd"));
	    		BigDecimal interestAmount1 = amount.multiply(ProductConstants.OLD_INTEREST_RATE)
	    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
	    				.multiply(new BigDecimal(validDays1));
	    		BigDecimal interestAmount2 = amount.multiply(ProductConstants.OLD_TWO_INTEREST_RATE)
	    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
	    				.multiply(new BigDecimal(validDays2));
	    		BigDecimal interestAmount3 = amount.multiply(ProductConstants.INTEREST_RATE)
	    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
	    				.multiply(new BigDecimal(validDays3));
	    		interestAmount = interestAmount1.add(interestAmount2).add(interestAmount3);
	    		interestAmount = interestAmount.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN);
	    	}
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return interestAmount;
    }

	
    /**
     * 新版汇天利利息算法 2016-01-27 上线
     * 汇天利利息计算    {本金 * (0.06 / 360)} 保留两位小数
     * @param amount 本金
     * @param investDate 出借时间 yyyy-MM-dd
     */
    public static BigDecimal getInterestTwo(BigDecimal amount,String investDate){
    	BigDecimal interestAmount = BigDecimal.ZERO;
    	if(amount == null || amount.compareTo(BigDecimal.ZERO) == 0 ){
    		return interestAmount;
    	}
    	if(StringUtils.isEmpty(investDate)){
    		return interestAmount;
    	}
    	int validDays;

    	try {
	    	//出借时间
	    	int idate = Integer.parseInt(GetDate.get10Time(investDate));
	    	//修改利率日期
	    	int mdate = Integer.parseInt(GetDate.get10Time(ProductConstants.UPDATE_DATE));
	    	if(idate > mdate){
				validDays = GetDate.daysBetween(investDate, GetDate.getDate("yyyy-MM-dd"));
		    	if(validDays > 0){
		    		interestAmount = amount.multiply(ProductConstants.INTEREST_RATE)
		    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
					        .multiply(new BigDecimal(validDays));
		    		interestAmount = interestAmount.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN);
		    	}
	    	}else{
	    		int validDays1 = GetDate.daysBetween(investDate, ProductConstants.UPDATE_DATE);
	    		int validDays2 = GetDate.daysBetween(ProductConstants.UPDATE_DATE, GetDate.getDate("yyyy-MM-dd"));
	    		BigDecimal interestAmount1 = amount.multiply(ProductConstants.OLD_INTEREST_RATE)
	    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
	    				.multiply(new BigDecimal(validDays1));
	    		BigDecimal interestAmount2 = amount.multiply(ProductConstants.INTEREST_RATE)
	    				.multiply(new BigDecimal(Math.pow(new BigDecimal(360).doubleValue(), -1)))
	    				.multiply(new BigDecimal(validDays2));
	    		interestAmount = interestAmount1.add(interestAmount2);
	    		interestAmount = interestAmount.divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return interestAmount;
    }


	/**
	 * 判断BigDecimal 是否为整数
	 * @param bd
	 * @return
	 */
    public static  boolean isIntegerValue(BigDecimal bd) {  
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;  
      } 


}
