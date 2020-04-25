package com.hyjf.callcenter.undertake;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterBorrowCreditCustomize;

@Service
public class SrchUndertakeInfoServiceImpl extends CustomizeMapper implements SrchUndertakeInfoService  {

	@Override
	public List<CallCenterBorrowCreditCustomize> selectBorrowCreditTenderList(CallCenterBorrowCreditCustomize callCenterBorrowCreditCustomize) {
		return this.callCenterBorrowCreditCustomizeMapper.selectBorrowCreditInfoList(callCenterBorrowCreditCustomize);
		
		            
	}
}
