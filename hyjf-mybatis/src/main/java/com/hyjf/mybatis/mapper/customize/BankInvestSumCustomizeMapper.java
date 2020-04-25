package com.hyjf.mybatis.mapper.customize;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 统计出借人累计出借
 * 
 * @author liuyang
 *
 */
public interface BankInvestSumCustomizeMapper {
	/**
	 * 获取用户汇直投出借金额
	 * 
	 * @param paramMap
	 * @return
	 */
	BigDecimal getHZTTenderAccountSum(Map<String, String> paramMap);

	/**
	 * 获取用户汇消费出借金额
	 * 
	 * @param paramMap
	 * @return
	 */
	BigDecimal getHXFTenderAccountSum(Map<String, String> paramMap);

	/**
	 * 获取用户汇天利出借金额
	 * 
	 * @param paramMap
	 * @return
	 */
	BigDecimal getHTLTenderAccountSum(Map<String, String> paramMap);

	/**
	 * 获取用户汇添金出借金额
	 * 
	 * @param paramMap
	 * @return
	 */
	BigDecimal getHTJTenderAccountSum(Map<String, String> paramMap);

	/**
	 * 获取用户汇转让出借金额
	 * 
	 * @param paramMap
	 * @return
	 */
	BigDecimal getHZRTenderAccountSum(Map<String, String> paramMap);

}
