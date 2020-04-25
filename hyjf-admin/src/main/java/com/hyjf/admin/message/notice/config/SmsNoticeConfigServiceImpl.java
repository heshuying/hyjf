package com.hyjf.admin.message.notice.config;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigExample;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigKey;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigWithBLOBs;


@Service
public class SmsNoticeConfigServiceImpl extends BaseServiceImpl implements SmsNoticeConfigService {

	@Override
	public List<SmsNoticeConfigWithBLOBs> queryConfig(SmsNoticeConfigExample noCon) {
		return smsNoticeConfigMapper.selectByExampleWithBLOBs(noCon);
		
	}

	@Override
	public Integer addConfig(SmsNoticeConfigWithBLOBs noCon) {
		return smsNoticeConfigMapper.insertSelective(noCon);
	}

	@Override
	public SmsNoticeConfigWithBLOBs queryById(Integer id, String name) {
		SmsNoticeConfigKey ky = new SmsNoticeConfigKey();
		ky.setId(id);
		ky.setName(name);
		return smsNoticeConfigMapper.selectByPrimaryKey(ky);
	}

	@Override
	public Integer updateConfig(SmsNoticeConfigWithBLOBs noCon) {
		return smsNoticeConfigMapper.updateByPrimaryKeySelective(noCon);
	}

	@Override
	public Integer onlyName(String name) {
		SmsNoticeConfigExample exam = new SmsNoticeConfigExample();
		exam.createCriteria().andNameEqualTo(name);
		return smsNoticeConfigMapper.countByExample(exam);
	}

}
