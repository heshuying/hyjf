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
package com.hyjf.app.hjhasset;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowCarinfoExample;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.app.AppHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.app.AppHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppMortgageCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectConsumeCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.app.AppVehiclePledgeCustomize;

@Service
public class HjhProjectServiceImpl extends BaseServiceImpl implements HjhProjectService {

	/**
	 * 查询指定项目类型的项目列表
	 */
	@Override
	public List<AppProjectListCustomize> searchProjectList(Map<String, Object> params) {
		List<AppProjectListCustomize> list = appProjectListCustomizeMapper.selectProjectList(params);
		return list;
	}

	/**
	 * 查询指定项目类型的项目数目
	 */
	@Override
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
	public List<HjhProjectFileBean> searchProjectFiles(String borrowNid, String url) {
		String files = appProjectListCustomizeMapper.searchProjectFiles(borrowNid);
		List<HjhProjectFileBean> fileList = new ArrayList<HjhProjectFileBean>();
		if (StringUtils.isNotEmpty(files)) {
			JSONArray jsonFiles = JSONArray.parseArray(files);
			for (int i = 0; i < jsonFiles.size(); i++) {
				JSONObject urlFile = jsonFiles.getJSONObject(i);
				JSONArray urls = urlFile.getJSONArray("data");
				for (int j = 0; j < urls.size(); j++) {
					JSONObject file = urls.getJSONObject(j);
					HjhProjectFileBean fileBean = new HjhProjectFileBean();
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
			Collections.sort(fileList, new Comparator<HjhProjectFileBean>() {
				public int compare(HjhProjectFileBean arg0, HjhProjectFileBean arg1) {
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
	public List<HjhProjectRepayPlanBean> getRepayPlan(String borrowNid) {
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
		List<HjhProjectRepayPlanBean> repayPlans = new ArrayList<HjhProjectRepayPlanBean>();
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
				if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {//原borrow.getReverifyStatus() == 3
					repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
				}
				HjhProjectRepayPlanBean planIntrest = new HjhProjectRepayPlanBean(repayTime, df.format(info.getRepayAccountInterest()),
						"利息");
				HjhProjectRepayPlanBean plan = new HjhProjectRepayPlanBean(repayTime, df.format(info.getRepayAccountCapital()), "本金");
				HjhProjectRepayPlanBean planAccount = new HjhProjectRepayPlanBean(repayTime, df.format(info.getRepayAccountCapital().add(info.getRepayAccountInterest())), "本息");
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
					if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {//原borrow.getReverifyStatus() == 3
						repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
					}
					String repayType = "本息";
					if (i < info.getListMonthly().size() - 1 && borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						repayType = "利息";
					}
					HjhProjectRepayPlanBean plan = new HjhProjectRepayPlanBean(repayTime, df.format(sub.getRepayAccount()), repayType);
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
	public List<HjhPlanCustomize> searchHjhPlanList(Map<String, Object> params) {
		List<HjhPlanCustomize> hjhPlanList = this.hjhPlanCustomizeMapper.selectHjhPlanList(params);
		return hjhPlanList;
	}
}
