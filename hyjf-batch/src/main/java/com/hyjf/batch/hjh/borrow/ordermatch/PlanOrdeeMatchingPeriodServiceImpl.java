package com.hyjf.batch.hjh.borrow.ordermatch;

import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.auto.HjhAccedeMapper;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 计划订单匹配时间
 * 
 * @author wxh
 */
@Service
public class PlanOrdeeMatchingPeriodServiceImpl implements PlanOrdeeMatchingPeriodService {


	Logger _log = LoggerFactory.getLogger(PlanOrdeeMatchingPeriodServiceImpl.class);
    @Autowired
    protected HjhAccedeMapper hjhAccedeMapper;
    
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	@Override
	public void sendMsgToNotFullBorrow() throws Exception {
		// 计划订单进入匹配期时间超过2天给工作人员发送预警短信
		HjhAccedeExample hjhAccedeExample = new HjhAccedeExample();
		HjhAccedeExample.Criteria criteria = hjhAccedeExample.createCriteria();
		criteria.andMatchDatesGreaterThanOrEqualTo(2);
		criteria.andOrderStatusIn(Arrays.asList(0,2));  // 订单状态处于自动出借中和自动出借成功(0,2) 2018年6月27日14:16:05
		List<HjhAccede> accedeList = hjhAccedeMapper.selectByExample(hjhAccedeExample);
        _log.info(" 订单进入匹配期时间超过2天，消息通知 accedtList.size() = " + (CollectionUtils.isEmpty(accedeList) ? 0 : accedeList.size()));
		if (accedeList!= null && accedeList.size() > 0) {
			_log.info("获取到的size = "+ accedeList.size());
			StringBuffer msg = new StringBuffer();
			String online = "计划订单匹配期>=2消息通知";
			for (int i=0;i<accedeList.size();i++) {
				String orderId = accedeList.get(i).getAccedeOrderId();
				msg.append("'");
				msg.append(orderId);
				msg.append("',");
				if (i % 3 == 0){
					msg.append("\r\n");
				}
			}
			msg.append("该计划订单匹配期已大于两天，请相关人员至后台查看并及时处理，谢谢");
			Boolean env_test = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
			_log.info("订单进入匹配期时间超过2天 evn_test is test ? " + env_test);
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
		}else{
            _log.info("计划订单匹配期>=2 size为空,不发送预警");
        }
	}
	

}
