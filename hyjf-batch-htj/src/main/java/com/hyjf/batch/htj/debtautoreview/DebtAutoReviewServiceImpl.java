package com.hyjf.batch.htj.debtautoreview;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.htj.debtautoreview.enums.BorrowSendTypeEnum;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.SmsLogMapper;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.messageprocesser.impl.SmsUtil;
import com.hyjf.mybatis.model.auto.BorrowSendType;
import com.hyjf.mybatis.model.auto.BorrowSendTypeExample;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.SmsLogExample;
import com.hyjf.mybatis.model.auto.SmsNoticeConfigWithBLOBs;
import com.hyjf.mybatis.model.customize.batch.BatchDebtBorrowCommonCustomize;

@Service
public class DebtAutoReviewServiceImpl extends BaseServiceImpl implements DebtAutoReviewService {
	@Autowired
	private SmsLogMapper smsLogMapper;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Override
	public void sendMsgToNotFullBorrow() throws Exception {
		// 查询符合条件的Borrow
		List<BatchDebtBorrowCommonCustomize> borrowList = batchDebtBorrowCustomizeMapper.searchNotFullBorrowMsg();
		if (borrowList != null && borrowList.size() > 0) {
			SmsNoticeConfigWithBLOBs smsNoticeConfig = SmsUtil.getNoticeConfig(CustomConstants.PARAM_TPL_XMDQ);
			// 发送短信
			for (int i = 0; i < borrowList.size(); i++) {
				SmsLogExample example = new SmsLogExample();
				SmsLogExample.Criteria cri = example.createCriteria();
				cri.andSenderEqualTo(borrowList.get(i).getBorrowNid());
				cri.andTypeEqualTo(smsNoticeConfig.getTitle());
				int smsLogSize = smsLogMapper.countByExample(example);
				if (smsLogSize == 0) {
					// 替换参数
					Map<String, String> messageStrMap = new HashMap<String, String>();
					messageStrMap.put("val_borrowid", borrowList.get(i).getBorrowNid());
					messageStrMap.put("val_date", borrowList.get(i).getTimeStartSrch());
					// 发送短信
					SmsMessage smsMessage = new SmsMessage(null, messageStrMap, null, null, MessageDefine.SMSSENDFORMANAGER, borrowList.get(i).getBorrowNid(), CustomConstants.PARAM_TPL_XMDQ, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
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
	public void updateDebtBorrow(Integer afterTime) throws Exception {
		// 获取当前时间
		int nowTime = GetDate.getNowTime10();
		List<DebtBorrowWithBLOBs> borrowList = batchDebtBorrowFullCustomizeMapper.selectAutoFullList(new BatchDebtBorrowCommonCustomize());
		if (borrowList != null && borrowList.size() > 0) {
			for (DebtBorrowWithBLOBs borrow : borrowList) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("borrow_nid", borrow.getBorrowNid());
				// 根据计划编号检索是否有清算中的计划
				Integer count = this.debtBorrowFullCustomizeMapper.countDebtInvestListByBorrowNid(params);
				// 自动放款时,没有正在清算的计划
				if (count == 0) {
					try {
						// 判断满标时间
						if (borrow.getBorrowFullTime() != null && borrow.getBorrowFullTime() != 0) {
							// 满标时间判断
							if (borrow.getBorrowFullTime() + afterTime * 60 < nowTime) {
								// 更新huiyingdai_borrow的如下字段：
								// 复审时间
								borrow.setReverifyTime(String.valueOf(nowTime));
								// 复审人ID
								borrow.setReverifyUserid("Auto");
								// 复审状态
								borrow.setReverifyStatus(3);
								// 复审状态（复审通过）
								borrow.setStatus(3);
								// 满标审核状态
								borrow.setBorrowFullStatus(1);
								// 2015年12月18日16:15:05 新加的更新字段
								borrow.setCommentStaus(1);
								// 更新时间
								borrow.setUpdatetime(new Date());
								// 复审成功时间
								borrow.setBorrowSuccessTime(nowTime);
								// 更新Remark
								borrow.setReverifyRemark("系统自动复审通过");
								boolean borrowFlag = this.debtBorrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
								if (borrowFlag) {
									String nid = borrow.getBorrowNid() + "_" + borrow.getUserId() + "_1";
									// 放款任务表
									DebtApicron borrowApicron = new DebtApicron();
									// 交易凭证号 生成规则：BorrowNid_userid_期数
									borrowApicron.setNid(nid);
									// 借款人编号
									borrowApicron.setUserId(borrow.getUserId());
									// 借款编号
									borrowApicron.setBorrowNid(borrow.getBorrowNid());
									// Status
									borrowApicron.setStatus(0);
									// ApiType
									borrowApicron.setApiType(0);
									// 创建时间
									borrowApicron.setCreateTime(nowTime);
									// 更新时间
									borrowApicron.setUpdateTime(nowTime);
									// 汇租赁当前期数
									borrowApicron.setPeriodNow(0);
									// 债转还款状态
									borrowApicron.setCreditRepayStatus(0);
									boolean apicronFlag = this.debtApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
									if (!apicronFlag) {
										throw new Exception("更新borrow表失败,项目编号：" + borrow.getBorrowNid());
									}
								} else {
									throw new Exception("更新borrow表失败,项目编号：" + borrow.getBorrowNid());
								}
							}
						} else {
							throw new Exception("borrow的borrowFullTime字段为空");
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
	}
}
