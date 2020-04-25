package com.hyjf.api.web.activity.mgm10.prizedraw;

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
import com.hyjf.activity.mgm10.prizedraw.PrizeDrawBean;
import com.hyjf.activity.mgm10.prizedraw.PrizeDrawDefine;
import com.hyjf.activity.mgm10.prizedraw.PrizeDrawResultBean;
import com.hyjf.activity.mgm10.prizedraw.PrizeDrawService;
import com.hyjf.activity.mgm10.prizedraw.PrizeListResultBean;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.auto.InviteRecommendPrize;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

@Controller
@RequestMapping(value = PrizeDrawDefine.REQUEST_MAPPING)
public class PrizeDrawServer extends BaseController{

    @Autowired
    private PrizeDrawService prizeDrawService;
    
    /**
     * 
     * 获取抽奖的奖品列表
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeDrawDefine.GET_PRIZELIST)
    public PrizeListResultBean getPrizeDrawList(@ModelAttribute PrizeDrawBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getPrizeDrawList";
        LogUtil.startLog(this.getClass().getName(), methodName);
        PrizeListResultBean resultBean = new PrizeListResultBean();
        
        //验证请求参数
//        if (Validator.isNull(form.getUserId())) {
//            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("请求参数非法");
//            return resultBean;
//        }
        
        //验签
//        if(!this.checkSign(form, BaseDefine.METHOD_PRIZE_DRAW_LIST)){
//            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("验签失败！");
//            return resultBean;
//        }
        
        //获取奖品列表
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("fileurl", getDoPath(PropUtils.getSystem("file.domain.url")));
        paraMap.put("prizeKind", CustomConstants.CONF_PRIZE_KIND_DRAW);
        paraMap.put("prizeStatus", String.valueOf(CustomConstants.CONF_PRIZE_STATUS_ON));
        List<PrizeGetCustomize> prizeList = prizeDrawService.getPrizeList(paraMap);
        resultBean.setData(prizeList);
        
        //获取用户的推荐星信息
        Integer needCount = Integer.parseInt(PropUtils.getSystem(PropertiesConstants.PRIZE_DRAW_RECOMMEND_COUNT));
        resultBean.setNeedCount(needCount);
        
        // 用户已登录则获取推荐星信息
        if (!Validator.isNull(form.getUserId())) {
            paraMap.put("userId", form.getUserId());
            InviteRecommend recommend = prizeDrawService.getRecommendInfo(paraMap);
            int recommendCount = 0;
            if(recommend != null){
                recommendCount = recommend.getPrizeAllCount() - recommend.getPrizeUsedCount();
            }
            if(recommendCount < 0){
                recommendCount = 0;
            }
            resultBean.setRecommendCount(recommendCount);
            resultBean.setCanDrawCount(recommendCount/needCount);
        }
        
        
        // 获取已中奖用户列表
        List<Map<String,Object>> prizeWinList = prizeDrawService.getPrizeWinList();
        resultBean.setPrizeWinList(prizeWinList);
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    /**
     * 
     * 抽奖处理 （活动已过期，方法屏蔽）
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    //@RequestMapping(value = PrizeDrawDefine.DO_PRIZE_DRAW)
    public synchronized PrizeDrawResultBean doPrizeDraw(@ModelAttribute PrizeDrawBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "doPrizeDraw";
        LogUtil.startLog(this.getClass().getName(), methodName);
        PrizeDrawResultBean resultBean = new PrizeDrawResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("1");
            resultBean.setStatusDesc("请求参数非法");
            LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_DO_PRIZE_DRAW)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            resultBean.setErrCode("3");
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            return resultBean;
        }
        
        //校验推荐星
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("userId", form.getUserId());
        InviteRecommend recommendInfo = prizeDrawService.getRecommendInfo(paraMap);
        if(recommendInfo == null){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("2");
            resultBean.setStatusDesc("对应的用户推荐星数据不存在");
            return resultBean;
        }
        Integer needCount = Integer.parseInt(PropUtils.getSystem(PropertiesConstants.PRIZE_DRAW_RECOMMEND_COUNT));
        int recommendValidCount = recommendInfo.getPrizeAllCount() - recommendInfo.getPrizeUsedCount();
        if(recommendValidCount < needCount){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("2");
            resultBean.setStatusDesc("用户推荐星不足以兑换该奖品");
            return resultBean;
        }
        
        // 抽奖
        String genResult = prizeDrawService.generatePrize(recommendInfo.getBlackUser());
        for(int i=0; i<50; i++){
            if(genResult.equals("0")){
                System.err.println("--------本次没有抽到奖品，当前第" + ++i + "次----------");
                genResult = prizeDrawService.generatePrize(recommendInfo.getBlackUser());
            }else {
                break;
            }
            
        }
        
        if(genResult.equals("0")){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("3");
            resultBean.setStatusDesc("没有抽到奖品");
            return resultBean;
        } else {
            PrizeGetCustomize prizeConf = prizeDrawService.getPrizeConfByGroup(genResult);
            
            resultBean.setPrizeName(prizeConf.getPrizeName());
            resultBean.setGroupCode(prizeConf.getPrizeGroupCode());
            resultBean.setPrizePicUrl(prizeConf.getPrizePicUrl());
            resultBean.setPrizeCount(1);
            resultBean.setRecommendCost(needCount);
            resultBean.setRemark(StringUtils.isEmpty(prizeConf.getRemark())? "" : prizeConf.getRemark());
            resultBean.setSuccessMsg(StringUtils.isEmpty(prizeConf.getSuccessMessage())?"" : prizeConf.getSuccessMessage());
            
            InviteRecommendPrize recommendPrize = new InviteRecommendPrize();
            recommendPrize.setPrizeCount(1);
            recommendPrize.setUsedRecommendCount(needCount);
            recommendPrize.setPrizeGroup(prizeConf.getPrizeGroupCode());
            recommendPrize.setUserId(form.getUserId());
            recommendPrize.setPrizeType(Integer.parseInt(prizeConf.getPrizeType()));
            if(prizeConf.getPrizeType().equals(String.valueOf(CustomConstants.CONF_PRIZE_TYPE_ENTITY))){
                recommendPrize.setRemark("人工发放");
            }
            boolean result =prizeDrawService.insertPrizeDraw(recommendPrize);
            
            // 发送优惠券
            if(result && prizeConf.getPrizeType().equals(String.valueOf(CustomConstants.CONF_PRIZE_TYPE_COUPON))){
                JSONObject sendResult = prizeDrawService.sendPrizeCoupon(String.valueOf(form.getUserId()), genResult, 1);
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
                    prizeDrawService.updatePrizeSendById(recommendPrize);

                    LogUtil.infoLog(this.getClass().getName(), methodName, "10月份推荐新用户活动优惠券发送成功");
                }else{
                    LogUtil.errorLog(this.getClass().getName(), methodName, "10月份推荐新用户活动优惠券发送失败，UserID：" + form.getUserId(), null);
                    //失败处理 奖品发送状态更新为未发放
                    recommendPrize.setPrizeSendFlag(Integer.parseInt(CustomConstants.PRIZE_SEND_FLAG_NO));
                    prizeDrawService.updatePrizeSendById(recommendPrize);
                }
            }
        }
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setErrCode("0");
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
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
