package com.hyjf.admin.message;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.SmsOntimeWithBLOBs;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.SmsCodeCustomize;

@Service
public class SmsCodeServiceImpl extends BaseServiceImpl implements SmsCodeService {

	@Override
	public List<SmsCodeCustomize> queryUser(SmsCodeBean msb) {
		SmsCodeCustomize sm = new SmsCodeCustomize();
		if (StringUtils.isNotEmpty(msb.getAdd_money_count())) {
			sm.setAdd_money_count(new BigDecimal(msb.getAdd_money_count()));
		}

		if (StringUtils.isNotEmpty(msb.getAdd_time_begin())) {
			int begin = Integer.parseInt(GetDate.get10Time(msb.getAdd_time_begin()));
			sm.setAdd_time_begin(begin);
		}

		if (StringUtils.isNotEmpty(msb.getAdd_time_end())) {
			int end = Integer.parseInt(GetDate.get10Time(msb.getAdd_time_end()));
			sm.setAdd_time_end(end);
		}

		if (StringUtils.isNotEmpty(msb.getRe_time_begin())) {
			int re_begin = Integer.parseInt(GetDate.get10Time(msb.getRe_time_begin()));
			sm.setRe_time_begin(re_begin);
		}

		if (StringUtils.isNotEmpty(msb.getRe_time_end())) {
			int re_end = Integer.parseInt(GetDate.get10Time(msb.getRe_time_end()));
			sm.setRe_time_end(re_end);
		}
		if (msb.getOpen_account() != null) {
			sm.setOpen_account(msb.getOpen_account());
		} else {
			// 2017-1-18modify by 周小帅 去掉默认选择所有用户
			/* sm.setOpen_account(3); */
			sm.setOpen_account(4);
		}

		return smsCodeCustomizeMapper.queryUser(sm);
	}

	@Override
	public Integer queryUserIdByPhone(String mobile) {
		return smsCodeCustomizeMapper.queryUserIdByPhone(mobile);
	}

	@Override
	public boolean sendSmsOntime(SmsCodeBean form) throws NumberFormatException, ParseException {
		SmsOntimeWithBLOBs smsOntime = new SmsOntimeWithBLOBs();
		smsOntime.setOpenAccount(form.getOpen_account());
		smsOntime.setChannelType(form.getChannelType());
		smsOntime.setMobile(form.getUser_phones());
		smsOntime.setContent(form.getMessage());
		smsOntime.setStatus(0);
		smsOntime.setOpenAccount(form.getOpen_account());
		if (StringUtils.isNotEmpty(form.getAdd_money_count())) {
			smsOntime.setAddMoneyCount(new BigDecimal(form.getAdd_money_count()));

		}
		if (StringUtils.isNotEmpty(form.getAdd_time_begin())) {
			smsOntime.setAddTimeBegin(form.getAdd_time_begin());
		}

		if (StringUtils.isNotEmpty(form.getAdd_time_end())) {
			smsOntime.setAddTimeEnd(form.getAdd_time_end());
		}

		if (StringUtils.isNotEmpty(form.getRe_time_begin())) {
			smsOntime.setReTimeBegin(form.getRe_time_begin());
		}

		if (StringUtils.isNotEmpty(form.getRe_time_end())) {
			smsOntime.setReTimeEnd(form.getRe_time_end());
		}
		smsOntime.setEndtime(Integer.parseInt((GetDate.datetimeFormat.parse(form.getOn_time()).getTime() / 1000) + ""));
		smsOntime.setIp(form.getIp());
		smsOntime.setCreateUserId(Integer.parseInt(ShiroUtil.getLoginUserId()));
		smsOntime.setCreateUserName(ShiroUtil.getLoginUsername());
		smsOntime.setCreateTime(GetDate.getNowTime10());
		return smsOntimeMapper.insertSelective(smsOntime) > 0 ? true : false;
	}

	@Override
	public boolean getUserByMobile(String mobile) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cri = example.createCriteria();
		cri.andMobileEqualTo(mobile);
		return usersMapper.countByExample(example) > 0 ? true : false;
	}
}
