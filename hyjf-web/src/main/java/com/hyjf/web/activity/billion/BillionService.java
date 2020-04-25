package com.hyjf.web.activity.billion;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.auto.ActivityBillionOne;
import com.hyjf.mybatis.model.auto.ActivityBillionSecond;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;
import com.hyjf.mybatis.model.customize.billion.ActivityBillionThirdCustomize;
import com.hyjf.web.BaseService;

/**
 * 满心满亿活动
 * @author Michael
 */
public interface BillionService extends BaseService {

		
	/**
	 * 获取总出借金额	
	 * @return
	 */
	public BigDecimal getTendSum();

	/**
	 * 获取活动记录
	 * @return
	 */
	public List<ActivityBillionOne> getBillionOneRecords();
	
	
	/**
	 * 获取活动2时间点记录
	 * @return
	 */
	public List<ActivityBillionSecondTime> getBillionSecondTimeRecords();
	
	
	/**
	 * 获取活动2获奖记录
	 * @return
	 */
	public List<ActivityBillionSecond> getBillionSecondRecords();
	
	
	/**
	 * 获取活动3前台展示数据
	 * @author pcc
	 * @return
	 */
    public List<ActivityBillionThirdCustomize> getBillionThirdActivityList();
    /**
     * 用户抢券发放
     * @author pcc
     * @param activityId
     * @param userId
     * @return
     */
    public String updateUserPartakeBillionThirdActivity(String killId, Integer userId);
	
}
