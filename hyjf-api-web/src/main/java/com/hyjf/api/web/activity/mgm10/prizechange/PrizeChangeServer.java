package com.hyjf.api.web.activity.mgm10.prizechange;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.activity.mgm10.prizechange.PrizeChangeBean;
import com.hyjf.activity.mgm10.prizechange.PrizeChangeCheckResultBean;
import com.hyjf.activity.mgm10.prizechange.PrizeChangeDefine;
import com.hyjf.activity.mgm10.prizechange.PrizeChangeResultBean;
import com.hyjf.activity.mgm10.prizechange.PrizeChangeService;
import com.hyjf.activity.mgm10.prizechange.PrizeListResultBean;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

@Controller
@RequestMapping(value = PrizeChangeDefine.REQUEST_MAPPING)
public class PrizeChangeServer extends BaseController{

    @Autowired
    private PrizeChangeService prizeChangeService;
    
    /**
     * 
     * 获取奖品兑换的奖品列表
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeChangeDefine.GET_PRIZELIST)
    public PrizeListResultBean getPrizeChangeList(@ModelAttribute PrizeChangeBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getPrizeChangeList";
        LogUtil.startLog(this.getClass().getName(), methodName);
        PrizeListResultBean resultBean = new PrizeListResultBean();
        
        //验证请求参数
//        if (Validator.isNull(form.getUserId())) {
//            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("请求参数非法");
//            return resultBean;
//        }
        //验签
//        if(!this.checkSign(form, BaseDefine.METHOD_PRIZE_CHANGE_LIST)){
//            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("验签失败！");
//            return resultBean;
//        }
        
        //获取奖品列表
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("fileurl", getDoPath(PropUtils.getSystem("file.domain.url")));
        paraMap.put("prizeKind", CustomConstants.CONF_PRIZE_KIND_CHANGE);
        paraMap.put("prizeStatus", String.valueOf(CustomConstants.CONF_PRIZE_STATUS_ON));
        List<PrizeGetCustomize> prizeList = prizeChangeService.getPrizeList(paraMap);
        resultBean.setData(prizeList);
        
        if (!Validator.isNull(form.getUserId())) {
            //获取用户的推荐星信息
            paraMap.put("userId", form.getUserId());
            InviteRecommend recommend = prizeChangeService.getRecommendInfo(paraMap);
            int recommendCount = 0;
            if(recommend != null){
                recommendCount = recommend.getPrizeAllCount() - recommend.getPrizeUsedCount();
            }
            if(recommendCount < 0){
                recommendCount = 0;
            }
            resultBean.setRecommendCount(recommendCount);
        }
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    /**
     * 
     * 兑奖处理 （活动已过期，方法屏蔽）
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    //@RequestMapping(value = PrizeChangeDefine.PRIZE_CHANGE_CHECK)
    public PrizeChangeCheckResultBean prizeChangeCheck(@ModelAttribute PrizeChangeBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "prizeChangeCheck";
        LogUtil.startLog(this.getClass().getName(), methodName);
        PrizeChangeCheckResultBean resultBean = new PrizeChangeCheckResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId()) || Validator.isNull(form.getGroupCode()) || Validator.isNull(form.getChangeCount())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("1");
            resultBean.setStatusDesc("请求参数非法");
            LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
            return resultBean;
        }
        
        if(form.getChangeCount() <= 0){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("3");
            resultBean.setStatusDesc("兑奖数量应大于0");
            LogUtil.errorLog(this.getClass().getName(), methodName, "兑奖数量应大于0", null);
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_DO_PRIZE_CHANGE)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("5");
            resultBean.setStatusDesc("验签失败！");
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            return resultBean;
        }
        
        //校验推荐星兑换奖品
        PrizeGetCustomize prizeConf = prizeChangeService.getPrizeConfByGroup(form.getGroupCode());
        
        if(prizeConf == null){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("5");
            resultBean.setStatusDesc("对应的奖品不存在");
            LogUtil.errorLog(this.getClass().getName(), methodName, "对应的奖品不存在", null);
            return resultBean;
        }
        
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("userId", form.getUserId());
        InviteRecommend recommendInfo = prizeChangeService.getRecommendInfo(paraMap);
        if(recommendInfo == null){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("2");
            resultBean.setStatusDesc("用户推荐星数据不存在");
            LogUtil.errorLog(this.getClass().getName(), methodName, "用户推荐星数据不存在", null);
            return resultBean;
        }
        
        
        
        // 校验奖品数量
        if(prizeConf.getPrizeReminderQuantity() <= 0 || prizeConf.getPrizeReminderQuantity() < form.getChangeCount()){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("4");
            resultBean.setStatusDesc("奖品不足");
            LogUtil.errorLog(this.getClass().getName(), methodName, "奖品不足", null);
            return resultBean;
        }
       
        // 校验推荐星
        int recommendNeedCount = prizeConf.getRecommendQuantity()*form.getChangeCount();
        int recommendValidCount = recommendInfo.getPrizeAllCount() - recommendInfo.getPrizeUsedCount();
        if(recommendValidCount < recommendNeedCount){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("2");
            resultBean.setStatusDesc("用户推荐星不足已兑换该奖品");
            LogUtil.errorLog(this.getClass().getName(), methodName, "用户推荐星不足已兑换该奖品", null);
            return resultBean;
        }
        
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setErrCode("0");
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        LogUtil.endLog(this.getClass().getName(), methodName);
        
        return resultBean;
    }

    
    
    /**
     * 
     * 兑奖处理 （活动已过期，方法禁用）
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    //@RequestMapping(value = PrizeChangeDefine.DO_PRIZE_CHANGE)
    public synchronized PrizeChangeResultBean doPrizeChange(@ModelAttribute PrizeChangeBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "doPrizeChange";
        LogUtil.startLog(this.getClass().getName(), methodName);
        PrizeChangeResultBean resultBean = new PrizeChangeResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId()) || Validator.isNull(form.getGroupCode()) || Validator.isNull(form.getChangeCount())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            resultBean.setErrCode("1");
            LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
            return resultBean;
        }
        
        if(form.getChangeCount() <= 0){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("3");
            resultBean.setStatusDesc("兑奖数量应大于0");
            LogUtil.errorLog(this.getClass().getName(), methodName, "兑奖数量应大于0", null);
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_DO_PRIZE_CHANGE)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("5");
            resultBean.setStatusDesc("验签失败！");
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            return resultBean;
        }
        
        //校验推荐星兑换奖品
        PrizeGetCustomize prizeConf = prizeChangeService.getPrizeConfByGroup(form.getGroupCode());
        if(prizeConf == null){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("5");
            resultBean.setStatusDesc("对应的奖品数据不存在");
            LogUtil.errorLog(this.getClass().getName(), methodName, "对应的奖品数据不存在", null);
            return resultBean;
        }
        
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("userId", form.getUserId());
        InviteRecommend recommendInfo = prizeChangeService.getRecommendInfo(paraMap);
        
        if(recommendInfo == null){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("2");
            resultBean.setStatusDesc("推荐星数据不存在");
            LogUtil.errorLog(this.getClass().getName(), methodName, "推荐星数据不存在", null);
            return resultBean;
        }
        
        // 校验奖品数量
        if(prizeConf.getPrizeReminderQuantity() <= 0 || prizeConf.getPrizeReminderQuantity() < form.getChangeCount()){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("4");
            resultBean.setStatusDesc("奖品不足");
            LogUtil.errorLog(this.getClass().getName(), methodName, "奖品不足", null);
            return resultBean;
        }
       
        // 校验推荐星
        int recommendNeedCount = prizeConf.getRecommendQuantity()*form.getChangeCount();
        int recommendValidCount = recommendInfo.getPrizeAllCount() - recommendInfo.getPrizeUsedCount();
        if(recommendValidCount < recommendNeedCount){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("2");
            resultBean.setStatusDesc("用户推荐星不足已兑换该奖品");
            LogUtil.errorLog(this.getClass().getName(), methodName, "用户推荐星不足已兑换该奖品", null);
            return resultBean;
        }
        
        //兑奖处理
        InviteRecommendPrize recommendPrize = new InviteRecommendPrize();
        recommendPrize.setPrizeCount(form.getChangeCount());
        recommendPrize.setUsedRecommendCount(recommendNeedCount);
        recommendPrize.setPrizeGroup(form.getGroupCode());
        recommendPrize.setUserId(form.getUserId());
        recommendPrize.setPrizeType(Integer.parseInt(prizeConf.getPrizeType()));
        if(prizeConf.getPrizeType().equals(String.valueOf(CustomConstants.CONF_PRIZE_TYPE_ENTITY))){
            recommendPrize.setRemark("人工发放");
        }
        boolean result = prizeChangeService.insertPrizeChange(recommendPrize, recommendNeedCount, form.getChangeCount());
        
        resultBean.setPrizeName(prizeConf.getPrizeName());
        resultBean.setPrizePicUrl(StringUtils.isEmpty(prizeConf.getPrizePicUrl())?"" : prizeConf.getPrizePicUrl());
        resultBean.setPrizeCount(form.getChangeCount());
        resultBean.setRecommendCost(recommendNeedCount);
        resultBean.setRemark(StringUtils.isEmpty(prizeConf.getRemark())?"" : prizeConf.getRemark());
        resultBean.setSuccessMsg(StringUtils.isEmpty(prizeConf.getSuccessMessage())?"" : prizeConf.getSuccessMessage());
        
        // 发送优惠券
        if(result && prizeConf.getPrizeType().equals(CustomConstants.CONF_PRIZE_TYPE_COUPON)){
            JSONObject sendResult = prizeChangeService.sendPrizeCoupon(String.valueOf(form.getUserId()), form.getGroupCode(), form.getChangeCount());
            // 发放是否成功状态
            int sendStatus = sendResult.getIntValue("status");
            // 发放优惠券的数量
            int sendCount = sendResult.getIntValue("couponCount");
            @SuppressWarnings("unchecked")
            List<String> couponUserCodeList = (List<String>)sendResult.get("retCouponUserCodes");
            if (sendStatus == 0 && sendCount > 0) {
                // 备注：用户优惠券编号
                if(couponUserCodeList.size()==1){
                    // 发放一张优惠券
                    recommendPrize.setRemark(couponUserCodeList.get(0));
                }else{
                    // 发放多张优惠券
                    StringBuffer remark = new StringBuffer();
                    for(int i=0;i<couponUserCodeList.size();i++){
                        remark.append(couponUserCodeList.get(i));
                        if(i != couponUserCodeList.size()-1){
                            remark.append("<br>");
                        }
                    }
                    recommendPrize.setRemark(remark.toString());
                }
                prizeChangeService.updatePrizeSendById(recommendPrize);
                LogUtil.infoLog(this.getClass().getName(), methodName, "10月份推荐新用户活动优惠券发送成功");
            }else{
                LogUtil.errorLog(this.getClass().getName(), methodName, "10月份推荐新用户活动优惠券发送失败，UserID：" + form.getUserId(), null);
                //失败处理 奖品发送状态更新为未发放
                recommendPrize.setPrizeSendFlag(Integer.parseInt(CustomConstants.PRIZE_SEND_FLAG_NO));
                prizeChangeService.updatePrizeSendById(recommendPrize);
            }
        }
        
        resultBean.setErrCode("0");
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        LogUtil.endLog(this.getClass().getName(), methodName);
        
        return resultBean;
    }
    
    public static String getDoPath(String path) {
        path = path.replace("\\", "/");
        String lastChar = path.substring(path.length() - 1);
        if (!"/".equals(lastChar)) {
            path += "/";
        }
        return path;
    }
}
