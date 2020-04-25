/**
 * Description:项目列表查询service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.app.hjhasset;

import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.app.AppHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.app.AppHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppMortgageCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectConsumeCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.app.AppVehiclePledgeCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;

public interface HjhProjectService extends BaseService {

	/**
	 * 查询项目列表
	 * 
	 * @param params
	 * @return
	 */
	List<AppProjectListCustomize> searchProjectList(Map<String, Object> params);

	/**
	 * 统计项目列表总数
	 * 
	 * @param params
	 * @return
	 */
	int countProjectListRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的个人项目的项目详情
	 * 
	 * @param borrowNid
	 * @return
	 */
	AppProjectPersonDetailCustomize searchProjectPersonDetail(String borrowNid);

	/**
	 * 查询相应的企业项目的项目详情
	 * 
	 * @param borrowNid
	 * @return
	 */
	AppProjectCompanyDetailCustomize searchProjectCompanyDetail(String borrowNid);

	/**
	 * 查询项目的用户出借列表
	 * 
	 * @param params
	 * @return
	 */
	List<AppProjectInvestListCustomize> searchProjectInvestList(Map<String, Object> params);

	/**
	 * 统计用户出借的项目总数
	 * 
	 * @param params
	 * @return
	 */
	int countProjectInvestRecordTotal(Map<String, Object> params);

	/**
	 * 查询汇消费项目的打包信息
	 * 
	 * @param params
	 * @return
	 */
	List<AppProjectConsumeCustomize> searchProjectConsumeList(Map<String, Object> params);

	/**
	 * 统计打包数据的项目总数
	 * 
	 * @param params
	 * @return
	 */
	int countProjectConsumeRecordTotal(Map<String, Object> params);

	/**
	 * 根据项目id获取项目信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	AppProjectDetailCustomize selectProjectDetail(String borrowNid);

	/**
	 * 
	 * 查询相应的汇资产信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	AppHzcProjectDetailCustomize searchHzcProjectDetail(String borrowNid);

	/**
	 * 查询相应的认证信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<AppProjectAuthenInfoCustomize> searchProjectAuthenInfos(String borrowNid);

	/**
	 * 计算获取还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<HjhProjectRepayPlanBean> getRepayPlan(String borrowNid);

	/**
	 * 根据相应的borrowNid获取相应的项目的处置预案
	 * 
	 * @param borrowNid
	 * @return
	 */
	AppHzcDisposalPlanCustomize searchDisposalPlan(String borrowNid);

	/**
	 * 根据borrowNid获取风控信息
	 * @param borrowNid
	 * @return
	 */
	AppRiskControlCustomize selectRiskControl(String borrowNid);

	/**
	 * 根据项目编号获取相应的房屋抵押信息
	 * @param borrowNid
	 * @return
	 */
	List<AppMortgageCustomize> selectMortgageList(String borrowNid);

	/**
	 * 根据项目编号获取相应的车辆抵押信息
	 * @param borrowNid
	 * @return
	 */
	List<AppVehiclePledgeCustomize> selectVehiclePledgeList(String borrowNid);

	/**
	 * 根据项目编号获取债券信息列表
	 * @param params
	 * @return
	 */
	List<AppProjectConsumeCustomize> selectProjectConsumeList(Map<String, Object> params);

	/**
	 * 统计债权总数
	 * @param params
	 * @return
	 */
	int countProjectConsumeListRecordTotal(Map<String, Object> params);

	/**
	 * 根据项目id获取文件
	 * @param borrowNid
	 * @return
	 */
	List<HjhProjectFileBean> searchProjectFiles(String borrowNid,String url);

	/**
	 * 根据userid获取登录用户信息
	 * @param userId
	 * @return
	 */
	Users searchLoginUser(Integer userId);
	
	/**
	 * 校验当前用户是否出借过指定项目
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	boolean checkTenderByUser(Integer userId,String borrowNid);

    List<BorrowCarinfo> selectBorrowCarInfo(String borrowNid);

	/**
	 * 汇计划列表查询 - 按照类型查询
	 * 冗余com.hyjf.web.hjhplan.HjhPlanService.searchHjhPlanList  无意改变依赖结构，所以这么做
	 * @param params
	 * @return
	 */
    List<HjhPlanCustomize> searchHjhPlanList(Map<String, Object> params);
}
