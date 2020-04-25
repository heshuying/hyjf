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

package com.hyjf.web.user.invest;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface InvestService {

	public Integer getUserIdByUsrcustId(String usrCustId);

	/**
	 * 根据项目类型获取项目类别对象
	 * 
	 * @param projectType
	 * @return
	 */
	public BorrowProjectType getBorrowProjectType(String projectType);

	/**
	 * 获取用户对象
	 * 
	 * @param userId
	 * @return
	 */
	public Users getUsers(Integer userId);

	/**
	 * 获取用户信息对象
	 * 
	 * @param userId
	 * @return
	 */
	public UsersInfo getUserInfo(Integer userId);

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
	 * 更新借款
	 * 
	 * @param record
	 * @return
	 */
	public boolean updateBorrow(Borrow record);

	/**
	 * 冻结
	 * 
	 * @param userId
	 * @param account
	 * @param tenderUsrcustid
	 * @param borrowerUsrcustid
	 * @param OrdId
	 * @param uuid
	 * @return
	 */
	public FreezeDefine freeze(Integer userId, String account, String tenderUsrcustid, String borrowerUsrcustid,
			String OrdId);

	/**
	 * 根据借款id获取借款信息
	 * 
	 * @param borrowId
	 * @return
	 */
	public Borrow getBorrowByNid(String borrowNid);

	/**
	 * 获取用户的汇付信息
	 * 
	 * @param userId
	 * @return 用户的汇付信息
	 */
	public AccountChinapnr getAccountChinapnr(Integer userId);

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
	 */
	public Boolean updateBeforeChinaPnR(String borrowNid, String OrdId, Integer userId, String account, String ip);

	/**
	 * 调用汇付天下接口成功后操作
	 * 
	 * @param borrowId
	 *            借款id
	 * @param userId
	 *            用户id
	 * @param account
	 *            出借金额
	 * @param ip
	 *            出借人ip
	 * @param client
	 *            客户端0PC，1微信2安卓APP，3IOS APP，4其他
	 * @return 操作是否成功
	 */
	public JSONObject userTender(Borrow borrow, ChinapnrBean bean);

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
	public JSONObject checkParam(String borrowNid, String account, Integer userId, String platform,
			String couponGrantId);

	/**
	 * 解冻用户出借
	 * 
	 * @param borrowUserId
	 * @param investUserId
	 * @param orderId
	 * @param trxId
	 * @param ordDate
	 * @return
	 * @throws Exception
	 */
	boolean unFreezeOrder(int borrowUserId, int investUserId, String orderId, String trxId, String ordDate)
			throws Exception;

	/**
	 * 根据优惠券编号 取得用户优惠券
	 * 
	 * @param couponGrantId
	 * @param userId
	 * @return
	 */
	CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId);

	/**
	 * 优惠券出借
	 * 
	 * @param borrowId
	 *            借款id
	 * @param userId
	 *            用户id
	 * @param account
	 *            出借金额
	 * @param ip
	 *            出借人ip
	 * @param client
	 *            客户端0PC，1微信2安卓APP，3IOS APP，4其他
	 * @return 操作是否成功
	 */
	Boolean updateCouponTender(String couponGrantId, String borrowNid, String ordDate, Integer userId, String account,
			String ip, int orderTime, String mainTenderNid, Map<String, Object> retMap);

	/**
	 * 
	 * @method: updateUserInvestFlagById
	 * @description: 更改新手出借状态位 1
	 * @param userId
	 * @return
	 * @mender: zhouxiaoshuai
	 * @date: 2016年5月30日 下午5:10:34
	 */
	public boolean updateUserInvestFlagById(Integer userId);

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
	 * 校验优惠券出借
	 * 
	 * @param projectTypeList
	 * @param projectTypeCd
	 * @return
	 */
	public String validateCouponProjectType(String projectTypeList, String projectTypeCd);

	/**
	 * 
	 * 根据用户id查询tender表数量
	 * 
	 * @author pcc
	 * @param userId
	 * @param borrowProjectType
	 * @return
	 */
	public int countBorrowTenderNum(Integer userId, String nid);

	/**
	 * 
	 * @method: checkParamAppointment
	 * @description: 预约投标校验
	 * @param borrowNid
	 * @param accountStr
	 * @param userId
	 * @param string
	 * @param appointCheck
	 * @return
	 * @return: JSONObject
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月27日 上午11:54:14
	 */
	public JSONObject checkParamAppointment(String borrowNid, String accountStr, Integer userId, String string,
			String appointCheck);

	/**
	 * 
	 * @method: updateAfterAppointRedis
	 * @description: 预约更新相关信息
	 * @param borrowId
	 * @param borrowNid
	 * @param projectType
	 * @param borrowStyle
	 * @param feeRate
	 * @param borrowPeriod
	 * @param borrowAccountWait
	 * @param orderId
	 * @param userId
	 * @param account
	 * @param Usrcustid
	 * @param ip
	 * @param client
	 * @return
	 * @throws Exception
	 * @return: Boolean
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月27日 下午1:45:36
	 */
	Boolean updateAfterAppointRedis(int borrowId, String borrowNid, int projectType, String borrowStyle,
			BigDecimal feeRate, int borrowPeriod, BigDecimal borrowAccountWait, String orderId, Integer userId,
			String account, String Usrcustid, BigDecimal borrowAccount, Integer client, String ip, String freezeTrxId)
			throws Exception;

	public int countBorrowTenderNum(Integer userId, String borrowNid, String nid);

	/**
	 * 
	 * @param couponGrantId
	 * @param borrow
	 * @param userId
	 * @param account
	 * @param couponOldTime
	 * @return
	 */
	public Map<String, Object> queryCouponData(String couponGrantId, Borrow borrow, Integer userId, String account,
			int couponOldTime);

	/**
	 * @param ordId
	 * @param couponGrantId
	 * @param couponGrantId
	 * @param userId
	 * @return
	 */

	public boolean updateCouponTenderStatus(String orderId, String couponGrantId, Integer userId);

	/**
	 * 回滚redis
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param account
	 */
	public void recoverRedis(String borrowNid, Integer userId, String account);

	/**
	 * 
	 * 更新用户出借临时表
	 * @param userId
	 * @param ordId
	 * @param ordId2
	 * @return
	 */
	/**
	 * 
	 * @method: checkIfSendCoupon
	 * @description: 	判断是否可以发送出借1000加息券		
	 *  @param user
	 * @param account 
	 *  @return 
	 * @return: boolean
	* @mender: zhouxiaoshuai
	 * @date:   2016年8月25日 下午3:46:42
	 */
	public boolean checkIfSendCoupon(Users user, String account);

	public int updateBorrowTenderTmp(Integer userId, String borrowNid, String ordId);

	public void extraUeldInvest(Borrow borrow, ChinapnrBean bean);
	
	public BorrowProjectType getProjectTypeByBorrowNid(String borrowNid);


}
