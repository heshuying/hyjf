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

package com.hyjf.mybatis.mapper.customize.app;

import com.hyjf.mybatis.model.customize.app.*;
import com.hyjf.mybatis.model.customize.wechat.WechatHomeProjectListCustomize;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppProjectListCustomizeMapper {

	List<AppProjectListCustomize> selectProjectList(Map<String, Object> params);

	int countProjectListRecordTotal(Map<String, Object> params);

	AppProjectPersonDetailCustomize selectProjectPersonDetail(@Param("borrowNid") String borrowNid);

	AppProjectCompanyDetailCustomize selectProjectCompanyDetail(@Param("borrowNid") String borrowNid);

	AppProjectDetailCustomize selectProjectDetail(@Param("borrowNid") String borrowNid);

	AppHzcProjectDetailCustomize searchHzcProjectDetail(@Param("borrowNid") String borrowNid);

	List<AppProjectAuthenInfoCustomize> searchProjectAuthenInfos(@Param("borrowNid") String borrowNid);

	AppHzcDisposalPlanCustomize searchDisposalPlan(@Param("borrowNid") String borrowNid);

	AppRiskControlCustomize selectRiskControl(@Param("borrowNid") String borrowNid);

	List<AppVehiclePledgeCustomize> selectVehiclePledgeList(@Param("borrowNid") String borrowNid);

	List<AppMortgageCustomize> selectMortgageList(@Param("borrowNid") String borrowNid);

	List<AppProjectConsumeCustomize> selectProjectConsumeList(Map<String, Object> params);

	int countProjectConsumeListRecordTotal(Map<String, Object> params);

	List<AppProjectInvestListCustomize> selectProjectInvestList(Map<String, Object> params);

	int countProjectInvestRecordTotal(Map<String, Object> params);

	String searchProjectFiles(@Param("borrowNid")String borrowNid);
	
	String searchHTJProjectFiles(@Param("borrowNid")String borrowNid);

	List<AppProjectListCustomize> selectHomeProjectList(Map<String, Object> params);
	
	List<AppProjectListCustomize> selectHomeRepayProjectList(Map<String, Object> params);
	
	/**
	 * 异步获取首页标的列表
	 * @param params
	 * @return
	 */
	List<WechatHomeProjectListCustomize> selectHomeProjectListAsyn(Map<String, Object> params);
	/**
	 * 微信端首页汇计划加载两条稍后开启
	 * @return
	 */
	List<WechatHomeProjectListCustomize> selectHomeHjhOpenLaterList();
	/**
	 * 首页无可投散标加载两条还款中和复审中记录
	 * @return
	 */
	List<WechatHomeProjectListCustomize> selectHomeRepaymentsProjectList();
	
	
}
