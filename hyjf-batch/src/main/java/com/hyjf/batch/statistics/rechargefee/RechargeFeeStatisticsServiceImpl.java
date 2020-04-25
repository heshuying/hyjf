/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.statistics.rechargefee;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.RechargeFeeStatisticsMapper;
import com.hyjf.mybatis.mapper.customize.RechargeFeeCustomizeMapper;
import com.hyjf.mybatis.model.auto.RechargeFeeStatistics;
import com.hyjf.mybatis.model.auto.RechargeFeeStatisticsExample;
import com.hyjf.mybatis.model.customize.RechargeFeeStatisticsCustomize;

/**
 * 充值手续费垫付对账单生成
 * @author 李深强
 */
@Service
public class RechargeFeeStatisticsServiceImpl extends BaseServiceImpl implements RechargeFeeStatisticsService {

    @Autowired
    private RechargeFeeCustomizeMapper rechargeFeeCustomizeMapper;
    
    @Autowired
    private RechargeFeeStatisticsMapper rechargeFeeStatisticsMapper;

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param staDate
	 * @return
	 * @author Michael
	 */
    
	@Override
	public int isExistsRecordByDate(String staDate) {
		int recordId = 0;
		if (StringUtils.isEmpty(staDate)) {
			return recordId;
		}
		RechargeFeeStatisticsExample example = new RechargeFeeStatisticsExample();
		RechargeFeeStatisticsExample.Criteria cra = example.createCriteria();
		cra.andStatDateEqualTo(staDate);
		List<RechargeFeeStatistics> list = rechargeFeeStatisticsMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			recordId = list.get(0).getId();
		}
		return recordId;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param rechargeFeeStatistics
	 * @author Michael
	 */
		
	@Override
	public void insertRechargeFeeStatisticsRecord(String staDate,RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize) {
		RechargeFeeStatistics record = new RechargeFeeStatistics();
		record.setRechargeAmount(rechargeFeeStatisticsCustomize.getRechargeAmount());	
		record.setQuickAmount(rechargeFeeStatisticsCustomize.getQuickAmount());
		record.setPersonalAmount(rechargeFeeStatisticsCustomize.getPersonalAmount());
		record.setComAmount(rechargeFeeStatisticsCustomize.getCompAmount());
		record.setFee(rechargeFeeStatisticsCustomize.getDianfuAmount());
		record.setUpdateTime(GetDate.getNowTime10());
		record.setStatDate(staDate);
		rechargeFeeStatisticsMapper.insertSelective(record);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param rechargeFeeStatistics
	 * @author Michael
	 */
		
	@Override
	public void updateRechargeFeeStatisticsRecord(int recordId,RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize) {
		RechargeFeeStatistics record = rechargeFeeStatisticsMapper.selectByPrimaryKey(recordId);
		record.setRechargeAmount(rechargeFeeStatisticsCustomize.getRechargeAmount());	
		record.setQuickAmount(rechargeFeeStatisticsCustomize.getQuickAmount());
		record.setPersonalAmount(rechargeFeeStatisticsCustomize.getPersonalAmount());
		record.setComAmount(rechargeFeeStatisticsCustomize.getCompAmount());
		record.setFee(rechargeFeeStatisticsCustomize.getDianfuAmount());
		record.setUpdateTime(GetDate.getNowTime10());
		rechargeFeeStatisticsMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 查询统计信息
	 * @param rechargeFeeStatisticsCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public RechargeFeeStatisticsCustomize selectRechargeFeeStatistics(RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize) {
		RechargeFeeStatisticsCustomize record = null;
		record = rechargeFeeCustomizeMapper.selectRechargeFeeStatistics(rechargeFeeStatisticsCustomize);
		if(record != null){
			if(record.getRechargeAmount() == null){
				record.setRechargeAmount(BigDecimal.ZERO);
			}
			if(record.getQuickAmount() == null){
				record.setQuickAmount(BigDecimal.ZERO);
			}
			if(record.getPersonalAmount() == null){
				record.setPersonalAmount(BigDecimal.ZERO);
			}
			if(record.getCompAmount() == null){
				record.setCompAmount(BigDecimal.ZERO);
			}
			if(record.getDianfuAmount() == null){
				record.setDianfuAmount(BigDecimal.ZERO);
			}
		}else{
			record = new RechargeFeeStatisticsCustomize();
			record.setRechargeAmount(BigDecimal.ZERO);
			record.setQuickAmount(BigDecimal.ZERO);
			record.setPersonalAmount(BigDecimal.ZERO);
			record.setCompAmount(BigDecimal.ZERO);
			record.setDianfuAmount(BigDecimal.ZERO);
		}
		return record;
	}

}
