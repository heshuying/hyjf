package com.hyjf.mybatis.mapper.customize;

import java.util.Map;

import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditRepayCustomize;

public interface CreditRepayCustomizeMapper {

	String countCreditRepaySum(Map<String,Object> params);

	AdminBorrowCreditRepayCustomize sumCreditRepay(CreditRepayExample example);
}