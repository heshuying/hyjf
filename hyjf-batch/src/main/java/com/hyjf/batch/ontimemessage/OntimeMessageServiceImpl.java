package com.hyjf.batch.ontimemessage;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.SmsOntimeExample;
import com.hyjf.mybatis.model.auto.SmsOntimeWithBLOBs;
import com.hyjf.mybatis.model.customize.SmsCodeCustomize;

/**
 * 自动定时发短信
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class OntimeMessageServiceImpl extends BaseServiceImpl implements OntimeMessageService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 取得定时发送API任务表
	 *
	 * @return
	 */
	public List<SmsOntimeWithBLOBs> getOntimeList(Integer status) {
		SmsOntimeExample example = new SmsOntimeExample();
		SmsOntimeExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(0);
		criteria.andEndtimeGreaterThanOrEqualTo(GetDate.getSearchStartTime(new Date()));
		criteria.andEndtimeLessThanOrEqualTo(GetDate.getNowTime10());
		List<SmsOntimeWithBLOBs> list = this.smsOntimeMapper.selectByExampleWithBLOBs(example);
		return list;
	}

	/**
	 * 更新定时发短信API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updatetOntime(Integer id, Integer status) {
		return updatetOntime(id, status, null);
	}

	/**
	 * 更新定时发短信API任务表
	 *
	 * @param id
	 * @param status
	 * @param data
	 * @return
	 */
	public int updatetOntime(Integer id, Integer status, String data) {
		SmsOntimeWithBLOBs record = new SmsOntimeWithBLOBs();
		record.setId(id);
		record.setStatus(status);
		if (Validator.isNotNull(data) || status == 2) {
			record.setRemark(data);
			record.setStarttime(GetDate.getMyTimeInMillis());
		}
		return this.smsOntimeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SmsCodeCustomize> queryUser(SmsOntimeWithBLOBs apicron) {
		SmsCodeCustomize sm = new SmsCodeCustomize();
		if (apicron.getAddMoneyCount() != null) {
			sm.setAdd_money_count(apicron.getAddMoneyCount());
		}

		if (StringUtils.isNotEmpty(apicron.getAddTimeBegin())) {
			int begin = Integer.parseInt(GetDate.get10Time(apicron.getAddTimeBegin()));
			sm.setAdd_time_begin(begin);
		}

		if (StringUtils.isNotEmpty(apicron.getAddTimeEnd())) {
			int end = Integer.parseInt(GetDate.get10Time(apicron.getAddTimeEnd()));
			sm.setAdd_time_end(end);
		}

		if (StringUtils.isNotEmpty(apicron.getReTimeBegin())) {
			int re_begin = Integer.parseInt(GetDate.get10Time(apicron.getReTimeBegin()));
			sm.setRe_time_begin(re_begin);
		}

		if (StringUtils.isNotEmpty(apicron.getReTimeEnd())) {
			int re_end = Integer.parseInt(GetDate.get10Time(apicron.getReTimeEnd()));
			sm.setRe_time_end(re_end);
		}
		if (apicron.getOpenAccount() != null) {
			sm.setOpen_account(apicron.getOpenAccount());
		} else {
			/* sm.setOpen_account(3); */
			sm.setOpen_account(4);
		}

		return smsCodeCustomizeMapper.queryUser(sm);
	}

}
