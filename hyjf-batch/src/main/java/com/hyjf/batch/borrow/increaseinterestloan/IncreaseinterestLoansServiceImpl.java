package com.hyjf.batch.borrow.increaseinterestloan;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自动扣款(放款服务)
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class IncreaseinterestLoansServiceImpl extends BaseServiceImpl implements IncreaseinterestLoansService {

	private static final String THIS_CLASS = IncreaseinterestLoansServiceImpl.class.getName();

	private Logger _log = LoggerFactory.getLogger(this.getClass());
	
	/** 用户ID */
	private static final String VAL_USERID = "userId";

	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 出借订单号 */
	private static final String VAL_ORDER_ID = "order_id";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";
	/**
	 * 加息收益
	 */
	private static final String VAL_PROFIT = "val_profit";

	/** 放款时间 */
	private static final String VAL_LOAN_TIME = "loan_time";

	/**下一期还款日*/
	private static final String VAL_NEXTRECOVERTIME = "val_nextrecovertime";

	/**最后还款日*/
	private static final String VAL_RECOVERTIME = "val_recovertime";

	/** 还款明细ID */
	private static final String PARAM_BORROWRECOVERID = "param_borrowrecoverid";

	/** 优惠券出借 */
	private static final String COUPON_TYPE = "coupon_type";

	/** 优惠券出借订单编号 */
	private static final String TENDER_NID = "tender_nid";

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 自动放款
	 *
	 * @throws Exception
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public List<Map<String, String>> updateBorrowLoans(BorrowApicron apicron, IncreaseInterestInvest borrowTender) throws Exception {

		_log.info("-----------融通宝加息放款开始---" + apicron.getBorrowNid() + "---------" + borrowTender.getLoanOrderId());
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		/** 基本变量 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 借款人ID
		Integer borrowUserid = apicron.getUserId();
		// 借款人信息
		Users borrowUser = this.getUsersByUserId(borrowUserid);
		/** 标的基本数据 */
		// 取得标的详情
		BorrowWithBLOBs borrow = getBorrow(borrowNid);
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		BorrowStyle borrowStyleMain = this.getborrowStyleByNid(borrowStyle);
		String borrowStyleName = borrowStyleMain.getName();
		// 年利率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 加息年利率
		BigDecimal extraYieldApr = borrow.getBorrowExtraYield();
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManagerScaleEnd());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 借款成功时间
		Integer borrowSuccessTime = borrow.getBorrowSuccessTime();
		// 项目类型
		Integer projectType = borrow.getProjectType();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		// 出借人用户ID
		Integer outUserId = borrowTender.getUserId();
		// 出借费用
		BigDecimal tenderAccount = BigDecimal.ZERO;
		// 利息
		BigDecimal interestTender = BigDecimal.ZERO;
		String loanOrderId = GetOrderIdUtils.getOrderId2(borrowTender.getUserId());
		String loanOrderDate = GetOrderIdUtils.getOrderDate();
		// 估计还款时间
		Integer recoverTime = null;
		// 出借订单号
		String ordId = borrowTender.getOrderId();
		// 出借金额
		tenderAccount = borrowTender.getAccount();
		// 计算利息
		InterestInfo interestInfo = CalculatesUtil.getInterestInfo(tenderAccount, borrowPeriod, extraYieldApr, borrowStyle, borrowSuccessTime, borrowMonthRate, borrowManagerScaleEnd, projectType,
				differentialRate, borrowVerifyTime);
		if (interestInfo != null) {
			interestTender = interestInfo.getRepayAccountInterest(); // 利息
			recoverTime = interestInfo.getRepayTime(); // 估计还款时间

		}
		// 写入还款明细表(hyjf_increase_interest_loan)
		IncreaseInterestLoan increaseInterestLoan = new IncreaseInterestLoan();
		increaseInterestLoan.setUserId(borrowTender.getUserId()); // 出借人
		increaseInterestLoan.setUserName(borrowTender.getCreateUserName());
		increaseInterestLoan.setBorrowNid(borrowNid); // 借款编号
		increaseInterestLoan.setInvestId(borrowTender.getId());// 出借id
		increaseInterestLoan.setInvestOrderId(ordId); // 出借订单号
		increaseInterestLoan.setInvestAccount(borrowTender.getAccount());// 出借金额
		increaseInterestLoan.setBorrowUserId(borrowUserid); // 借款人
		increaseInterestLoan.setBorrowUserName(borrowUser.getUsername()); // 借款人
		increaseInterestLoan.setBorrowApr(borrowApr);
		increaseInterestLoan.setBorrowStyleName(borrowStyleName);
		increaseInterestLoan.setBorrowExtraYield(extraYieldApr);
		increaseInterestLoan.setRepayPeriod(0); // 还款期数
		increaseInterestLoan.setBorrowStyle(borrowStyle);
		increaseInterestLoan.setBorrowPeriod(borrowPeriod);
		increaseInterestLoan.setLoanInterest(interestTender);
		increaseInterestLoan.setRemainPeriod(isMonth ? borrowPeriod : 1);// 剩余期限
		increaseInterestLoan.setRepayTime(GetterUtil.getString(recoverTime)); // 估计还款时间
		increaseInterestLoan.setRepayInterestWait(interestTender); // 预还利息
		increaseInterestLoan.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
		increaseInterestLoan.setRepayStatus(0);// 还款状态 0未还款 1还款中 2已还款
		increaseInterestLoan.setWeb(0);
		increaseInterestLoan.setCreateTime(nowTime);
		increaseInterestLoan.setCreateUserId(borrowTender.getUserId());
		increaseInterestLoan.setCreateUserName(borrowTender.getCreateUserName());
		increaseInterestLoan.setAddip(borrowTender.getAddip());
		boolean borrowRecoverFlag = this.insertBorrowRecover(increaseInterestLoan) > 0 ? true : false;
		if (borrowRecoverFlag) {
			// 更新出借详情表
			IncreaseInterestInvest newIncreaseInterestInvest = new IncreaseInterestInvest();
			newIncreaseInterestInvest.setId(borrowTender.getId()); // ID
			newIncreaseInterestInvest.setLoanOrderId(loanOrderId);
			newIncreaseInterestInvest.setLoanOrderDate(loanOrderDate);
			newIncreaseInterestInvest.setLoanAmount(interestTender); // 实际放款金额
			newIncreaseInterestInvest.setRepayInterest(interestTender);
			newIncreaseInterestInvest.setRepayInterestYes(new BigDecimal("0"));
			newIncreaseInterestInvest.setRepayInterestWait(interestTender);
			newIncreaseInterestInvest.setRepayTimes(0);// 已还款次数
			//add by cwyang 20180730 新增应还款时间 散标加息需求
			newIncreaseInterestInvest.setRepayTime(recoverTime);//应还款时间
			newIncreaseInterestInvest.setStatus(1); // 状态 0，未放款，1，已放款
//			newIncreaseInterestInvest.setClient(0);
			newIncreaseInterestInvest.setAddip(borrowTender.getAddip());
			newIncreaseInterestInvest.setWeb(2); // 写入网站收支明细
			newIncreaseInterestInvest.setUpdateTime(GetDate.getNowTime10());
			newIncreaseInterestInvest.setUpdateUserId(borrowTender.getUserId());
			newIncreaseInterestInvest.setUpdateUserName(borrowTender.getCreateUserName());
			boolean borrowTenderFlag = this.updateBorrowTender(newIncreaseInterestInvest) > 0 ? true : false;
			if (borrowTenderFlag) {
				// 插入每个标的总的还款信息
				boolean isInsert = false;
				IncreaseInterestRepay increaseInterestRepay = getBorrowRepay(borrowNid);
				if (increaseInterestRepay == null) {
					isInsert = true;
					increaseInterestRepay = new IncreaseInterestRepay();
					increaseInterestRepay.setUserId(borrowUserid); // 借款人ID
					increaseInterestRepay.setUserName(borrowUser.getUsername());
					increaseInterestRepay.setBorrowNid(borrowNid); // 借款标号
					increaseInterestRepay.setInvestId(borrowTender.getId());
					increaseInterestRepay.setInvestOrderId(ordId);
					increaseInterestRepay.setInvestAccount(borrowTender.getAccount());
					increaseInterestRepay.setBorrowApr(borrowApr);
					increaseInterestRepay.setBorrowStyleName(borrowStyleName);
					increaseInterestRepay.setBorrowExtraYield(extraYieldApr);
					increaseInterestRepay.setBorrowPeriod(borrowPeriod);
					increaseInterestRepay.setBorrowStyle(borrowStyle);
					increaseInterestRepay.setRepayStatus(0); // 还款状态
					increaseInterestRepay.setRepayPeriod(1); // 还款期数
					increaseInterestRepay.setRepayTime(GetterUtil.getString(recoverTime)); // 估计还款时间
					increaseInterestRepay.setOrderId("");
					increaseInterestRepay.setOrderDate("");
					increaseInterestRepay.setRemainPeriod(isMonth ? borrowPeriod : 1);
					increaseInterestRepay.setAlreadyRepayPeriod(0);
					increaseInterestRepay.setRepayInterest(BigDecimal.ZERO); // 预还利息
					increaseInterestRepay.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
					increaseInterestRepay.setRepayInterestWait(BigDecimal.ZERO); // 未还利息
					increaseInterestRepay.setAddip(borrow.getAddip()); // 发标ip
					increaseInterestRepay.setWeb(0);
					increaseInterestRepay.setCreateTime(nowTime); // 创建时间
					increaseInterestRepay.setCreateUserId(borrowUserid);
					increaseInterestRepay.setCreateUserName(borrowUser.getUsername());
					//add by yangchangwei 20180730 产品加息需求新增
					increaseInterestRepay.setBorrowAccount(borrow.getAccount());//借款金额
					increaseInterestRepay.setLoanActionTime(nowTime);//实际放款时间
				}
				increaseInterestRepay.setRepayInterest(increaseInterestRepay.getRepayInterest().add(interestTender)); // 预还利息
				increaseInterestRepay.setRepayInterestWait(increaseInterestRepay.getRepayInterestWait().add(interestTender)); // 待还利息
				int borrowRepayCnt = isInsert ? this.increaseInterestRepayMapper.insertSelective(increaseInterestRepay) : this.increaseInterestRepayMapper
						.updateByPrimaryKeySelective(increaseInterestRepay);
				Integer lastRecoverTime = recoverTime;
				_log.info("------------加息放款获得最后一期还款时间，标的号：" + borrowNid +",出借订单号：" + ordId);
				if (borrowRepayCnt > 0 ? true : false) {
					// [principal: 等额本金, month:等额本息,
					// month:等额本息,end:先息后本]
					if (isMonth) {
						// 更新分期还款计划表(increaseInterestLoanDetail)
						if (interestInfo != null && interestInfo.getListMonthly() != null) {
							IncreaseInterestLoanDetail increaseInterestLoanDetail = null;
							InterestInfo monthly = null;
							for (int j = 0; j < interestInfo.getListMonthly().size(); j++) {
								monthly = interestInfo.getListMonthly().get(j);
								if(j+1 == borrowPeriod){//最后一期的数据
									lastRecoverTime = monthly.getRepayTime();
                                    _log.info("------------加息放款获得最后一期还款时间，标的号：" + borrowNid +",出借订单号：" + ordId + ",还款时间：" + lastRecoverTime);
                                }
								increaseInterestLoanDetail = new IncreaseInterestLoanDetail();
								increaseInterestLoanDetail.setUserId(outUserId); // 出借人id
								increaseInterestLoanDetail.setBorrowNid(borrowNid); // 借款订单id
								increaseInterestLoanDetail.setUserName(borrowTender.getCreateUserName());
								increaseInterestLoanDetail.setBorrowUserId(borrowUserid); // 借款人ID
								increaseInterestLoanDetail.setBorrowUserName(borrowUser.getUsername()); // 借款人ID
								increaseInterestLoanDetail.setBorrowStyleName(borrowStyleName);
								increaseInterestLoanDetail.setBorrowStyle(borrowStyle);
								increaseInterestLoanDetail.setInvestId(borrowTender.getId());
								increaseInterestLoanDetail.setInvestOrderId(ordId);
								increaseInterestLoanDetail.setInvestAccount(borrowTender.getAccount());
								increaseInterestLoanDetail.setLoanInterest(monthly.getRepayAccountInterest()); // 预还利息
								increaseInterestLoanDetail.setRepayPeriod(j + 1);
								increaseInterestLoanDetail.setRepayTime(GetterUtil.getString(monthly.getRepayTime()));// 估计还款时间
								increaseInterestLoanDetail.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
								increaseInterestLoanDetail.setRepayInterestWait(monthly.getRepayAccountInterest()); // 未还利息
								increaseInterestLoanDetail.setRepayStatus(0);// 还款状态
																				// 0未还款
																				// 1还款中
																				// 2已还款
								increaseInterestLoanDetail.setCreateTime(nowTime); // 创建时间
								increaseInterestLoanDetail.setBorrowApr(borrowApr);
								increaseInterestLoanDetail.setBorrowExtraYield(extraYieldApr);
								increaseInterestLoanDetail.setBorrowPeriod(borrowPeriod);
								increaseInterestLoanDetail.setCreateUserId(borrowTender.getUserId());
								increaseInterestLoanDetail.setCreateUserName(borrowTender.getCreateUserName());
								increaseInterestLoanDetail.setAddip(borrowTender.getAddip());
								increaseInterestLoanDetail.setWeb(0);
								boolean borrowRecoverPlanFlag = this.increaseInterestLoanDetailMapper.insertSelective(increaseInterestLoanDetail) > 0 ? true : false;
								if (borrowRecoverPlanFlag) {
									// 更新总的还款计划表(increaseInterestRepayDetail)
									isInsert = false;
									IncreaseInterestRepayDetail increaseInterestRepayDetail = getBorrowRepayPlan(borrowNid, j + 1);
									if (increaseInterestRepayDetail == null) {
										isInsert = true;
										increaseInterestRepayDetail = new IncreaseInterestRepayDetail();
										increaseInterestRepayDetail.setUserId(borrowUserid); // 借款人ID
										increaseInterestRepayDetail.setUserName(borrowUser.getUsername());
										increaseInterestRepayDetail.setBorrowNid(borrowNid); // 借款标号
										increaseInterestRepayDetail.setInvestId(borrowTender.getId());
										increaseInterestRepayDetail.setInvestOrderId(ordId);
										increaseInterestRepayDetail.setInvestAccount(borrowTender.getAccount());
										increaseInterestRepayDetail.setBorrowApr(borrowApr);
										increaseInterestRepayDetail.setBorrowStyleName(borrowStyleName);
										increaseInterestRepayDetail.setBorrowExtraYield(extraYieldApr);
										increaseInterestRepayDetail.setBorrowPeriod(borrowPeriod);
										increaseInterestRepayDetail.setBorrowStyle(borrowStyle);
										increaseInterestRepayDetail.setRepayStatus(0); // 还款状态
										increaseInterestRepayDetail.setRepayPeriod(j + 1); // 还款期数
										increaseInterestRepayDetail.setRepayTime(GetterUtil.getString(monthly.getRepayTime())); // 估计还款时间
										increaseInterestRepayDetail.setOrderId("");
										increaseInterestRepayDetail.setOrderDate("");
										increaseInterestRepayDetail.setRepayInterest(BigDecimal.ZERO); // 预还利息
										increaseInterestRepayDetail.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
										increaseInterestRepayDetail.setRepayInterestWait(BigDecimal.ZERO); // 未还利息
										increaseInterestRepayDetail.setAddip(borrow.getAddip()); // 发标ip
										increaseInterestRepayDetail.setWeb(0);
										increaseInterestRepayDetail.setCreateTime(nowTime); // 创建时间
										increaseInterestRepayDetail.setCreateUserId(borrowUserid);
										increaseInterestRepayDetail.setCreateUserName(borrowUser.getUsername());
									}
									increaseInterestRepayDetail.setRepayInterest(increaseInterestRepayDetail.getRepayInterest().add(increaseInterestLoanDetail.getLoanInterest())); // 应还利息
									increaseInterestRepayDetail.setRepayInterestWait(increaseInterestRepayDetail.getRepayInterestWait().add(increaseInterestLoanDetail.getLoanInterest())); // 待还利息

									int borrowRepayPlanCnt = isInsert ? this.increaseInterestRepayDetailMapper.insertSelective(increaseInterestRepayDetail) : this.increaseInterestRepayDetailMapper
											.updateByPrimaryKeySelective(increaseInterestRepayDetail);
									if (borrowRepayPlanCnt > 0 ? false : true) {
										throw new Exception("分期还款计划表(increaseInterestRepayDetail)写入失败!" + "[出借订单号：" + ordId + "]，" + "[期数：" + j + 1 + "]");
									}

								} else {
									throw new Exception("分期放款款计划表(huiyingdai_borrow_recover_plan)写入失败!" + "[出借订单号：" + ordId + "]，" + "[期数：" + j + 1 + "]");
								}
							}
						}
					}
					// 更新账户信息(出借人)
					Account account = new Account();
					account.setUserId(borrowTender.getUserId());
					// 出借人资金总额 + 利息
					account.setBankTotal(interestTender);
					// 出借人待收金额 + 利息+ 本金
					account.setBankAwait(interestTender);
					// 出借人待收利息
					account.setBankAwaitInterest(interestTender);
					boolean investaccountFlag = this.adminAccountCustomizeMapper.updateOfRTBLoansTender(account) > 0 ? true : false;
					if (!investaccountFlag) {
						throw new Exception("出借人资金记录(huiyingdai_account)更新失败!" + "[出借订单号：" + ordId + "]");
					}else{
						Map<String, String> map = new HashMap<>();
						map.put(VAL_PROFIT,interestTender.toString());
						map.put(VAL_NEXTRECOVERTIME,GetDate.getDateMyTimeInMillis(recoverTime));
						map.put(VAL_RECOVERTIME,GetDate.getDateMyTimeInMillis(lastRecoverTime));
						map.put(VAL_USERID,String.valueOf(borrowTender.getUserId()));
						List<Map<String, String>> msgMap = new ArrayList<>();
						msgMap.add(map);
						// 发送短信
						sendSms(msgMap);
						// 推送消息
						sendMessage(msgMap);
					}
				} else {
					throw new Exception("的总的还款信息(increaseInterestRepay)" + (isInsert ? "插入" : "更新") + "失败!" + "[出借订单号：" + ordId + "]");
				}
			} else {
				throw new Exception("出借详情(IncreaseInterestInvest)更新失败!" + "[出借订单号：" + ordId + "]");
			}
		} else {
			throw new Exception("总的放款明细表(increaseInterestLoan)写入失败!" + "[出借订单号：" + ordId + "]");
		}
		_log.info("-----------放款结束---" + apicron.getBorrowNid() + "---------" + borrowTender.getLoanOrderId());
		return retMsgList;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<BorrowApicron> getBorrowApicronList(Integer status, Integer apiType) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(6);
		criteria.andExtraYieldStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<BorrowApicron> getBorrowApicronListWithRepayStatus(Integer status, Integer apiType) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andExtraYieldRepayStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setExtraYieldStatus(status);
		if (record.getWebStatus() == null) {
			record.setWebStatus(0);
		}
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateBorrowApicronOfRepayStatus(Integer id, Integer status) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setExtraYieldRepayStatus(status);
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @param data
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status, String data) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setExtraYieldStatus(status);
		if (Validator.isNotNull(data) || status == 1) {
			record.setData(data);
		}
		if (record.getWebStatus() == null) {
			record.setWebStatus(0);
		}
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	public List<IncreaseInterestInvest> getBorrowTenderList(String borrowNid) {
		IncreaseInterestInvestExample example = new IncreaseInterestInvestExample();
		IncreaseInterestInvestExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andStatusEqualTo(0);
		example.setOrderByClause(" id asc ");
		List<IncreaseInterestInvest> list = this.increaseInterestInvestMapper.selectByExample(example);
		return list;
	}

	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	public BorrowWithBLOBs getBorrow(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款信息
	 *
	 * @return
	 */
	public IncreaseInterestRepay getBorrowRepay(String borrowNid) {
		IncreaseInterestRepayExample example = new IncreaseInterestRepayExample();
		IncreaseInterestRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<IncreaseInterestRepay> list = this.increaseInterestRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款计划信息
	 *
	 * @return
	 */
	public IncreaseInterestRepayDetail getBorrowRepayPlan(String borrowNid, Integer period) {
		IncreaseInterestRepayDetailExample example = new IncreaseInterestRepayDetailExample();
		IncreaseInterestRepayDetailExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayPeriodEqualTo(period);
		List<IncreaseInterestRepayDetail> list = this.increaseInterestRepayDetailMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得满标日志
	 *
	 * @return
	 */
	public AccountBorrow getAccountBorrow(String borrowNid) {
		AccountBorrowExample example = new AccountBorrowExample();
		AccountBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<AccountBorrow> list = this.accountBorrowMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
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
	public int updateBorrowTender(IncreaseInterestInvest borrowTender) {
		return increaseInterestInvestMapper.updateByPrimaryKeySelective(borrowTender);
	}

	/**
	 * 写入还款明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertBorrowRecover(IncreaseInterestLoan borrowRecover) {
		return increaseInterestLoanMapper.insertSelective(borrowRecover);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.selectBorrowList(borrowCommonCustomize);
	}

	public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, String userId, String nid, int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		params.put("nid", nid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
		return list;
	}

	public int countProjectRepayPlanRecordTotal(String borrowNid, String userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		int total = webUserInvestListCustomizeMapper.countProjectRepayPlanRecordTotal(params);
		return total;
	}

	public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(String borrowNid, String userId, String nid, int offset, int limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper.selectProjectRepayPlanList(params);
		return projectRepayList;
	}

	/**
	 * 发送短信(出借成功)
	 *
	 * @param msgList
	 */
	public void sendSms(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_PROFIT)) && new BigDecimal(msg.get(VAL_PROFIT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile()) || (users.getInvestSms() != null && users.getInvestSms() == 1)) {
						return;
					}
					UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (StringUtils.isEmpty(userInfo.getTruename())) {
						msg.put(VAL_NAME, users.getUsername());
					} else if (userInfo.getTruename().length() > 1) {
						msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
					} else {
						msg.put(VAL_NAME, userInfo.getTruename());
					}
					Integer sex = userInfo.getSex();
					if (Validator.isNotNull(sex)) {
						if (sex.intValue() == 2) {
							msg.put(VAL_SEX, "女士");
						} else {
							msg.put(VAL_SEX, "先生");
						}
					}
					_log.info("userid=" + msg.get(VAL_USERID) + ";开始发送短信,发送金额" + msg.get(VAL_PROFIT));
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_JIAXIFANGKUAN,
							CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 发送邮件(出借成功)
	 *
	 * @param borrowNid
	 */
	public void sendMail(List<Map<String, String>> msgList, String borrowNid) {
		if (msgList != null && msgList.size() > 0 && Validator.isNotNull(borrowNid)) {
			for (Map<String, String> msg : msgList) {
				try {
					// 向每个出借人发送邮件
					if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
						String userId = msg.get(VAL_USERID);
						String orderId = msg.get(VAL_ORDER_ID);
						Users users = getUsersByUserId(Integer.valueOf(userId));
						if (users == null || Validator.isNull(users.getEmail())) {
							return;
						}
						String email = users.getEmail();
						msg.put(VAL_NAME, users.getUsername());
						UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
						if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
							if (usersInfo.getSex() % 2 == 0) {
								msg.put(VAL_SEX, "女士");
							} else {
								msg.put(VAL_SEX, "先生");
							}
						}
						String fileName = borrowNid + "_" + orderId + ".pdf";
						String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
						// 查询借款人用户名
						BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
						// 借款编码
						borrowCommonCustomize.setBorrowNidSrch(borrowNid);
						List<BorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
						if (recordList != null && recordList.size() == 1) {
							Map<String, Object> contents = new HashMap<String, Object>();
							 contents.put("record", recordList.get(0));
							contents.put("borrowNid", borrowNid);
							contents.put("nid", orderId);
							// 借款人用户名
							contents.put("borrowUsername", recordList.get(0).getUsername().substring(0,1)+"**");
							// 本笔的放款完成时间 (协议签订日期)
							contents.put("recoverTime", msg.get(VAL_LOAN_TIME));
							// 用户出借列表
							List<WebUserInvestListCustomize> userInvestList = this.selectUserInvestList(borrowNid, userId, orderId, -1, -1);
							if (userInvestList != null && userInvestList.size() == 1) {
								contents.put("userInvest", userInvestList.get(0));
							} else {
								_log.info("标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。出借订单号:" + orderId);
								return;
							}
							// 如果是分期还款，查询分期信息
							String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
							if (borrowStyle != null) {
							  //计算预期收益
		                        BigDecimal earnings = new BigDecimal("0");
		                        // 收益率
		                        
		                        String borrowAprString = StringUtils.isEmpty(recordList.get(0).getBorrowApr())?"0.00":recordList.get(0).getBorrowApr().replace("%", "");
		                        BigDecimal borrowApr = new BigDecimal(borrowAprString);
		                        //出借金额
		                        String accountString = StringUtils.isEmpty(recordList.get(0).getAccount())?"0.00":recordList.get(0).getAccount().replace(",", "");
		                        BigDecimal account = new BigDecimal(accountString);
		                       // 周期
		                        String borrowPeriodString = StringUtils.isEmpty(recordList.get(0).getBorrowPeriod())?"0":recordList.get(0).getBorrowPeriod();
		                        String regEx="[^0-9]";   
		                        Pattern p = Pattern.compile(regEx);   
		                        Matcher m = p.matcher(borrowPeriodString); 
		                        borrowPeriodString = m.replaceAll("").trim();
		                        Integer borrowPeriod = Integer.valueOf(borrowPeriodString);
		                        if (StringUtils.equals("endday", borrowStyle)){
		                            // 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷365*锁定期；
		                            earnings = DuePrincipalAndInterestUtils.getDayInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
		                        } else {
		                            // 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
		                            earnings = DuePrincipalAndInterestUtils.getMonthInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);

		                        }
		                        contents.put("earnings", earnings);
								if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
									int recordTotal = this.countProjectRepayPlanRecordTotal(borrowNid, userId, orderId);
									if (recordTotal > 0) {
										Paginator paginator = new Paginator(1, recordTotal);
										List<WebProjectRepayListCustomize> repayList = this.selectProjectRepayPlanList(borrowNid, userId, orderId, paginator.getOffset(), paginator.getLimit());
										contents.put("paginator", paginator);
										contents.put("repayList", repayList);
									} else {
										Paginator paginator = new Paginator(1, recordTotal);
										contents.put("paginator", paginator);
										contents.put("repayList", "");
									}
								}
							}
							String pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
							if (StringUtils.isNotEmpty(pdfUrl)) {
								File path = new File(filePath);
								if (!path.exists()) {
									path.mkdirs();
								}
								FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
							}
							String[] emails = { email };
							MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇盈金服互联网金融服务平台居间服务协议", null, new String[] { filePath + fileName }, emails,
									CustomConstants.EMAILPARAM_TPL_LOANS, MessageDefine.MAILSENDFORMAILINGADDRESS);
							mailMessageProcesser.gather(message);
							// modify by zhangjp 优惠券放款相关 start
							// 是否优惠券出借
							if (StringUtils.equals(msg.get(COUPON_TYPE), "1")) {
								CouponRecoverExample example = new CouponRecoverExample();
								example.createCriteria().andTenderIdEqualTo(msg.get(TENDER_NID));
								CouponRecover rc = new CouponRecover();
								rc.setNoticeFlg(1);
								// 将所有该笔出借的放款记录（分期或不分期）都改成通知状态
								this.couponRecoverMapper.updateByExampleSelective(rc, example);
							} else {
								// 更新BorrowRecover邮件发送状态
								String borrowRecoverId = msg.get(PARAM_BORROWRECOVERID);
								if (Validator.isNotNull(borrowRecoverId) && NumberUtils.isNumber(borrowRecoverId)) {
									BorrowRecover borrowRecover = new BorrowRecover();
									borrowRecover.setId(Integer.valueOf(borrowRecoverId));
									borrowRecover.setSendmail(1);
									this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover);
								}
							}
							// modify by zhangjp 优惠券放款相关 end
						} else {
							_log.info("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
							return;
						}
					}
				} catch (Exception e) {
					LogUtil.errorLog(THIS_CLASS, "sendMail", e);
				}
			}
		}
	}

	/**
	 * 推送消息
	 * 
	 * @param msgList
	 * @author Administrator
	 */

	@Override
	public void sendMessage(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_PROFIT)) && new BigDecimal(msg.get(VAL_PROFIT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null) {
						return;
					} else {
						UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
						if (StringUtils.isEmpty(userInfo.getTruename())) {
							msg.put(VAL_NAME, users.getUsername());
						} else if (userInfo.getTruename().length() > 1) {
							msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
						} else {
							msg.put(VAL_NAME, userInfo.getTruename());
						}
						Integer sex = userInfo.getSex();
						if (Validator.isNotNull(sex)) {
							if (sex.intValue() == 2) {
								msg.put(VAL_SEX, "女士");
							} else {
								msg.put(VAL_SEX, "先生");
							}
						}
						AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_JIAXIFANGKUAN);
						appMsProcesser.gather(smsMessage);
					}
				}
			}
		}
	}

	private BorrowStyle getborrowStyleByNid(String borrowStyle) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cri = example.createCriteria();
		cri.andNidEqualTo(borrowStyle);
		List<BorrowStyle> style = borrowStyleMapper.selectByExample(example);
		return style.get(0);
	}
}
