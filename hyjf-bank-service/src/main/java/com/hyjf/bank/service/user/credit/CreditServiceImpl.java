package com.hyjf.bank.service.user.credit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.bank.service.paginator.WebPaginator;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AccountManagementFeeUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditListCustomize;
import com.hyjf.mybatis.model.customize.web.CreditTenderListCustomize;
import com.hyjf.mybatis.model.customize.web.TenderBorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedStatisticCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditRepayPlanCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditAssignCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.bank.service.evalation.EvaluationService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class CreditServiceImpl extends BaseServiceImpl implements CreditService {
	Logger _log = LoggerFactory.getLogger(CreditServiceImpl.class);

	@Autowired
	private MqService mqService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;

    @Autowired
    private EvaluationService evaluationService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailProcesser;

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	private static DecimalFormat DF_COM_VIEW = new DecimalFormat("######0.00");

	private static String oldOrNewDate = "2016-12-27 20:00:00";

	public static JedisPool pool = RedisUtils.getConnection();

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 前端用户中心可转让债转的数据合计
	 *
	 * @return
	 */
	@Override
	public Integer countTenderToCredit(int userId, int nowTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nowTime", nowTime);
		params.put("userId", userId);
		int total = tenderCreditCustomizeMapper.countTenderToCredit(params);
		return total;
	}

	/**
	 * 前端用户中心可转让债转数据列表
	 *
	 * @return
	 */
	public List<TenderCreditCustomize> selectTenderToCreditList(Map<String, Object> params) {
		List<TenderCreditCustomize> list = tenderCreditCustomizeMapper.selectTenderToCreditList(params);
		return list;
	}

	/**
	 * 验证出借人当天是否可以债转
	 *
	 * @return
	 */
	@Override
	public Integer tenderAbleToCredit(Integer userId, int nowDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nowDate", nowDate);
		params.put("userId", userId);
		Integer creditedNum = tenderCreditCustomizeMapper.tenderAbleToCredit(params);
		return creditedNum;
	}

	/**
	 * 用户中心查询出借可债转详细
	 *
	 * @return
	 */
	@Override
	public List<TenderCreditCustomize> selectTenderToCreditDetail(int userId, int nowTime, String borrowNid, String tenderNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("nowTime", nowTime);
		params.put("borrowNid", borrowNid);
		params.put("tenderNid", tenderNid);
		List<TenderCreditCustomize> tenderToCreditDetail = tenderCreditCustomizeMapper.selectTenderToCreditList(params);
		return tenderToCreditDetail;
	}

	/**
	 * 用户中心查询 债转详细预计服务费计算
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> selectExpectCreditFee(String borrowNid, String tenderNid, String creditDiscount, int nowTime) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取借款信息
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		// 获取borrow_recover数据
		BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
		borrowRecoverCra.andBorrowNidEqualTo(borrowNid).andNidEqualTo(tenderNid);
		List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
		// 债转本息
		BigDecimal creditAccount = BigDecimal.ZERO;
		// 债转期全部利息
		BigDecimal creditInterest = BigDecimal.ZERO;
		// 垫付利息 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
		BigDecimal assignInterestAdvance = BigDecimal.ZERO;
		// 债转利息
		BigDecimal assignPayInterest = BigDecimal.ZERO;
		// 实付金额 承接本金*（1-折价率）+应垫付利息
		BigDecimal assignPay = BigDecimal.ZERO;
		// 预计收益 承接人债转本息—实付金额
		BigDecimal assignInterest = BigDecimal.ZERO;
		// 预计收益 出让人预期收益 =本金+本金持有期利息-本金*折让率-服务费
		BigDecimal expectInterest = BigDecimal.ZERO;
		// 可转本金
		BigDecimal creditCapital = BigDecimal.ZERO;
		// 折后价格
		BigDecimal creditPrice = BigDecimal.ZERO;
		// 服务费
		BigDecimal creditFee = BigDecimal.ZERO;
		if (borrowList != null && borrowList.size() > 0) {

			DebtConfigExample debtConfigExample = new DebtConfigExample();
			List<DebtConfig> debtConfig = debtConfigMapper.selectByExample(debtConfigExample);
			Borrow borrow = borrowList.get(0);
			String borrowStyle = borrow.getBorrowStyle();
			// 年利率
			BigDecimal yearRate = borrow.getBorrowApr().divide(new BigDecimal("100"));
			if (borrowRecoverList != null && borrowRecoverList.size() > 0 && !CollectionUtils.isEmpty(debtConfig)) {
				BorrowRecover borrowRecover = borrowRecoverList.get(0);
				// 可转本金
				creditCapital = borrowRecover.getRecoverCapital().subtract(borrowRecover.getRecoverCapitalYes()).subtract(borrowRecover.getCreditAmount());
				// 折后价格
				creditPrice = creditCapital.multiply(new BigDecimal(1).subtract(new BigDecimal(creditDiscount).divide(new BigDecimal(100)))).setScale(2, BigDecimal.ROUND_DOWN);
				// 到期还本还息和按天计息，到期还本还息
				if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
					int lastDays = 0;
					try {
						String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
						String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRecover.getRecoverTime()));
						lastDays = GetDate.daysBetween(nowDate, recoverDate);
					} catch (NumberFormatException | ParseException e) {
						e.printStackTrace();
					}
					// 剩余天数
					if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
						// 债转本息
						creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 债转期全部利息
						creditInterest = DuePrincipalAndInterestUtils.getDayInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 垫付利息 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
						assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvance(creditCapital, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays + ""));
						// 债转利息
						assignPayInterest = creditInterest;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						assignPay = creditPrice.add(assignInterestAdvance);
						// 预计收益 承接人债转本息—实付金额
						assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
						// 预计收益 出让人预期收益 =本金+本金持有期利息-本金*折让率-服务费
						expectInterest = creditCapital.add(assignInterestAdvance).subtract(creditCapital.multiply(new BigDecimal(creditDiscount).divide(new BigDecimal(100))))
								.subtract(assignPay.multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_DOWN));
					} else {// 按月
						// 债转本息
						creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 债转期全部利息
						creditInterest = DuePrincipalAndInterestUtils.getMonthInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 垫付利息 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
						assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvance(creditCapital, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays + ""));
						// 债转利息
						assignPayInterest = creditInterest;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						assignPay = creditPrice.add(assignInterestAdvance);
						// 预计收益 承接人债转本息—实付金额
						assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
						// 预计到账金额 出让人预期收益 =本金+本金持有期利息-本金*折让率-服务费
						expectInterest = creditCapital.add(assignInterestAdvance).subtract(creditCapital.multiply(new BigDecimal(creditDiscount).divide(new BigDecimal(100))))
								.subtract(assignPay.multiply(debtConfig.get(0).getAttornRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
					}
				}
				// 等额本息和等额本金和先息后本
				if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
					String bidNid = borrow.getBorrowNid();

					BorrowRepayPlanExample example = new BorrowRepayPlanExample();
					BorrowRepayPlanExample.Criteria cra = example.createCriteria();
					cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + 1);
					List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
					int lastDays = 0;
					if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
						try {
							String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
							String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0).getRepayTime()));
							lastDays = GetDate.daysBetween(nowDate, recoverDate);
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
					}
					// 债转本息
					creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(creditCapital, yearRate, borrow.getBorrowPeriod(), borrowRecover.getRecoverPeriod());
					// 每月应还利息
					BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(creditCapital, borrow.getBorrowApr().divide(new BigDecimal(100)), borrow.getBorrowPeriod(),
							borrow.getBorrowPeriod());
					// 债转期全部利息
					creditInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(creditCapital, yearRate, borrow.getBorrowPeriod(), borrowRecover.getRecoverPeriod());
					// 垫付利息 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
					assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(creditCapital, creditCapital, yearRate, interest, new BigDecimal(lastDays + ""));
					// 债转利息
					assignPayInterest = creditInterest;
					// 实付金额 承接本金*（1-折价率）+应垫付利息
					assignPay = creditPrice.add(assignInterestAdvance);
					// 预计收益 承接人债转本息—实付金额
					assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
					// 预计到账金额 出让人预期收益 =本金+本金持有期利息-本金*折让率-服务费
					expectInterest = creditCapital.add(assignInterestAdvance).subtract(creditCapital.multiply(new BigDecimal(creditDiscount).divide(new BigDecimal(100))))
							.subtract(assignPay.multiply(debtConfig.get(0).getAttornRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
				}
				// 服务费
				creditFee = assignPay.multiply(debtConfig.get(0).getAttornRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN);
			}
		}
		resultMap.put("creditAccount", DF_COM_VIEW.format(creditAccount.setScale(2, BigDecimal.ROUND_DOWN)));// 债转本息
		resultMap.put("creditInterest", DF_COM_VIEW.format(creditInterest.setScale(2, BigDecimal.ROUND_DOWN)));// 债转期全部利息
		resultMap.put("assignInterestAdvance", DF_COM_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));// 垫付利息
		resultMap.put("assignPayInterest", DF_COM_VIEW.format(assignPayInterest.setScale(2, BigDecimal.ROUND_DOWN)));// 债转利息
		resultMap.put("assignPay", DF_COM_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));// 实付金额
		resultMap.put("assignInterest", DF_COM_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));// 预计收益
		resultMap.put("creditCapital", DF_COM_VIEW.format(creditCapital.setScale(2, BigDecimal.ROUND_DOWN)));// 可转本金
		resultMap.put("creditPrice", DF_COM_VIEW.format(creditPrice.setScale(2, BigDecimal.ROUND_DOWN)));// 折后价格
		resultMap.put("expectInterest", DF_COM_VIEW.format(expectInterest.setScale(2, BigDecimal.ROUND_DOWN)));// 预计到账金额
		resultMap.put("creditFee", DF_COM_VIEW.format(creditFee));// 服务费
		return resultMap;
	}

	/**
	 * 债转提交保存
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer insertTenderToCredit(int userId, TenderBorrowCreditCustomize tenderBorrowCreditCustomize, String platform) throws Exception {
		// 当前日期
		Integer nowTime = GetDate.getNowTime10();
		// 债转计算
		Map<String, BigDecimal> creditCreateMap = selectExpectCreditFeeForBigDecimal(tenderBorrowCreditCustomize.getBorrowNid(), tenderBorrowCreditCustomize.getTenderNid(),
				tenderBorrowCreditCustomize.getCreditDiscount(), nowTime);
		Integer returnCreditNid = null;
		// 声明要保存的债转对象
		BorrowCredit borrowCredit = new BorrowCredit();

		// 获取borrow_recover数据
		BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
		borrowRecoverCra.andBorrowNidEqualTo(tenderBorrowCreditCustomize.getBorrowNid());
		borrowRecoverCra.andNidEqualTo(tenderBorrowCreditCustomize.getTenderNid());
		// 获取还款数据
		List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);

		// 获取borrow数据
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(tenderBorrowCreditCustomize.getBorrowNid());
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);

		if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
			BorrowRecover borrowRecover = borrowRecoverList.get(0);
			// 生成creditNid
			String nowDate = (GetDate.yyyyMMdd.format(new Date()) != null && !"".equals(GetDate.yyyyMMdd.format(new Date()))) ? GetDate.yyyyMMdd.format(new Date()) : "0";// 获取当前时间的日期
			Integer creditNumToday = this.tenderAbleToCredit(null, Integer.parseInt(nowDate)) != null ? this.tenderAbleToCredit(null, Integer.parseInt(nowDate)) : 0;
			String creditNid = nowDate.substring(2) + String.format("%04d", (creditNumToday + 1));
			// 获取待债转数据
			List<TenderCreditCustomize> tenderToCreditList = this.selectTenderToCreditDetail(userId, nowTime, tenderBorrowCreditCustomize.getBorrowNid(), tenderBorrowCreditCustomize.getTenderNid());
			borrowCredit.setCreditNid(Integer.parseInt(creditNid));// 债转nid
			borrowCredit.setCreditUserId(userId);// 转让用户id
			int lastdays = 0;
			int holddays = 0;
			if (tenderToCreditList != null && tenderToCreditList.size() > 0) {
				TenderCreditCustomize temp = tenderToCreditList.get(0);
				List<Borrow> borrows = this.searchBorrowList(temp.getBorrowNid());
				if (borrows != null && borrows.size() > 0) {
					String borrowStyle = borrows.get(0).getBorrowStyle();
					// 到期还本还息和按天计息，到期还本还息
					if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
						try {
							String nowDateStr = GetDate.getDateTimeMyTimeInMillis(nowTime);
							String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRecover.getRecoverTime()));
							String hodeDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRecover.getCreateTime()));
							lastdays = GetDate.daysBetween(nowDateStr, recoverDate);
							holddays = GetDate.daysBetween(hodeDate, nowDateStr);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					// 等额本息和等额本金和先息后本
					if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						String bidNid = borrows.get(0).getBorrowNid();
						BorrowRepayPlanExample example = new BorrowRepayPlanExample();
						BorrowRepayPlanExample.Criteria cra = example.createCriteria();
						cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrows.get(0).getBorrowPeriod());
						List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
						if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
							try {
								String hodeDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRecover.getCreateTime()));
								lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0).getRepayTime())));
								holddays = GetDate.daysBetween(hodeDate, GetDate.getDateTimeMyTimeInMillis(nowTime));
							} catch (NumberFormatException | ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
				borrowCredit.setBidNid(tenderToCreditList.get(0).getBorrowNid());// 原标nid
				borrowCredit.setBidApr(new BigDecimal(tenderToCreditList.get(0).getBorrowApr()));// 原标年化利率
				borrowCredit.setBidName(tenderToCreditList.get(0).getBorrowName());// 原标标题
				borrowCredit.setTenderNid(tenderToCreditList.get(0).getTenderNid());// 投标nid
				borrowCredit.setCreditStatus(0);// 转让状态 0.进行中,1.停止
				borrowCredit.setCreditOrder(0);// 排序
				borrowCredit.setCreditTerm(lastdays);// 债转期限-天
				borrowCredit.setCreditTermHold(holddays);
				borrowCredit.setCreditPeriod(borrowRecover.getRecoverPeriod());// 剩余的债转期数-期
				borrowCredit.setClient(Integer.parseInt(platform));// 客户端
				borrowCredit.setCreditCapital(creditCreateMap.get("creditCapital"));// 债转本金
				borrowCredit.setCreditAccount(creditCreateMap.get("creditAccount"));// 债转总额
				borrowCredit.setCreditInterest(creditCreateMap.get("creditInterest"));// 债转总利息
				borrowCredit.setCreditInterestAdvance(creditCreateMap.get("assignInterestAdvance"));// 需垫付利息
				borrowCredit.setCreditDiscount(new BigDecimal(tenderBorrowCreditCustomize.getCreditDiscount()));// 折价率
				borrowCredit.setCreditIncome(creditCreateMap.get("assignPay"));// 总收入,
				borrowCredit.setCreditFee(creditCreateMap.get("assignPay").multiply(StringUtils.isEmpty(tenderBorrowCreditCustomize.getAttornRate())?new BigDecimal(0.01):new BigDecimal(tenderBorrowCreditCustomize.getAttornRate()).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));// 服务费
				borrowCredit.setCreditPrice(creditCreateMap.get("creditPrice"));// 出让价格
				borrowCredit.setCreditCapitalAssigned(BigDecimal.ZERO);// 已认购本金
				borrowCredit.setCreditInterestAssigned(BigDecimal.ZERO);// 已承接部分的利息
				borrowCredit.setCreditInterestAdvanceAssigned(BigDecimal.ZERO);// 已垫付利息
				borrowCredit.setCreditRepayAccount(BigDecimal.ZERO);// 已还款总额
				borrowCredit.setCreditRepayCapital(BigDecimal.ZERO);// 已还本金
				borrowCredit.setCreditRepayInterest(BigDecimal.ZERO);// 已还利息
				borrowCredit.setCreditRepayEndTime(Integer.parseInt(GetDate.get10Time(tenderToCreditList.get(0).getRepayLastTime())));// 债转最后还款日
				borrowCredit.setCreditRepayLastTime(Integer.parseInt(borrowRecover.getRecoverYestime() != null ? borrowRecover.getRecoverYestime() : String.valueOf(nowTime)));// 上次还款日
				borrowCredit.setCreditRepayNextTime(Integer.parseInt(borrowRecover.getRecoverTime() != null ? borrowRecover.getRecoverTime() : String.valueOf(nowTime)));// 下次还款日
				borrowCredit.setCreditRepayYesTime(0);// 最终实际还款日
			}
			borrowCredit.setCreateDate(Integer.parseInt(GetDate.yyyyMMdd.format(new Date())));// 创建日期
			borrowCredit.setAddTime(nowTime);// 创建时间
			borrowCredit.setEndTime(nowTime + 24 * 3600 * 3);// 结束时间
			borrowCredit.setAssignTime(0);// 认购时间
			borrowCredit.setAssignNum(0);// 出借次数
			borrowCredit.setRepayStatus(0);// 还款状态 0还款中、1已还款、2还款失败
			_log.info("重新计算后的服务费========"+borrowCredit.getCreditFee());
			if (borrowList != null && borrowList.size() > 0) {
				if (borrowList.get(0).getBorrowStyle().equals("endmonth")) {
					borrowCredit.setRecoverPeriod(borrowList.get(0).getBorrowPeriod() - borrowRecover.getRecoverPeriod());// 从第几期开始
				} else {
					borrowCredit.setRecoverPeriod(0);// 从第几期开始
				}
			} else {
				borrowCredit.setRecoverPeriod(0);// 从第几期开始
			}
			_log.info("重新计算后的服务费========insertResultFlag:"+JSONObject.toJSONString(borrowCredit));
			boolean insertResultFlag = this.borrowCreditMapper.insertSelective(borrowCredit) > 0 ? true : false;
			_log.info("重新计算后的服务费========insertResultFlag:"+insertResultFlag);
			if (insertResultFlag) {
				returnCreditNid = borrowCredit.getCreditNid();
				// 还款表更新债转时间
				borrowRecover.setCreditTime(nowTime);
				boolean isUpdateFlag = this.borrowRecoverMapper.updateByPrimaryKey(borrowRecover) > 0 ? true : false;
				if (!isUpdateFlag) {
					throw new Exception("更新huiyingdai_borrow_recover表数据失败~!");
				}
				return returnCreditNid;
			} else {
				throw new Exception("插入huiyingdai_borrow_credit表数据失败~!");
			}
		} else {
			return 0;
		}
	}

	/**
	 * 用户中心查询转让中债转数据合计
	 *
	 * @return
	 */
	@Override
	public Integer countCreditInProgress(int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("creditStatus", "0");
		int total = tenderCreditCustomizeMapper.countCreditByStatus(params);
		return total;
	}

	/**
	 * 用户中心查询转让中债转列表资源
	 *
	 * @return
	 */
	@Override
	public List<TenderCreditDetailCustomize> selectCreditInProgress(int userId, int offset, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("creditStatus", "0");
		params.put("limitStart", offset);
		params.put("limitEnd", limit);
		List<TenderCreditDetailCustomize> list = tenderCreditCustomizeMapper.selectCreditByStatus(params);
		return list;
	}

	/**
	 * 用户中心查询已完成债转数据合计
	 *
	 * @return
	 */
	@Override
	public Integer countCreditStop(int userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("creditStatus", "12");// 此处查非零
		int total = tenderCreditCustomizeMapper.countCreditByStatus(params);
		return total;
	}

	/**
	 * 用户中心查询已完成债转列表资源
	 *
	 * @return
	 */
	@Override
	public List<TenderCreditDetailCustomize> selectCreditStop(int userId, int offset, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("creditStatus", "12");// 此处查非零
		params.put("limitStart", offset);
		params.put("limitEnd", limit);
		List<TenderCreditDetailCustomize> list = tenderCreditCustomizeMapper.selectCreditByStatus(params);
		return list;
	}

	/**
	 * 用户中心查询 已承接 债转资源出借详情统计
	 *
	 * @return
	 */
	@Override
	public int countCreditAssigned(Map<String, Object> params) {
		int count = tenderCreditCustomizeMapper.countCreditTenderAssigned(params);
		return count;
	}

	/**
	 * 用户中心查询 已承接 债转资源出借列表
	 *
	 * @return
	 */
	@Override
	public List<TenderCreditAssignedCustomize> selectCreditAssigned(Map<String, Object> params) {
		List<TenderCreditAssignedCustomize> list = tenderCreditCustomizeMapper.selectCreditTenderAssigned(params);
		try {
			if (list != null && list.size() > 0) {
				// 对重要参数进行MD5加密
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setUserId(TreeDESUtils.getEncrypt(String.valueOf(params.get("timestamp")), String.valueOf(list.get(i).getUserId())));
					list.get(i).setAssignNidMD5(TreeDESUtils.getEncrypt(String.valueOf(params.get("timestamp")), String.valueOf(list.get(i).getAssignNid())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 *
	 * 债转承接数据统计
	 *
	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public List<TenderCreditAssignedStatisticCustomize> selectCreditTenderAssignedStatistic(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectCreditTenderAssignedStatistic(params);
	}

	/**
	 * 用户中心查询 已承接 还款计划列表资源
	 *
	 * @return
	 */
	@Override
	public List<TenderCreditRepayPlanCustomize> selectCreditRepayPlanList(Map<String, Object> params) {
		List<TenderCreditRepayPlanCustomize> list = tenderCreditCustomizeMapper.selectCreditRepayPlan(params);
		return list;
	}

	/**
	 * 用户中心债转被出借的协议
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> selectUserCreditContract(CreditAssignedBean tenderCreditAssignedBean) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 当前登陆者ID
		Integer currentUserId = tenderCreditAssignedBean.getCurrentUserId();
		// 获取债转出借信息
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andAssignNidEqualTo(tenderCreditAssignedBean.getAssignNid()).andBidNidEqualTo(tenderCreditAssignedBean.getBidNid()).andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid())
				.andCreditTenderNidEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			CreditTender creditTender = creditTenderList.get(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creditNid", creditTender.getCreditNid());
			params.put("assignNid", creditTender.getAssignNid());
			List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectWebCreditTenderDetailForContract(params);
			if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
				if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
					tenderToCreditDetailList.get(0).setCreditRepayEndTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0).getCreditRepayEndTime())));
				}
				if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
					try {
						tenderToCreditDetailList.get(0).setCreditTime(GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				resultMap.put("tenderToCreditDetail", tenderToCreditDetailList.get(0));
			}
			// 获取借款标的信息
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(creditTender.getBidNid());
			List<Borrow> borrow = this.borrowMapper.selectByExample(borrowExample);
			// 获取债转信息
			BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
			BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
			borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(tenderCreditAssignedBean.getCreditNid())).andBidNidEqualTo(tenderCreditAssignedBean.getBidNid())
					.andTenderNidEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
			List<BorrowCredit> borrowCredit = this.borrowCreditMapper.selectByExample(borrowCreditExample);
			// 获取承接人身份信息
			UsersInfoExample usersInfoExample = new UsersInfoExample();
			UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
			usersInfoCra.andUserIdEqualTo(creditTender.getUserId());
			List<UsersInfo> usersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
			// 获取承接人平台信息
			UsersExample usersExample = new UsersExample();
			UsersExample.Criteria usersCra = usersExample.createCriteria();
			usersCra.andUserIdEqualTo(creditTender.getUserId());
			List<Users> users = this.usersMapper.selectByExample(usersExample);
			// 获取融资方平台信息
			UsersExample usersBorrowExample = new UsersExample();
			UsersExample.Criteria usersBorrowCra = usersBorrowExample.createCriteria();
			usersBorrowCra.andUserIdEqualTo(borrow.get(0).getUserId());
			List<Users> usersBorrow = this.usersMapper.selectByExample(usersBorrowExample);
			// 获取债转人身份信息
			UsersInfoExample usersInfoExampleCredit = new UsersInfoExample();
			UsersInfoExample.Criteria usersInfoCraCredit = usersInfoExampleCredit.createCriteria();
			usersInfoCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
			List<UsersInfo> usersInfoCredit = this.usersInfoMapper.selectByExample(usersInfoExampleCredit);
			// 获取债转人平台信息
			UsersExample usersExampleCredit = new UsersExample();
			UsersExample.Criteria usersCraCredit = usersExampleCredit.createCriteria();
			usersCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
			List<Users> usersCredit = this.usersMapper.selectByExample(usersExampleCredit);
			// 将int类型时间转成字符串
			creditTender.setAddTime(GetDate.times10toStrYYYYMMDD(Integer.valueOf(creditTender.getAddTime())));
			creditTender.setAddip(GetDate.getDateMyTimeInMillis(creditTender.getAssignRepayEndTime()));// 借用ip字段存储最后还款时间
			resultMap.put("creditTender", creditTender);
			if (borrow != null && borrow.size() > 0) {
				if (borrow.get(0).getReverifyTime() != null) {
					borrow.get(0).setReverifyTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getReverifyTime())));
				}
				if (borrow.get(0).getRepayLastTime() != null) {
					borrow.get(0).setRepayLastTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getRepayLastTime())));
				}
				resultMap.put("borrow", borrow.get(0));
				// 获取借款人信息
				UsersInfoExample usersInfoExampleBorrow = new UsersInfoExample();
				UsersInfoExample.Criteria usersInfoCraBorrow = usersInfoExampleBorrow.createCriteria();
				usersInfoCraBorrow.andUserIdEqualTo(borrow.get(0).getUserId());
				List<UsersInfo> usersInfoBorrow = this.usersInfoMapper.selectByExample(usersInfoExampleBorrow);
				if (usersInfoBorrow != null && usersInfoBorrow.size() > 0) {
					if (usersInfoBorrow.get(0).getTruename().length() > 1) {
						if (usersInfoBorrow.get(0).getUserId().equals(currentUserId)) {
							usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename());
						} else {
							usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename().substring(0, 1) + "**");
						}
					}
					if (usersInfoBorrow.get(0).getIdcard().length() > 8) {
						if (usersInfoBorrow.get(0).getUserId().equals(currentUserId)) {
							usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard());
						} else {
							usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard().substring(0, 4) + "**************");
						}
					}
					resultMap.put("usersInfoBorrow", usersInfoBorrow.get(0));
				}
			}
			if (borrowCredit != null && borrowCredit.size() > 0) {
				resultMap.put("borrowCredit", borrowCredit.get(0));
			}

			// 因为使用userid查询，所以只能有一条记录，取get(0)
			if (usersInfo != null && usersInfo.size() > 0) {
				if (usersInfo.get(0).getTruename().length() > 1) {
					if (usersInfo.get(0).getUserId().equals(currentUserId)) {
						// 本人信息不加密
						usersInfo.get(0).setTruename(usersInfo.get(0).getTruename());
					} else {

						usersInfo.get(0).setTruename(usersInfo.get(0).getTruename().substring(0, 1) + "**");
					}
				}
				if (usersInfo.get(0).getIdcard().length() > 8) {

					if (usersInfo.get(0).getUserId().equals(currentUserId)) {
						// 本人信息不加密
						usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard());
					} else {
						// 非本人加密
						usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard().substring(0, 4) + "**************");
					}
				}
				resultMap.put("usersInfo", usersInfo.get(0));
			}

			if (usersBorrow != null && usersBorrow.size() > 0) {
				if (usersBorrow.get(0).getUsername().length() > 1) {
					if (usersBorrow.get(0).getUserId().equals(currentUserId)) {
						usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername());
					} else {
						usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername().substring(0, 1) + "*****");
					}
				}
				resultMap.put("usersBorrow", usersBorrow.get(0));
			}

			if (users != null && users.size() > 0) {
				if (users.get(0).getUsername().length() > 1) {

					if (users.get(0).getUserId().equals(currentUserId)) {
						users.get(0).setUsername(users.get(0).getUsername());
					} else {
						users.get(0).setUsername(users.get(0).getUsername().substring(0, 1) + "*****");
					}
				}
				resultMap.put("users", users.get(0));
			}

			if (usersCredit != null && usersCredit.size() > 0) {
				if (usersCredit.get(0).getUsername().length() > 1) {
					if (usersCredit.get(0).getUserId().equals(currentUserId)) {
						usersCredit.get(0).setUsername(usersCredit.get(0).getUsername());
					} else {
						usersCredit.get(0).setUsername(usersCredit.get(0).getUsername().substring(0, 1) + "*****");
					}
				}
				resultMap.put("usersCredit", usersCredit.get(0));
			}


			//甲方(原债权人)
			if (usersInfoCredit != null && usersInfoCredit.size() > 0) {
				if (usersInfoCredit.get(0).getTruename().length() > 1) {
					if (usersInfoCredit.get(0).getUserId().equals(currentUserId)) {
						usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename());
					} else {
						usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename().substring(0, 1) + "**");
					}
				}

				if (usersInfoCredit.get(0).getIdcard().length() > 8) {
					if (usersInfoCredit.get(0).getUserId().equals(currentUserId)) {
						usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard());
					} else {
						usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard().substring(0, 4) + "**************");
					}
				}
				resultMap.put("usersInfoCredit", usersInfoCredit.get(0));
			}

			String phpWebHost = PropUtils.getSystem("hyjf.web.host.php");
			if (StringUtils.isNotEmpty(phpWebHost)) {
				resultMap.put("phpWebHost", phpWebHost);
			} else {
				resultMap.put("phpWebHost", "http://site.hyjf.com");
			}
		}
		return resultMap;
	}

	/**
	 * 前端Web页面查询汇转让数据合计(包含查询条件)
	 *
	 * @return
	 */
	@Override
	@Cached(name="webHomeCreditCntCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 5, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
	public Integer countWebCredit(Map<String, Object> params) {
		params.put("creditStatus", "0");
		int total = tenderCreditCustomizeMapper.countWebCredit(params);
		return total;
	}

	/**
	 * 前端Web页面查询汇转让数据列表(包含查询条件)
	 *
	 * @return
	 */
	@Override
	@Cached(name="webHomeCreditListCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 10, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
	public List<TenderCreditDetailCustomize> selectWebCreditList(Map<String, Object> params, int offset, int limit) {
		params.put("creditStatus", "0");
		params.put("limitStart", offset);
		params.put("limitEnd", limit);
		List<TenderCreditDetailCustomize> list = tenderCreditCustomizeMapper.selectWebCreditList(params);
		return list;
	}

	@Override
	public TenderToCreditDetailCustomize selectWebCreditDetail(String creditNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creditNid", creditNid);
		List<TenderToCreditDetailCustomize> creditDetailList = tenderCreditCustomizeMapper.selectWebCreditTenderDetail(params);
		if (creditDetailList != null && creditDetailList.size() == 1) {
			TenderToCreditDetailCustomize creditDetail = creditDetailList.get(0);
			creditDetail.setCreditTimeInt(creditDetail.getCreditTimeInt() + 3 * 24 * 3600);
			return creditDetail;
		}
		return null;
	}

	/**
	 * 前端Web页面出借可债转出借-还款计划查询(包含查询条件)
	 *
	 */
	@Override
	public Map<String, Object> selectWebCreditRepayList(String borrowNid, Integer currPage, Integer limitPage) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BigDecimal repayed = BigDecimal.ZERO;
		BigDecimal redayRepay = BigDecimal.ZERO;
		// 获取项目详情
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		if (borrowList != null && borrowList.size() > 0) {
			String borrowStyle = borrowList.get(0).getBorrowStyle();
			// 到期还本还息和按天计息，到期还本还息
			if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
				// 借款人还款信息
				BorrowRepayExample borrowRepayExample = new BorrowRepayExample();
				BorrowRepayExample.Criteria borrowRepayCra = borrowRepayExample.createCriteria();
				borrowRepayCra.andBorrowNidEqualTo(borrowNid);
				int count = this.borrowRepayMapper.countByExample(borrowRepayExample);
				WebPaginator paginator = new WebPaginator(currPage, count, limitPage, "repayPage", null);
				borrowRepayExample.setLimitStart(paginator.getOffset());
				borrowRepayExample.setLimitEnd(paginator.getLimit());
				List<BorrowRepay> borrowRepayList = this.borrowRepayMapper.selectByExample(borrowRepayExample);
				List<BorrowRepay> borrowRepayPlanList = new ArrayList<BorrowRepay>();
				if (borrowRepayList != null && borrowRepayList.size() > 0) {
					for (BorrowRepay borrowRepay : borrowRepayList) {
						borrowRepay.setRepayTime(GetDate.formatDate(GetDate.getDate(Integer.parseInt(borrowRepay.getRepayTime()) * 1000L)));
						if (borrowRepay.getRepayStatus() == 0) {
							repayed = repayed.add(borrowRepay.getRepayAccount());
						} else {
							redayRepay = redayRepay.add(borrowRepay.getRepayAccount());
						}
						borrowRepayPlanList.add(borrowRepay);
						// 向上取整，利息,预留
						// borrowRepay.setRepayInterest(borrowRepay.getRepayInterest().setScale(0,
						// BigDecimal.ROUND_HALF_UP));
					}
				}
				resultMap.put("repayPlan", borrowRepayPlanList);
				resultMap.put("repayed", repayed);
				resultMap.put("redayRepay", redayRepay);
				resultMap.put("repayPage", paginator);
			}
			// 等额本息和等额本金和先息后本
			if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
				// 还款信息
				BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
				BorrowRepayPlanExample.Criteria borrowRepayPlanCra = borrowRepayPlanExample.createCriteria();
				borrowRepayPlanCra.andBorrowNidEqualTo(borrowNid);
				List<BorrowRepayPlan> borrowRepayList = this.borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
				int count = this.borrowRepayPlanMapper.countByExample(borrowRepayPlanExample);
				WebPaginator paginator = new WebPaginator(currPage, count, limitPage, "repayPage", null);
				borrowRepayPlanExample.setLimitStart(paginator.getOffset());
				borrowRepayPlanExample.setLimitEnd(paginator.getLimit());
				List<BorrowRepayPlan> borrowRepayListPage = this.borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
				List<BorrowRepayPlan> borrowRepayPlanList = new ArrayList<BorrowRepayPlan>();
				// 全部数据遍历
				if (borrowRepayList != null && borrowRepayList.size() > 0) {
					for (BorrowRepayPlan borrowRepayPlan : borrowRepayList) {
						if (borrowRepayPlan.getStatus() == 0) {
							repayed = repayed.add(borrowRepayPlan.getRepayAccount());
						} else {
							redayRepay = redayRepay.add(borrowRepayPlan.getRepayAccount());
						}
					}
					resultMap.put("repayed", repayed);
					resultMap.put("redayRepay", redayRepay);
				}
				// 分页遍历
				if (borrowRepayListPage != null && borrowRepayListPage.size() > 0) {
					for (BorrowRepayPlan borrowRepayPlan : borrowRepayListPage) {
						borrowRepayPlan.setRepayTime(GetDate.formatDate(GetDate.getDate(Integer.parseInt(borrowRepayPlan.getRepayTime()) * 1000L)));
						if (borrowRepayPlan.getStatus() == 0) {
							repayed = repayed.add(borrowRepayPlan.getRepayAccount());
						} else {
							redayRepay = redayRepay.add(borrowRepayPlan.getRepayAccount());
						}
						borrowRepayPlanList.add(borrowRepayPlan);
					}
					resultMap.put("repayPlan", borrowRepayPlanList);
					resultMap.put("repayPage", paginator);
				}
			}
		}
		return resultMap;
	}

	/**
	 * 前端Web页面出借可债转出借-出借记录查询(包含查询条件)
	 *
	 */
	@Override
	public Map<String, Object> selectWebBorrowTenderList(String borrowNid, Integer currPage, Integer limitPage) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BigDecimal tendTotal = BigDecimal.ZERO;
		// 获取项目详情
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		if (borrowList != null && borrowList.size() > 0) {
			// 出借信息
			BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
			BorrowTenderExample.Criteria borrowTenderCra = borrowTenderExample.createCriteria();
			borrowTenderCra.andBorrowNidEqualTo(borrowNid).andStatusEqualTo(1);
			List<BorrowTender> borrowTenderList = this.borrowTenderMapper.selectByExample(borrowTenderExample);
			int count = this.borrowTenderMapper.countByExample(borrowTenderExample);
			WebPaginator paginator = new WebPaginator(currPage, count, limitPage, "tenderPage", null);
			borrowTenderExample.setLimitStart(paginator.getOffset());
			borrowTenderExample.setLimitEnd(paginator.getLimit());
			List<BorrowTender> borrowTenderListPage = this.borrowTenderMapper.selectByExample(borrowTenderExample);
			List<BorrowTender> borrowTenderList2 = new ArrayList<BorrowTender>();
			// 遍历全部
			if (borrowTenderList != null && borrowTenderList.size() > 0) {
				for (BorrowTender borrowTender : borrowTenderList) {
					tendTotal = tendTotal.add(borrowTender.getAccount());
				}
				resultMap.put("tendTotal", tendTotal);
			}
			// 分页遍历
			if (borrowTenderListPage != null && borrowTenderListPage.size() > 0) {
				for (BorrowTender borrowTender : borrowTenderListPage) {
					borrowTender.setOrderDate(GetDate.timestamptoStrYYYYMMDDHHMMSS(borrowTender.getAddtime()));
					// 获取用户信息
					UsersInfoExample usersInfoExample = new UsersInfoExample();
					UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
					usersInfoCra.andUserIdEqualTo(borrowTender.getUserId());
					List<UsersInfo> userInfoList = this.usersInfoMapper.selectByExample(usersInfoExample);
					borrowTender.setTenderUserName(userInfoList != null && userInfoList.size() > 0 ? userInfoList.get(0).getTruename() : "未知");
					borrowTenderList2.add(borrowTender);
					tendTotal = tendTotal.add(borrowTender.getAccount());
				}
				resultMap.put("tenders", borrowTenderList2);
				resultMap.put("tenderNum", count);
				resultMap.put("tenderPage", paginator);
			}
		}
		return resultMap;
	}

	public Map<String, Object> selectWebCreditTenderList(String creditNid, Integer currPage, Integer limitPage) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BigDecimal tendTotal = BigDecimal.ZERO;
		// 获取债转出借信息
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria creditTender = creditTenderExample.createCriteria();
		creditTender.andCreditNidEqualTo(creditNid);
		// 获取出借的总数
		int count = this.creditTenderMapper.countByExample(creditTenderExample);
		// 声明分页对象
		WebPaginator paginator = new WebPaginator(currPage, count, limitPage, "tenderPage", null);
		// 分页查询
		creditTenderExample.setLimitStart(paginator.getOffset());
		creditTenderExample.setLimitEnd(paginator.getLimit());
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
		List<CreditTender> creditTenderList2 = new ArrayList<CreditTender>();
		if (creditTenderList != null && creditTenderList.size() > 0) {
			// 遍历全部
			for (CreditTender creditTender_ : creditTenderList) {
				tendTotal = tendTotal.add(creditTender_.getAssignCapital());
				// 获取用户信息
				UsersExample usersExample = new UsersExample();
				UsersExample.Criteria usersCra = usersExample.createCriteria();
				usersCra.andUserIdEqualTo(creditTender_.getUserId());
				List<Users> userList = this.usersMapper.selectByExample(usersExample);
				creditTender_.setAddip(userList != null && userList.size() > 0 ? userList.get(0).getUsername().substring(0, 2) + "***" : "*");
				creditTender_.setAddTime(GetDate.timestamptoStrYYYYMMDDHHMMSS(creditTender_.getAddTime()));
				creditTenderList2.add(creditTender_);
			}
			resultMap.put("tendTotal", tendTotal);
			resultMap.put("tenders", creditTenderList2);
			resultMap.put("tenderNum", count);
			resultMap.put("tenderPage", paginator);
		} else {
			resultMap.put("tendTotal", 0.00);
			resultMap.put("tenders", null);
			resultMap.put("tenderNum", 0);
			resultMap.put("tenderPage", null);
		}
		return resultMap;
	}

	/**
	 * 前端Web页面出借可债转输入出借金额后收益提示(包含查询条件)
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> webCheckCreditTenderAssign(Integer userId, String creditNid, String assignCapital) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria accountCra = accountExample.createCriteria();
		accountCra.andUserIdEqualTo(userId);
		List<Account> accountList = this.accountMapper.selectByExample(accountExample);
		if (accountList != null && accountList.size() > 0) {
			if (accountList.get(0).getBankBalance().compareTo(new BigDecimal(assignCapital)) == -1) {
				resultMap.put("msg", "余额不足");
				return resultMap;
			}
		}
		// 获取债转数据
		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
		borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid));
		List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
		if (borrowCreditList != null && borrowCreditList.size() > 0) {
			BorrowCredit borrowCredit = borrowCreditList.get(0);
			if (borrowCredit.getCreditUserId().intValue() != userId.intValue()) {
				// 获取borrow_recover数据
				BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
				BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
				borrowRecoverCra.andBorrowNidEqualTo(borrowCredit.getBidNid()).andNidEqualTo(borrowCredit.getTenderNid());
				List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
				if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
					// 获取借款数据
					BorrowExample borrowExample = new BorrowExample();
					BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
					borrowCra.andBorrowNidEqualTo(borrowCredit.getBidNid());
					List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
					TenderToCreditAssignCustomize tenderToCreditAssign = new TenderToCreditAssignCustomize();
					// 计算折后价格
					BigDecimal assignPrice = new BigDecimal(assignCapital).setScale(2, BigDecimal.ROUND_DOWN).subtract(
							new BigDecimal(assignCapital).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal("100"), 18, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN));
					BigDecimal yearRate = borrowCredit.getBidApr().divide(new BigDecimal("100"));
					if (borrowList != null && borrowList.size() > 0) {
						Borrow borrow = borrowList.get(0);
						// 获取标的借款人
						Integer borrowUserId = borrow.getUserId();
						if (borrowUserId.intValue() == userId.intValue()) {
							resultMap.put("msg", "承接人不能为借款人");
							resultMap.put("resultType", "1");
							return resultMap;
						}
						String borrowStyle = borrow.getBorrowStyle();
						// 剩余待承接本金
						BigDecimal sellerCapitalWait = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
						// 待承接的待收收益
						BigDecimal sellerInterestWait = borrowCredit.getCreditInterest().subtract(borrowCredit.getCreditInterestAssigned());
						// 待垫付的垫付利息
						BigDecimal sellerInterestAdvanceWait = borrowCredit.getCreditInterestAdvance().subtract(borrowCredit.getCreditInterestAdvanceAssigned());
						// 到期还本还息和按天计息，到期还本还息
						if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
							int lastDays = borrowCredit.getCreditTerm();// 剩余天数
							if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
								// 债转本息
								BigDecimal creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait,
										yearRate, borrow.getBorrowPeriod());
								// 垫付利息
								// 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
								BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
										sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								BigDecimal assignPay = assignPrice.add(assignInterestAdvance);
								String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
										+ DF_FOR_VIEW.format(assignPay) + "元";
								// 预计收益 承接人债转本息—实付金额
								BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
								tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
								tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
								tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
								tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital()));
								tenderToCreditAssign.setAssignCapital(assignCapital);
								tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()));
								tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice));
								tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay));
								tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest));
								tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance));
								tenderToCreditAssign.setAssignPayText(assignPayText);
							} else {// 按月
								// 债转本息
								BigDecimal creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait,
										yearRate, borrow.getBorrowPeriod());
								// 垫付利息
								// 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
								BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
										sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								BigDecimal assignPay = assignPrice.add(assignInterestAdvance);
								String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
										+ DF_FOR_VIEW.format(assignPay) + "元";
								// 预计收益 承接人债转本息—实付金额
								BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
								tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
								tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
								tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
								tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
								tenderToCreditAssign.setAssignCapital(assignCapital);
								tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
								tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
								tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
								tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
								tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
								tenderToCreditAssign.setAssignPayText(assignPayText);
							}
						}
						// 等额本息和等额本金和先息后本
						if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
							int lastDays = 0;
							int nowTime = GetDate.getNowTime10();
							String bidNid = borrow.getBorrowNid();
							BorrowRepayPlanExample example = new BorrowRepayPlanExample();
							BorrowRepayPlanExample.Criteria cra = example.createCriteria();
							cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrowCredit.getRecoverPeriod() + 1);
							List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
							if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
								try {
									String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
									String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0).getRepayTime()));
									lastDays = GetDate.daysBetween(nowDate, recoverDate);
								} catch (NumberFormatException | ParseException e) {
									e.printStackTrace();
								}
							}
							// 已还多少期
							int repayPeriod = borrowCredit.getRecoverPeriod();
							// 应还总本息
							BigDecimal creditAccount = BigDecimal.ZERO;
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							BigDecimal assignPay = BigDecimal.ZERO;
							// 垫付利息
							// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
							BigDecimal assignInterestAdvance = BigDecimal.ZERO;
							if (new BigDecimal(assignCapital).compareTo(sellerCapitalWait) == 0) {
								// 最后一笔承接
								creditAccount = sellerCapitalWait.add(sellerInterestWait);
								// 剩余垫付利息
								assignInterestAdvance = sellerInterestAdvanceWait;
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								assignPay = assignPrice.add(assignInterestAdvance);
							} else {
								// 应还总本息
								creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(new BigDecimal(assignCapital), yearRate, borrow.getBorrowPeriod(),
										borrow.getBorrowPeriod());
								// 承接人每月应还利息
								BigDecimal interestAssign = BeforeInterestAfterPrincipalUtils.getPerTermInterest(new BigDecimal(assignCapital), borrowCredit.getBidApr().divide(new BigDecimal(100)),
										borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
								// 出让人每月应还利息
								BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(borrowCredit.getCreditCapital(), borrowCredit.getBidApr().divide(new BigDecimal(100)),
										borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
								// 应还总额
								creditAccount = creditAccount.subtract(interestAssign.multiply(new BigDecimal(repayPeriod)));
								// 垫付利息
								// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
								assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(new BigDecimal(assignCapital), borrowCredit.getCreditCapital(), yearRate, interest,
										new BigDecimal(lastDays + ""));
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								assignPay = assignPrice.add(assignInterestAdvance);
							}
							String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
									+ DF_FOR_VIEW.format(assignPay) + "元";
							// 预计收益 承接人债转本息—实付金额
							BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
							tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
							tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
							tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
							tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignCapital(assignCapital);
							tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPayText(assignPayText);
						}
						resultMap.put("tenderToCreditAssign", tenderToCreditAssign);
						if (new BigDecimal(tenderToCreditAssign.getAssignPay().replace(",", "")).compareTo(accountList.get(0).getBankBalance()) > 0) {
							resultMap.put("msg", "垫付利息可用余额不足，请充值");
							resultMap.put("resultType", "1");
						} else {
							resultMap.put("msg", "认购债权确认");
							resultMap.put("resultType", "0");
						}
					}
				} else {
					// 债转人不能购买自己的债转
					resultMap.put("msg", "未查询到用户的放款记录。");
					resultMap.put("resultType", "1");
				}
			} else {
				// 债转人不能购买自己的债转
				resultMap.put("msg", "不可以承接自己出让的债权。");
				resultMap.put("resultType", "1");
			}
		}
		return resultMap;
	}

	/**
	 * 前端Web页面出借可债转输入出借金额后收益提示 用户未登录 (包含查询条件)
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> webCheckCreditTenderAssignWithoutLogin(String creditNid, String assignCapital) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取债转数据
		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
		borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid));
		List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
		if (borrowCreditList != null && borrowCreditList.size() > 0) {
			BorrowCredit borrowCredit = borrowCreditList.get(0);
			// 获取borrow_recover数据
			BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
			BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
			borrowRecoverCra.andBorrowNidEqualTo(borrowCredit.getBidNid()).andNidEqualTo(borrowCredit.getTenderNid());
			List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
			if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
				// 获取借款数据
				BorrowExample borrowExample = new BorrowExample();
				BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
				borrowCra.andBorrowNidEqualTo(borrowCredit.getBidNid());
				List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
				TenderToCreditAssignCustomize tenderToCreditAssign = new TenderToCreditAssignCustomize();
				// 计算折后价格
				BigDecimal assignPrice = new BigDecimal(assignCapital).setScale(2, BigDecimal.ROUND_DOWN)
						.subtract(new BigDecimal(assignCapital).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal("100"), 18, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN));
				BigDecimal yearRate = borrowCredit.getBidApr().divide(new BigDecimal("100"));
				if (borrowList != null && borrowList.size() > 0) {
					Borrow borrow = borrowList.get(0);
					String borrowStyle = borrow.getBorrowStyle();
					// 剩余待承接本金
					BigDecimal sellerCapitalWait = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
					// 待承接的待收收益
					BigDecimal sellerInterestWait = borrowCredit.getCreditInterest().subtract(borrowCredit.getCreditInterestAssigned());
					// 待垫付的垫付利息
					BigDecimal sellerInterestAdvanceWait = borrowCredit.getCreditInterestAdvance().subtract(borrowCredit.getCreditInterestAdvanceAssigned());
					// 到期还本还息和按天计息，到期还本还息
					if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
						int lastDays = borrowCredit.getCreditTerm();// 剩余天数
						if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
							BigDecimal creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
									borrow.getBorrowPeriod());
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
									sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							BigDecimal assignPay = assignPrice.add(assignInterestAdvance);
							String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
									+ DF_FOR_VIEW.format(assignPay) + "元";
							// 预计收益 承接人债转本息—实付金额
							BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
							tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
							tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
							tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
							tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital()));
							tenderToCreditAssign.setAssignCapital(assignCapital);
							tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()));
							tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice));
							tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay));
							tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest));
							tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance));
							tenderToCreditAssign.setAssignPayText(assignPayText);
						} else {// 按月
							// 债转本息
							BigDecimal creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
									borrow.getBorrowPeriod());
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
									sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							BigDecimal assignPay = assignPrice.add(assignInterestAdvance);
							String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
									+ DF_FOR_VIEW.format(assignPay) + "元";
							// 预计收益 承接人债转本息—实付金额
							BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
							tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
							tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
							tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
							tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignCapital(assignCapital);
							tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPayText(assignPayText);
						}
					}
					// 等额本息和等额本金和先息后本
					if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						int lastDays = 0;
						String bidNid = borrow.getBorrowNid();
						BorrowRepayPlanExample example = new BorrowRepayPlanExample();
						BorrowRepayPlanExample.Criteria cra = example.createCriteria();
						cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrow.getBorrowPeriod());
						List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
						if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
							try {
								lastDays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(borrowCredit.getAddTime()),
										GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0).getRepayTime())));
							} catch (NumberFormatException | ParseException e) {
								e.printStackTrace();
							}
						}
						// 已还多少期
						int repayPeriod = borrowCredit.getRecoverPeriod();
						// 应还总本息
						BigDecimal creditAccount = BigDecimal.ZERO;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						BigDecimal assignPay = BigDecimal.ZERO;
						// 垫付利息
						// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
						BigDecimal assignInterestAdvance = BigDecimal.ZERO;
						if (new BigDecimal(assignCapital).compareTo(sellerCapitalWait) == 0) {
							// 最后一笔承接
							creditAccount = sellerCapitalWait.add(sellerInterestWait);
							// 剩余垫付利息
							assignInterestAdvance = sellerInterestAdvanceWait;
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						} else {
							// 应还总本息
							creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(new BigDecimal(assignCapital), yearRate, borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
							// 承接人每月应还利息
							BigDecimal interestAssign = BeforeInterestAfterPrincipalUtils.getPerTermInterest(new BigDecimal(assignCapital), borrowCredit.getBidApr().divide(new BigDecimal(100)),
									borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
							// 出让人每月应还利息
							BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(borrowCredit.getCreditCapital(), borrowCredit.getBidApr().divide(new BigDecimal(100)),
									borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
							// 应还总额
							creditAccount = creditAccount.subtract(interestAssign.multiply(new BigDecimal(repayPeriod)));
							// 垫付利息
							// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
							assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(new BigDecimal(assignCapital), borrowCredit.getCreditCapital(), yearRate, interest,
									new BigDecimal(lastDays + ""));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						}
						String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
								+ DF_FOR_VIEW.format(assignPay) + "元";
						// 预计收益 承接人债转本息—实付金额
						BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
						tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
						tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
						tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
						tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignCapital(assignCapital);
						tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPayText(assignPayText);
					}
					resultMap.put("tenderToCreditAssign", tenderToCreditAssign);
					resultMap.put("msg", "认购债权确认");
				}
			}
		}
		return resultMap;
	}

	/**
	 * 前端Web页面出借可债转输入出借金额后收益提示 用户未登录 (包含查询条件)
	 *
	 * @return
	 */
	@Override
	public TenderToCreditAssignCustomize getInterestInfo(String creditNid, String assignCapital, Integer userId) {
		// 获取债转数据
		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
		borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid));
		List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
		if (borrowCreditList != null && borrowCreditList.size() == 1) {
			BorrowCredit borrowCredit = borrowCreditList.get(0);
			// 计算折后价格
			BigDecimal assignPrice = new BigDecimal(assignCapital).setScale(2, BigDecimal.ROUND_DOWN)
					.subtract(new BigDecimal(assignCapital).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal("100"), 18, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN));
			// 获取borrow_recover数据
			BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
			BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
			borrowRecoverCra.andBorrowNidEqualTo(borrowCredit.getBidNid()).andNidEqualTo(borrowCredit.getTenderNid());
			List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
			if (borrowRecoverList != null && borrowRecoverList.size() == 1) {
				// 获取借款数据
				BorrowWithBLOBs borrow = this.getBorrowByNid(borrowCredit.getBidNid());
				TenderToCreditAssignCustomize tenderToCreditAssign = new TenderToCreditAssignCustomize();
				BigDecimal yearRate = borrowCredit.getBidApr().divide(new BigDecimal("100"));
				if (Validator.isNotNull(borrow)) {
					String borrowStyle = borrow.getBorrowStyle();
					// 剩余待承接本金
					BigDecimal sellerCapitalWait = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
					// 待承接的待收收益
					BigDecimal sellerInterestWait = borrowCredit.getCreditInterest().subtract(borrowCredit.getCreditInterestAssigned());
					// 待垫付的垫付利息
					BigDecimal sellerInterestAdvanceWait = borrowCredit.getCreditInterestAdvance().subtract(borrowCredit.getCreditInterestAdvanceAssigned());
					// 到期还本还息和按天计息，到期还本还息
					if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
						int lastDays = borrowCredit.getCreditTerm();// 剩余天数
						if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
							BigDecimal creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
									borrow.getBorrowPeriod());
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
									sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							BigDecimal assignPay = assignPrice.add(assignInterestAdvance);
							String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
									+ DF_FOR_VIEW.format(assignPay) + "元";
							// 预计收益 承接人债转本息—实付金额
							BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
							tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
							tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
							tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
							tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital()));
							tenderToCreditAssign.setAssignCapital(assignCapital);
							tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()));
							tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice));
							tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay));
							tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest));
							tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance));
							tenderToCreditAssign.setAssignPayText(assignPayText);
							tenderToCreditAssign.setAssignTotal(assignPay.toString());
						} else {// 按月
							// 债转本息
							BigDecimal creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
									borrow.getBorrowPeriod());
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
									sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							BigDecimal assignPay = assignPrice.add(assignInterestAdvance);
							String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
									+ DF_FOR_VIEW.format(assignPay) + "元";
							// 预计收益 承接人债转本息—实付金额
							BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
							tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
							tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
							tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
							tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignCapital(assignCapital);
							tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPayText(assignPayText);
							tenderToCreditAssign.setAssignTotal(assignPay.toString());
						}
					}
					// 等额本息和等额本金和先息后本
					if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						int lastDays = 0;
						String bidNid = borrow.getBorrowNid();
						BorrowRepayPlanExample example = new BorrowRepayPlanExample();
						BorrowRepayPlanExample.Criteria cra = example.createCriteria();
						cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrowCredit.getRecoverPeriod() + 1);
						List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
						if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
							try {
								lastDays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(borrowCredit.getAddTime()),
										GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0).getRepayTime())));
							} catch (NumberFormatException | ParseException e) {
								e.printStackTrace();
							}
						}
						// 已还多少期
						int repayPeriod = borrowCredit.getRecoverPeriod();
						// 应还总本息
						BigDecimal creditAccount = BigDecimal.ZERO;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						BigDecimal assignPay = BigDecimal.ZERO;
						// 垫付利息
						// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
						BigDecimal assignInterestAdvance = BigDecimal.ZERO;
						if (new BigDecimal(assignCapital).compareTo(sellerCapitalWait) == 0) {
							// 最后一笔承接
							creditAccount = sellerCapitalWait.add(sellerInterestWait);
							// 剩余垫付利息
							assignInterestAdvance = sellerInterestAdvanceWait;
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						} else {
							// 应还总本息
							creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(new BigDecimal(assignCapital), yearRate, borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
							// 承接人每月应还利息
							BigDecimal interestAssign = BeforeInterestAfterPrincipalUtils.getPerTermInterest(new BigDecimal(assignCapital), borrowCredit.getBidApr().divide(new BigDecimal(100)),
									borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
							// 出让人每月应还利息
							BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(borrowCredit.getCreditCapital(), borrowCredit.getBidApr().divide(new BigDecimal(100)),
									borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
							// 应还总额
							creditAccount = creditAccount.subtract(interestAssign.multiply(new BigDecimal(repayPeriod)));
							// 垫付利息
							// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
							assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(new BigDecimal(assignCapital), borrowCredit.getCreditCapital(), yearRate, interest,
									new BigDecimal(lastDays + ""));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						}
						String assignPayText = assignCapital + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance) + "="
								+ DF_FOR_VIEW.format(assignPay) + "元";
						// 预计收益 承接人债转本息—实付金额
						BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
						tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
						tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
						tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
						tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignCapital(assignCapital);
						tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPayText(assignPayText);
						tenderToCreditAssign.setAssignTotal(assignPay.toString());
					}
					// 全投的计算
					if (Validator.isNotNull(userId)) {
						Account account = this.getAccount(userId);
						if (Validator.isNotNull(account)) {
							BigDecimal balance = account.getBankBalance();
							BigDecimal creditDiscount = new BigDecimal(1).subtract(borrowCredit.getCreditDiscount().divide(new BigDecimal(100)));
							BigDecimal sum = sellerCapitalWait.multiply(creditDiscount).add(sellerInterestAdvanceWait);
							BigDecimal max = sellerCapitalWait.multiply(balance).divide(sum, 8, RoundingMode.DOWN);
							if (max.compareTo(sellerCapitalWait) > 0) {
								// 全投金额
								tenderToCreditAssign.setAssignCapitalMax((String.valueOf(sellerCapitalWait.intValue())));
							} else {
								// 全投金额
								tenderToCreditAssign.setAssignCapitalMax(String.valueOf(max.intValue()));
							}
						}
					}
					return tenderToCreditAssign;
				}
			}
		}
		return null;
	}

	/**
	 * 前端Web页面出借确定认购提交
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> saveCreditTenderAssign(Integer userId, String creditNid, String assignCapital, HttpServletRequest request, String platform, String logOrderId, String txDate,
													  String txTime, String seqNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria accountCra = accountExample.createCriteria();
		accountCra.andUserIdEqualTo(userId);
		List<Account> accountList = this.accountMapper.selectByExample(accountExample);
		if (accountList != null && accountList.size() == 1) {
			TenderToCreditAssignCustomize creditAssign = this.getInterestInfo(creditNid, assignCapital, userId);
			String assignPay = creditAssign.getAssignTotal();
			if (accountList.get(0).getBankBalance().compareTo(new BigDecimal(assignPay)) < 0) {
				resultMap.put("msg", "余额不足");
				return resultMap;
			}
		}
		// 获取债转数据
		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
		borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid));
		List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
		if (borrowCreditList != null && borrowCreditList.size() > 0) {
			BorrowCredit borrowCredit = borrowCreditList.get(0);
			if (borrowCredit.getCreditUserId().intValue() != userId.intValue()) {
				resultMap.put("creditUserId", borrowCredit.getCreditUserId());
				// 获取borrow_recover数据
				BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
				BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
				borrowRecoverCra.andBorrowNidEqualTo(borrowCredit.getBidNid()).andNidEqualTo(borrowCredit.getTenderNid());
				List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
				if (borrowRecoverList != null && borrowRecoverList.size() == 1) {
					BorrowRecover borrowRecover = borrowRecoverList.get(0);
					// 如果放款时间小于 20170703 重新计算已承接金额
					if (Integer.parseInt(borrowRecover.getAddtime()) < 1499011200 && borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
						// 计算已承接的债权
						BigDecimal assignedCapital = this.getAssignCapital(borrowRecover.getNid());
						resultMap.put("tenderMoney", borrowRecover.getRecoverCapital().subtract(assignedCapital));
					} else {
						resultMap.put("tenderMoney", borrowRecover.getRecoverCapital());
					}
					// 获取借款数据
					BorrowExample borrowExample = new BorrowExample();
					BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
					borrowCra.andBorrowNidEqualTo(borrowCredit.getBidNid());
					List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
					// 计算折后价格
					BigDecimal assignPrice = new BigDecimal(assignCapital).setScale(2, BigDecimal.ROUND_DOWN)
							.subtract(new BigDecimal(assignCapital).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
					BigDecimal yearRate = borrowCredit.getBidApr().divide(new BigDecimal("100"));
					if (borrowList != null && borrowList.size() > 0) {
						Borrow borrow = borrowList.get(0);
						// 获取标的借款人
						Integer borrowUserId = borrow.getUserId();
						if (borrowUserId.intValue() == userId.intValue()) {
							resultMap.put("msg", "承接人不能为借款人");
							return resultMap;
						}
						String borrowStyle = borrow.getBorrowStyle();
						// 债转本息
						BigDecimal creditAccount = null;
						// 债转期全部利息
						BigDecimal creditInterest = null;
						// 垫付利息 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
						BigDecimal assignInterestAdvance = null;
						// 债转利息
						BigDecimal assignPayInterest = null;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						BigDecimal assignPay = null;
						// 剩余待承接本金
						BigDecimal sellerCapitalWait = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
						// 待承接的待收收益
						BigDecimal sellerInterestWait = borrowCredit.getCreditInterest().subtract(borrowCredit.getCreditInterestAssigned());
						// 待垫付的垫付利息
						BigDecimal sellerInterestAdvanceWait = borrowCredit.getCreditInterestAdvance().subtract(borrowCredit.getCreditInterestAdvanceAssigned());
						// 到期还本还息和按天计息，到期还本还息
						if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
							// 剩余天数
							int lastDays = borrowCredit.getCreditTerm();// 剩余天数
							if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
								creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
										borrow.getBorrowPeriod());
								// 垫付利息
								// 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
								assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestAdvanceWait,
										yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
								// 债转期全部利息
								creditInterest = DuePrincipalAndInterestUtils.getDayInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
										borrow.getBorrowPeriod());
								// 债转利息
								assignPayInterest = creditInterest;
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								assignPay = assignPrice.add(assignInterestAdvance);
							} else {// 按月
								// 债转本息
								creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
										borrow.getBorrowPeriod());
								// 债转期全部利息
								creditInterest = DuePrincipalAndInterestUtils.getMonthInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
										borrow.getBorrowPeriod());
								// 垫付利息
								// 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
								assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
										sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
								// 债转利息
								// assignPayInterest =
								// creditInterest.subtract(assignInterestAdvance);
								assignPayInterest = creditInterest;
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								assignPay = assignPrice.add(assignInterestAdvance);
							}
						}
						// 先息后本
						if (borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
							int lastDays = 0;
							String bidNid = borrow.getBorrowNid();
							BorrowRepayPlanExample example = new BorrowRepayPlanExample();
							BorrowRepayPlanExample.Criteria cra = example.createCriteria();
							cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrowCredit.getRecoverPeriod() + 1);
							List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
							if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
								try {
									String nowDate = GetDate.getDateTimeMyTimeInMillis(borrowCredit.getAddTime());
									String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0).getRepayTime()));
									lastDays = GetDate.daysBetween(nowDate, recoverDate);
								} catch (NumberFormatException | ParseException e) {
									e.printStackTrace();
								}
							}
							// 已还多少期
							int repayPeriod = borrowCredit.getRecoverPeriod();
							if (new BigDecimal(assignCapital).compareTo(sellerCapitalWait) == 0) {
								// 最后一笔承接
								creditAccount = sellerCapitalWait.add(sellerInterestWait);
								// 承接人剩余利息
								assignPayInterest = sellerInterestWait;
								// 剩余垫付利息
								assignInterestAdvance = sellerInterestAdvanceWait;
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								assignPay = assignPrice.add(assignInterestAdvance);
							} else {
								// 承接人每月应还利息
								BigDecimal interestAssign = BeforeInterestAfterPrincipalUtils.getPerTermInterest(new BigDecimal(assignCapital), borrowCredit.getBidApr().divide(new BigDecimal(100)),
										borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
								// 应还总额
								creditAccount = new BigDecimal(assignCapital).add(interestAssign.multiply(new BigDecimal(borrow.getBorrowPeriod() - repayPeriod)));
								// 债转期全部利息
								creditInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(assignCapital), yearRate, borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
								// 承接人剩余利息
								assignPayInterest = interestAssign.multiply(new BigDecimal(borrow.getBorrowPeriod() - repayPeriod));
								// 出让人每月应还利息
								BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(borrowCredit.getCreditCapital(), borrowCredit.getBidApr().divide(new BigDecimal(100)),
										borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
								// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
								assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(new BigDecimal(assignCapital), borrowCredit.getCreditCapital(), yearRate, interest,
										new BigDecimal(lastDays + ""));
								// 实付金额 承接本金*（1-折价率）+应垫付利息
								assignPay = assignPrice.add(assignInterestAdvance);
							}
						}
						// 保存credit_tender_log表
						CreditTenderLog creditTenderLog = new CreditTenderLog();
						creditTenderLog.setUserId(userId);
						creditTenderLog.setCreditUserId(borrowCredit.getCreditUserId());
						creditTenderLog.setStatus(0);
						// 因为标的号必须六位之内 所以用id
						creditTenderLog.setBorrowId(borrow.getId());
						creditTenderLog.setBidNid(borrowCredit.getBidNid());
						creditTenderLog.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
						creditTenderLog.setCreditTenderNid(borrowCredit.getTenderNid());
						creditTenderLog.setAssignNid(logOrderId);
						creditTenderLog.setAssignCapital(new BigDecimal(assignCapital).setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignAccount(creditAccount.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignInterest(assignPayInterest.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignInterestAdvance(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignPrice(assignPrice.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignPay(assignPay.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignRepayAccount(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignRepayCapital(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignRepayInterest(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN));
						creditTenderLog.setAssignRepayEndTime(borrowCredit.getCreditRepayEndTime());
						creditTenderLog.setAssignRepayLastTime(borrowCredit.getCreditRepayLastTime());
						creditTenderLog.setAssignRepayNextTime(borrowCredit.getCreditRepayNextTime());
						creditTenderLog.setAssignRepayYesTime(0);
						creditTenderLog.setAssignRepayPeriod(borrowCredit.getCreditPeriod());// 还剩余多少期
						creditTenderLog.setAssignCreateDate(Integer.parseInt(GetDate.yyyyMMdd.format(new Date())));
						Long ifOldDate = null;
						try {
							ifOldDate = GetDate.datetimeFormat.parse(oldOrNewDate).getTime() / 1000;
						} catch (ParseException e) {
							System.err.println("债转算是否是新旧标区分时间错误，债转标号:" + borrowCredit.getCreditNid());
						}
                        DebtConfigExample debtConfigExample = new DebtConfigExample();
                        List<DebtConfig> debtConfig = debtConfigMapper.selectByExample(debtConfigExample);
						if (ifOldDate != null && ifOldDate <= borrowCredit.getAddTime()) {
                            if(!CollectionUtils.isEmpty(debtConfig)) {
                                creditTenderLog.setCreditFee(assignPay.multiply(debtConfig.get(0).getAttornRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
                            }else {
                                creditTenderLog.setCreditFee(assignPay.multiply(new BigDecimal(0.01)));
                            }
						} else {
							creditTenderLog.setCreditFee(assignPay.multiply(new BigDecimal(0.005)));
						}
						creditTenderLog.setTxDate(Integer.parseInt(txDate));
						creditTenderLog.setTxTime(Integer.parseInt(txTime));
						creditTenderLog.setSeqNo(Integer.parseInt(seqNo));
						creditTenderLog.setAddTime(String.valueOf(GetDate.getNowTime10()));
						creditTenderLog.setClient(Integer.parseInt(platform));
						creditTenderLog.setAddip(request.getRemoteAddr());
						creditTenderLog.setLogOrderId(logOrderId);// 银行请求订单号
						// 在提交债转临时数据之前先验证已经债转的总额是否还允许购买,传入债转标号,债转本金,承接的债转本金
						boolean allowCredit = true;
						if (allowCredit) {
							Integer insert = this.creditTenderLogMapper.insertSelective(creditTenderLog);
							if (insert != null && insert > 0) {
								resultMap.put("creditTenderLog", creditTenderLog);
								resultMap.put("msg", "认购债权临时保存完成");
								resultMap.put("borrow", borrow);
							} else {
								resultMap.put("msg", "认购债权保存失败");
							}
						} else {
							resultMap.put("msg", "认购债权保存失败");
						}
					} else {
						resultMap.put("msg", "当前认购人数太多,提交的认购债权本金已经失效,或者可以稍后再试");
					}
				} else {
					// 未查询用户的放款记录
					resultMap.put("msg", "未查询到用户的放款记录。");
				}
			} else { // 债转人不能购买自己的债转
				resultMap.put("msg", "不可以承接自己出让的债权。");
			}
		}
		return resultMap;
	}

	// 在提交债转临时数据之前先验证已经债转的总额是否还允许购买
	public synchronized boolean allowCreditTender(String creditNid, BigDecimal creditCapital, BigDecimal assignCapital) {
		Map<String, Object> params = new HashMap<String, Object>();
		boolean flag = false;
		String capitalSum = "0.00";
		params.put("creditNid", creditNid);
		// 获取已经承接债转的总额度
		capitalSum = this.tenderCreditCustomizeMapper.creditTenderCapitalSum(params);
		if (capitalSum != null) {
			// 判断债转总额度是否还大于已经承接的债转总额度
			if (creditCapital.compareTo(new BigDecimal(capitalSum)) > 0) {
				// 判断债转总额度是-已经承接的债转总额度(可出借余额)是否大于等于用户承接的债转本金
				if ((creditCapital.subtract(new BigDecimal(capitalSum))).compareTo(assignCapital) >= 0) {
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 前端Web页面出借确定认购提交后状态修改,交易失败
	 *
	 * @return
	 */
	@Override
	public Integer updateCreditTenderLogToFail(CreditTenderLog creditTenderLog) {
		Integer result = 0;
		if (creditTenderLog != null) {
			CreditTenderLogExample creditTenderLogExample = new CreditTenderLogExample();
			CreditTenderLogExample.Criteria creditTenderLogCra = creditTenderLogExample.createCriteria();
			creditTenderLogCra.andUserIdEqualTo(creditTenderLog.getUserId()).andBidNidEqualTo(creditTenderLog.getBidNid()).andCreditNidEqualTo(creditTenderLog.getCreditNid())
					.andAssignNidEqualTo(creditTenderLog.getAssignNid());
			creditTenderLog.setStatus(1);
			result = this.creditTenderLogMapper.updateByExampleSelective(creditTenderLog, creditTenderLogExample);
		}
		return result;
	}

	/**
	 * 前端Web页面出借确定认购汇付回调后状态修改,交易失败
	 *
	 * @return
	 */
	@Override
	public Integer updateCreditTenderLogToFail(BankCallBean bean, Integer userId) {
		Integer result = 0;
		if (bean.getOrderId() != null && !"".equals(bean.getOrderId())) {
			CreditTenderLogExample creditTenderLogExample = new CreditTenderLogExample();
			CreditTenderLogExample.Criteria creditTenderLogCra = creditTenderLogExample.createCriteria();
			if (bean.getOrderId() != null && !"".equals(bean.getOrderId())) {
				creditTenderLogCra.andAssignNidEqualTo(bean.getOrderId());
				if (userId != null) {
					creditTenderLogCra.andUserIdEqualTo(userId);
				}
				List<CreditTenderLog> creditTenderList = this.creditTenderLogMapper.selectByExample(creditTenderLogExample);
				for (CreditTenderLog creditTenderLog : creditTenderList) {
					creditTenderLog.setStatus(1);
					result = this.creditTenderLogMapper.updateByExampleSelective(creditTenderLog, creditTenderLogExample);
				}
			}
		}
		return result;
	}

	/**
	 * 债转汇付交易成功后回调处理 1.插入credit_tender 2.处理承接人account表和account_list表
	 * 3.处理出让人account表和account_list表 4.添加网站收支明细 5.更新borrow_credit
	 * 6.更新Borrow_recover 7.生成还款信息
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean updateTenderCreditInfo(String assignOrderId, Integer userId, String authCode) throws Exception {
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 检测响应状态
		// 获取CreditTenderLog信息
		CreditTenderLogExample tenderLogExample = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria tenderLogCra = tenderLogExample.createCriteria();
		tenderLogCra.andAssignNidEqualTo(assignOrderId).andUserIdEqualTo(userId);
		List<CreditTenderLog> creditTenderLogs = this.creditTenderLogMapper.selectByExample(tenderLogExample);
		if (creditTenderLogs != null && creditTenderLogs.size() == 1) {
			boolean tenderLogFlag = this.creditTenderLogMapper.deleteByExample(tenderLogExample) > 0 ? true : false;
			if (!tenderLogFlag) {
				throw new Exception("删除相应的承接log表失败，承接订单号：" + assignOrderId + ",用户userId:" + userId);
			}
			CreditTenderLog creditTenderLog = creditTenderLogs.get(0);
			// 债权结束标志位
			Integer debtEndFlag = 0;
			// 出让人userId
			int sellerUserId = creditTenderLog.getCreditUserId();
			// 原始出借订单号
			String tenderOrderId = creditTenderLog.getCreditTenderNid();
			// 项目编号
			String borrowNid = creditTenderLog.getBidNid();
			// 债转编号
			String creditNid = creditTenderLog.getCreditNid();
			// 取得债权出让人的用户在汇付天下的客户号
			BankOpenAccount sellerBankAccount = this.getBankOpenAccount(sellerUserId);
			// 出让人账户信息
			Account sellerAccount = this.getAccount(sellerUserId);
			// 取得承接债转的用户在汇付天下的客户号
			BankOpenAccount assignBankAccount = this.getBankOpenAccount(userId);
			// 承接人账户信息
			Account assignAccount = this.getAccount(userId);
			// 项目详情
			BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
			// 还款方式
			String borrowStyle = borrow.getBorrowStyle();
			// 项目总期数
			Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
			// 管理费率
			BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
			// 差异费率
			BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
			// 初审时间
			int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
			// 是否月标(true:月标, false:天标)
			boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
					|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
			// 管理费
			BigDecimal perManageSum = BigDecimal.ZERO;
			// 获取BorrowCredit信息
			BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
			BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
			borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid)).andCreditUserIdEqualTo(sellerUserId).andTenderNidEqualTo(tenderOrderId);
			List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
			// 5.更新borrow_credit
			if (borrowCreditList != null && borrowCreditList.size() == 1) {
				// 获取BorrowRecover信息
				BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
				BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
				borrowRecoverCra.andBorrowNidEqualTo(creditTenderLog.getBidNid()).andNidEqualTo(creditTenderLog.getCreditTenderNid());
				List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);

				BorrowCredit borrowCredit = borrowCreditList.get(0);
				borrowCredit.setCreditIncome(borrowCredit.getCreditIncome().add(creditTenderLog.getAssignPay()));// 总收入,本金+垫付利息
				borrowCredit.setCreditCapitalAssigned(borrowCredit.getCreditCapitalAssigned().add(creditTenderLog.getAssignCapital()));// 已认购本金
				borrowCredit.setCreditInterestAdvanceAssigned(borrowCredit.getCreditInterestAdvanceAssigned().add(creditTenderLog.getAssignInterestAdvance()));// 已垫付利息
				borrowCredit.setCreditInterestAssigned(borrowCredit.getCreditInterestAssigned().add(creditTenderLog.getAssignInterest()));// 已承接利息
				borrowCredit.setCreditFee(borrowCredit.getCreditFee().add(creditTenderLog.getCreditFee()));// 服务费
				borrowCredit.setAssignTime(GetDate.getNowTime10());// 认购时间
				borrowCredit.setAssignNum(borrowCredit.getAssignNum() + 1);// 出借次数
				// 完全承接的情况
				if (borrowCredit.getCreditCapitalAssigned().compareTo(borrowCredit.getCreditCapital()) == 0) {
					// add 合规数据上报 埋点 liubin 20181122 start
					// 推送数据到MQ 承接（完全）散
					JSONObject params = new JSONObject();
					params.put("creditNid", borrowCredit.getCreditNid()+"");
					params.put("flag", "1"); //1（散）2（智投）
					params.put("status", "2"); //2承接（完全）
					this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
					// add 合规数据上报 埋点 liubin 20181122 end

					if (borrowRecoverList != null && borrowRecoverList.size() == 1) {
						BorrowRecover borrowRecover = borrowRecoverList.get(0);
						// 调用银行结束债权接口
						try {
							boolean isDebtEndFlag = this.requestDebtEnd(borrowRecover, sellerBankAccount.getAccount());
							if (isDebtEndFlag) {
								// 债权结束成功
								debtEndFlag = 1;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到相应的borrowRecover数据!" + "，用户userId：" + sellerUserId + "，出借订单号：" + tenderOrderId);
					}
					// 发送承接完成短信
					this.sendCreditFullMessage(borrowCredit);
					borrowCredit.setCreditStatus(2);
				}
				boolean borrowCreditFlag = borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit) > 0 ? true : false;
				if (!borrowCreditFlag) {
					throw new Exception("更新相应的borrowCredit表失败，承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 1.插入credit_tender
				CreditTender creditTender = new CreditTender();
				creditTender.setAssignCreateDate(creditTenderLog.getAssignCreateDate());// 认购日期
				creditTender.setAssignPay(creditTenderLog.getAssignPay());// 支付金额
				creditTender.setCreditFee(creditTenderLog.getCreditFee());// 服务费
				creditTender.setAddTime(String.valueOf(nowTime));// 添加时间
				creditTender.setAssignCapital(creditTenderLog.getAssignCapital());// 出借本金
				creditTender.setUserId(userId);// 用户名称
				creditTender.setCreditUserId(sellerUserId);// 出让人id
				creditTender.setStatus(0);// 状态
				creditTender.setBidNid(creditTenderLog.getBidNid());// 原标标号
				creditTender.setCreditNid(creditTenderLog.getCreditNid());// 债转标号
				creditTender.setCreditTenderNid(creditTenderLog.getCreditTenderNid());// 债转投标单号
				creditTender.setAssignNid(creditTenderLog.getAssignNid());// 认购单号
				creditTender.setAssignAccount(creditTenderLog.getAssignAccount());// 回收总额
				creditTender.setAssignInterest(creditTenderLog.getAssignInterest());// 债转利息
				creditTender.setAssignInterestAdvance(creditTenderLog.getAssignInterestAdvance());// 垫付利息
				creditTender.setAssignPrice(creditTenderLog.getAssignPrice());// 购买价格
				creditTender.setAssignRepayAccount(creditTenderLog.getAssignRepayAccount());// 已还总额
				creditTender.setAssignRepayCapital(creditTenderLog.getAssignRepayCapital());// 已还本金
				creditTender.setAssignRepayInterest(creditTenderLog.getAssignRepayInterest());// 已还利息
				creditTender.setAssignRepayEndTime(creditTenderLog.getAssignRepayEndTime());// 最后还款日
				creditTender.setAssignRepayLastTime(creditTenderLog.getAssignRepayLastTime());// 上次还款时间
				creditTender.setAssignRepayNextTime(creditTenderLog.getAssignRepayNextTime());// 下次还款时间
				creditTender.setAssignRepayYesTime(creditTenderLog.getAssignRepayYesTime());// 最终实际还款时间
				creditTender.setAssignRepayPeriod(creditTenderLog.getAssignRepayPeriod());// 还款期数
				creditTender.setAddip(creditTenderLog.getAddip());// ip
				creditTender.setClient(0);// 客户端
				creditTender.setCreateRepay(0);// 已增加还款信息
				creditTender.setAuthCode(authCode);// 银行存管新增授权码
				creditTender.setRecoverPeriod(borrowCredit.getRecoverPeriod());// 已还款期数
				creditTender.setWeb(0);// 服务费收支

				// add by hesy  添加承接人承接时推荐人信息-- 开始
				UserInfoCustomize userInfoCustomize = userInfoCustomizeMapper.queryUserInfoByUserId(userId);
				SpreadsUsers spreadsUsers = this.getSpreadsUsersByUserId(userId);
				if (spreadsUsers != null) {
					int refUserId = spreadsUsers.getSpreadsUserid();
					// 查找用户推荐人详情信息
					UserInfoCustomize userInfoCustomizeRef = userInfoCustomizeMapper.queryUserInfoByUserId(refUserId);
					if (Validator.isNotNull(userInfoCustomizeRef)) {
						creditTender.setInviteUserName(userInfoCustomizeRef.getUserName());
						creditTender.setInviteUserAttribute(userInfoCustomizeRef.getAttribute());
						creditTender.setInviteUserRegionname(userInfoCustomizeRef.getRegionName());
						creditTender.setInviteUserBranchname(userInfoCustomizeRef.getBranchName());
						creditTender.setInviteUserDepartmentname(userInfoCustomizeRef.getDepartmentName());
					}

				} else if (userInfoCustomize.getAttribute() == 2 || userInfoCustomize.getAttribute() == 3) {
					creditTender.setInviteUserName(userInfoCustomize.getUserName());
					creditTender.setInviteUserAttribute(userInfoCustomize.getAttribute());
					creditTender.setInviteUserRegionname(userInfoCustomize.getRegionName());
					creditTender.setInviteUserBranchname(userInfoCustomize.getBranchName());
					creditTender.setInviteUserDepartmentname(userInfoCustomize.getDepartmentName());
				}

				// add by hesy  添加出让人承接时推荐人信息-- 开始
				UserInfoCustomize userInfoCustomizeSeller = userInfoCustomizeMapper.queryUserInfoByUserId(sellerUserId);
				SpreadsUsers spreadsUsersSeller = this.getSpreadsUsersByUserId(sellerUserId);
				if (spreadsUsersSeller != null) {
					int refUserId = spreadsUsersSeller.getSpreadsUserid();
					// 查找用户推荐人详情信息
					UserInfoCustomize userInfoCustomizeRef = userInfoCustomizeMapper.queryUserInfoByUserId(refUserId);
					if (Validator.isNotNull(userInfoCustomizeRef)) {
						creditTender.setInviteUserCreditName(userInfoCustomizeRef.getUserName());
						creditTender.setInviteUserCreditAttribute(userInfoCustomizeRef.getAttribute());
						creditTender.setInviteUserCreditRegionname(userInfoCustomizeRef.getRegionName());
						creditTender.setInviteUserCreditBranchname(userInfoCustomizeRef.getBranchName());
						creditTender.setInviteUserCreditDepartmentname(userInfoCustomizeRef.getDepartmentName());
					}

				} else if (userInfoCustomizeSeller.getAttribute() == 2 || userInfoCustomizeSeller.getAttribute() == 3) {
					creditTender.setInviteUserCreditName(userInfoCustomizeSeller.getUserName());
					creditTender.setInviteUserCreditAttribute(userInfoCustomizeSeller.getAttribute());
					creditTender.setInviteUserCreditRegionname(userInfoCustomizeSeller.getRegionName());
					creditTender.setInviteUserCreditBranchname(userInfoCustomizeSeller.getBranchName());
					creditTender.setInviteUserCreditDepartmentname(userInfoCustomizeSeller.getDepartmentName());
				}

				// creditTender插入数据库
				boolean tenderLog = this.creditTenderMapper.insertSelective(creditTender) > 0 ? true : false;
				if (!tenderLog) {
					throw new Exception("插入credittender表失败，承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 2.处理承接人account表和account_list表
				// 承接人账户信息
				Account assignAccountNew = new Account();
				assignAccountNew.setUserId(userId);
				assignAccountNew.setBankBalance(creditTender.getAssignPay());
				assignAccountNew.setBankTotal(creditTender.getAssignCapital().add(creditTender.getAssignInterest()).subtract(creditTender.getAssignPay()));
				assignAccountNew.setBankAwait(creditTender.getAssignAccount());// 银行待收+承接金额
				assignAccountNew.setBankAwaitCapital(creditTender.getAssignCapital());// 银行待收本金+承接本金
				assignAccountNew.setBankAwaitInterest(creditTender.getAssignInterest());// 银行待收利息+承接利息
				assignAccountNew.setBankInvestSum(creditTender.getAssignCapital());// 累计出借+承接本金
				// 更新账户信息
				boolean isAccountCrediterFlag = this.adminAccountCustomizeMapper.updateCreditAssignSuccess(assignAccountNew) > 0 ? true : false;
				if (!isAccountCrediterFlag) {
					throw new Exception("承接人承接债转后,更新承接人账户账户信息失败!承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 重新获取出让人用户账户信息
				assignAccount = this.getAccount(userId);
				AccountList assignAccountList = new AccountList();
				assignAccountList.setNid(creditTender.getAssignNid());
				assignAccountList.setUserId(userId);
				assignAccountList.setAmount(creditTender.getAssignPay());
				assignAccountList.setType(2);
				assignAccountList.setTrade("creditassign");
				assignAccountList.setTradeCode("balance");
				assignAccountList.setTotal(assignAccount.getTotal());
				assignAccountList.setBalance(assignAccount.getBalance());
				assignAccountList.setBankBalance(assignAccount.getBankBalance());
				assignAccountList.setBankAwait(assignAccount.getBankAwait());
				assignAccountList.setBankAwaitCapital(assignAccount.getBankAwaitCapital());
				assignAccountList.setBankAwaitInterest(assignAccount.getBankAwaitInterest());
				assignAccountList.setBankInvestSum(assignAccount.getBankInvestSum());
				assignAccountList.setBankInterestSum(assignAccount.getBankInterestSum());
				assignAccountList.setBankFrost(assignAccount.getBankFrost());
				assignAccountList.setBankInterestSum(assignAccount.getBankInterestSum());
				assignAccountList.setBankTotal(assignAccount.getBankTotal());
				assignAccountList.setPlanBalance(assignAccount.getPlanBalance());//汇计划账户可用余额
				assignAccountList.setPlanFrost(assignAccount.getPlanFrost());
				assignAccountList.setSeqNo(String.valueOf(creditTenderLog.getSeqNo()));
				assignAccountList.setTxDate(creditTenderLog.getTxDate());
				assignAccountList.setTxTime(creditTenderLog.getTxTime());
				assignAccountList.setBankSeqNo(String.valueOf(creditTenderLog.getTxDate()) + String.valueOf(creditTenderLog.getTxTime()) + String.valueOf(creditTenderLog.getSeqNo()));
				assignAccountList.setAccountId(assignBankAccount.getAccount());// 承接人电子账户号
				assignAccountList.setFrost(assignAccount.getFrost());
				assignAccountList.setAwait(assignAccount.getAwait());
				assignAccountList.setRepay(assignAccount.getRepay());
				assignAccountList.setRemark("购买债权");
				assignAccountList.setCreateTime(nowTime);
				assignAccountList.setBaseUpdate(nowTime);
				assignAccountList.setOperator(String.valueOf(userId));
				assignAccountList.setIp(creditTender.getAddip());
				assignAccountList.setIsUpdate(0);
				assignAccountList.setBaseUpdate(0);
				assignAccountList.setInterest(null);
				assignAccountList.setWeb(0);
				assignAccountList.setIsBank(1);
				assignAccountList.setCheckStatus(0);
				// 插入交易明细
				boolean assignAccountListFlag = this.accountListMapper.insertSelective(assignAccountList) > 0 ? true : false;
				if (!assignAccountListFlag) {
					throw new Exception("承接债转后,插入承接人交易明细accountList失败!承接订单号：" + assignOrderId + ",用户userId:" + userId);
				}
				// 3.处理出让人account表和account_list表
				Account sellerAccountNew = new Account();
				sellerAccountNew.setUserId(sellerUserId);
				sellerAccountNew.setBankBalance(creditTender.getAssignPay().subtract(creditTender.getCreditFee()));// 银行可用余额
				sellerAccountNew.setBankTotal(creditTender.getAssignPay().subtract(creditTender.getCreditFee()).subtract(creditTender.getAssignAccount()));// 银行总资产
				sellerAccountNew.setBankAwait(creditTender.getAssignAccount());// 出让人待收金额
				sellerAccountNew.setBankAwaitCapital(creditTender.getAssignCapital());// 出让人待收本金
				sellerAccountNew.setBankAwaitInterest(creditTender.getAssignInterest());// 出让人待收利息
				sellerAccountNew.setBankInterestSum(creditTender.getAssignInterestAdvance());// 出让人累计收益
				sellerAccountNew.setBankBalanceCash(creditTender.getAssignPay().subtract(creditTender.getCreditFee()));
				// 更新账户信息
				boolean isAccountFlag = this.adminAccountCustomizeMapper.updateCreditSellerSuccess(sellerAccountNew) > 0 ? true : false;
				if (!isAccountFlag) {
					throw new Exception("出借人承接债转后,更新出让人账户账户信息失败!承接订单号：" + assignOrderId);
				}
				// 重新获取用户账户信息
				sellerAccount = this.getAccount(sellerUserId);
				AccountList sellerAccountList = new AccountList();
				sellerAccountList.setNid(creditTender.getAssignNid());
				sellerAccountList.setUserId(creditTender.getCreditUserId());
				sellerAccountList.setAmount(creditTender.getAssignPay().subtract(creditTender.getCreditFee()));
				sellerAccountList.setType(1);
				sellerAccountList.setTrade("creditsell");
				sellerAccountList.setTradeCode("balance");
				sellerAccountList.setTotal(sellerAccount.getTotal());
				sellerAccountList.setBalance(sellerAccount.getBalance());
				sellerAccountList.setBankBalance(sellerAccount.getBankBalance());
				sellerAccountList.setBankAwait(sellerAccount.getBankAwait());
				sellerAccountList.setBankAwaitCapital(sellerAccount.getBankAwaitCapital());
				sellerAccountList.setBankAwaitInterest(sellerAccount.getBankAwaitInterest());
				sellerAccountList.setBankInterestSum(sellerAccount.getBankInterestSum());
				sellerAccountList.setBankInvestSum(sellerAccount.getBankInvestSum());
				sellerAccountList.setBankFrost(sellerAccount.getBankFrost());
				sellerAccountList.setBankTotal(sellerAccount.getBankTotal());
				sellerAccountList.setPlanBalance(sellerAccount.getPlanBalance());//汇计划账户可用余额
				sellerAccountList.setPlanFrost(sellerAccount.getPlanFrost());
				sellerAccountList.setSeqNo(String.valueOf(creditTenderLog.getSeqNo()));
				sellerAccountList.setTxDate(creditTenderLog.getTxDate());
				sellerAccountList.setTxTime(creditTenderLog.getTxTime());
				sellerAccountList.setBankSeqNo(String.valueOf(creditTenderLog.getTxDate()) + String.valueOf(creditTenderLog.getTxTime()) + String.valueOf(creditTenderLog.getSeqNo()));
				sellerAccountList.setAccountId(sellerBankAccount.getAccount());// 出让人电子账户号
				sellerAccountList.setFrost(sellerAccount.getFrost());
				sellerAccountList.setAwait(sellerAccount.getAwait());
				sellerAccountList.setRepay(sellerAccount.getRepay());
				sellerAccountList.setRemark("出让债权");
				sellerAccountList.setCreateTime(nowTime);
				sellerAccountList.setBaseUpdate(nowTime);
				sellerAccountList.setOperator(String.valueOf(creditTenderLog.getCreditUserId()));
				sellerAccountList.setIp(creditTenderLog.getAddip());
				sellerAccountList.setIsUpdate(0);
				sellerAccountList.setBaseUpdate(0);
				sellerAccountList.setInterest(null);
				sellerAccountList.setWeb(0);
				sellerAccountList.setIsBank(1);
				sellerAccountList.setCheckStatus(0);
				boolean sellerAccountListFlag = this.accountListMapper.insertSelective(sellerAccountList) > 0 ? true : false;// 插入交易明细
				if (!sellerAccountListFlag) {
					throw new Exception("承接债转后,插入出让人交易明细accountList失败!承接订单号：" + assignOrderId);
				}
				// 4.添加网站收支明细
				// 服务费大于0时,插入网站收支明细
				if (creditTender.getCreditFee().compareTo(BigDecimal.ZERO) > 0) {
					// 插入网站收支明细记录
					AccountWebList accountWebList = new AccountWebList();
					accountWebList.setOrdid(assignOrderId);
					accountWebList.setBorrowNid(creditTender.getBidNid());
					accountWebList.setAmount(creditTender.getCreditFee());
					accountWebList.setType(1);
					accountWebList.setTrade("CREDITFEE");
					accountWebList.setTradeType("债转服务费");
					accountWebList.setUserId(creditTender.getUserId());
					accountWebList.setUsrcustid(assignBankAccount.getAccount());
					AccountWebListExample webListExample = new AccountWebListExample();
					webListExample.createCriteria().andOrdidEqualTo(assignOrderId).andTradeEqualTo(CustomConstants.TRADE_LOANFEE);
					int webListCount = this.accountWebListMapper.countByExample(webListExample);
					if (webListCount == 0) {
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
						}
						accountWebList.setRemark(creditTender.getCreditNid());
						accountWebList.setNote(null);
						accountWebList.setCreateTime(nowTime);
						accountWebList.setOperator(null);
						accountWebList.setFlag(1);
						boolean accountWebListFlag = this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
						if (!accountWebListFlag) {
							throw new Exception("网站收支记录(huiyingdai_account_web_list)插入失败!" + "[承接订单号：" + assignOrderId + "]");
						}
					} else {
						throw new Exception("网站收支记录(huiyingdai_account_web_list)已存在!" + "[出借订单号：" + tenderOrderId + "]");
					}
				}

				// 6.更新Borrow_recover
				if (borrowRecoverList != null && borrowRecoverList.size() == 1) {
					BorrowRecover borrowRecover = borrowRecoverList.get(0);
					// 不分期
					if (!isMonth) {
						// 管理费
						BigDecimal perManage = BigDecimal.ZERO;
						// 如果是完全承接
						if (borrowCredit.getCreditStatus() == 2) {
							perManage = borrowRecover.getRecoverFee().subtract(borrowRecover.getCreditManageFee());
						} else {
							// 按月计息，到期还本还息end
							if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
								perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(creditTender.getAssignCapital(), feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
							}
							// 按天计息到期还本还息
							else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
								perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(creditTender.getAssignCapital(), feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
							}
						}
						perManageSum = perManage;
						CreditRepay creditRepay = new CreditRepay();
						creditRepay.setUserId(userId);// 用户名称
						creditRepay.setCreditUserId(creditTender.getCreditUserId());// 出让人id
						creditRepay.setStatus(0);// 状态
						creditRepay.setBidNid(creditTender.getBidNid());// 原标标号
						creditRepay.setCreditNid(creditTender.getCreditNid());// 债转标号
						creditRepay.setCreditTenderNid(creditTender.getCreditTenderNid());// 债转投标单号
						creditRepay.setAssignNid(creditTender.getAssignNid());// 认购单号
						creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
						creditRepay.setAssignAccount(creditTender.getAssignAccount());// 应还总额
						creditRepay.setAssignInterest(creditTender.getAssignInterest());// 应还利息
						creditRepay.setAssignInterestAdvance(creditTender.getAssignInterestAdvance());// 垫付利息
						creditRepay.setAssignPrice(creditTender.getAssignPrice());// 购买价格
						creditRepay.setAssignPay(creditTender.getAssignPay());// 支付金额
						creditRepay.setAssignRepayAccount(BigDecimal.ZERO);// 已还总额
						creditRepay.setAssignRepayCapital(BigDecimal.ZERO);// 已还本金
						creditRepay.setAssignRepayInterest(BigDecimal.ZERO);// 已还利息
						creditRepay.setAssignRepayEndTime(creditTender.getAssignRepayEndTime());// 最后还款日
						creditRepay.setAssignRepayLastTime(creditTender.getAssignRepayLastTime());// 上次还款时间
						creditRepay.setAssignRepayNextTime(creditTender.getAssignRepayNextTime());// 下次还款时间
						creditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
						creditRepay.setAssignRepayPeriod(1);// 还款期数
						creditRepay.setAssignCreateDate(creditTender.getAssignCreateDate());// 认购日期
						creditRepay.setAddTime(String.valueOf(nowTime));// 添加时间
						creditRepay.setAddip(creditTender.getAddip());// ip
						creditRepay.setClient(0);// 客户端
						creditRepay.setRecoverPeriod(1);// 原标还款期数
						creditRepay.setAdvanceStatus(0);
						creditRepay.setChargeDays(0);
						creditRepay.setChargeInterest(BigDecimal.ZERO);
						creditRepay.setDelayDays(0);
						creditRepay.setDelayInterest(BigDecimal.ZERO);
						creditRepay.setLateDays(0);
						creditRepay.setLateInterest(BigDecimal.ZERO);
						creditRepay.setUniqueNid(creditTender.getAssignNid() + "_1");// 唯一nid
						creditRepay.setManageFee(perManage);// 管理费
						creditRepay.setAuthCode(authCode);// 授权码
						creditRepayMapper.insertSelective(creditRepay);
					} else {
						// 管理费
						if (creditTender.getAssignRepayPeriod() > 0) {
							// 先息后本
							if (CalculatesUtil.STYLE_ENDMONTH.equals(borrowStyle)) {
								// 总的利息
								BigDecimal sumMonthInterest = BigDecimal.ZERO;
								// 每月偿还的利息
								BigDecimal perMonthInterest = BigDecimal.ZERO;
								for (int i = 1; i <= creditTender.getAssignRepayPeriod(); i++) {
									BigDecimal perManage = BigDecimal.ZERO;
									int periodNow = borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + i;
									// 获取borrow_recover_plan更新每次还款时间
									BorrowRecoverPlanExample borrowRecoverPlanExample = new BorrowRecoverPlanExample();
									BorrowRecoverPlanExample.Criteria borrowRecoverPlanCra = borrowRecoverPlanExample.createCriteria();
									borrowRecoverPlanCra.andBorrowNidEqualTo(creditTender.getBidNid()).andNidEqualTo(creditTender.getCreditTenderNid()).andRecoverPeriodEqualTo(periodNow);
									List<BorrowRecoverPlan> borrowRecoverPlanList = this.borrowRecoverPlanMapper.selectByExample(borrowRecoverPlanExample);
									if (borrowRecoverPlanList != null && borrowRecoverPlanList.size() > 0) {
										BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlanList.get(0);
										CreditRepay creditRepay = new CreditRepay();
										if (borrowCredit.getCreditStatus() == 2) {
											// 如果是最后一笔
											perManage = borrowRecoverPlan.getRecoverFee().subtract(borrowRecoverPlan.getCreditManageFee());
											perMonthInterest = borrowRecoverPlan.getRecoverInterest().subtract(borrowRecoverPlan.getCreditInterest());
											if (i == creditTender.getAssignRepayPeriod()) {
												creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
												creditRepay.setAssignAccount(creditTender.getAssignCapital().add(perMonthInterest));// 应还总额
												creditRepay.setAssignInterest(perMonthInterest);// 应还利息
											} else {
												creditRepay.setAssignCapital(BigDecimal.ZERO);// 应还本金
												creditRepay.setAssignAccount(perMonthInterest);// 应还总额
												creditRepay.setAssignInterest(perMonthInterest);// 应还利息
											}
										} else {
											// 如果不是最后一笔
											if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
												if (periodNow == borrowPeriod.intValue()) {
													perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate, borrowPeriod,
															borrowPeriod, differentialRate, 1, borrowVerifyTime);
												} else {
													perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate, borrowPeriod,
															borrowPeriod, differentialRate, 0, borrowVerifyTime);
												}
											}
											if (i == creditTender.getAssignRepayPeriod()) {
												BigDecimal lastPeriodInterest = creditTender.getAssignInterest().subtract(sumMonthInterest);
												creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
												creditRepay.setAssignAccount(creditTender.getAssignCapital().add(lastPeriodInterest));// 应还总额
												creditRepay.setAssignInterest(lastPeriodInterest);// 应还利息
											} else {
												// 每月偿还的利息
												perMonthInterest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(creditTender.getAssignCapital(),
														borrow.getBorrowApr().divide(new BigDecimal(100)), borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
												sumMonthInterest = sumMonthInterest.add(perMonthInterest);// 总的还款利息
												creditRepay.setAssignCapital(BigDecimal.ZERO);// 应还本金
												creditRepay.setAssignAccount(perMonthInterest);// 应还总额
												creditRepay.setAssignInterest(perMonthInterest);// 应还利息
											}
										}

										creditRepay.setUserId(userId);// 用户名称
										creditRepay.setCreditUserId(creditTender.getCreditUserId());// 出让人id
										creditRepay.setStatus(0);// 状态
										creditRepay.setBidNid(creditTender.getBidNid());// 原标标号
										creditRepay.setCreditNid(creditTender.getCreditNid());// 债转标号
										creditRepay.setCreditTenderNid(creditTender.getCreditTenderNid());// 债转投标单号
										creditRepay.setAssignNid(creditTender.getAssignNid());// 认购单号

										if (i == 1) {
											creditRepay.setAssignInterestAdvance(creditTender.getAssignInterestAdvance());// 垫付利息
										} else {
											creditRepay.setAssignInterestAdvance(BigDecimal.ZERO);// 垫付利息
										}
										creditRepay.setAssignPrice(creditTender.getAssignPrice());// 购买价格
										creditRepay.setAssignPay(creditTender.getAssignPay());// 支付金额
										creditRepay.setAssignRepayAccount(BigDecimal.ZERO);// 已还总额
										creditRepay.setAssignRepayCapital(BigDecimal.ZERO);// 已还本金
										creditRepay.setAssignRepayInterest(BigDecimal.ZERO);// 已还利息
										creditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
										creditRepay.setAssignRepayPeriod(i);// 还款期数
										creditRepay.setAssignCreateDate(creditTender.getAssignCreateDate());// 认购日期
										creditRepay.setAddTime(String.valueOf(nowTime));// 添加时间
										creditRepay.setAddip(creditTender.getAddip());// ip
										creditRepay.setClient(0);// 客户端
										creditRepay.setManageFee(BigDecimal.ZERO);// 管理费
										creditRepay.setUniqueNid(creditTender.getAssignNid() + "_" + String.valueOf(i));// 唯一nid
										creditRepay.setAuthCode(authCode);// 授权码
										creditRepay.setAdvanceStatus(0);
										creditRepay.setChargeDays(0);
										creditRepay.setChargeInterest(BigDecimal.ZERO);
										creditRepay.setDelayDays(0);
										creditRepay.setDelayInterest(BigDecimal.ZERO);
										creditRepay.setLateDays(0);
										creditRepay.setLateInterest(BigDecimal.ZERO);
										creditRepay.setAssignRepayEndTime(creditTender.getAssignRepayEndTime());// 最后还款日
										creditRepay.setAssignRepayLastTime(creditTender.getAssignRepayLastTime());// 上次还款时间
										creditRepay.setAssignRepayNextTime(Integer.parseInt(borrowRecoverPlan.getRecoverTime()));// 下次还款时间
										creditRepay.setRecoverPeriod(borrowRecoverPlan.getRecoverPeriod());// 原标还款期数
										creditRepay.setManageFee(perManage);// 管理费
										creditRepayMapper.insertSelective(creditRepay);
										// 更新borrowRecover
										// 承接本金
										borrowRecoverPlan.setCreditAmount(borrowRecoverPlan.getCreditAmount().add(creditRepay.getAssignCapital()));
										// 垫付利息
										borrowRecoverPlan.setCreditInterestAmount(borrowRecoverPlan.getCreditInterestAmount().add(creditRepay.getAssignInterestAdvance()));
										// 债转状态
										borrowRecoverPlan.setCreditStatus(borrowCredit.getCreditStatus());
										borrowRecoverPlan.setCreditManageFee(borrowRecoverPlan.getCreditManageFee().add(perManage));
										borrowRecoverPlan.setCreditInterest(borrowRecoverPlan.getCreditInterest().add(perMonthInterest));//
										this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan);
									}
									perManageSum = perManageSum.add(perManage);
								}
							}
						}
					}
					borrowRecover.setCreditAmount(borrowRecover.getCreditAmount().add(creditTender.getAssignCapital()));
					borrowRecover.setCreditInterestAmount(borrowRecover.getCreditInterestAmount().add(creditTender.getAssignInterestAdvance()));
					borrowRecover.setCreditStatus(borrowCredit.getCreditStatus());
					borrowRecover.setCreditManageFee(borrowRecover.getCreditManageFee().add(perManageSum));// 已收债转管理费
					borrowRecover.setDebtStatus(debtEndFlag);// 债权是否结束状态
					boolean borrowRecoverFlag = borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
					if (!borrowRecoverFlag) {
						throw new Exception("更新相应的放款信息表borrowrecover失败!" + "[出借订单号：" + tenderOrderId + "]");
					}
					// 更新渠道统计用户累计出借
					// 出借人信息
					Users users = getUsers(userId);
					if (Validator.isNull(users)) {
						throw new Exception("查询相应的承接用户user失败!" + "[用户userId：" + userId + "]");
					}
					// 更新渠道统计用户累计出借
					AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
					AppChannelStatisticsDetailExample.Criteria crt = channelExample.createCriteria();
					crt.andUserIdEqualTo(userId);
					List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(channelExample);
					if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
						AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("id", channelDetail.getId());
						// 认购本金
						params.put("accountDecimal", creditTenderLog.getAssignCapital());
						// 出借时间
						params.put("investTime", nowTime);
						// 项目类型
						params.put("projectType", "汇转让");
						// 首次投标项目期限
						String investProjectPeriod = borrowCredit.getCreditTerm() + "天";
						params.put("investProjectPeriod", investProjectPeriod);
						// 更新渠道统计用户累计出借
						if (users.getInvestflag() == 1) {
							// 更新相应的累计出借金额
							this.appChannelStatisticsDetailCustomizeMapper.updateAppChannelStatisticsDetail(params);
						} else if (users.getInvestflag() == 0) {
							// 更新首投出借
							this.appChannelStatisticsDetailCustomizeMapper.updateFirstAppChannelStatisticsDetail(params);
						}
						_log.info("用户:" + userId + "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号：" + creditTenderLog.getAssignNid());
					} else {
						// 更新huiyingdai_utm_reg的首投信息
						UtmRegExample utmRegExample = new UtmRegExample();
						UtmRegExample.Criteria utmRegCra = utmRegExample.createCriteria();
						utmRegCra.andUserIdEqualTo(userId);
						List<UtmReg> utmRegList = this.utmRegMapper.selectByExample(utmRegExample);
						if (utmRegList != null && utmRegList.size() > 0) {
							UtmReg utmReg = utmRegList.get(0);
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("id", utmReg.getId());
							params.put("accountDecimal", creditTenderLog.getAssignCapital());
							// 出借时间
							params.put("investTime", nowTime);
							// 项目类型
							params.put("projectType", "汇转让");
							// 首次投标项目期限
							String investProjectPeriod = borrowCredit.getCreditTerm() + "天";
							// 首次投标项目期限
							params.put("investProjectPeriod", investProjectPeriod);
							// 更新渠道统计用户累计出借
							if (users.getInvestflag() == 0) {
								// 更新huiyingdai_utm_reg的首投信息
								this.appChannelStatisticsDetailCustomizeMapper.updateFirstUtmReg(params);
							}
						}
					}
					// 更新新手标志位
					users.setInvestflag(1);
					boolean userFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
					if (!userFlag) {
						throw new Exception("更新相应的用户新手标志位失败!" + "[用户userId：" + userId + "]");
					}
					// 网站累计出借追加
					List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
					if (calculates != null && calculates.size() > 0) {
						CalculateInvestInterest calculateNew = new CalculateInvestInterest();
						calculateNew.setTenderSum(creditTenderLog.getAssignCapital());
						calculateNew.setId(calculates.get(0).getId());
						calculateNew.setCreateTime(GetDate.getDate(nowTime));
						this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
					}
					// 修改mongodb运营数据
					JSONObject params = new JSONObject();
					params.put("type", 1);// 出借
					params.put("money", creditTender.getAssignCapital());
					rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_OPERATION_DATA, JSONObject.toJSONString(params));
					this.sendCreditSuccessMessage(creditTender);

					// 神策数据统计 add by liuyang 20180726 start
					try {
						SensorsDataBean sensorsDataBean = new SensorsDataBean();
						sensorsDataBean.setUserId(userId);
						sensorsDataBean.setEventCode("receive_credit_assign");
						sensorsDataBean.setOrderId(assignOrderId);
						// 发送神策数据统计MQ
						_log.info("承接成功后发送神策数据统计MQ,承接订单号:[" + assignOrderId + "].");
						this.sendSensorsDataMQ(sensorsDataBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 神策数据统计 add by liuyang 20180726 end

					// add 合规数据上报 埋点 liubin 20181122 start
					params = new JSONObject();
					params.put("assignOrderId", creditTender.getAssignNid());
					params.put("flag", "1");//1（散）2（智投）
					params.put("status", "1"); //1承接（每笔）
					// 推送数据到MQ 承接（每笔）散
					mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_SINGLE_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
					// add 合规数据上报 埋点 liubin 20181122 end

					return true;
				} else {
					throw new Exception("未查询到相应的borrowRecover数据!" + "，用户userId：" + sellerUserId + "，出借订单号：" + tenderOrderId);
				}
			} else {
				throw new Exception("未查询到相应的borrowCredit数据!" + "，用户userId：" + sellerUserId + "，出借订单号：" + tenderOrderId);
			}
		} else {
			throw new Exception("查询相应的承接log表失败，承接订单号：" + assignOrderId);
		}
	}

	/**
	 * 发送法大大PDF生成MQ处理
	 *
	 * @param tenderUserId
	 * @param borrowNid
	 * @param assignOrderId
	 * @param creditNid
	 * @param creditTenderNid
	 */
	public void sendPdfMQ(Integer tenderUserId, String borrowNid, String assignOrderId, String creditNid, String creditTenderNid) {
		FddGenerateContractBean bean = new FddGenerateContractBean();
		bean.setOrdid(assignOrderId);
		bean.setAssignOrderId(assignOrderId);
		bean.setCreditNid(creditNid);
		bean.setCreditTenderNid(creditTenderNid);
		bean.setTenderUserId(tenderUserId);
		bean.setBorrowNid(borrowNid);
		bean.setTransType(3);
		bean.setTenderType(1);
		this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
	}

	/**
	 * 调用银行结束债权接口
	 *
	 * @param borrowRecover
	 * @return
	 */
	private boolean requestDebtEnd(BorrowRecover borrowRecover, String tenderAccountId) {
		// 出借人用户Id
		Integer tenderUserId = borrowRecover.getUserId();
		// 借款人用户Id
		Integer borrowUserId = borrowRecover.getBorrowUserid();
		BankOpenAccount borrowUserAccount = this.getBankOpenAccount(borrowUserId);
		String orderId = GetOrderIdUtils.getOrderId2(tenderUserId);
//		BankCallBean bean = new BankCallBean();
//		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
//		bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_END);
//		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
//		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
//		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
//		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
//		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
//		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
//		bean.setAccountId(borrowUserAccount.getAccount());// 融资人电子账号
//		bean.setOrderId(orderId);// 订单号
//		bean.setForAccountId(tenderAccountId);// 对手电子账号
//		bean.setProductId(borrowRecover.getBorrowNid());// 标的号
//		bean.setAuthCode(borrowRecover.getAuthCode());// 授权码
//		bean.setLogUserId(String.valueOf(tenderUserId));// 出借人用户Id
//		bean.setLogOrderId(orderId);
//		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
//		BankCallBean resultBean = BankCallUtils.callApiBg(bean);
//		resultBean.convert();
//		if (resultBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
//			return true;
//		} else {
//			return false;
//		}

		_log.info(borrowRecover.getBorrowNid() + " 承接结束债权  借款人: " + borrowUserId + "-" + borrowUserAccount.getAccount() + " 出借人: " + tenderUserId + "-" + tenderAccountId + " 授权码: " + borrowRecover.getAuthCode() + " 原始订单号: " + borrowRecover.getNid());
		BankCreditEnd record = new BankCreditEnd();
		record.setUserId(borrowUserId);
//		record.setUsername(borrowRecover);
		record.setTenderUserId(tenderUserId);
//		record.setTenderUsername(tenderUsername);
		record.setAccountId(borrowUserAccount.getAccount());
		record.setTenderAccountId(tenderAccountId);
		record.setOrderId(orderId);
		record.setBorrowNid(borrowRecover.getBorrowNid());
		record.setAuthCode(borrowRecover.getAuthCode());
		record.setCreditEndType(2); // 结束债权类型（1:还款，2:散标债转，3:计划债转）'
		record.setStatus(0);
		record.setOrgOrderId(borrowRecover.getNid());

		int nowTime = GetDate.getNowTime10();
		record.setCreateUser(tenderUserId);
		record.setCreateTime(nowTime);
		record.setUpdateUser(tenderUserId);
		record.setUpdateTime(nowTime);

		this.bankCreditEndMapper.insertSelective(record);
		return true;



	}


	/**
	 * 调用银行结束债权接口
	 *
	 * @param borrowNid
	 * @param borrowUserId
	 * @param borrowUserAccount
	 * @param tenderUserId
	 * @param tenderAccountId
	 * @param tenderAuthCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean requestDebtEnd(HjhDebtCredit credit, String tenderAccountId, String tenderAuthCode) throws Exception {



		String borrowNid = credit.getBorrowNid();
		Integer tenderUserId = credit.getUserId();

		Borrow borrow = this.selectBorrowByBorrowNid(borrowNid);
		if (borrow == null) {
			throw new Exception("结束债券接口：标的" + borrowNid + "不存在");
		}
		BankOpenAccount borrowBankOpenAccount = this.getBankOpenAccount(borrow.getUserId());
		if (borrowBankOpenAccount == null) {
			throw new Exception("结束债券接口：借款人" + borrow.getUserId() + "银行未开户");
		}

		String orderId = GetOrderIdUtils.getOrderId2(tenderUserId);
//		BankCallBean bean = new BankCallBean();
//		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
//		bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_END);
//		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
//		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
//		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
//		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
//		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
//		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
//		bean.setAccountId(borrowBankOpenAccount.getAccount());// 融资人电子账号
//		bean.setOrderId(orderId);// 订单号
//		bean.setForAccountId(tenderAccountId);// 对手电子账号
//		bean.setProductId(borrowNid);// 标的号
//		bean.setAuthCode(tenderAuthCode);// 授权码
//		bean.setLogUserId(String.valueOf(tenderUserId));// 出借人用户Id
//		bean.setLogOrderId(orderId);
//		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
//		BankCallBean resultBean = BankCallUtils.callApiBg(bean);
//		resultBean.convert();
//		if (resultBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
//			return true;
//		} else {
//			_log.error("银行结束债券返回值错误。返回值：" + resultBean == null?"null":resultBean.getRetCode());
//			return false;
//		}
		_log.info(borrowNid + " 直投还款结束债权  借款人: " + borrow.getUserId() + "-" + borrowBankOpenAccount.getAccount() + " 出借人: " + tenderUserId + "-" + tenderAccountId + " 授权码: " + tenderAuthCode + " 原始订单号: " + credit.getSellOrderId());
		BankCreditEnd record = new BankCreditEnd();
		record.setUserId(borrow.getUserId());
//		record.setUsername(borrowRecover);
		record.setTenderUserId(tenderUserId);
//		record.setTenderUsername(tenderUsername);
		record.setAccountId(borrowBankOpenAccount.getAccount());
		record.setTenderAccountId(tenderAccountId);
		record.setOrderId(orderId);
		record.setBorrowNid(borrowNid);
		record.setAuthCode(tenderAuthCode);
		record.setCreditEndType(2); // 结束债权类型（1:还款，2:散标债转，3:计划债转）
		record.setStatus(0);
		record.setOrgOrderId(credit.getSellOrderId());

		int nowTime = GetDate.getNowTime10();
		record.setCreateUser(tenderUserId);
		record.setCreateTime(nowTime);
		record.setUpdateUser(tenderUserId);
		record.setUpdateTime(nowTime);

		this.bankCreditEndMapper.insertSelective(record);
		return true;

	}


	/**
	 * 向承接人推送承接成功消息
	 *
	 * @param creditTender
	 */
	private void sendCreditSuccessMessage(CreditTender creditTender) {
		Users webUser = this.getUsers(creditTender.getUserId());
		UsersInfo usersInfo2 = this.getUsersInfoByUserId(creditTender.getUserId());
		// 发送承接成功消息
		if (webUser != null) {
			Map<String, String> param = new HashMap<String, String>();
			if (usersInfo2.getTruename() != null && usersInfo2.getTruename().length() > 1) {
				param.put("val_name", usersInfo2.getTruename().substring(0, 1));
			} else {
				param.put("val_name", usersInfo2.getTruename());
			}
			if (usersInfo2.getSex() == 1) {
				param.put("val_sex", "先生");
			} else if (usersInfo2.getSex() == 2) {
				param.put("val_sex", "女士");
			} else {
				param.put("val_sex", "");
			}
			param.put("val_title", creditTender.getCreditNid() + "");
			param.put("val_balance", creditTender.getAssignPay() + "");
			param.put("val_profit", creditTender.getAssignInterest() + "");
			param.put("val_amount", creditTender.getAssignAccount() + "");
			AppMsMessage appMsMessage = new AppMsMessage(null, param, webUser.getMobile(), MessageDefine.APPMSSENDFORMOBILE, CustomConstants.JYTZ_TPL_CJZQ);
			appMsProcesser.gather(appMsMessage);
		}
	}

	/**
	 * 向出让人推送债转完全承接消息
	 *
	 * @param borrowCredit
	 */
	private void sendCreditFullMessage(BorrowCredit borrowCredit) {
		// 满标
		Users webUser = this.getUsers(borrowCredit.getCreditUserId());
		UsersInfo usersInfo = this.getUsersInfoByUserId(borrowCredit.getCreditUserId());
		if (webUser != null) {
			Map<String, String> param = new HashMap<String, String>();
			if (usersInfo.getTruename() != null && usersInfo.getTruename().length() > 1) {
				param.put("val_name", usersInfo.getTruename().substring(0, 1));
			} else {
				param.put("val_name", usersInfo.getTruename());
			}
			if (usersInfo.getSex() == 1) {
				param.put("val_sex", "先生");
			} else if (usersInfo.getSex() == 2) {
				param.put("val_sex", "女士");
			} else {
				param.put("val_sex", "");
			}
			param.put("val_amount", borrowCredit.getCreditCapital() + "");
			param.put("val_profit", borrowCredit.getCreditInterestAdvanceAssigned() + "");
			// 发送短信验证码
			SmsMessage smsMessage = new SmsMessage(null, param, webUser.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZZQBZRCG,
					CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
			AppMsMessage appMsMessage = new AppMsMessage(null, param, webUser.getMobile(), MessageDefine.APPMSSENDFORMOBILE, CustomConstants.JYTZ_TPL_ZHUANRANGJIESHU);
			appMsProcesser.gather(appMsMessage);
		}
	}

	/**
	 * 获取SMS配置信息
	 *
	 * @return
	 * @author b
	 */

	@Override
	public SmsConfig getSmsConfig() {
		SmsConfigExample example = new SmsConfigExample();
		List<SmsConfig> list = smsConfigMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 发送SMS信息
	 *
	 * @param mobile
	 * @param reason
	 * @throws Exception
	 * @author b
	 */

	@Override
	public void sendSms(String mobile, String reason) throws Exception {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_phonenu", mobile);
		replaceStrs.put("val_reason", reason);
		// 发送短信验证码
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_DUANXINCHAOXIAN, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);
	}

	/**
	 * 发送邮件信息
	 *
	 * @param mobile
	 * @param reason
	 * @throws Exception
	 * @author b
	 */

	@Override
	public void sendEmail(String mobile, String reason) throws Exception {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_phonenu", mobile);
		replaceStrs.put("val_reason", reason);
		MailMessage message = new MailMessage(null, replaceStrs, null, null, null, null, CustomConstants.EMAILPARAM_TPL_DUANXINCHAOXIAN, MessageDefine.MAILSENDFORMAILINGTOSELF);
		mailProcesser.gather(message);

	}

	/**
	 * 保存短信验证码
	 */
	@Override
	public int saveSmsCode(String mobile, String checkCode) {
		SmsCode smsCode = new SmsCode();
		smsCode.setCheckfor(MD5.toMD5Code(mobile + checkCode));
		smsCode.setMobile(mobile);
		smsCode.setCheckcode(checkCode);
		smsCode.setPosttime(GetDate.getMyTimeInMillis());
		smsCode.setStatus(0);
		smsCode.setUserId(0);
		return smsCodeMapper.insertSelective(smsCode);
	}

	/**
	 * 检查短信验证码
	 */
	@Override
	public int checkMobileCode(String phone, String code) {
		int time = GetDate.getNowTime10();
		int timeAfter = time - 180;
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
		cra.andPosttimeLessThanOrEqualTo(time);
		cra.andMobileEqualTo(phone);
		cra.andCheckcodeEqualTo(code);
		return smsCodeMapper.countByExample(example);
	}

	/**
	 * 获取提交的债转数据
	 *
	 * @return
	 */
	@Override
	public List<BorrowCredit> selectBorrowCreditByNid(String creditNid) {
		// 获取borrow_credit数据
		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
		borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid));
		// 获取还款数据
		List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
		return borrowCreditList;
	}

	/********************************************************************** 华丽的分割线 ***************************************************************/
	/**
	 *
	 * 债转各项金额计算
	 */
	public Map<String, BigDecimal> selectExpectCreditFeeForBigDecimal(String borrowNid, String tenderNid, String creditDiscount, int nowTime) {
		Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>();
		// 获取借款信息
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		// 获取borrow_recover数据
		BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
		borrowRecoverCra.andBorrowNidEqualTo(borrowNid).andNidEqualTo(tenderNid);
		List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
		// 债转本息
		BigDecimal creditAccount = BigDecimal.ZERO;
		// 债转期全部利息
		BigDecimal creditInterest = BigDecimal.ZERO;
		// 垫付利息 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
		BigDecimal assignInterestAdvance = BigDecimal.ZERO;
		// 债转利息
		BigDecimal assignPayInterest = BigDecimal.ZERO;
		// 实付金额 承接本金*（1-折价率）+应垫付利息
		BigDecimal assignPay = BigDecimal.ZERO;
		// 预计收益 承接人债转本息—实付金额
		BigDecimal assignInterest = BigDecimal.ZERO;
		// 可转本金
		BigDecimal creditCapital = BigDecimal.ZERO;
		// 折后价格
		BigDecimal creditPrice = BigDecimal.ZERO;
		// 已发生债转的未还利息
		BigDecimal creditRepayInterestWait = BigDecimal.ZERO;

		if (borrowList != null && borrowList.size() > 0) {
			Borrow borrow = borrowList.get(0);
			String borrowStyle = borrow.getBorrowStyle();
			if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
				BorrowRecover borrowRecover = borrowRecoverList.get(0);
				// 可转本金
				creditCapital = borrowRecover.getRecoverCapital().subtract(borrowRecover.getRecoverCapitalYes()).subtract(borrowRecover.getCreditAmount());
				// 折后价格
				creditPrice = creditCapital.multiply(new BigDecimal(1).subtract(new BigDecimal(creditDiscount).divide(new BigDecimal(100)))).setScale(2, BigDecimal.ROUND_DOWN);
				// 年利率
				BigDecimal yearRate = borrow.getBorrowApr().divide(new BigDecimal("100"));
				// 到期还本还息和按天计息，到期还本还息
				if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
					int lastDays = 0;
					try {
						String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
						String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRecover.getRecoverTime()));
						lastDays = GetDate.daysBetween(nowDate, recoverDate);
					} catch (NumberFormatException | ParseException e) {
						e.printStackTrace();
					}
					// 剩余天数
					if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
						// 债转本息
						creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 债转期全部利息
						creditInterest = DuePrincipalAndInterestUtils.getDayInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 垫付利息 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
						assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvance(creditCapital, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays + ""));
						// 债转利息
						assignPayInterest = creditInterest;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						assignPay = creditPrice.add(assignInterestAdvance);
						// 预计收益 承接人债转本息—实付金额
						assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
					} else {// 按月
						// 债转本息
						creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 债转期全部利息
						creditInterest = DuePrincipalAndInterestUtils.getMonthInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 垫付利息 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
						assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvance(creditCapital, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays + ""));
						// 债转利息
						assignPayInterest = creditInterest;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						assignPay = creditPrice.add(assignInterestAdvance);
						// 预计收益 承接人债转本息—实付金额
						assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
					}
				}
				// 等额本息和等额本金和先息后本
				if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
					// 根据出借订单号检索已债转还款信息
					List<CreditRepay> creditRepayList = this.selectCreditRepayList(tenderNid);
					int lastDays = 0;
					String bidNid = borrow.getBorrowNid();
					BorrowRepayPlanExample example = new BorrowRepayPlanExample();
					BorrowRepayPlanExample.Criteria cra = example.createCriteria();
					cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + 1);
					List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
					if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
						try {
							String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
							String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0).getRepayTime()));
							lastDays = GetDate.daysBetween(nowDate, recoverDate);
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
					}
					// 债转本息
					creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(creditCapital, yearRate, borrowRecover.getRecoverPeriod(), borrowRecover.getRecoverPeriod());
					// 每月应还利息
					BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(creditCapital, borrow.getBorrowApr().divide(new BigDecimal(100)), borrowRecover.getRecoverPeriod(),
							borrowRecover.getRecoverPeriod());
					// 债转期全部利息
					// creditInterest =
					// BeforeInterestAfterPrincipalUtils.getInterestCount(creditCapital,
					// yearRate, borrowRecover.getRecoverPeriod(),
					// borrowRecover.getRecoverPeriod());
					if (creditRepayList != null && creditRepayList.size() > 0) {
						for (CreditRepay creditRepay : creditRepayList) {
							creditRepayInterestWait = creditRepayInterestWait.add(creditRepay.getAssignInterest());
						}
					}
					creditInterest = borrowRecover.getRecoverInterestWait().subtract(creditRepayInterestWait);
					// 垫付利息 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
					assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(creditCapital, creditCapital, yearRate, interest, new BigDecimal(lastDays + ""));
					// 债转利息
					assignPayInterest = creditInterest;
					// 实付金额 承接本金*（1-折价率）+应垫付利息
					assignPay = creditPrice.add(assignInterestAdvance);
					// 预计收益 承接人债转本息—实付金额
					assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
				}
			}
		}
		resultMap.put("creditAccount", creditAccount.setScale(2, BigDecimal.ROUND_DOWN));// 债转本息
		resultMap.put("creditInterest", creditInterest.setScale(2, BigDecimal.ROUND_DOWN));// 预计收益
		resultMap.put("assignInterestAdvance", assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN));// 垫付利息
		resultMap.put("assignPayInterest", assignPayInterest.setScale(2, BigDecimal.ROUND_DOWN));// 债转利息
		resultMap.put("assignPay", assignPay.setScale(2, BigDecimal.ROUND_DOWN));// 实付金额
		resultMap.put("assignInterest", assignInterest.setScale(2, BigDecimal.ROUND_DOWN));// 债转期全部利息
		resultMap.put("creditCapital", creditCapital.setScale(2, BigDecimal.ROUND_DOWN));// 可转本金
		resultMap.put("creditPrice", creditPrice.setScale(2, BigDecimal.ROUND_DOWN));// 折后价格
		return resultMap;
	}

	/**
	 * 根据出借订单号,检索已发送债转的还款信息
	 *
	 * @param tenderNid
	 * @return
	 */
	private List<CreditRepay> selectCreditRepayList(String tenderNid) {
		CreditRepayExample example = new CreditRepayExample();
		CreditRepayExample.Criteria cra = example.createCriteria();
		cra.andCreditTenderNidEqualTo(tenderNid);
		cra.andStatusEqualTo(0);
		return this.creditRepayMapper.selectByExample(example);
	}

	/**
	 * 债转汇付掉单数据手动恢复
	 *
	 * @return
	 */
	@Override
	public JSONObject updateTenderCreditInfoHandle(String assignNid) {
		JSONObject ret = new JSONObject();
		// 手动控制事务
		TransactionStatus txStatus = null;
		// 检验回调是否已经存入CreditTender表中
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andAssignNidEqualTo(assignNid);
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			ret.put("message", "该债转数据已经出借,无需修复");
			return ret;
		}
		// 首先更新CreditTenderLog状态是10
		// 获取CreditTenderLog信息
		CreditTenderLogExample creditTenderLogSuccessExample = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria creditTenderLogSuccessCra = creditTenderLogSuccessExample.createCriteria();
		creditTenderLogSuccessCra.andAssignNidEqualTo(assignNid);
		List<CreditTenderLog> creditTenderLogSuccessList = this.creditTenderLogMapper.selectByExample(creditTenderLogSuccessExample);
		for (CreditTenderLog creditTenderLogSuccess : creditTenderLogSuccessList) {
			creditTenderLogSuccess.setStatus(10);
			this.creditTenderLogMapper.updateByPrimaryKey(creditTenderLogSuccess);
		}
		try {
			// 开启事务
			txStatus = this.transactionManager.getTransaction(transactionDefinition);
			// 获取CreditTenderLog信息
			CreditTenderLogExample creditTenderLogExample = new CreditTenderLogExample();
			CreditTenderLogExample.Criteria creditTenderLogCra = creditTenderLogExample.createCriteria();
			creditTenderLogCra.andAssignNidEqualTo(assignNid);
			List<CreditTenderLog> creditTenderLogList = this.creditTenderLogMapper.selectByExample(creditTenderLogExample);
			CreditTender creditTender = null;
			if (creditTenderLogList != null && creditTenderLogList.size() > 0) {
				for (CreditTenderLog creditTenderLog : creditTenderLogList) {
					// 承接人账户信息
					AccountExample accountCrediterExample = new AccountExample();
					AccountExample.Criteria accountCrediterCra = accountCrediterExample.createCriteria();
					accountCrediterCra.andUserIdEqualTo(creditTenderLog.getUserId());
					List<Account> accountCrediterList = this.accountMapper.selectByExample(accountCrediterExample);
					// 出让人账户信息
					AccountExample accountTenderExample = new AccountExample();
					AccountExample.Criteria accountTenderCra = accountTenderExample.createCriteria();
					accountTenderCra.andUserIdEqualTo(creditTenderLog.getCreditUserId());
					List<Account> accountTenderList = this.accountMapper.selectByExample(accountTenderExample);
					// 承接人用户详细信息
					UsersInfoExample usersInfoExample = new UsersInfoExample();
					UsersInfoExample.Criteria userInfoCra = usersInfoExample.createCriteria();
					userInfoCra.andUserIdEqualTo(creditTenderLog.getUserId());
					List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(usersInfoExample);
					// 获取BorrowCredit信息
					BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
					BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
					borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditTenderLog.getCreditNid())).andCreditUserIdEqualTo(creditTenderLog.getCreditUserId())
							.andTenderNidEqualTo(creditTenderLog.getCreditTenderNid());
					List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(borrowCreditExample);
					// 获取BorrowRecover信息
					BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
					BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
					borrowRecoverCra.andBorrowNidEqualTo(creditTenderLog.getBidNid()).andNidEqualTo(creditTenderLog.getCreditTenderNid());
					List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
					// 获取借款信息
					BorrowExample borrowExample = new BorrowExample();
					BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
					borrowCra.andBorrowNidEqualTo(creditTenderLog.getBidNid());
					List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);

					// 根据汇付返回的额数据进行对比,
					// 1.插入credit_tender,
					// 2.处理承接人account表和account_list表,
					// 3.处理出让人account表和account_list表
					// 4.添加网站收支明细,
					// 5.更新borrow_credit,
					// 6.更新Borrow_recover,
					// 7.生成还款信息,

					// 1.插入credit_tender
					creditTender = new CreditTender();
					creditTender.setAssignCreateDate(creditTenderLog.getAssignCreateDate());// 认购日期
					creditTender.setAssignPay(creditTenderLog.getAssignPay());// 支付金额
					creditTender.setCreditFee(creditTenderLog.getCreditFee());// 服务费
					creditTender.setAddTime(creditTenderLog.getAddTime());// 添加时间
					creditTender.setAssignCapital(creditTenderLog.getAssignCapital());// 出借本金
					creditTender.setUserId(creditTenderLog.getUserId());// 用户名称
					creditTender.setCreditUserId(creditTenderLog.getCreditUserId());// 出让人id
					creditTender.setStatus(0);// 状态
					creditTender.setBidNid(creditTenderLog.getBidNid());// 原标标号
					creditTender.setCreditNid(creditTenderLog.getCreditNid());// 债转标号
					creditTender.setCreditTenderNid(creditTenderLog.getCreditTenderNid());// 债转投标单号
					creditTender.setAssignNid(creditTenderLog.getAssignNid());// 认购单号
					creditTender.setAssignAccount(creditTenderLog.getAssignAccount());// 回收总额
					creditTender.setAssignInterest(creditTenderLog.getAssignInterest());// 债转利息
					creditTender.setAssignInterestAdvance(creditTenderLog.getAssignInterestAdvance());// 垫付利息
					creditTender.setAssignPrice(creditTenderLog.getAssignPrice());// 购买价格
					creditTender.setAssignRepayAccount(creditTenderLog.getAssignRepayAccount());// 已还总额
					creditTender.setAssignRepayCapital(creditTenderLog.getAssignRepayCapital());// 已还本金
					creditTender.setAssignRepayInterest(creditTenderLog.getAssignRepayInterest());// 已还利息
					creditTender.setAssignRepayEndTime(creditTenderLog.getAssignRepayEndTime());// 最后还款日
					creditTender.setAssignRepayLastTime(creditTenderLog.getAssignRepayLastTime());// 上次还款时间
					creditTender.setAssignRepayNextTime(creditTenderLog.getAssignRepayNextTime());// 下次还款时间
					creditTender.setAssignRepayYesTime(creditTenderLog.getAssignRepayYesTime());// 最终实际还款时间
					creditTender.setAssignRepayPeriod(creditTenderLog.getAssignRepayPeriod());// 还款期数
					creditTender.setAddip(creditTenderLog.getAddip());// ip
					creditTender.setClient(0);// 客户端
					creditTender.setCreateRepay(0);// 已增加还款信息
					if (borrowCreditList != null && borrowCreditList.size() > 0) {
						creditTender.setRecoverPeriod(borrowCreditList.get(0).getRecoverPeriod());// 已还款期数
					} else {
						creditTender.setRecoverPeriod(0);// 已还款期数
					}
					creditTender.setWeb(0);// 服务费收支
					// creditTender插入数据库
					this.creditTenderMapper.insertSelective(creditTender);
					// 2.处理承接人account表和account_list表
					Account accountCrediter = null;
					AccountList accountCrediterListEntiry = null;
					if (accountCrediterList != null && accountCrediterList.size() > 0) {
						// 承接人账户信息
						accountCrediter = accountCrediterList.get(0);
						// 承接人可用余额
						BigDecimal balance = accountCrediter.getBalance();
						BigDecimal await = accountCrediter.getAwait();
						accountCrediter.setBalance(balance.subtract(creditTender.getAssignPay()));
						accountCrediter.setAwait(await.add(creditTender.getAssignAccount()));
						accountCrediterListEntiry = new AccountList();
						accountCrediterListEntiry.setNid(creditTender.getAssignNid());
						accountCrediterListEntiry.setUserId(creditTender.getUserId());
						accountCrediterListEntiry.setAmount(creditTender.getAssignPay());
						accountCrediterListEntiry.setType(2);
						accountCrediterListEntiry.setTrade("creditassign");
						accountCrediterListEntiry.setTradeCode("balance");
						accountCrediterListEntiry.setTotal(accountCrediter.getTotal());
						accountCrediterListEntiry.setBalance(accountCrediter.getBalance());
						accountCrediterListEntiry.setFrost(accountCrediter.getFrost());
						accountCrediterListEntiry.setAwait(accountCrediter.getAwait());
						accountCrediterListEntiry.setRepay(accountCrediter.getRepay());
						accountCrediterListEntiry.setRemark("购买债权");
						accountCrediterListEntiry.setCreateTime(GetDate.getNowTime10());
						accountCrediterListEntiry.setBaseUpdate(GetDate.getNowTime10());
						accountCrediterListEntiry.setOperator(String.valueOf(creditTenderLog.getUserId()));
						accountCrediterListEntiry.setIp(creditTender.getAddip());
						accountCrediterListEntiry.setIsUpdate(0);
						accountCrediterListEntiry.setBaseUpdate(0);
						accountCrediterListEntiry.setInterest(null);
						accountCrediterListEntiry.setWeb(0);
					}
					this.accountListMapper.insertSelective(accountCrediterListEntiry);// 插入交易明细
					this.accountMapper.updateByPrimaryKey(accountCrediter);// 更新账户信息

					// 3.处理出让人account表和account_list表
					Account accountTender = null;
					AccountList accountListTenderEntiry = null;
					if (accountTenderList != null && accountTenderList.size() > 0) {
						accountTender = accountTenderList.get(0);
						BigDecimal balance = accountTender.getBalance();
						BigDecimal await = accountTender.getAwait();
						accountTender.setBalance(balance.add(creditTender.getAssignPay()).subtract(creditTender.getCreditFee()));
						accountTender.setAwait(await.subtract(creditTender.getAssignAccount()));
						accountListTenderEntiry = new AccountList();
						accountListTenderEntiry.setNid(creditTender.getAssignNid());
						accountListTenderEntiry.setUserId(creditTender.getCreditUserId());
						accountListTenderEntiry.setAmount(creditTender.getAssignPay().subtract(creditTender.getCreditFee()));
						accountListTenderEntiry.setType(1);
						accountListTenderEntiry.setTrade("creditsell");
						accountListTenderEntiry.setTradeCode("balance");
						accountListTenderEntiry.setTotal(accountTender.getTotal());
						accountListTenderEntiry.setBalance(accountTender.getBalance());
						accountListTenderEntiry.setFrost(accountTender.getFrost());
						accountListTenderEntiry.setAwait(accountTender.getAwait());
						accountListTenderEntiry.setRepay(accountTender.getRepay());
						accountListTenderEntiry.setRemark("出让债权");
						accountListTenderEntiry.setCreateTime(GetDate.getNowTime10());
						accountListTenderEntiry.setBaseUpdate(GetDate.getNowTime10());
						accountListTenderEntiry.setOperator(String.valueOf(creditTenderLog.getCreditUserId()));
						accountListTenderEntiry.setIp(creditTenderLog.getAddip());
						accountListTenderEntiry.setIsUpdate(0);
						accountListTenderEntiry.setBaseUpdate(0);
						accountListTenderEntiry.setInterest(null);
						accountListTenderEntiry.setWeb(0);
					}
					this.accountListMapper.insertSelective(accountListTenderEntiry);// 插入交易明细
					this.accountMapper.updateByPrimaryKey(accountTender);// 更新账户信息

					// 4.添加网站收支明细
					AccountWebList accountWebList = new AccountWebList();
					accountWebList.setOrdid(creditTender.getAssignNid());
					accountWebList.setBorrowNid(creditTender.getBidNid());
					accountWebList.setAmount(creditTender.getCreditFee());
					accountWebList.setType(1);
					accountWebList.setTrade("CREDITFEE");
					accountWebList.setTradeType("债转服务费");
					accountWebList.setUserId(creditTender.getUserId());
					accountWebList.setUsrcustid(String.valueOf(creditTender.getCreditUserId()));
					accountWebList.setTruename(usersInfoList != null ? usersInfoList.get(0).getTruename() : "");
					accountWebList.setRegionName(null);
					accountWebList.setBranchName(null);
					accountWebList.setDepartmentName(null);
					accountWebList.setRemark(creditTender.getCreditNid());
					accountWebList.setNote(null);
					accountWebList.setCreateTime(GetDate.getNowTime10());
					accountWebList.setOperator(null);
					accountWebList.setFlag(1);
					this.accountWebListMapper.insertSelective(accountWebList);// 插入网站交易明细

					// 5.更新borrow_credit
					BorrowCredit borrowCredit = null;
					if (borrowCreditList != null && borrowCreditList.size() > 0) {
						borrowCredit = borrowCreditList.get(0);
						borrowCredit.setCreditIncome(borrowCredit.getCreditIncome().add(creditTender.getAssignPay()));// 总收入,本金+垫付利息
						borrowCredit.setCreditCapitalAssigned(borrowCredit.getCreditCapitalAssigned().add(creditTender.getAssignCapital()));// 已认购本金
						borrowCredit.setCreditInterestAssigned(borrowCredit.getCreditInterestAssigned().add(creditTender.getAssignInterestAdvance()));// 已垫付利息
						borrowCredit.setCreditFee(borrowCredit.getCreditFee().add(creditTender.getCreditFee()));// 服务费
						borrowCredit.setAssignTime(GetDate.getNowTime10());// 认购时间
						borrowCredit.setAssignNum(borrowCredit.getAssignNum() + 1);// 出借次数
						if (borrowCredit.getCreditCapitalAssigned().compareTo(borrowCredit.getCreditCapital()) == 0) {
							borrowCredit.setCreditStatus(1);
						}
					}
					borrowCreditMapper.updateByPrimaryKey(borrowCredit);

					// 6.更新Borrow_recover
					BorrowRecover borrowRecover = null;
					if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
						borrowRecover = borrowRecoverList.get(0);
						borrowRecover.setCreditAmount(borrowRecover.getCreditAmount().add(creditTender.getAssignCapital()));
						borrowRecover.setCreditInterestAmount(borrowRecover.getCreditInterestAmount().add(creditTender.getAssignInterestAdvance()));
					}
					borrowRecoverMapper.updateByPrimaryKey(borrowRecover);

					/** 7.生成还款信息 **/
					if (borrowList != null && borrowList.size() > 0) {
						for (Borrow borrow : borrowList) {
							// 到期还本还息和按天计息，到期还本还息
							if (borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_END) || borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_ENDDAY)) {
								CreditRepay creditRepay = new CreditRepay();
								creditRepay.setUserId(creditTender.getUserId());// 用户名称
								creditRepay.setCreditUserId(creditTender.getCreditUserId());// 出让人id
								creditRepay.setStatus(0);// 状态
								creditRepay.setBidNid(creditTender.getBidNid());// 原标标号
								creditRepay.setCreditNid(creditTender.getCreditNid());// 债转标号
								creditRepay.setCreditTenderNid(creditTender.getCreditTenderNid());// 债转投标单号
								creditRepay.setAssignNid(creditTender.getAssignNid());// 认购单号
								creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
								creditRepay.setAssignAccount(creditTender.getAssignAccount());// 应还总额
								creditRepay.setAssignInterest(creditTender.getAssignInterest());// 应还利息
								creditRepay.setAssignInterestAdvance(creditTender.getAssignInterestAdvance());// 垫付利息
								creditRepay.setAssignPrice(creditTender.getAssignPrice());// 购买价格
								creditRepay.setAssignPay(creditTender.getAssignPay());// 支付金额
								creditRepay.setAssignRepayAccount(BigDecimal.ZERO);// 已还总额
								creditRepay.setAssignRepayCapital(BigDecimal.ZERO);// 已还本金
								creditRepay.setAssignRepayInterest(BigDecimal.ZERO);// 已还利息
								creditRepay.setAssignRepayEndTime(creditTender.getAssignRepayEndTime());// 最后还款日
								creditRepay.setAssignRepayLastTime(creditTender.getAssignRepayLastTime());// 上次还款时间
								creditRepay.setAssignRepayNextTime(creditTender.getAssignRepayNextTime());// 下次还款时间
								creditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
								creditRepay.setAssignRepayPeriod(1);// 还款期数
								creditRepay.setAssignCreateDate(creditTender.getAssignCreateDate());// 认购日期
								creditRepay.setAddTime(String.valueOf(GetDate.getNowTime10()));// 添加时间
								creditRepay.setAddip(creditTender.getAddip());// ip
								creditRepay.setClient(0);// 客户端
								creditRepay.setRecoverPeriod(1);// 原标还款期数
								creditRepay.setManageFee(BigDecimal.ZERO);// 管理费
								creditRepay.setUniqueNid(creditTender.getAssignNid() + "_1");// 唯一nid

								creditRepayMapper.insertSelective(creditRepay);
							}
							// 等额本息和等额本金和先息后本
							if (borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_MONTH) || borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_PRINCIPAL)
									|| borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_ENDMONTH)) {
								if (creditTender.getAssignRepayPeriod() > 0) {
									// 先息后本
									if (borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_ENDMONTH)) {
										// 每月偿还的利息
										BigDecimal perMonthInterest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(creditTender.getAssignCapital(),
												borrow.getBorrowApr().divide(new BigDecimal(100)), borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
										for (int i = 1; i <= creditTender.getAssignRepayPeriod(); i++) {
											// 获取borrow_recover_plan更新每次还款时间
											BorrowRecoverPlanExample borrowRecoverPlanExample = new BorrowRecoverPlanExample();
											BorrowRecoverPlanExample.Criteria borrowRecoverPlanCra = borrowRecoverPlanExample.createCriteria();
											borrowRecoverPlanCra.andBorrowNidEqualTo(creditTender.getBidNid()).andNidEqualTo(creditTender.getCreditTenderNid())
													.andRecoverPeriodEqualTo(borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + i);
											List<BorrowRecoverPlan> borrowRecoverPlanList = this.borrowRecoverPlanMapper.selectByExample(borrowRecoverPlanExample);

											CreditRepay creditRepay = new CreditRepay();
											creditRepay.setUserId(creditTender.getUserId());// 用户名称
											creditRepay.setCreditUserId(creditTender.getCreditUserId());// 出让人id
											creditRepay.setStatus(0);// 状态
											creditRepay.setBidNid(creditTender.getBidNid());// 原标标号
											creditRepay.setCreditNid(creditTender.getCreditNid());// 债转标号
											creditRepay.setCreditTenderNid(creditTender.getCreditTenderNid());// 债转投标单号
											creditRepay.setAssignNid(creditTender.getAssignNid());// 认购单号
											creditRepay.setAssignInterest(perMonthInterest);// 应还利息
											if (i == creditTender.getAssignRepayPeriod()) {
												creditRepay.setAssignCapital(creditTender.getAssignCapital());// 应还本金
												creditRepay.setAssignAccount(creditTender.getAssignCapital().add(perMonthInterest));// 应还总额

											} else {
												creditRepay.setAssignCapital(BigDecimal.ZERO);// 应还本金
												creditRepay.setAssignAccount(perMonthInterest);// 应还总额
											}
											if (i == 1) {
												creditRepay.setAssignInterestAdvance(creditTender.getAssignInterestAdvance());// 垫付利息
											} else {
												creditRepay.setAssignInterestAdvance(BigDecimal.ZERO);// 垫付利息
											}
											creditRepay.setAssignPrice(creditTender.getAssignPrice());// 购买价格
											creditRepay.setAssignPay(creditTender.getAssignPay());// 支付金额
											creditRepay.setAssignRepayAccount(BigDecimal.ZERO);// 已还总额
											creditRepay.setAssignRepayCapital(BigDecimal.ZERO);// 已还本金
											creditRepay.setAssignRepayInterest(BigDecimal.ZERO);// 已还利息
											creditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
											creditRepay.setAssignRepayPeriod(i);// 还款期数
											creditRepay.setAssignCreateDate(creditTender.getAssignCreateDate());// 认购日期
											creditRepay.setAddTime(String.valueOf(GetDate.getNowTime10()));// 添加时间
											creditRepay.setAddip(creditTender.getAddip());// ip
											creditRepay.setClient(0);// 客户端
											creditRepay.setManageFee(BigDecimal.ZERO);// 管理费
											creditRepay.setUniqueNid(creditTender.getAssignNid() + "_" + String.valueOf(i));// 唯一nid
											if (borrowRecoverPlanList != null && borrowRecoverPlanList.size() > 0) {
												BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlanList.get(0);
												creditRepay.setAssignRepayEndTime(creditTender.getAssignRepayEndTime());// 最后还款日
												creditRepay.setAssignRepayLastTime(creditTender.getAssignRepayLastTime());// 上次还款时间
												creditRepay.setAssignRepayNextTime(Integer.parseInt(borrowRecoverPlan.getRecoverTime()));// 下次还款时间
												creditRepay.setRecoverPeriod(borrowRecoverPlan.getRecoverPeriod());// 原标还款期数
											}
											creditRepayMapper.insertSelective(creditRepay);
										}
									}
								}
							}
						}
					}
				}
				// 提交事务
				this.transactionManager.commit(txStatus);
				ret.put("message", "该数据修复完成！");
				ret.put("data", creditTender);
				return ret;
			} else {
				// 回滚事务
				this.transactionManager.rollback(txStatus);
				ret.put("message", "该数据修复完成！");
				return ret;
			}
		} catch (Exception e) {
			// 回滚事务
			this.transactionManager.rollback(txStatus);
			// 针对异步回调会产生异常判断是否已经添加数据成功
			ret.put("message", "该数据修复时发生异常！");
			ret.put("exception", e.toString());
			return ret;
		}
	}

	/**
	 *
	 * 获取借款信息
	 * 

	 * @param borrowNid
	 * @return
	 * @author liuyang
	 * @see com.hyjf.CreditService.tender.credit.TenderCreditService#searchBorrowList(java.lang.String)
	 */
	@Override
	public List<Borrow> searchBorrowList(String borrowNid) {
		// 获取借款信息
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		return borrowList;
	}

	/**
	 *
	 * 根据标号检索标的信息
	 * 

	 * @param borrowNid
	 * @return
	 * @author liuyang
	 * @see com.hyjf.CreditService.tender.credit.TenderCreditService#seachBorrowInfo(java.lang.String)
	 */
	@Override
	public Borrow seachBorrowInfo(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> list = borrowMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 *
	 * 分期标的还款计划
	 * 

	 * @param borrowNid
	 * @param borrowPeriod
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#searchBorrowRepayPlanList(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<BorrowRepayPlan> searchBorrowRepayPlanList(String borrowNid, Integer borrowPeriod) {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid).andRepayPeriodEqualTo(borrowPeriod);
		return this.borrowRepayPlanMapper.selectByExample(example);
	}

	public Map<String, Object> selectPlanWebCreditTenderList(String creditNid, Integer currPage, Integer limitPage) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BigDecimal tendTotal = BigDecimal.ZERO;
		// 获取债转出借信息
		DebtCreditTenderExample creditTenderExample = new DebtCreditTenderExample();
		DebtCreditTenderExample.Criteria creditTender = creditTenderExample.createCriteria();
		creditTender.andCreditNidEqualTo(creditNid);
		creditTender.andDelFlagEqualTo(1);
		// 获取出借的总数
		int count = this.debtCreditTenderMapper.countByExample(creditTenderExample);
		// 声明分页对象
		WebPaginator paginator = new WebPaginator(currPage, count, limitPage, "tenderPage", null);
		// 分页查询
		creditTenderExample.setLimitStart(paginator.getOffset());
		creditTenderExample.setLimitEnd(paginator.getLimit());
		List<DebtCreditTender> creditTenderList = this.debtCreditTenderMapper.selectByExample(creditTenderExample);
		List<DebtCreditTender> creditTenderList2 = new ArrayList<DebtCreditTender>();
		if (creditTenderList != null && creditTenderList.size() > 0) {
			// 遍历全部
			for (DebtCreditTender creditTender_ : creditTenderList) {
				tendTotal = tendTotal.add(creditTender_.getAssignCapital());
				// 获取用户信息
				UsersExample usersExample = new UsersExample();
				UsersExample.Criteria usersCra = usersExample.createCriteria();
				usersCra.andUserIdEqualTo(creditTender_.getUserId());
				List<Users> userList = this.usersMapper.selectByExample(usersExample);
				creditTender_.setAddip(userList != null && userList.size() > 0 ? userList.get(0).getUsername().substring(0, 2) + "***" : "*");
				creditTender_.setCreateUserName(GetDate.timestamptoStrYYYYMMDDHHMMSS(creditTender_.getCreateTime()));
				creditTenderList2.add(creditTender_);
			}
			resultMap.put("tendTotal", tendTotal);
			resultMap.put("tenders", creditTenderList2);
			resultMap.put("tenderNum", count);
			resultMap.put("tenderPage", paginator);
		} else {
			resultMap.put("tendTotal", 0.00);
			resultMap.put("tenders", null);
			resultMap.put("tenderNum", 0);
			resultMap.put("tenderPage", null);
		}
		return resultMap;
	}

	/**
	 * 根据订单号,用户ID查询债转出借记录
	 *
	 * @param logOrderId
	 * @param userId
	 * @return
	 */
	@Override
	public CreditTender creditTenderByAssignNid(String logOrderId, Integer userId) {
		// 获取债转出借信息
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andAssignNidEqualTo(logOrderId).andUserIdEqualTo(userId);
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			CreditTender creditTender = creditTenderList.get(0);
			return creditTender;
		} else {
			return null;
		}

	}

	/**
	 * 查询企业项目的项目详情
	 */
	@Override
	public WebProjectCompanyDetailCustomize searchProjectCompanyDetail(String borrowNid) {
		WebProjectCompanyDetailCustomize htlDetail = webProjectListCustomizeMapper.selectProjectCompanyDetail(borrowNid);
		return htlDetail;
	}

	/**
	 * 计算还款计划
	 *
	 * @param borrowNid
	 * @return
	 */
	@Override
	public List<RepayPlanBean> getRepayPlan(String borrowNid) {
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		Borrow borrow = this.getBorrowByNid(borrowNid);
		String borrowStyle = borrow.getBorrowStyle();
		Integer projectType = borrow.getProjectType();
		BigDecimal yearRate = borrow.getBorrowApr();
		Integer totalMonth = borrow.getBorrowPeriod();
		BigDecimal account = borrow.getAccount();
		Integer time = borrow.getBorrowSuccessTime();
		if (time == null) {
			time = (int) (System.currentTimeMillis() / 1000);
		}
		List<RepayPlanBean> repayPlans = new ArrayList<RepayPlanBean>();
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManagerScaleEnd());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 按月计息到期还本还息和按天计息，到期还本还息
		if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate,
					borrowVerifyTime);
			if (info != null) {
				String repayTime = "-";
				// 通过复审
				if (borrow.getReverifyStatus() == 3) {
					repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
				}
				RepayPlanBean planIntrest = new RepayPlanBean(repayTime, df.format(info.getRepayAccountInterest()), "利息");
				RepayPlanBean plan = new RepayPlanBean(repayTime, df.format(info.getRepayAccountCapital()), "本金");
				repayPlans.add(planIntrest);
				repayPlans.add(plan);
			}
		} else {// 等额本息和等额本金和先息后本
			InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate,
					borrowVerifyTime);
			if (info.getListMonthly() != null) {
				String repayTime = "-";
				for (int i = 0; i < info.getListMonthly().size(); i++) {
					InterestInfo sub = info.getListMonthly().get(i);
					// 通过复审
					if (borrow.getReverifyStatus() == 3) {
						repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
					}
					String repayType = "本息";
					if (i < info.getListMonthly().size() - 1 && borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						repayType = "利息";
					}
					RepayPlanBean plan = new RepayPlanBean(repayTime, df.format(sub.getRepayAccount()), repayType);
					repayPlans.add(plan);
				}
			}
		}
		return repayPlans;
	}

	@Override
	public int countTenderCreditInvestRecordTotal(Map<String, Object> params) {
		return appTenderCreditCustomizeMapper.countTenderCreditInvestRecordTotal(params);
	}

	/**
	 *
	 * 获取债转出借列表
	 * 

	 * @param params
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#searchTenderCreditInvestList(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditInvestListCustomize> searchTenderCreditInvestList(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.searchTenderCreditInvestList(params);
	}

	/**
	 *
	 * 获取可债转列表的数据
	 * 

	 * @param params
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#selectTenderToCreditList(java.util.Map)
	 */
	@Override
	public List<AppTenderToCreditListCustomize> selectTenderToCreditListApp(Map<String, Object> params) {
		return appTenderCreditCustomizeMapper.selectTenderToCreditList(params);
	}

	/**
	 *
	 * 根据原标标号检索标的信息
	 * 

	 * @param bidNid
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#selectBorrowByBorrowNid(java.lang.String)
	 */
	@Override
	public Borrow selectBorrowByBorrowNid(String bidNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(bidNid);
		List<Borrow> list = borrowMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 *
	 * 查询出借可债转详细
	 * 

	 * @param userId
	 * @param nowTime
	 * @param borrowNid
	 * @param tenderNid
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#selectTenderToCreditDetail(int,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<AppTenderCreditCustomize> selectTenderToCreditDetailApp(int userId, int nowTime, String borrowNid, String tenderNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("nowTime", nowTime);
		params.put("borrowNid", borrowNid);
		params.put("tenderNid", tenderNid);
		List<AppTenderCreditCustomize> tenderToCreditDetail = appTenderCreditCustomizeMapper.selectTenderToCreditDetail(params);
		return tenderToCreditDetail;
	}

	/**
	 *
	 * 获取债转垫付利息
	 * 

	 * @param borrowNid
	 * @param tenderNid
	 * @param creditDiscount
	 * @param nowTime
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#selectassignInterestForBigDecimal(java.lang.String,
	 * java.lang.String, java.lang.String, int)
	 */
	public Map<String, BigDecimal> selectassignInterestForBigDecimal(String borrowNid, String tenderNid, String creditDiscount, int nowTime) {
		Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>();
		// 获取借款信息
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
		// 获取borrow_recover数据
		BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
		borrowRecoverCra.andBorrowNidEqualTo(borrowNid).andNidEqualTo(tenderNid);
		List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
		// 债转本息
		BigDecimal creditAccount = BigDecimal.ZERO;
		// 债转期全部利息
		BigDecimal creditInterest = BigDecimal.ZERO;
		// 垫付利息 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
		BigDecimal assignInterestAdvance = BigDecimal.ZERO;
		// 债转利息
		BigDecimal assignPayInterest = BigDecimal.ZERO;
		// 实付金额 承接本金*（1-折价率）+应垫付利息
		BigDecimal assignPay = BigDecimal.ZERO;
		// 预计收益 承接人债转本息—实付金额
		BigDecimal assignInterest = BigDecimal.ZERO;
		// 可转本金
		BigDecimal creditCapital = BigDecimal.ZERO;
		// 折后价格
		BigDecimal creditPrice = BigDecimal.ZERO;

		if (borrowList != null && borrowList.size() > 0) {
			Borrow borrow = borrowList.get(0);
			String borrowStyle = borrow.getBorrowStyle();
			if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
				BorrowRecover borrowRecover = borrowRecoverList.get(0);
				// 可转本金
				creditCapital = borrowRecover.getRecoverCapital().subtract(borrowRecover.getRecoverCapitalYes()).subtract(borrowRecover.getCreditAmount());
				// 折后价格
				creditPrice = creditCapital.multiply(new BigDecimal(1).subtract(new BigDecimal(creditDiscount).divide(new BigDecimal(100)))).setScale(2, BigDecimal.ROUND_DOWN);
				// 年利率
				BigDecimal yearRate = borrow.getBorrowApr().divide(new BigDecimal("100"));
				// 到期还本还息和按天计息，到期还本还息
				if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
					int lastDays = 0;
					try {
						lastDays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecover.getRecoverTime())));
					} catch (NumberFormatException | ParseException e) {
						e.printStackTrace();
					}
					// 剩余天数
					if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
						// 债转本息
						creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 债转期全部利息
						creditInterest = DuePrincipalAndInterestUtils.getDayInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 垫付利息 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
						assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvance(creditCapital, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays + ""));
						// 债转利息
						assignPayInterest = creditInterest;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						assignPay = creditPrice.add(assignInterestAdvance);
						// 预计收益 承接人债转本息—实付金额
						assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
					} else {// 按月
						// 债转本息
						creditAccount = DuePrincipalAndInterestUtils.getMonthPrincipalInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 债转期全部利息
						creditInterest = DuePrincipalAndInterestUtils.getMonthInterest(creditCapital, yearRate, borrow.getBorrowPeriod());
						// 垫付利息 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
						assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvance(creditCapital, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays + ""));
						// 债转利息
						assignPayInterest = creditInterest;
						// 实付金额 承接本金*（1-折价率）+应垫付利息
						assignPay = creditPrice.add(assignInterestAdvance);
						// 预计收益 承接人债转本息—实付金额
						assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
					}
				}
				// 等额本息和等额本金和先息后本
				if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
					String bidNid = borrow.getBorrowNid();

					BorrowRepayPlanExample example1 = new BorrowRepayPlanExample();
					BorrowRepayPlanExample.Criteria cra1 = example1.createCriteria();
					cra1.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + 1);
					List<BorrowRepayPlan> borrowRepayPlanList = this.borrowRepayPlanMapper.selectByExample(example1);

					int lastDays = 0;
					if (borrowRepayPlanList != null && borrowRepayPlanList.size() > 0) {
						try {
							String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
							String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRepayPlanList.get(0).getRepayTime()));
							lastDays = GetDate.daysBetween(nowDate, recoverDate);
						} catch (NumberFormatException | ParseException e) {
							e.printStackTrace();
						}
					}
					// 债转本息
					creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(creditCapital, yearRate, borrow.getBorrowPeriod(), borrowRecover.getRecoverPeriod());
					// 每月应还利息
					BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(creditCapital, borrow.getBorrowApr().divide(new BigDecimal(100)), borrow.getBorrowPeriod(),
							borrow.getBorrowPeriod());
					// 债转期全部利息
					creditInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(creditCapital, yearRate, borrow.getBorrowPeriod(), borrowRecover.getRecoverPeriod());
					// 垫付利息 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
					assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(creditCapital, creditCapital, yearRate, interest, new BigDecimal(lastDays + ""));
					// 债转利息
					assignPayInterest = creditInterest;
					// 实付金额 承接本金*（1-折价率）+应垫付利息
					assignPay = creditPrice.add(assignInterestAdvance);
					// 预计收益 承接人债转本息—实付金额
					assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
				}
			}
		}
		resultMap.put("creditAccount", creditAccount.setScale(2, BigDecimal.ROUND_DOWN));// 债转本息
		resultMap.put("creditInterest", creditInterest.setScale(2, BigDecimal.ROUND_DOWN));// 预计收益
		resultMap.put("assignInterestAdvance", assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN));// 垫付利息
		resultMap.put("assignPayInterest", assignPayInterest.setScale(2, BigDecimal.ROUND_DOWN));// 债转利息
		resultMap.put("assignPay", assignPay.setScale(2, BigDecimal.ROUND_DOWN));// 实付金额
		resultMap.put("assignInterest", assignInterest.setScale(2, BigDecimal.ROUND_DOWN));// 债转期全部利息
		resultMap.put("creditCapital", creditCapital.setScale(2, BigDecimal.ROUND_DOWN));// 可转本金
		resultMap.put("creditPrice", creditPrice.setScale(2, BigDecimal.ROUND_DOWN));// 折后价格
		return resultMap;
	}

	/**
	 *
	 * 根据用户id,borrowNid,tenderNid判断用户是否已经发起债转
	 * 

	 * @param borrowNid
	 * @param tenderNid
	 * @param userId
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#selectBorrowCreditByBorrowNid(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public BorrowCredit selectBorrowCreditByBorrowNid(String borrowNid, String tenderNid, String userId) {
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria cra = example.createCriteria();
		cra.andBidNidEqualTo(borrowNid);
		cra.andTenderNidEqualTo(tenderNid);
		cra.andCreditUserIdEqualTo(Integer.valueOf(userId));
		cra.andCreditStatusEqualTo(0);
		List<BorrowCredit> list = this.borrowCreditMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 *
	 * 获取用户已承接债转详情
	 * 

	 * @param params
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#getCreditAssignDetail(java.util.Map)
	 */
	@Override
	public AppTenderCreditAssignedDetailCustomize getCreditAssignDetail(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.getCreditAssignDetail(params);
	}

	/**
	 *
	 * 获取用户债转记录件数
	 * 

	 * @param params
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#countCreditRecord(java.util.Map)
	 */
	@Override
	public int countCreditRecord(Map<String, Object> params) {
		// 转让记录状态:0:转让中,1:转让成功,2:全部
		String status = (String) params.get("countStatus");
		Integer userId = (Integer) params.get("userId");
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria cra = example.createCriteria();
		// 转让中
		if ("0".equals(status)) {
			// 转让中
			cra.andCreditStatusEqualTo(0);
		} else if ("1".equals(status)) {
			// 转让成功
			// 转让状态不为0:转让中 并且 已认购本金不为0
			cra.andCreditStatusNotEqualTo(0);
			cra.andCreditCapitalAssignedGreaterThan(BigDecimal.ZERO);
		}
		// 债转用户
		cra.andCreditUserIdEqualTo(userId);
		return borrowCreditMapper.countByExample(example);
	}

	/**
	 *
	 * 获取用户债转列表
     *
	 * @param params
	 * @return
	 * @author yyc
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#searchCreditRecordList(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditRecordListCustomize> searchCreditRecordList(Map<String, Object> params) {
		List<AppTenderCreditRecordListCustomize> list = appTenderCreditCustomizeMapper.searchCreditRecordList(params);
		return list;
	}

	/**
	 *
	 * 根据债转编号获取转让记录详情
	 *
	 * @param params
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#selectTenderCreditRecordDetail(java.lang.String)
	 */
	@Override
	public AppTenderCreditRecordDetailCustomize selectTenderCreditRecordDetail(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.selectTenderCreditRecordDetail(params);
	}

	/**
	 *
	 * 根据用户id,债权编号获取转让明细列表件数
	 *
	 * @param params
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#countCreditRecordDetailList(java.util.Map)
	 */
	@Override
	public int countCreditRecordDetailList(Map<String, Object> params) {
		int recordTotal = this.appTenderCreditCustomizeMapper.countCreditRecordDetailList(params);
		return recordTotal;
	}

	/**
	 *
	 * 根据用户id,债转编号获取转让明细列表
	 *
	 * @param params
	 * @return
	 * @author liuyang
	 * @see com.hyjf.app.tender.credit.AppTenderCreditService#getCreditRecordDetailList(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditRecordDetailListCustomize> getCreditRecordDetailList(Map<String, Object> params) {
		List<AppTenderCreditRecordDetailListCustomize> list = this.appTenderCreditCustomizeMapper.getCreditRecordDetailList(params);
		return list;
	}

	@Override
	public JSONObject checkCreditTenderParam(String creditNid, String account, String userId, String platform, BigDecimal balance) {
		// 判断用户userId是否存在
		if (StringUtils.isEmpty(userId) || !DigitalUtils.isInteger(userId)) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			Users user = this.getUsers(Integer.parseInt(userId));
			// 判断用户信息是否存在
			if (user == null) {
				return jsonMessage("用户信息不存在", "1");
			}
			// 判断用户是否禁用
			if (user.getStatus() == 1) {// 0启用，1禁用
				return jsonMessage("该用户已被禁用", "1");
			}
			BorrowCredit borrowCredit = this.getBorrowCredit(creditNid);
			BankOpenAccount accountChinapnrTender = this.getBankOpenAccount(Integer.parseInt(userId));
			// 用户未在平台开户
			if (accountChinapnrTender == null) {
				return jsonMessage("用户开户信息不存在", "1");
			}
			// 判断借款人开户信息是否存在
			if (StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
				return jsonMessage("用户银行客户号不存在", "1");
			}
			// 判断借款编号是否存在
			if (StringUtils.isEmpty(creditNid)) {
				return jsonMessage("借款项目不存在", "1");
			}
			// 判断借款信息是否存在
			if (borrowCredit == null || borrowCredit.getCreditId() == null) {
				return jsonMessage("债转项目不存在", "1");
			}
			BankOpenAccount accountChinapnrCreditUser = this.getBankOpenAccount(borrowCredit.getCreditUserId());
			if (accountChinapnrCreditUser == null) {
				return jsonMessage("出让人未开户", "1");
			}
			if (StringUtils.isEmpty(accountChinapnrCreditUser.getAccount())) {
				return jsonMessage("出让人银行客户号不存在", "1");
			}
			if (userId.equals(String.valueOf(borrowCredit.getCreditUserId()))) {
				return jsonMessage("您不能承接自己的债权", "1");
			}
			// 债转是否已停止
			if (borrowCredit.getCreditStatus() == 1) {
				return jsonMessage("债转已停止", "1");
			}
			// 判断用户出借金额是否为空
			if (StringUtils.isEmpty(account)) {
				return jsonMessage("请输入出借金额", "1");
			}
			// 还款金额是否数值
			if (!DigitalUtils.isNumber(account)) {
				return jsonMessage("出借金额格式错误", "1");
			}
			if ("0".equals(account)) {
				return jsonMessage("出借金额不能为0元", "1");
			}
			// 出借金额必须是整数
			int accountInt = Integer.parseInt(account);
			if (accountInt < 0) {
				return jsonMessage("出借金额不能为负数", "1");
			}
			// 获取borrow_recover数据
			// 实际支付
			BigDecimal assignPay = null;
			BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
			BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
			borrowRecoverCra.andBorrowNidEqualTo(borrowCredit.getBidNid()).andNidEqualTo(borrowCredit.getTenderNid());
			List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
			if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
				// 获取借款数据
				BorrowExample borrowExample = new BorrowExample();
				BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
				borrowCra.andBorrowNidEqualTo(borrowCredit.getBidNid());
				List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
				BigDecimal yearRate = borrowCredit.getBidApr().divide(new BigDecimal("100"));
				// 计算折后价格
				BigDecimal assignPrice = new BigDecimal(account).setScale(2, BigDecimal.ROUND_DOWN)
						.subtract(new BigDecimal(account).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal("100"), 18, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN));
				if (borrowList != null && borrowList.size() > 0) {
					Borrow borrow = borrowList.get(0);
					// 获取标的借款人
					Integer borrowUserId = borrow.getUserId();
					if (borrowUserId.intValue() == Integer.parseInt(userId)) {
						return jsonMessage("承接人不能为借款人", "1");
					}
					String borrowStyle = borrow.getBorrowStyle();
					// 到期还本还息和按天计息，到期还本还息
					if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
						int lastDays = borrowCredit.getCreditTerm();// 剩余天数
						if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvance(new BigDecimal(account), yearRate, borrow.getBorrowPeriod(),
									new BigDecimal(lastDays));
							// 实付金额
							// 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						} else {// 按月
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvance(new BigDecimal(account), yearRate, borrow.getBorrowPeriod(),
									new BigDecimal(lastDays));
							// 实付金额
							// 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);

						}
					}
					// 等额本息和等额本金和先息后本
					if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
						String bidNid = borrow.getBorrowNid();
						BorrowRepayPlanExample example = new BorrowRepayPlanExample();
						BorrowRepayPlanExample.Criteria cra = example.createCriteria();
						cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrowCredit.getRecoverPeriod() + 1);
						List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
						int lastDays = 0;
						Integer nowTime = GetDate.getNowTime10();// 获取当前时间
						if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
							try {
								String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
								String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRepayPlans.get(0).getRepayTime()));
								lastDays = GetDate.daysBetween(nowDate, recoverDate);
							} catch (NumberFormatException | ParseException e) {
								e.printStackTrace();
							}
						}
						// 已还多少期
						int repayPeriod = borrowCredit.getRecoverPeriod();
						// 应还总本息
						BigDecimal creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(new BigDecimal(account), yearRate, borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
						// 承接人每月应还利息
						BigDecimal interestAssign = BeforeInterestAfterPrincipalUtils.getPerTermInterest(new BigDecimal(account), borrowCredit.getBidApr().divide(new BigDecimal(100)),
								borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
						// 出让人每月应还利息
						BigDecimal interest = BeforeInterestAfterPrincipalUtils.getPerTermInterest(borrowCredit.getCreditCapital(), borrowCredit.getBidApr().divide(new BigDecimal(100)),
								borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
						// 应还总额
						creditAccount = creditAccount.subtract(interestAssign.multiply(new BigDecimal(repayPeriod)));
						// 垫付利息
						// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
						BigDecimal assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(new BigDecimal(account), borrowCredit.getCreditCapital(), yearRate, interest,
								new BigDecimal(lastDays));
						// 实付金额
						// 承接本金*（1-折价率）+应垫付利息
						assignPay = assignPrice.add(assignInterestAdvance);
					}
				}
			}
			if (balance.compareTo(assignPay) < 0) {
				return jsonMessage("余额不足", "1");
			}
			// 将出借金额转化为BigDecimal
			BigDecimal accountBigDecimal = new BigDecimal(account);
			// 债转剩余金额
			BigDecimal money = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
			if (money == BigDecimal.ZERO) {
				return jsonMessage("您来晚了，下次再来抢吧", "1");
			} else {
				if (accountBigDecimal.compareTo(money) > 0) {
					return jsonMessage("项目太抢手了！剩余可投金额只有" + money + "元", "1");
				} else {
					// 如果验证没问题，则返回出借人借款人的汇付账号
					Long borrowerUsrcustid = Long.parseLong(accountChinapnrCreditUser.getAccount());
					Long tenderUsrcustid = Long.parseLong(accountChinapnrTender.getAccount());
					JSONObject jsonMessage = new JSONObject();
					jsonMessage.put(CustomConstants.APP_STATUS, "0");
					jsonMessage.put("creditUsrcustid", borrowerUsrcustid);
					jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
					jsonMessage.put("creditId", borrowCredit.getCreditId());
					return jsonMessage;
				}
			}
		}
	}

	/**
	 * 组成返回信息
	 *
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String data, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(data)) {
			jo = new JSONObject();
			jo.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			jo.put(CustomConstants.APP_STATUS_DESC, data);
		}
		return jo;
	}

	@Override
	public BorrowCredit getBorrowCredit(String creditNid) {
		BorrowCredit borrowCredit = null;
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria cra = example.createCriteria();
		cra.andCreditNidEqualTo(Integer.valueOf(creditNid));
		List<BorrowCredit> list = this.borrowCreditMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			borrowCredit = list.get(0);
		} else {
			borrowCredit = new BorrowCredit();
		}
		return borrowCredit;
	}

	/**
	 * 根据用户Id更新用户新手标志位
	 */
	@Override
	public boolean updateUserInvestFlagById(Integer userId) {
		Integer newFlag = webUserInvestListCustomizeMapper.selectUserInvestFlag(userId);
		if (newFlag != null && newFlag == 0) {
			Integer investFlag = webUserInvestListCustomizeMapper.updateUserInvestFlag(userId);
			if (investFlag > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 同步回调收到后,根据logOrderId检索出借记录表
	 *
	 * @param logOrderId
	 * @return
	 */
	@Override
	public CreditTenderLog selectCreditTenderLogByOrderId(String logOrderId) {
		CreditTenderLogExample example = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria cra = example.createCriteria();
		cra.andLogOrderIdEqualTo(logOrderId);
		List<CreditTenderLog> list = this.creditTenderLogMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 调用江西银行购买债券查询接口
	 *
	 * @param creditTenderLog
	 * @return
	 */
	@Override
	public BankCallBean creditInvestQuery(String assignOrderId, Integer userId) {
		// 承接人用户Id
		BankOpenAccount tenderOpenAccount = this.getBankOpenAccount(userId);
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_INVEST_QUERY);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
		bean.setAccountId(tenderOpenAccount.getAccount());// 存管平台分配的账号
		bean.setOrgOrderId(assignOrderId);// 原购买债权订单号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogUserId(String.valueOf(userId));
		return BankCallUtils.callApiBg(bean);
	}

	@Override
	public boolean updateCreditTenderLog(String logOrderId, Integer userId) {
		CreditTenderLogExample example = new CreditTenderLogExample();
		CreditTenderLogExample.Criteria cra = example.createCriteria();
		cra.andAssignNidEqualTo(logOrderId);
		cra.andUserIdEqualTo(userId);
		CreditTenderLog record = new CreditTenderLog();
		record.setAssignNid(logOrderId);
		record.setUserId(userId);
		record.setAddTime(String.valueOf(GetDate.getNowTime10()));
		return this.creditTenderLogMapper.updateByExampleSelective(record, example) > 0 ? true : false;
	}

	@Override
	public JSONObject redisCreditTender(String creditNid, int userId, String tsfAmount) {

		JSONObject result = new JSONObject();
		// 冻结编号
		BigDecimal accountBigDecimal = new BigDecimal(tsfAmount);
		Jedis jedis = pool.getResource();
		String accountRedisWait = RedisUtils.get(creditNid);
		if (StringUtils.isNotBlank(accountRedisWait)) {
			// 操作redis
			while ("OK".equals(jedis.watch(creditNid))) {
				accountRedisWait = RedisUtils.get(creditNid);
				if (StringUtils.isNotBlank(accountRedisWait)) {
					if (new BigDecimal(accountRedisWait).compareTo(BigDecimal.ZERO) == 0) {
						result.put("message", "您来晚了，下次再来抢吧！");
						result.put("error", 1);
						break;
					} else {
						if (new BigDecimal(accountRedisWait).compareTo(accountBigDecimal) < 0) {
							result.put("message", "可投剩余金额为" + accountRedisWait + "元！");
							result.put("error", 1);
							break;
						} else {
							Transaction tx = jedis.multi();
							BigDecimal lastAccount = new BigDecimal(accountRedisWait).subtract(accountBigDecimal);
							tx.set(creditNid, lastAccount + "");
							List<Object> resultBean = tx.exec();
							if (resultBean == null || resultBean.isEmpty()) {
								jedis.unwatch();
							} else {
								String ret = (String) resultBean.get(0);
								if (ret != null && ret.equals("OK")) {
									result.put("message", "redis操作成功！");
									result.put("error", 0);
									_log.info("PC用户:" + userId + "***冻结前减redis：" + accountBigDecimal);
									break;
								} else {
									jedis.unwatch();
								}
							}
						}
					}
				} else {
					result.put("message", "您来晚了，下次再来抢吧！");
					result.put("error", 1);
					break;
				}
			}
		} else {
			result.put("message", "您来晚了，下次再来抢吧！");
			result.put("error", 1);
		}
		return result;
	}

	/**
	 *
	 * 可转让本金总额
	 *
	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public BigDecimal selectCanCreditMoneyTotal(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectCanCreditMoneyTotal(params);
	}

	/**
	 *
	 * 转让中本金总额
	 * 

	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public BigDecimal selectInCreditMoneyTotal(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectInCreditMoneyTotal(params);
	}

	/**
	 *
	 * 转让成功本金总额
	 *
	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public BigDecimal selectCreditSuccessMoneyTotal(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectCreditSuccessMoneyTotal(params);
	}

	/**
	 *
	 * 转让中未承接部分本金总额
	 * 

	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public BigDecimal selectInCreditNotAssignedMoneyTotal(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectInCreditNotAssignedMoneyTotal(params);
	}

	/**
	 *
	 * 转让中已承接部分本金总额
	 *
	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public BigDecimal selectInCreditAssignedMoneyTotal(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectInCreditAssignedMoneyTotal(params);
	}

	/**
	 *
	 * 查询转让记录
	 *
	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public List<TenderCreditDetailCustomize> selectCreditRecordList(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectCreditRecordList(params);
	}

	/**
	 *
	 * 统计转让记录总数
	 *
	 * @param params
	 * @return
	 * @author hsy
	 */
	@Override
	public int countCreditRecordTotal(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.countCreditRecordTotal(params);
	}

	@Override
	public List<CreditTenderListCustomize> selectWebCreditTenderList(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.selectWebCreditTenderList(params);
	}

	@Override
	public int countWebCreditTenderList(Map<String, Object> params) {
		return tenderCreditCustomizeMapper.countWebCreditTenderList(params);
	}

	@Override
	public JSONObject checkParam(String creditNid, String assignCapital, Integer userId, String string/*, Long lCreate*/) {

		JSONObject result = new JSONObject();
		if (Validator.isNull(userId)) {
			result.put("error", "1");
			result.put("data", "您未登陆，请先登录");
			result.put("errorCode", "707");
			return result;
		}
		Users user = this.getUsers(userId);
		// 判断用户信息是否存在
		if (Validator.isNull(user)) {
			result.put("error", "1");
			result.put("data", "用户信息不存在");
			result.put("errorCode", "707");
			return result;
		}
		// 判断用户是否禁用
		if (user.getStatus() == 1) {// 0启用，1禁用
			result.put("error", "1");
			result.put("data", "该用户已被禁用");
			return result;
		}
		// 判断用户是否开户
		if (user.getBankOpenAccount() == 0) {
			result.put("error", "1");
			result.put("data", "您尚未开户，请先去开户");
			result.put("errorCode", "708");
			return result;
		}
		// 判断用户是否设置了交易密码
		if (user.getIsSetPassword() == 0) {
			result.put("error", "1");
			result.put("data", "您尚未设置交易密码，请先去设置交易密码");
			result.put("errorCode", "709");
			return result;
		}
		// modify by liuyang 20180411 用户是否完成风险测评标识 start
		// JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
		// Integer isAnswer = (Integer) jsonObject.get("userEvaluationResultFlag");
		if (user.getIsEvaluationFlag() == 0) {
			result.put("error", "1");
			result.put("data", "根据监管要求，出借前必须进行风险测评。");
			result.put("errorCode", "710");
			return result;
		} else {
			if (user.getIsEvaluationFlag() == 1 && null != user.getEvaluationExpiredTime()) {
				//测评到期日
				Long lCreate = user.getEvaluationExpiredTime().getTime();
				//当前日期
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//已过期需要重新评测
					result.put("error", "2");
					result.put("data", "根据监管要求，出借前必须进行风险测评。");
					result.put("errorCode", "714");
					return result;
				}
			} else {
				result.put("error", "1");
				result.put("data", "根据监管要求，出借前必须进行风险测评。");
				result.put("errorCode", "710");
				return result;
			}
		}
		// modify by liuyang 20180411 用户是否完成风险测评标识 end
		// 判断借款编号是否存在
		if (StringUtils.isBlank(creditNid)) {
			result.put("error", "1");
			result.put("data", "债转项目不存在");
			return result;
		}
		BorrowCredit borrowCredit = this.getBorrowCredit(creditNid);
		// 判断借款信息是否存在
		if (Validator.isNull(borrowCredit)) {
			result.put("error", "1");
			result.put("data", "债转项目不存在");
			return result;
		}
		if (borrowCredit.getCreditUserId().intValue() == userId.intValue()) {
			result.put("error", "1");
			result.put("data", "出让人不可以承接自己发起的转让。");
			return result;
		}
		String borrowNid = borrowCredit.getBidNid();
		BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
		if (Validator.isNull(borrow)) {
			result.put("error", "1");
			result.put("data", "借款项目不存在");
			return result;
		}
		if (borrow.getUserId().intValue() == userId.intValue()) {
			result.put("error", "1");
			result.put("data", "借款人不可以承接自己的项目。");
			return result;
		}

		BigDecimal creditCapital = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
		// 判断用户出借金额是否为空
		if (StringUtils.isBlank(assignCapital)) {
			result.put("error", "1");
			result.put("data", "请输入出借金额。");
			return result;
		}
		// 还款金额是否数值
		try {
			// 出借金额必须是整数
			long accountInt = Long.parseLong(assignCapital);
			if (accountInt == 0) {
				result.put("error", "1");
				result.put("data", "出借金额不能为0元");
				return result;
			}
			if (accountInt < 0) {
				result.put("error", "1");
				result.put("data", "出借金额不能为负数");
				return result;
			}
		} catch (Exception e) {
			result.put("error", "1");
			result.put("data", "出借金额必须为整数");
			return result;
		}
        // TODO: 2018/10/13  出借开始
		// 新需求判断顺序变化
		// 将出借金额转化为BigDecimal
		BigDecimal accountBigDecimal = new BigDecimal(assignCapital);
		// 剩余可投金额
		Integer min = 1;
		// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
		if (min != null && min != 0 && creditCapital.compareTo(new BigDecimal(min)) == -1) {
			if (accountBigDecimal.compareTo(creditCapital) == 1) {
				result.put("error", "1");
				result.put("data", "剩余可投金额为" + creditCapital + "元");
				return result;
			}
			if (accountBigDecimal.compareTo(creditCapital) != 0) {
				result.put("error", "1");
				result.put("data", "剩余可投只剩" + creditCapital + "元，须全部购买");
				return result;
			}
		} else {
			// 项目的剩余金额大于最低起投金额
			if (accountBigDecimal.compareTo(new BigDecimal(min)) == -1) {
				result.put("error", "1");
				result.put("data", "1元起投");
				return result;
			} else {
				if (accountBigDecimal.compareTo(creditCapital) == 1) {
					result.put("error", "1");
					result.put("data", "项目最大出借额为" + creditCapital + "元");
					return result;
				}
			}
		}
		// 获取债转的详细参数
		TenderToCreditAssignCustomize creditAssign = this.getInterestInfo(creditNid, assignCapital, userId);
		if (Validator.isNull(creditAssign)) {
			result.put("error", "1");
			result.put("data", "获取债转信息失败");
			return result;
		}
		String assignPay = creditAssign.getAssignTotal();
		// 出借人记录
		Account tenderAccount = this.getAccount(userId);
		if (tenderAccount.getBankBalance().compareTo(new BigDecimal(assignPay)) < 0) {
			result.put("error", "1");
			result.put("data", "余额不足，请充值！");
			return result;
		}
		//判断用户的测评金额上限
		// TODO: 2018/10/13  校验用户测评金额和类型并返回
		//从user中获取客户类型，ht_user_evalation_result（用户测评总结表）
		UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
		if(userEvalationResultCustomize != null){
			EvaluationConfig evalConfig = new EvaluationConfig();
			//1.散标／债转出借者测评类型校验
			String debtEvaluationTypeCheck = "0";
			//2.散标／债转单笔投资金额校验
			String deptEvaluationMoneyCheck = "0";
			//3.散标／债转待收本金校验
			String deptCollectionEvaluationCheck = "0";
			//获取开关信息
			List<EvaluationConfig> evalConfigList = evaluationService.selectEvaluationConfig(evalConfig);
			if(evalConfigList != null && evalConfigList.size() > 0){
				evalConfig = evalConfigList.get(0);
				//1.散标／债转出借者测评类型校验
				debtEvaluationTypeCheck = evalConfig.getDebtEvaluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getDebtEvaluationTypeCheck());
				//2.散标／债转单笔投资金额校验
				deptEvaluationMoneyCheck = evalConfig.getDeptEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getDeptEvaluationMoneyCheck());
				//3.散标／债转待收本金校验
				deptCollectionEvaluationCheck = evalConfig.getDeptCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getDeptCollectionEvaluationCheck());
				//7.投标时校验（二期）(预留二期开发)
			}
			//初始化金额返回值
			String revaluation_money,revaluation_money_principal;
			//根据类型从redis或数据库中获取测评类型和上限金额
			String eval_type = userEvalationResultCustomize.getType();
			switch (eval_type){
				case "保守型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getConservativeEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getConservativeEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationPrincipalMoney());
					}
					break;
				case "稳健型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getSteadyEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getSteadyEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationPrincipalMoney());
					}
					break;
				case "成长型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_GROWTH) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getGrowupEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationSingleMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getGrowupEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationPrincipalMoney());
					}
					break;
				case "进取型":
					//从redis获取金额（单笔）
					revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE);
					//如果reids不存在或者为零尝试获取数据库（单笔）
					if("0".equals(revaluation_money)){
						revaluation_money = evalConfig.getEnterprisingEvaluationSinglMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationSinglMoney());
					}
					//从redis获取金额（代收本金）
					revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL);
					//如果reids不存在或者为零尝试获取数据库（代收本金）
					if("0".equals(revaluation_money_principal)){
						revaluation_money_principal = evalConfig.getEnterprisingEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationPrincipalMoney());
					}
					break;
				default:
					revaluation_money = null;
					revaluation_money_principal = null;
			}
			//当前日期
			/*Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测
				result.put("error", CustomConstants.BANK_TENDER_RETURN_ANSWER_EXPIRED);
				return result;
			}*/
			//风险类型校验
			if(CustomConstants.EVALUATION_CHECK.equals(debtEvaluationTypeCheck)){
				//计划类判断用户类型为稳健型以上才可以投资
				if(!CommonUtils.checkStandardInvestment(eval_type,"BORROW_ZZ",borrow.getInvestLevel())){
					//返回错误码
					result.put("error", CustomConstants.BANK_TENDER_RETURN_CUSTOMER_STANDARD_FAIL);
					//返回类型和限额
					result.put("status",false);
					result.put("message","您的风险等级为 #"+eval_type+"# \\n达到 #"+borrow.getInvestLevel()+"# 及以上才可以出借此项目");
					result.put("InvestLevel", eval_type);
					result.put("evalFlagType",borrow.getInvestLevel());
					return result;
				}
			}
			if(revaluation_money == null){
				_log.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
			}else {
				if(CustomConstants.EVALUATION_CHECK.equals(deptEvaluationMoneyCheck)){
					//金额对比判断（校验金额 大于 设置测评金额）
					if (new BigDecimal(assignCapital).compareTo(new BigDecimal(revaluation_money)) > 0) {
						//返回错误码
						result.put("error", CustomConstants.BANK_TENDER_RETURN_LIMIT_EXCESS);
						//返回类型和限额
						result.put("InvestLevel", eval_type);
						result.put("revaluation_money", StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
						return result;
					}
				}
			}
			if(revaluation_money_principal == null){
				_log.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
			}else {
				//代收本金限额校验
				if(CustomConstants.EVALUATION_CHECK.equals(deptCollectionEvaluationCheck)){
					//获取冻结金额和代收本金
					List<AccountDetailCustomize> accountInfos = evaluationService.queryAccountEvalDetail(userId);
					if(accountInfos!= null || accountInfos.size() >0){
						AccountDetailCustomize accountDetail =  accountInfos.get(0);
						BigDecimal planFrost = accountDetail.getPlanFrost();// plan_frost 汇添金计划真实冻结金额
						BigDecimal bankFrost = accountDetail.getBankFrost();// bank_frost 银行冻结金额
						BigDecimal bankAwaitCapital = accountDetail.getBankAwaitCapital();// bank_await_capital 银行待收本金
						BigDecimal account = BigDecimal.ZERO;
						//加法运算
						account = account.add(planFrost).add(bankFrost).add(bankAwaitCapital).add(new BigDecimal(assignCapital));
						//金额对比判断（校验金额 大于 设置测评金额）（代收本金）
						if (account.compareTo(new BigDecimal(revaluation_money_principal)) > 0) {
							//返回错误码
							result.put("error", CustomConstants.BANK_TENDER_RETURN_LIMIT_EXCESS_PRINCIPAL);
							//返回类型和限额
							result.put("status",false);
							result.put("InvestLevel", eval_type);
							result.put("revaluation_money_principal", StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue()));
							return result;
						}
					}
				}
			}
		}else{
			_log.info("=============该用户测评总结数据为空! userId="+userId);
		}

		// 返回成功状态
		result.put("error", "0");
		result.put("data", "校验成功！");
		return result;
	}
	
	

	@Override
	public Map<String, Object> selectUserPlanCreditContract(CreditAssignedBean tenderCreditAssignedBean) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取债转出借信息
		DebtCreditTenderExample creditTenderExample = new DebtCreditTenderExample();
		DebtCreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid())
				.andAssignOrderIdEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
		List<DebtCreditTender> creditTenderList = this.debtCreditTenderMapper.selectByExample(creditTenderExample);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			DebtCreditTender creditTender = creditTenderList.get(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creditNid", creditTender.getCreditNid());
			List<TenderToCreditDetailCustomize> tenderToCreditDetailList =
					tenderCreditCustomizeMapper.selectPlanWebCreditTenderDetail(params);
			if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
				if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
					tenderToCreditDetailList.get(0).setCreditRepayEndTime(
							GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0)
									.getCreditRepayEndTime())));
				}
				if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
					try {
						tenderToCreditDetailList.get(0).setCreditTime(
								GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(),
										"yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				resultMap.put("tenderToCreditDetail", tenderToCreditDetailList.get(0));
			}
			// 获取借款标的信息
			DebtBorrowExample borrowExample = new DebtBorrowExample();
			DebtBorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(creditTender.getBorrowNid());
			List<DebtBorrow> borrow = this.debtBorrowMapper.selectByExample(borrowExample);
			// 获取债转信息
			DebtCreditExample borrowCreditExample = new DebtCreditExample();
			DebtCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
			borrowCreditCra.andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid())
					.andBorrowNidEqualTo(creditTender.getBorrowNid());
			List<DebtCredit> borrowCredit = this.debtCreditMapper.selectByExample(borrowCreditExample);
			// 获取承接人身份信息
			UsersInfoExample usersInfoExample = new UsersInfoExample();
			UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
			usersInfoCra.andUserIdEqualTo(creditTender.getUserId());
			List<UsersInfo> usersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
			// 获取承接人平台信息
			UsersExample usersExample = new UsersExample();
			UsersExample.Criteria usersCra = usersExample.createCriteria();
			usersCra.andUserIdEqualTo(creditTender.getUserId());
			List<Users> users = this.usersMapper.selectByExample(usersExample);
			// 获取融资方平台信息
			UsersExample usersBorrowExample = new UsersExample();
			UsersExample.Criteria usersBorrowCra = usersBorrowExample.createCriteria();
			usersBorrowCra.andUserIdEqualTo(borrow.get(0).getUserId());
			List<Users> usersBorrow = this.usersMapper.selectByExample(usersBorrowExample);
			// 获取债转人身份信息
			UsersInfoExample usersInfoExampleCredit = new UsersInfoExample();
			UsersInfoExample.Criteria usersInfoCraCredit = usersInfoExampleCredit.createCriteria();
			usersInfoCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
			List<UsersInfo> usersInfoCredit = this.usersInfoMapper.selectByExample(usersInfoExampleCredit);
			// 获取债转人平台信息
			UsersExample usersExampleCredit = new UsersExample();
			UsersExample.Criteria usersCraCredit = usersExampleCredit.createCriteria();
			usersCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
			List<Users> usersCredit = this.usersMapper.selectByExample(usersExampleCredit);
			SimpleDateFormat sdfM = new SimpleDateFormat("MM");
			SimpleDateFormat sdfD = new SimpleDateFormat("dd");
			creditTender.setUpdateUserName(sdfM.format(new Date(creditTender.getCreateTime())));
			// 将int类型时间转成字符串
			creditTender.setAddip(sdfD.format(new Date(creditTender.getAssignRepayEndTime())));// 借用ip字段存储最后还款时间
			resultMap.put("creditTender", creditTender);
			if (borrow != null && borrow.size() > 0) {
				if (borrow.get(0).getReverifyTime() != null) {
					borrow.get(0).setReverifyTime(
							GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getReverifyTime())));
				}
				if (borrow.get(0).getRepayLastTime() != null) {
					borrow.get(0).setRepayLastTime(
							GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getRepayLastTime())));
				}
				resultMap.put("borrow", borrow.get(0));
				// 获取借款人信息
				UsersInfoExample usersInfoExampleBorrow = new UsersInfoExample();
				UsersInfoExample.Criteria usersInfoCraBorrow = usersInfoExampleBorrow.createCriteria();
				usersInfoCraBorrow.andUserIdEqualTo(borrow.get(0).getUserId());
				List<UsersInfo> usersInfoBorrow = this.usersInfoMapper.selectByExample(usersInfoExampleBorrow);
				if (usersInfoBorrow != null && usersInfoBorrow.size() > 0) {
					if (usersInfoBorrow.get(0).getTruename().length() > 1) {
						usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename().substring(0, 1) + "**");
					}
					if (usersInfoBorrow.get(0).getIdcard().length() > 8) {
						usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard().substring(0, 8) + "*****");
					}
					resultMap.put("usersInfoBorrow", usersInfoBorrow.get(0));
				}
			}
			if (borrowCredit != null && borrowCredit.size() > 0) {
				resultMap.put("borrowCredit", borrowCredit.get(0));
			}
			if (usersInfo != null && usersInfo.size() > 0) {
				if (usersInfo.get(0).getTruename().length() > 1) {
					usersInfo.get(0).setTruename(usersInfo.get(0).getTruename().substring(0, 1) + "**");
				}
				if (usersInfo.get(0).getIdcard().length() > 8) {
					usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard().substring(0, 8) + "*****");
				}
				resultMap.put("usersInfo", usersInfo.get(0));
			}
			if (usersBorrow != null && usersBorrow.size() > 0) {
				if (usersBorrow.get(0).getUsername().length() > 1) {
					usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername().substring(0, 1) + "**");
				}
				resultMap.put("usersBorrow", usersBorrow.get(0));
			}
			if (users != null && users.size() > 0) {
				if (users.get(0).getUsername().length() > 1) {
					users.get(0).setUsername(users.get(0).getUsername().substring(0, 1) + "**");
				}
				resultMap.put("users", users.get(0));
			}
			if (usersCredit != null && usersCredit.size() > 0) {
				if (usersCredit.get(0).getUsername().length() > 1) {
					usersCredit.get(0).setUsername(usersCredit.get(0).getUsername().substring(0, 1) + "**");
				}
				resultMap.put("usersCredit", usersCredit.get(0));
			}
			if (usersInfoCredit != null && usersInfoCredit.size() > 0) {
				if (usersInfoCredit.get(0).getTruename().length() > 1) {
					usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename().substring(0, 1) + "**");
				}
				if (usersInfoCredit.get(0).getIdcard().length() > 8) {
					usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard().substring(0, 8) + "*****");
				}
				resultMap.put("usersInfoCredit", usersInfoCredit.get(0));
			}
			String phpWebHost = PropUtils.getSystem("hyjf.web.host.php");
			if (StringUtils.isNotEmpty(phpWebHost)) {
				resultMap.put("phpWebHost", phpWebHost);
			} else {
				resultMap.put("phpWebHost", "http://site.hyjf.com");
			}
		}
		return resultMap;
	}
	/**
	 * 根据出借订单号查询已承接金额
	 *
	 * @param tenderNid
	 * @return
	 */
	private BigDecimal getAssignCapital(String tenderNid) {
		BigDecimal assignCapital = BigDecimal.ZERO;
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria cra = example.createCriteria();
		cra.andCreditTenderNidEqualTo(tenderNid);
		cra.andAddTimeLessThanOrEqualTo(String.valueOf(1499011200));
		List<CreditTender> list = this.creditTenderMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			for (CreditTender creditTender : list) {
				assignCapital = assignCapital.add(creditTender.getAssignCapital());
			}
		}
		return assignCapital;
	}

	/**
	 * 判断用户所在渠道是否允许债转
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public boolean isAllowChannelAttorn(Integer userId) {
		// 根据userId获取用户注册推广渠道
		UtmPlat utmPlat = utmPlatCustomizeMapper.selectUtmPlatByUserId(userId);
		if (utmPlat != null && utmPlat.getAttornFlag() == 0)
			return false;
		return true;
	}

	/**
	 * 用户中心债转被出借的协议(汇计划)
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> selectHJHUserCreditContract(CreditAssignedBean tenderCreditAssignedBean) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取债转出借信息
		HjhDebtCreditTenderExample creditTenderExample = new HjhDebtCreditTenderExample();
		HjhDebtCreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andBorrowNidEqualTo(tenderCreditAssignedBean.getBidNid());
		creditTenderCra.andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid());
		creditTenderCra.andInvestOrderIdEqualTo(tenderCreditAssignedBean.getCreditTenderNid());
		creditTenderCra.andAssignOrderIdEqualTo(tenderCreditAssignedBean.getAssignNid());

		// 当前用户的id
		Integer currentUserId = tenderCreditAssignedBean.getCurrentUserId();

		//查询 hyjf_hjh_debt_credit_tender 表
		List<HjhDebtCreditTender> creditTenderList = this.hjhDebtCreditTenderMapper.selectByExample(creditTenderExample);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			HjhDebtCreditTender creditTender = creditTenderList.get(0);
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("creditNid", creditTender.getCreditNid());//取得 hyjf_hjh_debt_credit_tender 表的债转编号
			params.put("assignOrderId", creditTender.getAssignOrderId());//取得 hyjf_hjh_debt_credit_tender 表的债转编号

			//查看债转详情
			List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectHJHWebCreditTenderDetail(params);//查汇计划债转详情

			if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
				if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
					tenderToCreditDetailList.get(0).setCreditRepayEndTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0).getCreditRepayEndTime())));
				}
				if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
					try {
						tenderToCreditDetailList.get(0).setCreditTime(GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				resultMap.put("tenderToCreditDetail", tenderToCreditDetailList.get(0));
			}


			// 获取借款标的信息
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(creditTender.getBorrowNid());
			List<Borrow> borrow = this.borrowMapper.selectByExample(borrowExample);

			// 获取债转信息(新表)
			HjhDebtCreditExample borrowCreditExample = new HjhDebtCreditExample();
			HjhDebtCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
			borrowCreditCra.andBorrowNidEqualTo(tenderCreditAssignedBean.getBidNid());
			borrowCreditCra.andCreditNidEqualTo(tenderCreditAssignedBean.getCreditNid());
			borrowCreditCra.andInvestOrderIdEqualTo(tenderCreditAssignedBean.getCreditTenderNid());//??
			List<HjhDebtCredit> borrowCredit = this.hjhDebtCreditMapper.selectByExample(borrowCreditExample);


			// 获取承接人身份信息
			UsersInfoExample usersInfoExample = new UsersInfoExample();
			UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
			usersInfoCra.andUserIdEqualTo(creditTender.getUserId());
			List<UsersInfo> usersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);

			// 获取承接人平台信息
			UsersExample usersExample = new UsersExample();
			UsersExample.Criteria usersCra = usersExample.createCriteria();
			usersCra.andUserIdEqualTo(creditTender.getUserId());
			List<Users> users = this.usersMapper.selectByExample(usersExample);

			// 获取融资方平台信息
			UsersExample usersBorrowExample = new UsersExample();
			UsersExample.Criteria usersBorrowCra = usersBorrowExample.createCriteria();
			usersBorrowCra.andUserIdEqualTo(borrow.get(0).getUserId());
			List<Users> usersBorrow = this.usersMapper.selectByExample(usersBorrowExample);

			// 获取债转人身份信息
			UsersInfoExample usersInfoExampleCredit = new UsersInfoExample();
			UsersInfoExample.Criteria usersInfoCraCredit = usersInfoExampleCredit.createCriteria();
			usersInfoCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
			List<UsersInfo> usersInfoCredit = this.usersInfoMapper.selectByExample(usersInfoExampleCredit);

			// 获取债转人平台信息
			UsersExample usersExampleCredit = new UsersExample();
			UsersExample.Criteria usersCraCredit = usersExampleCredit.createCriteria();
			usersCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
			List<Users> usersCredit = this.usersMapper.selectByExample(usersExampleCredit);
			// 将int类型时间转成字符串
			/*creditTender.setCreateTime(Integer.valueOf(GetDate.times10toStrYYYYMMDD(creditTender.getCreateTime())));*/
			String createTime = GetDate.times10toStrYYYYMMDD(creditTender.getCreateTime());
			String addip = GetDate.getDateMyTimeInMillis(creditTender.getAssignRepayEndTime());// 借用ip字段存储最后还款时
			resultMap.put("createTime", createTime);//记得去JSP上替换 creditResult.data.creditTender.addip 字段，要新建一个JSP
			resultMap.put("addip", addip);//记得去JSP上替换 creditResult.data.creditTender.addip 字段，要新建一个JSP
			resultMap.put("creditTender", creditTender);

			if (borrow != null && borrow.size() > 0) {
				if (borrow.get(0).getReverifyTime() != null) {
					borrow.get(0).setReverifyTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getReverifyTime())));
				}
				if (borrow.get(0).getRepayLastTime() != null) {
					borrow.get(0).setRepayLastTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getRepayLastTime())));
				}
				resultMap.put("borrow", borrow.get(0));
				// 获取借款人信息
				UsersInfoExample usersInfoExampleBorrow = new UsersInfoExample();
				UsersInfoExample.Criteria usersInfoCraBorrow = usersInfoExampleBorrow.createCriteria();
				usersInfoCraBorrow.andUserIdEqualTo(borrow.get(0).getUserId());
				List<UsersInfo> usersInfoBorrow = this.usersInfoMapper.selectByExample(usersInfoExampleBorrow);
				if (usersInfoBorrow != null && usersInfoBorrow.size() > 0) {
					if (usersInfoBorrow.get(0).getTruename().length() > 1) {

						if (usersInfoBorrow.get(0).getUserId().equals(currentUserId)) {
							usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename());
						} else {
							usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename().substring(0, 1) + "**");
						}
					}
					if (usersInfoBorrow.get(0).getIdcard().length() > 8) {

						if (usersInfoBorrow.get(0).getUserId().equals(currentUserId)) {
							usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard());
						} else {
							usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard().substring(0, 4) + "**************");
						}
					}
					resultMap.put("usersInfoBorrow", usersInfoBorrow.get(0));
				}
			}

			if (borrowCredit != null && borrowCredit.size() > 0) {
				resultMap.put("borrowCredit", borrowCredit.get(0));
			}


			if (usersInfo != null && usersInfo.size() > 0) {
				if (usersInfo.get(0).getTruename().length() > 1) {
					if (usersInfo.get(0).getUserId().equals(currentUserId)) {
						usersInfo.get(0).setTruename(usersInfo.get(0).getTruename());
					} else {
						usersInfo.get(0).setTruename(usersInfo.get(0).getTruename().substring(0, 1) + "**");
					}

				}
				if (usersInfo.get(0).getIdcard().length() > 8) {
					if (usersInfo.get(0).getUserId().equals(currentUserId)) {
						usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard());
					} else {
						usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard().substring(0, 4) + "**************");
					}
				}
				resultMap.put("usersInfo", usersInfo.get(0));
			}


			if (usersBorrow != null && usersBorrow.size() > 0) {
				if (usersBorrow.get(0).getUsername().length() > 1) {

					if (usersBorrow.get(0).getUserId().equals(currentUserId)) {
						usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername());
					} else {
						usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername().substring(0, 1) + "*****");
					}
				}
				resultMap.put("usersBorrow", usersBorrow.get(0));
			}

			if (users != null && users.size() > 0) {
				if (users.get(0).getUsername().length() > 1) {

					if (users.get(0).getUserId().equals(currentUserId)) {
						users.get(0).setUsername(users.get(0).getUsername());
					} else {
						users.get(0).setUsername(users.get(0).getUsername().substring(0, 1) + "*****");
					}
				}
				resultMap.put("users", users.get(0));
			}

			if (usersCredit != null && usersCredit.size() > 0) {
				if (usersCredit.get(0).getUsername().length() > 1) {
					if (usersCredit.get(0).getUserId().equals(currentUserId)) {
						usersCredit.get(0).setUsername(usersCredit.get(0).getUsername());
					} else {
						usersCredit.get(0).setUsername(usersCredit.get(0).getUsername().substring(0, 1) + "*****");
					}
				}
				resultMap.put("usersCredit", usersCredit.get(0));
			}

			if (usersInfoCredit != null && usersInfoCredit.size() > 0) {
				if (usersInfoCredit.get(0).getTruename().length() > 1) {
					if (usersInfoCredit.get(0).getUserId().equals(currentUserId)) {
						usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename());
					} else {
						usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename().substring(0, 1) + "**");
					}
				}
				if (usersInfoCredit.get(0).getIdcard().length() > 8) {
					if (usersInfoCredit.get(0).getUserId().equals(currentUserId)) {
						usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard());
					} else {
						usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard().substring(0, 4) + "**************");
					}
				}
				resultMap.put("usersInfoCredit", usersInfoCredit.get(0));
			}

			String phpWebHost = PropUtils.getSystem("hyjf.web.host.php");
			if (StringUtils.isNotEmpty(phpWebHost)) {
				resultMap.put("phpWebHost", phpWebHost);
			} else {
				resultMap.put("phpWebHost", "http://site.hyjf.com");
			}
		}
		return resultMap;
	}

	/**
	 * 发送神策统计MQ
	 *
	 * @param sensorsDataBean
	 */
	@Override
	public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("eventCode", sensorsDataBean.getEventCode());
		params.put("userId", sensorsDataBean.getUserId());
		if ("submit_credit_assign".equals(sensorsDataBean.getEventCode())) {
			// 发起转让MQ
			params.put("presetProps", sensorsDataBean.getPresetProps());
			params.put("creditNid", sensorsDataBean.getCreditNid());
		} else if ("receive_credit_assign".equals(sensorsDataBean.getEventCode())) {
			// 承接债转MQ
			params.put("orderId", sensorsDataBean.getOrderId());
		}
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.SENSORS_DATA_ROUTINGKEY_CREDIT, JSONObject.toJSONString(params));
	}

}
