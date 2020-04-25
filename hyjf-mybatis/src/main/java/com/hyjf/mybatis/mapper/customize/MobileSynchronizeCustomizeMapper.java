package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.MobileSynchronizeCustomize;

/**
 * 同步手机号Mapper
 * 
 * @author liuyang
 *
 */
public interface MobileSynchronizeCustomizeMapper {
	/**
	 * 检索已开户用户数量
	 * 
	 * @param param
	 * @return
	 */
	public Integer countBankOpenAccountUsers(Map<String, Object> param);

	/**
	 * 检索已开户用户列表
	 * 
	 * @param param
	 * @return
	 */
	public List<MobileSynchronizeCustomize> selectBankOpenAccountUsersList(Map<String, Object> param);
}
