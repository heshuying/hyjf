package com.hyjf.admin.message.mail.template;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SmsMailTemplate;
import com.hyjf.mybatis.model.auto.SmsMailTemplateExample;
import com.hyjf.mybatis.model.auto.SmsMailTemplateKey;


@Service
public class SmsMailTemplateServiceImpl extends BaseServiceImpl implements SmsMailTemplateService {

	
	@Override
	public List<SmsMailTemplate> queryMailTem(SmsMailTemplateExample smsTem) {
		return smsMailTemplateMapper.selectByExampleWithBLOBs(smsTem);
	}
	
	@Override
	public Integer queryMailTemCount(SmsMailTemplateExample smsTem) {
		return smsMailTemplateMapper.countByExample(smsTem);
	}

	@Override
	public Integer addMailTem(SmsMailTemplate smsTem) {
		return smsMailTemplateMapper.insertSelective(smsTem);
	}

	@Override
	public SmsMailTemplate queryById(Integer id,String mailValue) {
		SmsMailTemplateKey ky = new SmsMailTemplateKey();
		ky.setId(id);
		ky.setMailValue(mailValue);
		return smsMailTemplateMapper.selectByPrimaryKey(ky);
	}

	@Override
	public Integer updateMailTem(SmsMailTemplate smsTem) {
		return smsMailTemplateMapper.updateByPrimaryKeySelective(smsTem);
	}
}
