/**
 * Description:汇直投查询service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.app.project;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.app.hjhplan.HjhPlanService;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultExampleCustomize;
import com.hyjf.mybatis.model.customize.app.*;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {
	private Logger _log = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	private HjhPlanService hjhPlanService;
	/**
	 * 查询指定项目类型的项目列表
	 */
	@Override
	@Cached(name="appHomeHZTCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public List<AppProjectListCustomize> searchProjectList(Map<String, Object> params) {
		List<AppProjectListCustomize> list = appProjectListCustomizeMapper.selectProjectList(params);
		return list;
	}

	/**
	 * 查询指定项目类型的项目数目
	 */
	@Override
	@Cached(name="appHomeHZTCountCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public int countProjectListRecordTotal(Map<String, Object> params) {
		int total = appProjectListCustomizeMapper.countProjectListRecordTotal(params);
		return total;
	}

	/**
	 * 查询个人借款项目的项目详情
	 */
	@Override
	public AppProjectPersonDetailCustomize searchProjectPersonDetail(String borrowNid) {
		AppProjectPersonDetailCustomize htlDetail = appProjectListCustomizeMapper.selectProjectPersonDetail(borrowNid);
		return htlDetail;
	}

	/**
	 * 查询企业项目的项目详情
	 */
	@Override
	public AppProjectCompanyDetailCustomize searchProjectCompanyDetail(String borrowNid) {
		AppProjectCompanyDetailCustomize htlDetail = appProjectListCustomizeMapper
				.selectProjectCompanyDetail(borrowNid);
		return htlDetail;
	}

	/**
	 * 查询指定项目的项目出借详情
	 */
	@Override
	public List<AppProjectInvestListCustomize> searchProjectInvestList(Map<String, Object> params) {
		List<AppProjectInvestListCustomize> list = appProjectListCustomizeMapper.selectProjectInvestList(params);
		return list;
	}

	/**
	 * 统计指定项目的项目出借总数
	 */
	@Override
	public int countProjectInvestRecordTotal(Map<String, Object> params) {
		int hztInvestTotal = appProjectListCustomizeMapper.countProjectInvestRecordTotal(params);
		return hztInvestTotal;
	}

	/**
	 * 查询汇消费项目的打包信息列表
	 */
	@Override
	public List<AppProjectConsumeCustomize> searchProjectConsumeList(Map<String, Object> params) {
		List<AppProjectConsumeCustomize> list = appProjectListCustomizeMapper.selectProjectConsumeList(params);
		return list;
	}

	/**
	 * 统计汇消费项目的打包信息列表总数
	 */
	@Override
	public int countProjectConsumeRecordTotal(Map<String, Object> params) {
		int total = appProjectListCustomizeMapper.countProjectConsumeListRecordTotal(params);
		return total;
	}

	/**
	 * 根据borrowNid获取借款项目
	 * 
	 * @param borrowNid
	 * @return
	 */
	@Override
	public AppProjectDetailCustomize selectProjectDetail(String borrowNid) {
		AppProjectDetailCustomize borrow = appProjectListCustomizeMapper.selectProjectDetail(borrowNid);
		return borrow;
	}

	/**
	 * 查询相应的汇资产信息
	 */
	@Override
	public AppHzcProjectDetailCustomize searchHzcProjectDetail(String borrowNid) {
		AppHzcProjectDetailCustomize hzcInfo = appProjectListCustomizeMapper.searchHzcProjectDetail(borrowNid);
		return hzcInfo;
	}

	/**
	 * 查询认证信息
	 */
	@Override
	public List<AppProjectAuthenInfoCustomize> searchProjectAuthenInfos(String borrowNid) {
		List<AppProjectAuthenInfoCustomize> authenInfoList = appProjectListCustomizeMapper
				.searchProjectAuthenInfos(borrowNid);
		return authenInfoList;
	}

	@Override
	public AppHzcDisposalPlanCustomize searchDisposalPlan(String borrowNid) {
		AppHzcDisposalPlanCustomize disposalPlan = appProjectListCustomizeMapper.searchDisposalPlan(borrowNid);
		return disposalPlan;
	}

	@Override
	public AppRiskControlCustomize selectRiskControl(String borrowNid) {
		AppRiskControlCustomize riskControl = appProjectListCustomizeMapper.selectRiskControl(borrowNid);
		return riskControl;
	}

	@Override
	public List<AppMortgageCustomize> selectMortgageList(String borrowNid) {
		List<AppMortgageCustomize> mortgageList = appProjectListCustomizeMapper.selectMortgageList(borrowNid);
		return mortgageList;
	}

	@Override
	public List<AppVehiclePledgeCustomize> selectVehiclePledgeList(String borrowNid) {
		List<AppVehiclePledgeCustomize> vehiclePledgeList = appProjectListCustomizeMapper
				.selectVehiclePledgeList(borrowNid);
		return vehiclePledgeList;
	}
	
	/**
	 * 
	 * 查询标的车辆信息
	 * @author hsy
	 * @param borrowNid
	 * @return
	 */
	@Override
    public List<BorrowCarinfo> selectBorrowCarInfo(String borrowNid){
	    if(StringUtils.isEmpty(borrowNid)){
	        return new ArrayList<BorrowCarinfo>();
	    }
	    
	    BorrowCarinfoExample example = new BorrowCarinfoExample();
	    example.createCriteria().andBorrowNidEqualTo(borrowNid);
	    return borrowCarinfoMapper.selectByExample(example);
	}

	@Override
	public List<AppProjectConsumeCustomize> selectProjectConsumeList(Map<String, Object> params) {
		List<AppProjectConsumeCustomize> consumeList = appProjectListCustomizeMapper.selectProjectConsumeList(params);
		return consumeList;
	}

	@Override
	public int countProjectConsumeListRecordTotal(Map<String, Object> params) {
		int total = appProjectListCustomizeMapper.countProjectConsumeListRecordTotal(params);
		return total;
	}

	@Override
	public List<ProjectFileBean> searchProjectFiles(String borrowNid, String url) {
		String files = appProjectListCustomizeMapper.searchProjectFiles(borrowNid);
		List<ProjectFileBean> fileList = new ArrayList<ProjectFileBean>();
		if (StringUtils.isNotEmpty(files)) {
			JSONArray jsonFiles = JSONArray.parseArray(files);
			for (int i = 0; i < jsonFiles.size(); i++) {
				JSONObject urlFile = jsonFiles.getJSONObject(i);
				JSONArray urls = urlFile.getJSONArray("data");
				for (int j = 0; j < urls.size(); j++) {
					JSONObject file = urls.getJSONObject(j);
					ProjectFileBean fileBean = new ProjectFileBean();
					fileBean.setFileName(file.getString("name"));
					fileBean.setFileUrl(url + file.getString("fileurl"));
					if (StringUtils.isNotEmpty(file.getString("imageSort"))) {
						fileBean.setSort(file.getString("imageSort"));
					} else {
						fileBean.setSort(String.valueOf(j));
					}
					fileList.add(fileBean);
				}
			}
		}
		if (fileList != null && fileList.size() > 1) {
			Collections.sort(fileList, new Comparator<ProjectFileBean>() {
				public int compare(ProjectFileBean arg0, ProjectFileBean arg1) {
					return arg0.getSort().compareTo(arg1.getSort());
				}
			});
		}
		return fileList;
	}

	/**
	 * 计算还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	@Override
	public List<RepayPlanBean> getRepayPlan(String borrowNid) {
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		Borrow borrow = this.getBorrowByNid(borrowNid);
		String borrowStyle = borrow.getBorrowStyle();
		Integer projectType = borrow.getProjectType();
		BigDecimal yearRate = borrow.getBorrowApr();
		Integer totalMonth = borrow.getBorrowPeriod();
		BigDecimal account = borrow.getAccount();
		Integer time = borrow.getBorrowSuccessTime();
		if (time == null) {
			time = (int) (System.currentTimeMillis() / 1000);
		}
		List<RepayPlanBean> repayPlans = new ArrayList<RepayPlanBean>();
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(
				borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getBorrowManagerScaleEnd());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 按月计息到期还本还息和按天计息，到期还本还息
		if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,
					borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);
			if (info != null) {
				String repayTime = "-";
				// 通过复审
				if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
					repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
				}
				RepayPlanBean planIntrest = new RepayPlanBean(repayTime, df.format(info.getRepayAccountInterest()),
						"利息");
				RepayPlanBean plan = new RepayPlanBean(repayTime, df.format(info.getRepayAccountCapital()), "本金");
				RepayPlanBean planAccount = new RepayPlanBean(repayTime, df.format(info.getRepayAccountCapital().add(info.getRepayAccountInterest())), "本息");
				if(borrow.getIsNew() == 0){
				    repayPlans.add(planIntrest);
				    repayPlans.add(plan);
				}else if(borrow.getIsNew() == 1){
				    repayPlans.add(planAccount);
				}
			}
		} else {// 等额本息和等额本金和先息后本
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,
					borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);
			if (info.getListMonthly() != null) {
				String repayTime = "-";
				for (int i = 0; i < info.getListMonthly().size(); i++) {
					InterestInfo sub = info.getListMonthly().get(i);
					// 通过复审
					if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
						repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
					}
					String repayType = "本息";
					if (i < info.getListMonthly().size() - 1 && borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						repayType = "利息";
					}
					RepayPlanBean plan = new RepayPlanBean(repayTime, df.format(sub.getRepayAccount()), repayType);
					repayPlans.add(plan);
				}
			}
		}
		return repayPlans;
	}

	@Override
	public Users searchLoginUser(Integer userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		return user;
	}

	// add APP1.3.5改版 未登录用户或项目状态为还款中且未出借过项目的用户不能看到相关文件 by zhangjp start
	/**
	 * 校验当前用户是否出借过指定项目
	 * 
	 * @param userId
	 * @param borrowNid
	 * @return true 已出借，false 未出借
	 */
	@Override
	public boolean checkTenderByUser(Integer userId, String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andBorrowNidEqualTo(borrowNid);
		int count = this.borrowTenderMapper.countByExample(example);
		return count > 0 ? true : false;
	}

	// add APP1.3.5改版 未登录用户或项目状态为还款中且未出借过项目的用户不能看到相关文件 by zhangjp end

	/**
	 * 汇计划列表查询 - 按照类型查询
	 */
	@Override
	@Cached(name="appHomeHJHCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public List<HjhPlanCustomize> searchHjhPlanList(Map<String, Object> params) {
		List<HjhPlanCustomize> hjhPlanList = this.hjhPlanCustomizeMapper.selectAppHjhPlanList(params);
		return hjhPlanList;
	}

	@Override
	@Cached(name="appHomeHJHCountCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 600, timeUnit = TimeUnit.SECONDS)
	public Integer countHjhPlan(Map<String, Object> params) {
		return hjhPlanCustomizeMapper.countHjhPlanList(params);
	}

	/**
	 * 定时发标剩余时间
	 * @param borrowNid
	 * @return
	 */
	@Override
	public String getTimeLeft(String borrowNid) {
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria criteria = borrowExample.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		return String.valueOf(borrowMapper.selectByExample(borrowExample).get(0).getOntime());
	}

	/**
	 * 项目可投金额
	 * @param borrowNid
	 * @return
	 */
	@Override
	public String getBorrowAccountWait(String borrowNid) {
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria criteria = borrowExample.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrows = borrowMapper.selectByExample(borrowExample);
		if (borrows.size()>0 && borrows!= null){
			return String.valueOf(borrows.get(0).getBorrowAccountWait());
		}
		return null;
	}

	/**
	 * 查询项目类型
	 * @param borrowType
	 * @return
	 */
	@Override
	public String getProjectType(String borrowType) {
		BorrowProjectTypeExample typeExample = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria criteria = typeExample.createCriteria();
		criteria.andBorrowProjectTypeEqualTo(borrowType);
		return borrowProjectTypeMapper.selectByExample(typeExample).get(0).getBorrowName();
	}


	/**
	 * 查询待收金额
	 * @param params
	 * @return
	 */
	@Override
	public String getMyProjectWaitAmountTotal(Map<String, Object> params) {
		Object userId = params.get("userId");
		BorrowTenderExample tenderExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = tenderExample.createCriteria();
		criteria.andUserIdEqualTo((Integer) userId);
		List<BorrowTender> borrowTenders = borrowTenderMapper.selectByExample(tenderExample);
		BigDecimal waitMoney = BigDecimal.ZERO;
		for (BorrowTender borrowTender : borrowTenders){
			BigDecimal money = borrowTender.getRecoverAccountWait();
			waitMoney.add(money);
		}
		return String.valueOf(waitMoney);
	}

	/**
	 * 查询我的债权总数
	 * @param params
	 * @return
	 */
	@Override
	public Integer countMyProjectList(Map<String, Object> params) {
		Object userId = params.get("userId");
		BorrowTenderExample borrowExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = borrowExample.createCriteria();
		criteria.andUserIdEqualTo((Integer) userId);
		String type = (String) params.get("type");
		criteria.andInvestTypeEqualTo(Integer.valueOf(type));
		return borrowTenderMapper.countByExample(borrowExample);
	}


	/**
	 * 获取我的转让记录
	 * @param params
	 * @return
	 */
	@Override
	public List<BorrowCredit> selectMyCreditRecord(Map<String, Object> params) {
		Integer userId = (Integer) params.get("userId");
		BorrowCreditExample creditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria criteria = creditExample.createCriteria();
		criteria.andCreditUserIdEqualTo(userId);
		List<BorrowCredit> borrowCredits = borrowCreditMapper.selectByExample(creditExample);
		return borrowCredits;
	}
	
	@Override
	public UserEvalationResultCustomize getUserEvalationResult(Integer userId) {
		UserEvalationResultCustomize userEvalationResultCustomize = null;
		UserEvalationResultExampleCustomize creditExample = new UserEvalationResultExampleCustomize();
		UserEvalationResultExampleCustomize.Criteria criteria = creditExample.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<UserEvalationResultCustomize> list = userEvalationResultCustomizeMapper.selectByExample(creditExample);
		
		if(list != null && list.size() > 0){
			userEvalationResultCustomize = list.get(0);
		}
		return userEvalationResultCustomize;
	}

	@Override
	public BorrowRepay findByBorrowNid(String borrowNid) {
		BorrowRepay borrowRepay = null;
		BorrowRepayExample repayExample = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = repayExample.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = borrowRepayMapper.selectByExample(repayExample);
		
		if(list != null && list.size() > 0){
			borrowRepay = list.get(0);
		}

		if (borrowRepay != null) {
			Borrow borrow = this.getBorrowByNid(borrowNid);
			String borrowStyle = borrow.getBorrowStyle();
			Integer projectType = borrow.getProjectType();
			BigDecimal yearRate = borrow.getBorrowApr();
			Integer totalMonth = borrow.getBorrowPeriod();
			BigDecimal account = borrow.getAccount();
			Integer time = borrow.getBorrowSuccessTime();
			if (time == null) {
				time = (int) (System.currentTimeMillis() / 1000);
			}
			List<RepayPlanBean> repayPlans = new ArrayList<RepayPlanBean>();
			// 差异费率
			BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
			//初审时间
			int borrowVerifyTime =Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
			// 月利率(算出管理费用[上限])
			BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO: new BigDecimal(borrow.getManageFeeRate());
			// 月利率(算出管理费用[下限])
			BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO: new BigDecimal(borrow.getBorrowManagerScaleEnd());
			// 按月计息到期还本还息和按天计息，到期还本还息
			if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
				InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
				if (info != null) {
					BigDecimal repayAccountInterest = info.getRepayAccountInterest();
					BigDecimal repayAccountCapital = info.getRepayAccountCapital();
					if (repayAccountInterest != null && repayAccountCapital != null && borrowRepay != null) {
						borrowRepay.setRepayAccount(repayAccountInterest.add(repayAccountCapital));
					}
				}
			} else {// 等额本息和等额本金和先息后本
				InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,
						borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
				if (info.getListMonthly() != null) {
					String repayTime = "-";
					for (int i = 0; i < info.getListMonthly().size(); i++) {
						InterestInfo sub = info.getListMonthly().get(i);
						borrowRepay.setRepayAccount(sub.getRepayAccount());
					}
				}
			}
			return borrowRepay;
		} else {
			return borrowRepay;
		}
	}

	@Override
	public List<BorrowRepayPlan> findRepayPlanByBorrowNid(String borrowNid) {
	/*	BorrowRepayPlanExample repayExample = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria criteria = repayExample.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
	return borrowRepayPlanMapper.selectByExample(repayExample);*/
		Borrow borrow = this.getBorrowByNid(borrowNid);
		String borrowStyle = borrow.getBorrowStyle();
		Integer projectType = borrow.getProjectType();
		BigDecimal yearRate = borrow.getBorrowApr();
		Integer totalMonth = borrow.getBorrowPeriod();
		BigDecimal account = borrow.getAccount();
		Integer time = borrow.getBorrowSuccessTime();
		if (time == null) {
			time = (int) (System.currentTimeMillis() / 1000);
		}
		List<BorrowRepayPlan> repayPlans = new ArrayList<>();
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		//初审时间
		int borrowVerifyTime =Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO: new BigDecimal(borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO: new BigDecimal(borrow.getBorrowManagerScaleEnd());
		// 按月计息到期还本还息和按天计息，到期还本还息
		if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
			if (info != null) {
				String repayTime = "-";
				// 通过复审
				if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
					repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
				}
				BorrowRepayPlan planIntrest = new BorrowRepayPlan();
				planIntrest.setRepayTime(repayTime);
				planIntrest.setRepayAccount(info.getRepayAccount());
				repayPlans.add(planIntrest);
			}
		} else {// 等额本息和等额本金和先息后本
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,
					borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
			if (info.getListMonthly() != null) {
				String repayTime = "-";
				for (int i = 0; i < info.getListMonthly().size(); i++) {
					InterestInfo sub = info.getListMonthly().get(i);
					// 通过复审
					if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
						repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
					}
					String repayType = "本息";
					if (i < info.getListMonthly().size() - 1 && borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						repayType = "利息";
					}
					BorrowRepayPlan planIntrest = new BorrowRepayPlan();
					planIntrest.setRepayTime(repayTime);
					planIntrest.setRepayAccount(sub.getRepayAccount());
					repayPlans.add(planIntrest);
				}
			}
		}
		return repayPlans;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @return
	 * @author lm
	 */
		
	@Override
	public BorrowUsers getBorrowUsersByNid(String borrowNid) {
		if(StringUtils.isBlank(borrowNid)){
			return null;
		}
		BorrowUsersExample example = new BorrowUsersExample();
		BorrowUsersExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowUsers> list = this.borrowUsersMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			// add by liushouyi nifa2 20181206 start
			// 处理企业注册时间（数据库存放格式YYYY-MM-DD）
			if (StringUtils.isNotBlank(list.get(0).getComRegTime()) && list.get(0).getComRegTime().length() >= 4){
				String comRegTime = list.get(0).getComRegTime().substring(0,4).concat("年");
				list.get(0).setComRegTime(comRegTime);
			}
			// add by liushouyi nifa2 20181206 end
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public BorrowManinfo getBorrowManinfoByNid(String borrowNid) {
		if(StringUtils.isBlank(borrowNid)){
			return null;
		}
		BorrowManinfoExample example = new BorrowManinfoExample();
		BorrowManinfoExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		// upd by liushouyi nifa 20181206 start
//      	List<BorrowManinfo> list = this.borrowManinfoMapper.selectByExample(example);
		List<BorrowManinfo> list = this.borrowManinfoCustomizeMapper.selectBorrowManInfoByBorrowNid(borrowNid);
		// upd by liushouyi nifa 20181206 end
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public String countMoneyByBorrowId(Map<String, Object> params) {
		return this.borrowTenderInfoCustomizeMapper.countMoneyByBorrowId(params);
	}

	@Override
	@Deprecated
	public BorrowRecover getByborrowNid(String borrowNid,Integer userId) {
		if(StringUtils.isEmpty(borrowNid)){
			return new BorrowRecover();
	    }
	    
	    BorrowRecoverExample example = new BorrowRecoverExample();
	    example.createCriteria().andBorrowNidEqualTo(borrowNid).andUserIdEqualTo(userId);
	    List<BorrowRecover> list = borrowRecoverMapper.selectByExample(example);
	    if(list != null && list.size() > 0){
	    	return list.get(0);
	    }else{
	    	return new BorrowRecover();
	    }
	}

	@Override
	public BorrowTender getBorrowTender(Integer userId, String borrowNid) {
		if(StringUtils.isEmpty(borrowNid)){
			return null;
	    }
		BorrowTenderExample example = new BorrowTenderExample();
	    example.createCriteria().andBorrowNidEqualTo(borrowNid).andUserIdEqualTo(userId);
	    List<BorrowTender> list = borrowTenderMapper.selectByExample(example);
	    if(list != null && list.size() > 0){
	    	return list.get(0);
	    }else{
	    	return null;
	    }
	}

	@Override
	public AppCouponInfoCustomize getCouponfigByUserIdAndBorrowNid(
			Integer userId, String orderId) {
			Map<String, Object> paraMap = new HashMap<>();
			paraMap.put("userId", userId);
			paraMap.put("orderId", orderId);
		return couponConfigCustomizeMapper.getCouponfigByUserIdAndBorrowNid(paraMap);
	}

	@Override
	public List<BorrowRecoverPlan> getByUserIdAndBorrowNid(Integer userId,
			String borrowNid) {
		if(StringUtils.isEmpty(borrowNid)){
			return null;
	    }
	    
		BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
	    example.createCriteria().andBorrowNidEqualTo(borrowNid).andUserIdEqualTo(userId);
	    List<BorrowRecoverPlan> list = borrowRecoverPlanMapper.selectByExample(example);
	    return list;
	}

	@Override
	public BorrowCredit getByOrderId(String orderId,Integer userId) {
		if(StringUtils.isEmpty(orderId)){
			return null;
	    }
		BorrowCreditExample example = new BorrowCreditExample();
	    example.createCriteria().andTenderNidEqualTo(orderId).andCreditUserIdEqualTo(userId);
	    List<BorrowCredit> list = borrowCreditMapper.selectByExample(example);
	    if(list != null && list.size() > 0){
	    	return list.get(0);
	    }else{
	    	return null;
	    }
	}
	/**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param borrowNid
     * @return
     * @author Michael
     */
        
    @Override
    public List<BorrowHouses> getBorrowHousesByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowHousesExample example = new BorrowHousesExample();
        BorrowHousesExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowHouses> list = this.borrowHousesMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list;
        }
        return null;
            
    }

    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param borrowNid
     * @return
     * @author Michael
     */
        
    @Override
    public List<BorrowCarinfo> getBorrowCarinfoByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowCarinfoExample example = new BorrowCarinfoExample();
        BorrowCarinfoExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowCarinfo> list = this.borrowCarinfoMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list;
        }
        return null;
            
    }

	/**
	 * 根据出借订单号获取出借信息
	 * @param orderId
	 * @return
	 */
	@Override
	public BorrowTender selectBorrowTender(String orderId) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		criteria.andNidEqualTo(orderId);
		List<BorrowTender> borrowTenders = borrowTenderMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(borrowTenders)) {
			return borrowTenders.get(0);
		}
		return null;
	}

	/**
	 *  根据订单号获取还款信息
	 * @param orderId
	 * @return
	 */
	@Override
	public BorrowRecover selectBorrowRecoverByNid(String orderId) {
		BorrowRecoverExample example = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria criteria = example.createCriteria();
		criteria.andNidEqualTo(orderId);
		List<BorrowRecover> borrowRecovers = borrowRecoverMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(borrowRecovers)) {
			return borrowRecovers.get(0);
		}
		return null;
	}

	/**
	 * 出借人获取分期还款信息
	 * @param orderId
	 * @return
	 */
	@Override
	public List<BorrowRecoverPlan> selectBorrowRecoverPlanByNid(String orderId) {
		BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
		BorrowRecoverPlanExample.Criteria criteria = example.createCriteria();
		criteria.andNidEqualTo(orderId);
		return borrowRecoverPlanMapper.selectByExample(example);
	}

    /**
     * 根据nid查找borrowTender
     * @param tenderNid
     * @return
     */
    @Override
    public BorrowTender selectBorrowTenderByNid(String tenderNid) {
        BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
        BorrowTenderExample.Criteria criteria = borrowTenderExample.createCriteria();
        criteria.andNidEqualTo(tenderNid);
        List<BorrowTender> borrowTenders = borrowTenderMapper.selectByExample(borrowTenderExample);
		if (!CollectionUtils.isEmpty(borrowTenders)) {
            return borrowTenders.get(0);
        }
        return null;
    }

	/**
	 * 通过assignNid查询承接人出借
	 * @param assignNid
	 * @return
	 */
	@Override
	public CreditTender selectCreditTender(String assignNid) {
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria criteria = creditTenderExample.createCriteria();
		criteria.andAssignNidEqualTo(assignNid);
		List<CreditTender> creditTenders = creditTenderMapper.selectByExample(creditTenderExample);
		if (!CollectionUtils.isEmpty(creditTenders)) {
			return creditTenders.get(0);
		}
		return null;
	}

	/**
	 * 通过assignNid查询承接人还款
	 * @param assignNid
	 * @return
	 */
	@Override
	public List<CreditRepay> selectCreditRepayList(String assignNid) {
		CreditRepayExample creditRepayExample = new CreditRepayExample();
		CreditRepayExample.Criteria criteria = creditRepayExample.createCriteria();
		criteria.andAssignNidEqualTo(assignNid);
		return creditRepayMapper.selectByExample(creditRepayExample);
	}

	@Override
	public boolean isTenderBorrow(Integer userId, String borrowNid,
								  String borrowType) {
		List list = null;

		//根据borrowNid查询borrow表
		Borrow borrow = hjhPlanService.getBorrowByBorrowNid(borrowNid);
		if (borrow.getPlanNid() != null && borrow.getPlanNid().length() > 1) {
			return true;
		}
		try {
			if (borrowType != null && borrowType.contains("1")) {
				HjhDebtCreditTenderExample exa = new HjhDebtCreditTenderExample();
				HjhDebtCreditTenderExample.Criteria criteria = exa.createCriteria();
				criteria.andUserIdEqualTo(userId).andBorrowNidEqualTo(borrowNid);
				list = hjhDebtCreditTenderMapper.selectByExample(exa);
			} else {
				BorrowTenderExample example = new BorrowTenderExample();
				BorrowTenderExample.Criteria criteria = example.createCriteria();
				criteria.andBorrowNidEqualTo(borrowNid).andUserIdEqualTo(userId);
				list = borrowTenderMapper.selectByExample(example);
			}
		} catch (Exception e) {
			_log.error("查询承接信息出错...", e);
		}
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<BorrowCredit> getBorrowList(String orderId, Integer userId) {
		if(StringUtils.isEmpty(orderId)){
			return null;
	    }
		BorrowCreditExample example = new BorrowCreditExample();
	    example.createCriteria().andTenderNidEqualTo(orderId).andCreditUserIdEqualTo(userId);
	    List<BorrowCredit> list = borrowCreditMapper.selectByExample(example);
	    return list;
	}

	@Override
	public AccountBorrow selectAccountBorrow(String borrowNid) {
		AccountBorrowExample example = new AccountBorrowExample();
		AccountBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<AccountBorrow> accountBorrows = accountBorrowMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(accountBorrows)) {
			return accountBorrows.get(0);
		}
		return null;
	}

	@Override
	public BigDecimal getCreditTender(String creditNid) {
		BigDecimal creditFee = creditTenderMapper.selectByCreditNid(creditNid);
		if (creditFee == null) {
			return BigDecimal.ZERO;
		}
		return creditFee;
	};


	@Override
	public List<ActdecListedOne> getActdecList(String borrowNid) {
		ActdecListedOneExample example = new ActdecListedOneExample();
		 com.hyjf.mybatis.model.auto.ActdecListedOneExample.Criteria criteria = example.createCriteria();
		 criteria.andNumberEqualTo(borrowNid);
		return actdecListedOneMapper.selectByExample(example);
	}
	/**
	 * 取得借款信息
	 *
	 * @return
	 */
	@Override
	public BorrowRepay getBorrowRepay(String borrowNid) {
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public int countUserInvest(Integer userId, String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andBorrowNidEqualTo(borrowNid);
		int count = this.borrowTenderMapper.countByExample(example);
		return count;
	}
	/**
	 * 根据borrowNid，userId查找承接记录
	 */
	@Override
	public int countCreditTender(Map<String, Object> params) {
		return webProjectListCustomizeMapper.countCreditTender(params);
	}
	
	/**
	 * 根据参数查找债转记录
	 */
	@Override
	public List<HjhDebtCredit> selectHjhDebtCreditList(Map<String, Object> mapParam) {
		if(null!=mapParam&&mapParam.size()>0) {
			String borrowNid = mapParam.get("borrowNid").toString();
			HjhDebtCreditExample hjhDebtCreditExample = new HjhDebtCreditExample();
	        HjhDebtCreditExample.Criteria criteria = hjhDebtCreditExample.createCriteria();
	        if(mapParam.containsKey("inStatus")) {
	        	List<Integer> listIn = (List<Integer>) mapParam.get("inStatus");
	        	criteria.andCreditStatusIn(listIn);
	        }
	        criteria.andBorrowNidEqualTo(borrowNid);
			List<HjhDebtCredit> listHjhDebtCredit = this.hjhDebtCreditMapper.selectByExample(hjhDebtCreditExample);
			return listHjhDebtCredit;
		}
		return null;
	}
}
