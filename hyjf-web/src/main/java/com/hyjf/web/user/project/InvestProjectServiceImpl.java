package com.hyjf.web.user.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class InvestProjectServiceImpl extends BaseServiceImpl implements InvestProjectService {

	@Override
	public List<WebUserProjectListCustomize> selectUserProjectList(ProjectListBean hzt,int limitStart,int limitEnd) {
		Map<String ,Object> params=new HashMap<String ,Object>();
		String status = StringUtils.isNotEmpty(hzt.getStatus())?hzt.getStatus():null;
		String userId = StringUtils.isNotEmpty(hzt.getUserId())?hzt.getUserId():null;
		params.put("status", status);
		params.put("userId", userId);
		if(limitStart==0||limitStart>0){
			params.put("limitStart", limitStart);
		}
		if(limitEnd>0){
			params.put("limitEnd", limitEnd);
		}
		List<WebUserProjectListCustomize> list=webUserInvestListCustomizeMapper.selectUserProjectList(params);
		return list;
	}
	@Override
	public int countUserProjectRecordTotal(ProjectListBean form) {
		Map<String ,Object> params=new HashMap<String ,Object>();
		String status =StringUtils.isNotEmpty(form.getStatus())?form.getStatus():null;
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		params.put("userId", userId);
		params.put("status", status);
		int total=webUserInvestListCustomizeMapper.countUserProjectRecordTotal(params);
		return total;
	}


	@Override
	public List<WebUserInvestListCustomize> selectUserInvestList(UserInvestListBean form, int limitStart, int limitEnd) {
		String borrowNid =StringUtils.isNotEmpty(form.getBorrowNid())?form.getBorrowNid():null;
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		Map<String ,Object> params=new HashMap<String ,Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		if(limitStart==0||limitStart>0){
			params.put("limitStart", limitStart);
		}
		if(limitEnd>0){
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list=webUserInvestListCustomizeMapper.selectUserInvestList(params);
		return list;
	}
	@Override
	public int countUserInvestRecordTotal(UserInvestListBean form) {
		String borrowNid =StringUtils.isNotEmpty(form.getBorrowNid())?form.getBorrowNid():null;
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		Map<String ,Object> params=new HashMap<String ,Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		int  total=webUserInvestListCustomizeMapper.countUserInvestRecordTotal(params);
		return total;
	}
	@Override
	public int countProjectRepayRecordTotal(ProjectRepayListBean form) {
		Map<String ,Object> params=new HashMap<String ,Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		String borrowNid =StringUtils.isNotEmpty(form.getBorrowNid())?form.getBorrowNid():null;
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		int total = webUserInvestListCustomizeMapper.countProjectRepayRecordTotal(params);
		return total;
	}
	@Override
	public List<WebProjectRepayListCustomize> selectProjectRepayList(ProjectRepayListBean form, int offset, int limit) {

		Map<String ,Object> params=new HashMap<String ,Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId())?form.getUserId():null;
		String borrowNid =StringUtils.isNotEmpty(form.getBorrowNid())?form.getBorrowNid():null;
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper.selectProjectRepayList(params);
		return projectRepayList;
	}

}
