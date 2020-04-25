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
package com.hyjf.app.project;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.app.AppCouponInfoCustomize;
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

public interface ProjectService extends BaseService {

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
	List<RepayPlanBean> getRepayPlan(String borrowNid);

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
	List<ProjectFileBean> searchProjectFiles(String borrowNid,String url);

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

    Integer countHjhPlan(Map<String, Object> params);

	/**
	 * 定时发标剩余时间
	 * @param borrowNid
	 * @return
	 */
	String getTimeLeft(String borrowNid);

	/**
	 * 查询可投金额
	 * @param borrowNid
	 * @return
	 */
	String getBorrowAccountWait(String borrowNid);

	/**
	 * 查询项目类型
	 * @param borrowType
	 * @return
	 */
	String getProjectType(String borrowType);


	/**
	 * 查询代收金额
	 * @param params
	 * @return
	 */
	String getMyProjectWaitAmountTotal(Map<String, Object> params);

	/**
	 * 查询我的债权总数
	 * @param params
	 * @return
	 */
	Integer countMyProjectList(Map<String, Object> params);

	/**
	 * 获取我的转让记录
	 * @param params
	 * @return
	 */
	List<BorrowCredit> selectMyCreditRecord(Map<String, Object> params);
	
	/**
	 * 根据用户ID获取用户测评结果对象
	 * @param userId 用户ID
	 * @return UserEvalationResult
	 */
	UserEvalationResultCustomize getUserEvalationResult(Integer userId);

	/**
	 * 根据borrowNid查询对象
	 * @param borrowNid
	 * @return
	 */
	BorrowRepay findByBorrowNid(String borrowNid);

	/**
	 * 根据borrowNid查询List
	 * @param borrowNid
	 * @return
	 */
	List<BorrowRepayPlan> findRepayPlanByBorrowNid(String borrowNid);

	/**
	 * 根据borrowNid查询用户对象
	 * @param borrowNid
	 * @return
	 */
	BorrowUsers getBorrowUsersByNid(String borrowNid);

	/**
	 * 根据borrowNid获取借款人信息
	 * @param borrowNid
	 * @return
	 */
	BorrowManinfo getBorrowManinfoByNid(String borrowNid);

	/**
	 * 根据borrowNid查询所发标的出借金额
	 * @param params
	 * @return
	 */
	String countMoneyByBorrowId(Map<String, Object> params);

	/**
     * 获取房产抵押信息
     * @param borrowNid
     * @return
     */
    List<BorrowHouses> getBorrowHousesByNid(String borrowNid);
    /**
     * 获取车辆抵押信息
     * @param borrowNid
     * @return
     */
    List<BorrowCarinfo> getBorrowCarinfoByNid(String borrowNid);

    /**
     * 根据用户ID和borrowNid获取对象
     * @param borrowNid
     * @param userId 用户ID
     * @return
     */
    @Deprecated
	BorrowRecover getByborrowNid(String borrowNid, Integer userId);

	/**
     * 根据用户ID和borrowNid获取对象
     * @param borrowNid
     * @param userId 用户ID
     * @return
     */
	BorrowTender getBorrowTender(Integer userId, String borrowNid);

	/**
     * 根据用户ID和borrowNid和订单ID获取对象
     * @param borrowNid
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return
     */
	AppCouponInfoCustomize getCouponfigByUserIdAndBorrowNid(Integer userId, String orderId);

	/**
	 * 根据用户ID和borrowNid获取对象
	 * @param userId 用户ID
	 * @param borrowNid
	 * @return
	 */
	List<BorrowRecoverPlan> getByUserIdAndBorrowNid(Integer userId,
			String borrowNid);

	/**
	 * 根据订单ID和用户ID获取债转信息
	 * @param orderId 订单ID
	 * @param userId 用户ID
	 * @return
	 */
	BorrowCredit getByOrderId(String orderId, Integer userId);

	/**
	 * 获取出借信息
	 * @param orderId
	 * @return
	 */
    BorrowTender selectBorrowTender(String orderId);

	/**
	 * 出借人获取还款信息
	 * @param orderId
	 * @return
	 */
	BorrowRecover selectBorrowRecoverByNid(String orderId);
	/**
	 * 出借人获取分期还款信息
	 * @param orderId
	 * @return
	 */
	List<BorrowRecoverPlan> selectBorrowRecoverPlanByNid(String orderId);

    /**
     * 通过nid查找borrowTender
     * @param tenderNid
     * @return
     */
    BorrowTender selectBorrowTenderByNid(String tenderNid);

	/**
	 * 通过assignNid查询承接人出借
	 * @param assignNid
	 * @return
	 */
	CreditTender selectCreditTender(String assignNid);

	/**
	 * 通过assignNid查询承接人还款
	 * @param assignNid
	 * @return
	 */
	List<CreditRepay> selectCreditRepayList(String assignNid);

	/**
	 * 判断用户是否出借该标
	 * @param userId 用户ID
	 * @param borrowNid 标的ID
	 * @param borrowType 标的类型（如不为空。则为债转出借，1是债转 ，0 普通）
	 * @return boolean
	 */
	boolean isTenderBorrow(Integer userId, String borrowNid, String borrowType);

	/**
	 * 根据订单ID和用户ID获取债转信息
	 * @param orderId 用户ID
	 * @param userId 债转信息
	 * @return
	 */
	List<BorrowCredit> getBorrowList(String orderId, Integer userId);

	/**
	 * 根据borrowNid返回放款信息
	 * @param borrowNid
	 * @return
	 */
    AccountBorrow selectAccountBorrow(String borrowNid);

	BigDecimal getCreditTender(String creditNid);
	/**
	 * 根据borrowNid返回上市列表
	 * @param borrowNid
	 * @return
	 */
    List<ActdecListedOne> getActdecList(String borrowNid);

	BorrowRepay getBorrowRepay(String borrowNid);
	/**
	 * 获取用户出借
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	int countUserInvest(Integer userId, String borrowNid);

	/**
	 * 根据borrowNid，userId查找承接记录
	 */
	int countCreditTender(Map<String, Object> params);
	
	/**
	 * 根据参数查找债转记录
	 */
    List<HjhDebtCredit> selectHjhDebtCreditList(Map<String,Object> mapParam);
}
