package com.hyjf.admin.manager.hjhplan.planlist;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhAllocationEngineExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.HjhPlanWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhRegion;
import com.hyjf.mybatis.model.auto.HjhRegionExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.PlanListCommonCustomize;
import com.alibaba.fastjson.JSONObject;

import cn.jpush.api.utils.StringUtils;

@Service
public class PlanListServiceImpl extends BaseServiceImpl implements PlanListService {
	
	/**
	 * 
	 * @method: countPlan
	 * @description: 计划数量查询
	 * @return: int
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	@Override
	public int countPlan(PlanListCommonCustomize planListCommonCustomize) {
		int ret = 0;
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidLike("%" + planListCommonCustomize.getPlanNidSrch() + "%");
		}
		// 传入查询计划名称
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNameSrch())) {
			cra.andPlanNameLike("%" + planListCommonCustomize.getPlanNameSrch() + "%");
		}
		// 传入锁定期
		if (StringUtils.isNotEmpty(planListCommonCustomize.getLockPeriodSrch())) {
			cra.andLockPeriodEqualTo(Integer.valueOf(planListCommonCustomize.getLockPeriodSrch()));
		}
		// 传入查询出借状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanStatusSrch())) {		
			cra.andPlanInvestStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanStatusSrch()));
		}
		// 传入查询显示状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanDisplayStatusSrch())) {		
//			cra.andPlanInvestStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanDisplayStatusSrch()));
			cra.andPlanDisplayStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanDisplayStatusSrch()));
		}
		// 传入查询添加时间
		if (StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeStart())&&StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeEnd())) {		
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			long start = 0;
			long end = 0;
			try {
				start = formatter.parse(planListCommonCustomize.getAddTimeStart()).getTime()/1000;
				end = formatter.parse(planListCommonCustomize.getAddTimeEnd()).getTime()/1000;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			criteria.andConfigAddTimeBetween((int)start, (int)end);
			cra.andAddTimeLessThanOrEqualTo((int)end+86399);
			cra.andAddTimeGreaterThanOrEqualTo((int)start);
		}
		
		// 传入还款方式 汇计划三期新增
		if (StringUtils.isNotEmpty(planListCommonCustomize.getBorrowStyleSrch())) {
			cra.andBorrowStyleEqualTo(planListCommonCustomize.getBorrowStyleSrch());
		}

		// 排序
		example.setOrderByClause("add_time Desc");
		ret = this.hjhPlanMapper.countByExample(example);
		return ret;
	}

	/**
	 * 
	 * @method: selectPlanList
	 * @description: 计划列表查询
	 * @return: List
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	@Override
	public List<HjhPlan> selectPlanList(PlanListCommonCustomize planListCommonCustomize) {
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidLike("%" + planListCommonCustomize.getPlanNidSrch() + "%");
		}
		// 传入查询计划名称
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNameSrch())) {
			cra.andPlanNameLike("%" + planListCommonCustomize.getPlanNameSrch() + "%");
		}
		// 传入锁定期
		if (StringUtils.isNotEmpty(planListCommonCustomize.getLockPeriodSrch())) {
			cra.andLockPeriodEqualTo(Integer.valueOf(planListCommonCustomize.getLockPeriodSrch()));
		}
		// 传入查询出借状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanStatusSrch())) {		
			cra.andPlanInvestStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanStatusSrch()));
		}
		// 传入查询显示状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanDisplayStatusSrch())) {		
			cra.andPlanDisplayStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanDisplayStatusSrch()));
		}
		
		// 传入查询添加时间
		if (StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeStart())&&StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeEnd())) {		
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			long start = 0;
			long end = 0;
			try {
				start = formatter.parse(planListCommonCustomize.getAddTimeStart()).getTime()/1000;
				end = formatter.parse(planListCommonCustomize.getAddTimeEnd()).getTime()/1000;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			criteria.andConfigAddTimeBetween((int)start, (int)end);
			cra.andAddTimeLessThanOrEqualTo((int)end+86399);
			cra.andAddTimeGreaterThanOrEqualTo((int)start);
		}
		
		// 传入还款方式 汇计划三期新增
		if (StringUtils.isNotEmpty(planListCommonCustomize.getBorrowStyleSrch())) {
			cra.andBorrowStyleEqualTo(planListCommonCustomize.getBorrowStyleSrch());
		}
		
		// 传入排序
		example.setOrderByClause("add_time Desc");
		// 传入分页
		if (planListCommonCustomize.getLimitStart() >= 0) {
			example.setLimitStart(planListCommonCustomize.getLimitStart());
			example.setLimitEnd(planListCommonCustomize.getLimitEnd());
		}
		List<HjhPlan> result = this.hjhPlanMapper.selectByExample(example);
		return result;
	}
	
	/**
	 * 根据计划编号查询数据是否存在
	 * 
	 * @Title isExistsRecord
	 * @param planNid
	 * @return
	 */
	@Override
	public boolean isExistsRecord(String planNid) {
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(planNid)) {
			cra.andPlanNidEqualTo(planNid);
		}
		List<HjhPlan> result = this.hjhPlanMapper.selectByExample(example);
		if (result != null && result.size() != 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取计划数据
	 * 
	 * @Title getPlanInfo
	 * @param planListBean
	 * @return
	 */
	@Override
	public PlanListBean getPlanInfo(PlanListBean planListBean) {
		HjhPlanWithBLOBs hjhPlanWithBLOBs = this.getHjhPlanWithBLOBs(planListBean.getDebtPlanNid());
		if (hjhPlanWithBLOBs != null) {
			this.getPlanCommonFiled(planListBean, hjhPlanWithBLOBs);
			planListBean.setMinInvestCounts(String.valueOf(hjhPlanWithBLOBs.getMinInvestCounts()));
		}
		return planListBean;
	}
	
	/**
	 * 获取计划信息
	 * 
	 * @Title getDebtPlanWithBLOBs
	 * @param planNid
	 * @return
	 */
	public HjhPlanWithBLOBs getHjhPlanWithBLOBs(String planNid) {
		if (StringUtils.isEmpty(planNid)) {
			return null;
		}
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		cra.andPlanNidEqualTo(planNid);
		List<HjhPlanWithBLOBs> result = this.hjhPlanMapper.selectByExampleWithBLOBs(example);
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
	public void getPlanCommonFiled(PlanListBean planListBean, HjhPlanWithBLOBs hjhPlanWithBLOBs) {
		// 计划编号
		planListBean.setDebtPlanNid(this.getValue(String.valueOf(hjhPlanWithBLOBs.getPlanNid())));
		// 计划名称
		planListBean.setDebtPlanName(this.getValue(String.valueOf(hjhPlanWithBLOBs.getPlanName())));
		// 还款方式
		planListBean.setBorrowStyle(this.getValue(String.valueOf(hjhPlanWithBLOBs.getBorrowStyle())));
		// 锁定期
		planListBean.setLockPeriod(this.getValue(String.valueOf(hjhPlanWithBLOBs.getLockPeriod())));
		// 锁定期天、月
		planListBean.setIsMonth(this.getValue(String.valueOf(hjhPlanWithBLOBs.getIsMonth())));
		// 预期出借利率率
		planListBean.setExpectApr(this.getValue(String.valueOf(hjhPlanWithBLOBs.getExpectApr())));
		// 最低加入金额
		planListBean.setDebtMinInvestment(this.getValue(String.valueOf(hjhPlanWithBLOBs.getMinInvestment())));
		// 最高加入金额
		planListBean.setDebtMaxInvestment(this.getValue(String.valueOf(hjhPlanWithBLOBs.getMaxInvestment())));
		// 出借增量
		planListBean.setDebtInvestmentIncrement(this.getValue(String.valueOf(hjhPlanWithBLOBs.getInvestmentIncrement())));
		// 可用券配置
		planListBean.setCouponConfig(this.getValue(String.valueOf(hjhPlanWithBLOBs.getCouponConfig())));
		// 出借状态
		planListBean.setDebtPlanStatus(this.getValue(String.valueOf(hjhPlanWithBLOBs.getPlanInvestStatus())));
		// 显示状态
		planListBean.setPlanDisplayStatusSrch(this.getValue(String.valueOf(hjhPlanWithBLOBs.getPlanDisplayStatus())));
		// 计划介绍
		planListBean.setPlanConcept(this.getValue(String.valueOf(hjhPlanWithBLOBs.getPlanConcept())));
		// 计划原理
		planListBean.setPlanPrinciple(this.getValue(String.valueOf(hjhPlanWithBLOBs.getPlanPrinciple())));
		// 风控保障措施
		planListBean.setSafeguardMeasures(this.getValue(String.valueOf(hjhPlanWithBLOBs.getSafeguardMeasures())));
		// 风险保证金措施
		planListBean.setMarginMeasures(this.getValue(String.valueOf(hjhPlanWithBLOBs.getMarginMeasures())));
		// 常见问题
		planListBean.setNormalQuestion(this.getValue(String.valueOf(hjhPlanWithBLOBs.getNormalQuestions())));
		//添加时间
		planListBean.setAddTime(this.getValue(String.valueOf(hjhPlanWithBLOBs.getAddTime())));
		// 智投服务风险测评等级
		planListBean.setInvestLevel(this.getValue(String.valueOf(hjhPlanWithBLOBs.getInvestLevel())));
	}
	private String getValue(String value) {
		if (StringUtils.isNotEmpty(value)) {
			return value;
		}
		return "";
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
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanName)) {
			cra.andPlanNameEqualTo(debtPlanName);
			cra.andDelFlgEqualTo(0);
		}
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			cra.andPlanNidNotEqualTo(debtPlanNid);
			cra.andDelFlgEqualTo(0);
		}
		ret = this.hjhPlanMapper.countByExample(example);
		return ret;
	}
	/**
	 * 根据计划编号获取计划数量
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param debtPlanTypeName
	 * @return
	 */
	public int isDebtPlanNExist( String debtPlanNid) {
		int ret = 0;
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			cra.andPlanNidEqualTo(debtPlanNid);
			cra.andDelFlgEqualTo(0);
		}
		ret = this.hjhPlanMapper.countByExample(example);
		return ret;
	}
	
	/**
	 * 
	 * 画面信息校验(入力+格式+大小)
	 * 
	 * @Title validatorFieldCheck
	 * @param mav
	 * @param PlanListBean
	 * @param isExistsRecord
	 */
	@Override
	public void validatorFieldCheck(ModelAndView mav, PlanListBean PlanListBean, boolean isExistsRecord) {
		// 计划名称
		if (!isExistsRecord) {
			String debtPlanName = PlanListBean.getDebtPlanName();
			int debtPlanNameCount = this.isDebtPlanNameExist(debtPlanName, "");
			if (debtPlanNameCount > 0) {
				ValidatorFieldCheckUtil.validateSpecialError(mav, "debtPlanName", "repeat");
			}
		}
		// 计划期限+还款方式
		if (!isExistsRecord) {
			String lockPeriod=PlanListBean.getLockPeriod();
//			String lockPeriodDay = PlanListBean.getDebtLockPeriodDay();
//			String lockPeriodMonth = PlanListBean.getDebtLockPeriodMonth();
			String isMonth=PlanListBean.getIsMonth();
			int lockPeriodCount=0;
			if(StringUtils.isEmpty(isMonth)){
				String borrowStyle = PlanListBean.getBorrowStyle();//(二期使用月时放开)
				lockPeriodCount = this.isLockPeriodExist(lockPeriod, borrowStyle,isMonth);
			}
			
			//String borrowStyle = PlanListBean.getBorrowStyle();(二期使用月时放开)
//			if(StringUtils.isEmpty(lockPeriodDay)){
//				lockPeriod = lockPeriodMonth;
//			}else{
//				lockPeriod = lockPeriodDay;
//			}
			//int lockPeriodCount = this.isLockPeriodExist(lockPeriod, borrowStyle);(二期使用月时用此查询)
			
			if (lockPeriodCount > 0) {
//				if(StringUtils.isEmpty(lockPeriodDay)){
//					ValidatorFieldCheckUtil.validateSpecialError(mav, "debtLockPeriodMonth", "repeat");
//				}else{
//					ValidatorFieldCheckUtil.validateSpecialError(mav, "debtLockPeriodDay", "repeat");
//				}
				if(StringUtils.isEmpty(lockPeriod)){
					ValidatorFieldCheckUtil.validateSpecialError(mav, "lockPeriod", "repeat");
				}
			}
		}
		// 还款方式
		ValidatorFieldCheckUtil.validateRequired(mav, "borrowStyle", PlanListBean.getBorrowStyle());
		// 预期出借利率
		ValidatorFieldCheckUtil.validateSignlessNumLength(mav, "expectApr", PlanListBean.getExpectApr(), 2, 2, true);
		// 最低加入金额(只验证数字格式)
		boolean debtMinInvestmentFlag =ValidatorFieldCheckUtil.validateDecimal(mav, "debtMinInvestment", PlanListBean.getDebtMinInvestment(), 10, true);
		// 最高加入金额(只验证数字格式)
//		boolean debtMaxInvestmentFlag = ValidatorFieldCheckUtil.validateDecimal(mav, "debtMaxInvestment", PlanListBean.getDebtMaxInvestment(), 10, true);
		// 出借增量
		ValidatorFieldCheckUtil.validateDecimal(mav, "debtInvestmentIncrement", PlanListBean.getDebtInvestmentIncrement(), 10, true);
		// 可用券配置
		ValidatorFieldCheckUtil.validateRequired(mav, "couponConfig", PlanListBean.getCouponConfig());
		// 出借状态
		ValidatorFieldCheckUtil.validateRequired(mav, "debtPlanStatus", PlanListBean.getDebtPlanStatus());
		// 计划介绍
		ValidatorFieldCheckUtil.validateRequired(mav, "planConcept", PlanListBean.getPlanConcept());
		// 常见问题
		ValidatorFieldCheckUtil.validateRequired(mav, "normalQuestion", PlanListBean.getNormalQuestion());
		
//		if (debtMinInvestmentFlag && StringUtils.isNotEmpty(PlanListBean.getDebtMinInvestment()) && StringUtils.isNotEmpty(PlanListBean.getDebtMaxInvestment())) {
//			// 最低加入金额
//			BigDecimal debtMinInvestment = new BigDecimal(PlanListBean.getDebtMinInvestment());
//			// 最高加入金额
//			BigDecimal debtMaxInvestment = new BigDecimal(PlanListBean.getDebtMaxInvestment());
//			// 最低加入金额≤最高加入金额
//			if (debtMinInvestment.compareTo(debtMaxInvestment) > 0) {
//				ValidatorFieldCheckUtil.validateSpecialError(mav, "debtMinInvestment", "debtMinInvestment.error");
//			}
//		}

		// 智投投资风险测评等级
		ValidatorFieldCheckUtil.validateRequired(mav, "investLevel", PlanListBean.getInvestLevel());
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
	 * 更新操作
	 * 
	 * @Title updateRecord
	 * @param planListBean
	 * @throws Exception
	 */
	@Override
	public void updateRecord(PlanListBean planListBean) throws Exception {
		// 原列表计划编号
		String debtPlanNid = planListBean.getDebtPlanNid();
		
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			HjhPlanExample example = new HjhPlanExample(); 
			HjhPlanExample.Criteria cra = example.createCriteria();
			cra.andPlanNidEqualTo(debtPlanNid);
			
			List<HjhPlanWithBLOBs> planList = this.hjhPlanMapper.selectByExampleWithBLOBs(example);
			if (planList != null && planList.size() > 0) {
				HjhPlanWithBLOBs plan = planList.get(0);
				// 计划编号
				plan.setPlanNid(StringUtils.isEmpty(planListBean.getDebtPlanNid()) ? "" : planListBean.getDebtPlanNid());
				// 计划名称
				plan.setPlanName(StringUtils.isEmpty(planListBean.getDebtPlanName()) ? "" : planListBean.getDebtPlanName());
				// 还款方式
				plan.setBorrowStyle(StringUtils.isEmpty(planListBean.getBorrowStyle()) ? "" : planListBean.getBorrowStyle());
				if(StringUtils.isEmpty(planListBean.getLockPeriod())){
					
				}else{
					// 锁定期
					plan.setLockPeriod(Integer.parseInt(planListBean.getLockPeriod()));
				}
				if(StringUtils.isEmpty(planListBean.getIsMonth())){
					
				}else{
					// 锁定期天、月
					plan.setIsMonth(Integer.parseInt(planListBean.getIsMonth()));
				}	
				// 预期出借利率率
				plan.setExpectApr(StringUtils.isEmpty(planListBean.getExpectApr()) ? BigDecimal.ZERO : new BigDecimal(planListBean.getExpectApr()));
				// 最低出借金额
				plan.setMinInvestment(new BigDecimal(planListBean.getDebtMinInvestment()));
				// 最高可出借金额
				plan.setMaxInvestment(StringUtils.isEmpty(planListBean.getDebtMaxInvestment()) ? BigDecimal.ZERO : new BigDecimal(planListBean.getDebtMaxInvestment()));
				// 出借增量
				plan.setInvestmentIncrement(new BigDecimal(planListBean.getDebtInvestmentIncrement()));
				// 可用券配置
				plan.setCouponConfig(planListBean.getCouponConfig());
				// 出借状态
				plan.setPlanInvestStatus(StringUtils.isEmpty(planListBean.getDebtPlanStatus()) ? 0 : Integer.parseInt(planListBean.getDebtPlanStatus()));
				// 显示状态
				plan.setPlanDisplayStatus(StringUtils.isEmpty(planListBean.getPlanDisplayStatusSrch()) ? 0 : Integer.parseInt(planListBean.getPlanDisplayStatusSrch()));
				// 计划介绍
				plan.setPlanConcept(StringUtils.isEmpty(planListBean.getPlanConcept()) ? "" : planListBean.getPlanConcept());
				// 计划原理
				plan.setPlanPrinciple(StringUtils.isEmpty(planListBean.getPlanPrinciple()) ? "" : planListBean.getPlanPrinciple());
				// 风控保障措施
				plan.setSafeguardMeasures(StringUtils.isEmpty(planListBean.getSafeguardMeasures()) ? "" : planListBean.getSafeguardMeasures());
				// 风险保证金措施
				plan.setMarginMeasures(StringUtils.isEmpty(planListBean.getMarginMeasures()) ? "" : planListBean.getMarginMeasures());
				// 常见问题
				plan.setNormalQuestions(StringUtils.isEmpty(planListBean.getNormalQuestion()) ? "" : planListBean.getNormalQuestion());
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				// 更新时间
				plan.setUpdateTime(GetDate.getMyTimeInMillis());
				// 更新用户ID
				plan.setUpdateUser(Integer.parseInt(adminSystem.getId()));
				
				//更新最小自动投标笔数
				plan.setMinInvestCounts(Integer.valueOf(planListBean.getMinInvestCounts()));
				// 智投服务风险测评等级
				plan.setInvestLevel(planListBean.getInvestLevel());
				// 数据更新
				int success = this.hjhPlanMapper.updateByPrimaryKeySelective(plan);
				
				if(success > 0){
					//计划列表更新成功才更新引擎表
					HjhAllocationEngineExample example1 = new HjhAllocationEngineExample(); 
					HjhAllocationEngineExample.Criteria cra1 = example1.createCriteria();
					cra1.andPlanNidEqualTo(debtPlanNid);
					List<HjhAllocationEngine> resultList = this.hjhAllocationEngineMapper.selectByExample(example1);
					for(HjhAllocationEngine hjhAllocationEngine : resultList){
						hjhAllocationEngine.setPlanName(planListBean.getDebtPlanName());
						this.hjhAllocationEngineMapper.updateByPrimaryKeySelective(hjhAllocationEngine);
					}
					
					//计划列表更新成功才更新计划专区表
					HjhRegionExample example2 = new HjhRegionExample();
					HjhRegionExample.Criteria cra2 = example2.createCriteria();
					cra2.andPlanNidEqualTo(debtPlanNid);
					List<HjhRegion> resultList2 = this.hjhRegionMapper.selectByExample(example2);
					for(HjhRegion hjhRegion : resultList2){
						hjhRegion.setPlanName(planListBean.getDebtPlanName());
						this.hjhRegionMapper.updateByPrimaryKeySelective(hjhRegion);
					}
				}
			}
		}
	}

	/**
	 * 插入操作
	 * 
	 * @Title insertRecord
	 * @param planListBean
	 * @throws Exception
	 */
	@Override
	public void insertRecord(PlanListBean planListBean) throws Exception {
		HjhPlanWithBLOBs plan = new HjhPlanWithBLOBs();
		// 计划编号
		plan.setPlanNid(planListBean.getDebtPlanNid());
		// 计划名称
		plan.setPlanName(planListBean.getDebtPlanName());
		if(StringUtils.isEmpty(planListBean.getLockPeriod())){
			
		}else{
			// 锁定期
			plan.setLockPeriod(Integer.parseInt(planListBean.getLockPeriod()));
		}
		if(StringUtils.isEmpty(planListBean.getIsMonth())){
			
		}else{
			// 锁定期天、月
			plan.setIsMonth(Integer.parseInt(planListBean.getIsMonth()));
		}
		// 预期出借利率率
		plan.setExpectApr(new BigDecimal(planListBean.getExpectApr()));
		// 最低出借金额
		plan.setMinInvestment(new BigDecimal(planListBean.getDebtMinInvestment()));
		// 最高可出借金额(非必须入力)
		plan.setMaxInvestment(StringUtils.isEmpty(planListBean.getDebtMaxInvestment()) ? BigDecimal.ZERO : new BigDecimal(planListBean.getDebtMaxInvestment()));
		// 出借增量
		plan.setInvestmentIncrement(new BigDecimal(planListBean.getDebtInvestmentIncrement()));
		// 计划可投金额
		plan.setAvailableInvestAccount(BigDecimal.ZERO);
		// 待还总额
		plan.setRepayWaitAll(BigDecimal.ZERO);
		// 出借状态
		plan.setPlanInvestStatus(Integer.valueOf(planListBean.getDebtPlanStatus()));
		// 显示状态
		plan.setPlanDisplayStatus(Integer.valueOf(planListBean.getPlanDisplayStatusSrch()));
		// 添加时间
		plan.setAddTime(GetDate.getMyTimeInMillis());
		// 还款方式
		plan.setBorrowStyle(planListBean.getBorrowStyle());
		// 是否可用券
		plan.setCouponConfig(planListBean.getCouponConfig());
		// 计划介绍
		plan.setPlanConcept(planListBean.getPlanConcept());
		// 最小自动投标笔数
		plan.setMinInvestCounts(Integer.valueOf(planListBean.getMinInvestCounts()));
		// 计划原理
		plan.setPlanPrinciple("计划原理");
		// 风控保障措施
		plan.setSafeguardMeasures("风控保障措施");
		// 风险保证金措施
		plan.setMarginMeasures("风险保证金措施");
		// 常见问题
		plan.setNormalQuestions(planListBean.getNormalQuestion());
		// 累积加入总额
		plan.setJoinTotal(BigDecimal.ZERO);
		// 待还本金
		plan.setPlanWaitCaptical(BigDecimal.ZERO);
		// 待还利息
		plan.setPlanWaitInterest(BigDecimal.ZERO);
		// 已还总额
		plan.setRepayTotal(BigDecimal.ZERO);
		// 已还利息
		plan.setPlanRepayInterest(BigDecimal.ZERO);
		// 已还本金
		plan.setPlanRepayCapital(BigDecimal.ZERO);
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		// 创建人id
		plan.setCreateUser(Integer.parseInt(adminSystem.getId()));
		// 发起时间
		plan.setCreateTime(GetDate.getMyTimeInMillis());
		// 修改人ID
		plan.setUpdateUser(null);
		// 修改时间
		plan.setUpdateTime(null);
		// 删除标识
		plan.setDelFlg(0);
		// 智投服务风险测评等级
		plan.setInvestLevel(planListBean.getInvestLevel());
		this.hjhPlanMapper.insertSelective(plan);
	}
	
	/**
	 * 更新启用/关闭状态
	 * 
	 * @Title updatePlanRecord
	 * @param planListBean
	 * @throws Exception
	 */
	@Override
	public void updatePlanRecord(PlanListBean planListBean) throws Exception {
		String debtPlanNid = planListBean.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			HjhPlanExample example = new HjhPlanExample(); 
			HjhPlanExample.Criteria cra = example.createCriteria();
			cra.andPlanNidEqualTo(debtPlanNid);
			
			List<HjhPlanWithBLOBs> planList = this.hjhPlanMapper.selectByExampleWithBLOBs(example);
			if (planList != null && planList.size() > 0) {
				HjhPlanWithBLOBs plan = planList.get(0);

				if (planListBean.getEnableOrDisplayFlag()==1){
					if (plan.getPlanInvestStatus() == 1) {
						plan.setPlanInvestStatus(2);
					} else {
						plan.setPlanInvestStatus(1);
					}
				}else if(planListBean.getEnableOrDisplayFlag()==2){
					if (plan.getPlanDisplayStatus() == 1) {
						plan.setPlanDisplayStatus(2);
					} else {
						plan.setPlanDisplayStatus(1);
					}

				}
				// 数据更新
				this.hjhPlanMapper.updateByPrimaryKeySelective(plan);
			}
		}
	}
	/**
	 * 更新显示/隐藏状态
	 * 
	 * @Title updatePlanRecord
	 * @param planListBean
	 * @throws Exception
	 */
	@Override
	public void updatePlanDisplayRecord(PlanListBean planListBean) throws Exception {
		String debtPlanNid = planListBean.getDebtPlanNid();
		if (StringUtils.isNotEmpty(debtPlanNid)) {
			HjhPlanExample example = new HjhPlanExample(); 
			HjhPlanExample.Criteria cra = example.createCriteria();
			cra.andPlanNidEqualTo(debtPlanNid);
			
			List<HjhPlanWithBLOBs> planList = this.hjhPlanMapper.selectByExampleWithBLOBs(example);
			if (planList != null && planList.size() > 0) {
				HjhPlanWithBLOBs plan = planList.get(0);
				if (plan.getPlanDisplayStatus() == 1) {
					plan.setPlanDisplayStatus(2);
				} else {
					plan.setPlanDisplayStatus(1);
				}
				// 数据更新
				this.hjhPlanMapper.updateByPrimaryKeySelective(plan);
			}
		}
	}
	
	/**
	 * 根据计划名称获取计划数量
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param debtPlanTypeName
	 * @return
	 */
	public int isLockPeriodExist(String lockPeriod) {
		int ret = 0;
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(lockPeriod)) {
			cra.andLockPeriodEqualTo(Integer.valueOf(lockPeriod));
//			cra.andBorrowStyleEqualTo(borrowStyle);//(二期使用月时放开)
		}
		ret = this.hjhPlanMapper.countByExample(example);
		return ret;
	}
	/**
	 * 根据计划还款方式，锁定期，锁定期类型获取计划数量(月)
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param debtPlanTypeName
	 * @return
	 */
	public int isLockPeriodExist(String lockPeriod,String borrowStyle,String isMonth) {
		int ret = 0;
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(lockPeriod)) {
			cra.andLockPeriodEqualTo(Integer.valueOf(lockPeriod));
			cra.andIsMonthEqualTo(Integer.valueOf(isMonth));
			cra.andBorrowStyleEqualTo(borrowStyle);//(二期使用月时放开) 还款方式
		}
		ret = this.hjhPlanMapper.countByExample(example);
		return ret;
	}
	/**
	 * 判断计划编号是否重复
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param request
	 * @return
	 */
	@Override
	public String isDebtPlanNidExist(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		message = message.replace("{label}", "智投编号");
		String debtPlanNid = request.getParameter("param");
		int debtPlanCount = this.isDebtPlanNExist(debtPlanNid);
		// 计划编号重复
		if (debtPlanCount != 0) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, "智投编号不能重复！");
			return ret.toString();
		}
		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
		return ret.toJSONString();
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param planListCommonCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public String sumHjhPlan(PlanListCommonCustomize planListCommonCustomize) {
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidLike("%" + planListCommonCustomize.getPlanNidSrch() + "%");
		}
		// 传入查询计划名称
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNameSrch())) {
			cra.andPlanNameLike("%" + planListCommonCustomize.getPlanNameSrch() + "%");
		}
		// 传入锁定期
		if (StringUtils.isNotEmpty(planListCommonCustomize.getLockPeriodSrch())) {
			cra.andLockPeriodEqualTo(Integer.valueOf(planListCommonCustomize.getLockPeriodSrch()));
		}
		// 传入查询出借状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanStatusSrch())) {		
			cra.andPlanInvestStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanStatusSrch()));
		}
		// 传入查询显示状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanDisplayStatusSrch())) {		
			cra.andPlanDisplayStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanDisplayStatusSrch()));
		}
		// 传入还款方式 汇计划三期新增
		if (StringUtils.isNotEmpty(planListCommonCustomize.getBorrowStyleSrch())) {
			cra.andBorrowStyleEqualTo(planListCommonCustomize.getBorrowStyleSrch());
		}
		// 传入查询添加时间
		if (StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeStart())&&StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeEnd())) {		
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			long start = 0;
			long end = 0;
			try {
				start = formatter.parse(planListCommonCustomize.getAddTimeStart()).getTime()/1000;
				end = formatter.parse(planListCommonCustomize.getAddTimeEnd()).getTime()/1000;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cra.andAddTimeLessThanOrEqualTo((int)end+86399);
			cra.andAddTimeGreaterThanOrEqualTo((int)start);
		}
		
		String result = this.hjhPlanListCustomizeMapper.sumHjhPlan(example);
		return result;
	}

	/**
	 * 查询开放额度累计
	 * @param planListCommonCustomize
	 * @return
	 * @author LIBIN
	 */
	@Override
	public String sumOpenAccount(PlanListCommonCustomize planListCommonCustomize) {
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidLike("%" + planListCommonCustomize.getPlanNidSrch() + "%");
		}
		// 传入查询计划名称
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNameSrch())) {
			cra.andPlanNameLike("%" + planListCommonCustomize.getPlanNameSrch() + "%");
		}
		// 传入锁定期
		if (StringUtils.isNotEmpty(planListCommonCustomize.getLockPeriodSrch())) {
			cra.andLockPeriodEqualTo(Integer.valueOf(planListCommonCustomize.getLockPeriodSrch()));
		}
		// 传入查询出借状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanStatusSrch())) {		
			cra.andPlanInvestStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanStatusSrch()));
		}
		// 传入查询显示状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanDisplayStatusSrch())) {		
			cra.andPlanDisplayStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanDisplayStatusSrch()));
		}
		// 传入还款方式 汇计划三期新增
		if (StringUtils.isNotEmpty(planListCommonCustomize.getBorrowStyleSrch())) {
			cra.andBorrowStyleEqualTo(planListCommonCustomize.getBorrowStyleSrch());
		}
		// 传入查询添加时间
		if (StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeStart())&&StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeEnd())) {		
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			long start = 0;
			long end = 0;
			try {
				start = formatter.parse(planListCommonCustomize.getAddTimeStart()).getTime()/1000;
				end = formatter.parse(planListCommonCustomize.getAddTimeEnd()).getTime()/1000;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cra.andAddTimeLessThanOrEqualTo((int)end+86399);
			cra.andAddTimeGreaterThanOrEqualTo((int)start);
		}
		String result = this.hjhPlanListCustomizeMapper.sumOpenAccount(example);
		return result;

	}

	@Override
	public String sumJoinTotal(PlanListCommonCustomize planListCommonCustomize) {
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidLike("%" + planListCommonCustomize.getPlanNidSrch() + "%");
		}
		// 传入查询计划名称
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanNameSrch())) {
			cra.andPlanNameLike("%" + planListCommonCustomize.getPlanNameSrch() + "%");
		}
		// 传入锁定期
		if (StringUtils.isNotEmpty(planListCommonCustomize.getLockPeriodSrch())) {
			cra.andLockPeriodEqualTo(Integer.valueOf(planListCommonCustomize.getLockPeriodSrch()));
		}
		// 传入查询出借状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanStatusSrch())) {		
			cra.andPlanInvestStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanStatusSrch()));
		}
		// 传入查询显示状态
		if (StringUtils.isNotEmpty(planListCommonCustomize.getPlanDisplayStatusSrch())) {		
			cra.andPlanDisplayStatusEqualTo(Integer.valueOf(planListCommonCustomize.getPlanDisplayStatusSrch()));
		}
		// 传入还款方式 汇计划三期新增
		if (StringUtils.isNotEmpty(planListCommonCustomize.getBorrowStyleSrch())) {
			cra.andBorrowStyleEqualTo(planListCommonCustomize.getBorrowStyleSrch());
		}
		// 传入查询添加时间
		if (StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeStart())&&StringUtils.isNotEmpty(planListCommonCustomize.getAddTimeEnd())) {		
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			long start = 0;
			long end = 0;
			try {
				start = formatter.parse(planListCommonCustomize.getAddTimeStart()).getTime()/1000;
				end = formatter.parse(planListCommonCustomize.getAddTimeEnd()).getTime()/1000;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cra.andAddTimeLessThanOrEqualTo((int)end+86399);
			cra.andAddTimeGreaterThanOrEqualTo((int)start);
		}
		String result = this.hjhPlanListCustomizeMapper.sumJoinTotal(example);
		return result;
	}
}
