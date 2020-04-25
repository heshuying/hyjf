package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminRechargeWarnExceptionCustomize;
/**
 * 充值监控异常查询Mapper
 * 
 * @author 孙亮
 * @since 2016年3月30日16:53:45
 */
public interface AdminRechargeWarnExceptionCustomizeMapper {

	/**
	 * 根据条件查询银行卡个数
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryRechargeWarnCount(AdminRechargeWarnExceptionCustomize rechargeWarnExceptionCustomize);

	/**
	 * 根据条件查询银行卡列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<AdminRechargeWarnExceptionCustomize> queryRechargeWarnList(AdminRechargeWarnExceptionCustomize rechargeWarnExceptionCustomize);
}
