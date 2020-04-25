package com.hyjf.callcenter.transfer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterBorrowCreditCustomize;

@Service
public class SrchTransferInfoServiceImpl extends CustomizeMapper implements SrchTransferInfoService   {
	/**
	 * 按照用户名/手机号查询转让信息
	 * @param user
	 * @return List<AccountBank>
	 * @author LIBIN
	 */
	@Override
	public List<CallCenterBorrowCreditCustomize> selectBorrowCreditList(CallCenterBorrowCreditCustomize callCenterBorrowCreditCustomize) {
		return this.callCenterBorrowCreditCustomizeMapper.selectBorrowCreditList(callCenterBorrowCreditCustomize);
	}

}
