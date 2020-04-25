package com.hyjf.admin.message.config;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SmsConfigExample;
import com.hyjf.mybatis.model.auto.SmsConfigWithBLOBs;


@Service
public class SmsConfigServiceImpl extends BaseServiceImpl implements SmsConfigService {

	
	@Override
	public SmsConfigWithBLOBs queryConfig(SmsConfigWithBLOBs con) {
		
		SmsConfigExample example = new SmsConfigExample();
		
		List<SmsConfigWithBLOBs> smscon = smsConfigMapper.selectByExampleWithBLOBs(example);
		
		if(smscon!=null&&smscon.size()!=0){
			return smscon.get(0);
		}
		return null;
	}

	@Override
	public Integer addConfig(SmsConfigWithBLOBs con) {
		return smsConfigMapper.insertSelective(con);
	}

	@Override
	public Integer updateConfig(SmsConfigWithBLOBs con) {
		return smsConfigMapper.updateByPrimaryKeySelective(con);
	}

}
