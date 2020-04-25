package com.hyjf.api.surong.user.regist;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;


public interface RegisterService {

	/** 注册 */
	public Map<String,Object> insertUserAction(String mobile, String password,String loginIp, HttpServletRequest request,Users user);

	/** 根据手机号判断用户是否存在 */
	public Users countUserByMobile(String mobile);

	/** 根据用户id获取开户信息 */
	public List<BankOpenAccount> getAccountInfo(Integer userId);
	
	/** 根据用户id获取快捷卡信息*/
	public List<BankCard> getBankCard(Integer userId);
	
	/** 根据id获取用户信息*/
	public List<UsersInfo> getUser(Integer userId);
	
	/**
     * 根据id修改用户信息
     */
	public void updateUser(Integer userId);


}
