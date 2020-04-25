package com.hyjf.mqreceiver.coupon.tender;

import java.math.BigDecimal;
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
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.invest.CouponInvestResultBean;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.plan.coupon.PlanCouponBean;
import com.hyjf.plan.coupon.PlanCouponService;
import com.rabbitmq.client.Channel;

public class HjhCouponTenderMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(HjhCouponTenderMessageHadnle.class);

    @Autowired
    private PlanCouponService planCouponService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------直投使用优惠券开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【直投使用优惠券】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【直投使用优惠券】接收到的消息：" + msgBody);
        
        PlanCouponBean paramBean;
        try {
            paramBean = JSONObject.parseObject(msgBody, PlanCouponBean.class);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(paramBean.getMoney()) && Validator.isNull(paramBean.getCouponGrantId())
                && Validator.isNull(paramBean.getPlanNid()) && Validator.isNull(paramBean.getUserId()) 
                && Validator.isNull(paramBean.getCouponOldTime())) {
            _log.error("【直投使用优惠券】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String hjhRedisKey = "hjhcoupontender:" + paramBean.getCouponGrantId();
        String hztRedisKey = "hztcoupontender:" + paramBean.getCouponGrantId();
        boolean hjhResult = RedisUtils.tranactionSet(hztRedisKey, 300);
        boolean hztResult = RedisUtils.tranactionSet(hjhRedisKey, 300);
        if(!hztResult||!hjhResult){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("当前优惠券正在使用....");
            return;
        }
        CouponInvestResultBean couponInvestResultBean=new  CouponInvestResultBean();
        // 下单时间
        String ordDate = GetDate.getServerDateTime(1, new Date());
        // 金额
        String account = paramBean.getMoney();
        String ip = paramBean.getIp();
        String couponGrantId = paramBean.getCouponGrantId();
        String planNid = paramBean.getPlanNid();
        String ordId = paramBean.getOrdId();
        int userId = paramBean.getUserId();
        int couponOldTime = paramBean.getCouponOldTime();
        
        // 出借平台
        String platform = paramBean.getPlatform();
        CouponConfigCustomizeV2 cuc = null;
        if (StringUtils.isNotEmpty(couponGrantId)) {
            cuc = planCouponService.getCouponUser(couponGrantId,userId);
            if(cuc==null||cuc.getUsedFlag()!=0){
                _log.error("【直投使用优惠券】当前优惠券不存在或已使用");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
            // 排他check用
            couponOldTime = cuc.getUserUpdateTime();
        }else{
            _log.error("【直投使用优惠券】优惠券id为空");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        int couponType = cuc.getCouponType();
        int client = StringUtils.isNotEmpty(platform) ? Integer.valueOf(platform) : 0;
        // 用户优惠券更新时间 排他校验用
        // int orderTime = cuc.getUpdateTime();
        BaseResultBean baseResultBean = this.validateCoupon(paramBean);
        if (BaseResultBean.STATUS_FAIL.equals(baseResultBean.getStatus())) {
            _log.error("【直投使用优惠券】"+baseResultBean.getStatusDesc());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        Map<String, Object> retMap = new HashMap<String, Object>();
        // 优惠券出借
        boolean couponSuccess;
        try {
            couponSuccess = planCouponService.updateCouponTender(couponGrantId, planNid, ordDate, userId, account, ip, client,
                    couponOldTime,ordId, retMap);
            if (!couponSuccess) {
                _log.error("【直投使用优惠券】选择的优惠券异常，请刷新后重试！");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        } catch (Exception e) {
            _log.error("【直投使用优惠券】选择的优惠券异常，请刷新后重试！Exception");
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

        couponInvestResultBean.setAccountDecimal(couponAccount.toString());
        // 优惠券收益
        couponInvestResultBean.setCouponInterest(retMap.get("couponInterest").toString());
        // 优惠券类别
        String couponTypeString="";
        if(couponTypeInt==1){
            couponTypeString="体验金";
        } else if(couponTypeInt==2){
            couponTypeString="加息券";
        } else if(couponTypeInt==3){
            couponTypeString="代金券";
        }
        couponInvestResultBean.setCouponTypeInt(couponTypeInt+"");
        couponInvestResultBean.setCouponType(couponTypeString);
        // 优惠券额度
        couponInvestResultBean.setCouponQuota(couponQuota.toString());;
        // 跳转到成功画面
//        modelAndView.setViewName(InvestDefine.INVEST_SUCCESS_PATH);
        couponInvestResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        couponInvestResultBean.setStatusDesc("");
        _log.info("【直投使用优惠券】优惠券使用信息：" + JSONObject.toJSON(couponInvestResultBean));
         channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
         RedisUtils.del(hjhRedisKey);
         RedisUtils.del(hztRedisKey);
        _log.info("----------------------------直投优惠券使用结束--------------------------------" + this.toString());
    }
    
    
    
    public BaseResultBean validateCoupon(PlanCouponBean paramBean) {
        BaseResultBean baseResultBean=new BaseResultBean();
     // 金额
        String account = paramBean.getMoney();
        String couponGrantId = paramBean.getCouponGrantId();
        String planNid = paramBean.getPlanNid();
        int userId = paramBean.getUserId();

        // 直投查询项目信息
        HjhPlan hjhPlan=this.planCouponService.getHjhPlanByPlanNid(planNid);
        // 出借平台
        String platform = paramBean.getPlatform();
        
        
        CouponConfigCustomizeV2 couponConfig=null;
        if (StringUtils.isNotEmpty(couponGrantId)) {
            couponConfig = planCouponService.getCouponUser(couponGrantId,userId);
            if(couponConfig==null||couponConfig.getUsedFlag()!=0){
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("当前优惠券不存在或已使用");
                return baseResultBean;
            }
        }else{
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("优惠券id为空");
            return baseResultBean;
        }
        
        
        String config = hjhPlan.getCouponConfig();
         // 验证项目加息券或体验金是否可用
            if(couponConfig.getCouponType() == 1){
                if(config.indexOf("1") == -1){
                    baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                    baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                    return baseResultBean;
                }
            }else if(couponConfig.getCouponType() == 2){
                if(config.indexOf("2") == -1){
                    baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                    baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                    return baseResultBean;
                }
            }else if(couponConfig.getCouponType() == 3){
                if(config.indexOf("3") == -1){
                    baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                    baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                    return baseResultBean;
                }
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
        // 项目类型 pcc20160715
        
        if (couponConfig.getProjectType().indexOf("6")==-1) {

            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("对不起，您选择的优惠券不能用于当前类别标的");
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

        // 计划期限
        int planPeriod = hjhPlan.getLockPeriod();
        int couponExType = couponConfig.getProjectExpirationType();
        int expirationLength =
                couponConfig.getProjectExpirationLength() == null ? 0 : couponConfig.getProjectExpirationLength();
        int expirationMin =
                couponConfig.getProjectExpirationLengthMin() == null ? 0 : couponConfig.getProjectExpirationLengthMin();
        int expirationMax =
                couponConfig.getProjectExpirationLengthMax() == null ? 0 : couponConfig.getProjectExpirationLengthMax();

        // 等于
        if (couponExType == 1) {
            if (expirationLength != planPeriod) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        } else if (couponExType == 2) {
            // 期限范围
            if (planPeriod < expirationMin || planPeriod > expirationMax) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        } else if (couponExType == 3) {
            // 大于等于
            if (planPeriod < expirationLength) {
                baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                baseResultBean.setStatusDesc("您选择的优惠券不满足使用条件，请核对后重新选择！");
                return baseResultBean;
            }
        } else if (couponExType == 4) {
            // 小于等于
            if (planPeriod > expirationLength) {
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
        
        
        if (couponConfig.getAddFlg()==1&&!"0".equals(account)) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("当前优惠券不能与本金公用");
            return baseResultBean;
        }
        
        baseResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        baseResultBean.setStatusDesc("");
        return baseResultBean;
    }
    
}
