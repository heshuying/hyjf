package com.hyjf.web.hjhdetail;

import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.web.BaseService;
/**
 * Description:项目详情service接口
 * Copyright: Copyright (HYJF Corporation) 2017
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 */
public interface HjhDetailService extends BaseService {
	
	/**
	 * 根据计划nid查询相应的计划详情
	 * 
	 * @param planNid
	 * @return
	 */
	DebtPlanDetailCustomize selectDebtPlanDetail(String planNid);
	
	/**
	 * 查询用户的加入记录
	 * 
	 * @param planNid
	 * @param userId
	 * @return
	 */
	int countUserAccede(String planNid, Integer userId);
	
	/**
	 * 查询用户的加入记录
	 * 
	 * @param planNid
	 * @param userId
	 * @return
	 */
	HjhPlan getPlanByNid(String planNid);
	
	/**
	 * 统计相应的计划加入记录总数
	 * 
	 * @param params
	 * @return
	 */
	int countPlanAccedeRecordTotal(Map<String, Object> params);
	
	/**
	 * 统计相应的计划总数
	 * 
	 * @param params
	 * @return
	 */
	Long selectPlanAccedeSum(Map<String, Object> params);
	
	/**
	 * 查询相应的计划的加入列表
	 * 
	 * @param params
	 * @return
	 */
	List<DebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params);
	
	/**
	 * 查询相应的计划标的记录总数
	 * 
	 * @param params
	 * @return
	 */
	int countPlanBorrowRecordTotal(Map<String, Object> params);
	
	/**
	 * 查询相应的计划标的记录列表
	 * 
	 * @param params
	 * @return
	 */
	List<DebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params);
	
	/**
	 * 授权校验
	 * 
	 * @param params
	 * @return
	 */
	JSONObject checkParamPlan(String planNid, String money, Integer userId, String couponGrantId, String redisKey, String threshold);
	
	/**
	 * 更新各种表
	 * 
	 * @param params
	 * @return
	 */
    boolean updateAfterPlanRedis(String planNid, String frzzeOrderId, Integer userId, String accountStr,
            String tenderUsrcustid, int cilent, String ipAddr, String freezeTrxId, String frzzeOrderDate, String planOrderId, String couponGrantId, ModelAndView modelAndView, String couponInterest)
            throws Exception;
    
	/**
	 * 更新各种表
	 * 
	 * @param params
	 * @return
	 */
    void recoverRedis(String planNid, Integer userId, String account, String period, String redisKey);
    
    /**
     * 根据参数查找债转记录
     */
    List<HjhDebtCredit> selectHjhDebtCreditList(Map<String,Object> mapParam);

	/**
	 * 发送神策数据统计MQ
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
