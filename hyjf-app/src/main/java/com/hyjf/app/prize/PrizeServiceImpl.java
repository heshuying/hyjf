package com.hyjf.app.prize;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PrizeCodeUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.mybatis.model.auto.UserPrizeCode;
import com.hyjf.mybatis.model.auto.UserPrizeCodeExample;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeCodeCustomize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service
public class PrizeServiceImpl extends BaseServiceImpl implements PrizeService {

	/**
	 * 取得活动奖品列表
	 */
	@Override
	public List<AppUserPrizeCodeCustomize> getPrizeList(String prizeSelfCode) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(prizeSelfCode)) {
			paraMap.put("prizeSelfCode", prizeSelfCode);
		}
		List<AppUserPrizeCodeCustomize> prizeList = this.appUserPrizeCodeCustomizeMapper
				.getPrizeList(paraMap);

		return prizeList;
	}

	/**
	 * 取得剩余夺宝次数
	 */
	@Override
	public int getUserPrizeCount(int userId, String activityId) {
		// 取得活动
		ActivityList activity = this.activityListMapper
				.selectByPrimaryKey(Integer.valueOf(activityId));
		// 活动开始时间
		int startDate = activity.getTimeStart();
		// 活动结束时间
		int endDate = activity.getTimeEnd();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("userId", userId);
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		// 汇消费、尊享汇除外
		paraMap.put("projectType1", 11);
		paraMap.put("projectType2", 8);
		// 取得活动期内出借总额
		BigDecimal accountSum = this.appUserPrizeCodeCustomizeMapper
				.getUserTenderAccountSum(paraMap);
		accountSum = accountSum == null ? BigDecimal.ZERO : accountSum;
		// 出借每满1000元即可获得一次夺宝机会
		BigDecimal div = accountSum.divide(new BigDecimal(
				PrizeDefine.TENDER_ACCOUNT_SCD), 0, BigDecimal.ROUND_DOWN);
		// 取得用户已参与夺宝的次数
		UserPrizeCodeExample exampleCode = new UserPrizeCodeExample();
		UserPrizeCodeExample.Criteria criteria = exampleCode.createCriteria();
		criteria.andUserIdEqualTo(userId);
		int joinedCount = this.userPrizeCodeMapper.countByExample(exampleCode);
		return div.intValue() - joinedCount;
	}

	/**
	 * 
	 * 取得活动期内累计出借
	 * 
	 * @author hsy
	 * @param userId
	 * @param activityId
	 * @return
	 */
	@Override
	public BigDecimal getTenderAccountSum(int userId, String activityId) {
		// 取得活动
		ActivityList activity = this.activityListMapper
				.selectByPrimaryKey(Integer.valueOf(activityId));
		// 活动开始时间
		int startDate = activity.getTimeStart();
		// 活动结束时间
		int endDate = activity.getTimeEnd();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("userId", userId);
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		// 汇消费、尊享汇除外
		paraMap.put("projectType1", 11);
		paraMap.put("projectType2", 8);
		// 取得活动期内出借总额
		BigDecimal accountSum = this.appUserPrizeCodeCustomizeMapper
				.getUserTenderAccountSum(paraMap);
		return accountSum;
	}

	/**
	 * 
	 * 查询该用户是否发送过优惠券
	 * 
	 * @author hsy
	 * @param userId
	 * @param activityId
	 * @return
	 */
	@Override
	public boolean getCouponSendStatus(int userId, String activityId) {
		CouponUserExample example = new CouponUserExample();
		example.createCriteria().andUserIdEqualTo(userId)
				.andCouponSourceEqualTo(2)
				.andActivityIdEqualTo(Integer.parseInt(activityId));
		List<CouponUser> couponUsers = couponUserMapper
				.selectByExample(example);
		if (couponUsers != null && couponUsers.size() == 2) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 发放优惠券
	 * 
	 * @author hsy
	 * @param userId
	 * @return
	 */
	@Override
	public JSONObject sendCoupon(int userId) {
		CommonParamBean paramBean = new CommonParamBean();
		paramBean.setUserId(String.valueOf(userId));

		paramBean.setSendFlg(3);
		// 调用发放优惠券接口
		String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
		JSONObject sendResult = JSONObject.parseObject(jsonStr);

		return sendResult;
	}

	/**
	 * 更新用户兑奖码列表
	 */
	@Override
	public synchronized JSONObject updatePrize(int userId, String prizeSelfCode) {
		int nowTime = GetDate.getNowTime10();
		JSONObject result = new JSONObject();
		// 并发校验
		// 活动编号
		String activityId = PropUtils
				.getSystem(PropertiesConstants.TENDER_PRIZE_ACTIVITY_ID);
		int prizeCount = this.getUserPrizeCount(userId, activityId);
		if (prizeCount <= 0) {
			result.put(CustomConstants.APP_STATUS,
					CustomConstants.APP_STATUS_FAIL);
			return jsonMessage("抱歉，您当前没有夺宝机会！", "2");
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("prizeSelfCode", prizeSelfCode);
		// 取得该奖品对应的最大的用户自增编码
		AppUserPrizeCodeCustomize userPrize = this.appUserPrizeCodeCustomizeMapper
				.getUserPrizeIdentityId(paraMap);
		if(userPrize.getAllPersonCount() <= userPrize.getJoinedPersonCount()){
			result.put(CustomConstants.APP_STATUS,
					CustomConstants.APP_STATUS_FAIL);
			return jsonMessage("夺宝次数已满", "3");
		}
		// 根据当前的最大自增编码+1，生成用户兑奖码
		String prizeCode = PrizeCodeUtils.getUserPrizeCode(
				userPrize.getPrizeIdentityId() + 1, prizeSelfCode);

		UserPrizeCode userPrizeCode = new UserPrizeCode();
		// 夺宝用户
		userPrizeCode.setUserId(userId);
		// 兑奖号码
		userPrizeCode.setPrizeCode(prizeCode);
		// 奖品编号
		userPrizeCode.setPrizeId(userPrize.getPrizeId());
		// 兑奖号自增编号
		userPrizeCode.setPrizeIdentityId(userPrize.getPrizeIdentityId() + 1);
		userPrizeCode.setAddTime(nowTime);
		userPrizeCode.setAddUser(String.valueOf(userId));
		userPrizeCode.setUpdateTime(nowTime);
		userPrizeCode.setUpdateUser(String.valueOf(userId));
		userPrizeCode.setDelFlg(0);
		// 插入一条兑奖码数据
		this.userPrizeCodeMapper.insertSelective(userPrizeCode);
		Map<String, Object> paraMap1 = new HashMap<String, Object>();
		paraMap1.put("prizeSelfCode", prizeSelfCode);
		this.appUserPrizeCodeCustomizeMapper.updatePrizeJoinedCount(paraMap1);
		// 设置正常转账
		result.put(CustomConstants.APP_STATUS,
				CustomConstants.APP_STATUS_SUCCESS);
		result.put("prizeName", userPrize.getPrizeName());
		result.put("prizeCode", prizeCode);
		if(!checkMaxJoinedCount(prizeSelfCode)){
			result.put("canPrize", false);
		}
		return result;
	}
	
	/**
	 * 校验是否达到最大参与人次
	 * @return true：可以继续夺宝  false：相反
	 */
	@Override
	public boolean checkMaxJoinedCount(String prizeSelfCode){
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("prizeSelfCode", prizeSelfCode);
		int count = this.appUserPrizeCodeCustomizeMapper.checkMaxJoinedCount(paraMap);
		return count == 1 ? true : false;
	}

	/**
	 * 检查参数的正确性
	 * 
	 * @param userId
	 * @param transAmt
	 *            交易金额
	 * @param flag
	 *            交易类型，1购买 2赎回
	 * @return
	 */
	public JSONObject checkParam(Integer userIdInt) {

		if (userIdInt == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		}

		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("errorCode", "0");
		return jsonMessage;
	}

	/**
	 * 组成返回信息
	 * 
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String data, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(data)) {
			jo = new JSONObject();
			jo.put("errorCode", error);
			jo.put("data", data);
		}
		return jo;
	}

	/**
	 * 取得用户兑奖码列表
	 */
	@Override
	public List<AppUserPrizeCodeCustomize> getUserPrizeCodeList(int userId) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("userId", userId);
		List<AppUserPrizeCodeCustomize> userPrizeList = this.appUserPrizeCodeCustomizeMapper
				.getUserPrizeCodeList(paraMap);
		return userPrizeList;
	}

}