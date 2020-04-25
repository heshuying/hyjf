package com.hyjf.app.user.myasset;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AccountAccurate;
import com.hyjf.mybatis.model.auto.AccountAccurateExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductExample;
import com.hyjf.mybatis.model.customize.web.WebPandectBorrowRecoverCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectCreditTenderCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectRecoverMoneyCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectWaitMoneyCustomize;

@Service
public class MyAssetServiceImpl extends BaseServiceImpl implements MyAssetService {

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
		WebPandectRecoverMoneyCustomize prRtb = webPandectCustomizeMapper.queryRecoverMoneyForRtb(userId);
		if (pr != null) {
			// 已回收的本金
			assetBean.setRecoverCapital(pr.getRecoverCapital() == null ? new BigDecimal(0) : pr.getRecoverCapital());
			// 已回收的利息
			if (prRtb != null && prRtb.getRecoverInterest() != null) {
				assetBean.setRecoverInterest(pr.getRecoverInterest() == null ? new BigDecimal(0) : pr
						.getRecoverInterest().add(prRtb.getRecoverInterest()));
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
	 * 
	 * 获取某用户优惠券待收收益总和
	 */
	@Override
	public String queryCouponInterestTotal(Integer userId) {
		return couponRecoverCustomizeMapper.selectCouponReceivedInterestTotal(userId);
	}

	@Override
	public String queryCouponInterestWait(Integer userId) {
		return couponRecoverCustomizeMapper.selectCouponInterestTotal(userId);
	}

}
