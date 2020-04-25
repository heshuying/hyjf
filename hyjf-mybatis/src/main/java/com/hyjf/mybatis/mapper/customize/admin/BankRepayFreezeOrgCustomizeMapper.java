package com.hyjf.mybatis.mapper.customize.admin;

import com.hyjf.mybatis.model.customize.admin.BankRepayFreezeOrgCustomize;

import java.util.List;
import java.util.Map;

/**
 * 担保机构代偿冻结
 */
public interface BankRepayFreezeOrgCustomizeMapper {

	List<BankRepayFreezeOrgCustomize> selectList(Map<String, Object> paraMap);

	int selectCount(Map<String, Object> paraMap);

}

	