package com.hyjf.admin.exception.offline.recharge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.OfflineRechargeCustomize;

@Service
public class OfflineRechargeServiceImpl extends BaseServiceImpl implements OfflineRechargeService {

	/**
	 * 取得需要查询的线下充值的用户信息
	 */
	@Override
	public List<OfflineRechargeCustomize> selectUserAccount(OfflineRechargeBean offlineRechargeBean) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("username", offlineRechargeBean.getUsernameSrch());
		paramMap.put("mobile", offlineRechargeBean.getPhoneSrch());
		paramMap.put("bankOpenAccount", offlineRechargeBean.getBankOpenAccountSrch());
		List<OfflineRechargeCustomize> listUserAccount = this.offlineRechargeCustomizeMapper.selectUserAccount(paramMap);
		return listUserAccount;
	}
	

}
