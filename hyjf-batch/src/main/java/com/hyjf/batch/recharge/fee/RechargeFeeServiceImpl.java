/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge.fee;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.RechargeFeeReconciliationMapper;
import com.hyjf.mybatis.mapper.auto.UserTransferMapper;
import com.hyjf.mybatis.mapper.customize.RechargeFeeCustomizeMapper;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliationExample;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.UserTransferExample;
import com.hyjf.mybatis.model.customize.RechargeFeeCustomize;

/**
 * 充值手续费垫付对账单生成
 * @author 李深强
 */
@Service
public class RechargeFeeServiceImpl extends BaseServiceImpl implements RechargeFeeService {

    @Autowired
    private RechargeFeeCustomizeMapper rechargeFeeCustomizeMapper;
    
    @Autowired
    private RechargeFeeReconciliationMapper rechargeFeeReconciliationMapper;

    @Autowired
    private UserTransferMapper userTransferMapper;

    
	/**
	 * 查询借款人充值垫付手续费
	 * @param rechargeFee
	 * @return
	 * @author Michael
	 */
	@Override
	public List<RechargeFeeCustomize> selectRechargeFeeReconciliationList(RechargeFeeCustomize rechargeFee) {
		return rechargeFeeCustomizeMapper.selectRechargeFeeReconciliationList(rechargeFee);
	}

	/**
	 *插入数据
	 * @param rechargeFee
	 * @author Michael
	 */
		
	@Override
	public void insertRechargeFeeReconciliation(RechargeFeeCustomize rechargeFee) {
		RechargeFeeReconciliation record = new RechargeFeeReconciliation();
		record.setStartTime(rechargeFee.getStartTime());
		record.setEndTime(rechargeFee.getEndTime());
		record.setRechargeAmount(rechargeFee.getRechargeAmount());
		record.setRechargeFee(rechargeFee.getRechargeFee());
		record.setUserId(rechargeFee.getUserId());
		record.setUserName(rechargeFee.getUserName());
		record.setStatus(0);//待付款
		record.setAddTime(GetDate.getNowTime10());
		record.setIsMail(0);//未发送邮件
		record.setRemark("定时任务插入");
		record.setRechargeNid(getRechargeNid(rechargeFee.getUserId(),rechargeFee.getUserName()));
		this.rechargeFeeReconciliationMapper.insertSelective(record);
	}
 
	/**
     * 获取编号
     * @param userId
     * @param username
     * @return
     */
    public String getRechargeNid(Integer userId,String username){
    	String rechargeNid = "";
    	String monthday = "";
		Calendar cal = Calendar.getInstance();
		int yearnow = cal.get(Calendar.YEAR);
		int monthnow = cal.get(Calendar.MONTH) + 1;
		if (monthnow < 10) {
			monthday = String.valueOf(yearnow) +"-"+ "0" + String.valueOf(monthnow) + "-01";
		} else {
			monthday = String.valueOf(yearnow) +"-"+ String.valueOf(monthnow)+ "-01";
		}
		//月初时间
		int sssTime = Integer.parseInt(GetDate.get10Time(monthday));
		RechargeFeeReconciliationExample example = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andAddTimeBetween(sssTime, GetDate.getNowTime10());
		int count = this.rechargeFeeReconciliationMapper.countByExample(example);
		if(count > 0){
			rechargeNid = GetDate.getMonthDay()+"0"+(count + 1) + "_" + username;
		}else{
			rechargeNid = GetDate.getMonthDay()+"01" + "_" + username;
		}
    	return rechargeNid;
    }

	/**
	 * 取未付款数据
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<RechargeFeeReconciliation> selectFeeListDelay() {
		RechargeFeeReconciliationExample example = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(0);//取未付款数据
		cra.andIsMailNotEqualTo(2);//2为30天后数据  不用提醒
		return  this.rechargeFeeReconciliationMapper.selectByExample(example);
	}

	/**
	 * 更新数据库
	 * @param rechargeFeeReconciliation
	 * @author Michael
	 */
	@Override
	public void updateRechargeFeeReconciliation(RechargeFeeReconciliation rechargeFeeReconciliation) {
		this.rechargeFeeReconciliationMapper.updateByPrimaryKeySelective(rechargeFeeReconciliation);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<UserTransfer> selectTransferingRecord() {
		UserTransferExample example = new UserTransferExample();
		UserTransferExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(1);
		return userTransferMapper.selectByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userTransfer
	 * @author Michael
	 */
	@Override
	public void updateTransferRecord(UserTransfer userTransfer) {
		userTransfer.setStatus(4);//已过期
		userTransferMapper.updateByPrimaryKeySelective(userTransfer);
	}

}
