package com.hyjf.batch.bank.borrow.autoreview;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.SmsLogMapper;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.messageprocesser.impl.SmsUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatchAutoReviewServiceImpl extends BaseServiceImpl implements BatchAutoReviewService {

	private Logger _log = LoggerFactory.getLogger(BatchAutoReviewServiceImpl.class);
	@Autowired
	private SmsLogMapper smsLogMapper;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	@Resource(name="myAmqpTemplate")
    AmqpTemplate amqpTemplate;
	
    public void sendMessage(String key,Object message){
        String msgString = JSONObject.toJSONString(message);
        amqpTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME,key,msgString);
    }

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
	public void updateBorrow(BorrowWithBLOBs borrow, Integer afterTime) throws Exception {

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
		// 借款人ID
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		
		// 获取标的费率信息
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
		//add by cwyang 校验标的是否爆标
		boolean result = this.checkBorrowTenderOverFlow(borrowNid);
		if (result) {
			_log.info("标的爆标,请进行处理![用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
			//throw new RuntimeException("标的爆标,请进行处理![用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
		}
		//end
		String nid = borrow.getBorrowNid() + "_" + borrow.getUserId() + "_1";
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria crt = example.createCriteria();
		crt.andNidEqualTo(nid);
		crt.andApiTypeEqualTo(0);
		List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
		if ((borrowApicrons == null || borrowApicrons.size() == 0) && !result) {
			// 满标审核状态
			int borrowFullStatus = borrow.getBorrowFullStatus();
			if (borrowFullStatus == 1) {
				// 如果标的出借记录存在没有授权码的记录，则不进行放款
				int countErrorTender = this.countBorrowTenderError(borrowNid);
				if (countErrorTender == 0) {
					// 判断满标时间
					if (borrow.getBorrowFullTime() != null && borrow.getBorrowFullTime() != 0) {
						// 满标时间判断
						if (borrow.getBorrowFullTime() + afterTime * 60 < nowTime) {
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
								//add by cwyang 20180730 加息需求变更
								boolean increase = Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield());
								if(increase){
									borrowApicron.setExtraYieldStatus(0);// 融通宝加息相关的放款状态
									borrowApicron.setExtraYieldRepayStatus(0);// 融通宝相关的加息还款状态
								}else{
									borrowApicron.setExtraYieldStatus(1);// 融通宝加息相关的放款状态
									borrowApicron.setExtraYieldRepayStatus(1);// 融通宝相关的加息还款状态
								}
								borrowApicron.setCreateTime(nowTime);// 创建时间
								borrowApicron.setUpdateTime(nowTime);// 更新时间
								boolean apicronFlag = this.borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
								if (!apicronFlag) {
									throw new Exception("更新borrow表失败,项目编号：" + borrow.getBorrowNid());
								}
							}
						}
					} else {
						throw new Exception("borrow的borrowFullTime字段为空");
					}
					
				}
			}
		}
	}

	/**
	 * 校验出借数据是否爆标
	 * @param borrowNid
	 * @return true:爆标/异常  false:正常
	 */
	private boolean checkBorrowTenderOverFlow(String borrowNid) {
		//校验出借金额是否超标
				BorrowTenderExample btexample = new BorrowTenderExample();
				btexample.createCriteria().andBorrowNidEqualTo(borrowNid);
				List<BorrowTender> btList = this.borrowTenderMapper.selectByExample(btexample);
				BigDecimal sumTender = new BigDecimal(0);
				if (btList != null && btList.size() > 0) {
					for (int i = 0; i < btList.size(); i++) {
						sumTender = sumTender.add(btList.get(i).getAccount());
					}
				}
				BorrowExample borrowExample = new BorrowExample();
				borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
				List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
				BigDecimal borrowAccount = borrowList.get(0).getAccount();
				if (sumTender.compareTo(borrowAccount) > 0) {
					_log.error("========出借总额超过标的总额!========");
					return true;
				}
				//校验出借掉单造成的爆标
				BorrowTenderTmpExample example = new BorrowTenderTmpExample();
				example.createCriteria().andIsBankTenderEqualTo(1).andBorrowNidEqualTo(borrowNid);
				List<BorrowTenderTmp> borrowtmpList = this.borrowTenderTmpMapper.selectByExample(example);
				int index = 0;
				if (borrowtmpList!=null && borrowtmpList.size() > 0) {//复审时存在全部掉单的出借数据,可能导致爆标的出现
					for (int i = 0; i < borrowtmpList.size(); i++) {
						BorrowTenderTmp info = borrowtmpList.get(i);
						String accountID = this.getAccountIDByUserId(info.getUserId());
						BankCallBean callBean = queryBorrowTenderList(accountID, info.getNid(), info.getUserId()+"");
						if (callBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(callBean.getRetCode())
								&& StringUtils.isNoneBlank(callBean.getAuthCode())) {
							_log.error("==========存在全部掉单的出借数据,可能导致爆标,出借订单号:" + info.getNid());
							tenderCancel(info,accountID);
							index++;
						}
					}
					if (index == 0) {
						return false;
					}else{
						return true;
					}
				}else{
					return false;
				}
	}

	/**
	 * 出借撤销
	 * @param info 
	 * @param accountID 
	 */
	private void tenderCancel(BorrowTenderTmp info, String accountID) {
		String nid = info.getNid();
		// TODO Auto-generated method stub
		BankCallBean callBean = bidCancel(info.getUserId(), accountID, info.getBorrowNid(), nid, info.getAccount().toString());
		if (Validator.isNotNull(callBean)) {
			String retCode = StringUtils.isNotBlank(callBean.getRetCode()) ? callBean.getRetCode() : "";
			//出借正常撤销或出借订单不存在则删除冗余数据
			if (retCode.equals(BankCallConstant.RESPCODE_SUCCESS) || retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST1) 
					|| retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST2) || retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_RIGHT)){
				try {
					boolean flag = updateBidCancelRecord(info);
					if (flag) {
						_log.info("===============出借掉单数据已撤销,原出借订单号:" + nid);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("出借撤销数据处理异常!原订单号:" + nid + "异常原因:" + e.getMessage()); 
				}
			}else{
				throw new RuntimeException("出借撤销接口返回错误!原订单号:" + nid + ",返回码:" + retCode);
			}
		}else{
			throw new RuntimeException("出借撤销接口异常!");
		}
	}

	
	/**
	 * 出借撤销历史数据处理
	 * @param tenderTmp
	 * @return
	 * @throws Exception
	 */
	private boolean updateBidCancelRecord(BorrowTenderTmp tenderTmp) throws Exception {
		Integer userId = tenderTmp.getUserId();
		boolean tenderTmpFlag = this.borrowTenderTmpMapper.deleteByPrimaryKey(tenderTmp.getId()) > 0 ? true : false;
		if (!tenderTmpFlag) {
			throw new Exception("删除出借日志表失败，出借订单号：" + tenderTmp.getNid());
		}
		FreezeHistory freezeHistory = new FreezeHistory();
		freezeHistory.setTrxId(tenderTmp.getNid());
		freezeHistory.setNotes("自动任务银行出借撤销");
		freezeHistory.setFreezeUser(this.getUsersByUserId(userId).getUsername());
		freezeHistory.setFreezeTime(GetDate.getNowTime10());
		boolean freezeHisLog = this.freezeHistoryMapper.insert(freezeHistory) > 0 ? true : false;
		if (!freezeHisLog) {
			throw new Exception("插入出借删除日志表失败，出借订单号：" + tenderTmp.getNid());
		}
		return true;
	}
	
	/**
	 * 银行出借撤销
	 * 
	 * @param userId
	 * @param accountId
	 * @param productId
	 * @param orgOrderId
	 * @param txAmount
	 * @return
	 */
	public BankCallBean bidCancel(Integer userId, String accountId, String productId, String orgOrderId, String txAmount) {
		// 标的出借撤销
		BankCallBean bean = new BankCallBean();
		String orderId = GetOrderIdUtils.getOrderId2(userId);
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		bean.setVersion(BankCallConstant.VERSION_10); // 版本号(必须)
		bean.setTxCode(BankCallMethodConstant.TXCODE_BID_CANCEL); // 交易代码
		bean.setInstCode(instCode);
		bean.setBankCode(bankCode);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6)); // 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountId);// 电子账号
		bean.setOrderId(orderId); // 订单号(必须)
		bean.setTxAmount(CustomUtil.formatAmount(txAmount));// 交易金额
		bean.setProductId(productId);// 标的号
		bean.setOrgOrderId(orgOrderId);// 原标的订单号
		bean.setLogOrderId(orderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
		bean.setLogUserId(String.valueOf(userId));// 用户Id
		bean.setLogUserName(this.getUsersByUserId(userId).getUsername()); // 用户名
		bean.setLogRemark("投标申请撤销"); // 备注
		// 调用汇付接口
		BankCallBean result = BankCallUtils.callApiBg(bean);
		return result;
	}
	
	/**
	 * 获得电子账号
	 * @param userId
	 * @return
	 */
	private String getAccountIDByUserId(Integer userId) {
		BankOpenAccountExample example = new BankOpenAccountExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<BankOpenAccount> openAccountInfo = this.bankOpenAccountMapper.selectByExample(example );
		String  account = openAccountInfo.get(0).getAccount();
		return account;
	}

	/**
	 * 根据相应信息接口查询投标申请
	 * 
	 * @param accountId
	 * @param orgOrderId
	 * @return
	 */
	private BankCallBean queryBorrowTenderList(String accountId, String orgOrderId, String userId) {
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型
		bean.setLogUserId(userId);
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setOrgOrderId(orgOrderId);
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(Integer.parseInt(userId)));
		// 调用接口
		return BankCallUtils.callApiBg(bean);
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
		//modify by cwyang 查询非计划标的
		example.createCriteria().andStatusEqualTo(3).andBorrowFullStatusEqualTo(1).andPlanNidIsNull();
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
