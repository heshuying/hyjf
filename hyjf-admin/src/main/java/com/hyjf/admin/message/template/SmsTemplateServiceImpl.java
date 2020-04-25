package com.hyjf.admin.message.template;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SmsTemplate;
import com.hyjf.mybatis.model.auto.SmsTemplateExample;
import com.hyjf.mybatis.model.auto.SmsTemplateKey;


@Service
public class SmsTemplateServiceImpl extends BaseServiceImpl implements SmsTemplateService {

	
	@Override
	public List<SmsTemplate> queryTem(SmsTemplateExample smsTem) {
		return smsTemplateMapper.selectByExampleWithBLOBs(smsTem);
	}
	
	@Override
	public Integer queryTemCount(SmsTemplateExample smsTem) {
		return smsTemplateMapper.countByExample(smsTem);
	}

	@Override
	public Integer addTem(SmsTemplate smsTem) {
		return smsTemplateMapper.insertSelective(smsTem);
	}

	@Override
	public SmsTemplate queryById(Integer id,String tplCode) {
		SmsTemplateKey ky = new SmsTemplateKey();
		ky.setId(id);
		ky.setTplCode(tplCode);
		return smsTemplateMapper.selectByPrimaryKey(ky);
	}

	@Override
	public Integer updateTem(SmsTemplate smsTem) {
		return smsTemplateMapper.updateByPrimaryKeySelective(smsTem);
	}
}
