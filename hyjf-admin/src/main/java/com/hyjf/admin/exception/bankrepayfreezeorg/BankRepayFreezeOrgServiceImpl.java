package com.hyjf.admin.exception.bankrepayfreezeorg;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankRepayOrgFreezeLog;
import com.hyjf.mybatis.model.auto.BankRepayOrgFreezeLogExample;
import com.hyjf.mybatis.model.customize.admin.BankRepayFreezeOrgCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankRepayFreezeOrgServiceImpl extends BaseServiceImpl implements BankRepayFreezeOrgService {

	private Logger logger = LoggerFactory.getLogger(BankRepayFreezeOrgServiceImpl.class);

	@Override
	public Integer selectCount(BankRepayFreezeOrgBean form) {
		Map<String,Object> paraMap = new HashMap<>();
		if(StringUtils.isNotBlank(form.getBorrowNid())){
			paraMap.put("borrowNid", form.getBorrowNid());
		}
		if(StringUtils.isNotBlank(form.getInstCode())){
			paraMap.put("instCode", form.getInstCode());
		}
		if(StringUtils.isNotBlank(form.getPlanNid())){
			paraMap.put("planNid", form.getPlanNid());
		}
		if(StringUtils.isNotBlank(form.getBorrowUserName())){
			paraMap.put("borrowUserName", form.getBorrowUserName());
		}
		if(StringUtils.isNotBlank(form.getRepayUserName())){
			paraMap.put("repayUserName", form.getRepayUserName());
		}
		if(StringUtils.isNotBlank(form.getOrderId())){
			paraMap.put("orderId", form.getOrderId());
		}
		if(StringUtils.isNotBlank(form.getSubmitTimeStartSrch())){
			paraMap.put("submitTimeStartSrch", form.getSubmitTimeStartSrch());
		}
		if(StringUtils.isNotBlank(form.getSubmitTimeEndSrch())){
			paraMap.put("submitTimeEndSrch", form.getSubmitTimeEndSrch());
		}
		return bankRepayFreezeOrgCustomizeMapper.selectCount(paraMap);
	}

	@Override
	public List<BankRepayFreezeOrgCustomize> selectList(BankRepayFreezeOrgBean form) {
		Map<String,Object> paraMap = new HashMap<>();
		if(StringUtils.isNotBlank(form.getBorrowNid())){
			paraMap.put("borrowNid", form.getBorrowNid());
		}
		if(StringUtils.isNotBlank(form.getInstCode())){
			paraMap.put("instCode", form.getInstCode());
		}
		if(StringUtils.isNotBlank(form.getPlanNid())){
			paraMap.put("planNid", form.getPlanNid());
		}
		if(StringUtils.isNotBlank(form.getBorrowUserName())){
			paraMap.put("borrowUserName", form.getBorrowUserName());
		}
		if(StringUtils.isNotBlank(form.getRepayUserName())){
			paraMap.put("repayUserName", form.getRepayUserName());
		}
		if(StringUtils.isNotBlank(form.getOrderIdSrch())){
			paraMap.put("orderId", form.getOrderIdSrch());
		}
		if(StringUtils.isNotBlank(form.getSubmitTimeStartSrch())){
			paraMap.put("submitTimeStartSrch", form.getSubmitTimeStartSrch());
		}
		if(StringUtils.isNotBlank(form.getSubmitTimeEndSrch())){
			paraMap.put("submitTimeEndSrch", form.getSubmitTimeEndSrch());
		}
		if(form.getLimitStart() >= 0){
			paraMap.put("limitStart", form.getLimitStart());
		}
		if(form.getLimitEnd() >= 0){
			paraMap.put("limitEnd", form.getLimitEnd());
		}
		return bankRepayFreezeOrgCustomizeMapper.selectList(paraMap);
	}

	/**
	 * 根据id查询代偿冻结记录
	 * @param id
	 * @return
	 */
	@Override
	public BankRepayOrgFreezeLog getFreezeLogById(Integer id){
		return bankRepayOrgFreezeLogMapper.selectByPrimaryKey(id);
	}

	@Override
	public BankRepayOrgFreezeLog getFreezeLog(String orderId, String borrowNid) {
		BankRepayOrgFreezeLogExample example = new BankRepayOrgFreezeLogExample();
		BankRepayOrgFreezeLogExample.Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BankRepayOrgFreezeLog> bankRepayOrgFreezeLogList = bankRepayOrgFreezeLogMapper.selectByExample(example);
		if(bankRepayOrgFreezeLogList != null && bankRepayOrgFreezeLogList.size() >0){
			return bankRepayOrgFreezeLogList.get(0);
		}
		return null;
	}

	/**
	 * 根据id删除代偿冻结记录
	 * @param id
	 * @return
	 */
	@Override
	public int deleteFreezeLogById(Integer id){
		BankRepayOrgFreezeLog freezeLog = new BankRepayOrgFreezeLog();
		freezeLog.setId(id);
		freezeLog.setDelFlag(1);
		freezeLog.setUpdateTime(GetDate.getNowTime10());
		return bankRepayOrgFreezeLogMapper.updateByPrimaryKeySelective(freezeLog);
	}



}
