package com.hyjf.batch.hjh.borrow.autoreview;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.SmsLogMapper;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.messageprocesser.impl.SmsUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatchHjhAutoReviewServiceImpl extends BaseServiceImpl implements BatchHjhAutoReviewService {

	@Autowired
	private SmsLogMapper smsLogMapper;


	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

	@Override
	public void sendMsgToNotFullBorrow() throws Exception {
		// 查询符合条件的Borrow
		List<BorrowCommonCustomize> borrowList = borrowCustomizeMapper.searchNotFullBorrowMsg();
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
					SmsMessage smsMessage = new SmsMessage(null, messageStrMap, null, null, MessageDefine.SMSSENDFORMANAGER, borrowList.get(i).getBorrowNid(), CustomConstants.PARAM_TPL_XMDQ,
							CustomConstants.CHANNEL_TYPE_NORMAL);
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
	public void updateBorrow(BorrowWithBLOBs borrow) throws Exception {		
		
		/*--------------upd by liushouyi HJH3 Start----------------*/
		//遍历自动发标的数据原抽出表中所有字段、表关联后仅查借款编号
		borrow = this.getBorrowByNid(borrow.getBorrowNid());
		/*--------------upd by liushouyi HJH3 Start----------------*/
		// 获取当前时间
		int nowTime = GetDate.getNowTime10();
		String borrowNid = borrow.getBorrowNid();// 項目编号
		int borrowUserId = borrow.getUserId();// 借款人userId
		String borrowUserName = borrow.getBorrowUserName();// 借款人用户名
		String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
		/** 标的基本数据 */
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 借款期数
		// 项目类型
		Integer projectType = borrow.getProjectType();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		
		// 获取标的费率信息  后续放开
//		String borrowClass = this.getBorrowProjectClass(borrow.getProjectType()+ "");
//		BorrowFinmanNewCharge borrowFinmanNewCharge = this.selectBorrowApr(borrowClass,borrow.getInstCode(),borrow.getAssetType(),borrow.getBorrowStyle(),borrow.getBorrowPeriod());
//		if(borrowFinmanNewCharge == null || borrowFinmanNewCharge.getChargeMode() == null){
//			_log.info("获取标的费率信息失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]"+borrowClass);
//			throw new RuntimeException("获取标的费率信息失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
//		}
		
		// 取得借款人账户信息
		Account borrowAccount = this.getAccountByUserId(borrowUserId);
		if (borrowAccount == null) {
			throw new RuntimeException("借款人账户不存在。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
		}
		// 借款人在汇付的账户信息
		BankOpenAccount borrowerAccount = this.getBankOpenAccount(borrowUserId);
		if (borrowerAccount == null) {
			throw new RuntimeException("借款人未开户。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
		}
		String nid = borrow.getBorrowNid() + "_" + borrow.getUserId() + "_1";
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria crt = example.createCriteria();
		crt.andNidEqualTo(nid);
		crt.andApiTypeEqualTo(0);
		List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
		if (borrowApicrons == null || borrowApicrons.size() == 0) {
			// 满标审核状态
			int borrowFullStatus = borrow.getBorrowFullStatus();
			if (borrowFullStatus == 1) {
				// 如果标的出借记录存在没有授权码的记录，则不进行放款
				int countErrorTender = this.countBorrowTenderError(borrowNid);
				if (countErrorTender == 0) {
					// 判断满标时间
					if (borrow.getBorrowFullTime() != null && borrow.getBorrowFullTime() != 0) {
						// 满标时间判断
						BorrowExample borrowExample = new BorrowExample();
						borrowExample.createCriteria().andIdEqualTo(borrow.getId()).andReverifyStatusEqualTo(borrow.getReverifyStatus());
						// 更新huiyingdai_borrow的如下字段：
						borrow.setReverifyTime(String.valueOf(nowTime));// 复审时间
						borrow.setReverifyUserid("Auto"); // 复审人userId
						borrow.setReverifyUserName("Auto");// 复审人用戶名
						borrow.setReverifyStatus(1);// 复审状态
						borrow.setStatus(3);// 复审状态（复审通过）
						borrow.setCommentStaus(1);
						borrow.setReverifyRemark("系统自动复审通过");// 更新Remark
						borrow.setBorrowSuccessTime(nowTime);// 复审成功时间
						borrow.setUpdatetime(new Date());// 更新时间
						boolean borrowUpdateFlag = this.borrowMapper.updateByExampleSelective(borrow, borrowExample)> 0 ? true : false;
						if (borrowUpdateFlag) {
							// 放款任务表
							BorrowApicron borrowApicron = new BorrowApicron();
							borrowApicron.setNid(nid);// 交易凭证号 生成规则：BorrowNid_userid_期数
							borrowApicron.setUserId(borrowUserId); // 借款人编号
							borrowApicron.setUserName(borrowUserName);// 用户名
							borrowApicron.setBorrowNid(borrow.getBorrowNid()); // 借款编号
							borrowApicron.setBorrowAccount(borrow.getAccount());
							if(!isMonth){
								borrowApicron.setBorrowPeriod(1);
							}else{
								borrowApicron.setBorrowPeriod(borrowPeriod);
							}
							borrowApicron.setFailTimes(0);
							borrowApicron.setStatus(1);// 放款任务状态 0初始
							borrowApicron.setApiType(0);// 任务类型放款
							borrowApicron.setPeriodNow(0);// 放款期数
							borrowApicron.setCreditRepayStatus(0);// 债转还款状态
							if(projectType==13){
								borrowApicron.setExtraYieldStatus(0);// 融通宝加息相关的放款状态
								borrowApicron.setExtraYieldRepayStatus(0);// 融通宝相关的加息还款状态
							}else{
								borrowApicron.setExtraYieldStatus(1);// 融通宝加息相关的放款状态
								borrowApicron.setExtraYieldRepayStatus(1);// 融通宝相关的加息还款状态
							}
							borrowApicron.setCreateTime(nowTime);// 创建时间
							borrowApicron.setUpdateTime(nowTime);// 更新时间 
							borrowApicron.setPlanNid(borrow.getPlanNid());//计划编号
							boolean apicronFlag = this.borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
							if (!apicronFlag) {
								throw new Exception("更新borrow表失败,项目编号：" + borrow.getBorrowNid());
							}
						
						} else {
							throw new Exception("更新borrow表失败,项目编号：" + borrow.getBorrowNid());
						}
					} else {
						throw new Exception("borrow的borrowFullTime字段为空");
					}
				}
			}
		}
}


	private Account getAccountByUserId(int userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	@Override
	public List<BorrowWithBLOBs> selectAutoReview() {
		BorrowExample  example = new BorrowExample();
		example.createCriteria().andStatusEqualTo(3).andBorrowFullStatusEqualTo(1).andPlanNidIsNotNull();
		List<BorrowWithBLOBs> borrowList = borrowMapper.selectByExampleWithBLOBs(example);
		return borrowList;
	}

	/**
	 * 取得借款计划信息
	 *
	 * @return
	 */
	public BorrowRepayPlan getBorrowRepayPlan(String borrowNid, Integer period) {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayPeriodEqualTo(period);
		List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 校验出借数据的合法性
	 * @param borrowNid
	 * @return
	 */
	private int countBorrowTenderError(String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andApiStatusEqualTo(0);
		criteria.andAuthCodeIsNull();
		int count = this.borrowTenderMapper.countByExample(example);
		return count;
	}


	/**
	 * 项目类型
	 * 
	 * @return
	 * @author Administrator
	 */
	private String getBorrowProjectClass(String borrowCd) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		cra.andBorrowCdEqualTo(borrowCd);

		List<BorrowProjectType> list = this.borrowProjectTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getBorrowClass();
		}
		return "";
	}

	/**
	 * 根据项目类型，期限，获取借款利率
	 * 
	 * @return
	 */
	private BorrowFinmanNewCharge selectBorrowApr(String projectType,String instCode, Integer instProjectType, String borrowStyle,Integer chargetime) {
		BorrowFinmanNewChargeExample example = new BorrowFinmanNewChargeExample();
		BorrowFinmanNewChargeExample.Criteria cra = example.createCriteria();
		cra.andProjectTypeEqualTo(projectType);
		cra.andInstCodeEqualTo(instCode);
		cra.andAssetTypeEqualTo(instProjectType);
		cra.andManChargeTimeTypeEqualTo(borrowStyle);
		cra.andManChargeTimeEqualTo(chargetime);
		cra.andStatusEqualTo(0);
		
		List<BorrowFinmanNewCharge> list = this.borrowFinmanNewChargeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
		
	}

}
