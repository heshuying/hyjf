package com.hyjf.admin.manager.plan.plancommon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanBorrow;
import com.hyjf.mybatis.model.auto.DebtPlanBorrowExample;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.auto.DebtPlanConfigExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;

/**
 * 计划录入共通Service实现类
 * 
 * @ClassName PlanCommonServiceImpl
 * @author liuyang
 * @date 2016年9月18日 下午2:28:49
 */
@Service
public class PlanCommonServiceImpl extends BaseServiceImpl implements PlanCommonService {

	/**
	 * 获取计划配置信息
	 * 
	 * @Title getDebtPlanConfigList
	 * @return
	 */
	@Override
	public List<DebtPlanConfig> getDebtPlanConfigList() {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		DebtPlanConfigExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(1);
		cra.andDelFlagEqualTo(0);
		List<DebtPlanConfig> result = this.debtPlanConfigMapper.selectByExample(example);
		return result;
	}

	/**
	 * 获取还款方式
	 * 
	 * @Title getBorrowStyleList
	 * @return
	 */
	@Override
	public List<BorrowStyle> getBorrowStyleList() {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(0);
		List<BorrowStyle> result = this.borrowStyleMapper.selectByExample(example);
		return result;
	}

	/**
	 * 获取计划的关联资产的件数
	 * 
	 * @Title countDebtPlanBorrowList
	 * @param param
	 * @return
	 */
	@Override
	public int countDebtPlanBorrowList(Map<String, Object> param) {
		return this.planCustomizeMapper.countDebtPlanBorrowList(param);
	}

	/**
	 * 获取计划的关联资产信息
	 * 
	 * @Title getDebtPlanBorrowList
	 * @param param
	 * @return
	 */
	@Override
	public List<DebtPlanBorrowCustomize> getDebtPlanBorrowList(Map<String, Object> param) {
		List<DebtPlanBorrowCustomize> result = this.planCustomizeMapper.getDebtPlanBorrowList(param);
		return result;
	}

	/**
	 * 获取计划预编号
	 * 
	 * @Title getPlanPreNid
	 * @return
	 */
	@Override
	public String getPlanPreNid() {
		String yyyymm = GetDate.getServerDateTime(13, new Date());
		String mmdd = yyyymm.substring(2);

		String planPreNid = this.planCustomizeMapper.getPlanPreNid(mmdd);
		if (StringUtils.isEmpty(planPreNid)) {
			return mmdd + "001";
		}
		return String.valueOf(Long.valueOf(planPreNid) + 1);
	}

	/**
	 * 根据主键判断借款数据是否存在
	 * 
	 * @Title isExistsRecord
	 * @param planNid
	 * @param planPreNid
	 * @return
	 */
	@Override
	public boolean isExistsRecord(String planNid, String planPreNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(planNid)) {
			cra.andDebtPlanNidEqualTo(planNid);
		}
		if (StringUtils.isNotEmpty(planPreNid)) {
			cra.andDebtPrePlanNidEqualTo(Integer.valueOf(planPreNid));
		}
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);

		if (result != null && result.size() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取计划数据
	 * 
	 * @Title getPlanInfo
	 * @param planCommonBean
	 * @return
	 */
	@Override
	public PlanCommonBean getPlanInfo(PlanCommonBean planCommonBean) {
		DebtPlanWithBLOBs debtPlanWithBLOBs = this.getDebtPlanWithBLOBs(planCommonBean.getDebtPlanNid());
		if (debtPlanWithBLOBs != null) {
			// 计划信息数据放置
			this.getPlanCommonFiled(planCommonBean, debtPlanWithBLOBs);
		}
		return planCommonBean;
	}

	/**
	 * 获取计划信息
	 * 
	 * @Title getDebtPlanWithBLOBs
	 * @param planNid
	 * @return
	 */
	@Override
	public DebtPlanWithBLOBs getDebtPlanWithBLOBs(String planNid) {
		if (StringUtils.isEmpty(planNid)) {
			return null;
		}
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		cra.andDebtPlanNidEqualTo(planNid);
		List<DebtPlanWithBLOBs> result = this.debtPlanMapper.selectByExampleWithBLOBs(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 计划数据获取
	 * 
	 * @Title getPlanCommonFiled
	 * @param planCommonBean
	 * @param debtPlanWithBLOBs
	 */
	@Override
	public void getPlanCommonFiled(PlanCommonBean planCommonBean, DebtPlanWithBLOBs debtPlanWithBLOBs) {
		// 计划预编号
		planCommonBean.setDebtPlanPreNid(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtPrePlanNid())));
		// 计划编号
		planCommonBean.setDebtPlanNid(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtPlanNid())));
		// 计划类型
		planCommonBean.setDebtPlanType(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtPlanType())));
		// 计划类型名称
		planCommonBean.setDebtPlanTypeName(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtPlanTypeName())));
		// 计划名称
		planCommonBean.setDebtPlanName(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtPlanName())));
		// 计划金额
		planCommonBean.setDebtPlanMoney(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtPlanMoney().intValue())));
		// 锁定期
		planCommonBean.setDebtLockPeriod(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtLockPeriod())));
		// 预期出借利率率
		planCommonBean.setExpectApr(this.getValue(String.valueOf(debtPlanWithBLOBs.getExpectApr())));
		// 退出方式
		planCommonBean.setDebtQuitStyle(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtQuitStyle())));
		// 退出天数
		planCommonBean.setDebtQuitPeriod(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtQuitPeriod())));
		// 计划概念
		planCommonBean.setPlanConcept(this.getValue(String.valueOf(debtPlanWithBLOBs.getPlanConcept())));
		// 计划原理
		planCommonBean.setPlanPrinciple(this.getValue(String.valueOf(debtPlanWithBLOBs.getPlanPrinciple())));
		// 风控保障措施
		planCommonBean.setSafeguardMeasures(this.getValue(String.valueOf(debtPlanWithBLOBs.getSafeguardMeasures())));
		// 风险保证金措施
		planCommonBean.setMarginMeasures(this.getValue(String.valueOf(debtPlanWithBLOBs.getMarginMeasures())));
		// 计划状态
		planCommonBean.setDebtPlanStatus(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtPlanStatus())));
		// 申购开始时间
		planCommonBean.setBuyBeginTime(this.getValue(GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(debtPlanWithBLOBs.getBuyBeginTime()))));
		// 申购期限(天)
		planCommonBean.setBuyPeriodDay(this.getValue(String.valueOf(debtPlanWithBLOBs.getBuyPeriodDay())));
		// 申购期限(小时)
		planCommonBean.setBuyPeriodHour(this.getValue(String.valueOf(debtPlanWithBLOBs.getBuyPeriodHour() == 0 ? StringUtils.EMPTY : debtPlanWithBLOBs.getBuyPeriodHour())));
		// 申购总期限
		planCommonBean.setBuyPeriod(this.getValue(String.valueOf(debtPlanWithBLOBs.getBuyPeriod())));
		// 最低加入金额
		planCommonBean.setDebtMinInvestment(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtMinInvestment())));
		// 递增金额
		planCommonBean.setDebtInvestmentIncrement(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtInvestmentIncrement())));
		// 最大出借金额
		planCommonBean.setDebtMaxInvestment(this.getValue(String.valueOf(debtPlanWithBLOBs.getDebtMaxInvestment())));
		// 是否立即审核
		planCommonBean.setIsAudits(this.getValue(String.valueOf(debtPlanWithBLOBs.getIsAudits())));
		// 可用券配置
		planCommonBean.setCouponConfig(this.getValue(debtPlanWithBLOBs.getCouponConfig()));
	}

	private String getValue(String value) {
		if (StringUtils.isNotEmpty(value)) {
			return value;
		}
		return "";
	}

	/**
	 * 根据计划类型检索已经发布该类型的计划数量
	 * 
	 * @Title getPlanByDebtPlanType
	 * @param debtPlanType
	 * @return
	 */
	@Override
	public int getPlanByDebtPlanType(String debtPlanType) {
		int ret = 0;
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanType)) {
			cra.andDebtPlanTypeEqualTo(Integer.parseInt(debtPlanType));
		}
		cra.andShowStatusEqualTo(0);
		ret = this.debtPlanMapper.countByExample(example);
		return ret;
	}

	/**
	 * 根据计划类型检索计划配置信息
	 * 
	 * @Title getPlanConfigByDebtPlanType
	 * @param debtPlanType
	 * @return
	 */
	@Override
	public DebtPlanConfig getPlanConfigByDebtPlanType(String debtPlanType) {
		DebtPlanConfigExample example = new DebtPlanConfigExample();
		DebtPlanConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanType)) {
			cra.andDebtPlanTypeEqualTo(Integer.valueOf(debtPlanType));
		}
		List<DebtPlanConfig> result = this.debtPlanConfigMapper.selectByExample(example);
		if (result != null && result.size() != 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 判断计划名称是否重复
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param request
	 * @return
	 */
	@Override
	public String isDebtPlanNameExist(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		String debtPlanNid = request.getParameter("debtPlanNid");
		message = message.replace("{label}", "智投名称");
		String param = request.getParameter("param");
		if (StringUtils.isEmpty(param)) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		int debtPlanCount = this.isDebtPlanNameExist(param, debtPlanNid);
		// 计划名称重复
		if (debtPlanCount != 0) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, "智投名称不能重复！");
			return ret.toString();
		}
		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
		return ret.toJSONString();
	}

	/**
	 * 根据计划名称获取计划数量
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param debtPlanTypeName
	 * @return
	 */
	public int isDebtPlanNameExist(String debtPlanName, String debtPlanNid) {
		int ret = 0;
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanName)) {
			cra.andDebtPlanNameEqualTo(debtPlanName);
			cra.andDelFlagEqualTo(0);
		}
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			cra.andDebtPlanNidNotEqualTo(debtPlanNid);
		}
		ret = this.debtPlanMapper.countByExample(example);
		return ret;
	}

	/**
	 * 
	 * 画面信息校验
	 * 
	 * @Title validatorFieldCheck
	 * @param mav
	 * @param planCommonBean
	 * @param isExistsRecord
	 */
	@Override
	public void validatorFieldCheck(ModelAndView mav, PlanCommonBean planCommonBean, boolean isExistsRecord) {
		// 计划类型
		if (!isExistsRecord) {
			ValidatorFieldCheckUtil.validateRequired(mav, "debtPlanType", planCommonBean.getDebtPlanType());
		}
		// 计划预编号
		if (!isExistsRecord) {
			String debtPlanPreNid = StringUtils.isEmpty(planCommonBean.getDebtPlanPreNid()) ? planCommonBean.getDebtPlanNid() : planCommonBean.getDebtPlanPreNid();
			boolean planPreNidFlag = ValidatorFieldCheckUtil.validateRequired(mav, "planPreNid", debtPlanPreNid);
			if (planPreNidFlag) {
				if (StringUtils.isEmpty(planCommonBean.getDebtPlanPreNid())) {
					planPreNidFlag = this.isExistsRecord(planCommonBean.getDebtPlanNid(), StringUtils.EMPTY);
				} else {
					planPreNidFlag = this.isExistsRecord(StringUtils.EMPTY, planCommonBean.getDebtPlanPreNid());
				}
				if (planPreNidFlag) {
					ValidatorFieldCheckUtil.validateSpecialError(mav, "planPreNid", "repeat");
				}
			}
		}
		// 计划名称
		if (!isExistsRecord) {
			String debtPlanName = planCommonBean.getDebtPlanName();
			int debtPlanNameCount = this.isDebtPlanNameExist(debtPlanName, StringUtils.EMPTY);
			if (debtPlanNameCount > 0) {
				ValidatorFieldCheckUtil.validateSpecialError(mav, "debtPlanName", "repeat");
			}
		}
		// 计划金额
		boolean debtPlanMoneyFlag = ValidatorFieldCheckUtil.validateSignlessNum(mav, "debtPlanMoney", planCommonBean.getDebtPlanMoney(), 10, true);
		// 预期出借利率
		ValidatorFieldCheckUtil.validateSignlessNumLength(mav, "expectApr", planCommonBean.getExpectApr(), 2, 2, true);
		// 退出所需天数
		ValidatorFieldCheckUtil.validateRequired(mav, "debtQuitPeriod", planCommonBean.getDebtQuitPeriod());
		// 提交审核
		ValidatorFieldCheckUtil.validateRequired(mav, "isAudits", planCommonBean.getIsAudits());
		// 计划概念
		ValidatorFieldCheckUtil.validateRequired(mav, "planConcept", planCommonBean.getPlanConcept());
		// 计划原理
		ValidatorFieldCheckUtil.validateRequired(mav, "planPrinciple", planCommonBean.getPlanPrinciple());
		// 风控保障措施
		ValidatorFieldCheckUtil.validateRequired(mav, "safeguardMeasures", planCommonBean.getSafeguardMeasures());
		// 风险保证金措施
		ValidatorFieldCheckUtil.validateRequired(mav, "marginMeasures", planCommonBean.getMarginMeasures());
		// 申购开始时间
		ValidatorFieldCheckUtil.validateRequired(mav, "buyBeginTime", planCommonBean.getBuyBeginTime());
		// 申购期限(天)
		ValidatorFieldCheckUtil.validateRequired(mav, "buyPeriodDay", planCommonBean.getBuyPeriodDay());
		// 最低加入金额
		boolean debtMinInvestmentFlag = ValidatorFieldCheckUtil.validateDecimal(mav, "debtMinInvestment", planCommonBean.getDebtMinInvestment(), 10, true);
		// 递增金额
		ValidatorFieldCheckUtil.validateDecimal(mav, "debtInvestmentIncrement", planCommonBean.getDebtInvestmentIncrement(), 10, true);
		// 最高加入金额
		ValidatorFieldCheckUtil.validateDecimal(mav, "debtMaxInvestment", planCommonBean.getDebtMaxInvestment(), 10, true);
		// 最高加入金额
		boolean debtMaxInvestmentFlag = ValidatorFieldCheckUtil.validateDecimal(mav, "debtMaxInvestment", planCommonBean.getDebtMaxInvestment(), 10, false);

		if (debtMinInvestmentFlag && debtPlanMoneyFlag && debtMaxInvestmentFlag && StringUtils.isNotEmpty(planCommonBean.getDebtMinInvestment())
				&& StringUtils.isNotEmpty(planCommonBean.getDebtPlanMoney()) && StringUtils.isNotEmpty(planCommonBean.getDebtMaxInvestment())) {
			// 最低加入金额
			BigDecimal debtMinInvestment = new BigDecimal(planCommonBean.getDebtMinInvestment());
			// 计划金额
			BigDecimal debtPlanMoney = new BigDecimal(planCommonBean.getDebtPlanMoney());
			// 最高加入金额
			BigDecimal debtMaxInvestment = new BigDecimal(planCommonBean.getDebtMaxInvestment());
			// 最低加入金额≤最高加入金额≤计划金额
			if (debtMinInvestment.compareTo(debtPlanMoney) > 0) {
				ValidatorFieldCheckUtil.validateSpecialError(mav, "debtMinInvestment", "debtMinInvestment");
			}
			if (debtMaxInvestment.compareTo(debtPlanMoney) > 0) {
				ValidatorFieldCheckUtil.validateSpecialError(mav, "debtMaxInvestment", "debtMaxInvestment");
			}
			if (debtMinInvestment.compareTo(debtMaxInvestment) > 0) {
				ValidatorFieldCheckUtil.validateSpecialError(mav, "debtMinInvestment", "debtMinInvestment.error");
			}
		}

		// 资产配置不为空的情况
		if (StringUtils.isNotEmpty(planCommonBean.getDebtPlanBorrowNid()) && !isExistsRecord) {
			// 资产配置信息更新
			List<String> debtPlanBorrowNid = JSONArray.parseArray(planCommonBean.getDebtPlanBorrowNid(), String.class);
			for (int i = 0; i < debtPlanBorrowNid.size(); i++) {
				String borrowNid = debtPlanBorrowNid.get(i);
				DebtBorrow borrow = this.getBorrow(borrowNid);
				if (borrow != null && borrow.getBorrowAccountWait().compareTo(BigDecimal.ZERO) == 0) {
					ValidatorFieldCheckUtil.validateSpecialError(mav, "borrow", "borrow.error");
				}
			}
		}

	}

	/**
	 * 获取资产配置信息
	 * 
	 * @Title getBorrow
	 * @param borrowNid
	 * @return
	 */
	private DebtBorrow getBorrow(String borrowNid) {
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrow> list = this.debtBorrowMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 插入操作
	 * 
	 * @Title insertRecord
	 * @param planCommonBean
	 * @throws Exception
	 */
	@Override
	public void insertRecord(PlanCommonBean planCommonBean) throws Exception {
		DebtPlanWithBLOBs plan = new DebtPlanWithBLOBs();
		// 获取计划配置信息
		DebtPlanConfig debtPlanConfig = this.getPlanConfigByDebtPlanType(planCommonBean.getDebtPlanType());
		if (debtPlanConfig != null) {
			// 是否立即审核
			String isAudits = planCommonBean.getIsAudits();
			// 立即审核
			if (CustomConstants.PLAN_ISAUDITS_YES.equals(isAudits)) {
				// 待审核
				plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_1);
			} else {
				// 发起中
				plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_0);
			}
			// 计划类型
			plan.setDebtPlanType(Integer.parseInt(planCommonBean.getDebtPlanType()));
			// 计划类型名称
			plan.setDebtPlanTypeName(debtPlanConfig.getDebtPlanTypeName());
			// 计划编号
			plan.setDebtPlanNid(planCommonBean.getDebtPlanNid());
			// 预发布计划编号
			plan.setDebtPrePlanNid(StringUtils.isEmpty(planCommonBean.getDebtPlanPreNid()) ? Integer.parseInt(planCommonBean.getDebtPlanPreNidHid()) : Integer.parseInt(planCommonBean
					.getDebtPlanPreNid()));
			// 计划名称
			plan.setDebtPlanName(planCommonBean.getDebtPlanName());
			// 计划金额
			BigDecimal debtPlanMoney = new BigDecimal(planCommonBean.getDebtPlanMoney());
			plan.setDebtPlanMoney(debtPlanMoney);
			// 计划加入金额
			plan.setDebtPlanMoneyYes(BigDecimal.ZERO);
			// 计划余额
			plan.setDebtPlanMoneyWait(BigDecimal.ZERO);
			// 计划完成率
			plan.setDebtPlanAccountScale(BigDecimal.ZERO);
			// 预期出借利率
			plan.setExpectApr(new BigDecimal(planCommonBean.getExpectApr()));
			// 实际出借利率
			plan.setActualApr(BigDecimal.ZERO);
			// 出借范围(一期未实装)
			plan.setInvestmentScope(StringUtils.EMPTY);
			// 退出方式
			plan.setDebtQuitStyle(debtPlanConfig.getDebtQuitStyle());
			// 退出天数
			plan.setDebtQuitPeriod(Integer.parseInt(planCommonBean.getDebtQuitPeriod()));
			// 是否立即审核
			plan.setIsAudits(planCommonBean.getIsAudits());
			// 计划概念
			plan.setPlanConcept(planCommonBean.getPlanConcept());
			// 计划原理
			plan.setPlanPrinciple(planCommonBean.getPlanPrinciple());
			// 风控保障措施
			plan.setSafeguardMeasures(planCommonBean.getSafeguardMeasures());
			// 风险保证金措施
			plan.setMarginMeasures(planCommonBean.getMarginMeasures());

			Date date = GetDate.stringToFormatDate(planCommonBean.getBuyBeginTime(), "yyyy-MM-dd HH:mm");
			Long buyBeginTime = GetDate.getMillis(date) / 1000;
			// 申购开始时间
			plan.setBuyBeginTime(buyBeginTime.intValue());
			// 申购期限(天)
			int day = Integer.parseInt(planCommonBean.getBuyPeriodDay());
			// 申购期限(小时)
			int hour = Integer.parseInt(StringUtils.isEmpty(planCommonBean.getBuyPeriodHour()) ? "0" : planCommonBean.getBuyPeriodHour());
			int buyEndTime = buyBeginTime.intValue() + day * 24 * 60 * 60 + hour * 60 * 60;
			// 申购结束时间
			plan.setBuyEndTime(buyEndTime);
			// 申购期限（天）
			plan.setBuyPeriodDay(Integer.parseInt(planCommonBean.getBuyPeriodDay()));
			// 申购期限(小时)
			plan.setBuyPeriodHour(StringUtils.isEmpty(planCommonBean.getBuyPeriodHour()) ? 0 : Integer.parseInt(planCommonBean.getBuyPeriodHour()));
			// 申购总期限（小时）
			plan.setBuyPeriod(Integer.parseInt(planCommonBean.getBuyPeriodDay()) * 24
					+ (StringUtils.isEmpty(planCommonBean.getBuyPeriodHour()) ? 0 : Integer.parseInt(planCommonBean.getBuyPeriodHour())));
			// 锁定期(月)
			plan.setDebtLockPeriod(debtPlanConfig.getDebtLockPeriod());
			// 最低出借金额
			plan.setDebtMinInvestment(new BigDecimal(planCommonBean.getDebtMinInvestment()));
			// 出借增量
			plan.setDebtInvestmentIncrement(new BigDecimal(planCommonBean.getDebtInvestmentIncrement()));
			// 最高可出借金额
			plan.setDebtMaxInvestment(StringUtils.isEmpty(planCommonBean.getDebtMaxInvestment()) ? BigDecimal.ZERO : new BigDecimal(planCommonBean.getDebtMaxInvestment()));
			// 计划可用余额
			plan.setDebtPlanBalance(BigDecimal.ZERO);
			// 计划总冻结金额
			plan.setDebtPlanFrost(BigDecimal.ZERO);
			// 满标/到期时间
			plan.setFullExpireTime(0);
			// 应还款总额
			plan.setRepayAccountAll(BigDecimal.ZERO);
			// 应还利息
			plan.setRepayAccountInterest(BigDecimal.ZERO);
			// 应还本金
			plan.setRepayAccountCapital(BigDecimal.ZERO);
			// 实还总额
			plan.setRepayAccountYes(BigDecimal.ZERO);
			// 实还利息
			plan.setRepayAccountInterestYes(BigDecimal.ZERO);
			// 实还本金
			plan.setRepayAccountCapitalYes(BigDecimal.ZERO);
			// 未还款总额
			plan.setRepayAccountWait(BigDecimal.ZERO);
			// 未还款利息
			plan.setRepayAccountInterestWait(BigDecimal.ZERO);
			// 未还款本金
			plan.setRepayAccountCapitalWait(BigDecimal.ZERO);
			// 服务费
			plan.setServiceFee(BigDecimal.ZERO);
			// 最小出借笔数
			plan.setMinInvestNumber(debtPlanConfig.getMinInvestNumber());
			// 最大出借笔数
			plan.setMaxInvestNumber(debtPlanConfig.getMaxInvestNumber());
			// 出借循环次数
			plan.setCycleTimes(debtPlanConfig.getCycleTimes());
			// 无视规则出借次数
			plan.setUnableCycleTimes(debtPlanConfig.getUnableCycleTimes());
			// 出借金额界定
			plan.setInvestAccountLimit(debtPlanConfig.getInvestAccountLimit());
			// 最低剩余出借金额
			plan.setMinSurplusInvestAccount(debtPlanConfig.getMinSurplusInvestAccount());
			// 取整金额
			plan.setRoundAmount(debtPlanConfig.getRoundAmount());
			// web是否表示(汇添金测试用:0:表示,1:不表示)
			plan.setShowStatus(0);
			// 应清算时间
			plan.setLiquidateShouldTime(0);
			// 实际清算时间
			plan.setLiquidateFactTime(0);
			// 还款时间
			plan.setDelFlag(0);
			AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
			plan.setCreateUserId(Integer.parseInt(adminSystem.getId()));
			// 创建人用户名
			plan.setCreateUserName(adminSystem.getUsername());
			// 发起时间
			plan.setCreateTime(GetDate.getMyTimeInMillis());
			// 修改人ID
			plan.setUpdateUserId(null);
			// 修改时间
			plan.setUpdateTime(null);
			// 修改人用户名
			plan.setUpdateUserName(null);
			// 审核人ID
			plan.setAuditUserId(null);
			// 审核时间
			plan.setAuditTime(null);
			// 审核备注
			plan.setAuditRemark(null);
			plan.setLiquidateArrivalAmount(BigDecimal.ZERO);
			plan.setLiquidateApr(BigDecimal.ZERO);
			// 提成计算状态
			plan.setCommissionStatus(0);
			// 提成总额
			plan.setCommissionTotal(BigDecimal.ZERO);
			// 可用券配置
			plan.setCouponConfig(planCommonBean.getCouponConfig());
			this.debtPlanMapper.insertSelective(plan);
		}
	}

	/**
	 * 更新操作
	 * 
	 * @Title updateRecord
	 * @param planCommonBean
	 * @throws Exception
	 */
	@Override
	public void updateRecord(PlanCommonBean planCommonBean) throws Exception {
		// 计划编号
		String debtPlanNid = planCommonBean.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			DebtPlanExample example = new DebtPlanExample();
			DebtPlanExample.Criteria cra = example.createCriteria();
			cra.andDebtPlanNidEqualTo(debtPlanNid);
			List<DebtPlanWithBLOBs> planList = this.debtPlanMapper.selectByExampleWithBLOBs(example);
			if (planList != null && planList.size() > 0) {
				DebtPlanWithBLOBs plan = planList.get(0);

				// 计划金额
				plan.setDebtPlanMoney(StringUtils.isEmpty(planCommonBean.getDebtPlanMoney()) ? BigDecimal.ZERO : new BigDecimal(planCommonBean.getDebtPlanMoney()));
				// 预期出借利率
				plan.setExpectApr(StringUtils.isEmpty(planCommonBean.getExpectApr()) ? BigDecimal.ZERO : new BigDecimal(planCommonBean.getExpectApr()));
				// 退出所需天数
				plan.setDebtQuitPeriod(StringUtils.isEmpty(planCommonBean.getDebtQuitPeriod()) ? 0 : Integer.parseInt(planCommonBean.getDebtQuitPeriod()));
				// 计划概念
				plan.setPlanConcept(StringUtils.isEmpty(planCommonBean.getPlanConcept()) ? StringUtils.EMPTY : planCommonBean.getPlanConcept());
				// 计划原理
				plan.setPlanPrinciple(StringUtils.isEmpty(planCommonBean.getPlanPrinciple()) ? StringUtils.EMPTY : planCommonBean.getPlanPrinciple());
				// 风控保障措施
				plan.setSafeguardMeasures(StringUtils.isEmpty(planCommonBean.getSafeguardMeasures()) ? StringUtils.EMPTY : planCommonBean.getSafeguardMeasures());
				// 风险保证金措施
				plan.setMarginMeasures(StringUtils.isEmpty(planCommonBean.getMarginMeasures()) ? StringUtils.EMPTY : planCommonBean.getMarginMeasures());
				// 申购开始时间
				Date date = GetDate.stringToFormatDate(planCommonBean.getBuyBeginTime(), "yyyy-MM-dd HH:mm");
				Long buyBeginTime = GetDate.getMillis(date) / 1000;
				plan.setBuyBeginTime(buyBeginTime.intValue());
				// 申购期限(天)
				int day = Integer.parseInt(planCommonBean.getBuyPeriodDay());
				// 申购期限(小时)
				int hour = Integer.parseInt(StringUtils.isEmpty(planCommonBean.getBuyPeriodHour()) ? "0" : planCommonBean.getBuyPeriodHour());
				int buyEndTime = buyBeginTime.intValue() + day * 24 * 60 * 60 + hour * 60 * 60;
				// 申购结束时间
				plan.setBuyEndTime(buyEndTime);
				// 申购期限（天）
				plan.setBuyPeriodDay(Integer.parseInt(planCommonBean.getBuyPeriodDay()));
				// 申购期限(小时)
				plan.setBuyPeriodHour(StringUtils.isEmpty(planCommonBean.getBuyPeriodHour()) ? 0 : Integer.parseInt(planCommonBean.getBuyPeriodHour()));
				// 申购总期限（小时）
				plan.setBuyPeriod(Integer.parseInt(planCommonBean.getBuyPeriodDay()) * 24
						+ (StringUtils.isEmpty(planCommonBean.getBuyPeriodHour()) ? 0 : Integer.parseInt(planCommonBean.getBuyPeriodHour())));
				// 最低出借金额
				plan.setDebtMinInvestment(new BigDecimal(planCommonBean.getDebtMinInvestment()));
				// 出借增量
				plan.setDebtInvestmentIncrement(new BigDecimal(planCommonBean.getDebtInvestmentIncrement()));
				// 最高可出借金额
				plan.setDebtMaxInvestment(StringUtils.isEmpty(planCommonBean.getDebtMaxInvestment()) ? BigDecimal.ZERO : new BigDecimal(planCommonBean.getDebtMaxInvestment()));

				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				// 更新时间
				plan.setUpdateTime(GetDate.getMyTimeInMillis());
				// 更新用户ID
				plan.setUpdateUserId(Integer.parseInt(adminSystem.getId()));
				// 更新用户名
				plan.setUpdateUserName(adminSystem.getUsername());
				// 可用券配置
				plan.setCouponConfig(planCommonBean.getCouponConfig());

				// 数据更新
				this.debtPlanMapper.updateByPrimaryKeySelective(plan);
			}
		}
	}

	/**
	 * 计划的资产配置信息插入
	 * 
	 * @Title insertDebtPlanBorrowNidRecord
	 * @param planCommonBean
	 * @param debtPlanBorrowNidList
	 */
	@Override
	public void insertDebtPlanBorrowNidRecord(PlanCommonBean planCommonBean, List<String> debtPlanBorrowNidList) {
		// 根据计划ID获取已经存在的资产配置信息
		String debtPlanNid = planCommonBean.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			// 先将既存的资产配置删除
			DebtPlanBorrowExample example = new DebtPlanBorrowExample();
			DebtPlanBorrowExample.Criteria cra = example.createCriteria();
			cra.andDebtPlanNidEqualTo(debtPlanNid);
			this.debtPlanBorrowMapper.deleteByExample(example);
			for (int i = 0; i < debtPlanBorrowNidList.size(); i++) {
				String debtPlanBorrowNid = debtPlanBorrowNidList.get(i);
				// 根据借款nid查询Debtborrow表
				DebtBorrowExample borrowExample = new DebtBorrowExample();
				DebtBorrowExample.Criteria borrowCra = borrowExample.createCriteria();
				borrowCra.andBorrowNidEqualTo(debtPlanBorrowNid);
				List<DebtBorrow> borrowList = this.debtBorrowMapper.selectByExample(borrowExample);
				if (borrowList != null && borrowList.size() != 0) {
					DebtBorrow borrow = borrowList.get(0);
					// 是否有计划关联:1是
					borrow.setBorrowPlanSelected(1);
					this.debtBorrowMapper.updateByPrimaryKey(borrow);
				}

				DebtPlanBorrow debtPlanBorrow = new DebtPlanBorrow();
				debtPlanBorrow.setBorrowNid(debtPlanBorrowNid);
				debtPlanBorrow.setDebtPlanNid(debtPlanNid);
				debtPlanBorrow.setType(0);
				debtPlanBorrow.setAddType(0);
				debtPlanBorrow.setDelFlag(0);
				debtPlanBorrow.setCreateTime(GetDate.getMyTimeInMillis());
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				debtPlanBorrow.setCreateUserId(Integer.parseInt(adminSystem.getId()));
				debtPlanBorrow.setCreateUserName(adminSystem.getUsername());
				this.debtPlanBorrowMapper.insertSelective(debtPlanBorrow);
			}
		}
	}

	/**
	 * 根据汇添金专属标号查询已关联的计划编号
	 * 
	 * @Title getDebtPlanNidListByBorrowNid
	 * @param borrowNid
	 * @return
	 */
	@Override
	public List<String> getDebtPlanNidListByBorrowNid(String borrowNid) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(borrowNid)) {
			param.put("borrowNid", borrowNid);
		}
		List<String> debtPlanNidList = this.planCustomizeMapper.getDebtPlanNidListByBorrowNid(param);
		return debtPlanNidList;
	}

	/**
	 * 根据计划编号,专属标号查询该计划是否已经关联资产
	 * 
	 * @Title getPlanIsSelected
	 * @param debtPlanNid
	 * @param borrowNid
	 * @return
	 */
	@Override
	public String getPlanIsSelected(String debtPlanNid, String borrowNid) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			param.put("debtPlanNid", debtPlanNid);
		}
		if (StringUtils.isNotEmpty(borrowNid)) {
			param.put("borrowNid", borrowNid);
		}
		String isSelected = this.planCustomizeMapper.getPlanIsSelected(param);
		return isSelected;
	}

	/**
	 * 获取关联资产总计
	 * 
	 * @Title countDebtPlanAmount
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> countDebtPlanAmount(Map<String, Object> param) {
		return this.planCustomizeMapper.countDebtPlanAmount(param);
	}

	/**
	 * 判断计划预编号是否重复
	 * 
	 * @Title isExistsPlanPreNidRecord
	 * @param request
	 * @return
	 */
	@Override
	public String isExistsPlanPreNidRecord(HttpServletRequest request) {

		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		String debtPlanPreNid = request.getParameter("param");
		message = message.replace("{label}", "计划预编号");
		String param = request.getParameter("param");
		if (StringUtils.isEmpty(param)) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		int debtPlanCount = this.isExistsPlanPreNid(debtPlanPreNid);
		// 计划名称重复
		if (debtPlanCount != 0) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, "计划预编码已经存在,请重新输入。");
			return ret.toString();
		}
		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
		return ret.toJSONString();
	}

	/**
	 * 计划预编号是否重复
	 * 
	 * @Title isExistsPlanPreNid
	 * @param debtPlanPreNid
	 * @return
	 */
	private int isExistsPlanPreNid(String debtPlanPreNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		cra.andDebtPrePlanNidEqualTo(Integer.parseInt(debtPlanPreNid));
		return this.debtPlanMapper.countByExample(example);
	}
}
