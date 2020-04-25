/**
 * 出借信息
 */

package com.hyjf.mybatis.mapper.customize.apiweb;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.apiweb.BorrowTenderInfoCustomize;
import com.hyjf.mybatis.model.customize.bifa.UserIdAccountSumBean;
import org.apache.ibatis.annotations.Param;


public interface BorrowTenderInfoCustomizeMapper {
	
	/**
	 * 取得标的信息
	 * @param paramMap
	 * @return
	 */
	BorrowTenderInfoCustomize getBorrowTenderInfo(Map<String,Object> paramMap);
	
	/**
	 * 取得标的信息
	 * @param paramMap
	 * @return
	 */
	Integer getCouponProfitTime(Map<String,Object> paramMap);
	
	/**
     * 根据borrowNid查询所发标的出借总金额
     * @param params
     * @return
     */
	String countMoneyByBorrowId(Map<String, Object> params);
	/**
	 * 北互金 散标出借成功(放款成功)
	 * @param daySubSeven
	 * @return
	 */
	List<UserIdAccountSumBean> getBorrowTenderAccountSum(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate);
}
