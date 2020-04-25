package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.htj.DebtAdminCreditCustomize;

/**
 * 
 * @ClassName DebtAdminCreditCustomizeMapper
 * @author 汇添金发起的转让类产品
 * @date 2016年11月7日 下午6:12:56
 */
public interface DebtAdminCreditCustomizeMapper {

	/**
	 * 检索汇添金转让类产品件数
	 * 
	 * @Title countCreditProject
	 * @param param
	 * @return
	 */
	public int countCreditProject(Map<String, Object> param);

	/**
	 * 检索汇添金转让类产品列表
	 * 
	 * @Title selectDebtCreditProject
	 * @param param
	 * @return
	 */
	public List<DebtAdminCreditCustomize> selectDebtCreditProject(Map<String, Object> param);

}
