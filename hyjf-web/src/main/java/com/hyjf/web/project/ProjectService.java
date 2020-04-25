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
package com.hyjf.web.project;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectConsumeCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebVehiclePledgeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowHzcDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowVehiclePledgeCustomize;
import com.hyjf.web.BaseService;

public interface ProjectService extends BaseService {

	/**
	 * 查询项目列表
	 * 
	 * @param params
	 * @return
	 */
	List<WebProjectListCustomize> searchProjectList(Map<String, Object> params);

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
	WebProjectPersonDetailCustomize searchProjectPersonDetail(String borrowNid);
	
	/**
	 * 查询相应的个人项目的项目详情(汇添金项目专用)
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectPersonDetailCustomize searchHTJProjectPersonDetail(String borrowNid);

	/**
	 * 查询相应的企业项目的项目详情
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectCompanyDetailCustomize searchProjectCompanyDetail(String borrowNid);


	/**
	 * 查询相应的企业项目的项目详情(汇添金项目专用)
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectCompanyDetailCustomize searchHTJProjectCompanyDetail(String borrowNid);
	
	/**
	 * 查询项目的用户出借列表
	 * 
	 * @param params
	 * @return
	 */
	List<WebProjectInvestListCustomize> searchProjectInvestList(Map<String, Object> params);

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
	List<WebProjectConsumeCustomize> searchProjectConsumeList(Map<String, Object> params);

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
	WebProjectDetailCustomize selectProjectDetail(String borrowNid);
	
	/**
	 * 根据项目id获取项目信息（预览调用）
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectDetailCustomize selectProjectPreview(String borrowNid);
	
	/**
	 * 根据项目id获取汇添金项目信息(预览调用)
	 * @Title selectHTJProjectPreview
	 * @param borrowNid
	 * @return
	 */
	WebProjectDetailCustomize selectHTJProjectPreview(String borrowNid);
	

	/**
	 * 
	 * 查询相应的汇资产信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebHzcProjectDetailCustomize searchHzcProjectDetail(String borrowNid);

	/**
	 * 
	 * 查询相应的汇资产信息(汇添金项目专用)
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebHzcProjectDetailCustomize searchHTJHzcProjectDetail(String borrowNid);
	
	/**
	 * 查询相应的认证信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<WebProjectAuthenInfoCustomize> searchProjectAuthenInfos(String borrowNid);

	/**
	 * 查询相应的认证信息(汇添金专用)
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<WebProjectAuthenInfoCustomize> searchHTJProjectAuthenInfos(String borrowNid);

	
	/**
	 * 计算获取还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<RepayPlanBean> getRepayPlan(String borrowNid);

	/**
	 * 根据相应的borrowNid获取相应的项目的处置预案
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebHzcDisposalPlanCustomize searchDisposalPlan(String borrowNid);
	
	/**
	 * 根据相应的borrowNid获取相应的项目的处置预案(汇添金专用)
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebHzcDisposalPlanCustomize searchHTJDisposalPlan(String borrowNid);

	/**
	 * 根据borrowNid获取风控信息
	 * @param borrowNid
	 * @return
	 */
	WebRiskControlCustomize selectRiskControl(String borrowNid);
	
	/**
	 * 根据borrowNid获取风控信息(汇添金项目专用)
	 * @param borrowNid
	 * @return
	 */
	WebRiskControlCustomize selectHTJRiskControl(String borrowNid);

	/**
	 * 根据项目编号获取相应的房屋抵押信息
	 * @param borrowNid
	 * @return
	 */
	List<WebMortgageCustomize> selectMortgageList(String borrowNid);
	
	/**
	 * 根据项目编号获取相应的房屋抵押信息(汇添金专用)
	 * @param borrowNid
	 * @return
	 */
	List<WebMortgageCustomize> selectHTJMortgageList(String borrowNid);

	/**
	 * 根据项目编号获取相应的车辆抵押信息
	 * @param borrowNid
	 * @return
	 */
	List<WebVehiclePledgeCustomize> selectVehiclePledgeList(String borrowNid);

	/**
	 * 根据项目编号获取相应的车辆抵押信息(汇添金专用)
	 * @param borrowNid
	 * @return
	 */
	List<WebVehiclePledgeCustomize> selectHTJVehiclePledgeList(String borrowNid);
	/**
	 * 根据项目编号获取债券信息列表
	 * @param borrowNid
	 * @return
	 */
	List<WebProjectConsumeCustomize> selectProjectConsumeList(Map<String, Object> params);

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
	List<ProjectFileBean> searchProjectFiles(String borrowNid,String url);
	
	/**
	 * 根据项目id获取文件(汇添金项目)
	 * @param borrowNid
	 * @return
	 */
	List<ProjectFileBean> searchHTJProjectFiles(String borrowNid,String url);


	/**
	 * 根据userid获取登录用户信息
	 * @param userId
	 * @return
	 */
	Users searchLoginUser(Integer userId);

	Borrow selectBorrowByNid(String borrowNid);

	int countUserInvest(Integer userId, String borrowNid);
	
	/**
	 * 获取最优优惠券
	 * @author pcc
	 * @param borrowNid
	 * @param string
	 * @param money
	 * @param platform
	 * @return
	 */
    UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform);

    UserCouponConfigCustomize getBestCouponById(String couponId);

	DebtPlanBorrowDetailCustomize selectHtjProjectDetail(String borrowNid);

	DebtPlanBorrowHzcDetailCustomize searchHzcPlanProjectDetail(String borrowNid);

	DebtPlanBorrowHzcDisposalPlanCustomize searchDebtPlanBorrowHzcDisposalPlan(
			String borrowNid);

	List<RepayPlanBean> getHtjRepayPlan(String borrowNid);

	List<ProjectFileBean> searchDebtPlanBorrowFiles(String borrowNid,
			String hOST_URL);

	DebtPlanBorrowCompanyDetailCustomize selectProjectCompanyDetail(String borrowNid);

	DebtPlanBorrowPersonDetailCustomize selectDebtPlanBorrowPersonDetail(
			String borrowNid);

	DebtPlanBorrowRiskControlCustomize selectDebtPlanBorrowRiskControl(String borrowNid);

	List<DebtPlanBorrowMortgageCustomize> selectDebtPlanBorrowMortgageList(String borrowNid);

	List<DebtPlanBorrowVehiclePledgeCustomize> selectDebtPlanBorrowVehiclePledgeList(
			String borrowNid);

	List<DebtPlanBorrowAuthenInfoCustomize> searchDebtPlanBorrowAuthenInfo(
			String borrowNid);

	int countUserDebtInvest(Integer userId, String borrowNid);

	DebtBorrow selectDebtBorrowByNid(String borrowNid);

	int countDebtPlanProjectInvestRecordTotal(Map<String, Object> params);

	List<WebProjectInvestListCustomize> searchDeptPlanProjectInvestList(
			Map<String, Object> params);

}
