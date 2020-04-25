package com.hyjf.admin.manager.borrow.increaseinterest.investdetail;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvestExample;

/**
 * 融通宝加息出借明细Service实现类
 * 
 * @ClassName InvestDetailServiceImpl
 * @author liuyang
 * @date 2016年12月28日 上午11:46:04
 */
@Service
public class IncreaseInterestInvestDetailServiceImpl extends BaseServiceImpl implements IncreaseInterestInvestDetailService {

	/**
	 * 融通宝加息交易明细检索件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	@Override
	public int countRecordList(IncreaseInterestInvestDetailBean form) {

		IncreaseInterestInvestExample example = new IncreaseInterestInvestExample();

		IncreaseInterestInvestExample.Criteria cra = example.createCriteria();
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			cra.andInvestUserNameLike("%" + form.getUserNameSrch() + "%");
		}
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 出借时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andCreateTimeBetween(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(form.getTimeStartSrch())), GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(form.getTimeEndSrch())));
		}
		return this.increaseInterestInvestMapper.countByExample(example);
	}

	/**
	 * 融通宝加息交易明细检索
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	@Override
	public List<IncreaseInterestInvest> selectRecordList(IncreaseInterestInvestDetailBean form, int limitStart, int limitEnd) {

		IncreaseInterestInvestExample example = new IncreaseInterestInvestExample();
		IncreaseInterestInvestExample.Criteria cra = example.createCriteria();
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			cra.andInvestUserNameLike("%" + form.getUserNameSrch() + "%");
		}
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 出借时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andCreateTimeBetween(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(form.getTimeStartSrch())), GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(form.getTimeEndSrch())));
		}
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		example.setOrderByClause(" create_time desc");
		return this.increaseInterestInvestMapper.selectByExample(example);
	}
	
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public String sumAccount(IncreaseInterestInvestDetailBean form) {
		
		IncreaseInterestInvestExample example = new IncreaseInterestInvestExample();
		IncreaseInterestInvestExample.Criteria cra = example.createCriteria();
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			cra.andInvestUserNameLike("%" + form.getUserNameSrch() + "%");
		}
		// 项目编号
		if (StringUtils.isNotEmpty(form.getBorrowNidSrch())) {
			cra.andBorrowNidEqualTo(form.getBorrowNidSrch());
		}
		// 出借时间
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			cra.andCreateTimeBetween(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(form.getTimeStartSrch())), GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(form.getTimeEndSrch())));
		}

		return this.increaseInterestInvestCustomizeMapper.sumAccount(example);
	}
}
