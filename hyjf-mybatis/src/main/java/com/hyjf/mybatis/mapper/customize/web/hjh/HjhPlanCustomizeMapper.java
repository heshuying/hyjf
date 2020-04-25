/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 上午11:01:57
 *           Modification History:
 *           Modified by :
 * */

package com.hyjf.mybatis.mapper.customize.web.hjh;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.customize.app.AppMyHjhDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanRepayCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistListCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;

public interface HjhPlanCustomizeMapper {

	/**
	 * 查询相应的计划列表信息
	 * 
	 * @param params
	 * @return
	 */
	List<HjhPlanCustomize> selectHjhPlanList(Map<String, Object> params);

	/**
	 * 
	 * 记录数查询
	 * @author hsy
	 * @param params
	 * @return
	 */
	Integer countHjhPlanList(Map<String, Object> params);
	/**
	 * 
	 * 汇计划数据统计查询
	 * @author hsy
	 * @return
	 */
	List<Map<String, Object>> searchPlanStatisticData();
	
	/**
	 * 查询相应的项目详情
	 * 
	 * @param planNid
	 * @return
	 */
	DebtPlanDetailCustomize selectDebtPlanDetail(@Param("planNid") String planNid);
	
	/**
	 * 查询相应的计划加入明细记录
	 * 
	 * @param params
	 * @return
	 */
	int countPlanAccedeRecordTotal(Map<String, Object> params);
	
	/**
	 * 查询相应的计划加入总额
	 * 
	 * @param params
	 * @return
	 */
	Long selectPlanAccedeSum(Map<String, Object> params);
	
	/**
	 * 查询相应的计划的加入明细
	 * 
	 * @param params
	 * @return
	 */
	List<DebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params);
	
	/**
	 * 
	 * 用户汇计划出借信息详情
	 * @author pcc
	 * @param params
	 * @return
	 */
    UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params);
    /**
     * 
     * 查询用户汇计划出借关联项目
     * @author pcc
     * @param params
     * @return
     */
    List<UserHjhInvistListCustomize> selectUserHjhInvistBorrowList(Map<String, Object> params);
    
    /**
     * 更新汇计划金额
     * @param hjhPlan
     * @return
     */
    int updatePlanAccount(HjhPlan hjhPlan);
    
    /**
     * 更新还款后复投的开放额度
     * @param hjhPlan
     * @return
     */
    int updateRepayPlanAccount(HjhPlan hjhPlan);
    
    /**
     * 更新汇计划出借明细金额
     * @param hjhAccede
     * @return
     */
    int updateInvestAccount(HjhAccede hjhAccede);
    
	/**
	 * 查询相应的计划标的总数
	 * 
	 * @param params
	 * @return
	 */
	int countPlanBorrowRecordTotal(Map<String, Object> params);
	
	/**
	 * @param params
	 * @return
	 */
	List<DebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params);
	
	/**
	 * 
	 * @method: selectUserAppointmentInfo
	 * @description: 			
	 * @param userId
	 * @return
	 * @return: Map<String,Object>
	 * @date:   2016年7月27日 上午11:36:35
	 */
	Map<String, Object> selectUserAppointmentInfo(String userId);
	
	/**
	 * 更新相应的汇计划专属标的加入的用户的账户信息
	 * 
	 * @param investAccount
	 * @return
	 */
	int updateOfPlanJoin(Account investAccount);
	
	/**
	 * 加入计划后更新计划总信息
	 * 
	 * @param plan
	 * @return
	 */
	int updateByDebtPlanId(Map<String, Object> plan);
	/**
	 * 
	 * 查询加入计划明细自动出借项目列表数量
	 * @author pcc
	 * @param params
	 * @return
	 */
    int countUserHjhInvistBorrowListTotal(Map<String, Object> params);
    
    /**
     * 导出汇计划还款列表
     * @param params
     * @return
     */
    List<HjhPlanRepayCustomize> exportPlanRepayList(Map<String, Object> params);

	/**
	 * 首页汇计划展示，排序规则出借中，锁定期限，和selectHjhPlanList仅仅是排序不同
	 * @param params
	 * @return
	 */
	List<HjhPlanCustomize> selectIndexHjhPlanList(Map<String, Object> params);

	/**
	 * 查询app相应的计划列表信息
	 *
	 * @param params
	 * @return
	 */
	List<HjhPlanCustomize> selectAppHjhPlanList(Map<String, Object> params);
	
    /**
     * 更新汇计划出借明细金额
     * @param hjhAccede
     * @return
     */
    int updateHjhAccedeForHjhProcess(HjhAccede hjhAccede);

	/**
	 * 查询app我的计划详情
	 * @param params
	 * @return
	 */
	AppMyHjhDetailCustomize selectAppMyHjhDetail(Map<String, Object> params);
	
	/**
	 * 根据 accedeOrderId 获取出借金额
	 */
	BigDecimal getAccdeAcount(String accedeOrderId);

	/**
	 * 更新汇计划还款信息
	 * @param repayParam
	 * @return
	 */
	int updateHjhRepayForHjhRepay(HjhRepay repayParam);
	/**
	 * 
	 * 首页汇计划推广计划列表 - 首页显示
	 * @author pcc
	 * @param params
	 * @return
	 */
    List<HjhPlanCustomize> selectIndexHjhExtensionPlanList(Map<String, Object> params);
    /**
     *
     * 首页汇计划推广计划列表 - 首页显示无可投
     * @author pcc
     * @param params
     * @return
     */
	List<HjhPlanCustomize> selectIndexHjhExtensionPlanListByLockTime(Map<String, Object> params);

	/**
	 * 智投服务更新用户账户
	 * @param account
	 * @return
	 */
	int updateOfHjhPlanJoin(Account account);
}
