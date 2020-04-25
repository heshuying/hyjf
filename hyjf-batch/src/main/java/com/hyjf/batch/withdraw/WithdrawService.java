package com.hyjf.batch.withdraw;

import java.util.List;

import com.hyjf.mybatis.model.auto.Accountwithdraw;

/**
 * 提现相关接口
 * @author 孙亮
 * @since 2016年4月5日13:57:48
 *
 */
public interface WithdrawService {

	/**
	 * 查询处理中的提现列表
	 * @return
	 */
    public List<Accountwithdraw> queryNoResultWithdrawList(String startDateTime); 
    
    /**
     * 更新处理中的数据
     * @param accountWithdraw
     */
    public void handleWithdrawStatus(Accountwithdraw accountWithdraw);
}
