package com.hyjf.server.module.wkcd.user.regist;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersLog;
import com.hyjf.server.BaseServiceImpl;

@Service
public class WkcdRegistServiceImpl extends BaseServiceImpl implements WkcdRegistService {

	/**
	 * 根据手机号获取用户信息
	 * 
	 * @param mobile
	 * @return
	 */
	public boolean existUser(String mobile) {
		UsersExample example1 = new UsersExample();
		example1.createCriteria().andMobileEqualTo(mobile);
		int size = usersMapper.countByExample(example1);
		if (size > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 根据手机号获取用户信息
	 * @param mobile
	 * @return
	 */
	public Users getUserByMobile(String mobile){
		UsersExample example1 = new UsersExample();
		example1.setOrderByClause(" user_id Desc ");
		example1.createCriteria().andMobileEqualTo(mobile);
		List<Users> users = usersMapper.selectByExample(example1);
		if(users!=null && users.size()>0){
			return users.get(0);
		}
		return null;
	}
	
	/**
	 * 注册用户
	 * 
	 * @param mobile
	 * @param password
	 * @return
	 */
	public int registUser(String mobile, String password, Users returnUser) {
		try {
			// 写入用户信息表
			Users user = new Users();
			String userName = getUniqueUsername(mobile);
			user.setUsername(userName);
			user.setMobile(mobile);
			user.setRechargeSms(0);
			user.setWithdrawSms(0);
			user.setInvestflag(0);
			user.setInvestSms(0);
			user.setRecieveSms(0);
			user.setVersion(new BigDecimal("0"));
			user.setUserType(0); // 用户类型 0普通用户 1企业用户
			user.setRegEsb(5);// 账户开通平台 0pc 1微信 2安卓 3IOS 4其他 5微可
			int time = GetDate.getNowTime10();
			String codeSalt = GetCode.getRandomCode(6);
			user.setSalt(codeSalt);
			user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt));
			user.setRegIp(returnUser.getLoginIp());
			user.setRegTime(time);
			user.setLoginIp(returnUser.getLoginIp());
			user.setLoginTime(time);
			user.setLastTime(time);
			user.setLogintime(1);// 登录次数
			user.setStatus(0);//用户是否锁定,0未锁定,1锁定
			user.setBorrowSms(0);
			user.setPid(5);//5 微可平台
			user.setUsernamep("");
			user.setPtype(0);
			user.setOpenAccount(0);
			System.out.println("saveRegistUser***********************************预插入user：" + JSON.toJSONString(user));
			usersMapper.insertSelective(user);

			// 写入用户详情表
			int userId = user.getUserId();
			returnUser.setUserId(userId);
			UsersInfo userInfo = new UsersInfo();
			userInfo.setAttribute(0);// 默认为无主单
			userInfo.setUserId(userId);
			userInfo.setRoleId(2);//用户角色1出借人2借款人
			userInfo.setMobileIsapprove(1);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setUpdateTime(time);
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
			userInfo.setBorrowerType(2);
			userInfo.setIntro("");
			userInfo.setInterest("");
			userInfo.setUpdateTime((int) System.currentTimeMillis() / 1000);
			userInfo.setParentId(0);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setPromoteway(0);
			userInfo.setIsContact(false);
			System.out.println(
					"saveRegistUser***********************************预插入userInfo：" + JSON.toJSONString(userInfo));
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
			System.out.println(
					"saveRegistUser***********************************预插入account：" + JSON.toJSONString(account));
			accountMapper.insertSelective(account);

			// 保存用户注册日志
			UsersLog userLog = new UsersLog();
			userLog.setUserId(userId);
			userLog.setEvent("register");
			userLog.setContent("注册成功");
			System.out.println(
					"saveRegistUser***********************************预插入userLog：" + JSON.toJSONString(userLog));
			usersLogMapper.insertSelective(userLog);

		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "registUser", e);
			return 0;
		}
		return 1;
	}
	
	
}


