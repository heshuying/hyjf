package com.hyjf.bank.service.user.pandect;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.AccountAccurate;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.customize.BorrowRecoverLatestCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectBorrowRecoverCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectCreditTenderCustomize;

public interface PandectService  extends BaseService {

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
     * 
     * 查询优惠券已得收益
     * @author hsy
     * @param userId
     * @return
     */
    public String queryCouponInterestTotal(Integer userId);

    /**
     * 
     * 查询优惠券待收收益
     * @author hsy
     * @param userId
     * @return
     */
    String queryCouponInterestTotalWait(Integer userId);
	
     /**
	 * 企业用户是否已开户
	 * @param userId
	 * @return 0 未开户 1已开户
	 */
	public int isCompAccount(Integer userId);	
     /**
	 * 企业用户是否已绑定用户
	 * @param userId
	 * @return  -1 未绑定，0初始，1成功，2失败 
	 */
	public int isCompBindUser(Integer userId);

    /**
	 * 待付款充值手续费记录数/金额
	 * @return
	 */
	public Map<String,String> countRechargeFee(Integer userId);    /**
	 * 本周充值金额、手续费 
	 * @return
	 */
	public Map<String,String> countRechargeAmountWeek(Integer userId);
/**
 * 
 * @param usrId 
 * @param appointment 
 * @method: checkAppointmentStatus
 * @description: 			查看预约授权状态
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月26日 下午1:53:08
 */
	public Map<String, Object> checkAppointmentStatus(Integer usrId, String appointment) ;
/**
 * 
 * @method: updateUserAuthStatus
 * @description: 更新预约授权数据			
 *  @param tenderPlanType  P  部分授权  W 完全授权
 *  @param appointment 
 * @param userId 
 * @return: void
* @mender: zhouxiaoshuai
 * @date:   2016年7月26日 下午3:24:55
 */
public boolean updateUserAuthStatus(String tenderPlanType, String appointment, String userId);

List<BorrowRecoverLatestCustomize> selectLatestBorrowRecoverList(Map<String, Object> paraMap);


	}


