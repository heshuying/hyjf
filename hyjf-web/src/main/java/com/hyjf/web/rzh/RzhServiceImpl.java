/**
 * Description:我要融资service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.rzh;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Loan;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import com.hyjf.mybatis.model.auto.SmsLogWithBLOBs;
import com.hyjf.web.BaseServiceImpl;

@Service
public class RzhServiceImpl extends BaseServiceImpl implements RzhService {

	@Override
	public void saveUserLoan(RzhBean ru) {
		// 校验验证码
		int time = GetDate.getNowTime10();
			Loan loan = new Loan();
			loan.setName(ru.getName());
			loan.setTel(ru.getMobile());
			loan.setSex(Integer.parseInt(ru.getSex()));
			loan.setAge(0);
			loan.setMoney(ru.getPrice());
			loan.setDay(ru.getApproach());
			loan.setUse(ru.getUse());
			loan.setProvince(ru.getProvince());
			loan.setCity(ru.getCity());
			loan.setArea(ru.getArea());
			loan.setMortgage(ru.getInfo());
			loan.setAddip(ru.getIp());
			loan.setMortgageState(0);
			loan.setContent("");
			loan.setState(0);
			loan.setAddtime(time);
			if (StringUtils.isNotEmpty(ru.getGname())) {
				loan.setGname(ru.getGname());
			}
			if (StringUtils.isNotEmpty(ru.getGyear())) {
				loan.setGyear(ru.getGyear());
			}
			if (StringUtils.isNotEmpty(ru.getGdress())) {
				loan.setGdress(ru.getGdress());
			}
			if (StringUtils.isNotEmpty(ru.getGplay())) {
				loan.setGplay(ru.getGplay());
			}
			if (StringUtils.isNotEmpty(ru.getGpro())) {
				loan.setGpro(ru.getGpro());
			}
			if (StringUtils.isNotEmpty(ru.getGget())) {
				loan.setGmoney(ru.getGmoney());
			}
			if (StringUtils.isNotEmpty(ru.getGget())) {
				loan.setGget(ru.getGget());
			}
			if (StringUtils.isNotEmpty(ru.getGpay())) {
				loan.setGpay(ru.getGpay());
			}
			//保存相应的融资数据
			loanMapper.insertSelective(loan);

	}

	@Override
	public void saveSmsLog(String mobile, String smsInfo) {
		SmsLogWithBLOBs smsLog = new SmsLogWithBLOBs();
		smsLog.setType("出借短信验证码");
		smsLog.setMobile(mobile);
		smsLog.setContent(smsInfo);
		smsLogMapper.insertSelective(smsLog);
	}

//	@Override
//	public SmsTemplate getMessTemplate(String templateCode) {
//		SmsTemplateExample example = new SmsTemplateExample();
//		SmsTemplateExample.Criteria crt = example.createCriteria();
//		crt.andTplCodeEqualTo(templateCode);
//		List<SmsTemplate> template = smsTemplateMapper.selectByExampleWithBLOBs(example);
//		if (template != null && template.size() == 1) {
//			return template.get(0);
//		} else {
//			return null;
//		}
//	}

	@Override
	public int checkMobileCode(String phone, String code) {
		int time = GetDate.getNowTime10();
		int timeAfter = time - 1800;
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
		cra.andPosttimeLessThanOrEqualTo(time);
		cra.andMobileEqualTo(phone);
		cra.andCheckcodeEqualTo(code);
		List<SmsCode> smscodes = smsCodeMapper.selectByExample(example);
		if (smscodes != null && smscodes.size() > 0) {
			return smscodes.size();
		} else {
			return 0;
		}

	}

}
