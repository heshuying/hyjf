package com.hyjf.admin.msgpush.history;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistoryExample;

@Service
public class MessagePushHistoryServiceImpl extends BaseServiceImpl implements MessagePushHistoryService {

	@Override
	public Integer getRecordCount(MessagePushHistoryBean form) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
		MessagePushMsgHistoryExample.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(form.getHistoryTagIdSrch())) {
			criteria.andTagIdEqualTo(Integer.valueOf(form.getHistoryTagIdSrch()));
		}
		if (StringUtils.isNotEmpty(form.getHistoryTitleSrch())) {
			criteria.andMsgTitleLike("%"+form.getHistoryTitleSrch()+"%");
		}
		if (StringUtils.isNotEmpty(form.getHistoryCodeSrch())) {
			criteria.andMsgCodeLike("%"+form.getHistoryCodeSrch()+"%");
		}
		if (StringUtils.isNotEmpty(form.getHistoryCreateUserNameSrch())) {
		//	criteria.andCreateUserNameLike(form.getHistoryCreateUserNameSrch());
			criteria.andMsgDestinationLike("%"+form.getHistoryCreateUserNameSrch()+"%");
		}
		if (StringUtils.isNotEmpty(form.getHistoryTerminalSrch())) {
			criteria.andMsgTerminalLike(form.getHistoryTerminalSrch());
		}
		if (form.getHistorySendStatusSrch() != null) {
			criteria.andMsgSendStatusEqualTo(form.getHistorySendStatusSrch());
		}
		if (StringUtils.isNotEmpty(form.getStartSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getHistoryCreateUserNameSrch());
				criteria.andSendTimeGreaterThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotEmpty(form.getEndSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getEndSendTimeSrch());
				criteria.andSendTimeLessThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		if (form.getHistoryFirstReadTerminalSrch() != null) {
			try {
				criteria.andMsgFirstreadPlatEqualTo(Integer.parseInt(form.getHistoryFirstReadTerminalSrch()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		criteria.andMsgSendStatusNotEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_0);
		return this.messagePushMsgHistoryMapper.countByExample(example);
	}

	@Override
	public List<MessagePushMsgHistory> getRecordList(MessagePushHistoryBean form, int limitStart, int limitEnd) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
		MessagePushMsgHistoryExample.Criteria criteria = example.createCriteria();
		// 条件查询
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		if (StringUtils.isNotEmpty(form.getHistoryTagIdSrch())) {
			criteria.andTagIdEqualTo(Integer.valueOf(form.getHistoryTagIdSrch()));
		}
		if (StringUtils.isNotEmpty(form.getHistoryTitleSrch())) {
			criteria.andMsgTitleLike("%"+form.getHistoryTitleSrch()+"%");
		}
		if (StringUtils.isNotEmpty(form.getHistoryCodeSrch())) {
			criteria.andMsgCodeLike("%"+form.getHistoryCodeSrch()+"%");
		}
		if (StringUtils.isNotEmpty(form.getHistoryCreateUserNameSrch())) {
			criteria.andMsgDestinationLike("%"+form.getHistoryCreateUserNameSrch()+"%");
		}
		if (StringUtils.isNotEmpty(form.getHistoryTerminalSrch())) {
			criteria.andMsgTerminalLike(form.getHistoryTerminalSrch());
		}
		if (form.getHistorySendStatusSrch() != null) {
			criteria.andMsgSendStatusEqualTo(form.getHistorySendStatusSrch());
		}
		if (StringUtils.isNotEmpty(form.getStartSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getHistoryCreateUserNameSrch());
				criteria.andSendTimeGreaterThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotEmpty(form.getEndSendTimeSrch())) {
			try {
				Integer time = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getEndSendTimeSrch());
				criteria.andSendTimeLessThanOrEqualTo(time);
			} catch (Exception e) {
			}
		}
		if (form.getHistoryFirstReadTerminalSrch() != null) {
			try {
				criteria.andMsgFirstreadPlatEqualTo(Integer.parseInt(form.getHistoryFirstReadTerminalSrch()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		criteria.andMsgSendStatusNotEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_0);
		example.setOrderByClause("create_time DESC");
		return this.messagePushMsgHistoryMapper.selectByExample(example);
	}

}
