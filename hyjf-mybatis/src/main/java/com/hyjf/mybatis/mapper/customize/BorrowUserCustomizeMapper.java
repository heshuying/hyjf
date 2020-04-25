package com.hyjf.mybatis.mapper.customize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.Users;

/**
 * 债权迁移后,更新借款人账户信息,检索借款人列表用
 * 
 * @author liuyang
 *
 */

public interface BorrowUserCustomizeMapper {
	/**
	 * 检索借款人列表
	 * 
	 * @return
	 */
	public List<Users> searchBorrowUserList();

	/**
	 * 获取借款人待还管理费
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getBorrowUserBankWaitRepay(Map<String, Object> param);

}
