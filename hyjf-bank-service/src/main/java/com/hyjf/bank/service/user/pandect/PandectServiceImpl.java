package com.hyjf.bank.service.user.pandect;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AccountAccurate;
import com.hyjf.mybatis.model.auto.AccountAccurateExample;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecordExample;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecordsExample;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductExample;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliationExample;
import com.hyjf.mybatis.model.customize.BorrowRecoverLatestCustomize;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectBorrowRecoverCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectCreditTenderCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectRecoverMoneyCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectWaitMoneyCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class PandectServiceImpl extends BaseServiceImpl implements PandectService {

	/**
	 * 债转统计
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public WebPandectCreditTenderCustomize queryCreditInfo(Integer userId) {
		WebPandectCreditTenderCustomize pcc = webPandectCustomizeMapper.queryCreditInfo(userId);
		return pcc;
	}

	/**
	 * 债转信息 去掉已债转（r.recover_status=1）; 去掉待收已债转（r.recover_status=0）
	 * 
	 * @param userId
	 * @param recoverStatus
	 * @return
	 */
	@Override
	public WebPandectBorrowRecoverCustomize queryRecoverInfo(Integer userId, Integer recoverStatus) {
		WebPandectBorrowRecoverCustomize pbr = webPandectCustomizeMapper.queryRecoverInfo(userId, recoverStatus);
		return pbr;
	}

	/**
	 * 查询待收、已回收、累计、冻结等金额
	 * 
	 * @param userId
	 * @return
	 */
	public AssetBean queryAsset(Integer userId) {
		AssetBean assetBean = new AssetBean();
		// 投标冻结金额
		BigDecimal frostCapital = webPandectCustomizeMapper.queryFrostCapital(userId);
		assetBean.setFrostCapital(frostCapital == null ? new BigDecimal(0) : frostCapital);

		WebPandectWaitMoneyCustomize pw = webPandectCustomizeMapper.queryWaitMoney(userId);
		if (pw != null) {
			// 待收本金
			assetBean.setWaitCapital(pw.getWaitCapital() == null ? new BigDecimal(0) : pw.getWaitCapital());
			WebPandectWaitMoneyCustomize pwRtb = webPandectCustomizeMapper.queryWaitMoneyForRtb(userId);
			if (pwRtb != null && pwRtb.getRecoverInterest() != null) {
				// 待收利息
				assetBean.setWaitInterest(pw.getRecoverInterest() == null ? new BigDecimal(0) : pw.getRecoverInterest()
						.add(pwRtb.getRecoverInterest()));
			} else {
				// 待收利息
				assetBean
						.setWaitInterest(pw.getRecoverInterest() == null ? new BigDecimal(0) : pw.getRecoverInterest());
			}

		}

		WebPandectRecoverMoneyCustomize pr = webPandectCustomizeMapper.queryRecoverMoney(userId);
		// 融通宝累计收益
		WebPandectRecoverMoneyCustomize repayRtb = webPandectCustomizeMapper.queryRecoverMoneyForRtb(userId);
		if (pr != null) {
			// 已回收的本金
			assetBean.setRecoverCapital(pr.getRecoverCapital() == null ? new BigDecimal(0) : pr.getRecoverCapital());
			// 已回收的利息
			if (repayRtb != null) {
				assetBean
						.setRecoverInterest(pr.getRecoverInterest().add(repayRtb.getRecoverInterest()) == null ? new BigDecimal(
								0) : pr.getRecoverInterest().add(repayRtb.getRecoverInterest()));
			} else {
				assetBean.setRecoverInterest(pr.getRecoverInterest() == null ? new BigDecimal(0) : pr
						.getRecoverInterest());
			}
		}

		// 取得还款明细
		BorrowRecoverExample recoverExample = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria recovercriteria = recoverExample.createCriteria();
		recovercriteria.andUserIdEqualTo(userId);
		recovercriteria.andRecoverStatusEqualTo(0); // 0是未还（包含分期未还完），1是已还
		recoverExample.setOrderByClause(" recover_time asc ");
		recoverExample.setLimitStart(0);
		recoverExample.setLimitEnd(1);
		List<BorrowRecover> listRecover = this.borrowRecoverMapper.selectByExample(recoverExample);
		if (listRecover != null && listRecover.size() > 0) {
			BorrowRecover recover = listRecover.get(0);
			// 取得还款详情
			BorrowRepayExample repayExample = new BorrowRepayExample();
			BorrowRepayExample.Criteria repayCriteria = repayExample.createCriteria();
			repayCriteria.andBorrowNidEqualTo(recover.getBorrowNid());
			repayCriteria.andRepayStatusEqualTo(0); // 1 成功
			repayExample.setOrderByClause(" repay_status asc, repay_time asc, id desc  ");
			repayExample.setLimitStart(0);
			repayExample.setLimitEnd(1);
			List<BorrowRepay> listRepay = this.borrowRepayMapper.selectByExample(repayExample);

			if (listRepay != null && listRepay.size() > 0) {
				BorrowRepay borrowRepay = listRepay.get(0);
				// 估计还款时间
				assetBean.setRecoverTime(borrowRepay.getRepayTime());
				// 最近待收金额
				BigDecimal recoverAccount = webPandectCustomizeMapper.queryRecoverAccount(userId,
						borrowRepay.getBorrowNid());

				assetBean.setRecoverAccount(recoverAccount);
			} else {
				// 估计还款时间
				assetBean.setRecoverTime("--");
				// 最近待收金额
				assetBean.setRecoverAccount(new BigDecimal(0));
			}
		} else {
			// 估计还款时间
			assetBean.setRecoverTime("--");
			// 最近待收金额
			assetBean.setRecoverAccount(new BigDecimal(0));
		}

		// 累计出借金额
		BigDecimal accountAll = webPandectCustomizeMapper.queryInvestTotal(userId);
		assetBean.setInvestTotal(accountAll == null ? new BigDecimal(0) : accountAll);

		return assetBean;
	}

	/**
	 * 获取汇天利 购买明细表可赎回金额总额
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal queryHtlSumRestAmount(Integer userId) {
		return webPandectCustomizeMapper.queryHtlSumRestAmount(userId);
	}

	/**
	 * 获取汇天利 总收益
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal queryHtlSumInterest(Integer userId) {
		return webPandectCustomizeMapper.queryHtlSumInterest(userId);
	}

	/**
	 * 获取汇天利配置
	 * 
	 * @return
	 */
	public Product queryHtlConfig() {
		Product product = productMapper.selectByExample(new ProductExample()).get(0);
		return product;
	}

	/**
	 * 根据userid获取 huiyingdai_account_accurate 表数据
	 * 
	 * @param userId
	 * @return
	 */
	public List<AccountAccurate> queryAccountAccurateByUserid(Integer userId) {
		AccountAccurateExample example = new AccountAccurateExample();
		AccountAccurateExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);

		List<AccountAccurate> list = accountAccurateMapper.selectByExample(example);
		return list;
	}

	/**
	 * 根据userid维护 huiyingdai_account_accurate 表数据
	 * 
	 * @param userId
	 * @return
	 */
	public int updateAccountAccurateByUserid(AccountAccurate aa, Integer userId) {
		AccountAccurateExample example = new AccountAccurateExample();
		AccountAccurateExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);

		return accountAccurateMapper.updateByExampleSelective(aa, example);
	}

	/**
	 * 录入 huiyingdai_account_accurate 表数据
	 * 
	 * @param aa
	 * @return
	 */
	public int insertAccountAccurate(AccountAccurate aa) {
		return accountAccurateMapper.insertSelective(aa);
	}

	/**
	 * 企业用户是否已开户
	 * 
	 * @param userId
	 * @return 0 未开户 1已开户
	 * @author Michael
	 */

	@Override
	public int isCompAccount(Integer userId) {
		CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
		CorpOpenAccountRecordExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andStatusEqualTo(6);// 已开户成功
		return corpOpenAccountRecordMapper.countByExample(example);
	}

	/**
	 * 企业用户是否已绑定用户
	 * 
	 * @param userId
	 * @return -1 未绑定，0初始，1成功，2失败
	 * @author Michael
	 */

	@Override
	public int isCompBindUser(Integer userId) {
		int result = -1;
		DirectionalTransferAssociatedRecordsExample example = new DirectionalTransferAssociatedRecordsExample();
		DirectionalTransferAssociatedRecordsExample.Criteria cra = example.createCriteria();
		cra.andTurnOutUserIdEqualTo(userId);
		List<DirectionalTransferAssociatedRecords> dList = directionalTransferAssociatedRecordsMapper
				.selectByExample(example);
		if (dList != null && dList.size() > 0) {
			result = dList.get(0).getAssociatedState();
		}
		return result;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @return
	 * @author Michael
	 */

	@Override
	public Map<String, String> countRechargeFee(Integer userId) {
		Map<String, String> map = new HashMap<String, String>();
		RechargeFeeReconciliationExample example = new RechargeFeeReconciliationExample();
		RechargeFeeReconciliationExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(0);
		cra.andUserIdEqualTo(userId);
		List<RechargeFeeReconciliation> list = this.rechargeFeeReconciliationMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			BigDecimal amount = BigDecimal.ZERO;
			for (int i = 0; i < list.size(); i++) {
				amount = amount.add(list.get(i).getRechargeFee());
			}
			map.put("rechargeFeeCount", String.valueOf(list.size()));
			map.put("rechargeFeeAmount", amount.toString());
		} else {
			map.put("rechargeFeeCount", "0");
			map.put("rechargeFeeAmount", "0");
		}
		return map;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param userId
	 * @return
	 * @author Michael
	 */

	@Override
	public Map<String, String> countRechargeAmountWeek(Integer userId) {
		Map<String, String> map = new HashMap<String, String>();
		String startDate = this.getFirstDayOfWeek();
		String endDate = GetDate.getDate("yyyy-MM-dd");
		RechargeCustomize rechargeCustomize = new RechargeCustomize();
		rechargeCustomize.setUserId(userId);
		rechargeCustomize.setStartDate(startDate);
		rechargeCustomize.setEndDate(endDate);
		rechargeCustomize.setStatusSearch("1");
		List<RechargeCustomize> list = this.rechargeCustomizeMapper.queryRechargeList(rechargeCustomize);
		if (list != null && list.size() > 0) {
			BigDecimal dianfuAmount = BigDecimal.ZERO;
			BigDecimal rechargeAmount = BigDecimal.ZERO;
			for (int i = 0; i < list.size(); i++) {
				dianfuAmount = dianfuAmount.add(list.get(i).getDianfuFee());
				rechargeAmount = rechargeAmount.add(list.get(i).getMoney());
			}
			map.put("rechargeAmount", String.valueOf(rechargeAmount));
			map.put("dianfuAmount", String.valueOf(dianfuAmount));
		} else {
			map.put("rechargeAmount", "0");
			map.put("dianfuAmount", "0");
		}
		return map;
	}

	/**
	 * 
	 * 查询优惠券已得收益
	 * 
	 * @author hsy
	 * @param userId
	 * @return
	 */
	@Override
	public String queryCouponInterestTotal(Integer userId) {
		return couponRecoverCustomizeMapper.selectCouponReceivedInterestTotal(userId);
	}

	/**
	 * 
	 * 查询优惠券待收收益
	 * 
	 * @author hsy
	 * @param userId
	 * @return
	 */
	@Override
	public String queryCouponInterestTotalWait(Integer userId) {
		return couponRecoverCustomizeMapper.selectCouponInterestTotal(userId);
	}

	public String getFirstDayOfWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		// 今天是一周中的第几天
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		// 计算一周开始的日期
		c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(c.getTime());
	}

	/**
	 * 
	 * @method: checkAppointmentStatus
	 * @description: 查看预约授权状态
	 * @return
	 * @param tenderUsrcustid
	 * @return
	 * @return: Map<String, Object>
	 * @throws Exception
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:53:08
	 */
	@Override
	public Map<String, Object> checkAppointmentStatus(Integer usrId, String appointment) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(usrId);
		if (outCust == null) {
			map.put("appointmentFlag", false);
			map.put("isError", "1");
			return map;
		}
		if (appointment.equals("0")) {
			// 此笔加入是否已经完成 0出借中 1出借完成 2还款中 3还款完成
			DebtPlanAccedeExample example = new DebtPlanAccedeExample();
			DebtPlanAccedeExample.Criteria cri = example.createCriteria();
			cri.andUserIdEqualTo(usrId);
			cri.andStatusNotEqualTo(5);
			int pcount = debtPlanAccedeMapper.countByExample(example);
			if (pcount > 0) {
				map.put("appointmentFlag", false);
				map.put("isError", "2");
				return map;
			}
		}
		// 调用预约授权查询接口
		ChinapnrBean queryTransStatBean = queryAppointmentStatus(outCust.getChinapnrUsrcustid());
		if (queryTransStatBean == null && appointment.equals("0")) {
			map.put("appointmentFlag", false);
			map.put("isError", "0");
			return map;
		} else if (queryTransStatBean == null && appointment.equals("1")) {
			map.put("appointmentFlag", true);
			map.put("isError", "1");
			return map;
		} else {
			String queryRespCode = queryTransStatBean.getRespCode();
			System.out.print("调用自动出借授权查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)
					&& !ChinaPnrConstant.RESPCODE_NOTEXIST.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", "调用预约授权查询接口失败。" + message + ",[返回码："
						+ queryRespCode + "]", null);
				map.put("appointmentFlag", false);
				map.put("isError", "1");
				return map;
			} else {
				// 汇付预约状态
				String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
				if ("N".equals(transStat) && appointment.equals("0")) {
					map.put("appointmentFlag", true);
					map.put("isError", "0");
					return map;
				} else if ("C".equals(transStat) && appointment.equals("1")) {
					map.put("appointmentFlag", true);
					map.put("isError", "0");
					return map;
				} else if (ChinaPnrConstant.RESPCODE_NOTEXIST.equals(queryRespCode) && appointment.equals("1")) {
					map.put("appointmentFlag", true);
					map.put("isError", "0");
					return map;
				} else {
					map.put("appointmentFlag", false);
					map.put("isError", "0");
					return map;
				}
			}
		}
	}

	/**
	 * 更新预约状态
	 */
	@Override
	public boolean updateUserAuthStatus(String tenderPlanType, String appointment, String userId) {
		int authType = 0;
		if (tenderPlanType != null && tenderPlanType.equals("P")) {
			authType = 1;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("authType", authType);
		params.put("appointment", Integer.parseInt(appointment));
		params.put("userId", userId);
		int AuthFlag = webPandectCustomizeMapper.updateUserAuthStatus(params);
		int AppointmentFlag = webPandectCustomizeMapper.insertAppointmentAuthLog(params);
		if (AuthFlag > 0 && AppointmentFlag > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * 获取最近还款列表
	 * @author hsy
	 * @param paraMap
	 * @return
	 */
	@Override
    public List<BorrowRecoverLatestCustomize> selectLatestBorrowRecoverList(Map<String, Object> paraMap){
	    return borrowRecoverCustomizeMapper.selectLatestBorrowRecoverList(paraMap);
	}

	/**
	 * 调用预约授权查询接口
	 *
	 * @return
	 */
	private ChinapnrBean queryAppointmentStatus(Long Usrcustid) {

		// 调用预约授权查询接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_TENDER_PLAN); // 消息类型(必须)
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));// 商户号(必须)
		bean.setUsrCustId(Usrcustid + ""); // 订单日期(必须)
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			System.out.println("调用预约授权查询接口失败![参数：" + bean.getAllParams() + "]");
			return null;
		}
		return chinapnrBean;
	}
	
	

}
