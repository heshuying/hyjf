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

import com.hyjf.mybatis.model.auto.UsersPortrait;
import com.hyjf.mybatis.model.customize.web.*;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApiProjectListCustomizeMapper {
    /**
     * 
     * 获取可出借项目列表（新）
     * @author pcc
     * @param params
     * @return
     */
    List<ApiProjectListCustomize> selectProjectListNew(Map<String, Object> params);

    /**
	 * 获取出借详细信息
	 * @param {instCode：机构编号（必填）,startTime:开始时间（必填），endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	List<InvestListCustomize> searchInvestListNew(Map<String, Object> params);

	/**
	 * 获取出借详细信息
	 * @param {instCode：机构编号（必填）,startTime:开始时间（必填），endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	List<RepayListCustomize> searchRepayList(Map<String, Object> params);

	/**
	 * 获取集团组织架构信息
	 * @param params
	 * @return
	 */
	List<OrganizationStructure> searchOrganizationList(
			Map<String, Object> params);

	/**
	 * 获取员工信息
	 * @param params
	 * @return
	 */
	List<Empinfo> searchEmpInfoList(Map<String, Object> params);

	/**
	 * 根据汇盈金服用户ID获取开户信息
	 * @param userIdList 用户ID
	 * @return
	 */
	List<InvestRepayCustomize> searchInvestRepaysListNew(@Param("userIdList")List<Integer> userIdList);

	/**
	 * 插入新数据
	 * @param usersPortrait
	 */
	void insertImformation(UsersPortrait usersPortrait);

}
