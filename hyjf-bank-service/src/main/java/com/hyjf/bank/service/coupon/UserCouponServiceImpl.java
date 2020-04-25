package com.hyjf.bank.service.coupon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.*;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("unchecked")
@Service
public class UserCouponServiceImpl extends BaseServiceImpl implements UserCouponService {
    private static final String THIS_CLASS = UserCouponServiceImpl.class.getName();
    Logger _log = LoggerFactory.getLogger(UserCouponServiceImpl.class);
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;

    // 评测送加息券
    static List<String> couponCodeOtherList = new ArrayList<String>();

    static {
        // 加息券编号
        couponCodeOtherList.add(PropUtils.getSystem("hyjf.coupon.id"));
    }

    // 注册送体验金
    static List<String> couponCodeRegList = new ArrayList<String>();
    static {
        // 体验金编号
        couponCodeRegList.add("");
    }

    // 出借夺宝送代金券
//    static List<String> couponCodePrizeList = new ArrayList<String>();
//    static {
//        // 出借夺宝
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.prize.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            couponCodePrizeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "出借夺宝优惠券发放id没有配置", null);
//        }
//
//    }

    // 投之家注册送68元代金券活动代金券编号
//    static List<String> tzjRegisterCouponCodeList = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.tzj.register.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            tzjRegisterCouponCodeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "投之家注册送68元代金券活动代金券编号没有配置", null);
//        }
//
//    }

    // 出借满1000送加息券活动优惠券编号
//    static List<String> investmentGiveCouponCodeList = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.investment.give.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            investmentGiveCouponCodeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "出借满1000送加息券活动优惠券编号没有配置", null);
//        }
//
//    }

    // 全站注册送68元代金券活动代金券编号
//    static List<String> registerGiveCouponCodeList = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.register.give.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            registerGiveCouponCodeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "全站注册送68元代金券活动代金券编号没有配置", null);
//        }
//
//    }
    // 助力百亿 100万及以上 送 1.2%加息券
//    static List<String> billionSecondOneCouponCodeList = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.billion.second.one.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            billionSecondOneCouponCodeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "助力百亿出借100万及以上 送 1.2%加息券编号没有配置", null);
//        }
//
//    }
    // 助力百亿出借30万-100万（不含） 送300元代金券
//    static List<String> billionSecondTwoCouponCodeList = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.billion.second.two.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            billionSecondTwoCouponCodeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "助力百亿出借30万-100万（不含） 送300元代金券编号没有配置", null);
//        }
//    }
    // 助力百亿出借5万-30万（不含） 送100元代金券
//    static List<String> billionSecondThreeCouponCodeList = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.billion.second.three.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            billionSecondThreeCouponCodeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "助力百亿出借5万-30万（不含） 送100元代金券编号没有配置", null);
//        }
//    }

    // 助力百亿出借5万(不含)以下送30元代金券
//    static List<String> billionSecondFourCouponCodeList = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.billion.second.four.coupon.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            billionSecondFourCouponCodeList = Arrays.asList(codeArray);
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "助力百亿出借5万(不含)以下送30元代金券编号没有配置", null);
//        }
//    }
    
    // 注册送188元新手红包
    /*static List<String> regist188CouponCodeList = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.register.188.coupon.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            regist188CouponCodeList = Arrays.asList(codeArray);
        } else {
            LogUtil.errorLog(THIS_CLASS, "", "注册送188元新手红包代金券编号没有配置", null);
        }
    }*/
    
    // 注册送888元新手红包
    static List<String> regist888CouponCodeList = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.register.888.coupon.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            regist888CouponCodeList = Arrays.asList(codeArray);
        } else {
            LogUtil.errorLog(THIS_CLASS, "", "注册送888元新手红包代金券编号没有配置", null);
        }
    }
    //#2017年10月签到活动1
//    static List<String> actSignInList1 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("com.hyjf.activity.actsignin.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSignInList1 = Arrays.asList(new String[]{codeArray[0]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2017年10月签到活动没有配置1", null);
//        }
//    }
    //#2017年10月签到活动2
//    static List<String> actSignInList2 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("com.hyjf.activity.actsignin.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSignInList2 = Arrays.asList(new String[]{codeArray[1]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2017年10月签到活动没有配置2", null);
//        }
//    }
    //#2017年10月签到活动3
//    static List<String> actSignInList3 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("com.hyjf.activity.actsignin.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSignInList3 = Arrays.asList(new String[]{codeArray[2]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2017年10月签到活动没有配置3", null);
//        }
//    }
    //#2018元旦月签到活动4
//    static List<String> actSignInList4 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("com.hyjf.activity.actsignin.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSignInList4 = Arrays.asList(new String[]{codeArray[3]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "#2018元旦月签到活动4没有配置3", null);
//        }
//    }
    //#2018元旦月签到活动5
//    static List<String> actSignInList5 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("com.hyjf.activity.actsignin.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSignInList5 = Arrays.asList(new String[]{codeArray[4]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", "2018元旦月签到活动5没有配置3", null);
//        }
//    }
    //#2018元旦月签到活动6
//    static List<String> actSignInList6 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("com.hyjf.activity.actsignin.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSignInList6 = Arrays.asList(new String[]{codeArray[5]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2017年10月签到活动没有配置3", null);
//        }
//    }
//    //#2018年春节活动
//    static List<String> actSpringList1 = new ArrayList<String>();
//    static {
//    	LogUtil.errorLog(THIS_CLASS, "", " 2018年春节活动配置如下:"+PropUtils.getSystem("hyjf.act.dec.2018.spring.code"), null);
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.act.dec.2018.spring.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSpringList1 = Arrays.asList(new String[]{codeArray[0]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2018年春节活动没有配置", null);
//        }
//    }
//    //#2018年春节活动
//    static List<String> actSpringList2 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.act.dec.2018.spring.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSpringList2 = Arrays.asList(new String[]{codeArray[1]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2018年春节活动没有配置", null);
//        }
//    }
//    //#2018年春节活动
//    static List<String> actSpringList3 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.act.dec.2018.spring.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSpringList3 = Arrays.asList(new String[]{codeArray[2]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2018年春节活动没有配置", null);
//        }
//    }
//    //#2018年春节活动
//    static List<String> actSpringList4 = new ArrayList<String>();
//    static {
//        String[] codeArray = null;
//        String codes = PropUtils.getSystem("hyjf.act.dec.2018.spring.code");
//        if (StringUtils.isNotEmpty(codes)) {
//            codeArray = codes.split(",");
//            actSpringList4 = Arrays.asList(new String[]{codeArray[3]});
//        } else {
//            LogUtil.errorLog(THIS_CLASS, "", " 2018年春节活动没有配置", null);
//        }
//    }
    static List<String> actLiseted2List1 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List1 = Arrays.asList(new String[]{codeArray[0]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置1", null);
        }
    }
    static List<String> actLiseted2List2 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List2 = Arrays.asList(new String[]{codeArray[1]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置2", null);
        }
    }
    static List<String> actLiseted2List3 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List3 = Arrays.asList(new String[]{codeArray[2]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置3", null);
        }
    }
    static List<String> actLiseted2List4 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List4 = Arrays.asList(new String[]{codeArray[3],codeArray[3]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置4", null);
        }
    }


    static List<String> actLiseted2List5 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List5 = Arrays.asList(new String[]{codeArray[4]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置5", null);
        }
    }
    static List<String> actLiseted2List6 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List6 = Arrays.asList(new String[]{codeArray[5]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置6", null);
        }
    }
    static List<String> actLiseted2List7 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List7 = Arrays.asList(new String[]{codeArray[6]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置7", null);
        }
    }
    static List<String> actLiseted2List8 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List8 = Arrays.asList(new String[]{codeArray[7]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置8", null);
        }
    }
    static List<String> actLiseted2List9 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List9 = Arrays.asList(new String[]{codeArray[8]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置9", null);
        }
    }
    static List<String> actLiseted2List10 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List10 = Arrays.asList(new String[]{codeArray[9]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置10", null);
        }
    }
    static List<String> actLiseted2List11 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List11 = Arrays.asList(new String[]{codeArray[10]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置11", null);
        }
    }
    static List<String> actLiseted2List12 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List12 = Arrays.asList(new String[]{codeArray[11]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置12", null);
        }
    }
    static List<String> actLiseted2List13 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List13 = Arrays.asList(new String[]{codeArray[12]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置13", null);
        }
    }
    static List<String> actLiseted2List14 = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.listed2.code");
        if (StringUtils.isNotEmpty(codes)) {
            codeArray = codes.split(",");
            actLiseted2List14 = Arrays.asList(new String[]{codeArray[13]});
        } else {
            LogUtil.errorLog(THIS_CLASS, "", " 上市活动加息券没有配置14", null);
        }
    }
    static List<String> act518List = new ArrayList<String>();
    static {
        String[] codeArray = null;
        String codes = PropUtils.getSystem("hyjf.act.518.code");
        if (StringUtils.isNotEmpty(codes)) {
        	codeArray = codes.split(",");
        	Collections.addAll(act518List, codeArray);
        } else {
            LogUtil.errorLog(THIS_CLASS, "", "518活动券没有配置", null);
        }
    }
    /** 活动编号 */
    static final String SEND_ACTIVITY_ID = PropUtils.getSystem("hyjf.activity.id");

    /** 出借夺宝活动ID */
    static final String PRIZE_ACTIVITY_ID = PropUtils.getSystem("tender.prize.activity.id");

    /** 投之家注册送68元代金券活动ID */
    static final String TZJ_REGISTER_ACTIVITY_ID = PropUtils.getSystem("hyjf.tzj.register.coupon.activity.id");

    /** 出借满1000送加息券活动id */
    static final String INVESTMENT_GIVE_ACTIVITY_ID = PropUtils.getSystem("hyjf.investment.give.coupon.activity.id");

    /** 全站注册送68元代金券活动id */
    static final String REGISTER_GIVE_COUPON_ACTIVITY_ID = PropUtils.getSystem("hyjf.register.give.coupon.activity.id");

    /** 十月份邀请新用户送优惠券活动id*/
    static final String MGM10_ACTIVITY_ID = PropUtils.getSystem(PropertiesConstants.MGM10_ACTIVITY_ID);
    
    /** 十一月份满心满亿活动id */
    static final String BILLION_COUPON_ACTIVITY_ID = PropUtils.getSystem("hyjf.billion.coupon.activity.id");
    
    /** 2016新年活动id */
    static final String NEWYEAR_2016_ACTIVITY_ID = PropUtils.getSystem("hyjf.newyear.2016.activity.id");
    
    /** 注册送188元新手红包 */
    //static final String REGIST_188_ACTIVITY_ID = PropUtils.getSystem("hyjf.register.188.activity.id");
    
    /** 注册送888元新手红包 */
    static final String REGIST_888_ACTIVITY_ID = PropUtils.getSystem("hyjf.register.888.activity.id");
    
    /** 七月份活动*/
    static final String ACTIVITY_SEVEN_ID = PropUtils.getSystem("activity.invite.seven.id");
    /** 2017年10月签到活动1*/
    static final String ACTIVITY_ACTSIGNIN_ID = PropUtils.getSystem("hyjf.actten2017.id");
    
    /**2018春节活动*/
    static final String ACTIVITY_SPRING_ID= PropUtils.getSystem("hyjf.act.dec.2018.spring.id");
    /** 上市活动2 */
    static final String ACTIVITY_LISTED2_ID = PropUtils.getSystem("hyjf.act.dec.2018.list.id");
//    518理财活动
    static final String ACTIVITY_518_ID = PropUtils.getSystem("hyjf.act.dec.2018.518.id");

    /**
     * 
     * 自动发放用户优惠券
     * 
     * @author pcc
     * @param paramBean
     * @throws Exception
     */
    @Override
    public synchronized JSONObject insertUserCoupon(UserCouponBean paramBean) throws Exception {
        String methodName = "insertUserCoupon";
        JSONObject retResult = new JSONObject();
        try {
            List<String> couponCodeList = new ArrayList<String>();
            int couponCount = 0;
            int activityId = Integer.MIN_VALUE;
            // 用户编号
            String userId = paramBean.getUserId();
            // 发送优惠券类别标识
            Integer sendFlg = paramBean.getSendFlg();
            // vip发放优惠券编号
            String sendCouponCode = StringUtils.EMPTY;
            // vip发放优惠券的数量
            int sendCouponCount = 0;
            // 优惠券来源1：手动发放，2：活动发放，3：vip礼包
            int couponSource = Integer.MIN_VALUE;
            // 需要返回的用户优惠券编号列表
            List<String> retCouponUserCodes = new ArrayList<String>();
            // 评测
            if(Validator.isNull(sendFlg) || sendFlg == 0){
            	if(!StringUtils.isBlank(paramBean.getCouponCode())){
            		couponCodeList.addAll(Arrays.asList(paramBean.getCouponCode().split(",")));
            		
            		sendCouponCount = paramBean.getSendCount();
            		// 如果翻倍
            		if(sendCouponCount > 1){
            			for(int i=1; i<sendCouponCount; i++){
            				couponCodeList.add(paramBean.getCouponCode());
            			}
            		}
            		
            	}
		        
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, paramBean.getActivityId(), paramBean.getCouponSource(),paramBean.getRemark(),retCouponUserCodes);
            }else if (sendFlg == UserCouponDefine.NUM_ONE) {
                couponCodeList = couponCodeOtherList;
                activityId = Integer.valueOf(SEND_ACTIVITY_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"");
            } else if (sendFlg == UserCouponDefine.NUM_TWO) {
                couponSource = 3;
                // VIP礼包发券
                int vipId = paramBean.getVipId();
                VipAuthExample example = new VipAuthExample();
                example.createCriteria().andVipIdEqualTo(vipId);
                // 取得相应vip的优惠券配置信息
                List<VipAuth> vipAuthList = this.vipAuthMapper.selectByExample(example);
                if (vipAuthList != null && vipAuthList.size() > 0) {
                    // VipAuth vipAuth = vipAuthList.get(0);
                    for (VipAuth vipAuth : vipAuthList) {
                        // 该vip级别对应的优惠券编号
                        sendCouponCode = vipAuth.getCouponCode();
                        // 该vip级别对应的优惠券发放数量
                        sendCouponCount = vipAuth.getCouponQuantity();
                        couponCodeList = new ArrayList<String>();
                        for (int i = 0; i < sendCouponCount; i++) {
                            couponCodeList.add(sendCouponCode);
                        }
                        // 发放优惠券
                        couponCount += this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"");
                    }

                }
            } /*else if (sendFlg == UserCouponDefine.NUM_THREE) {
                couponCodeList = couponCodePrizeList;
                activityId = Integer.valueOf(PRIZE_ACTIVITY_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"");
            } else if (sendFlg == UserCouponDefine.NUM_FOUR) {
                couponCodeList = tzjRegisterCouponCodeList;
                activityId = Integer.valueOf(TZJ_REGISTER_ACTIVITY_ID);
                couponSource = 2;
                if (checkSendRepeat(couponCodeList, userId, activityId)) {
                    // 发放优惠券
                    couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"");
                } else {
                    retResult.put("statusDesc", "用户已发放该活动优惠券");
                }
            } else if (sendFlg == UserCouponDefine.NUM_FIVE) {
                couponCodeList = investmentGiveCouponCodeList;
                activityId = Integer.valueOf(INVESTMENT_GIVE_ACTIVITY_ID);
                couponSource = 2;
                if (checkSendRepeat(couponCodeList, userId, activityId)) {
                    // 发放优惠券
                    couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"");
                } else {
                    retResult.put("statusDesc", "用户已发放该活动优惠券");
                }

            } else if (sendFlg == UserCouponDefine.NUM_SIX) {
                couponCodeList = registerGiveCouponCodeList;
                activityId = Integer.valueOf(REGISTER_GIVE_COUPON_ACTIVITY_ID);
                couponSource = 2;
                if (checkSendRepeat(couponCodeList, userId, activityId)) {
                    // 发放优惠券
                    couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"");
                } else {
                    retResult.put("statusDesc", "用户已发放该活动优惠券");
                }

            }*/ else if (sendFlg == UserCouponDefine.NUM_SEVEN){
                couponSource = 2; //活动发放
                activityId = Integer.valueOf(MGM10_ACTIVITY_ID);
                
                String groupCode = paramBean.getPrizeGroupCode();
                int sendCount = paramBean.getSendCount();
                if(sendCount <0 ){
                    sendCount = 0;
                }
                
                if(StringUtils.isEmpty(groupCode) || StringUtils.isEmpty(userId)){
                    retResult.put("status", 1);
                    retResult.put("statusDesc", "发券参数不正确");
                    LogUtil.errorLog(this.getClass().getName(), methodName, "发券参数不正确", null);
                    return retResult;
                }
                
                InvitePrizeConfExample pexample = new InvitePrizeConfExample();
                pexample.createCriteria().andPrizeGroupCodeEqualTo(groupCode);
                List<InvitePrizeConf> prizes = invitePrizeConfMapper.selectByExample(pexample);
                for(int i=0; i<sendCount; i++){
                    for(InvitePrizeConf prize : prizes){
                        couponCodeList.add(prize.getCouponCode());
                    }
                }
                // 发放优惠券
                couponCount += this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"", retCouponUserCodes);
                
            }/*else if(sendFlg == UserCouponDefine.NUM_EIGHT){
            	  if(paramBean.getSubFlg() == 1){
            		  couponCodeList = billionSecondOneCouponCodeList;
            	  }else if(paramBean.getSubFlg() == 2){
            		  couponCodeList = billionSecondTwoCouponCodeList;
            	  }else if(paramBean.getSubFlg() == 3){
            		  couponCodeList = billionSecondThreeCouponCodeList;
            	  }else if(paramBean.getSubFlg() == 4){
            		  couponCodeList = billionSecondFourCouponCodeList;
            	  }
                  activityId = Integer.valueOf(BILLION_COUPON_ACTIVITY_ID);
                  couponSource = 2;
                  if (checkSendRepeat(couponCodeList, userId, activityId)) {
                      // 发放优惠券
                      couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"", retCouponUserCodes);
                  } else {
                      retResult.put("statusDesc", "用户已发放该活动优惠券");
                  }
            }*/else if(sendFlg == UserCouponDefine.NUM_NINE){
                couponCodeList.add(paramBean.getPrizeGroupCode());
                activityId = Integer.valueOf(BILLION_COUPON_ACTIVITY_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"", retCouponUserCodes);
            }else if(sendFlg == UserCouponDefine.NUM_TEN){
            	// 2016新年活动
            	couponCodeList.addAll(Arrays.asList(paramBean.getCouponCode().split(",")));
		    	activityId = Integer.valueOf(NEWYEAR_2016_ACTIVITY_ID);
		        couponSource = 2;
		        sendCouponCount = paramBean.getSendCount();
		        // 如果翻倍
		        if(sendCouponCount == 2){
		        	couponCodeList.addAll(Arrays.asList(paramBean.getCouponCode().split(",")));
		        }
		        // 发放优惠券
		        couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"", retCouponUserCodes);
            	
                
            } else if (sendFlg == UserCouponDefine.NUM_11) {
            	// 注册送888元新手红包
                couponCodeList = regist888CouponCodeList;
                activityId = Integer.valueOf(REGIST_888_ACTIVITY_ID);
                couponSource = 2;
                if (checkSendRepeat(couponCodeList, userId, activityId)) {
                    // 发放优惠券
                    couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"");
                } else {
                    retResult.put("statusDesc", "用户已发放该活动优惠券");
                }

            }else if(sendFlg == UserCouponDefine.NUM_12){
                // 七月份活动发券
                couponCodeList.addAll(Arrays.asList(paramBean.getCouponCode().split(",")));
                activityId = Integer.valueOf(ACTIVITY_SEVEN_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }
           /* else if(sendFlg == UserCouponDefine.NUM_15){
                // 2017年10月签到活动
                couponCodeList=actSignInList1;
                activityId = Integer.valueOf(ACTIVITY_ACTSIGNIN_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }else if(sendFlg == UserCouponDefine.NUM_16){
                // 2017年10月签到活动
                couponCodeList=actSignInList2;
                activityId = Integer.valueOf(ACTIVITY_ACTSIGNIN_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }else if(sendFlg == UserCouponDefine.NUM_17){
                // 2017年10月签到活动
                couponCodeList=actSignInList3;
                activityId = Integer.valueOf(ACTIVITY_ACTSIGNIN_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }else if(sendFlg == UserCouponDefine.NUM_18){
                // 2017年10月签到活动
                couponCodeList=actSignInList4;
                activityId = Integer.valueOf(ACTIVITY_ACTSIGNIN_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }else if(sendFlg == UserCouponDefine.NUM_19){
                // 2017年10月签到活动
                couponCodeList=actSignInList5;
                activityId = Integer.valueOf(ACTIVITY_ACTSIGNIN_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }else if(sendFlg == UserCouponDefine.NUM_20){
                // 2017年10月签到活动
                couponCodeList=actSignInList6;
                activityId = Integer.valueOf(ACTIVITY_ACTSIGNIN_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }*/
//            else if(sendFlg == UserCouponDefine.NUM_21){
//                // 2018新年活动
//                couponCodeList=actSpringList1;
//                activityId = Integer.valueOf(ACTIVITY_SPRING_ID);
//                couponSource = 2;
//                // 发放优惠券
//                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
//
//            }
//            else if(sendFlg == UserCouponDefine.NUM_22){
//                // 2018新年活动
//                couponCodeList=actSpringList2;
//                activityId = Integer.valueOf(ACTIVITY_SPRING_ID);
//                couponSource = 2;
//                // 发放优惠券
//                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
//
//            }
//            else if(sendFlg == UserCouponDefine.NUM_23){
//                // 2018新年活动
//                couponCodeList=actSpringList3;
//                activityId = Integer.valueOf(ACTIVITY_SPRING_ID);
//                couponSource = 2;
//                // 发放优惠券
//                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
//
//            }
//            else if(sendFlg == UserCouponDefine.NUM_24){
//                // 2018新年活动
//                couponCodeList=actSpringList4;
//                activityId = Integer.valueOf(ACTIVITY_SPRING_ID);
//                couponSource = 2;
//                // 发放优惠券
//                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
//
        //    }
        else if(sendFlg == UserCouponDefine.NUM_25){
                // 上市活动
                couponCodeList=actLiseted2List1;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                // 2活动发放
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }else if(sendFlg == UserCouponDefine.NUM_26){
                // 上市活动
                couponCodeList=actLiseted2List2;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }else if(sendFlg == UserCouponDefine.NUM_27){
                // 上市活动
                couponCodeList=actLiseted2List3;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }else if(sendFlg == UserCouponDefine.NUM_28){
                // 上市活动
                couponCodeList=actLiseted2List4;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_29){
                // 上市活动
                couponCodeList=actLiseted2List5;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_30){
                // 上市活动
                couponCodeList=actLiseted2List6;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_31){
                // 上市活动
                couponCodeList=actLiseted2List7;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_32){
                // 上市活动
                couponCodeList=actLiseted2List8;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_33){
                // 上市活动
                couponCodeList=actLiseted2List9;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_34){
                // 上市活动
                couponCodeList=actLiseted2List10;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_35){
                // 上市活动
                couponCodeList=actLiseted2List11;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_36){
                // 上市活动
                couponCodeList=actLiseted2List12;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_37){
                // 上市活动
                couponCodeList=actLiseted2List13;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);

            }
            else if(sendFlg == UserCouponDefine.NUM_38){
                // 上市活动
                couponCodeList=actLiseted2List14;
                activityId = Integer.valueOf(ACTIVITY_LISTED2_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
                
            }else if (ACTIVITY_518_ID.equals(String.valueOf(sendFlg))) {
            	 // 上市活动act518List
            	couponCodeList.add(act518List.get(Integer.valueOf(paramBean.getRemark())));
                activityId = Integer.valueOf(ACTIVITY_518_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"",retCouponUserCodes);
            }else if(sendFlg == UserCouponDefine.NUM_39){
                // 2018中秋活动
                couponCodeList.add(paramBean.getCouponCode());
                activityId = Integer.valueOf(ActivityDateUtil.MIDAU_ACTIVITY_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"", retCouponUserCodes);
            }else if(sendFlg == UserCouponDefine.NUM_40){
                // 双十一秒杀活动
                couponCodeList.add(paramBean.getCouponCode());
                activityId = Integer.valueOf(ActivityDateUtil.TWOELEVEN_ACTIVITY_ID);
                couponSource = 2;
                // 发放优惠券
                couponCount = this.sendConponAction(couponCodeList, userId, sendFlg, activityId, couponSource,"", retCouponUserCodes);
            }
            retResult.put("couponCode", couponCodeList);
            retResult.put("retCouponUserCodes", retCouponUserCodes);
            retResult.put("status", 0);
            retResult.put("couponCount", couponCount);
            _log.info("发放优惠券：" + couponCount + " 张");
            if (couponCount > 0) {
                _log.info("--------------发放优惠券push消息推送开始-----------------");
                String couponCode = couponCodeList.get(0);
                CouponConfigExample example = new CouponConfigExample();
                example.createCriteria().andCouponCodeEqualTo(couponCode);
                List<CouponConfig> couponCodeLi = this.couponConfigMapper.selectByExample(example);
                Integer couponType = null;
                if(couponCodeLi!=null&&couponCodeLi.size()>0){
                	couponType = couponCodeLi.get(0).getCouponType();
                }
                Map<String, String> param = new HashMap<String, String>();
                param.put("val_number", String.valueOf(couponCount));
                param.put("val_coupon_type",
                		couponType == UserCouponDefine.NUM_TWO ? "加息券"
                                : couponType == UserCouponDefine.NUM_THREE ? "代金券" : "体验金");
                _log.info("用户id：" + userId + " 优惠券类型：" + param.get("val_coupon_type"));
                AppMsMessage appMsMessage =
                        new AppMsMessage(Integer.parseInt(userId), param, null, MessageDefine.APPMSSENDFORUSER,
                                CustomConstants.JYTZ_COUPON_SUCCESS);
                appMsProcesser.gather(appMsMessage);
                _log.info("--------------发放优惠券push消息推送结束------------------");
            }
        } catch (Exception e) {
            LogUtil.errorLog(THIS_CLASS, methodName, e);
            throw e;

        }
        return retResult;

    }
    
    /**
     * 
     * 批量自动发放用户优惠券
     * 
     * @author xj
     * @param paramBean
     * @throws Exception
     */
    @Override
    public synchronized JSONObject batchInsertUserCoupon(BatchUserCouponBean paramBean) throws Exception {
        String methodName = "batchInsertUserCoupon";
        JSONObject retResult = new JSONObject();
        try {
            
        	List<BatchSubUserCouponBean> subeans = JSON.parseArray(paramBean.getUsercoupons(), BatchSubUserCouponBean.class);
            int totalcouponCount = 0;
            int succouponCount = 0;
            
            // 优惠券来源1：手动发放，2：活动发放，3：vip礼包
            int couponSource = 2;
            _log.info("批量发放优惠券开始： 预计" + subeans.size() + " 张");
            if(paramBean.getUsercoupons()!= null && subeans.size() >0){
            	
            	for (BatchSubUserCouponBean userCouponBean : subeans) {
            		
            		 List<String> couponCodeList = userCouponBean.getCouponCode();
            		 Integer activityId = null;
            		 if(StringUtils.isNotBlank(userCouponBean.getActivityId())){
            			 activityId = Integer.parseInt(userCouponBean.getActivityId().trim());
            		 }
//                     String userId = userCouponBean.getUserId();
                     String userName = userCouponBean.getUserName();
                     _log.info("批量发放优惠券当前用户名：" + userName);
                     if(StringUtils.isBlank(userName)){
                    	 continue;
                     }
                     Users user = this.getUserByUserName(userName);
                     _log.info("批量发放优惠券User：" + user);
                     if(user == null){
                    	 continue;
                     }
                     if(couponCodeList ==null || couponCodeList.isEmpty()){
                    	 continue;
                     }
                     
                     totalcouponCount = totalcouponCount+couponCodeList.size();
                    // 发放优惠券
                    int couponCount = this.sendConponAction(couponCodeList, String.valueOf(user.getUserId()), activityId, couponSource);
                    succouponCount = succouponCount + couponCount;
                    _log.info(user.getUserId()+ " 发放优惠券：" + couponCount + " 张");
				}
            	
            }
            
            retResult.put("status", 0);
            retResult.put("totalcouponCount", totalcouponCount);
            retResult.put("couponCount", succouponCount);
            _log.info("发放优惠券：" + totalcouponCount + " 张");
            
            
        } catch (Exception e) {
            LogUtil.errorLog(THIS_CLASS, methodName, e);
            throw e;

        }
        return retResult;

    }
    
    private int sendConponAction(List<String> couponCodeList, String userId, Integer activityId,
            int couponSource) throws Exception {
    	
    	// sendflg设置1跳过活动id不设置的逻辑
    	return sendConponAction(couponCodeList, userId, 1, activityId, couponSource,"上传csv文件，批量发券");
    }
    
    public int sendConponAction(List<String> couponCodeList, String userId, Integer sendFlg, Integer activityId,
        Integer couponSource, String content) throws Exception {
        _log.info("用户："+userId+",执行发券逻辑开始  " + GetDate.dateToString(new Date()));
        String methodName = "sendConponAction";
        int nowTime = GetDate.getNowTime10();
        // String couponGroupCode = CreateUUID.createUUID();
        
        UsersInfo userInfo = this.getUsersInfoByUserId(Integer.parseInt(userId));
        if(userInfo == null){
        	return 0;
        }
        
        String channelName = this.getChannelNameByUserId(Integer.parseInt(userId));
        
        int couponCount = 0;
        if (couponCodeList != null && couponCodeList.size() > 0) {
            for (String couponCode : couponCodeList) {
                // 如果优惠券的发行数量已大于等于配置的发行数量，则不在发放该类别优惠券
                if (!this.checkSendNum(couponCode)) {
                    LogUtil.infoLog(THIS_CLASS, methodName, "优惠券发行数量超出上限，不再发放！");
                    continue;
                }
                CouponUser couponUser = new CouponUser();
                couponUser.setCouponCode(couponCode);
                if (StringUtils.contains(couponCode, "PT")) {
                    // 体验金编号
                    couponUser.setCouponUserCode(GetCode.getCouponUserCode(1));
                } else if (StringUtils.contains(couponCode, "PJ")) {
                    // 加息券编号
                    couponUser.setCouponUserCode(GetCode.getCouponUserCode(2));
                } else if (StringUtils.contains(couponCode, "PD")) {
                    // 代金券编号
                    couponUser.setCouponUserCode(GetCode.getCouponUserCode(3));
                }
                // 优惠券组编号
                // couponUser.setCouponGroupCode(couponGroupCode);
                couponUser.setUserId(Integer.parseInt(userId));
                if (Validator.isNotNull(sendFlg) && sendFlg != UserCouponDefine.NUM_TWO && Validator.isNotNull(activityId)) {
                    // 购买vip与活动无关
                    couponUser.setActivityId(activityId);
                }
                couponUser.setUsedFlag(CustomConstants.USER_COUPON_STATUS_UNUSED);

                // 根据优惠券编码查询优惠券
                CouponConfigExample emConfig = new CouponConfigExample();
                CouponConfigExample.Criteria caConfig = emConfig.createCriteria();
                caConfig.andCouponCodeEqualTo(couponCode);
                List<CouponConfig> configList = couponConfigMapper.selectByExample(emConfig);
                if (configList == null || configList.isEmpty()) {
                    continue;
                }
                CouponConfig config = configList.get(0);

                Integer status = config.getStatus();
                if(status==null||status==1||status==3){
                    LogUtil.infoLog(THIS_CLASS, methodName, "优惠券审核未通过，无法发放！（coupon）"+couponCode);
                    continue;
                }
                // 加息券编号
                couponUser.setCouponUserCode(GetCode.getCouponUserCode(config.getCouponType()));

                if (config.getExpirationType() == 1) { // 截止日
                    couponUser.setEndTime(config.getExpirationDate());
                } else if(config.getExpirationType() == 2) { // 时长
                    couponUser.setEndTime((int) (GetDate.countDate(2, config.getExpirationLength()).getTime() / 1000));
                } else if(config.getExpirationType() == 3){
                	couponUser.setEndTime((int) (GetDate.countDate(5, config.getExpirationLengthDay()).getTime() / 1000));
                }
                couponUser.setCouponSource(couponSource);
                couponUser.setAddTime(nowTime);
                couponUser.setAddUser(CustomConstants.OPERATOR_AUTO_REPAY);
                couponUser.setUpdateTime(nowTime);
                couponUser.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
                couponUser.setDelFlag(CustomConstants.FALG_NOR);
                couponUser.setChannel(channelName);
                couponUser.setAttribute(userInfo.getAttribute());
                couponUser.setContent(StringUtils.isEmpty(content)?"":content);
                couponUserMapper.insertSelective(couponUser);
                couponCount++;
            }
            _log.info("发放优惠券成功，发放张数：" + couponCount);
        }
        _log.info("用户："+userId+",执行发券逻辑结束  " + GetDate.dateToString(new Date()));
        return couponCount;
    }
    
    /**
     * 带返回发放的用户优惠券编号
     * @param couponCodeList
     * @param userId
     * @param sendFlg
     * @param activityId
     * @param couponSource
     * @param retCouponUserCodeList
     * @return
     * @throws Exception
     */
    private int sendConponAction(List<String> couponCodeList, String userId, Integer sendFlg, Integer activityId,
            Integer couponSource,String content, List<String> retCouponUserCodes) throws Exception {
            String methodName = "sendConponAction";
            int nowTime = GetDate.getNowTime10();
            // String couponGroupCode = CreateUUID.createUUID();
            UsersInfo userInfo = this.getUsersInfoByUserId(Integer.parseInt(userId));
            String channelName = this.getChannelNameByUserId(Integer.parseInt(userId));

            int couponCount = 0;
            if (couponCodeList != null) {
                for (String couponCode : couponCodeList) {
                    // 如果优惠券的发行数量已大于等于配置的发行数量，则不在发放该类别优惠券
                    if (!this.checkSendNum(couponCode)) {
                        _log.info("UserCouponServiceImpl sendConponAction 优惠券发行数量超出上限，不再发放！couponCode ：" + couponCode);
                        continue;
                    }
                    couponCount++;
                    CouponUser couponUser = new CouponUser();
                    couponUser.setCouponCode(couponCode);
                    if (StringUtils.contains(couponCode, "PT")) {
                        // 体验金编号
                        couponUser.setCouponUserCode(GetCode.getCouponUserCode(1));
                    } else if (StringUtils.contains(couponCode, "PJ")) {
                        // 加息券编号
                        couponUser.setCouponUserCode(GetCode.getCouponUserCode(2));
                    } else if (StringUtils.contains(couponCode, "PD")) {
                        // 代金券编号
                        couponUser.setCouponUserCode(GetCode.getCouponUserCode(3));
                    }
                    // 优惠券组编号
                    // couponUser.setCouponGroupCode(couponGroupCode);
                    couponUser.setUserId(Integer.parseInt(userId));
                    if (Validator.isNotNull(sendFlg) && sendFlg != UserCouponDefine.NUM_TWO && Validator.isNotNull(activityId)) {
                        // 购买vip与活动无关
                        couponUser.setActivityId(activityId);
                    }
                    couponUser.setUsedFlag(CustomConstants.USER_COUPON_STATUS_UNUSED);

                    // 根据优惠券编码查询优惠券
                    CouponConfigExample emConfig = new CouponConfigExample();
                    CouponConfigExample.Criteria caConfig = emConfig.createCriteria();
                    caConfig.andCouponCodeEqualTo(couponCode);
                    List<CouponConfig> configList = couponConfigMapper.selectByExample(emConfig);
                    if (configList == null || configList.isEmpty()) {
                        continue;
                    }
                    CouponConfig config = configList.get(0);

                    // 加息券编号
                    couponUser.setCouponUserCode(GetCode.getCouponUserCode(config.getCouponType()));

                    if(config.getExpirationType() == 1){ //截止日
                        couponUser.setEndTime(config.getExpirationDate());
                    }else if(config.getExpirationType() == 2){  //时长（月）
                        Date endDate = GetDate.countDate(GetDate.getDate(), 2, config.getExpirationLength());
                        couponUser.setEndTime((int)(endDate.getTime()/1000));
                    }else if(config.getExpirationType() == 3){  //时长（天）
                        Date endDate = GetDate.countDate(GetDate.getDate(), 5, config.getExpirationLengthDay());
                        couponUser.setEndTime((int)(endDate.getTime()/1000));
                    }
                    
                    couponUser.setCouponSource(couponSource);
                    couponUser.setAddTime(nowTime);
                    couponUser.setAddUser(CustomConstants.OPERATOR_AUTO_REPAY);
                    couponUser.setUpdateTime(nowTime);
                    couponUser.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
                    couponUser.setDelFlag(CustomConstants.FALG_NOR);
                    couponUser.setChannel(channelName);
                    couponUser.setAttribute(userInfo.getAttribute());
                    couponUser.setContent(StringUtils.isEmpty(content)?"":content);
                    couponUserMapper.insertSelective(couponUser);
                    // 需要返回的用户优惠券编号
                    retCouponUserCodes.add(couponUser.getCouponUserCode());
                }
                _log.info("发放优惠券成功，发放张数：" + couponCount);
            }
            return couponCount;
        }

    /**
     * 校验优惠券的已发行数量
     * 
     * @return
     */
    private boolean checkSendNum(String couponCode) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("couponCode", couponCode);
        int remain = couponConfigCustomizeMapper.checkCouponSendExcess(param);

        return remain > 0 ? true : false;
    }

    /**
     * 校验是否重复发放
     * 
     * @param couponCodeList
     * @param userId
     * @return
     */
    private boolean checkSendRepeat(List<String> couponCodeList, String userId, int activityId) {
        CouponUserExample couponUserExample = new CouponUserExample();
        CouponUserExample.Criteria criteria = couponUserExample.createCriteria();
        criteria.andCouponCodeIn(couponCodeList);
        criteria.andActivityIdEqualTo(activityId);
        criteria.andUserIdEqualTo(new Integer(userId));
        criteria.andDelFlagEqualTo(0);
        List<CouponUser> couponUserList = this.couponUserMapper.selectByExample(couponUserExample);
        return couponUserList == null || couponUserList.size() == 0 ? true : false;
    }

}
