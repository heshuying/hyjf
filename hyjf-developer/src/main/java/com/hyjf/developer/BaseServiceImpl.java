package com.hyjf.developer;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BorrowConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.ParamNameExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

public class BaseServiceImpl extends CustomizeMapper implements BaseService {

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
}
