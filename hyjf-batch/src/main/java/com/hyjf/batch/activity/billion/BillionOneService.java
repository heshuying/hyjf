package com.hyjf.batch.activity.billion;

import java.util.Date;
import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;

public interface BillionOneService extends BaseService {

   /**
    * 获取统计数据
    * @return
    */
	public CalculateInvestInterest getCalculateRecord();
	
    /**
     * 满心满亿 生成中奖用户
     * num  1:96亿
     * 		2:97亿
     * 		3:98亿
     * 		4:99亿
     * 		5:100亿
     */
	public void prizeGenerate(int num,Date updateTime);
	
	/**
	 * 获取所有到达点时间
	 * @return
	 */
	public List<ActivityBillionSecondTime> getAccordTimes();
	
	/**
	 * 助力百亿 活动 生成中奖信息
	 * @param startTime
	 * @param endTime
	 */
	public void  createBillionSecondPrize(int startTime,int endTime);
	
}
