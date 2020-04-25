package com.hyjf.api.surong.user.regist;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.PreRegist;
import com.hyjf.mybatis.model.auto.PreRegistExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UsersLog;

@Service
public class RegisterServiceImpl extends BaseServiceImpl  implements RegisterService {
	/** 注册 */
	@Override
	public Map<String,Object> insertUserAction(String mobile, String password,
			String loginIp, HttpServletRequest request, Users returnUser) {
	    int userId = 0;
	    Map<String,Object> map = new HashMap<String,Object>();
		try {
			// 写入用户信息表
			Users user = new Users();
			String userName = getUniqueUsername(mobile);
			user.setUsername(userName);
			user.setMobile(mobile);
			user.setLoginIp(loginIp);
			user.setRechargeSms(0);
			user.setWithdrawSms(0);
			user.setInvestflag(0);
			user.setInvestSms(0);
			user.setRecieveSms(0);
			user.setVersion(new BigDecimal("0"));
			user.setUserType(0);
			user.setIsSetPassword(0);
			user.setBankOpenAccount(0);
			// 融东风机构编号为10000017
			user.setInstCode("10000017");
			// 写入用户详情表
			UsersInfo userInfo = new UsersInfo();
			// 以下语句用来设置用户有无主单开始 2015年12月30日18:28:34 孙亮
			userInfo.setAttribute(0);// 默认为无主单
			// 1.注册成功时，推荐人关联
			// B1、用户在注册时，没有填写推荐人，也没有预注册，或预注册时没有关联推荐人，则推荐人为空；
			// B2、用户注册时，填写了推荐人，忽略关联推荐人，以填写的推荐人为准；
			// B3、用户注册时，没有填写推荐人，预注册表中，有关联推荐人，以关联的推荐人为准；
			PreRegistExample preRegistExample = new PreRegistExample();
			preRegistExample.createCriteria().andMobileEqualTo(mobile);
			List<PreRegist> preRegistList = preRegistMapper.selectByExample(preRegistExample);
			if (preRegistList != null && preRegistList.size() != 0) {
				for (int i = 0; i < preRegistList.size(); i++) {
					PreRegist model = preRegistList.get(i);
					model.setRegistFlag(1);
					model.setRegistTime(GetDate.getMyTimeInMillis());
					preRegistMapper.updateByPrimaryKey(model);
				}
			}
			if (StringUtils.isNotBlank(request.getParameter("platform"))) {
				user.setRegEsb(Integer.parseInt(request.getParameter("platform")));
			}
			int time = GetDate.getNowTime10();
			String codeSalt = GetCode.getRandomCode(6);
			user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt));
			user.setRegIp(loginIp);
			user.setRegTime(time);
			user.setLoginIp(loginIp);
			user.setLoginTime(time);
			user.setLastIp(loginIp);
			user.setLastTime(time);
			user.setLogintime(1);// 登录次数
			user.setStatus(0);

			user.setSalt(codeSalt);
			user.setBorrowSms(0);
			// user.setAccountEsb(0);
			user.setPid(0);
			user.setUsernamep("");
			user.setPtype(0);
			user.setOpenAccount(0);
			usersMapper.insertSelective(user);
			map.put("user", user);
            
			userId = user.getUserId();
			returnUser.setUserId(userId);
			// userInfo表
			userInfo.setUserId(userId);
			userInfo.setRoleId(2);
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
			userInfo.setAddress("");
			userInfo.setIntro("");
			userInfo.setInterest("");
			userInfo.setUpdateTime((int) System.currentTimeMillis() / 1000);
			userInfo.setParentId(0);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setPromoteway(0);
            userInfo.setBorrowerType(1);
			userInfo.setIsContact(false);
			System.out.println("saveRegistUser***********************************预插入userInfo："
					+ JSON.toJSONString(userInfo));
			usersInfoMapper.insertSelective(userInfo);
			// 写入用户账户表
			{
				Account account = new Account();
				account.setUserId(userId);
				account.setTotal(BigDecimal.ZERO);
				account.setIncome(BigDecimal.ZERO);
				account.setExpend(BigDecimal.ZERO);
				account.setBalance(BigDecimal.ZERO);
				account.setBalanceCash(BigDecimal.ZERO);
				account.setBalanceFrost(BigDecimal.ZERO);
				account.setFrost(BigDecimal.ZERO);
				account.setAwait(BigDecimal.ZERO);
				account.setRepay(BigDecimal.ZERO);
				account.setFrostCash(BigDecimal.ZERO);
				account.setRecMoney(BigDecimal.ZERO);
				account.setFee(BigDecimal.ZERO);
				account.setInMoney(BigDecimal.ZERO);
				account.setInMoneyFlag(0);
				// 注册时为account表增加
				// planTotal,planBalance,planFrost,planAccountWait,planCapitalWait,planInterestWait,planRepayInterest默认值
				account.setPlanAccedeTotal(BigDecimal.ZERO);
				account.setPlanBalance(BigDecimal.ZERO);
				account.setPlanFrost(BigDecimal.ZERO);
				account.setPlanAccountWait(BigDecimal.ZERO);
				account.setPlanCapitalWait(BigDecimal.ZERO);
				account.setPlanInterestWait(BigDecimal.ZERO);
				account.setPlanRepayInterest(BigDecimal.ZERO);
				account.setVersion(BigDecimal.ZERO);
				System.out.println("saveRegistUser***********************************预插入account："
						+ JSON.toJSONString(account));
				accountMapper.insertSelective(account);
			}
			{
				UsersLog userLog = new UsersLog();
				userLog.setUserId(userId);
				userLog.setIp(loginIp);
				userLog.setEvent("register");
				userLog.setContent("注册成功");
				System.out.println("saveRegistUser***********************************预插入userLog："
						+ JSON.toJSONString(userLog));
				usersLogMapper.insertSelective(userLog);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LogUtil.errorLog(RegisterServiceImpl.class.getName(), "saveRegistUser", e);
			map.put("userId", 0);
			return map;
		}
		map.put("userId", userId);
		return map;
	}

	/** 获取唯一username */
	private String getUniqueUsername(String mobile) {
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
	 * 根据手机号判断用户是否存在
	 */
	@Override
	public Users countUserByMobile(String mobile) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andMobileEqualTo(mobile);
		List<Users> list = usersMapper.selectByExample(example);
		return (list!=null&&list.size()>0)?list.get(0):null;
	}
	
	/**
     * 根据用户id获取开户信息
     */
    @Override
    public List<BankOpenAccount> getAccountInfo(Integer userId) {
        BankOpenAccountExample example = new BankOpenAccountExample();
        BankOpenAccountExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return bankOpenAccountMapper.selectByExample(example);
    }
    
    /**
     * 根据用户id获取快捷卡信息
     */
    @Override
    public List<BankCard> getBankCard(Integer userId) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return bankCardMapper.selectByExample(example);
    }
    
    /**
     * 根据id获取用户信息
     */
    @Override
    public List<UsersInfo> getUser(Integer userId) {
        UsersInfoExample example = new UsersInfoExample();
        UsersInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return usersInfoMapper.selectByExample(example);
    }
    
    /**
     * 根据id修改用户信息
     */
    @Override
    public void updateUser(Integer userId) {
        UsersInfoExample example = new UsersInfoExample();
        UsersInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        UsersInfo record = new UsersInfo();
        record.setRoleId(2);
        record.setBorrowerType(1);
        usersInfoMapper.updateByExampleSelective(record, example);
    }

}
