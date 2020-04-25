package com.hyjf.api.web.plan.coupon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.api.web.vip.apply.ApplyServer;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.invest.AvailableCouponResultBean;
import com.hyjf.invest.CouponInvestResultBean;
import com.hyjf.invest.InvestDefine;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.plan.coupon.PlanAvailableCouponResultBean;
import com.hyjf.plan.coupon.PlanCountCouponUsersResultBean;
import com.hyjf.plan.coupon.PlanCouponBean;
import com.hyjf.plan.coupon.PlanCouponDefine;
import com.hyjf.plan.coupon.PlanCouponService;
import com.hyjf.plan.coupon.PlanUserCouponConfigResultBean;
import com.hyjf.vip.apply.ApplyDefine;

@Controller
@RequestMapping(value = PlanCouponDefine.REQUEST_MAPPING)
public class PlanCouponServer extends BaseController {
	Logger _log = LoggerFactory.getLogger(ApplyServer.class);
    @Autowired
    private PlanCouponService planCouponService;

  
    
    /**
     * 
     * 获取用户优惠券总张数
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PlanCouponDefine.COUNT_COUPON_USERS, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public BaseResultBean countCouponUsers(HttpServletRequest request, HttpServletResponse response,
        PlanCouponBean paramBean) {

        PlanCountCouponUsersResultBean result = new PlanCountCouponUsersResultBean();
        // 检查参数正确性
        if (Validator.isNull(paramBean.getUsedFlag()) ||  Validator.isNull(paramBean.getUserId())) {
            result.setStatus(PlanCountCouponUsersResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        
        
        if(!this.checkSign(paramBean, PlanCouponDefine.METHOD_PLAN_COUNT_COUPON_USERS)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        if (paramBean.getMoney() == null || "".equals(paramBean.getMoney()) || paramBean.getMoney().length() == 0) {
            paramBean.setMoney("0");
        }
        result = planCouponService.countCouponUsers(paramBean);

        return result;
    }
    
    
    /**
     * 
     * 获取用户可用优惠券总张数
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PlanCouponDefine.GET_USER_COUPON_AVAILABLE_COUNT, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public BaseResultBean getUserCouponAvailableCount(HttpServletRequest request, HttpServletResponse response,
        PlanCouponBean paramBean) {

        PlanAvailableCouponResultBean result = new PlanAvailableCouponResultBean();
        // 检查参数正确性
        if (Validator.isNull(paramBean.getPlanNid()) ||  Validator.isNull(paramBean.getUserId())) {
            result.setStatus(PlanCountCouponUsersResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        if(!this.checkSign(paramBean, PlanCouponDefine.METHOD_PLAN_GET_USER_COUPON_AVAILABLE_COUNT)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        int couponAvailableCount = planCouponService.getUserCouponAvailableCount(paramBean.getPlanNid(), paramBean.getUserId(),paramBean.getMoney(), paramBean.getPlatform());
        result.setAvailableCouponListCount(couponAvailableCount);

        result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
        result.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);

        return result;
    }
    
    
    /**
     * 
     * 获取用户最优优惠券信息
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PlanCouponDefine.GET_BEST_COUPON, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public BaseResultBean getBestCoupon(HttpServletRequest request, HttpServletResponse response,
        PlanCouponBean paramBean) {

        PlanUserCouponConfigResultBean result = new PlanUserCouponConfigResultBean();
        // 检查参数正确性
        if (Validator.isNull(paramBean.getPlanNid()) ||  Validator.isNull(paramBean.getUserId())) {
            result.setStatus(PlanCountCouponUsersResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        if(!this.checkSign(paramBean, PlanCouponDefine.METHOD_PLAN_GET_BEST_COUPON)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        UserCouponConfigCustomize couponConfig = planCouponService.getBestCoupon(paramBean.getPlanNid(), paramBean.getUserId(),paramBean.getMoney(), paramBean.getPlatform());

        if(couponConfig!=null&&couponConfig.getUserCouponId()!=null){
            result.setCouponConfigJson(JSONObject.toJSONString(couponConfig));
        }else{
            result.setCouponConfigJson(""); 
        }
        result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
        result.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);

        return result;
    }
    
    /**
     * 
     * 计算优惠券预期收益
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PlanCouponDefine.GET_COUPON_INTEREST, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public BaseResultBean getCouponInterest(HttpServletRequest request, HttpServletResponse response,
        PlanCouponBean paramBean) {

        PlanUserCouponConfigResultBean result = new PlanUserCouponConfigResultBean();
        // 检查参数正确性
        if (Validator.isNull(paramBean.getPlanNid()) ||  Validator.isNull(paramBean.getCouponGrantId())) {
            result.setStatus(PlanCountCouponUsersResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        if(!this.checkSign(paramBean, PlanCouponDefine.METHOD_PLAN_GET_COUPON_INTEREST)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        String couponInterest = planCouponService.getCouponInterest(paramBean.getPlanNid(), paramBean.getCouponGrantId(),paramBean.getMoney());

        result.setCouponInterest(couponInterest);
        result.setStatus(CustomConstants.APP_STATUS_SUCCESS);
        result.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);

        return result;
    }
    
    
    /**
     * 
     * 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PlanCouponDefine.GET_PROJECT_AVAILABLE_USER_COUPON_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public BaseResultBean getProjectAvailableUserCoupon(HttpServletRequest request, HttpServletResponse response,
        PlanCouponBean paramBean) {

        PlanAvailableCouponResultBean result = new PlanAvailableCouponResultBean();
        // 检查参数正确性
        if (Validator.isNull(paramBean.getPlanNid()) ||  Validator.isNull(paramBean.getUserId())) {
            result.setStatus(AvailableCouponResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            return result;
        }
        
        
        if(!this.checkSign(paramBean, InvestDefine.METHOD_PLAN_GET_PROJECT_AVAILABLE_USER_COUPON_ACTION)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        if (paramBean.getMoney() == null || "".equals(paramBean.getMoney()) || paramBean.getMoney().length() == 0) {
            paramBean.setMoney("0");
        }
        result = planCouponService.getProjectAvailableUserCoupon(paramBean);

        return result;
    }
    
    
    /**
     * 优惠券出借校验
     * @param account
     * @param userId 
     * @param couponType
     * @param borrowNid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = InvestDefine.VALIDATE_COUPON_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public BaseResultBean validateCoupon( HttpServletRequest request, HttpServletResponse response,PlanCouponBean paramBean) {
        BaseResultBean baseResultBean=new BaseResultBean();
     // 金额
        String account = paramBean.getMoney();
        String couponGrantId = paramBean.getCouponGrantId();
        String planNid = paramBean.getPlanNid();
        int userId = paramBean.getUserId();
     // 检查参数正确性
        if (Validator.isNull(planNid) ||  Validator.isNull(couponGrantId)) {
            baseResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            baseResultBean.setStatusDesc("请求参数非法");
            return baseResultBean;
        }
        // 汇计划查询项目信息
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
    
    
    /**
     * 体验金出借
     * @param modelAndView
     * @param request
     * @param cuc
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PlanCouponDefine.COUPON_TENDER_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public CouponInvestResultBean couponTender(HttpServletRequest request, HttpServletResponse response,PlanCouponBean paramBean) {
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
        
        if(!this.checkSign(paramBean, PlanCouponDefine.METHOD_PLAN_COUPON_TENDER_ACTION)){
            couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            couponInvestResultBean.setStatusDesc("验签失败！");
            return couponInvestResultBean;
        }
        
        // 出借平台
        String platform = paramBean.getPlatform();
        CouponConfigCustomizeV2 cuc = null;
        if (StringUtils.isNotEmpty(couponGrantId)) {
            cuc = planCouponService.getCouponUser(couponGrantId,userId);
            if(cuc==null||cuc.getUsedFlag()!=0){
                couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                couponInvestResultBean.setStatusDesc("当前优惠券不存在或已使用");
                return couponInvestResultBean;
            }
            // 排他check用
            couponOldTime = cuc.getUserUpdateTime();
        }else{
            couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            couponInvestResultBean.setStatusDesc("优惠券id为空");
            return couponInvestResultBean;
        }
        int couponType = cuc.getCouponType();
        int client = StringUtils.isNotEmpty(platform) ? Integer.valueOf(platform) : 0;
        // 用户优惠券更新时间 排他校验用
        // int orderTime = cuc.getUpdateTime();
        // 优惠券出借校验
        PlanCouponBean bean=createValidateCouponCheckSign(paramBean);
        BaseResultBean baseResultBean = this.validateCoupon(request,response,bean);
        if (BaseResultBean.STATUS_FAIL.equals(baseResultBean.getStatus())) {
            couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            couponInvestResultBean.setStatusDesc(baseResultBean.getStatusDesc());
            return couponInvestResultBean;
        }

        Map<String, Object> retMap = new HashMap<String, Object>();
        // 优惠券出借
        boolean couponSuccess;
        try {
            couponSuccess = planCouponService.updateCouponTender(couponGrantId, planNid, ordDate, userId, account, ip, client,
                    couponOldTime,ordId, retMap);
            if (!couponSuccess) {
                _log.info("选择的优惠券异常，请刷新后重试！");
                couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                couponInvestResultBean.setStatusDesc("选择的优惠券异常，请刷新后重试！");
                return couponInvestResultBean;
            }
        } catch (Exception e) {
            _log.error("选择的优惠券异常，请刷新后重试！",e);
            couponInvestResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            couponInvestResultBean.setStatusDesc("选择的优惠券异常，请刷新后重试！");
            return couponInvestResultBean;
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
        return couponInvestResultBean;
    }
    


    private PlanCouponBean createValidateCouponCheckSign(PlanCouponBean paramBean) {
        String accessKey = PropUtils
                .getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
        PlanCouponBean bean=new PlanCouponBean();
        Integer userId = paramBean.getUserId();
        Long timestamp = paramBean.getTimestamp();
        String planNid = paramBean.getPlanNid();
        String platform = paramBean.getPlatform();
        String couponGrantId = paramBean.getCouponGrantId()==null?"":paramBean.getCouponGrantId();
        String money = paramBean.getMoney()==null?"":paramBean.getMoney();
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + planNid + 
                money + platform + couponGrantId + timestamp + accessKey));
        bean.setUserId(userId);
        bean.setTimestamp(timestamp);
        bean.setPlanNid(planNid);
        bean.setPlatform(platform);
        bean.setCouponGrantId(couponGrantId);
        bean.setMoney(money);
        bean.setChkValue(sign);
        return bean;
    }
    
    /**
     * 
     * 更新汇添金优惠券收益及还款时间
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PlanCouponDefine.COUPON_RECOVER_HTJ_ACTION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public void updateCouponRecoverHtj(HttpServletRequest request, HttpServletResponse response,
        PlanCouponBean paramBean) {

        PlanCountCouponUsersResultBean result = new PlanCountCouponUsersResultBean();
        // 检查参数正确性
        if (Validator.isNull(paramBean.getPlanNid())) {
            result.setStatus(PlanCountCouponUsersResultBean.STATUS_FAIL);
            result.setStatusDesc("请求参数非法");
            //return result;
        }
        
        
        if(!this.checkSign(paramBean, PlanCouponDefine.METHOD_PLAN_COUPON_RECOVER)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            //return result;
        }
        try{
            System.out.println("*******计划编号：" + paramBean.getPlanNid()+"还款开始******************");
            // 更新优惠券还款收益及时间
            this.planCouponService.updateCouponRecoverHtj(paramBean.getPlanNid());
            System.out.println("*******计划编号：" + paramBean.getPlanNid()+"还款结束******************");
        }catch(Exception e){
            _log.info("汇添金计划优惠券更新优惠券收益失败，计划编号："+paramBean.getPlanNid());
        }
        
    }
    
    
}
