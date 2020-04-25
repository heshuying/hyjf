/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.user.transfer;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.UserTransfer;

public interface TransferService {

	/**
	 * 获取用户的汇付信息
	 * 
	 * @param userId
	 * @return 用户的汇付信息
	 */
	public AccountChinapnr getAccountChinapnr(Integer userId);

	/**
	 * 校验用户的转让参数
	 * @param orderId
	 * @param ret
	 * @return
	 */
	public UserTransfer checkTransferParam(String orderId, JSONObject ret);

	/**
	 * 更新用户转让为转让中
	 * @param userTransfer
	 * @return
	 */
	public boolean updateUserTransfer(UserTransfer userTransfer);

	/**
	 * 用户完成转让后，处理业务逻辑
	 * @param userTransfer
	 * @param ip 
	 * @return
	 * @throws Exception 
	 */
	public boolean updateAfterUserTansfer(UserTransfer userTransfer, String ip) throws Exception;

	/**
	 * 更新此笔债转为失败
	 * @param userTransfer
	 * @param date
	 * @return
	 */
	boolean updateUserTransferFail(UserTransfer userTransfer, Date date);
	/**
	 * @param userTransfer
	 * @return
	 */
		
	public UserTransfer selectUserTransfer(UserTransfer userTransfer);
}
