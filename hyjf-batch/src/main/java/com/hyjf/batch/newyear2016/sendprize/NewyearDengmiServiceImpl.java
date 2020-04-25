package com.hyjf.batch.newyear2016.sendprize;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.NewyearQuestionUser;
import com.hyjf.mybatis.model.auto.NewyearQuestionUserExample;
import com.hyjf.mybatis.model.auto.NewyearSendPrize;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 2016新年活动-猜灯谜发放代金券
 * 
 * @author zhangjinpeng
 * 
 */
@Service
public class NewyearDengmiServiceImpl extends BaseServiceImpl implements NewyearDengmiService {

	private static final String THIS_CLASS = NewyearDengmiServiceImpl.class.getName();

	Logger _log = LoggerFactory.getLogger(NewyearDengmiServiceImpl.class);

	/**
	 * 猜灯谜发放代金券
	 */
	@Override
	public void updatePrizeSend(NewyearQuestionUser prize) throws Exception {
		String methodName = "updatePrizeSend";
		Integer nowTime = GetDate.getNowTime10();
		try {
			// 取得相应金额对应的代金券编号
			String couponCode = CouponEnum.getName(prize.getPrizeJine());
			if (StringUtils.isEmpty(couponCode)) {
				return;
			}
			CommonParamBean paramBean = new CommonParamBean();
			paramBean.setSendFlg(10);
			paramBean.setUserId(prize.getUserId().toString());
			// 优惠券编号
			paramBean.setCouponCode(couponCode);
			// 是否翻倍
			if (prize.getDoubleFlg() == 1) {
				paramBean.setSendCount(2);
			} else {
				paramBean.setSendCount(1);
			}
			// 发放优惠券
			String result = CommonSoaUtils.sendUserCoupon(paramBean);
			int couponCount = 0;
			List<String> couponUserCodeList = null;
			try {
				JSONObject obj = JSONObject.parseObject(result);
				couponCount = obj.getIntValue("couponCount");
				couponUserCodeList = JSONObject.parseArray(obj.getString("retCouponUserCodes"), String.class);
			} catch (Exception e) {
				// json转换失败，说明服务端抛出异常，通讯失败
				couponCount = -1;
				_log.info("2016新年活动-猜灯谜发放代金券失败！用户编号：" + prize.getUserId() + ",应发放数量：" + paramBean.getSendCount());
			}
			// 循环插入用户奖品记录（翻倍的情况，是一个用户发放两张代金券）
			for(String couponUserCode:couponUserCodeList){
				NewyearSendPrize record = new NewyearSendPrize();
				// 所属活动-红红火火闹元宵
				record.setActivityFlg(2);
				// 发放时间
				record.setAddTime(nowTime);
				// 优惠券编号
				record.setCouponCode(couponUserCode);
				// 优惠券面额
				record.setCouponJine(prize.getPrizeJine());
				record.setDelFlg(0);
				if (couponCount > 0) {
					// 发放成功
					record.setSendStatus(1);
				} else {
					// 发放失败
					record.setSendStatus(0);
				}
				// 用户编号
				record.setUserId(prize.getUserId());
				// 记录编号
				record.setUserPrizeId(prize.getId());
				this.newyearSendPrizeMapper.insertSelective(record);
			}

			// 更新奖品发放状态
			prize.setSendFlg(1);
			this.newyearQuestionUserMapper.updateByPrimaryKeySelective(prize);
			if (couponCount == -1) {
				_log.info("服务通讯失败，发放新年活动代金券暂停执行");
				throw new Exception();
			}

		} catch (Exception e) {
			_log.info(THIS_CLASS + "==>" + methodName + "==>" + "2016新年活动-猜灯谜发放代金券异常！");
			throw new RuntimeException("2016新年活动-猜灯谜发放代金券异常！");
		}
	}

	/**
	 * 取得猜灯谜用户
	 */
	@Override
	public List<NewyearQuestionUser> selectQuestionUserList() {
		NewyearQuestionUserExample example = new NewyearQuestionUserExample();
		NewyearQuestionUserExample.Criteria criteria = example.createCriteria();
		// 当前奖励
		criteria.andCurrentExchangeEqualTo(1);
		// 金额大于0
		criteria.andPrizeJineGreaterThan(0);
		// 回答正确
		// criteria.andUserAnswerResultEqualTo(0);
		// 奖励未发放
		criteria.andSendFlgEqualTo(0);
		// 查询闹元宵活动需要发放的所有代金券
		List<NewyearQuestionUser> prizeList = this.newyearQuestionUserMapper.selectByExample(example);
		return prizeList;
	}

}
