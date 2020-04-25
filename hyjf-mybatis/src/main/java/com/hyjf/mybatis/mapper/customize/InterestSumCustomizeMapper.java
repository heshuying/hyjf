package com.hyjf.mybatis.mapper.customize;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 债权迁移后,计算累计收益
 * 
 * @author liuyang
 *
 */
public interface InterestSumCustomizeMapper {

	/**
	 * 根据用户Id查询用户的汇天利累计收益
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getHtlInvestInterestSum(Map<String, String> param);

	/**
	 * 根据用户查询用户汇转让的累计收益
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getHzrInvestInterestSum(Map<String, String> param);

	/**
	 * 根据用户Id查询用户的汇直投累计收益
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getHztInvestInterestSum(Map<String, String> param);

	/**
	 * 出让人出让的累计收益
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getHztCreditInterestSum(Map<String, String> param);

	/**
	 * 出让人收到承接的垫付利息
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getHzrCreditInterestAdvance(Map<String, String> param);

	/**
	 * 查询融通宝加息部分的收益
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getIncreaseInterestSum(Map<String, String> param);

	/**
	 * 根据用户ID查询优惠券的收益
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getCouponInterestSum(Map<String, String> param);
}
