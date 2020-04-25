package com.hyjf.app.user.myasset;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.AccountAccurate;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.customize.web.WebPandectBorrowRecoverCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectCreditTenderCustomize;

public interface MyAssetService extends BaseService {

//	/** 根据用户id获取用户信息*/
//	public UsersInfo queryUserInfoById(Integer userId);
	

	/**
	 * 债转统计
	 * @param userId
	 * @return
	 */
	public WebPandectCreditTenderCustomize queryCreditInfo(Integer userId);
	
	/**债转信息
	 * 去掉已债转（r.recover_status=1）; 去掉待收已债转（r.recover_status=0）
	 * @param userId
	 * @param recoverStatus
	 * @return
	 */
	public WebPandectBorrowRecoverCustomize queryRecoverInfo(Integer userId,Integer recoverStatus);       
	
	/**
	 * 查询待收、已回收、累计、冻结等金额
	 * @param userId
	 * @return
	 */
	public AssetBean queryAsset(Integer userId);
	
	/**
	 * 获取汇天利 购买明细表可赎回金额总额
	 * @param userId
	 * @return
	 */
	public BigDecimal queryHtlSumRestAmount(Integer userId);
	/**
	 * 获取汇天利 总收益
	 * @param userId
	 * @return
	 */
	public BigDecimal queryHtlSumInterest(Integer userId);
	
	/**
	 * 获取汇天利配置
	 * @return
	 */
	public Product queryHtlConfig();
	
	/**
	 * 根据userid获取 huiyingdai_account_accurate 表数据
	 * @param userId
	 * @return
	 */
	public List<AccountAccurate> queryAccountAccurateByUserid(Integer userId);
	/**
	 * 根据userid维护 huiyingdai_account_accurate 表数据
	 * @param userId
	 * @return
	 */
	public int updateAccountAccurateByUserid(AccountAccurate aa,Integer userId);
	/**
	 * 录入 huiyingdai_account_accurate 表数据
	 * @param aa
	 * @return
	 */
	public int insertAccountAccurate(AccountAccurate aa);
	
    /**
     * 获取某用户优惠券累计收益总和
     * @param userId
     * @return
     */
	public String queryCouponInterestTotal(Integer userId);
	/**
     * 获取某用户优惠券待收收益总和
     * @param userId
     * @return
     */
	public String queryCouponInterestWait(Integer userId);
}
