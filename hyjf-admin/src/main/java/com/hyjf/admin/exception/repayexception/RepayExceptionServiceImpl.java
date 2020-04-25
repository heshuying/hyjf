package com.hyjf.admin.exception.repayexception;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample.Criteria;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class RepayExceptionServiceImpl extends BaseServiceImpl implements RepayExceptionService {

	/** 等待 */
	private static final String TYPE_WAIT = "wait";
	/** 完成 */
	private static final String TYPE_YES = "yes";
	/** 部分完成 */
	private static final String TYPE_WAIT_YES = "wait_yes";

	/**
	 * 出借明细列表
	 *
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<RepayExceptionCustomize> selectBorrowRepaymentList(RepayExceptionCustomize repayExceptionCustomize) {
		return this.repayExceptionCustomizeMapper.selectBorrowRepaymentList(repayExceptionCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 *
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRepayment(RepayExceptionCustomize repayExceptionCustomize) {
		return this.repayExceptionCustomizeMapper.countBorrowRepayment(repayExceptionCustomize);
	}

	/**
	 * 重新还款
	 *
	 * @param record
	 */
	@Override
	public void updateBorrowApicronRecord(String borrowNid, Integer periodNow) {
		int nowTime = GetDate.getNowTime10();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowApicronExample borrowExample = new BorrowApicronExample();
			BorrowApicronExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			borrowCra.andPeriodNowEqualTo(periodNow);
			borrowCra.andApiTypeEqualTo(1);
			BorrowApicron borrowApicron = new BorrowApicron(); // 放款任务表
			borrowApicron.setStatus(1);// Status
			borrowApicron.setUpdateTime(nowTime); // 更新时间
			this.borrowApicronMapper.updateByExampleSelective(borrowApicron, borrowExample);
		}
	}

	/**
	 * 手动还款
	 *
	 * @param record
	 */
	@Override
	public void updateRrepayTemp(String borrowNid, Integer periodNow) {
		try {
			// 取得借款详情
			BorrowExample example = new BorrowExample();
			BorrowExample.Criteria criteria = example.createCriteria();
			criteria.andBorrowNidEqualTo(borrowNid);
			example.setOrderByClause(" id asc ");
			List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);
			BorrowWithBLOBs borrow = list.get(0);

			// 取得还款详情
			BorrowRepayExample repayExample = new BorrowRepayExample();
			BorrowRepayExample.Criteria repayCriteria = repayExample.createCriteria();
			repayCriteria.andBorrowNidEqualTo(borrowNid);
			repayExample.setOrderByClause(" id asc ");
			List<BorrowRepay> listRepay = this.borrowRepayMapper.selectByExample(repayExample);
			BorrowRepay borrowRepay = listRepay.get(0);

			// 取得还款明细
			BorrowRecoverExample recoverExample = new BorrowRecoverExample();
			BorrowRecoverExample.Criteria recovercriteria = recoverExample.createCriteria();
			recovercriteria.andBorrowNidEqualTo(borrowNid);
			recovercriteria.andCreditTimeEqualTo(0); // 排除债转
			recoverExample.setOrderByClause(" id asc ");
			List<BorrowRecover> listRecover = this.borrowRecoverMapper.selectByExample(recoverExample);

			// 借款人用户ID
			Integer borrowUserid = borrow.getUserId();

			if (listRecover != null && listRecover.size() > 0) {
				for (BorrowRecover borrowRecover : listRecover) {
					// 出借订单号
					String tenderOrdId = borrowRecover.getNid();
					// 出借人用户ID
					Integer tenderUserId = borrowRecover.getUserId();

					// 订单号
					String repayOrdIdOld = GetOrderIdUtils.getRepayOrderId(borrowRecover.getTenderId(), 1);
					String repayOrdId = GetOrderIdUtils.getRepayOrderId(borrowRecover.getTenderId(), 2);
					borrowRecover.setRepayOrdid(repayOrdId);

					// 出借详情
					BorrowTender borrowTender = this.borrowTenderMapper.selectByPrimaryKey(borrowRecover.getTenderId());

					// 还款金额
					BigDecimal recoverAccount = BigDecimal.ZERO;
					// 还款本金
					BigDecimal recoverCapital = BigDecimal.ZERO;
					// 还款利息
					BigDecimal recoverInterest = BigDecimal.ZERO;
					// 逾期利息
					BigDecimal lateInterest = BigDecimal.ZERO;
					// 延期利息
					BigDecimal delayInterest = BigDecimal.ZERO;
					// 提前还款少还利息
					BigDecimal chargeInterest = borrowRecover.getChargeInterest().multiply(new BigDecimal(-1));
					// 提前天数
					Integer chargeDays = borrowRecover.getChargeDays() * -1;
					// 管理费
					BigDecimal recoverFee = BigDecimal.ZERO;
					// 还款金额(实际)
					BigDecimal recoverAccountYes = chargeInterest;
					// 还款本金(实际)
					BigDecimal recoverCapitalYes = BigDecimal.ZERO;
					// 还款利息(实际)
					BigDecimal recoverInterestYes = chargeInterest;
					// 总收入金额=还款金额+逾期利息+延期利息+提前还款少还利息(负数)
					BigDecimal recoverAccountAll = recoverAccount.add(lateInterest).add(delayInterest).add(chargeInterest);

					// 出借人在汇付的账户信息
					AccountChinapnr tenderUserCust = getChinapnrUserInfo(tenderUserId);
					if (tenderUserCust == null) {
						throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
					}
					// 借款人在汇付的账户信息
					AccountChinapnr borrowUserCust = getChinapnrUserInfo(borrowUserid);
					if (borrowUserCust == null) {
						throw new Exception("借款人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
					}

					// 出借人客户号
					Long tenderUserCustId = tenderUserCust.getChinapnrUsrcustid();
					// 借款人客户号
					Long borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();

					// 调用交易查询接口
					ChinapnrBean queryTransStatBean = queryTransStat(repayOrdId, borrowRecover.getRepayOrddate(), "REPAYMENT");
					String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
					// 调用接口失败时(000,422以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)) {
						String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
						throw new Exception("调用交易查询接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
					}

					// 汇付交易状态
					String transStat = queryTransStatBean.getTransStat();
					// I:初始 P:部分成功
					if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode) || (!"I".equals(transStat) && !"P".equals(transStat))) {
						// 分账账户串（当 管理费！=0 时是必填项）
						String divDetails = "";
						// if (recoverFee.compareTo(BigDecimal.ZERO) != 0) {
						JSONArray ja = new JSONArray();
						JSONObject jo = new JSONObject();
						// 分账账户号(子账户号,从配置文件中取得)
						jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCTID));
						// 分账金额
						jo.put(ChinaPnrConstant.PARAM_DIVAMT, recoverFee.toString());
						ja.add(jo);
						divDetails = ja.toString();
						// }
						// 入参扩展域(2.0用)
						String reqExts = "";
						// 调用汇付接口
						ChinapnrBean repaymentBean = repayment(borrowUserid, String.valueOf(borrowUserCustId), recoverAccountAll.toString(), // 实际还款金额-手续费
								recoverFee.toString(), borrowRecover.getRepayOrdid(), borrowRecover.getRepayOrddate(), tenderOrdId, GetOrderIdUtils.getOrderDate(borrowTender.getAddtime()),
								String.valueOf(tenderUserCustId), divDetails, reqExts);
						respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
						// 调用接口失败时(000以外)
						if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_REPAY_OUT.equals(respCode) && !ChinaPnrConstant.RESPCODE_REPEAT_REPAY.equals(respCode)) {
							String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
							throw new Exception("调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
						}
					}

					AccountListExample accountListExample = new AccountListExample();
					accountListExample.createCriteria().andNidEqualTo(repayOrdIdOld).andTradeEqualTo("tender_recover_yes");
					List<AccountList> listAccountList = this.accountListMapper.selectByExample(accountListExample);

					// 判断该收支明细是否存在时,跳出本次循环
					if (listAccountList != null && listAccountList.size() > 0) {

						// 更新账户信息(出借人)
						Account account = new Account();
						account.setUserId(tenderUserId);
						// 出借人资金总额
						account.setTotal(lateInterest.add(delayInterest).add(chargeInterest));
						// 出借人可用余额
						account.setBalance(recoverAccountAll);
						// 出借人待收金额
						account.setAwait(BigDecimal.ZERO);
						int accountCnt = this.adminAccountCustomizeMapper.updateOfRepayTender(account);
						if (accountCnt == 0) {
							throw new Exception("出借人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
						}
						// 取得账户信息(出借人)
						AccountExample accountExample = new AccountExample();
						accountExample.createCriteria().andUserIdEqualTo(tenderUserId);
						List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
						account = listAccount.get(0);

						// 写入收支明细
						AccountList accountList = listAccountList.get(0);
						accountList.setAmount(accountList.getAmount().add(recoverAccountAll)); // 出借总收入
						accountList.setTotal(accountList.getTotal().add(recoverAccountAll)); // 出借人资金总额
						accountList.setBalance(accountList.getBalance().add(recoverAccountAll)); // 出借人可用金额
						accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
						int accountListCnt = accountListMapper.updateByPrimaryKeySelective(accountList);
						if (accountListCnt == 0) {
							throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[出借订单号：" + tenderOrdId + "]");
						}
					}

					// 更新还款明细表

					borrowRecover.setRecoverAccountYes(borrowRecover.getRecoverAccountYes().add(recoverAccountYes));
					borrowRecover.setRecoverInterestYes(borrowRecover.getRecoverInterestYes().add(recoverInterestYes));
					borrowRecover.setRecoverCapitalYes(borrowRecover.getRecoverCapitalYes().add(recoverCapitalYes));
					borrowRecover.setRecoverAccountWait(borrowRecover.getRecoverAccountWait().subtract(recoverAccount));
					borrowRecover.setRecoverInterestWait(borrowRecover.getRecoverInterestWait().subtract(recoverInterest));
					borrowRecover.setRecoverCapitalWait(borrowRecover.getRecoverCapitalWait().subtract(recoverCapital));
					// borrowRecover.setWeb(2); // 写入网站收支
					int borrowRecoverCnt = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover);
					if (borrowRecoverCnt == 0) {
						throw new Exception("还款明细(huiyingdai_borrow_recover)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
					}

					// 更新总的还款明细
					borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(recoverAccountYes).add(recoverFee));
					borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(recoverAccountYes));
					borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(recoverInterestYes));
					borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(recoverCapitalYes));
					borrowRepay.setChargeInterest(borrowRepay.getChargeInterest().add(chargeInterest));
					borrowRepay.setChargeDays(chargeDays);
					int borrowRepayCnt = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay);
					if (borrowRepayCnt == 0) {
						throw new Exception("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
					}

					// 更新借款表
					example = new BorrowExample();
					criteria = example.createCriteria();
					criteria.andBorrowNidEqualTo(borrowNid);
					example.setOrderByClause(" id asc ");
					list = this.borrowMapper.selectByExampleWithBLOBs(example);
					borrow = list.get(0);

					BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
					newBrrow.setId(borrow.getId());
					newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(recoverAccountYes)); // 总还款利息
					newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(recoverInterestYes)); // 总还款利息
					newBrrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(recoverCapitalYes)); // 总还款本金
					newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(recoverAccount)); // 未还款总额
					newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(recoverInterest)); // 未还款利息
					newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(recoverCapital)); // 未还款本金
					int borrowCnt = this.borrowMapper.updateByPrimaryKeySelective(newBrrow);
					if (borrowCnt == 0) {
						throw new Exception("借款详情(huiyingdai_borrow)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
					}

					// 更新出借表
					borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(recoverAccountYes));
					borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(recoverInterestYes));
					borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(recoverCapitalYes));
					borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(recoverAccount));
					borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(recoverInterest));
					borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(recoverCapital));
					int borrowTenderCnt = borrowTenderMapper.updateByPrimaryKeySelective(borrowTender);
					if (borrowTenderCnt == 0) {
						throw new Exception("出借表(huiyingdai_borrow_tender)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自动扣款（还款）(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean repayment(Integer borrowUserId, String outCustId, String transAmt, String fee, String ordId, String ordDate, String subOrdId, String subOrdDate, String inCustId,
			String divDetails, String reqExt) {
		String methodName = "repayment";

		// 调用汇付接口(自动扣款（还款）)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_REPAYMENT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setOutCustId(outCustId); // 出账客户号(必须)
		bean.setSubOrdId(subOrdId); // 订单号(必须)
		bean.setSubOrdDate(subOrdDate); // 订单日期(必须)
		bean.setTransAmt(CustomUtil.formatAmount(transAmt)); // 交易金额(必须)
		bean.setFee(CustomUtil.formatAmount(fee)); // 扣款手续费(必须)
		bean.setInCustId(inCustId); // 入账客户号(必须)
		bean.setDivDetails(divDetails); // 分账账户串(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("自动扣款（还款）"); // 备注
		bean.setLogClient("0"); // PC

		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(RepayExceptionDefine.THIS_CLASS, methodName, new Exception("调用自动扣款（还款）接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}

		return chinapnrBean;
	}

	/**
	 * 交易状态查询(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean queryTransStat(String ordId, String ordDate, String queryTransType) {

		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setQueryTransType(queryTransType); // 交易查询类型

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("交易状态查询"); // 备注
		bean.setLogClient("0"); // PC

		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			return null;
		}
		return chinapnrBean;
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public boolean updateBorrowRepay(String orderId, String period) throws Exception {

		// 根据标号获取当前还款的总信息
		BorrowRecover borrowRecover = this.selectBorrowRecoverByNid(orderId);
		// 项目编号
		String borrowNid = borrowRecover.getBorrowNid();
		// 获取标信息
		Borrow borrow = this.selectBorrow(borrowNid);
		if (StringUtils.isBlank(borrowRecover.getRepayOrdid())) {
			String repayOrderId = GetOrderIdUtils.getOrderId2(borrowRecover.getUserId());
			borrowRecover.setRepayOrdid(repayOrderId);
			// 设置还款时间
			borrowRecover.setRepayOrddate(GetOrderIdUtils.getOrderDate());
			// 更新还款信息
			this.updateBorrowRecover(borrowRecover);
		}

		String methodName = "updateBorrowRepay";
		System.out.println("-----------还款开始---" + borrowNid + "------" + borrowRecover.getRepayOrdid());

		/** 基本变量 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款人ID
		Integer borrowUserId = borrow.getUserId();
		// 当前期数
		Integer periodNow = Integer.parseInt(period);

		/** 标的基本数据 */
		// 取得还款详情
		BorrowRepay borrowRepay = getBorrowRepay(borrowNid);

		// 还款时间
		Integer recoverLastTime = borrowRecover.getCreateTime();
		// 还款时间
		Date recoverLastDate = GetDate.getDate(recoverLastTime * 1000L);
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 出借订单号
		String tenderOrdId = null;
		// 出借人用户ID
		Integer tenderUserId = null;

		// 总收入金额
		BigDecimal recoverAccountAll = BigDecimal.ZERO;
		// 还款总额
		BigDecimal recoverAccount = BigDecimal.ZERO;
		// 还款本金
		BigDecimal recoverCapital = BigDecimal.ZERO;
		// 还款利息
		BigDecimal recoverInterest = BigDecimal.ZERO;
		// 还款总额(实际)
		BigDecimal recoverAccountYes = BigDecimal.ZERO;
		// 还款本金(实际)
		BigDecimal recoverCapitalYes = BigDecimal.ZERO;
		// 还款利息(实际)
		BigDecimal recoverInterestYes = BigDecimal.ZERO;
		// 逾期利息
		BigDecimal lateInterest = BigDecimal.ZERO;
		// 延期利息
		BigDecimal delayInterest = BigDecimal.ZERO;
		// 提前还款少还利息
		BigDecimal chargeInterest = BigDecimal.ZERO;
		// 提前天数
		Integer chargeDays = 0;
		// 管理费
		BigDecimal recoverFee = BigDecimal.ZERO;
		// 出借ID
		Integer tenderId = null;
		// 还款时间
		String recoverTime = null;

		// 出借信息
		BorrowTender borrowTender = null;

		// 出借人在汇付的账户信息
		AccountChinapnr tenderUserCust = null;
		// 出借人客户号
		Long tenderUserCustId = null;
		// 借款人客户号
		Long borrowUserCustId = null;

		// 出借订单号
		tenderOrdId = borrowRecover.getNid();
		// 出借人用户ID
		tenderUserId = borrowRecover.getUserId();
		// 出借ID
		tenderId = borrowRecover.getTenderId();
		// 取得出借信息
		borrowTender = getBorrowTender(tenderId);

		// 出借人在汇付的账户信息
		tenderUserCust = getChinapnrUserInfo(tenderUserId);
		if (tenderUserCust == null) {
			throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
		}
		// 出借人客户号
		tenderUserCustId = tenderUserCust.getChinapnrUsrcustid();

		AccountChinapnr borrowUserCust = getChinapnrUserInfo(borrowUserId);
		// 借款人客户号
		borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();

		// 分期还款计划表
		BorrowRecoverPlan borrowRecoverPlan = null;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);

		// [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
		if (isMonth) {
			// 取得分期还款计划表
			borrowRecoverPlan = selectBorrowRecoverPlan(orderId, periodNow);
			if (borrowRecoverPlan == null) {
				throw new Exception("分期还款计划表数据不存在。[项目编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrdId + "]，" + "[期数：" + periodNow + "]");
			}

			// 还款时间
			recoverTime = borrowRecoverPlan.getRecoverTime();
			// 还款金额
			recoverAccount = borrowRecoverPlan.getRecoverAccount();
			// 还款本金
			recoverCapital = borrowRecoverPlan.getRecoverCapital();
			// 还款利息
			recoverInterest = borrowRecoverPlan.getRecoverInterest();
			// 逾期利息
			lateInterest = borrowRecoverPlan.getLateInterest();
			// 延期利息
			delayInterest = borrowRecoverPlan.getDelayInterest();
			// 提前还款少还利息
			chargeInterest = borrowRecoverPlan.getChargeInterest();
			// 提前天数
			chargeDays = borrowRecoverPlan.getChargeDays();
			// 管理费
			recoverFee = borrowRecoverPlan.getRecoverFee();
			// 还款金额(实际)
			recoverAccountYes = borrowRecoverPlan.getRecoverAccount().add(lateInterest).add(delayInterest).add(chargeInterest);
			// 还款本金(实际)
			recoverCapitalYes = borrowRecoverPlan.getRecoverCapital();
			// 还款利息(实际)
			recoverInterestYes = borrowRecoverPlan.getRecoverInterest().add(lateInterest).add(delayInterest).add(chargeInterest);
			// 总收入金额=还款金额+逾期利息+延期利息+提前还款少还利息(负数)
			recoverAccountAll = recoverAccount.add(lateInterest).add(delayInterest).add(chargeInterest);
		}
		// [endday: 按天计息, end:按月计息]
		else {
			// 还款时间
			recoverTime = borrowRecover.getRecoverTime();
			// 还款金额
			recoverAccount = borrowRecover.getRecoverAccount();
			// 还款本金
			recoverCapital = borrowRecover.getRecoverCapital();
			// 还款利息
			recoverInterest = borrowRecover.getRecoverInterest();
			// 逾期利息
			lateInterest = borrowRecover.getLateInterest();
			// 延期利息
			delayInterest = borrowRecover.getDelayInterest();
			// 提前还款少还利息
			chargeInterest = borrowRecover.getChargeInterest();
			// 提前天数
			chargeDays = borrowRecover.getChargeDays();
			// 管理费
			recoverFee = borrowRecover.getRecoverFee();
			// 还款金额(实际)
			recoverAccountYes = borrowRecover.getRecoverAccount().add(lateInterest).add(delayInterest).add(chargeInterest);
			// 还款本金(实际)
			recoverCapitalYes = borrowRecover.getRecoverCapital();
			// 还款利息(实际)
			recoverInterestYes = borrowRecover.getRecoverInterest().add(lateInterest).add(delayInterest).add(chargeInterest);
			// 总收入金额=还款金额+逾期利息+延期利息+提前还款少还利息(负数)
			recoverAccountAll = recoverAccount.add(lateInterest).add(delayInterest).add(chargeInterest);
		}
		// 借款人在汇付的可用余额小于还款金额时报错
		BigDecimal chinapnrBalance = getUserBalance(borrowUserCust.getChinapnrUsrcustid());
		if (chinapnrBalance.compareTo(recoverAccountAll) < 0) {
			System.out.println("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[项目编号：" + borrowNid + "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + recoverAccountAll + "]");
			throw new Exception("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[项目编号：" + borrowNid + "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + recoverAccountAll + "]");
		}
		// 调用交易查询接口
		ChinapnrBean queryTransStatBean = queryTransStat(borrowRecover.getRepayOrdid(), borrowRecover.getRepayOrddate(), "REPAYMENT");
		String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
		// 调用接口失败时(000,422以外)
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)) {
			String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
			LogUtil.errorLog(RepayExceptionDefine.THIS_CLASS, methodName, "调用交易查询接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
			throw new Exception("调用交易查询接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
		}
		// 汇付交易状态
		String transStat = queryTransStatBean.getTransStat();
		// I:初始 P:部分成功
		if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode) || (!"I".equals(transStat) && !"P".equals(transStat))) {
			// 分账账户串（当 管理费！=0 时是必填项）
			String divDetails = "";
			if (recoverFee.compareTo(BigDecimal.ZERO) != 0) {
				JSONArray ja = new JSONArray();
				JSONObject jo = new JSONObject();
				// 分账账户号(子账户号,从配置文件中取得)
				jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCTID));
				// 分账金额
				jo.put(ChinaPnrConstant.PARAM_DIVAMT, recoverFee.toString());
				ja.add(jo);
				divDetails = ja.toString();
			}
			// 入参扩展域(2.0用)
			String reqExts = "";
			// 调用汇付接口
			ChinapnrBean repaymentBean = repayment(borrowUserId, String.valueOf(borrowUserCustId), recoverAccountAll.toString(), // 实际还款金额-手续费
					recoverFee.toString(), borrowRecover.getRepayOrdid(), borrowRecover.getRepayOrddate(), tenderOrdId, GetOrderIdUtils.getOrderDate(borrowTender.getAddtime()),
					String.valueOf(tenderUserCustId), divDetails, reqExts);
			respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
				LogUtil.errorLog(RepayExceptionDefine.THIS_CLASS, methodName, "调用自动扣款（还款）接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
				throw new Exception("调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
			}
		}

		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNid(borrowRecover.getRepayOrdid()) == 0) {

			// 更新账户信息(出借人)
			Account account = new Account();
			account.setUserId(tenderUserId);
			// 出借人资金总额
			account.setTotal(lateInterest.add(delayInterest).add(chargeInterest));
			// 出借人可用余额
			account.setBalance(recoverAccountAll);
			// 出借人待收金额
			account.setAwait(recoverAccount);
			int accountCnt = this.adminAccountCustomizeMapper.updateOfRepayTender(account);
			if (accountCnt == 0) {
				throw new Exception("出借人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
			}
			// 取得账户信息(出借人)
			account = this.getAccountByUserId(borrowTender.getUserId());
			if (account == null) {
				throw new Exception("出借人账户信息不存在。[出借人ID：" + borrowTender.getUserId() + "]，" + "[出借订单号：" + tenderOrdId + "]");
			}

			// 写入收支明细
			AccountList accountList = new AccountList();
			accountList.setNid(borrowRecover.getRepayOrdid()); // 出借标识
			accountList.setUserId(tenderUserId); // 出借人
			accountList.setAmount(recoverAccountAll); // 出借总收入
			accountList.setType(1); // 1收入
			accountList.setTrade("tender_recover_yes"); // 出借成功
			accountList.setTradeCode("balance"); // 余额操作
			accountList.setTotal(account.getTotal()); // 出借人资金总额
			accountList.setBalance(account.getBalance()); // 出借人可用金额
			accountList.setFrost(account.getFrost()); // 出借人冻结金额
			accountList.setAwait(account.getAwait()); // 出借人待收金额
			// accountList.setRemark("出借还款"); // 出借还款
			accountList.setCreateTime(nowTime); // 创建时间
			accountList.setBaseUpdate(nowTime); // 更新时间
			accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
			accountList.setRemark(borrowNid);
			accountList.setIp(borrow.getAddip()); // 操作IP
			accountList.setIsUpdate(0);
			accountList.setBaseUpdate(0);
			accountList.setInterest(BigDecimal.ZERO); // 利息
			accountList.setWeb(0); // PC
			int accountListCnt = insertAccountList(accountList);
			if (accountListCnt == 0) {
				throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[出借订单号：" + tenderOrdId + "]");
			}
		}

		// 更新还款明细表
		// 分期并且不是最后一期
		if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
			borrowRecover.setRecoverStatus(0); // 未还款
			borrowRecover.setRecoverTime(String.valueOf(DateUtils.getRepayDate(borrowStyle, recoverLastDate, periodNow + 1, 0))); // 计算下期时间
			borrowRecover.setRecoverType(TYPE_WAIT);
		} else {
			borrowRecover.setRecoverStatus(1); // 已还款
			borrowRecover.setRecoverYestime(String.valueOf(nowTime)); // 实际还款时间
			borrowRecover.setRecoverTime(recoverTime);
			borrowRecover.setRecoverType(TYPE_YES);
		}

		// 分期时
		if (borrowRecoverPlan != null) {
			borrowRecover.setRecoverPeriod(periodNext);
		}
		borrowRecover.setRecoverAccountYes(borrowRecover.getRecoverAccountYes().add(recoverAccountYes));
		borrowRecover.setRecoverInterestYes(borrowRecover.getRecoverInterestYes().add(recoverInterestYes));
		borrowRecover.setRecoverCapitalYes(borrowRecover.getRecoverCapitalYes().add(recoverCapitalYes));
		borrowRecover.setRecoverAccountWait(borrowRecover.getRecoverAccountWait().subtract(recoverAccount));
		borrowRecover.setRecoverInterestWait(borrowRecover.getRecoverInterestWait().subtract(recoverInterest));
		borrowRecover.setRecoverCapitalWait(borrowRecover.getRecoverCapitalWait().subtract(recoverCapital));
		borrowRecover.setWeb(2); // 写入网站收支
		int borrowRecoverCnt = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover);
		if (borrowRecoverCnt == 0) {
			throw new Exception("还款明细(huiyingdai_borrow_recover)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}

		// 更新总的还款明细
		borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(recoverAccountYes).add(recoverFee));
		borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(recoverAccountYes));
		borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(recoverInterestYes));
		borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(recoverCapitalYes));
		borrowRepay.setLateRepayDays(0);
		borrowRepay.setChargeInterest(borrowRepay.getChargeInterest().add(chargeInterest));
		borrowRepay.setChargeDays(chargeDays);
		// 用户是否提前还款
		borrowRepay.setAdvanceStatus(borrowRecover.getAdvanceStatus());
		int borrowRepayCnt = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay);
		if (borrowRepayCnt == 0) {
			throw new Exception("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}

		// 更新借款表
		BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
		newBrrow.setId(borrow.getId());
		BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManager());
		newBrrow.setBorrowManager(borrowManager.add(recoverFee).toString());
		newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(recoverAccountYes)); // 总还款利息
		newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(recoverInterestYes)); // 总还款利息
		newBrrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(recoverCapitalYes)); // 总还款本金
		newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(recoverAccount)); // 未还款总额
		newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(recoverInterest)); // 未还款利息
		newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(recoverCapital)); // 未还款本金
		newBrrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(recoverFee));
		int borrowCnt = this.borrowMapper.updateByPrimaryKeySelective(newBrrow);
		if (borrowCnt == 0) {
			throw new Exception("借款详情(huiyingdai_borrow)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}

		// 更新出借表
		borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(recoverAccountYes));
		borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(recoverInterestYes));
		borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(recoverCapitalYes));
		borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(recoverAccount));
		borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(recoverInterest));
		borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(recoverCapital));
		int borrowTenderCnt = borrowTenderMapper.updateByPrimaryKeySelective(borrowTender);
		if (borrowTenderCnt == 0) {
			throw new Exception("出借表(huiyingdai_borrow_tender)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}

		// 分期时
		if (borrowRecoverPlan != null) {
			// 更新还款计划表
			borrowRecoverPlan.setRecoverStatus(1);
			borrowRecoverPlan.setRecoverYestime(String.valueOf(nowTime));
			borrowRecoverPlan.setRecoverAccountYes(recoverAccountYes);
			borrowRecoverPlan.setRecoverInterestYes(recoverInterestYes);
			borrowRecoverPlan.setRecoverCapitalYes(recoverCapitalYes);
			borrowRecoverPlan.setRecoverAccountWait(BigDecimal.ZERO);
			borrowRecoverPlan.setRecoverCapitalWait(BigDecimal.ZERO);
			borrowRecoverPlan.setRecoverInterestWait(BigDecimal.ZERO);
			borrowRecoverPlan.setRecoverType(TYPE_YES);
			int borrowRecoverPlanCnt = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan);
			if (borrowRecoverPlanCnt == 0) {
				throw new Exception("还款计划表(huiyingdai_borrow_recover_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
			}

			// 更新总的还款计划
			BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
			if (borrowRepayPlan != null) {
				borrowRepayPlan.setRepayType(TYPE_WAIT_YES);
				borrowRepayPlan.setRepayDays("0");
				borrowRepayPlan.setRepayStep(4);
				borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
				borrowRepayPlan.setRepayStatus(1);
				borrowRepayPlan.setRepayYestime(String.valueOf(nowTime));
				borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccountAll().add(recoverAccountYes).add(recoverFee));
				borrowRepayPlan.setRepayAccountYes(borrowRepayPlan.getRepayAccountYes().add(recoverAccountYes));
				borrowRepayPlan.setRepayInterestYes(borrowRepayPlan.getRepayInterestYes().add(recoverInterestYes));
				borrowRepayPlan.setRepayCapitalYes(borrowRepayPlan.getRepayCapitalYes().add(recoverCapitalYes));
				borrowRepayPlan.setLateRepayDays(0);
				borrowRepayPlan.setChargeInterest(borrowRepayPlan.getChargeInterest().add(chargeInterest));
				borrowRepayPlan.setChargeDays(borrowRecoverPlan.getChargeDays());
				// 用户是否提前还款
				borrowRepayPlan.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
				int borrowRepayPlanCnt = this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan);
				if (borrowRepayPlanCnt == 0) {
					throw new Exception("总的还款计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
			} else {
				throw new Exception("总的还款计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
			}
		}

		// 管理费大于0时,插入网站收支明细
		if (recoverFee.compareTo(BigDecimal.ZERO) > 0) {
			// 插入网站收支明细记录
			AccountWebList accountWebList = new AccountWebList();
			accountWebList.setOrdid(borrowTender.getNid() + "_" + periodNow);// 订单号
			accountWebList.setBorrowNid(borrowNid); // 出借编号
			accountWebList.setUserId(tenderUserId); // 出借者
			accountWebList.setAmount(recoverFee); // 管理费
			accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
			accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
			accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 还款服务费
			accountWebList.setRemark(borrowNid); // 出借编号
			accountWebList.setCreateTime(nowTime);
			int accountWebListCnt = insertAccountWebList(accountWebList);
			if (accountWebListCnt == 0) {
				throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getNid() + "]");
			}
		}

		System.out.println("-----------还款结束---" + borrowNid + "---------" + borrowRecover.getRepayOrdid() + "---------------");
		return true;
	}

	@Override
	public BorrowRecover selectBorrowRecoverByNid(String orderId) {
		BorrowRecoverExample recoverExample = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria crt = recoverExample.createCriteria();
		crt.andNidEqualTo(orderId);
		List<BorrowRecover> borrowRecovers = this.borrowRecoverMapper.selectByExample(recoverExample);
		if (borrowRecovers != null && borrowRecovers.size() == 1) {
			return borrowRecovers.get(0);
		}
		return null;
	}

	@Override
	public Borrow selectBorrow(String borrowNid) {
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria crt = borrowExample.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrows = this.borrowMapper.selectByExample(borrowExample);
		if (borrows != null && borrows.size() == 1) {
			return borrows.get(0);
		}
		return null;
	}

	@Override
	public BorrowRecoverPlan selectBorrowRecoverPlan(String orderId, int period) {

		BorrowRecoverPlanExample recoverExample = new BorrowRecoverPlanExample();
		BorrowRecoverPlanExample.Criteria crt = recoverExample.createCriteria();
		crt.andNidEqualTo(orderId);
		crt.andRecoverPeriodEqualTo(period);
		List<BorrowRecoverPlan> borrowRecoverPlans = this.borrowRecoverPlanMapper.selectByExample(recoverExample);
		if (borrowRecoverPlans != null && borrowRecoverPlans.size() == 1) {
			return borrowRecoverPlans.get(0);
		}
		return null;
	}

	/**
	 * 更新还款完成状态
	 *
	 * @param borrowNid
	 * @param periodNow
	 */
	public void updateBorrowStatus(String borrowNid, Integer periodNow) {
		// 当前时间
		int nowTime = GetDate.getNowTime10();

		// 查询recover
		BorrowRecoverExample recoverExample = new BorrowRecoverExample();
		recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverStatusEqualTo(0).andCreditTimeEqualTo(0);
		int recoverCnt = this.borrowRecoverMapper.countByExample(recoverExample);

		BorrowRepay newBorrowRepay = new BorrowRepay();
		Borrow borrow = selectBorrow(borrowNid);
		BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
		newBrrow.setRepayTimes(borrow.getRepayTimes() + 1); // 还款次数

		// 查询是否有债转数据
		recoverExample = new BorrowRecoverExample();
		recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverStatusEqualTo(0);
		int recoverCnt2 = this.borrowRecoverMapper.countByExample(recoverExample);

		// 如果有债转数据未还款,不更新为已还款
		String repayType = TYPE_WAIT;
		int repayStatus = 0;
		if (recoverCnt2 == 0) {
			repayType = TYPE_WAIT_YES;
			repayStatus = 1;
		}

		// 还款金额
		if (recoverCnt == 0) {
			newBorrowRepay.setRepayType(repayType);
			newBorrowRepay.setRepayStatus(repayStatus); // 已还款
			newBorrowRepay.setRepayYestime(String.valueOf(GetDate.getNowTime10())); // 实际还款时间

			newBrrow.setRepayFullStatus(repayStatus);
			newBrrow.setRepayNextTime(Integer.valueOf(newBorrowRepay.getRepayYestime())); // 实际还款时间
		} else {
			newBorrowRepay.setRepayType(repayType);
			newBorrowRepay.setRepayStatus(repayStatus);// 未还款

			String borrowStyle = borrow.getBorrowStyle();
			if (StringUtils.equals(CustomConstants.BORROW_STYLE_ENDDAY, borrowStyle) || StringUtils.equals(CustomConstants.BORROW_STYLE_END, borrowStyle)) {
				// 不分期的场合，下期还款时间取当前时间
				newBorrowRepay.setRepayTime(String.valueOf(GetDate.getNowTime10())); // 下期还款时间
			} else {
				// 分期的场合，根据项目编号和还款期数从还款计划表中取得还款时间
				com.hyjf.mybatis.model.auto.BorrowRepayPlanExample example = new com.hyjf.mybatis.model.auto.BorrowRepayPlanExample();
				Criteria borrowCriteria = example.createCriteria();
				borrowCriteria.andBorrowNidEqualTo(borrowNid);
				borrowCriteria.andRepayPeriodEqualTo(periodNow + 1);
				List<BorrowRepayPlan> replayPlan = borrowRepayPlanMapper.selectByExample(example);
				if (replayPlan.size() > 0) {
					// 取得下期还款时间
					String repayTime = replayPlan.get(0).getRepayTime();
					// 设置下期还款时间
					newBorrowRepay.setRepayTime(repayTime);
				}

			}
			newBrrow.setRepayNextTime(Integer.valueOf(newBorrowRepay.getRepayTime())); // 下期还款时间
		}
		newBorrowRepay.setRepayDays("0");
		newBorrowRepay.setRepayStep(4);
		newBorrowRepay.setRepayActionTime(String.valueOf(nowTime));

		// 更新BorrowRepay
		BorrowRepayExample repayExample = new BorrowRepayExample();
		repayExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		this.borrowRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample);

		// 更新Borrow
		BorrowExample borrowExample = new BorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		this.borrowMapper.updateByExampleSelective(newBrrow, borrowExample);
	}

	/**
	 * 取得还款信息
	 *
	 * @return
	 */
	private BorrowRepay getBorrowRepay(String borrowNid) {
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayStatusEqualTo(0);
		example.setOrderByClause(" id asc ");
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得总的还款计划表
	 *
	 * @param borrowRepayPlan
	 * @param borrowNid
	 * @param period
	 * @return
	 */
	private BorrowRepayPlan getBorrowRepayPlan(String borrowNid, Integer period) {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayPeriodEqualTo(period);
		List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	public BorrowTender getBorrowTender(Integer tenderId) {
		BorrowTender borrowTender = this.borrowTenderMapper.selectByPrimaryKey(tenderId);
		return borrowTender;
	}

	/**
	 * 判断该收支明细是否存在
	 *
	 * @param accountList
	 * @return
	 */
	private int countAccountListByNid(String nid) {
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("tender_recover_yes");
		return this.accountListMapper.countByExample(accountListExample);
	}

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	private Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 写入收支明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertAccountList(AccountList accountList) {
		// 写入收支明细
		return this.accountListMapper.insertSelective(accountList);
	}

	/**
	 * 判断网站收支是否存在
	 *
	 * @param nid
	 * @return
	 */
	private int countAccountWebList(String nid, String trade) {
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
		return this.accountWebListMapper.countByExample(example);
	}

	/**
	 * 插入网站收支记录
	 *
	 * @param nid
	 * @return
	 */
	private int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门信息
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return 0;
	}

	/**
	 * 设置部门名称
	 *
	 * @param accountWebList
	 */
	private void setDepartments(AccountWebList accountWebList) {
		if (accountWebList != null) {
			Integer userId = accountWebList.getUserId();
			UsersInfo usersInfo = getUsersInfoByUserId(userId);

			if (usersInfo != null) {

				Integer attribute = usersInfo.getAttribute();

				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = getUsersByUserId(userId);

					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}

					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是无主单，全插
					else if (users != null && (attribute == 1)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是有主单
					else if (users != null && (attribute == 0)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
				}
				accountWebList.setTruename(usersInfo.getTruename());
				accountWebList.setFlag(1);
			}
		}
	}

	/**
	 * 查询客户在汇付的余额
	 *
	 * @param usrCustId
	 * @return
	 */
	private BigDecimal getUserBalance(Long usrCustId) {
		BigDecimal balance = BigDecimal.ZERO;

		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG); // 消息类型(必须)
		bean.setUsrCustId(String.valueOf(usrCustId));
		; // 用户客户号(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("用户余额查询"); // 备注
		bean.setLogClient("0"); // PC

		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean != null) {
			try {
				balance = new BigDecimal(chinapnrBean.getAvlBal().replace(",", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return balance;
	}

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	public int updateBorrowRecover(BorrowRecover recoder) {
		int cnt = this.borrowRecoverMapper.updateByPrimaryKeySelective(recoder);
		return cnt;
	}
}
