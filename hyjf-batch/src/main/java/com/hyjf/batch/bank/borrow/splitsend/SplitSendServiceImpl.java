package com.hyjf.batch.bank.borrow.splitsend;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SplitSendServiceImpl extends BaseServiceImpl implements SplitSendService {
	
	Logger _log = LoggerFactory.getLogger(SplitSendServiceImpl.class);

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 
	 * @param tplname
	 * @return
	 */
	public String queryMobiles(String tplname) {
		String mobiles = this.ontimeTenderCustomizeMapper.queryMobiles(tplname);
		return mobiles;
	}

	/**
	 * 自动发标（下一个标的）
	 * 
	 * @param borrow
	 * @param nowTime
	 */
	@Override
	public boolean updateFireBorrow(BorrowWithBLOBs borrow, long nowTime) {

		// 借款到期时间
		borrow.setBorrowEndTime(String.valueOf(nowTime + borrow.getBorrowValidTime() * 86400));
		// 状态
		borrow.setStatus(2);
		// 是否满标
		borrow.setBorrowFullStatus(0);
		// 初审通过时间
		borrow.setVerifyTime(String.valueOf(nowTime));
		// 初审备注
		borrow.setVerifyRemark("自动发标(初审)");
		// 初审人员
		borrow.setVerifyUserid("auto");
		// 初审用户
		borrow.setVerifyUserName("auto");
		// 剩余可出借金额
		borrow.setBorrowAccountWait(borrow.getAccount());
		boolean fireFlag = this.borrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
		if (fireFlag) {
			if ("0".equals(borrow.getIsEngineUsed())) {
				// borrowNid，借款的borrowNid,account借款总额
				if (RedisUtils.get(borrow.getBorrowNid())!= null) {
					_log.info(borrow.getBorrowNid()+" 拆分标自动发标异常：redis已经存在");
					throw new RuntimeException("拆分标自动发标异常，redis已经存在 标号：" + borrow.getBorrowNid());
				}

				RedisUtils.set(String.valueOf(borrow.getBorrowNid()), String.valueOf(borrow.getAccount()));
				// 短信参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("val_title", borrow.getBorrowNid());
				SmsMessage smsMessage = new SmsMessage(null, params, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_ZDFB, CustomConstants.CHANNEL_TYPE_NORMAL);
				smsProcesser.gather(smsMessage);
				_log.info("定时发标自动任务 "+String.valueOf(borrow.getBorrowNid()) + "标的已经自动发标！");
			} else if ("1".equals(borrow.getIsEngineUsed())){
				// 进计划的拆分标发送加入计划消息队列
				this.sendToMQ(borrow,  RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
			}
			return true;
		} else {
			throw new RuntimeException("自动发标失败，标号：" + borrow.getBorrowNid());
		}
	}

	public void sendToMQ(Borrow borrow, String routingKey){
		// 加入到消息队列
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("borrowNid", borrow.getBorrowNid());
		params.put("instCode", borrow.getInstCode());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
	}

	/**
	 * 获取下一个借款编号
	 * 
	 * @param borrowNidMain
	 * @return
	 */
	@Override
	public String getNextBorrowNid(String borrowNidMain) {
		DecimalFormat df = new DecimalFormat("00");
		return borrowNidMain.substring(0, borrowNidMain.length() - 2) + df.format((Integer.valueOf(borrowNidMain.substring(borrowNidMain.length() - 2, borrowNidMain.length())) + 1));
	}

	@Override
	public List<BorrowWithBLOBs> queryAllSplitSend() {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andStatusEqualTo(1).andVerifyStatusEqualTo(2);
		List<BorrowWithBLOBs> borrows = this.borrowMapper.selectByExampleWithBLOBs(example);
		return borrows;
	}

	@Override
	public Integer getAfterTime(BorrowSendTypeEnum BorrowSendType) throws Exception {
		BorrowSendTypeExample sendTypeExample = new BorrowSendTypeExample();
		BorrowSendTypeExample.Criteria sendTypeCriteria = sendTypeExample.createCriteria();
		sendTypeCriteria.andSendCdEqualTo(BorrowSendType.getValue());
		List<BorrowSendType> sendTypeList = borrowSendTypeMapper.selectByExample(sendTypeExample);
		if (sendTypeList == null || sendTypeList.size() == 0) {
			throw new Exception("数据库查不到" + BorrowSendType.class);
		}
		BorrowSendType sendType = sendTypeList.get(0);
		if (sendType.getAfterTime() == null) {
			throw new Exception("sendType.getAfterTime()==null");
		}
		return sendType.getAfterTime();
	}

	@Override
	public BorrowWithBLOBs getPreBorrow(String borrowPreNid, String borrowPreNidNew) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowPreNidEqualTo(borrowPreNid);
		crt.andBorrowPreNidNewLessThan(borrowPreNidNew);
		// HJH3 进计划的拆分标的抽出
//		crt.andIsEngineUsedEqualTo(0);
		example.setOrderByClause("borrow_pre_nid_new DESC");
		example.setLimitStart(0);
		example.setLimitEnd(1);
		List<BorrowWithBLOBs> borrows = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (borrows != null && borrows.size() == 1) {
			return borrows.get(0);
		}
		return null;
	}

}
