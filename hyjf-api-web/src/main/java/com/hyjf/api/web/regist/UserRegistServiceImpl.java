package com.hyjf.api.web.regist;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersExample.Criteria;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersLog;
import com.hyjf.mybatis.model.auto.Utm;
import com.hyjf.mybatis.model.auto.UtmExample;
import com.hyjf.mybatis.model.auto.UtmReg;

@Service
public class UserRegistServiceImpl extends BaseServiceImpl implements
		UserRegistService {

	/**
	 * 根据手机号判断用户是否存在
	 */
	@Override
	public int countUserByMobile(String param) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andMobileEqualTo(param);
		int cnt = usersMapper.countByExample(example);
		return cnt;
	}

	/**
	 * 根据邮件判断用户是否存在
	 */
	@Override
	public int countUserByEmail(String param) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andEmailEqualTo(param);
		int cnt = usersMapper.countByExample(example);
		return cnt;
	}

	/**
	 * 保存用户信息
	 */
	@Override
	public String insertRegistUser(UserRegistBean paramBean) throws Exception {
		// 注册时间
		int regtime = GetDate.getNowTime10();
		paramBean.setRegtime(String.valueOf(regtime));

		// 写入用户信息表
		Users user = new Users();
		// 用户名
		user.setUsername(paramBean.getUsernameSelf());
		// 手机号
		user.setMobile(paramBean.getMobile());
		// 电子邮箱
		user.setEmail(paramBean.getEmail());
		user.setLogintime(regtime);
		user.setRegIp(StringUtils.EMPTY);
		user.setRegTime(regtime);
		user.setLoginIp(StringUtils.EMPTY);
		user.setLoginTime(regtime);
		user.setLastIp(StringUtils.EMPTY);
		user.setLastTime(regtime);
		user.setRechargeSms(0);
		user.setWithdrawSms(0);
		user.setInvestSms(0);
		user.setRecieveSms(0);
		user.setVersion(new BigDecimal("0"));

		String codeSalt = GetCode.getRandomCode(6);
		// 随机生成6位的密码
		String password = GetCode.getRandomCode(6);
		// 设置密码
		user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt));
		// 设置用户是否锁定，0未锁定
		user.setStatus(0);
		// 加密验证字符
		user.setSalt(codeSalt);
		// 发标短信
		user.setBorrowSms(0);
		// PC
		user.setRegEsb(0);
		// 第三方平台编号
		user.setPid(Integer.valueOf(paramBean.getFrom()));
		// 第三方平台相关联的用户名
		user.setUsernamep(paramBean.getUsername());
		user.setPtype(0);
		user.setOpenAccount(0);
		// 插入数据
		usersMapper.insertSelective(user);

		int userId = user.getUserId();

		// 写入用户详情表
		UsersInfo userInfo = new UsersInfo();

		userInfo.setUserId(userId);
		userInfo.setRoleId(1);
		userInfo.setMobileIsapprove(1);
		userInfo.setTruenameIsapprove(0);
		userInfo.setEmailIsapprove(0);
		userInfo.setUpdateTime(regtime);
		userInfo.setPromoteway(0);
		userInfo.setIs51(0);
		userInfo.setIsStaff(0);
		userInfo.setDepartmentId(0);
		userInfo.setNickname("");
		userInfo.setBirthday("");
		userInfo.setSex(1);
		userInfo.setIdcard("");
		userInfo.setEducation("");
		userInfo.setProvince("");
		userInfo.setCity("");
		userInfo.setArea("");
		userInfo.setAddress("");
		userInfo.setIntro("");
		userInfo.setInterest("");
		userInfo.setParentId(0);
		userInfo.setTruenameIsapprove(0);
		userInfo.setEmailIsapprove(0);
		userInfo.setPromoteway(0);
		userInfo.setIsContact(false);
		userInfo.setAttribute(0);
		usersInfoMapper.insertSelective(userInfo);
		// 写入用户账户表
		Account account = new Account();
		account.setUserId(userId);
		account.setTotal(BigDecimal.ZERO);
		account.setIncome(new BigDecimal(0));
		account.setExpend(new BigDecimal(0));
		account.setBalance(new BigDecimal(0));
		account.setBalanceCash(new BigDecimal(0));
		account.setBalanceFrost(new BigDecimal(0));
		account.setFrost(new BigDecimal(0));
		account.setAwait(new BigDecimal(0));
		account.setRepay(new BigDecimal(0));
		account.setFrostCash(new BigDecimal(0));
		account.setRecMoney(new BigDecimal(0));
		account.setFee(new BigDecimal(0));
		account.setInMoney(new BigDecimal(0));
		account.setInMoneyFlag(0);
		account.setVersion(new BigDecimal(0));
		
		accountMapper.insertSelective(account);
		int utmId = this.getUtmId(paramBean.getFrom());
		// 保存第三方推广平台
		if (utmId != -1) {
			UtmReg utmReg = new UtmReg();
			utmReg.setUserId(userId);
			utmReg.setCreateTime(GetDate.getNowTime10());
			utmReg.setUtmId(utmId);
			utmReg.setOpenAccount(0);
			utmReg.setBindCard(0);
			utmRegMapper.insertSelective(utmReg);
		}
		// 保存用户注册日志
		UsersLog userLog = new UsersLog();
		userLog.setUserId(userId);
		userLog.setEvent("register");
		userLog.setContent("注册成功");

		usersLogMapper.insertSelective(userLog);

		return password;
	}

	/**
	 * 取得渠道推广编号
	 * 
	 * @param fromCode
	 * @return
	 */
	public int getUtmId(String fromCode) {
		UtmExample exampleUtm = new UtmExample();
		exampleUtm.createCriteria().andSourceIdEqualTo(
				Integer.valueOf(fromCode));
		List<Utm> utmList = utmMapper.selectByExample(exampleUtm);
		if (utmList.size() > 0) {
			Utm utm = utmList.get(0);
			return utm.getUtmId();
		}
		return -1;
	}

	/**
	 * 用户名是否已经存在 true：已存在 false：不存在
	 */
	@Override
	public boolean checkUsernameExists(UserRegistBean paramBean) {
		// 校验第三方平台用户是否已经在本站注册过
		UsersExample userThrExample = new UsersExample();
		Criteria criteria = userThrExample.createCriteria();
		criteria.andUsernamepEqualTo(paramBean.getUsername());
		criteria.andPidEqualTo(Integer.valueOf(paramBean.getFrom()));
		int userCount = usersMapper.countByExample(userThrExample);
		if (userCount > 0) {
			return true;
		}

		// 校验用户名是否已经存在
		UsersExample userExample = new UsersExample();
		userExample.createCriteria()
				.andUsernameEqualTo(paramBean.getUsername());
		userCount = usersMapper.countByExample(userExample);
		if (userCount > 0) {
			// 如果用户名已经存在，则生成新的用户名，生成规则是增加前缀“tll”
			String newUsername = "tll" + paramBean.getUsername();
			UsersExample newUserExample = new UsersExample();
			newUserExample.createCriteria().andUsernameEqualTo(newUsername);
			int newUserCount = usersMapper.countByExample(newUserExample);
			if (newUserCount > 0) {
				// 新用户名已经存在
				return true;
			} else {
				// 新用户名不存在，设置新用户名
				paramBean.setUsernameSelf(newUsername);
			}
		} else {
			// 设置用户名
			paramBean.setUsernameSelf(paramBean.getUsername());
		}
		return false;
	}
}
