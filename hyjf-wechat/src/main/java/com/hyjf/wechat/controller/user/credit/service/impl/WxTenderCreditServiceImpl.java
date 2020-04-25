package com.hyjf.wechat.controller.user.credit.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.credit.RepayPlanBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AccountManagementFeeUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DigitalUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.CreditTenderLog;
import com.hyjf.mybatis.model.auto.CreditTenderLogExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.SmsConfigExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderBorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRepayPlanListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditAssignCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditListCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditAssignCustomize;
import com.hyjf.mybatis.model.customize.web.TenderToCreditDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.user.credit.entity.WxTenderCreditAssignedBean;
import com.hyjf.wechat.controller.user.credit.service.WxTenderCreditService;

import redis.clients.jedis.JedisPool;

/**
 * 
 * 债权转让Service
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月28日
 * @see 下午5:20:17
 */
@Service
public class WxTenderCreditServiceImpl extends BaseServiceImpl implements WxTenderCreditService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailProcesser;

	public static JedisPool pool = RedisUtils.getConnection();

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");
	private static String oldOrNewDate = "2016-12-27 20:00:00";

	@Override
	public int countTenderCreditListRecordCount(Map<String, Object> params) {
		// 债转状态
		params.put("creditStatus", "0");
		return appTenderCreditCustomizeMapper.countTenderCreditListRecordCount(params);
	}

	@Override
	public List<AppProjectListCustomize> searchTenderCreditList(Map<String, Object> params) {
		return appTenderCreditCustomizeMapper.searchTenderCreditList(params);
	}

	@Override
	public AppTenderToCreditDetailCustomize selectCreditTenderDetail(String creditNid) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creditNid", creditNid);
		List<AppTenderToCreditDetailCustomize> list = appTenderCreditCustomizeMapper.selectCreditTenderDetail(params);
		AppTenderToCreditDetailCustomize appTenderToCreditDetailCustomize = null;
		if (list != null && list.size() > 0) {
			appTenderToCreditDetailCustomize = list.get(0);
		} else {
			return null;
		}
		return appTenderToCreditDetailCustomize;
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
				if (borrow.getReverifyStatus() == 6) {
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
					if (borrow.getReverifyStatus() == 6) {
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

	/**
	 * 
	 * 查询债转出借记录件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#countTenderCreditInvestRecordTotal(java.util.Map)
	 */
	@Override
	public int countTenderCreditInvestRecordTotal(Map<String, Object> params) {
		return appTenderCreditCustomizeMapper.countTenderCreditInvestRecordTotal(params);
	}

	/**
	 * 
	 * 获取债转出借列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#searchTenderCreditInvestList(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditInvestListCustomize> searchTenderCreditInvestList(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.searchTenderCreditInvestList(params);
	}

	/**
	 * 
	 * 获取可债转列表的数量
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowTime
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#countTenderToCredit(int,
	 *      int)
	 */
	@Override
	public int countTenderToCredit(int userId, int nowTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nowTime", nowTime);
		params.put("userId", userId);
		int total = appTenderCreditCustomizeMapper.countTenderToCredit(params);
		return total;
	}

	/**
	 * 
	 * 获取可债转列表的数据
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectTenderToCreditList(java.util.Map)
	 */
	@Override
	public List<AppTenderToCreditListCustomize> selectTenderToCreditList(Map<String, Object> params) {
		return appTenderCreditCustomizeMapper.selectTenderToCreditList(params);
	}

	/**
	 * 
	 * 验证出借人当天是否可以债转
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowDate
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#tenderAbleToCredit(java.lang.Integer,
	 *      int)
	 */
	@Override
	public Integer tenderAbleToCredit(Integer userId, int nowDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nowDate", nowDate);
		params.put("userId", userId);
		Integer creditedNum = appTenderCreditCustomizeMapper.tenderAbleToCredit(params);
		return creditedNum;
	}

	/**
	 * 
	 * 查询出借可债转详细
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowTime
	 * @param borrowNid
	 * @param tenderNid
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectTenderToCreditDetail(int,
	 *      int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<AppTenderCreditCustomize> selectTenderToCreditDetail(int userId, int nowTime, String borrowNid, String tenderNid) {
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
	 * 获取用户已承接债转列表的件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#countCreditAssigned(java.util.Map)
	 */
	@Override
	public int countCreditAssigned(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.countCreditTenderAssigned(params);
	}

	/**
	 * 
	 * 查询用户已承接债转列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectCreditAssigned(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditAssignedListCustomize> selectCreditAssigned(Map<String, Object> params) {
		List<AppTenderCreditAssignedListCustomize> list = appTenderCreditCustomizeMapper.selectCreditTenderAssigned(params);
		return list;
	}

	/**
	 * 
	 * 获取用户已承接债转详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#getCreditAssignDetail(java.util.Map)
	 */
	@Override
	public AppTenderCreditAssignedDetailCustomize getCreditAssignDetail(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.getCreditAssignDetail(params);
	}

	/**
	 * 
	 * 获取用户债转记录件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#countCreditRecord(java.util.Map)
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
	 * @author yyc
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#searchCreditRecordList(java.util.Map)
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
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectTenderCreditRecordDetail(java.lang.String)
	 */
	@Override
	public AppTenderCreditRecordDetailCustomize selectTenderCreditRecordDetail(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.selectTenderCreditRecordDetail(params);
	}

	/**
	 * 
	 * 根据用户id,债权编号获取转让明细列表件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#countCreditRecordDetailList(java.util.Map)
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
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#getCreditRecordDetailList(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditRecordDetailListCustomize> getCreditRecordDetailList(Map<String, Object> params) {
		List<AppTenderCreditRecordDetailListCustomize> list = this.appTenderCreditCustomizeMapper.getCreditRecordDetailList(params);
		return list;
	}

	/**
	 * 
	 * 根据债转nid获取债转信息
	 * 
	 * @author liuyang
	 * @param creditNid
	 * @return
	 */
	@Override
	public BorrowCredit selectCreditTenderByCreditNid(String creditNid) {
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria cra = example.createCriteria();
		cra.andCreditNidEqualTo(Integer.valueOf(creditNid));
		List<BorrowCredit> list = borrowCreditMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * 根据原标标号检索标的信息
	 * 
	 * @author liuyang
	 * @param bidNid
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectBorrowByBorrowNid(java.lang.String)
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
	 * 获取原标的借款方式
	 * 
	 * @author liuyang
	 * @param borrowType
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectBorrowStyleByStyle(java.lang.String)
	 */
	@Override
	public BorrowStyle selectBorrowStyleByStyle(String borrowType) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andNidEqualTo(borrowType);
		List<BorrowStyle> list = borrowStyleMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * 获取未分期还款计划的件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#countRepayRecoverListRecordTotal(java.util.Map)
	 */
	@Override
	public int countRepayRecoverListRecordTotal(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.countRepayRecoverListRecordTotal(params);
	}

	/**
	 * 
	 * 获取不分期债转的还款计划
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectRepayRecoverList(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params) {

		return appTenderCreditCustomizeMapper.selectRepayRecoverList(params);
	}

	/**
	 * 
	 * 获取分期债转的还款计划件数
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#countRepayPlanListRecordTotal(java.util.Map)
	 */
	@Override
	public int countRepayPlanListRecordTotal(Map<String, Object> params) {
		// 用户id
		String userId = (String) params.get("userId");
		// 债转编号
		String creditNid = (String) params.get("creditNid");
		// 认购单号
		String assignNid = (String) params.get("assignNid");
		CreditRepayExample example = new CreditRepayExample();
		CreditRepayExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(Integer.parseInt(userId));
		cra.andAssignNidEqualTo(assignNid);
		cra.andCreditNidEqualTo(creditNid);
		return creditRepayMapper.countByExample(example);
	}

	/**
	 * 
	 * 获取分期债转的还款计划列表
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectRepayPlanList(java.util.Map)
	 */
	@Override
	public List<AppTenderCreditRepayPlanListCustomize> selectRepayRecoverPlanList(Map<String, Object> params) {
		List<AppTenderCreditRepayPlanListCustomize> list = this.appTenderCreditCustomizeMapper.selectRepayRecoverPlanList(params);
		return list;
	}

	/**
	 * 
	 * 查看债转协议
	 * 
	 * @author liuyang
	 * @param appTenderCreditAssignedBean
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectUserCreditContract(com.hyjf.WxTenderCreditAssignedBean.user.credit.AppTenderCreditAssignedBean)
	 */
	@Override
	public Map<String, Object> selectUserCreditContract(WxTenderCreditAssignedBean appTenderCreditAssignedBean) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取债转出借信息
		CreditTenderExample creditTenderExample = new CreditTenderExample();
		CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
		creditTenderCra.andAssignNidEqualTo(appTenderCreditAssignedBean.getAssignNid()).andBidNidEqualTo(appTenderCreditAssignedBean.getBidNid())
				.andCreditNidEqualTo(appTenderCreditAssignedBean.getCreditNid()).andCreditTenderNidEqualTo(appTenderCreditAssignedBean.getCreditTenderNid());
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			CreditTender creditTender = creditTenderList.get(0);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creditNid", creditTender.getCreditNid());
			List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectWebCreditTenderDetail(params);
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
			borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(appTenderCreditAssignedBean.getCreditNid())).andBidNidEqualTo(appTenderCreditAssignedBean.getBidNid())
					.andTenderNidEqualTo(appTenderCreditAssignedBean.getCreditTenderNid());
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
			// String phpWebHost = PropUtils.getSystem("hyjf.web.host.php");
			// if(StringUtils.isNotEmpty(phpWebHost)){
			// resultMap.put("phpWebHost", phpWebHost);
			// }else{
			// resultMap.put("phpWebHost", "http://site.hyjf.com");
			// }
		}
		return resultMap;
	}

	/**
	 * 
	 * 检验手机验证码
	 * 
	 * @author liuyang
	 * @param phone
	 * @param code
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#checkMobileCode(java.lang.String,
	 *      java.lang.String)
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
	 * 
	 * 债转提交保存
	 * 
	 * @author liuyang
	 * @param userId
	 * @param nowTime
	 * @param appTenderBorrowCreditCustomize
	 * @return
	 * @throws Exception
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#insertTenderToCredit(int,
	 *      int,
	 *      com.hyjf.mybatis.model.customize.app.AppTenderBorrowCreditCustomize)
	 */
	@Override
	public Integer insertTenderToCredit(int userId, int nowTime, AppTenderBorrowCreditCustomize appTenderBorrowCreditCustomize, String platform) throws Exception {
		// 债转计算
		Map<String, BigDecimal> creditCreateMap = selectExpectCreditFeeForBigDecimal(appTenderBorrowCreditCustomize.getBorrowNid(), appTenderBorrowCreditCustomize.getTenderNid(),
				appTenderBorrowCreditCustomize.getCreditDiscount(), nowTime);
		// 声明要保存的债转对象
		BorrowCredit borrowCredit = new BorrowCredit();
		Integer now = GetDate.getNowTime10();
		Integer returnCreditNid = null;
		// 获取borrow_recover数据
		BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria borrowRecoverCra = borrowRecoverExample.createCriteria();
		borrowRecoverCra.andBorrowNidEqualTo(appTenderBorrowCreditCustomize.getBorrowNid());
		borrowRecoverCra.andNidEqualTo(appTenderBorrowCreditCustomize.getTenderNid());
		// 获取还款数据
		List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(borrowRecoverExample);
		// 获取borrow数据
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(appTenderBorrowCreditCustomize.getBorrowNid());
		List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);

		if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
			BorrowRecover borrowRecover = borrowRecoverList.get(0);
			// 生成creditNid
			String nowDate = (GetDate.yyyyMMdd.format(new Date()) != null && !"".equals(GetDate.yyyyMMdd.format(new Date()))) ? GetDate.yyyyMMdd.format(new Date()) : "0";// 获取当前时间的日期
			Integer creditNumToday = this.tenderAbleToCredit(null, Integer.parseInt(nowDate)) != null ? this.tenderAbleToCredit(null, Integer.parseInt(nowDate)) : 0;
			String creditNid = nowDate.substring(2) + String.format("%04d", (creditNumToday + 1));
			// 获取待债转数据
			List<AppTenderCreditCustomize> tenderToCreditList = this.selectTenderToCreditDetail(userId, nowTime, appTenderBorrowCreditCustomize.getBorrowNid(),
					appTenderBorrowCreditCustomize.getTenderNid());
			borrowCredit.setCreditNid(Integer.parseInt(creditNid));// 债转nid
			borrowCredit.setCreditUserId(userId);// 转让用户id
			int lastdays = 0;
			int holddays = 0;
			if (tenderToCreditList != null && tenderToCreditList.size() > 0) {
				if (borrowList != null && borrowList.size() > 0) {
					String borrowStyle = borrowList.get(0).getBorrowStyle();
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
						String bidNid = borrowList.get(0).getBorrowNid();
						BorrowRepayPlanExample example = new BorrowRepayPlanExample();
						BorrowRepayPlanExample.Criteria cra = example.createCriteria();
						cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrowList.get(0).getBorrowPeriod());
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
				borrowCredit.setCreditCapital(creditCreateMap.get("creditCapital"));// 债转本金
				borrowCredit.setCreditAccount(creditCreateMap.get("creditAccount"));// 债转总额
				borrowCredit.setCreditInterest(creditCreateMap.get("creditInterest"));// 债转总利息
				borrowCredit.setCreditInterestAdvance(creditCreateMap.get("assignInterestAdvance"));// 需垫付利息
				borrowCredit.setCreditDiscount(new BigDecimal(appTenderBorrowCreditCustomize.getCreditDiscount()));// 折价率
				borrowCredit.setCreditIncome(creditCreateMap.get("assignPay"));// 总收入,本金+垫付利息
				borrowCredit.setCreditFee(creditCreateMap.get("assignPay").multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_DOWN));// 服务费
				borrowCredit.setCreditPrice(creditCreateMap.get("creditPrice"));// 出让价格
				borrowCredit.setCreditCapitalAssigned(BigDecimal.ZERO);// 已认购本金
				borrowCredit.setCreditInterestAssigned(BigDecimal.ZERO);// 已承接部分的利息
				borrowCredit.setCreditInterestAdvanceAssigned(BigDecimal.ZERO);// 已垫付利息
				borrowCredit.setCreditRepayAccount(BigDecimal.ZERO);// 已还款总额
				borrowCredit.setCreditRepayCapital(BigDecimal.ZERO);// 已还本金
				borrowCredit.setCreditRepayInterest(BigDecimal.ZERO);// 已还利息
				borrowCredit.setCreditRepayEndTime(Integer.parseInt(GetDate.get10Time(tenderToCreditList.get(0).getRepayLastTime())));// 债转最后还款日
				borrowCredit.setCreditRepayLastTime(Integer.parseInt(borrowRecover.getRecoverYestime() != null ? borrowRecover.getRecoverYestime() : String.valueOf(now)));// 上次还款日
				borrowCredit.setCreditRepayNextTime(Integer.parseInt(borrowRecover.getRecoverTime() != null ? borrowRecover.getRecoverTime() : String.valueOf(now)));// 下次还款日
				borrowCredit.setCreditRepayYesTime(0);// 最终实际还款日
			}
			borrowCredit.setCreateDate(Integer.parseInt(GetDate.yyyyMMdd.format(new Date())));// 创建日期
			borrowCredit.setAddTime(now);// 创建时间
			borrowCredit.setEndTime(now + 24 * 3600 * 3);// 结束时间
			borrowCredit.setAssignTime(0);// 认购时间
			borrowCredit.setAssignNum(0);// 出借次数
			borrowCredit.setRepayStatus(0);// 还款状态 0还款中、1已还款、2还款失败
			if (borrowList != null && borrowList.size() > 0) {
				if (borrowList.get(0).getBorrowStyle().equals("endmonth")) {
					borrowCredit.setRecoverPeriod(borrowList.get(0).getBorrowPeriod() - borrowRecover.getRecoverPeriod());// 从第几期开始
				} else {
					borrowCredit.setRecoverPeriod(0);// 从第几期开始
				}
			} else {
				borrowCredit.setRecoverPeriod(0);// 从第几期开始
			}
			borrowCredit.setClient(Integer.parseInt(platform));// 客户端
			boolean insertResultFlag = this.borrowCreditMapper.insertSelective(borrowCredit) > 0 ? true : false;
			if (insertResultFlag) {
				returnCreditNid = borrowCredit.getCreditNid();
				// 还款表更新债转时间
				borrowRecover.setCreditTime(now);
				boolean isUpdateFlag = this.borrowRecoverMapper.updateByPrimaryKey(borrowRecover) > 0 ? true : false;
				if (!isUpdateFlag) {
					throw new Exception("更新huiyingdai_borrow_recover表数据失败~!");
				}
				// 更新成功后,将债转金额放到redis里面
				RedisUtils.set(String.valueOf(borrowCredit.getCreditNid()), String.valueOf(borrowCredit.getCreditCapital()));
			} else {
				throw new Exception("插入huiyingdai_borrow_credit表数据失败~!");
			}
		} else {
			return returnCreditNid;
		}
		return returnCreditNid;
	}

	/********************************************************************** 华丽的分割线 ***************************************************************/
	/**
	 * 
	 * 债转各项金额计算
	 *
	 *
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
					// 根据出借订单号检索已债转还款信息
					List<CreditRepay> creditRepayList = this.selectCreditRepayList(tenderNid);
					String bidNid = borrow.getBorrowNid();
					BorrowRepayPlanExample example = new BorrowRepayPlanExample();
					BorrowRepayPlanExample.Criteria cra = example.createCriteria();
					cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + 1);
					List<BorrowRepayPlan> borrowRepayPlans = this.borrowRepayPlanMapper.selectByExample(example);
					int lastDays = 0;
					if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
						try {
							String nowDate = GetDate.getDateTimeMyTimeInMillis(nowTime);
							String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRepayPlans.get(0).getRepayTime()));
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
					// creditInterest =
					// BeforeInterestAfterPrincipalUtils.getInterestCount(creditCapital,
					// yearRate, borrow.getBorrowPeriod(),
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
	 * 
	 * 获取债转垫付利息
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @param tenderNid
	 * @param creditDiscount
	 * @param nowTime
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectassignInterestForBigDecimal(java.lang.String,
	 *      java.lang.String, java.lang.String, int)
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
			// 获取债转的详细参数
			TenderToCreditAssignCustomize creditAssign = this.getInterestInfo(creditNid,assignCapital, userId);
			String assignPay = creditAssign.getAssignTotal();
			if (accountList.get(0).getBankBalance().compareTo(new BigDecimal(assignPay))<0) {
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
				if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
					BorrowRecover borrowRecover = borrowRecoverList.get(0);
					// 如果放款时间小于 20170703 重新计算已承接金额
					if(Integer.parseInt(borrowRecover.getAddtime())< 1499011200 && borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0){
						// 计算已承接的债权
						BigDecimal assignedCapital = this.getAssignCapital(borrowRecover.getNid());
						resultMap.put("tenderMoney", borrowRecover.getRecoverCapital().subtract(assignedCapital));
					}else{
						resultMap.put("tenderMoney", borrowRecover.getRecoverCapital());
					}
					// 获取借款数据
					BorrowExample borrowExample = new BorrowExample();
					BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
					borrowCra.andBorrowNidEqualTo(borrowCredit.getBidNid());
					List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
					// 计算折后价格
					BigDecimal assignPrice = new BigDecimal(assignCapital).setScale(2, BigDecimal.ROUND_DOWN).subtract(
							new BigDecimal(assignCapital).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_DOWN));
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
								assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
										sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
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
						// 等额本息和等额本金和先息后本
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
								creditAccount = new BigDecimal(assignCapital).add(interestAssign.multiply(new BigDecimal(borrow.getBorrowPeriod()-repayPeriod)));
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
						if (ifOldDate != null && ifOldDate <= borrowCredit.getAddTime()) {
							creditTenderLog.setCreditFee(assignPay.multiply(new BigDecimal(0.01)));
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

	/**
	 * 债转汇付交易成功后回调处理
	 * 
	 * @return
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
				BigDecimal creditIncome =  creditTenderLog.getAssignPay();// 总收入,本金+垫付利息
				//borrowCredit.setCreditIncome(borrowCredit.getCreditIncome().add(creditTenderLog.getAssignPay()));
				BigDecimal creditCapitalAssigned =  creditTenderLog.getAssignCapital();// 已认购本金
//				borrowCredit.setCreditCapitalAssigned(borrowCredit.getCreditCapitalAssigned().add(creditTenderLog.getAssignCapital()));// 已认购本金
				BigDecimal creditInterestAdvanceAssigned =  creditTenderLog.getAssignInterestAdvance();// 已垫付利息
//				borrowCredit.setCreditInterestAdvanceAssigned(borrowCredit.getCreditInterestAdvanceAssigned().add(creditTenderLog.getAssignInterestAdvance()));// 已垫付利息
				BigDecimal creditInterestAssigned =  creditTenderLog.getAssignInterest();// 已承接利息
//				borrowCredit.setCreditInterestAssigned(borrowCredit.getCreditInterestAssigned().add(creditTenderLog.getAssignInterest()));// 已承接利息
				BigDecimal creditFee =  creditTenderLog.getCreditFee();// 服务费
//				borrowCredit.setCreditFee(borrowCredit.getCreditFee().add(creditTenderLog.getCreditFee()));// 服务费
//				borrowCredit.setAssignTime(GetDate.getNowTime10());// 认购时间
//				borrowCredit.setAssignNum(borrowCredit.getAssignNum() + 1);// 出借次数
				// 完全承接的情况
				Integer creditStatus = borrowCredit.getCreditStatus();
				if ((borrowCredit.getCreditCapitalAssigned().add(creditCapitalAssigned)).compareTo(borrowCredit.getCreditCapital()) == 0) {
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
//					borrowCredit.setCreditStatus(2);
					creditStatus = 2;
				}
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("creditId", borrowCredit.getCreditId());
				paramMap.put("creditIncome", creditIncome==null?"0":creditIncome);
				paramMap.put("creditCapitalAssigned", creditCapitalAssigned==null?"0":creditCapitalAssigned);
				paramMap.put("creditInterestAdvanceAssigned", creditInterestAdvanceAssigned==null?"0":creditInterestAdvanceAssigned);
				paramMap.put("creditInterestAssigned", creditInterestAssigned==null?"0":creditInterestAssigned);
				paramMap.put("creditFee", creditFee==null?"0":creditFee);
				paramMap.put("creditStatus", creditStatus);
				try {
					borrowCreditCustomizeMapper.updateParam(paramMap);
				} catch (Exception e) {
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
				creditTender.setClient(creditTenderLog.getClient());// 客户端
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

				}else if(userInfoCustomize.getAttribute() == 2 || userInfoCustomize.getAttribute() ==3 ){
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

				}else if(userInfoCustomizeSeller.getAttribute() == 2 || userInfoCustomizeSeller.getAttribute() ==3 ){
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
						// 网站累计出借追加
						List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
						if (calculates != null && calculates.size() > 0) {
							CalculateInvestInterest calculateNew = new CalculateInvestInterest();
							calculateNew.setTenderSum(creditTenderLog.getAssignCapital());
							calculateNew.setId(calculates.get(0).getId());
							calculateNew.setCreateTime(GetDate.getDate(nowTime));
							this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
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
//						System.out.println("用户:" + userId + "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号：" + creditTenderLog.getAssignNid());
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
					this.sendCreditSuccessMessage(creditTender);
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
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_END);
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
		bean.setAccountId(borrowUserAccount.getAccount());// 融资人电子账号
		bean.setOrderId(orderId);// 订单号
		bean.setForAccountId(tenderAccountId);// 对手电子账号
		bean.setProductId(borrowRecover.getBorrowNid());// 标的号
		bean.setAuthCode(borrowRecover.getAuthCode());// 授权码
		bean.setLogUserId(String.valueOf(tenderUserId));// 出借人用户Id
		bean.setLogOrderId(orderId);
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		BankCallBean resultBean = BankCallUtils.callApiBg(bean);
		resultBean.convert();
		if (resultBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
			return true;
		} else {
			return false;
		}
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
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_DUANXINCHAOXIAN, CustomConstants.CHANNEL_TYPE_NORMAL);
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
	 * 
	 * 根据userId获取用户信息
	 * 
	 * @author liuyang
	 * @param userId
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#getUserInfoByUserId(java.lang.String)
	 */
	@Override
	public UserInfoCustomize getUserInfoByUserId(String userId) {
		UserInfoCustomize userInfo = this.userInfoCustomizeMapper.selectUserInfoByUserId(Integer.parseInt(userId));
		return userInfo;
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

	/**
	 * 
	 * 根据用户id,borrowNid,tenderNid判断用户是否已经发起债转
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @param tenderNid
	 * @param userId
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#selectBorrowCreditByBorrowNid(java.lang.String,
	 *      java.lang.String, java.lang.String)
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

		// 获取债转数据
		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
		borrowCreditCra.andCreditNidEqualTo(Integer.valueOf(creditNid));
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
				AppTenderToCreditAssignCustomize tenderToCreditAssign = new AppTenderToCreditAssignCustomize();
				BigDecimal yearRate = borrowCredit.getBidApr().divide(new BigDecimal("100"));
				// 计算折后价格
				BigDecimal assignPrice = new BigDecimal(assignCapital).setScale(2, BigDecimal.ROUND_DOWN).subtract(
						new BigDecimal(assignCapital).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal("100"), 18, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN));
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
							BigDecimal creditAccount = DuePrincipalAndInterestUtils.getDayPrincipalInterestAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait, sellerInterestWait, yearRate,
									borrow.getBorrowPeriod());
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvanceAfterCredit(new BigDecimal(assignCapital), sellerCapitalWait,
									sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							BigDecimal assignPay = assignPrice.add(assignInterestAdvance);
							String assignPayText = DF_FOR_VIEW.format(assignPay) + "=" + DF_FOR_VIEW.format(new BigDecimal(assignCapital)) + "✕(1-"
									+ DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance);
							// 预计收益 承接人债转本息—实付金额
							BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
							tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
							tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
							tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
							tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital()));
							tenderToCreditAssign.setCreditCapitalNum(borrowCredit.getCreditCapital());
							tenderToCreditAssign.setAssignCapital(assignCapital);
							tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()));
							tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice));
							tenderToCreditAssign.setAssignPriceNum(assignPrice);
							tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay));
							tenderToCreditAssign.setAssignPayNum(assignPay);
							tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest));
							tenderToCreditAssign.setAssignInterestNum(assignInterest);
							tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance));
							tenderToCreditAssign.setAssignInterestAdvanceNum(assignInterestAdvance);
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
							String assignPayText = DF_FOR_VIEW.format(assignPay) + "=" + DF_FOR_VIEW.format(new BigDecimal(assignCapital)) + "✕(1-"
									+ DF_FOR_VIEW.format(borrowCredit.getCreditDiscount()) + "%)+" + DF_FOR_VIEW.format(assignInterestAdvance);
							// 预计收益 承接人债转本息—实付金额
							BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
							tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
							tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
							tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
							tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setCreditCapitalNum(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN));
							tenderToCreditAssign.setAssignCapital(assignCapital);
							tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPriceNum(assignPrice.setScale(2, BigDecimal.ROUND_DOWN));
							tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignPayNum(assignPay.setScale(2, BigDecimal.ROUND_DOWN));
							tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterestNum(assignInterest.setScale(2, BigDecimal.ROUND_DOWN));
							tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
							tenderToCreditAssign.setAssignInterestAdvanceNum(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN));
							tenderToCreditAssign.setAssignPayText(assignPayText);
						}
					}
					// 等额本息和等额本金和先息后本
					if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {

						String bidNid = borrow.getBorrowNid();
						// 获取当前时间
						BorrowRecover borrowRecover = borrowRecoverList.get(0);
						BorrowRepayPlanExample example1 = new BorrowRepayPlanExample();
						BorrowRepayPlanExample.Criteria cra1 = example1.createCriteria();
						cra1.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(borrow.getBorrowPeriod() - borrowRecover.getRecoverPeriod() + 1);
						List<BorrowRepayPlan> borrowRepayPlanList = this.borrowRepayPlanMapper.selectByExample(example1);
						int lastDays = 0;
						if (borrowRepayPlanList != null && borrowRepayPlanList.size() > 0) {
							try {
								String nowDate = GetDate.getDateTimeMyTimeInMillis(borrowCredit.getAddTime());
								String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRepayPlanList.get(0).getRepayTime()));
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
						String assignPayText = DF_FOR_VIEW.format(assignPay) + "=" + DF_FOR_VIEW.format(new BigDecimal(assignCapital)) + "✕(1-" + DF_FOR_VIEW.format(borrowCredit.getCreditDiscount())
								+ "%)+" + DF_FOR_VIEW.format(assignInterestAdvance);
						// 预计收益 承接人债转本息—实付金额
						BigDecimal assignInterest = creditAccount.subtract(assignPay);// 计算出借收益
						tenderToCreditAssign.setBorrowNid(borrow.getBorrowNid());
						tenderToCreditAssign.setCreditNid(String.valueOf(borrowCredit.getCreditNid()));
						tenderToCreditAssign.setTenderNid(borrowCredit.getTenderNid());
						tenderToCreditAssign.setCreditCapital(DF_FOR_VIEW.format(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setCreditCapitalNum(borrowCredit.getCreditCapital().setScale(2, BigDecimal.ROUND_DOWN));
						tenderToCreditAssign.setAssignCapital(assignCapital);
						tenderToCreditAssign.setCreditDiscount(DF_FOR_VIEW.format(borrowCredit.getCreditDiscount().setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPrice(DF_FOR_VIEW.format(assignPrice.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPriceNum(assignPrice.setScale(2, BigDecimal.ROUND_DOWN));
						tenderToCreditAssign.setAssignPay(DF_FOR_VIEW.format(assignPay.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignPayNum(assignPay.setScale(2, BigDecimal.ROUND_DOWN));
						tenderToCreditAssign.setAssignInterest(DF_FOR_VIEW.format(assignInterest.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignInterestNum(assignInterest.setScale(2, BigDecimal.ROUND_DOWN));
						tenderToCreditAssign.setAssignInterestAdvance(DF_FOR_VIEW.format(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN)));
						tenderToCreditAssign.setAssignInterestAdvanceNum(assignInterestAdvance.setScale(2, BigDecimal.ROUND_DOWN));
						tenderToCreditAssign.setAssignPayText(assignPayText);
					}
					resultMap.put("tenderToCreditAssign", tenderToCreditAssign);
					if (new BigDecimal(tenderToCreditAssign.getAssignPay().replaceAll(",", "")).compareTo(accountList.get(0).getBankBalance()) < 0) {
						resultMap.put("msg", "垫付利息可用余额不足，请充值");
						resultMap.put("resultType", "1");
					} else {
						resultMap.put("msg", "认购债权确认");
						resultMap.put("resultType", "0");
					}
				}
			}
			// } else {
			// // 债转人不能购买自己的债转
			// resultMap.put("msg", "不可以承接自己出让的债权。");
			// }
		}
		return resultMap;
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
			if (user.getStatus() == 1) {// 0启用，1禁用
			    // 判断用户是否禁用
				return jsonMessage("该用户已被禁用", "1");
			}
			BorrowCredit borrowCredit = this.getBorrowCredit(creditNid);
			BankOpenAccount accountChinapnrTender = this.getBankOpenAccount(Integer.parseInt(userId));
			// 用户未在平台开户
			if (accountChinapnrTender == null) {
				return jsonMessage("用户开户信息不存在", "1");
			}
			// 判断借款人开户信息是否存在
			if (accountChinapnrTender.getAccount() == null) {
				return jsonMessage("用户汇付客户号不存在", "1");
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
			if (accountChinapnrCreditUser.getAccount() == null) {
				return jsonMessage("出让人汇付客户号不存在", "1");
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
			BigDecimal assignPay = BigDecimal.ZERO;
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
				BigDecimal assignPrice = new BigDecimal(account).setScale(2, BigDecimal.ROUND_DOWN).subtract(
						new BigDecimal(account).multiply(borrowCredit.getCreditDiscount().divide(new BigDecimal("100"), 18, BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN));
				if (borrowList != null && borrowList.size() > 0) {
					Borrow borrow = borrowList.get(0);
					// 获取标的借款人
					Integer borrowUserId = borrow.getUserId();
					if (borrowUserId == Integer.parseInt(userId)) {
						return jsonMessage("承接人不能为借款人", "1");
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
						// 剩余天数
						int lastDays = borrowCredit.getCreditTerm();
						if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {// 按天
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷360*融资期限-债权本金*年化收益÷360*剩余期限
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getDayAssignInterestAdvanceAfterCredit(new BigDecimal(account), sellerCapitalWait,
									sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
							// 实付金额
							// 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						} else {// 按月
							// 垫付利息
							// 垫息总额=债权本金*年化收益÷12*融资期限-债权本金*年化收益÷360*剩余天数
							BigDecimal assignInterestAdvance = DuePrincipalAndInterestUtils.getMonthAssignInterestAdvanceAfterCredit(new BigDecimal(account), sellerCapitalWait,
									sellerInterestAdvanceWait, yearRate, borrow.getBorrowPeriod(), new BigDecimal(lastDays));
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
						if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
							try {
								String nowDate = GetDate.getDateTimeMyTimeInMillis(borrowCredit.getAddTime());
								String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(borrowRepayPlans.get(0).getRepayTime()));
								lastDays = GetDate.daysBetween(nowDate, recoverDate);
							} catch (NumberFormatException | ParseException e) {
								e.printStackTrace();
							}
						}
						// 已还多少期
						int repayPeriod = borrowCredit.getRecoverPeriod();

						// 应还总本息
						BigDecimal creditAccount = BigDecimal.ZERO;
						// 垫付利息
						// 垫息总额=出借人认购本金/出让人转让本金*出让人本期利息）-（债权本金*年化收益÷360*本期剩余天数
						BigDecimal assignInterestAdvance = BigDecimal.ZERO;
						if (new BigDecimal(account).compareTo(sellerCapitalWait) == 0) {
							// 最后一笔承接
							creditAccount = sellerCapitalWait.add(sellerInterestWait);
							// 剩余垫付利息
							assignInterestAdvance = sellerInterestAdvanceWait;
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						} else {
							// 应还总本息
							creditAccount = BeforeInterestAfterPrincipalUtils.getPrincipalInterestCount(new BigDecimal(account), yearRate, borrow.getBorrowPeriod(), borrow.getBorrowPeriod());
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
							assignInterestAdvance = BeforeInterestAfterPrincipalUtils.getAssignInterestAdvance(new BigDecimal(account), borrowCredit.getCreditCapital(), yearRate, interest,
									new BigDecimal(lastDays + ""));
							// 实付金额 承接本金*（1-折价率）+应垫付利息
							assignPay = assignPrice.add(assignInterestAdvance);
						}
					}
				}
			}
			if (balance.compareTo(assignPay) < 0) {
				return jsonMessage("可用金额不足", "1");
			}
			// 将出借金额转化为BigDecimal
			BigDecimal accountBigDecimal = new BigDecimal(account);
			// 债转剩余金额
			BigDecimal money = borrowCredit.getCreditCapital().subtract(borrowCredit.getCreditCapitalAssigned());
			if (money == BigDecimal.ZERO) {
				return jsonMessage("您来晚了，下次再来抢吧", "1");
			} else {
				if (accountBigDecimal.compareTo(money) > 0) {
					//return jsonMessage("项目太抢手了！剩余可投金额只有" + money + "元", "1");
					return jsonMessage("出借金额不能大于项目剩余", "1");
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

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param userId
	 * @return
	 * @author b
	 */

	@Override
	public Users getUsers(Integer userId) {
		if (userId == null) {
			return null;
		}
		// 查找用户
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria criteria2 = usersExample.createCriteria();
		criteria2.andUserIdEqualTo(userId);
		List<Users> userList = usersMapper.selectByExample(usersExample);
		Users users = null;
		if (userList != null && !userList.isEmpty()) {
			users = userList.get(0);

		}
		return users;

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
		}
		return borrowCredit;
	}

	/**
	 * 
	 * 获取借款信息
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @return
	 * @see com.hyjf.web.tender.credit.TenderCreditService#searchBorrowList(java.lang.String)
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
	 * 分期标的还款计划
	 * 
	 * @author liuyang
	 * @param borrowNid
	 * @param borrowPeriod
	 * @return
	 * @see com.hyjf.app.user.credit.AppTenderCreditService#searchBorrowRepayPlanList(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<BorrowRepayPlan> searchBorrowRepayPlanList(String borrowNid, Integer borrowPeriod) {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid).andRepayPeriodEqualTo(borrowPeriod);
		return this.borrowRepayPlanMapper.selectByExample(example);
	}

	/**
	 * 根据订单号,用户ID查询用户出借债券记录
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
		}
		return null;
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
	public CreditTenderLog selectCreditTenderLogByLogOrderId(String logOrderId) {
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

	@Override
	public TenderToCreditAssignCustomize getInterestInfo(String creditNid, String assignCapital, int userId) {
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
					return tenderToCreditAssign;
				}
			}
		}
		return null;
	}

	/**
	 * 判断用户所在渠道是否允许债转
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

	@Override
	public Borrow getBorrowByBorrowNid(String bidNid) {
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria criteria = borrowExample.createCriteria();
		criteria.andBorrowNidEqualTo(bidNid);
		List<Borrow> borrows = borrowMapper.selectByExample(borrowExample);
		if (borrows != null){
			return borrows.get(0);
		}else {
			return null;
		}
	}

	/**
	 * 获取出借信息
	 * @param bidNid
	 * @param userId
	 * @return
	 */
	@Override
	public BorrowTender getInvestRecord(String bidNid, Integer userId) {
		BorrowTenderExample tenderExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = tenderExample.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andBorrowNidEqualTo(bidNid);
		List<BorrowTender> borrowTenders = borrowTenderMapper.selectByExample(tenderExample);
		return borrowTenders.get(0);
	}

	/**
	 * 获取优惠券出借信息
	 * @param bidNid
	 * @param userId
	 * @return
	 */
	@Override
	public BorrowTenderCpn getCpnInvest(String bidNid, Integer userId) {
		BorrowTenderCpnExample example = new BorrowTenderCpnExample();
		BorrowTenderCpnExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andBorrowNidEqualTo(bidNid);
		return borrowTenderCpnMapper.selectByExample(example).get(0);
	}

	@Override
	public BorrowCredit getBorrowCreditByBorrowNid(String borrowNid) {
		BorrowCredit borrowCredit = null;
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria cra = example.createCriteria();
		cra.andBidNidEqualTo(borrowNid);
		List<BorrowCredit> list = this.borrowCreditMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			borrowCredit = list.get(0);
		}
		return borrowCredit;
	}

	/**
	 * 根据出借订单号查询已承接金额
	 * @param tenderNid
	 * @return
	 */
	private BigDecimal getAssignCapital(String tenderNid){
		BigDecimal assignCapital = BigDecimal.ZERO;
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria cra = example.createCriteria();
		cra.andCreditTenderNidEqualTo(tenderNid);
		cra.andAddTimeLessThanOrEqualTo(String.valueOf(1499011200));
		List<CreditTender> list = this.creditTenderMapper.selectByExample(example);
		if(list!=null && list.size()>0){
			for (CreditTender creditTender : list) {
				assignCapital = assignCapital.add(creditTender.getAssignCapital());
			}
		}
		return assignCapital;
	}
}
