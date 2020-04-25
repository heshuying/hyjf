package com.hyjf.mqreceiver.wrb.service.impl;

import com.hyjf.mybatis.model.customize.wrb.WrbTenderNotifyCustomize;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mqreceiver.wrb.service.WrbCallBackService;

/**
 * @author xiasq
 * @version WrbCallBackServiceImpl, v0.1 2018/3/8 11:26
 */

@Service
public class WrbCallBackServiceImpl extends BaseServiceImpl implements WrbCallBackService {

	/**
	 * 风车理财根据出借订单号查询出借信息
	 * @param nid
	 * @param userId
	 * @return
	 */
	@Override
	public WrbTenderNotifyCustomize searchBorrowTenderByNid(String nid, Integer userId) {
		return wrbQueryCustomizeMapper.selectWrbTenderInfo(nid,userId);
	}
}
