package com.hyjf.admin.manager.hjhplan.reinvestdetail;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDetailCustomize;

@Service
public class HjhReInvestDetailServiceImpl extends BaseServiceImpl implements HjhReInvestDetailService {

	/**
	 * 查询符合条件的资金明细数量
	 * 
	 * @param hjhReInvestDetailCustomize
	 * @return
	 * @author HJH
	 */
	@Override
	public Integer queryHjhReInvestDetailCount(HjhReInvestDetailCustomize hjhReInvestDetailCustomize) {
		Integer accountCount = this.hjhReInvestDetailCustomizeMapper.queryReInvestDetailCount(hjhReInvestDetailCustomize);
		return accountCount;

	}

	/**
	 * 资金明细列表查询
	 * 
	 * @param hjhReInvestDetailCustomize
	 * @return
	 * @author HJH
	 */
	@Override
	public List<HjhReInvestDetailCustomize> queryHjhReInvestDetails(HjhReInvestDetailCustomize hjhReInvestDetailCustomize) {
		List<HjhReInvestDetailCustomize> accountInfos = this.hjhReInvestDetailCustomizeMapper.queryReInvestDetails(hjhReInvestDetailCustomize);
		return accountInfos;

	}

	/**
	 * 查询用户交易明细的交易类型
	 * 
	 * @return
	 */
	@Override
	public String queryReInvestDetailTotal(HjhReInvestDetailCustomize hjhReInvestDetailCustomize) {
		String total = this.hjhReInvestDetailCustomizeMapper.queryReInvestDetailTotal(hjhReInvestDetailCustomize);
		return total;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param hjhReInvestDetailCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<HjhReInvestDetailCustomize> exportReInvestDetails(
			HjhReInvestDetailCustomize hjhReInvestDetailCustomize) {
		return this.hjhReInvestDetailCustomizeMapper.exportReInvestDetails(hjhReInvestDetailCustomize);
	}

}
