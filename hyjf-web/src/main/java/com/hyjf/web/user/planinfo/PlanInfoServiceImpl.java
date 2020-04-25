package com.hyjf.web.user.planinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.user.mytender.ProjectRepayListBean;
import com.hyjf.web.user.mytender.UserInvestListBean;

import cn.jpush.api.utils.StringUtils;

@Service
public class PlanInfoServiceImpl extends BaseServiceImpl implements PlanInfoService {

	@Override
	public List<PlanLockCustomize> selectUserProjectList(Map<String ,Object> params) {
		PlanCommonCustomize planCommonCustomize= new PlanCommonCustomize();
		if (params.get("status")!=null) {
			planCommonCustomize.setStatus(params.get("status")+"");	
		}
		if (params.get("userId")!=null) {
			planCommonCustomize.setUserId(params.get("userId")+"");
		}
		if (params.get("limitStart")!=null) {
			planCommonCustomize.setLimitStart(Integer.parseInt(params.get("limitStart")+""));	
		}
		if (params.get("limitEnd")!=null) {
			planCommonCustomize.setLimitEnd(Integer.parseInt(params.get("limitEnd")+""));
		}
		if (params.get("accedeOrderId")!=null) {
			planCommonCustomize.setAccedeOrderId(params.get("accedeOrderId")+"");
		}
		return planLockCustomizeMapper.selectPlanAccedeList(planCommonCustomize);
	}
	@Override
	public Long countUserProjectRecordTotal(Map<String, Object> params) {
		PlanCommonCustomize planCommonCustomize= new PlanCommonCustomize();
		if (params.get("status")!=null) {
			planCommonCustomize.setStatus(params.get("status")+"");	
		}
		if (params.get("userId")!=null) {
			planCommonCustomize.setUserId(params.get("userId")+"");
		}
		Long total=planLockCustomizeMapper.countPlanAccede(planCommonCustomize);
		return total;
	}
	@Override
    public List<PlanLockCustomize> selectUserProjectListCapital(Map<String ,Object> params) {
        PlanCommonCustomize planCommonCustomize= new PlanCommonCustomize();
        if (params.get("status")!=null) {
            planCommonCustomize.setStatus(params.get("status")+""); 
        }
        if (params.get("userId")!=null) {
            planCommonCustomize.setUserId(params.get("userId")+"");
        }
        if (params.get("limitStart")!=null) {
            planCommonCustomize.setLimitStart(Integer.parseInt(params.get("limitStart")+""));   
        }
        if (params.get("limitEnd")!=null) {
            planCommonCustomize.setLimitEnd(Integer.parseInt(params.get("limitEnd")+""));
        }
        if (params.get("accedeOrderId")!=null) {
            planCommonCustomize.setAccedeOrderId(params.get("accedeOrderId")+"");
        }
        return planLockCustomizeMapper.selectPlanAccedeListForAdmin(planCommonCustomize);
    }
	@Override
	public HashMap<String, Object> selectPlanInfoSum(String accedeOrderId) {
		return planLockCustomizeMapper.selectPlanInfoSum(accedeOrderId);
	}
	@Override
	public Long countDebtInvestList(Map<String, Object> params) {
		return planLockCustomizeMapper.countInvest(params);
	}
	@Override
	public List<PlanInvestCustomize> selectPlanInvestList(Map<String, Object> params) {
		return	planLockCustomizeMapper.selectInvestList(params);
	}
	@Override
	public List<DebtPlan> getPlanByPlanNid(String planNid) {
		DebtPlanExample example=new DebtPlanExample();
		DebtPlanExample.Criteria cri=example.createCriteria();
		cri.andDebtPlanNidEqualTo(planNid);
		return debtPlanMapper.selectByExample(example);
		
	}
	@Override
	public BatchDebtPlanAccedeCustomize selectPlanAccedeInfo(
			DebtPlanAccede debtPlanAccede) {
		Map<String, String> params = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(debtPlanAccede.getPlanNid())) {
			params.put("planNidSrch", debtPlanAccede.getPlanNid());
		}
		if (StringUtils.isNotEmpty(debtPlanAccede.getAccedeOrderId())) {
			params.put("accedeOrderIdSrch", debtPlanAccede.getAccedeOrderId());
		}
		return this.batchDebtPlanAccedeCustomizeMapper.selectPlanAccedeInfo(params);
	}
	@Override
	public List<DebtPlanBorrowCustomize> getDebtPlanBorrowList(
			Map<String, Object> param) {
		List<DebtPlanBorrowCustomize> result = this.planCustomizeMapper.getDebtPlanBorrowList(param);
		return result;
	}
	@Override
	public List<DebtBorrowCustomize> selectBorrowList(
			DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.selectBorrowList(debtBorrowCommonCustomize);
	}
	@Override
	public int countProjectRepayPlanRecordTotal(ProjectRepayListBean bean) {
		Map<String ,Object> params=new HashMap<String ,Object>();
		String userId = StringUtils.isNotEmpty(bean.getUserId())?bean.getUserId():null;
		String borrowNid =StringUtils.isNotEmpty(bean.getBorrowNid())?bean.getBorrowNid():null;
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", bean.getNid());
		int total = webUserInvestListCustomizeMapper.countProjectLoanDetailRecordTotal(params);
		return total;
	}
	@Override
	public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(
			ProjectRepayListBean form, int offset, int limit) {

		Map<String ,Object> params=new HashMap<String ,Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		String borrowNid =StringUtils.isNotEmpty(form.getBorrowNid())?form.getBorrowNid():null;
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", form.getNid());
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper.selectProjectLoanDetailList(params);
		return projectRepayList;
	}
	@Override
	public List<WebUserInvestListCustomize> selectUserInvestList(
			UserInvestListBean form, int limitStart, int limitEnd) {
		String borrowNid =StringUtils.isNotEmpty(form.getBorrowNid())?form.getBorrowNid():null;
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		String nid =StringUtils.isNotEmpty(form.getNid())?form.getNid():null;
		Map<String ,Object> params=new HashMap<String ,Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		params.put("nid", nid);
		if(limitStart==0||limitStart>0){
			params.put("limitStart", limitStart);
		}
		if(limitEnd>0){
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list=webUserInvestListCustomizeMapper.selectUserDebtInvestList(params);
		return list;
	}
	@Override
	public int countUserInvestRecordTotal(UserInvestListBean form) {
		String borrowNid =StringUtils.isNotEmpty(form.getBorrowNid())?form.getBorrowNid():null;
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		Map<String ,Object> params=new HashMap<String ,Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		int  total=webUserInvestListCustomizeMapper.countUserDebtInvestRecordTotal(params);
		return total;
	}
	@Override
	public Long countDebtInvestListNew(Map<String, Object> params) {
		return planLockCustomizeMapper.countInvestNew(params);
	}
	@Override
	public List<PlanInvestCustomize> selectPlanInvestListNew(Map<String, Object> params) {
		return	planLockCustomizeMapper.selectInvestListNew(params);
	}
	@Override
	public List<PlanInvestCustomize> selectInvestCreditList(
			Map<String, Object> params1) {
		return planLockCustomizeMapper.selectInvestCreditList(params1);
	}
	@Override
	public List<PlanInvestCustomize> selectCreditCreditList(
			Map<String, Object> params1) {
		return planLockCustomizeMapper.selectCreditCreditList(params1);
	}
}
