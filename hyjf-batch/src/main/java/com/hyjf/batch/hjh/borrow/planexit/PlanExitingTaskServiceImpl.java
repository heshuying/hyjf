package com.hyjf.batch.hjh.borrow.planexit;

import com.hyjf.batch.hjh.borrow.ordermatch.PlanOrdeeMatchingPeriodServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.auto.HjhRepayMapper;
import com.hyjf.mybatis.mapper.auto.SmsLogMapper;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.auto.HjhRepayExample;
import com.hyjf.mybatis.model.auto.HjhRepayExample.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class PlanExitingTaskServiceImpl implements PlanExitingTaskService {

	@Autowired
	private SmsLogMapper smsLogMapper;
	Logger _log = LoggerFactory.getLogger(PlanOrdeeMatchingPeriodServiceImpl.class);
    @Autowired
    protected HjhRepayMapper hjhRepayMapper;
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	@Override
	public void sendMsgToNotFullBorrow() throws Exception {
		//计划退出时间大约2
		
		HjhRepayExample hjhRepayExample = new HjhRepayExample();
		//获取当前的时间减去两天的时间就是前天的时间
		int nowTime1 = GetDate.getNowTime10()-172800;
		Criteria createCriteria = hjhRepayExample.createCriteria();
		createCriteria.andOrderStatusEqualTo(5);
		createCriteria.andRepayShouldTimeLessThanOrEqualTo(nowTime1);
		List<HjhRepay> repayList = hjhRepayMapper.selectByExample(hjhRepayExample);
		//判断结果并发邮件
		String online = "计划退出中时间>=2消息通知";// 取得是否线上
		_log.info("计划退出中时间>=2，消息通知 repayList.size() = " + (CollectionUtils.isEmpty(repayList) ? 0 : repayList.size()));
		StringBuffer msg = new StringBuffer();
		if (repayList!= null && repayList.size() > 0) {
			for (int i=0;i<repayList.size();i++) {
				String orderId = repayList.get(i).getAccedeOrderId();
				msg.append("'");
				msg.append(orderId);
				msg.append("'");
				if (i % 3 == 0){
					msg.append("\r\n");
				}
			}
			msg.append("该计划订单退出中已大于两天，请相关人员至后台查看并及时处理，谢谢");
			Boolean env_test = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
			_log.info("计划退出中时间>=2 evn_test is test ? " + env_test);
			String emailList= "";
			if (env_test){
				emailList = PropUtils.getSystem("hyjf.alerm.email.test");
			}else{
				emailList = PropUtils.getSystem("hyjf.alerm.email");
			}
			String [] toMail = emailList.split(",");
			MailMessage mailmessage = new MailMessage(null, null,online, msg.toString(), null, toMail, null,MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
			mailMessageProcesser.gather(mailmessage);
			_log.info("邮件发送完成");
		}else {
			_log.info("计划退出中时间>=2 size为空,不发送预警");
		}
	}

	

}
