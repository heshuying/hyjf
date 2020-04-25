package com.hyjf.mqreceiver.coupon.tender;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.invest.CouponInvestResultBean;
import com.hyjf.invest.InvestBean;
import com.hyjf.invest.InvestService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.rabbitmq.client.Channel;

public class HztCouponTenderMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(HztCouponTenderMessageHadnle.class);

    @Autowired
    private InvestService investService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------汇直投使用优惠券开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【汇直投使用优惠券】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【汇直投使用优惠券】接收到的消息：" + msgBody);
        
        InvestBean investBean;
        try {
            investBean = JSONObject.parseObject(msgBody, InvestBean.class);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(investBean.getMoney()) && Validator.isNull(investBean.getCouponGrantId())
                && Validator.isNull(investBean.getBorrowNid()) && Validator.isNull(investBean.getUserId()) 
                && Validator.isNull(investBean.getCouponOldTime())) {
            _log.error("【汇直投使用优惠券】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String hjhRedisKey = "hjhcoupontender:" + investBean.getCouponGrantId();
        String hztRedisKey = "hztcoupontender:" + investBean.getCouponGrantId();
        boolean hjhResult = RedisUtils.tranactionSet(hztRedisKey, 300);
        boolean hztResult = RedisUtils.tranactionSet(hjhRedisKey, 300);
        if(!hztResult||!hjhResult){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("当前优惠券正在使用....");
            return;
        }
        // 下单时间
        String ordDate = GetDate.getServerDateTime(1, new Date());
        // 金额
        String account = investBean.getMoney();
        String ip = investBean.getIp();
        String couponGrantId = investBean.getCouponGrantId();
        String borrowNid = investBean.getBorrowNid();
        String ordId = investBean.getOrdId();
        int userId = investBean.getUserId();
        int couponOldTime = investBean.getCouponOldTime();
        
        // 根据项目编号获取相应的项目
        Borrow borrow = investService.getBorrowByNid(borrowNid);
        // 出借平台
        String platform = investBean.getPlatform();
        CouponConfigCustomizeV2 cuc = null;
//      BigDecimal couponQuota = null;
        if (StringUtils.isNotEmpty(couponGrantId)) {
            cuc = investService.getCouponUser(couponGrantId, userId);
            if (cuc == null || cuc.getUsedFlag() != 0) {
                _log.error("【汇直投使用优惠券】当前优惠券不存在或已使用");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
//          couponQuota = cuc.getCouponQuota();
            // 排他check用
            couponOldTime = cuc.getUserUpdateTime();
        } else {
            _log.error("【汇直投使用优惠券】优惠券id为空");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        int couponType = cuc.getCouponType();
        int client = StringUtils.isNotEmpty(platform) ? Integer.valueOf(platform) : 0;
        // 用户优惠券更新时间 排他校验用
        // int orderTime = cuc.getUpdateTime();
        // 优惠券出借校验
        BaseResultBean baseResultBean = this.validateCoupon(investBean);
        if (BaseResultBean.STATUS_FAIL.equals(baseResultBean.getStatus())) {
            _log.error("【汇直投使用优惠券】"+baseResultBean.getStatusDesc());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        Map<String, Object> retMap = new HashMap<String, Object>();
        // 优惠券出借
        boolean couponSuccess;
        try {
            couponSuccess = investService.updateCouponTender(couponGrantId, borrowNid, ordDate, userId, account, ip,
                    client, couponOldTime, ordId, retMap);
            if (!couponSuccess) {
                _log.error("【汇直投使用优惠券】选择的优惠券异常，请刷新后重试！");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        } catch (Exception e) {
            _log.error("【汇直投使用优惠券】选择的优惠券异常，请刷新后重试！Exception");
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        // 优惠券出借额度
        BigDecimal couponAccount = (BigDecimal) retMap.get("couponAccount");
        // 优惠券类别
        int couponTypeInt = Integer.valueOf(couponType);
        // 优惠券面值
        BigDecimal couponQuota = new BigDecimal(retMap.get("couponQuota").toString());
        // 优惠券收益
        String couponInterest = null;
        BigDecimal earnings = null;
        BigDecimal borrowApr = new BigDecimal(0);
        if (couponTypeInt == 2) {//add by cywang 加息券时,计算利率根据优惠券面值
            borrowApr = cuc.getCouponQuota();//borrow.getBorrowApr(); modify by cwyang 2017-5-10 计算优惠券的收益的时候应该使用优惠券的面值进行计算
        }else{
            _log.info("================pcc 体验金和代金券的利率有标的决定========");
            borrowApr = borrow.getBorrowApr();
        }
        BigDecimal djBorrowApr = borrow.getBorrowApr();
        _log.info("==============pcc 优惠券出借利率是 " + borrowApr);
        Integer borrowPeriod = borrow.getBorrowPeriod();
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        if (couponTypeInt == 1) {//体验金的预期收益
            couponInterest = df.format(getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), djBorrowApr));
        } else {
            System.out.println("============cwyang borrow.getBorrowStyle() is " + borrow.getBorrowStyle());
            switch (borrow.getBorrowStyle()) {
            case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
                // 计算预期收益
                earnings = DuePrincipalAndInterestUtils.getMonthInterest(couponAccount,
                        borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                couponInterest = df.format(earnings);
                break;
            case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
                earnings = DuePrincipalAndInterestUtils.getDayInterest(couponAccount,
                        borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                couponInterest = df.format(earnings);
                break;
            case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
                earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(couponAccount,
                        borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
                        BigDecimal.ROUND_DOWN);
                couponInterest = df.format(earnings);
                break;
            case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
                earnings = AverageCapitalPlusInterestUtils.getInterestCount(couponAccount,
                        borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                couponInterest = df.format(earnings);
                break;
            case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
                earnings = AverageCapitalUtils.getInterestCount(couponAccount, borrowApr.divide(new BigDecimal("100")),
                        borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                couponInterest = df.format(earnings);
                break;
            default:
                break;
            }
        }
        CouponInvestResultBean couponInvestResultBean = new CouponInvestResultBean();
        couponInvestResultBean.setAccountDecimal(couponAccount.toString()); 
        // 优惠券收益
        couponInvestResultBean.setCouponInterest(couponInterest);
        _log.info("===================pcc 优惠券预期收益为: " + couponInterest);
        System.out.println("===================cwyang 优惠券预期收益为: " + couponInterest);
        // 优惠券类别
        String couponTypeString = "";
        if (couponTypeInt == 1) {
            couponTypeString = "体验金";
        } else if (couponTypeInt == 2) {
            couponTypeString = "加息券";
        } else if (couponTypeInt == 3) {
            couponTypeString = "代金券";
        }
        couponInvestResultBean.setCouponTypeInt(couponTypeInt + "");
        couponInvestResultBean.setCouponType(couponTypeString);
        // 优惠券额度
        couponInvestResultBean.setCouponQuota(couponQuota.toString());
        ;
        // 跳转到成功画面
        // modelAndView.setViewName(InvestDefine.INVEST_SUCCESS_PATH);
        couponInvestResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        couponInvestResultBean.setStatusDesc("");
        
        _log.info("优惠券使用信息：" + JSONObject.toJSON(couponInvestResultBean));
         channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
         RedisUtils.del(hjhRedisKey);
         RedisUtils.del(hztRedisKey);
         
        _log.info("----------------------------汇直投优惠券使用结束--------------------------------" + this.toString());
    }
    
    
    
    public BaseResultBean validateCoupon(InvestBean investBean) {
        BaseResultBean baseResultBean = new BaseResultBean();
        // 金额
        String account = investBean.getMoney();
        String couponGrantId = investBean.getCouponGrantId();
        String borrowNid = investBean.getBorrowNid();
        int userId = investBean.getUserId();

        
        // 出借平台
        String platform = investBean.getPlatform();


        CouponConfigCustomizeV2 couponConfig = null;
        if (StringUtils.isNotEmpty(couponGrantId)) {
            couponConfig = investService.getCouponUser(couponGrantId, userId);
        }

        if (StringUtils.isNotEmpty(couponGrantId)) {
            couponConfig = investService.getCouponUser(couponGrantId, userId);
            if (couponConfig == null || couponConfig.getUsedFlag() != 0) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("当前优惠券不存在或已使用");
                return baseResultBean;
            }
        } else {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("优惠券id为空");
            return baseResultBean;
        }
        // 根据项目编号获取相应的项目
        Borrow borrow = investService.getBorrowByNid(borrowNid);
        // 加息券标识（0：禁用，1：可用）
        int interestCoupon = borrow.getBorrowInterestCoupon();
        // 体验金标识（0：禁用，1：可用）
        int moneyCoupon = borrow.getBorrowTasteMoney();
        // CouponType(1：体验金，2：加息券)
        if (couponConfig.getCouponType() != 3&&((interestCoupon == 0 && moneyCoupon == 0)
                || (interestCoupon == 1 && moneyCoupon != 1 && couponConfig.getCouponType() == 1)
                || (interestCoupon != 1 && moneyCoupon == 1 && couponConfig.getCouponType() == 2))) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
            return baseResultBean;
        }
        // 取得优惠券配置
        if (couponConfig.getUsedFlag() != 0) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("您选择的券已经使用，请重新选择优惠券！");
            return baseResultBean;
        }
        // 加息券不能单独出借
        if ((StringUtils.isEmpty(account) || StringUtils.equals(account, "0")) && couponConfig.getCouponType() == 2) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("加息券出借，真实出借金额不能为空！");
            return baseResultBean;
        }

        // 操作系统
        if (!StringUtils.contains(couponConfig.getCouponSystem(), platform)) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("对不起，当前平台不能使用此优惠券！");
            return baseResultBean;
        }

        String projectType = String.valueOf(borrow.getProjectType());

        String msg = investService.validateCouponProjectType(couponConfig.getProjectType(), projectType);

        if (msg != null && msg.length() != 0) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc(msg);
            return baseResultBean;
        }
        // 项目金额
        // 金额类别
        int tenderQuotaType = couponConfig.getTenderQuotaType();
        // 出借金额
        BigDecimal tenderAccount = new BigDecimal(account);
        // 金额范围
        if (tenderQuotaType == 1) {
            // 出借金额最小额度
            BigDecimal tenderQuotaMin = new BigDecimal(couponConfig.getTenderQuotaMin());
            // 出借金额最大额度
            BigDecimal tenderQuotaMax = new BigDecimal(couponConfig.getTenderQuotaMax());
            // 比较出借金额（-1表示小于,0是等于,1是大于）
            int minCheck = tenderAccount.compareTo(tenderQuotaMin);
            int maxCheck = tenderAccount.compareTo(tenderQuotaMax);
            if (minCheck == -1 || maxCheck == 1) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        } else if (tenderQuotaType == 2) {
            // 大于等于
            BigDecimal tenderQuota = new BigDecimal(couponConfig.getTenderQuota());
            // 比较出借金额（-1表示小于,0是等于,1是大于）
            int chkFlg = tenderAccount.compareTo(tenderQuota);
            if (chkFlg == -1) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }

        }
        // 项目期限
        int borrowPeriod = borrow.getBorrowPeriod();
        int couponExType = couponConfig.getProjectExpirationType();
        int expirationLength = couponConfig.getProjectExpirationLength() == null ? 0 : couponConfig
                .getProjectExpirationLength();
        int expirationMin = couponConfig.getProjectExpirationLengthMin() == null ? 0 : couponConfig
                .getProjectExpirationLengthMin();
        int expirationMax = couponConfig.getProjectExpirationLengthMax() == null ? 0 : couponConfig
                .getProjectExpirationLengthMax();
        if (StringUtils.equals(borrow.getBorrowStyle(), CustomConstants.BORROW_STYLE_ENDDAY)) {
            expirationLength = expirationLength * 30;
            expirationMin = expirationMin * 30;
            expirationMax = expirationMax * 30;
        }
        // 等于
        if (couponExType == 1) {
            if (expirationLength != borrowPeriod) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        } else if (couponExType == 2) {
            // 期限范围
            if (borrowPeriod < expirationMin || borrowPeriod > expirationMax) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        } else if (couponExType == 3) {
            // 大于等于
            if (borrowPeriod < expirationLength) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        } else if (couponExType == 4) {
            // 小于等于
            if (borrowPeriod > expirationLength) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        }
        // 截止时间
        // yyyy-MM-dd 的时间戳
        int nowTime = GetDate.strYYYYMMDD2Timestamp2(GetDate.getDataString(GetDate.date_sdf));
        if (couponConfig.getEndTime() < nowTime) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("当前优惠券无法使用，优惠券已过期");
            return baseResultBean;
        }
        baseResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        baseResultBean.setStatusDesc("");
        return baseResultBean;
    }
    
    //计算代金券收益
    private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
        BigDecimal earnings = new BigDecimal("0");

        earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
                .divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
                .setScale(2, BigDecimal.ROUND_DOWN);

        return earnings;

    }
}
