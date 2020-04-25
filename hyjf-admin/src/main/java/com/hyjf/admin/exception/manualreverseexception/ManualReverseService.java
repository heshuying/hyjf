package com.hyjf.admin.exception.manualreverseexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.ManualReverseCustomize;

/**
 * 手动冲正Service
 * 
 * @author PC-LIUSHOUYI
 */

public interface ManualReverseService extends BaseService {

	/**
	 * 检索手动冲正数量
	 * 
	 * @param form
	 * @return
	 */
		
	public int countManualReverse(ManualReverseBean form);

	/**
	 * 获取手动冲正数据
	 * 
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
		
	public List<ManualReverseCustomize> selectManualReverseList(ManualReverseBean form, int offset, int limit);
	
	/**
	 * 资金明细表插入数据
	 * 
	 * @param form
	 * @return
	 */
	public boolean insertAccountList(ManualReverseBean form, HttpServletRequest request);
	
	/**
	 * 添加手动冲正数据
	 * 
	 * @param form
	 * @return
	 */
		
	public void insertManualReverse(ManualReverseBean form, boolean result);

	/**
	 * 用户是否存在
	 * 
	 * @param userId
	 * @return
	 */
	public String isExistsUser(HttpServletRequest request);
	
	/**
	 * 用户是否存在(用户账户查询)
	 * @param request
	 * @return
	 */
	public int isExistsUser(String userId);
	
	/**
	 * 通过用户名带出用户开户账户
	 * 
	 * @param userName
	 * @return
	 */
	public String getAccountId(String userName);
	
	/**
	 * 通过账户信息获取用户信息
	 * 
	 * @param accountId
	 * @return
	 */
	public String getUser(String accountId);
	
	/**
	 * 校验订单号是否存在
	 * 
	 * @param form
	 * @return
	 */
	public boolean isExistsOrderId(ManualReverseBean form);
}

	