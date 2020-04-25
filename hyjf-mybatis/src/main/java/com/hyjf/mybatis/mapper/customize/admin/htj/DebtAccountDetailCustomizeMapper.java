package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.htj.DebtAccountDetailCustomize;

public interface DebtAccountDetailCustomizeMapper {
	/**
	 * 资金明细 （明细数量）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountDetailCount(DebtAccountDetailCustomize accountDetailCustomize);

	/**
	 * 资金明细 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<DebtAccountDetailCustomize> queryAccountDetails(DebtAccountDetailCustomize accountDetailCustomize);

}
