package com.hyjf.batch.hjh.borrow.planorderexception;

import java.util.List;

import com.hyjf.common.util.PropUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.mapper.auto.HjhAccedeMapper;
import com.hyjf.mybatis.mapper.auto.SmsLogMapper;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 计划订单出借异常，消息通知
 * 
 * @author wxh
 * 
 */
@Service
public class PlanOrderExceptionTaskServiceImpl implements PlanOrderExceptionTaskService {

	@Autowired
	private SmsLogMapper smsLogMapper;
	@Autowired
	protected HjhAccedeMapper hjhAccedeMapper;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	Logger _log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Override
	public void sendMsgToNotFullBorrow() throws Exception {
		// 计划订单出借异常，消息通知
		HjhAccedeExample hjhAccedeExample = new HjhAccedeExample();
		hjhAccedeExample.createCriteria().andOrderStatusGreaterThanOrEqualTo(80);
		List<HjhAccede> accedeList = hjhAccedeMapper.selectByExample(hjhAccedeExample);
		// 判断结果如果不为空,就给相应的工作人员发送短信和邮件通知
        _log.info(" 计划订单出借异常，消息通知 accedtList.size() = " + (CollectionUtils.isEmpty(accedeList) ? 0 : accedeList.size()));
		if (accedeList != null && accedeList.size() > 0) {
			_log.info("size=" + accedeList.size());
			// 发送邮件通知
			String online = "计划订单出借异常消息通知";//
			StringBuffer msg = new StringBuffer();
			msg.append("计划订单出借异常，请至网站后台“异常出借-汇计化自动出借异常”查看并及时处理，谢谢");

			Boolean env_test = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
			_log.info("计划订单出借异常 evn_test is test ? " + env_test);
			String emailList= "";
			if (env_test){
				emailList = PropUtils.getSystem("hyjf.alerm.email.test");
			}else{
				emailList = PropUtils.getSystem("hyjf.alerm.email");
			}
			String [] toMail = emailList.split(",");
			MailMessage mailmessage = new MailMessage(null, null, online, msg.toString(), null, toMail, null,
					MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
			mailMessageProcesser.gather(mailmessage);
			// 发送短信通知
			SmsMessage smsMessage = new SmsMessage(null, null, null, null, MessageDefine.SMSSENDFORMANAGER, null,
					CustomConstants.JYTZ_PLAN_ORDER_EXCECEPTION, CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
			_log.info("短信发送成功");
		}else {
		    _log.info("计划订单出借异常size为空, 不发送预警");
        }
	}

}
