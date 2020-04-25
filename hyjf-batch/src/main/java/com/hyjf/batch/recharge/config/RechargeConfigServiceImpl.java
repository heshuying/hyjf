/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge.config;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.BankConfigMapper;
import com.hyjf.mybatis.mapper.auto.BankRechargeLimitConfigMapper;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfigExample;

/**
 * 充值限额查询
 * @author 李深强
 */
@Service
public class RechargeConfigServiceImpl extends BaseServiceImpl implements RechargeConfigService {

    @Autowired
    private BankRechargeLimitConfigMapper bankRechargeLimitConfigMapper;
    
    @Autowired
    private BankConfigMapper bankConfigMapper;

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param bankCode
	 * @param singleTransQuota
	 * @param cardDailyTransQuota
	 * @author Michael
	 */
		
	@Override
	public void updateBankRechargeConfig(String bankCode, String singleTransQuota, String cardDailyTransQuota) {
		if(StringUtils.isEmpty(bankCode)){
			return;
		}
		//获取银行卡信息
		BankConfig bank = this.getBankConfigByCode(bankCode);
		if(bank == null){
			return;
		}
		//获取快捷卡配置信息
		BankRechargeLimitConfigExample example = new BankRechargeLimitConfigExample();
		BankRechargeLimitConfigExample.Criteria cra = example.createCriteria();
		cra.andBankIdEqualTo(bank.getId());
		List<BankRechargeLimitConfig> list = this.bankRechargeLimitConfigMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			for(int i = 0;i < list.size();i++){
				BankRechargeLimitConfig config = list.get(i);
				if(StringUtils.isEmpty(singleTransQuota)){
					config.setSingleQuota(null);
				}else{
					config.setSingleQuota(new BigDecimal(singleTransQuota.replaceAll(",", "")));
				}
				if(StringUtils.isEmpty(cardDailyTransQuota)){
					config.setSingleCardQuota(null);
				}else{
					config.setSingleCardQuota(new BigDecimal(cardDailyTransQuota.replaceAll(",", "")));
				}
				//更新数据
				this.bankRechargeLimitConfigMapper.updateByPrimaryKeySelective(config);
			}
		}
	}
	/**
	 * 根据银行编码获取银行信息
	 * @param code
	 * @return
	 */
	public BankConfig getBankConfigByCode(String code){
		//获取银行卡信息
		BankConfigExample example = new BankConfigExample();
        BankConfigExample.Criteria cra = example.createCriteria();
        cra.andQuickPaymentEqualTo(1);//支持快捷支付
        cra.andCodeEqualTo(code);
		List<BankConfig> bankList = this.bankConfigMapper.selectByExample(example);
		if(bankList != null && bankList.size() > 0){
			return bankList.get(0);
		}
		return null;
	}

}
