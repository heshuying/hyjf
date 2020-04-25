package com.hyjf.batch.coupon.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponConfigExample;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;

/**
 * 
 * 优惠券过期发送push消息
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月22日
 * @see 上午11:43:23
 */
@Service
public class CouponExpiredPushServiceImpl extends BaseServiceImpl implements CouponExpiredPushService {

    private static final String THIS_CLASS = CouponExpiredPushServiceImpl.class.getName();
    
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;
    
    /**
     * 检查并发送push消息
     */
	@Override
	public void sendExpiredMsgAct() {
		String methodName = "sendExpiredMsgAct";
		LogUtil.startLog(THIS_CLASS, methodName, "优惠券即将过期push消息提醒开始");
		//yyyy-MM-dd 的时间戳
		int nowBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
		int nowEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 1));
		
		//两天后
		int towBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 2));
        int towEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 3));
        
        //三天后
        int threeBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 3));
        int threeEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 4));
        
        //七天后
        int sevenBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 7));
        int sevenEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 8));
        
        //前一天
        int yestodayBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, -1));
        int yestodayEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
		
		// 取得体验金出借（无真实出借）的还款列表
		CouponUserExample example = new CouponUserExample();
		CouponUserExample.Criteria criteria = example.createCriteria();
		criteria.andDelFlagEqualTo(0);
		// 未使用
		criteria.andUsedFlagEqualTo(0);
		criteria.andEndTimeGreaterThanOrEqualTo(nowBeginDate);
		criteria.andEndTimeLessThan(nowEndDate);
		
		CouponUserExample.Criteria criteria2 = example.createCriteria();
        criteria2.andDelFlagEqualTo(0);
        // 未使用
        criteria2.andUsedFlagEqualTo(0);
        criteria2.andEndTimeGreaterThanOrEqualTo(towBeginDate);
        criteria2.andEndTimeLessThan(towEndDate);
        example.or(criteria2);
        
        CouponUserExample.Criteria criteria3 = example.createCriteria();
        criteria3.andDelFlagEqualTo(0);
        // 未使用
        criteria3.andUsedFlagEqualTo(0);
        criteria3.andEndTimeGreaterThanOrEqualTo(threeBeginDate);
        criteria3.andEndTimeLessThan(threeEndDate);
        example.or(criteria3);
        
        CouponUserExample.Criteria criteria7 = example.createCriteria();
        criteria7.andDelFlagEqualTo(0);
        // 未使用
        criteria7.andUsedFlagEqualTo(0);
        // 截止日小于当前时间
        criteria7.andEndTimeGreaterThanOrEqualTo(sevenBeginDate);
        criteria7.andEndTimeLessThan(sevenEndDate);
        example.or(criteria7);
        
        List<CouponUser> couponUsers = couponUserMapper.selectByExample(example);
        
        for(CouponUser cUser: couponUsers){
            CouponConfigExample cExample = new CouponConfigExample();
            cExample.createCriteria().andCouponCodeEqualTo(cUser.getCouponCode());
            List<CouponConfig> config = couponConfigMapper.selectByExample(cExample);
            if(config == null || config.isEmpty()){
                LogUtil.errorLog(this.getClass().getName(), methodName, "根据优惠券编号没有查询到优惠券配置，编号：" + cUser.getCouponCode(), null);
                continue;
            }
            
            LogUtil.infoLog(this.getClass().getName(), methodName, "即将过期的优惠券编号：" + cUser.getCouponUserCode() + " 面值：" + config.get(0).getCouponQuota() + " 有效期截止日： " + GetDate.formatDate(Long.parseLong(cUser.getEndTime()+"000")));
            
//            UsersInfoExample uExample = new UsersInfoExample();
//            uExample.createCriteria().andUserIdEqualTo(cUser.getUserId());
//            List<UsersInfo> userInfo = usersInfoMapper.selectByExample(uExample);
//            if(userInfo == null || userInfo.isEmpty()){
//                LogUtil.errorLog(this.getClass().getName(), methodName, "根据用户ID没有查询到用户详情信息，用户ID：" + cUser.getUserId(), null);
//                continue;
//            }
            
            //发送push消息
            Map<String, String> param = new HashMap<String, String>();
            param.put("val_coupon_balance", config.get(0).getCouponType() == 1 ? config.get(0).getCouponQuota() + "元" : config.get(0).getCouponType() == 2 ? config.get(0).getCouponQuota() + "%" : config.get(0).getCouponQuota() + "元");
            param.put("val_coupon_type", config.get(0).getCouponType() == 1 ? "体验金" : config.get(0).getCouponType() == 2 ? "加息券" : "代金券");
            param.put("val_profit_deadline", GetDate.formatDate(Long.parseLong(cUser.getEndTime()+"000")));
            AppMsMessage appMsMessage = new AppMsMessage(cUser.getUserId(), param, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_COUPON_DEADLINE);
            appMsProcesser.gather(appMsMessage);
        }
        
        //昨天过期的
        CouponUserExample exampleY = new CouponUserExample();
        CouponUserExample.Criteria criteriaY = exampleY.createCriteria();
        criteriaY.andDelFlagEqualTo(0);
        // 未使用
        criteriaY.andUsedFlagEqualTo(0);
        criteriaY.andEndTimeGreaterThanOrEqualTo(yestodayBeginDate);
        criteriaY.andEndTimeLessThan(yestodayEndDate);
        List<CouponUser> couponUsersExp = couponUserMapper.selectByExample(exampleY);
        for(CouponUser cUser: couponUsersExp){
            CouponConfigExample cExample = new CouponConfigExample();
            cExample.createCriteria().andCouponCodeEqualTo(cUser.getCouponCode());
            List<CouponConfig> config = couponConfigMapper.selectByExample(cExample);
            if(config == null || config.isEmpty()){
                LogUtil.errorLog(this.getClass().getName(), methodName, "根据优惠券编号没有查询到优惠券配置，编号：" + cUser.getCouponCode(), null);
                continue;
            }
            
            LogUtil.infoLog(this.getClass().getName(), methodName, "昨天过期的优惠券编号：" + cUser.getCouponUserCode() + " 面值：" + config.get(0).getCouponQuota() + " 有效期截止日： " + GetDate.formatDate(Long.parseLong(cUser.getEndTime()+"000")));
            
            //发送push消息
            Map<String, String> param = new HashMap<String, String>();
            param.put("val_coupon_balance", config.get(0).getCouponType() == 1 ? config.get(0).getCouponQuota() + "元" : config.get(0).getCouponType() == 2 ? config.get(0).getCouponQuota() + "%" : config.get(0).getCouponQuota() + "元");
            param.put("val_coupon_type", config.get(0).getCouponType() == 1 ? "体验金" : config.get(0).getCouponType() == 2 ? "加息券" : "代金券");
            AppMsMessage appMsMessage = new AppMsMessage(cUser.getUserId(), param, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_COUPON_INVALIDED);
            appMsProcesser.gather(appMsMessage);
        }
        
		LogUtil.endLog(THIS_CLASS, methodName, "优惠券即将过期push消息提醒结束");
	}

	public static void main(String[] args) {
	    int yestodayBeginDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf, 7));
        int yestodayEndDate = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf,8));
	    System.out.println(yestodayBeginDate);
	    System.out.println(yestodayEndDate);
    }
    
}
