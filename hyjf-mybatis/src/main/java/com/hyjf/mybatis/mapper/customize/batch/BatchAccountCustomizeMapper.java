package com.hyjf.mybatis.mapper.customize.batch;

import com.hyjf.mybatis.model.auto.Account;

/**
 * 会员用户开户记录初始化列表查询
 * 
 * @ClassName BatchAccountCustomizeMapper
 * @author liuyang
 * @date 2017年1月3日 下午3:53:50
 */
public interface BatchAccountCustomizeMapper {

	/**
	 * 加息还款后,更新出借人账户信息
	 * 
	 * @Title updateAccountAfterRepay
	 * @param account
	 * @return
	 */
	public int updateAccountAfterRepay(Account account);

}
