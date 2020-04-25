package com.hyjf.admin.manager.borrow.borrowfull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewChargeExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.FreezeHistory;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFullCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BorrowFullServiceImpl extends BaseServiceImpl implements BorrowFullService {

	private Logger _log = LoggerFactory.getLogger(BorrowFullServiceImpl.class);
	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public Long countBorrowFull(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowFullCustomizeMapper.countBorrowFull(borrowCommonCustomize);

	}

	/**
	 * 复审记录
	 * 
	 * @param borrowCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowFullCustomize> selectBorrowFullList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowFullCustomizeMapper.selectBorrowFullList(borrowCommonCustomize);

	}

	/**
	 * 获取复审状态
	 * 
	 * @param record
	 */
	@Override
	public boolean isBorrowStatus16(BorrowFullBean borrowBean) {
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);
			if (borrowList != null && borrowList.size() == 1) {
				BorrowWithBLOBs borrow = borrowList.get(0);
				if (borrow.getStatus() == 16) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 更新复审 6个表放入一个事物
	 * 
	 * @param record
	 */
	@Override
	public boolean updateReverifyRecord(BorrowFullBean borrowBean,String msg) {

		String borrowNid = borrowBean.getBorrowNid();// 项目编号
		int nowTime = GetDate.getNowTime10();
		Date nowDate = GetDate.getDate(nowTime);
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
			//如果标的未复审
			if (borrow.getReverifyStatus() == 0) {
				Integer borrowUserId = borrow.getUserId();// 借款人userId
				String borrowUserName = borrow.getBorrowUserName();// 借款人用户名
				String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
				/** 标的基本数据 */
				Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 借款期数
				// 初审时间
				// 项目类型
				Integer projectType = borrow.getProjectType();
				// 借款人ID
				// 是否月标(true:月标, false:天标)
				boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
						|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
				
				// 获取标的费率信息
//				String borrowClass = this.getBorrowProjectClass(borrow.getProjectType()+ "");
//				BorrowFinmanNewCharge borrowFinmanNewCharge = this.selectBorrowApr(borrowClass,borrow.getInstCode(),borrow.getAssetType(),borrow.getBorrowStyle(),borrow.getBorrowPeriod());
//				if(borrowFinmanNewCharge == null || borrowFinmanNewCharge.getChargeMode() == null){
//					msg = "获取标的费率信息失败。[用户ID：" + borrowUserId + "]," + "[项目编号：" + borrowNid + "]";
//					_log.info(msg+borrowClass);
//					throw new RuntimeException("获取标的费率信息失败。[用户ID：" + borrowUserId + "]," + "[项目编号：" + borrowNid + "]");
//				}
				
				// 取得借款人账户信息
				Account borrowAccount = this.getAccountByUserId(borrowUserId);
				if (borrowAccount == null) {
					msg = "借款人账户不存在。[用户ID：" + borrowUserId + "]," + "[项目编号：" + borrowNid + "]";
					throw new RuntimeException("借款人账户不存在。[用户ID：" + borrowUserId + "]," + "[项目编号：" + borrowNid + "]");
				}
				// 借款人在汇付的账户信息
				BankOpenAccount borrowerAccount = this.getBankOpenAccount(borrowUserId);
				if (borrowerAccount == null) {
					msg = "借款人未开户。[用户ID：" + borrowUserId + "]," + "[项目编号：" + borrowNid + "]";
					throw new RuntimeException("借款人未开户。[用户ID：" + borrowUserId + "]," + "[项目编号：" + borrowNid + "]");
				}
				String nid = borrow.getBorrowNid() + "_" + borrow.getUserId() + "_1";
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				// 如果标的出借记录存在没有授权码的记录，则不进行放款
				int countErrorTender = this.countBorrowTenderError(borrowNid);
				//add by cwyang 校验标的是否爆标
				_log.info("====================cwyang 开始校验是否爆标");
				boolean result = this.checkBorrowTenderOverFlow(borrowNid);
				_log.info("校验结果: " + result);
				if (result) {
					msg = "标的爆标,请进行处理![用户ID：" + borrowUserId + "]," + "[项目编号：" + borrowNid + "]";
					return false;
				}
				//end
				if (countErrorTender == 0) {
					BorrowApicronExample example = new BorrowApicronExample();
					BorrowApicronExample.Criteria crt = example.createCriteria();
					crt.andNidEqualTo(nid);
					crt.andApiTypeEqualTo(0);
					List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
					if (borrowApicrons == null || borrowApicrons.size() == 0) {
						BorrowExample borrowExample = new BorrowExample();
						borrowExample.createCriteria().andIdEqualTo(borrow.getId()).andReverifyStatusEqualTo(borrow.getReverifyStatus());
						// 更新huiyingdai_borrow的如下字段：
						borrow.setReverifyTime(String.valueOf(nowTime));// 复审时间
						borrow.setReverifyUserid(adminSystem.getId()); // 复审人ID
						borrow.setReverifyUserName(adminSystem.getUsername());// 复审用户名
						borrow.setReverifyStatus(1);// 复审状态
						borrow.setStatus(3);// 复审状态（复审通过）
						borrow.setReverifyRemark(borrowBean.getReverifyRemark());// 复审备注
						borrow.setBorrowSuccessTime(nowTime); // 借款成功时间
						borrow.setUpdatetime(nowDate);// 更新时间
						boolean borrowUpdateFlag = this.borrowMapper.updateByExampleSelective(borrow, borrowExample) > 0 ? true : false;
						if (borrowUpdateFlag) {
							// 放款任务表
							BorrowApicron borrowApicron = new BorrowApicron();
							borrowApicron.setNid(nid);// 交易凭证号
							borrowApicron.setUserId(borrow.getUserId());// userId
							borrowApicron.setUserName(borrowUserName);// 用户名
							borrowApicron.setBorrowNid(borrow.getBorrowNid());// 项目编号
							borrowApicron.setBorrowAccount(borrow.getAccount());
							if (!isMonth) {
								borrowApicron.setBorrowPeriod(1);
							} else {
								borrowApicron.setBorrowPeriod(borrowPeriod);
							}
							borrowApicron.setFailTimes(0);
							borrowApicron.setStatus(1);// 放款状态
							borrowApicron.setApiType(0);// 任务类型0放款1还款
							borrowApicron.setWebStatus(0);// 无用字段
							borrowApicron.setPeriodNow(0);// 默认值，代表还款期数
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
							borrowApicron.setUpdateTime(nowTime); // 更新时间
							// add by liushouyi HJH3
							// 汇计划的复审带上计划编号
							if (StringUtils.isNotBlank(borrow.getPlanNid())) {
								borrowApicron.setPlanNid(borrow.getPlanNid());//计划编号
							}
							boolean apicronFlag = this.borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
							if (apicronFlag) {
								return true;
							}
						
						}
					}
				} else {
					return false;
				}
			}
		}
		return false;
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
		_log.info("=========标的"+borrowNid+"出借总额:"+sumTender);
		
		BorrowExample borrowExample = new BorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		BigDecimal borrowAccount = borrowList.get(0).getAccount();
		_log.info("=========标的"+borrowNid+"总额:"+borrowAccount);
		if (sumTender.compareTo(borrowAccount) > 0) {
			_log.error("=====标的"+borrowNid+"===出借总额超过标的总额!========");
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
	
	/**
	 * 校验出借数据的合法性
	 * 
	 * @param borrowNid
	 * @return
	 */
	private int countBorrowTenderError(String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andStatusEqualTo(0);
		criteria.andAuthCodeIsNull();
		int count = this.borrowTenderMapper.countByExample(example);
		return count;
	}

	/**
	 * 重新放款
	 * 
	 * @param record
	 */
	@Override
	public boolean updateBorrowApicronRecord(BorrowFullBean borrowBean) {
		int nowTime = GetDate.getNowTime10();
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowApicronExample borrowExample = new BorrowApicronExample();
			BorrowApicronExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			BorrowApicron borrowApicron = new BorrowApicron();// 放款任务表
			borrowApicron.setStatus(1);// 放款状态
			borrowApicron.setFailTimes(0);
			borrowApicron.setUpdateTime(nowTime);// 更新时间
			boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(borrowApicron, borrowExample) > 0 ? true : false;
			if (apicronFlag) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 复审详细中的列表
	 * 
	 * @param record
	 */
	@Override
	public List<BorrowFullCustomize> selectFullList(String borrowNid, int limitStart, int limitEnd) {
		return this.borrowFullCustomizeMapper.selectFullList(borrowNid, limitStart, limitEnd);
	}

	/**
	 * 复审详细
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public BorrowFullCustomize selectFullInfo(String borrowNid) {
		return this.borrowFullCustomizeMapper.selectFullInfo(borrowNid);
	}

	/**
	 * 合计
	 * 
	 * @param borrowFullCustomize
	 * @return
	 */
	public BorrowFullCustomize sumAmount(String borrowNid) {
		return this.borrowFullCustomizeMapper.sumAmount(borrowNid);
	}

	/**
	 * 流标
	 * 
	 * @param record
	 */
	public void updateBorrowRecordOver(BorrowFullBean borrowBean) {
		Date systemNowDate = new Date();
		String borrowNid = borrowBean.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);

			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);

			if (borrowList != null && borrowList.size() == 1) {
				AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
				BorrowWithBLOBs borrow = borrowList.get(0);
				// 复审人ID
				borrow.setReverifyUserid(adminSystem.getId());// Auto
				// 复审状态（流标）
				borrow.setStatus(6);
				// 复审备注
				borrow.setReverifyRemark(GetDate.getServerDateTime(6, new Date()) + " " + adminSystem.getId() + " 复审流标");

				// 更新时间
				borrow.setUpdatetime(systemNowDate);

				this.borrowMapper.updateByExampleSelective(borrow, borrowExample);
			}
		}
	}

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 更新放款状态
	 *
	 * @param accountList
	 * @return
	 */
	public boolean updateBorrowTender(BorrowTender borrowTender) {
		return borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
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
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
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

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowCommonCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public BorrowFullCustomize sumAccount(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowFullCustomizeMapper.sumAccount(borrowCommonCustomize);
			
	}

	
}
