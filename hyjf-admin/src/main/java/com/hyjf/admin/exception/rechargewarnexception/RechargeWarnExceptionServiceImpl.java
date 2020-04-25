package com.hyjf.admin.exception.rechargewarnexception;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminRechargeWarnExceptionCustomize;

@Service
public class RechargeWarnExceptionServiceImpl extends BaseServiceImpl implements RechargeWarnExceptionService {

	/**
	 * 查询符合条件的充值记录数量
	 * 
	 * @param rechargeCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryRechargeWarnCount(AdminRechargeWarnExceptionCustomize rechargeWarnExceptionCustomize) {
		Integer accountCount = this.adminRechargeWarnExceptionCustomizeMapper.queryRechargeWarnCount(rechargeWarnExceptionCustomize);
		return accountCount;

	}

	/**
	 * 充值管理列表查询
	 * 
	 * @param rechargeCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<AdminRechargeWarnExceptionCustomize> queryRechargeWarnList(AdminRechargeWarnExceptionCustomize rechargeWarnExceptionCustomize) {
		List<AdminRechargeWarnExceptionCustomize> accountInfos = this.adminRechargeWarnExceptionCustomizeMapper.queryRechargeWarnList(rechargeWarnExceptionCustomize);
		return accountInfos;

	}

}
