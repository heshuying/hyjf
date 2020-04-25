/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.web.*;
import org.apache.ibatis.annotations.Param;

public interface WebUserRepayListCustomizeMapper {
	/**  查询借款人借款列表  */
	List<WebUserRepayProjectListCustomize> selectUserRepayProjectList(Map<String, Object> params);
	/** 查询借款人借款数量   */
	int countUserRepayProjectRecordTotal(Map<String, Object> params);
	/**  查询垫付机构借款列表   */
	List<WebUserRepayProjectListCustomize> selectOrgRepayProjectList(Map<String, Object> params);
	/**  查询垫付机构借款数  */
	int countOrgRepayProjectRecordTotal(Map<String, Object> params);
	
	List<WebUserRepayCustomize> selectUserRepay(Map<String, Object> params);
	
	List<WebUserRepayDetailListCustomize> selectUserRepayDetailList(Map<String, Object> params);

	List<WebUserRepayCustomize> selectUserRepayPlanList(Map<String, Object> params);

	List<WebUserRepayDetailListCustomize> selectUserRepayPlanDetailList(Map<String, Object> params);
    /**
     * 查询垫付机构的未还款金额
     * @param userId
     * @return
     */
	BigDecimal selectRepayOrgRepaywait(@Param("userId") Integer userId);
	
    /**
     * 查询垫付机构的待收垫付总额
     * @param userId
     * @return
     */
	BigDecimal selectUncollectedRepayOrgRepaywait(@Param("userId") Integer userId);
	
	/**
	 * 查询垫付机构已垫付项目数量
	 * @param params
	 * @return
	 */
	int countOrgRepayRecordTotal(Map<String, Object> params);
	/**
	 * 查询垫付机构已垫付的项目列表
	 * @param params
	 * @return
	 */
	List<WebUserRepayProjectListCustomize> searchOrgRepayList(Map<String, Object> params);

    int countUserPayProjectRecordTotal(Map<String, Object> params);


    WebUserTransferBorrowInfoCustomize getBorrowInfo(String borrowNid);
	/**
	 * 用户待还债转详情列表 非计划
	 * @param paraMap
	 * @return
	 */
	List<WebUserRepayTransferCustomize> selectUserRepayTransferListByCreditTender(Map<String, Object> paraMap);

	/**
	 * 用户待还债转详情列表 计划
	 * @param paraMap
	 * @return
	 */
    List<WebUserRepayTransferCustomize> selectUserRepayTransferListByHjhCreditTender(Map<String, Object> paraMap);

	/**
	 * 用户待还债转详情列表总条数 非计划
	 * @param borrowNid
	 * @return
	 */
    int selectUserRepayTransferListTotalByCreditTender(String borrowNid);

	/**
	 * 用户待还债转详情列表总条数 计划
	 * @param borrowNid
	 * @return
	 */
    int selectUserRepayTransferListTotalByHjhCreditTender(String borrowNid);

}
