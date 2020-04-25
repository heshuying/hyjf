package com.hyjf.batch.borrow.bankcard;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.AccountChinapnr;

/**
 * 调用汇付接口,更新银行卡信息接口
 * 
 * @author 孙亮
 * @since 2016年1月18日 上午8:42:33
 */
public interface UpdateBankCardService extends BaseService {
	/**
	 * 获取所有的开户用户
	 * 
	 * @return
	 */
	public List<AccountChinapnr> getAllAccountChinapnr();

	/**
	 * 更新某开户用户的银行卡信息
	 * 
	 * @param accountChinapnr
	 * @return
	 */
	public Boolean updateAccountChinapnrBank(AccountChinapnr accountChinapnr);
}
