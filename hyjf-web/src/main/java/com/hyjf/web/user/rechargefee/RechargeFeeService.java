/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.rechargefee;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.RechargeFeeReconciliation;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.web.BaseService;

public interface RechargeFeeService extends BaseService {

	/**
	 * 查询用户充值手续费列表
	 * @param rechargeFee
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<RechargeFeeReconciliation> queryRechargeFeeList(RechargeFeeBean rechargeFee, int limitStart, int limitEnd);

	/**
	 * 统计用户充值手续费列表数据总数
	 * @param form
	 * @return
	 */
	int countFeeRecordTotal(RechargeFeeBean form);

	
	/**
	 * 校验参数
	 * @return
	 */
	RechargeFeeReconciliation checkParam(String recordId, JSONObject ret);
	
	/**
	 * 更新数据  返回true or false
	 * @param rechargeFeeReconciliation
	 * @return
	 */
	boolean updateRechargeFee(RechargeFeeReconciliation rechargeFeeReconciliation,String orderId);
	
	/**
	 * 校验数据是否重复提交
	 * @param recordId
	 * @param orderId
	 * @param ret
	 * @return
	 */
	boolean checkRecordIsPay(String recordId,String orderId, JSONObject ret);
	
	
	/**
	 * 回调成功更新
	 * @param recordId
	 * @param orderId
	 * @return
	 */
	boolean updateRecordSuccess(Integer recordId,String orderId,String ip);
	
	/**
	 * 回调失败更新
	 * @param recordId
	 * @param orderId
	 * @return
	 */
	boolean updateRecordFail(Integer recordId,String orderId);
	
	
	/**
	 * 充值管理 （列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<RechargeCustomize> queryRechargeList(RechargeCustomize rechargeCustomize) ;
	/**
	 * 通过订单号获取UserTransfer
	 * @param orderId
	 * @return
	 */
	public UserTransfer getUserTransferByOrderId(String orderId);

	
}
