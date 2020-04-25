package com.hyjf.app.prize;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeCodeCustomize;

public interface PrizeService extends BaseService {
	
	/**
	 * 取得奖品列表
	 * @param 
	 * @return
	 */
	List<AppUserPrizeCodeCustomize> getPrizeList(String prizeSelfCode);
	
	/**
	 * 取得用户剩余抽奖次数
	 * @param userId
	 * @param activityId
	 * @return
	 */
	int getUserPrizeCount(int userId,String activityId);
	
	/**
	 * 用户参与夺宝
	 * @param userId
	 * @param prizeSelfCode
	 * @return
	 */
	JSONObject updatePrize(int userId,String prizeSelfCode);
	
	/**
	 * 校验
	 * @param account
	 * @param userIdInt
	 * @return
	 */
	JSONObject checkParam(Integer userIdInt);
	
	/**
	 * 取得用户兑奖码列表
	 * @param userId
	 * @return
	 */
	List<AppUserPrizeCodeCustomize> getUserPrizeCodeList(int userId);

    JSONObject sendCoupon(int userId);

    boolean getCouponSendStatus(int userId, String activityId);

    BigDecimal getTenderAccountSum(int userId, String activityId);
    
    boolean checkMaxJoinedCount(String prizeSelfCode);
	
}
