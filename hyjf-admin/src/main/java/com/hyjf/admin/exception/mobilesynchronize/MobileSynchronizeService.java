package com.hyjf.admin.exception.mobilesynchronize;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.MobileSynchronizeCustomize;

/**
 * 同步手机号Service
 * 
 * @author liuyang
 *
 */
public interface MobileSynchronizeService extends BaseService {

	/**
	 * 检索已开户用户数量
	 * 
	 * @param form
	 * @return
	 */
	public int countBankOpenAccountUsers(MobileSynchronizeBean form);

	/**
	 * 检索已开户用户列表
	 * 
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<MobileSynchronizeCustomize> selectBankOpenAccountUsersList(MobileSynchronizeBean form, int limitStart, int limitEnd);

	/**
	 * 更新用户手机号
	 * 
	 * @param accountId
	 * @param userId
	 * @return
	 */
	public boolean updateMobile(String accountId, String userId) throws Exception;

	/**
	 * 同步手机号之后,发送CA认证客户信息修改MQ
	 * @param userId
	 */
	void sendCAMQ(String userId);
}
