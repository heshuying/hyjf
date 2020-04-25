package com.hyjf.batch.exception.chongzhensearch;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Accountwithdraw;

/**
* 江西银行提现掉单异常处理
* @author LiuBin
* @date 2017年8月1日 上午9:59:24
*
*/ 
public interface ChongZhenSearchService extends BaseService {

	/**
	 * 检索提现中的提现记录
	 * 
	 * @return
	 */
	public List<Accountwithdraw> selectBankWithdrawList();

	/**
	 * 更新处理中的提现记录
	 * 
	 * @param accountWithdraw
	 */
	public void updateWithdraw(Accountwithdraw accountWithdraw);

}
