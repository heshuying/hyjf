package com.hyjf.batch.vip.gift;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.VipUserUpgrade;
import com.hyjf.mybatis.model.auto.VipUserUpgradeExample;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 自动检查体验金收益过期
 *
 * @author Administrator
 *
 */
@Service
public class GiftSendServiceImpl extends BaseServiceImpl implements GiftSendService {

    private static final String THIS_CLASS = GiftSendServiceImpl.class.getName();

    /**
     * 检查是否有未发放的vip礼包
     * 如果有未发放的vip礼包，则进行发放
     */
	@Override
	public void updateGiftSend() {
	    System.out.println("vip礼包batch发放开始");
		String methodName = "updateGiftSend";
		LogUtil.startLog(THIS_CLASS, methodName, "检查是否有未发放的vip礼包 开始。");
		VipUserUpgradeExample example = new VipUserUpgradeExample();
		example.createCriteria().andGiftFlgEqualTo(0);
		List<VipUserUpgrade> giftList = this.vipUserUpgradeMapper.selectByExample(example);
		for(VipUserUpgrade gift:giftList){
			CommonParamBean paramBean = new CommonParamBean();
			paramBean.setUserId(String.valueOf(gift.getUserId()));
			// 2:vip礼包
			paramBean.setSendFlg(2);
			// vip编号
			paramBean.setVipId(gift.getVipId());
			// 调用发放优惠券接口
			String jsonStr = CommonSoaUtils.sendUserCoupon(paramBean);
			// vip升级成长编号
			int upgradeId = gift.getId();
			JSONObject sendResult = JSONObject.parseObject(jsonStr);
			// 发放是否成功状态
			int sendStatus = sendResult.getIntValue("status");
			// 发放优惠券的数量
			int sendCount = sendResult.getIntValue("couponCount");
			if (sendStatus == 0 && sendCount > 0) {
				// 更新礼包发放状态
				try {
					this.updateGiftStatus(gift);
				} catch (Exception e) {
					LogUtil.errorLog(THIS_CLASS, methodName, "系统异常，礼包发放失败。用户编号：gift.getUserId()，vip成长编号："+upgradeId,e);
				}
			}
		}
		LogUtil.endLog(THIS_CLASS, methodName, "检查是否有未发放的vip礼包 结束。");
		
	}
	
	/**
	 * 更新礼包发放状态
	 */
	private void updateGiftStatus(VipUserUpgrade upgrade) throws Exception {
		int nowTime = GetDate.getNowTime10();
		// 0：未发放，1：已发放
		upgrade.setGiftFlg(1);
		upgrade.setUpdateTime(nowTime);
		upgrade.setUpdateUser(CustomConstants.OPERATOR_AUTO_GIFT);
		upgrade.setDelFlg(0);
		this.vipUserUpgradeMapper.updateByPrimaryKeySelective(upgrade);
	}

    
}
