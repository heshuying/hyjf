package com.hyjf.api.web.coupon.mycoupon;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.coupon.mycoupon.CouponBean;
import com.hyjf.coupon.mycoupon.CouponDefine;
import com.hyjf.coupon.mycoupon.CouponDetailResultBean;
import com.hyjf.coupon.mycoupon.CouponResultBean;
import com.hyjf.coupon.mycoupon.CouponService;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserForAppCustomize;

@Controller
@RequestMapping(value = CouponDefine.REQUEST_MAPPING)
public class CouponServer extends BaseController {

    @Autowired
    private CouponService couponService;
    
    /**
     * 
     * 获取我的优惠券列表
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.GET_USERCOUPON)
    public CouponResultBean getUserCoupons(@ModelAttribute CouponBean couponBean, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), CouponDefine.GET_USERCOUPON);
    	CouponResultBean resultBean = new CouponResultBean();
        
        //验证请求参数
        if (Validator.isNull(couponBean.getUserId()) || Validator.isNull(couponBean.getCouponStatus())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(couponBean, BaseDefine.METHOD_COUPON_USER_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        createCouponListPage(resultBean, couponBean.getCouponStatus(), couponBean.getPage(), couponBean.getPageSize(), couponBean.getUserId(), couponBean.getHost());
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), CouponDefine.GET_USERCOUPON);
        return resultBean;
    }

    /**
     * 
     * 查询优惠券页面方法
     * @author hsy
     * @param ret
     * @param couponStatus
     * @param page
     * @param pageSize
     * @param userId
     */
    private void createCouponListPage(CouponResultBean resultBean, String couponStatus, Integer page, Integer pageSize,
        Integer userId, String host) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("usedFlag", couponStatus);
        paraMap.put("userId", userId);
        if(StringUtils.isEmpty(host)){
            paraMap.put("host", CouponDefine.HOST + CouponDefine.REQUEST_MAPPING
                    + CouponDefine.GET_USERCOUPON_DETAIL);
        }else{
            paraMap.put("host", host);
        }
        
        Integer recordTotal = couponService.countCouponUsers(paraMap);
        if (recordTotal > 0) {
            // 组织分页数据
            int offSet = (page - 1) * pageSize;
            if (offSet == 0 || offSet > 0) {
                paraMap.put("limitStart", offSet);
            }
            if (pageSize > 0) {
                paraMap.put("limitEnd", pageSize);
            }
            List<CouponUserForAppCustomize> recordList = couponService.getCouponUserList(paraMap);
            resultBean.setData(recordList);
            resultBean.setCouponTotal(String.valueOf(recordTotal));
            resultBean.setCouponStatus(couponStatus);
            
            //拉取完数据更新用户优惠券已读状态
            if(couponStatus.equals(String.valueOf(CustomConstants.USER_COUPON_STATUS_UNUSED)) && !recordList.isEmpty()){
                couponService.updateCouponReadFlag(userId, CustomConstants.USER_COUPON_READ_FLAG_YES);
            }
        } else {
            resultBean.setData(new ArrayList<CouponUserCustomize>());
            resultBean.setCouponTotal("0");
            resultBean.setCouponStatus(couponStatus);
        }
    }
    
    /**
     * 
     * 优惠券详情页
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.GET_USERCOUPON_DETAIL)
    public CouponDetailResultBean getUserCouponDetail(@ModelAttribute CouponBean couponBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), CouponDefine.GET_USERCOUPON_DETAIL);
        CouponDetailResultBean resultBean = new CouponDetailResultBean();
        
        //请求参数校验
        if(Validator.isNull(couponBean.getId())){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(couponBean, BaseDefine.METHOD_COUPON_USER_DETAIL)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        Map<String,Object> paramMap = new HashMap<String,Object>();

        paramMap.put("couponUserId", couponBean.getId());
        CouponTenderDetailCustomize detail=new CouponTenderDetailCustomize();
        detail=couponService.getUserCouponDetail(paramMap);
        
        //未检索到记录
        if(detail == null){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("未检索到记录");
            return resultBean;
        }
        
        BigDecimal receivedMoney = new BigDecimal("0");
        List<CouponRecoverCustomize> list=
                couponService.getUserCouponRecover(paramMap);
        
        //计算已得收益
        if(list != null && !list.isEmpty()){
            for(CouponRecoverCustomize recover: list){
                if(StringUtils.isNotEmpty(recover.getReceivedFlg()) && recover.getReceivedFlg().equals("已领取") && StringUtils.isNotEmpty(recover.getRecoverInterest())){
                    receivedMoney = receivedMoney.add(new BigDecimal(recover.getRecoverInterest()));
                }
            }
        }
        DecimalFormat format = new DecimalFormat("########0.00");
        LogUtil.infoLog(this.getClass().getName(), "getUserCouponDetail", "已得收益：" + format.format(receivedMoney));
        
        String borrowAprOriginal;
        String borrowPeriodOriginal;
        String borrowStyle;
        
        //计算预期收益
        BigDecimal earnings = new BigDecimal("0");
        if((!StringUtils.isEmpty(detail.getBorrowTenderStatus()) && detail.getBorrowTenderStatus().equals("0")) 
        		|| (!StringUtils.isEmpty(detail.getOrderStatus()) && (detail.getOrderStatus().equals("0") || detail.getOrderStatus().equals("2")))){
        	if(detail.getTenderType().equals("3")){
            	borrowAprOriginal = detail.getExpectApr();
            	borrowPeriodOriginal = detail.getLockPeriod();
            	borrowStyle = detail.getPlanStyle();
            }else{
            	borrowAprOriginal = detail.getBorrowAprOriginal();
            	borrowPeriodOriginal = detail.getBorrowPeriodOriginal();
            	borrowStyle = detail.getBorrowStyle();
            }
        	
        	//出借中计算预期收益
            if(borrowStyle.equalsIgnoreCase(CalculatesUtil.STYLE_END)){ 
                //还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
                if(detail.getCouponType().equals("1")){
                    //体验金
                    earnings = DuePrincipalAndInterestUtils
                            .getMonthInterest(new BigDecimal(detail.getCouponQuotaOriginal()), new BigDecimal(borrowAprOriginal), Integer.parseInt(borrowPeriodOriginal))
                            .divide(new BigDecimal("100"));
                }else if(detail.getCouponType().equals("2")){
                    //加息券
                    earnings = DuePrincipalAndInterestUtils
                            .getMonthInterest(new BigDecimal(detail.getAccount()), new BigDecimal(detail.getCouponQuotaOriginal()), Integer.parseInt(borrowPeriodOriginal))
                            .divide(new BigDecimal("100"));
                }else if(detail.getCouponType().equals("3")){
                    //代金券
                    earnings = DuePrincipalAndInterestUtils
                            .getMonthInterest(new BigDecimal(detail.getCouponQuotaOriginal()), new BigDecimal(borrowAprOriginal), Integer.parseInt(borrowPeriodOriginal))
                            .divide(new BigDecimal("100")).add(new BigDecimal(detail.getCouponQuotaOriginal()));
                }
            }
            else if(borrowStyle.equalsIgnoreCase(CalculatesUtil.STYLE_ENDDAY)){
                // 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
                if(detail.getCouponType().equals("1")){
                    //体验金
                    earnings =
                            DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(detail.getCouponQuotaOriginal()),
                                    new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("2")){
                    //加息券
                    earnings =
                            DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(detail.getAccount()),
                                    new BigDecimal(detail.getCouponQuotaOriginal()).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("3")){
                    //代金券
                    earnings =
                            DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(detail.getCouponQuotaOriginal()),
                                    new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN).add(new BigDecimal(detail.getCouponQuotaOriginal()));
                }
                
            }
            else if(borrowStyle.equalsIgnoreCase(CalculatesUtil.STYLE_ENDMONTH)){
                // 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
                if(detail.getCouponType().equals("1")){
                    //体验金
                    earnings =
                            BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(detail.getCouponQuotaOriginal()),
                                    new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("2")){
                    //加息券
                    earnings =
                            BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(detail.getAccount()),
                                    new BigDecimal(detail.getCouponQuotaOriginal()).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("3")){
                    //代金券
                    earnings =
                            BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(detail.getCouponQuotaOriginal()),
                                    new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN).add(new BigDecimal(detail.getCouponQuotaOriginal()));
                }
            }
            else if(borrowStyle.equalsIgnoreCase(CalculatesUtil.STYLE_MONTH)){
                // 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
                if(detail.getCouponType().equals("1")){
                    //体验金
                    earnings =
                            AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(detail.getCouponQuotaOriginal()),
                                    new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("2")){
                    //加息券
                    earnings =
                            AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(detail.getAccount()),
                                    new BigDecimal(detail.getCouponQuotaOriginal()).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("3")){
                    //代金券
                    earnings =
                            AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(detail.getCouponQuotaOriginal()),
                                    new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,
                                    BigDecimal.ROUND_DOWN).add(new BigDecimal(detail.getCouponQuotaOriginal()));
                }
            }
            else if(borrowStyle.equalsIgnoreCase(CalculatesUtil.STYLE_PRINCIPAL)){
                // 还款方式为”等额本金“
                if(detail.getCouponType().equals("1")){
                    //体验金
                    earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(detail.getCouponQuotaOriginal()),new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("2")){
                    //加息券
                    earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(detail.getAccount()),new BigDecimal(detail.getCouponQuotaOriginal()).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,BigDecimal.ROUND_DOWN);
                }else if(detail.getCouponType().equals("3")){
                    //代金券
                    earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(detail.getCouponQuotaOriginal()),new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(borrowPeriodOriginal)).setScale(2,BigDecimal.ROUND_DOWN).add(new BigDecimal(detail.getCouponQuotaOriginal()));
                }
            }
            else{
                earnings = BigDecimal.ZERO;
            }
            
            // 体验金都是按天计息
            if(detail.getCouponType().equals("1")){
                earnings =
                        DuePrincipalAndInterestUtils.getDayInterestExperience(new BigDecimal(detail.getCouponQuotaOriginal()),
                                new BigDecimal(borrowAprOriginal).divide(new BigDecimal("100")), Integer.parseInt(detail.getCouponProfitTimeOrgin())).setScale(2,
                                BigDecimal.ROUND_DOWN);
            }
        }
        
        //操作平台
        List<ParamName> clients=this.couponService.getParamNameList("CLIENT");
        
        //被选中操作平台
        String clientString = "";
        String clientSed[] = StringUtils.split(detail.getCouponSystem(), ",");
        for(int i=0 ; i< clientSed.length;i++){
            if("-1".equals(clientSed[i])){
                clientString=clientString+"不限";
                break;
            }else{
                for (ParamName paramName : clients) {
                    if(clientSed[i].equals(paramName.getNameCd())){
                        if(i!=0&&clientString.length()!=0){
                            clientString=clientString+"/";
                        }
                        clientString=clientString+paramName.getName();
                        
                    }
                }
            }
        }
        detail.setCouponSystem(clientString);
        
        
        //被选中项目类型   pcc20160715
        String projectString = "";
        String projectSed[] = StringUtils.split(detail.getProjectType(), ",");

         if(detail.getProjectType().indexOf("-1")!=-1){
             projectString="所有散标/新手/智投项目";
           }else{
               projectString=projectString+"所有";
               for (String project : projectSed) {
                    if("1".equals(project)){
                        projectString=projectString+"散标/";
                    }  
                    if("2".equals(project)){
                        projectString=projectString+"";
                    } 
                    if("3".equals(project)){
                        projectString=projectString+"新手/";
                    } 
                    if("4".equals(project)){
                        projectString=projectString+"";
                    } 
                    if("5".equals(project)){
                        projectString=projectString+"";
                    }
                    if("6".equals(project)){
                        projectString=projectString+"智投/";
                    }
               }
               projectString = StringUtils.removeEnd(
                       projectString, "/");
               projectString=projectString+"项目";
        }
        detail.setProjectType(projectString);
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        resultBean.setDetail(detail);
        resultBean.setCouponRecoverlist(list);
        resultBean.setEarnings(format.format(earnings));
        resultBean.setReceivedMoney(format.format(receivedMoney));
        
        LogUtil.endLog(this.getClass().getName(), CouponDefine.GET_USERCOUPON_DETAIL);
        return resultBean;
    }
    
    

}
