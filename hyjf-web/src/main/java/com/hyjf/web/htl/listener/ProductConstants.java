/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2015年12月28日 下午3:50:17
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.htl.listener;

import java.math.BigDecimal;

/**
 * @author Michael
 */

public class ProductConstants {

	/** 白海燕账户id */
	public static  int BAI_USERID = Integer.parseInt(ProductUtils.getProduct().getPnumber());
	/** 对公账户id **/
	public static  int PUB_USERID = Integer.parseInt(ProductUtils.getProduct().getPnumberNew());
	/** 汇天利利率 */
	public static  BigDecimal INTEREST_RATE = ProductUtils.getProduct().getInterestRate();
//	/** 出借人上限 */
//	public static  BigDecimal USER_PUPPER = ProductUtils.getProduct().getPupper();
//	/** 出借人上限 */
//	public static  BigDecimal USER_PLOWER = ProductUtils.getProduct().getPlower();
//	/** 产品上限 */
//	public static  BigDecimal ALL_PUPPER = ProductUtils.getProduct().getPlower();
//	/** 是否可转入 0:可转入 1:不可转入 */
//	public static  int IS_TENDER = ProductUtils.getProduct().getIsTender();
//	/** 是否可赎回 0:可赎回 1:不可赎回 */
//	public static  int IS_REDEEM = ProductUtils.getProduct().getIsRedeem();
	/** 汇天利利率-旧版*/
	public static BigDecimal OLD_INTEREST_RATE = new BigDecimal("0.06");
	/** 汇天利利率-第二版*/
	public static BigDecimal OLD_TWO_INTEREST_RATE = new BigDecimal("0.045");
	/** 汇天利利率-修改时间*/
	public static String  UPDATE_DATE = "2016-01-26";
	/** 汇天利利率-第二次修改时间*/
	public static String  UPDATE_DATE_TWO = "2016-04-14";
}

	