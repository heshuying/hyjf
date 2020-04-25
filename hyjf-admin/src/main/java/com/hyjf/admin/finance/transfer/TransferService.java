package com.hyjf.admin.finance.transfer;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.Users;

public interface TransferService extends BaseService {

	/**
	 * 根据开户参数，获取开户信息
	 * 
	 * @param TransferListBean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */

	public List<UserTransfer> getRecordList(TransferListBean form, int limitStart,int limitEnd);

	/**
	 * 统计开户记录总数
	 * 
	 * @param TransferListBean
	 * @return
	 */

	public int countRecordTotal(TransferListBean form);

	/**
	 * 根据开户参数，获取开户信息
	 * 
	 * @param TransferListBean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */

	public List<UserTransfer> exportRecordList(TransferListBean form);

	
	/**
	 * 根据用户名校验用户条件，并获取用户的余额 1. 校验用户是否已经存在 2. 校验用户是否已经开户 3. 查询用户的余额
	 * 
	 * @param outUserName
	 * @param ret
	 */
	public void checkTransfer(String outUserName, JSONObject ret);

	/**
	 * 根据用户名校验用户条件，并获取用户的余额 1. 校验用户是否已经存在 2. 校验用户是否已经开户 3. 查询用户的余额
	 * 
	 * @param outUserName
	 * @param ret
	 */
	public void searchBalance(String outUserName, JSONObject ret);

	/**
	 * 保存转账链接
	 * 
	 * @param form
	 * @return
	 */
	public boolean insertTransfer(TransferCustomizeBean form);

	/**
	 * 校验转账参数
	 * 
	 * @param modelAndView
	 * @param form
	 */
	public void checkTransferParam(ModelAndView modelAndView, TransferCustomizeBean form);

	/**
	 * 根据id获取转账信息
	 * 
	 * @param parseInt
	 * @return
	 */
	public UserTransfer searchUserTransferById(int parseInt);

	/**
	 * 根据userid获取用户信息
	 * 
	 * @param outUserId
	 * @return
	 */
	public Users searchUserByUserId(Integer outUserId);

}
