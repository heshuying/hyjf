/**
 * Description:汇计划service接口
 * Copyright: Copyright (HYJF Corporation) 2017
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 */
package com.hyjf.wechat.controller.hjh;

import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.wechat.base.BaseService;

public interface WxHjhPlanService extends BaseService {
	
	/**
	 * 根据计划nid查询相应的计划详情
	 * 
	 * @param planNid
	 * @return
	 */
	DebtPlanDetailCustomize selectDebtPlanDetail(String planNid);
	
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
	 * 根据userid获取登录用户信息
	 * @param userId
	 * @return
	 */
	Users searchLoginUser(Integer userId);
	
	/**
	 * 查询用户的加入记录
	 * 
	 * @param planNid
	 * @param userId
	 * @return
	 */
	int countUserAccede(String planNid, Integer userId);
	
    /**
     * 
     * 根据用户id查询用户签约授权信息
     * @author pcc
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserId(Integer userId);

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
	 * 获取用户优惠券信息
	 * @param couponGrantId
	 * @param userId
	 * @return
	 */
	CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId);

	/**
	 * 用户出借校验
	 * @param borrowNid
	 * @param account
	 * @param valueOf
	 * @param clientWechat
	 * @param cuc
	 * @return
	 */
	JSONObject checkHJHParam(String borrowNid, String account, String valueOf, String clientWechat,
			CouponConfigCustomizeV2 cuc);
	
	/**
	 * 出借
	 * @param modelAndView
	 * @param planNid
	 * @param account
	 * @param userId
	 * @param platform
	 * @param ip
	 * @param couponGrantId
	 * @return
	 */
	JSONObject updateInvestInfo(ModelAndView modelAndView,String planNid, String account, String userId, String platform, String ip,String couponGrantId);

	/**
	 * 发送神策数据统计MQ
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
