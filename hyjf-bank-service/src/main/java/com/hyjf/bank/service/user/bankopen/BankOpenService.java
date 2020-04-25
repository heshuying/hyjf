/**
 * Description:用户银行开户
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.bank.service.user.bankopen;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface BankOpenService extends BaseService {

	/**
	 * 保存相应的数据，更新相应的字段
	 * 
	 * @param bean
	 * @param params
	 * @return
	 */
	public boolean updateUserAccount(BankCallBean bean);

	/**
	 * 根据用户id获取用户的手机号
	 * 
	 * @param userId
	 * @return
	 */
	public String getUsersMobile(Integer userId);

	/**
	 * 根据手机号统计用户的数量
	 * 
	 * @param userId
	 * 
	 * @param mobile
	 * @return
	 */
	public JSONObject selectUserByMobile(int userId, String mobile);

	/**
	 * 
	 * @method: checkIfSendCoupon
	 * @description: 查看是否可用注册68代金券
	 * @param user
	 * @return
	 * @return: boolean
	 * @mender: zhouxiaoshuai
	 * @date: 2016年8月24日 下午2:06:17
	 */
	public boolean checkIfSendCoupon(Users user);
	
	/**
	 * 保存用户的初始开户记录
	 * @param userId
	 * @param userName
	 * @param orderId
	 * @param clientPc
	 * @param clientPc2 
	 * @return
	 */
	public boolean updateUserAccountLog(int userId, String userName, String mobile, String logOrderId, String clientPc,String name,String idno,String cardNo);

	/**
	 * 保存用户的开户验证码
	 * @param userId
	 * @param logOrderId
	 * @param srvAuthCode
	 * @return
	 */
	public boolean updateUserAccountLog(int userId, String logOrderId, String srvAuthCode);

	/**
	 * 保存用户的开户参数
	 * @param openAccountLog 
	 * @param openAccoutBean
	 * @return
	 */
	public boolean updateUserAccountLog(BankOpenAccountLog openAccountLog, BankCallBean openAccoutBean);

	/**
	 * 查询用户的开户日志
	 * @param userId
	 * @param logOrderId
	 * @return
	 */
	public BankOpenAccountLog selectUserAccountLog(Integer userId, String logOrderId);

	/**
	 * 
	 * @param userId
	 * @param logOrderId
	 * @param status
	 * @return
	 */
	public boolean updateUserAccountLog(Integer userId, String logOrderId, int status);

	public boolean existMobile(String param);
	/**
	 * 校验身份证的唯一性(本地是否开户)
	 * @param idNo
	 * @return
	 */
	public boolean checkIdNo(String idNo);

	/**
	 * 保存用户的初始开户记录 add by jijun 2018/04/04
	 * @param userId
	 * @param userName
	 * @param orderId
	 * @param clientPc
	 * @param clientPc2 
	 * @return
	 */
	public boolean updateUserAccountLog(int userId, String userName, String mobile, String logOrderId, String clientPc);

	/**
	 * 开户成功后,发送CAMQ  add by jijun 2018/04/11
	 * @param userId
	 */
	public void sendCAMQ(String userId);

	/**
	 * 根据用户ID获取用户详情信息
	 * @param userId 用户ID
	 * @return
	 */
	public UsersInfo selectByUserId(Integer userId);
	
}
