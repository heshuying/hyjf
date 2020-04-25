package com.hyjf.admin.manager.plan.joindetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeDetailCustomize;

/**
 * 汇添金加入明细Service实现类
 * 
 * @ClassName HtjJoinDetailServiceImpl
 * @author liuyang
 * @date 2016年9月29日 上午9:48:22
 */
@Service
public class HtjJoinDetailServiceImpl extends BaseServiceImpl implements HtjJoinDetailService {

	/**
	 * 检索加入明细的件数
	 * 
	 * @Title countAccedeRecord
	 * @param form
	 * @return
	 */
	@Override
	public int countAccedeRecord(HtjJoinDetailBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 计划订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 冻结订单号
		if (StringUtils.isNotEmpty(form.getFreezeOrderIdSrch())) {
			param.put("freezeOrderIdSrch", form.getFreezeOrderIdSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 用户属性
		if (StringUtils.isNotEmpty(form.getUserAttributeSrch())) {
			param.put("userAttributeSrch", form.getUserAttributeSrch());
		}
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入时间
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}

		return this.adminPlanAccedeDetailCustomizeMapper.countAccedeRecord(param);
	}

	/**
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	@Override
	public List<AdminPlanAccedeDetailCustomize> selectAccedeRecordList(HtjJoinDetailBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 计划订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 冻结订单号
		if (StringUtils.isNotEmpty(form.getFreezeOrderIdSrch())) {
			param.put("freezeOrderIdSrch", form.getFreezeOrderIdSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 用户属性
		if (StringUtils.isNotEmpty(form.getUserAttributeSrch())) {
			param.put("userAttributeSrch", form.getUserAttributeSrch());
		}
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入时间
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}
		if (form.getLimitStart() >= 0) {
			param.put("limitStart", form.getLimitStart());
		}
		if (form.getLimitEnd() >= 0) {
			param.put("limitEnd", form.getLimitEnd());
		}
		return this.adminPlanAccedeDetailCustomizeMapper.selectAccedeRecordList(param);
	}

	/**
	 * 检索加入总计金额
	 * 
	 * @Title sumJoinAccount
	 * @param form
	 * @return
	 */
	@Override
	public String sumJoinAccount(HtjJoinDetailBean form) {

		Map<String, Object> param = new HashMap<String, Object>();
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 计划订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 冻结订单号
		if (StringUtils.isNotEmpty(form.getFreezeOrderIdSrch())) {
			param.put("freezeOrderIdSrch", form.getFreezeOrderIdSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 用户属性
		if (StringUtils.isNotEmpty(form.getUserAttributeSrch())) {
			param.put("userAttributeSrch", form.getUserAttributeSrch());
		}
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入时间
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}

		return this.adminPlanAccedeDetailCustomizeMapper.sumJoinAccount(param);
	}

}
