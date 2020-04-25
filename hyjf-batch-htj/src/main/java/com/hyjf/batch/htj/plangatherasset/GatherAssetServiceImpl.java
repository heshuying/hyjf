package com.hyjf.batch.htj.plangatherasset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanBorrowCustomize;

@Service
public class GatherAssetServiceImpl extends BaseServiceImpl implements GatherAssetService {

	
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	@Autowired
    @Qualifier("mailProcesser")
    private MessageProcesser mailMessageProcesser;
	/**
	 * 查询相应的两天未满标的汇天金专属标的
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public List<BatchDebtPlanBorrowCustomize> selectUnFullDebtBorrow(int num) {
		
		List<BatchDebtPlanBorrowCustomize> planBorrows = batchDebtPlanBorrowCustomizeMapper.selectDebtPlanBorrowUnfull(num);
		return planBorrows;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @author Administrator
	 */
		
	@Override
	public void sendSms(String borrowNid) {
		// 发送短信
		Map<String, String> replaceStrs = new HashMap<String, String>();
        replaceStrs.put("val_title", borrowNid);
        SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_JYTZ_HTJ_ZCBFMJ, CustomConstants.CHANNEL_TYPE_NORMAL);
        smsProcesser.gather(smsMessage);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowNid
	 * @author Administrator
	 */
		
	@Override
	public void sendEmail(String borrowNid) {
		// 发送邮件
		Map<String, String> replaceStrs = new HashMap<String, String>();
	    replaceStrs.put("val_title", borrowNid);
        // 取得是否线上
        String online = "生产环境";
        String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
        if (payUrl == null || !payUrl.contains("online")) {
            online = "测试环境";
        }
        String[] toMail = new String[] {};
        if ("测试环境".equals(online)) {
            toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
        } else {
            toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com","sunjianhua@hyjf.com"};
        }
        MailMessage smsMessage = new MailMessage(null, replaceStrs, "[" + online + "] " + "资产部分募集警报", null, null, toMail, CustomConstants.EMAILPARAM_TPL_JYTZ_HTJ_ZCBFMJ,MessageDefine.MAILSENDFORMAILINGADDRESS);
        mailMessageProcesser.gather(smsMessage);
	}
	
}
