package com.hyjf.callcenter.withdrawal;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterWithdrawCustomize;

@Service
public class SrchWithdrawalInfoServiceImpl extends CustomizeMapper implements SrchWithdrawalInfoService {
	/**
	 * 获取提现列表
	 *
	 * @return
	 */
	@Override
	public List<CallcenterWithdrawCustomize> getWithdrawRecordList(CallcenterWithdrawCustomize callcenterWithdrawCustomize) {
		return this.callcenterwithdrawCustomizeMapper.selectWithdrawList(callcenterWithdrawCustomize);
	}

}
