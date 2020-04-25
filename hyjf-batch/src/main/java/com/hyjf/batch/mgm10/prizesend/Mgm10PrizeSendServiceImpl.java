package com.hyjf.batch.mgm10.prizesend;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.auto.InviteRecommendPrizeExample;
import com.hyjf.soa.apiweb.CommonParamBean;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 10月份活动--发放虚拟奖品（优惠券）
 * 
 * @author zhangjinpeng
 * 
 */
@Service
public class Mgm10PrizeSendServiceImpl extends BaseServiceImpl implements Mgm10PrizeSendService {

	private static final String THIS_CLASS = Mgm10PrizeSendServiceImpl.class.getName();

	/**
	 * 10月份MGM活动（优惠券发放标示）
	 */
	private static final int MGM10_ACTIVITY_FLAG = 7;

	Logger _log = LoggerFactory.getLogger(Mgm10PrizeSendServiceImpl.class);

	/**
	 * 10月份活动--发放虚拟奖品
	 */
	@Override
	public void updatePrizeSend() throws Exception {
		String methodName = "updatePrizeSend";
		Integer nowTime = GetDate.getNowTime10();
		try {
			InviteRecommendPrizeExample example = new InviteRecommendPrizeExample();
			InviteRecommendPrizeExample.Criteria criteria = example.createCriteria();
			// 奖品类别：优惠券
			criteria.andPrizeTypeEqualTo(2);
			// 奖品发放状态：未发放
			criteria.andPrizeSendFlagEqualTo(0);
			criteria.andDelFlgEqualTo(0);
			List<InviteRecommendPrize> prizeList = this.inviteRecommendPrizeMapper.selectByExample(example);
			for (InviteRecommendPrize prize : prizeList) {
				CommonParamBean paramBean = new CommonParamBean();
				// 根据奖品组编号发奖，存在一个奖品等级对应多个实物奖品的情况，即：一组奖品
				paramBean.setPrizeGroupCode(prize.getPrizeGroup());
				paramBean.setUserId(prize.getUserId().toString());
				paramBean.setSendFlg(MGM10_ACTIVITY_FLAG);
				// 调用发放优惠券接口
				String result = CommonSoaUtils.sendUserCoupon(paramBean);
				JSONObject obj = JSONObject.parseObject(result);
				int status = obj.getIntValue("status");
				int couponCount = obj.getIntValue("couponCount");
				@SuppressWarnings("unchecked")
				List<String> couponUserCodeList = (List<String>)obj.get("retCouponUserCodes");
				if (status == 0 && couponCount > 0) {
					prize.setPrizeSendFlag(1);
					prize.setUpdateTime(nowTime);
					// 备注：用户优惠券编号
					if(couponUserCodeList.size()==1){
						// 发放一张优惠券
						prize.setRemark(couponUserCodeList.get(0));
					}else{
						// 发放多张优惠券
						StringBuffer remark = new StringBuffer();
						for(int i=0;i<couponUserCodeList.size();i++){
							remark.append(couponUserCodeList.get(i));
							if(i != couponUserCodeList.size()-1){
								remark.append("<br />");
							}
						}
						prize.setRemark(remark.toString());
					}
					this.inviteRecommendPrizeMapper.updateByPrimaryKeySelective(prize);
					_log.info(THIS_CLASS + "==>" + methodName + "==>" + "用户：" + prize.getUserId() + "10月份MGM活动-补偿发放虚拟奖品成功！发放数量："+couponCount);
				}

			}
			
		} catch (Exception e) {
			_log.info(THIS_CLASS + "==>" + methodName + "==>" + "10月份MGM活动-补偿发放虚拟奖品异常！");
			throw new RuntimeException("10月份MGM活动-补偿发放虚拟奖品异常！");
		}
	}

}
