package com.hyjf.web;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankSmsAuthCode;
import com.hyjf.mybatis.model.auto.BankSmsAuthCodeExample;
import com.hyjf.mybatis.model.auto.BorrowConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.ParamNameExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsLogWithBLOBs;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultExampleCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl extends CustomizeMapper implements BaseService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Override
	public Users getUsers(Integer userId) {
		return usersMapper.selectByPrimaryKey(userId);
	}

	/**
	 * 添加短信记录表
	 *
	 * @param content
	 *            短信内容
	 * @param mobile
	 *            手机号码
	 * @param checkCode
	 *            验证码(没有验证码，设为空)
	 * @param remark
	 *            发送备注，如 注册填写“注册”
	 * @param status
	 *            发送状态，第三方返回状态
	 * @return
	 */

	@Override
	public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status) {

		if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(content)) {
			// 带验证码
			if (checkCode != null) {
				SmsCode smsCode = new SmsCode();
				smsCode.setMobile(mobile);
				smsCode.setCheckfor(MD5.toMD5Code(mobile + checkCode));
				smsCode.setCheckcode(checkCode);
				smsCode.setPosttime(GetDate.getNowTime10());
				smsCode.setStatus(status);
				smsCodeMapper.insertSelective(smsCode);
			}

			// 插入短信记录
			SmsLogWithBLOBs smsLog = new SmsLogWithBLOBs();

			smsLog.setMobile(mobile);
			smsLog.setContent(content);
			smsLog.setPosttime(GetDate.getNowTime10());
			smsLog.setStatus(status);
			smsLog.setType(remark);
			smsLogMapper.insertSelective(smsLog);
			return true;
		}
		return false;
	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */

	@Override
	public AccountChinapnr getAccountChinapnr(Integer userId) {
		AccountChinapnr accountChinapnr = null;
		if (userId == null) {
			return null;
		}
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			accountChinapnr = list.get(0);
		}
		return accountChinapnr;
	}

	/**
	 * 获取用户信息
	 *
	 * @param username
	 * @param password
	 * @return 获取用户信息
	 */
	@Override
	public Users getUsers(String username, String password) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);

		List<Users> usersList = this.usersMapper.selectByExample(example);
		if (usersList != null && usersList.size() > 0) {
			return usersList.get(0);
		}
		return null;
	}

	/**
	 * 获取用户的账户信息
	 *
	 * @param userId
	 * @return 获取用户的账户信息
	 */
	@Override
	public Account getAccount(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<Account> listAccount = accountMapper.selectByExample(example);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 获取系统配置
	 *
	 * @return
	 */
	@Override
	public String getBorrowConfig(String configCd) {
		BorrowConfig borrowConfig = this.borrowConfigMapper.selectByPrimaryKey(configCd);
		return borrowConfig.getConfigValue();
	}

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public UsersInfo getUsersInfoByUserId(Integer userId) {
		if (userId != null) {
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(example);
			if (usersInfoList != null && usersInfoList.size() > 0) {
				return usersInfoList.get(0);
			}
		}
		return null;
	}

	/**
	 * 获取数据字典表的下拉列表
	 *
	 * @return
	 */
	@Override
	public List<ParamName> getParamNameList(String nameClass) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		cra.andNameClassEqualTo(nameClass);
		cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		example.setOrderByClause(" sort ASC ");
		return this.paramNameMapper.selectByExample(example);
	}

	/** 获取唯一username */
	@Override
	public String getUniqueUsername(String mobile) {
		String username = "hyjf" + mobile.substring(mobile.length() - 6, mobile.length());
		// 第一规则
		UsersExample ue = new UsersExample();
		UsersExample.Criteria cr = ue.createCriteria();
		cr.andUsernameEqualTo(username);
		int cn1 = usersMapper.countByExample(ue);
		if (cn1 > 0) {
			// 第二规则
			UsersExample ue2 = new UsersExample();
			UsersExample.Criteria cr2 = ue2.createCriteria();
			username = "hyjf" + mobile;
			cr2.andUsernameEqualTo(username);
			int cn2 = usersMapper.countByExample(ue2);
			if (cn2 > 0) {
				// 第三规则
				int i = 0;
				while (true) {
					i++;
					UsersExample ue3 = new UsersExample();
					UsersExample.Criteria cr3 = ue3.createCriteria();
					username = "hyjf" + mobile.substring(mobile.length() - 6, mobile.length()) + i;
					cr3.andUsernameEqualTo(username);
					int cn3 = usersMapper.countByExample(ue3);
					if (cn3 == 0) {
						break;
					}
				}
			}
		}
		return username;
	}

	/**
	 * 为加强版发送验证码
	 *
	 * @param request
	 * @param form
	 * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvAuthCode:
	 *         srvAuthCode}
	 */
	public BankCallBean sendSms(Integer userId, String srvTxCode, String mobile, String client) {
		// 调用存管接口发送验证码
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_SMSCODE_APPLY);// 交易代码cardBind
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getOrderDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(client);// 交易渠道000001手机APP 000002网页
		bean.setSrvTxCode(srvTxCode);
		bean.setMobile(mobile);// 交易渠道
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
		bean.setLogUserId(String.valueOf(userId));// 请求用户名
		try {
			BankCallBean mobileBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNull(mobileBean)) {
				return null;
			}
			// 短信发送返回结果码
			String retCode = mobileBean.getRetCode();
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
				return null;
			}
			if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
				return null;
			}
			// 业务授权码
			String srvAuthCode = mobileBean.getSrvAuthCode();
			if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
				boolean smsFlag = this.updateBankSmsLog(userId, srvTxCode, srvAuthCode);
				if (smsFlag) {
					return mobileBean;
				} else {
					return null;
				}
			} else {
				// 保存用户开户日志
				srvAuthCode = this.selectBankSmsLog(userId, srvTxCode, srvAuthCode);
				if (Validator.isNull(srvAuthCode)) {
					return null;
				} else {
					mobileBean.setSrvAuthCode(srvAuthCode);
					return mobileBean;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BankOpenAccount getBankOpenAccount(Integer userId) {
		BankOpenAccount bankOpenAccount = null;
		if (userId == null) {
			return null;
		}
		BankOpenAccountExample example = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<BankOpenAccount> list = bankOpenAccountMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			bankOpenAccount = list.get(0);
		}
		return bankOpenAccount;
	}

	/**
	 * 根据用户id查询推荐人
	 */
	@Override
	public SpreadsUsers getRecommendUser(Integer userId){
		SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
		SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
		spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
		List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);

		if(sList !=null && !sList.isEmpty()){
			return sList.get(0);
		}

		return null;
	}

	/**
	 * 获取用户优惠券数量
	 * @param userId
	 * @param usedFlag  0：未使用，1：已使用，2：审核不通过，3：待审核，4：已失效
	 * @return
	 */
	@Override
	public int getUserCouponCount(Integer userId, String usedFlag) {
		Map<String ,Object> params=new HashMap<String ,Object>();
		params.put("userId", userId);
		params.put("usedFlag", usedFlag);
		int total=couponUserListCustomizeMapper.countCouponUserList(params);
		return total;
	}

	/**
	 * 保存用户的相应的授权代码
	 *
	 * @param userId
	 * @param srvTxCode
	 * @param srvAuthCode
	 * @return
	 */
	private boolean updateBankSmsLog(Integer userId, String srvTxCode, String srvAuthCode) {

		Date nowDate = new Date();
		Users user = this.getUsers(userId);
		BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
		example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
		List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
		if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
			BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
			smsAuthCode.setSrvAuthCode(srvAuthCode);
			smsAuthCode.setUpdateTime(nowDate);
			smsAuthCode.setUpdateUserId(userId);
			smsAuthCode.setUpdateUserName(user.getUsername());
			boolean smsAuthCodeUpdateFlag = this.bankSmsAuthCodeMapper.updateByPrimaryKeySelective(smsAuthCode) > 0 ? true : false;
			if (smsAuthCodeUpdateFlag) {
				return true;
			} else {
				return false;
			}
		} else {
			this.bankSmsAuthCodeMapper.deleteByExample(example);
			BankSmsAuthCode smsAuthCode = new BankSmsAuthCode();
			smsAuthCode.setUserId(userId);
			smsAuthCode.setSrvTxCode(srvTxCode);
			smsAuthCode.setSrvAuthCode(srvAuthCode);
			smsAuthCode.setStatus(1);
			smsAuthCode.setCreateTime(nowDate);
			smsAuthCode.setCreateUserId(userId);
			smsAuthCode.setCreateUserName(user.getUsername());
			boolean smsAuthCodeInsertFlag = this.bankSmsAuthCodeMapper.insertSelective(smsAuthCode) > 0 ? true : false;
			if (smsAuthCodeInsertFlag) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 查询用户的授权码
	 *
	 * @param userId
	 * @param srvTxCode
	 * @param srvAuthCode
	 * @return
	 */
	private String selectBankSmsLog(Integer userId, String srvTxCode, String srvAuthCode) {
		BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
		example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
		List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
		if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
			BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
			return smsAuthCode.getSrvAuthCode();
		}
		return null;
	}

	/**
	 * 返回用户测评信息
	 *
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userId
	 * @return
	 * @author PC-LIUSHOUYI
	 */
    @Override
    public UserEvalationResultCustomize selectUserEvalationResultByUserId(Integer userId) {
        UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserEvalationResultCustomize> userEvalationResultCustomize = userEvalationResultCustomizeMapper.selectByExample(example);
        if (userEvalationResultCustomize != null && userEvalationResultCustomize.size() > 0) {
            return userEvalationResultCustomize.get(0);
        } else {
            return null;
        }
    }

	/**
	 * 发送用户日志记录MQ
	 *
	 * @param userOperationLogEntity
	 */
	@Override
	public void sendUserLogMQ(UserOperationLogEntity userOperationLogEntity){
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.USER_LOG_SAVE, JSONObject.toJSONString(userOperationLogEntity));

	}
	@Override
	public String getUserRoleId(String userName){
		String roleId = "";
		UsersExample example1 = new UsersExample();
		UsersExample example2 = new UsersExample();
		example1.createCriteria().andUsernameEqualTo(userName);
		UsersExample.Criteria c2 = example2.createCriteria().andMobileEqualTo(userName);
		example1.or(c2);
		List<Users> users = usersMapper.selectByExample(example1);
		if(!CollectionUtils.isEmpty(users)){
			UsersInfoExample infoExample = new UsersInfoExample();
			UsersInfoExample.Criteria criteria = infoExample.createCriteria();
			criteria.andUserIdEqualTo(users.get(0).getUserId());
			List<UsersInfo> usersInfos = usersInfoMapper.selectByExample(infoExample);
			roleId = String.valueOf(usersInfos.get(0).getRoleId());
		}
		return roleId;
	}

	/**
	 * 通过用户ID 关联用户所在的渠道
	 * @param userId
	 * @return
	 * @Author : huanghui
	 */
	@Override
	public AdminUserDetailCustomize selectUserUtmInfo(Integer userId) {
		return adminUsersCustomizeMapper.selectUserUtmInfo(userId);
	}
}
