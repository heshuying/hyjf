/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.user.invest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface AppTenderService extends BaseService {

	/**
	 * 根据项目类型获取项目类别对象
	 * 
	 * @param projectType
	 * @return
	 */
	public BorrowProjectType getBorrowProjectType(String projectType);

	/**
	 * 判断用户是否可以投新用户标
	 * 
	 * @param userId
	 * @param projectType
	 * @return
	 */
	public boolean checkIsNewUserCanInvest(Integer userId);

	/**
	 * 判断用户是否可以投51老用户标
	 * 
	 * @param userId
	 * @param projectType
	 * @return
	 */
	public boolean checkIs51UserCanInvest(Integer userId);

	/**
	 * 调用汇付天下接口前操作
	 * 
	 * @param borrowId
	 *            借款id
	 * @param userId
	 *            用户id
	 * @param account
	 *            出借金额
	 * @param ip
	 *            出借人ip
	 * @return 操作是否成功
	 * @throws Exception 
	 */
	public boolean updateTenderLog(String borrowNid, String OrdId, Integer userId, String account, String ip) throws Exception ;

	/**
	 * 保存用戶的投資数据
	 * @param borrow
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public JSONObject userTender(Borrow borrow, BankCallBean bean) throws Exception;

	/**
	 * 校验出借参数
	 * 
	 * @param borrowNid
	 * @param account
	 * @param userId
	 * @param platform
	 * @param couponGrantId
	 * @return
	 */
	public JSONObject checkParam(String borrowNid, String account, Integer userId, String platform, String couponGrantId);

	/**
	 * 出借失败后,投标申请撤销
	 * 
	 * @param borrowUserId
	 * @param investUserId
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public boolean bidCancel(int investUserId, String productId, String orgOrderId, String txAmount) throws Exception;

	/**
	 * 根据优惠券编号 取得用户优惠券
	 * 
	 * @param couponGrantId
	 * @param userId
	 * @return
	 */
	CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId);

	/**
	 * 
	 * 根据出借类别返回出借类别文案
	 * 
	 * @author pcc
	 * @param borrowProjectType
	 * @return
	 */
	public String getProjectTypeName(BorrowProjectType borrowProjectType);


	/**
	 * 融通宝加息收益
	 * @param borrow
	 * @param bean
	 */
	public void extraUeldInvest(Borrow borrow, BankCallBean bean);

	/**
	 * 操作redis
	 * @param userId 
	 * @param borrowNid
	 * @param account
	 * @return
	 */
	public JSONObject redisTender(int userId, String borrowNid, String account);

	/**
	 * 更新相应的出借临时表
	 * @param userId
	 * @param borrowNid
	 * @param ordId
	 * @return
	 */
	public boolean updateBorrowTenderTmp(int userId, String borrowNid, String ordId);

	/**
	 * 更新相应的出借数据
	 * @param userId
	 * @param borrowNid
	 * @param orderId
	 * @param bean
	 * @return
	 */
	public boolean updateTender(int userId, String borrowNid, String orderId, BankCallBean bean);
	
	/**
	 * 根据优惠券id判断优惠券是否被使用
	 * add by cwyang 2017-5-11
	 * @param couponGrantId
	 * @return
	 */
	public JSONObject getCouponIsUsed(String orderID,String couponGrantId,int userId);
	
	BorrowWithBLOBs selectBorrowById(int borrowId);

	/**
	 * 发送神策数据统计MQ
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
