package com.hyjf.web;

import java.util.List;

import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

/**
 * <p>
 * BaseService
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public interface BaseService {
	/**
	 * 获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public Users getUsers(Integer userId);

	/**
	 * 发送短信添加数据
	 * 
	 * @param content
	 * @param mobile
	 * @param checkCode
	 * @param remark
	 * @param status
	 * @return
	 */
	public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status);

	/**
	 * 获取用户的汇付信息
	 * 
	 * @param userId
	 * @return 用户的汇付信息
	 */
	public AccountChinapnr getAccountChinapnr(Integer userId);

	/**
	 * 获取用户信息
	 * 
	 * @param username
	 * @param password
	 * @return 获取用户信息
	 */
	public Users getUsers(String username, String password);

	/**
	 * 获取用户的账户信息
	 * 
	 * @param userId
	 * @return 用户的身份证号
	 */
	public Account getAccount(Integer userId);

	/**
	 * 获取系统配置
	 * 
	 * @return
	 */
	public String getBorrowConfig(String configCd);

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId);

	/**
	 * 获取数据字典表的下拉列表
	 *
	 * @return
	 */
	public List<ParamName> getParamNameList(String nameClass);

	/**
	 * 获取唯一username
	 * 
	 * @param mobile
	 * @return
	 */
	public String getUniqueUsername(String mobile);

	/**
	 * 为加强版发送验证码
	 *
	 * @param request
	 * @param form
	 * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvTxCode:
	 *         srvTxCode}
	 */
	public BankCallBean sendSms(Integer userId, String srvTxCode, String mobile, String client);
	
	
	/**
     * 获取用户的银行开户信息
     * 
     * @param userId
     * @return 用户的汇付信息
     */
    public BankOpenAccount getBankOpenAccount(Integer userId);
    /**
     * 获取用户优惠券数量
     * @param userId
     * @param usedFlag  0：未使用，1：已使用，2：审核不通过，3：待审核，4：已失效
     * @return
     */
    public int getUserCouponCount(Integer userId,String usedFlag);

	SpreadsUsers getRecommendUser(Integer userId);
	
    /** 返回用户测评信息 */
    public UserEvalationResultCustomize selectUserEvalationResultByUserId(Integer userId);

	/**
	 * 发送用户日志记录MQ
	 *
	 * @param userOperationLogEntity
	 */
	void sendUserLogMQ(UserOperationLogEntity userOperationLogEntity);

	String getUserRoleId(String userName);

	/**
	 * 通过用户ID 关联用户所在的渠道
	 * @param userId
	 * @return
	 * @Author : huanghui
	 */
	AdminUserDetailCustomize selectUserUtmInfo(Integer userId);
}
