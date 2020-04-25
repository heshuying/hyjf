package com.hyjf.batch.synchronizeMeaage.mobile;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.AccountMobileAynch;

import java.util.List;

/**
 * 同步手机号Service
 * 
 * @author 李晟
 *
 */
public interface MobileSynchronizeService extends BaseService {



	/**
	 * 更新用户手机号
	 * 
	 * @param accountMobileAynch
	 * @return
	 */
	public boolean updateMobile(AccountMobileAynch accountMobileAynch) throws Exception;

	/**
	 * 同步手机号之后,发送CA认证客户信息修改MQ
	 * @param userId
	 */
	void sendCAMQ(String userId);

	/**
	 * 查询未同步数据
	 * @return
	 */
	public List<AccountMobileAynch> searchAccountMobileAynch(String flag);


}
