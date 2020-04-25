package com.hyjf.api.web.activity.newyear.wealthcard;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.newyear.wealthcard.WealthCardBean;
import com.hyjf.activity.newyear.wealthcard.WealthCardDefine;
import com.hyjf.activity.newyear.wealthcard.WealthCardGiveResultBean;
import com.hyjf.activity.newyear.wealthcard.WealthCardPhoneCheckResultBean;
import com.hyjf.activity.newyear.wealthcard.WealthCardPrizeResultBean;
import com.hyjf.activity.newyear.wealthcard.WealthCardResultBean;
import com.hyjf.activity.newyear.wealthcard.WealthCardService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantity;
import com.hyjf.mybatis.model.auto.NewyearPrizeConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller
@RequestMapping(value=WealthCardDefine.REQUEST_MAPPING)
public class WealthCardServer extends BaseController{
    
    private static final Integer ACTIVITY_A_START_TIME = 1484841600;
    
    private static final Integer ACTIVITY_A_END_TIME = 1486223999; 
  
  // 活动已结束的时间
//  private static final Integer ACTIVITY_A_END_TIME = 1484534108; 

    @Autowired
    private WealthCardService wealthCardService;
    
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser; 
    
    /**
     * 
     * 获取用户财富卡数据
     * @author hsy
     * @param wealthCardBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WealthCardDefine.GET_USERCARD_COUNT)
    public WealthCardResultBean getUserCardCount(@ModelAttribute WealthCardBean wealthCardBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getUserCardCount";
        LogUtil.startLog(this.getClass().getName(), WealthCardDefine.GET_USERCARD_COUNT);
        WealthCardResultBean resultBean = new WealthCardResultBean();
        try{
            //验证请求参数
            if (Validator.isNull(wealthCardBean.getUserId())) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("请求参数非法");
                LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
                return resultBean;
            }
    
            //验签
            if(!this.checkSign(wealthCardBean, BaseDefine.METHOD_GET_USERCARD_COUNT)){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("验签失败！");
                LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
                return resultBean;
            }
    
            NewyearCaishenCardQuantity cardQuantity = wealthCardService.getUserCardQuantity(String.valueOf(wealthCardBean.getUserId()));
            
            if(cardQuantity != null){
                resultBean.setCountJin(cardQuantity.getCardJinQuantity());
                resultBean.setCountJi(cardQuantity.getCardJiQuantity());
                resultBean.setCountNa(cardQuantity.getCardNaQuantity());
                resultBean.setCountFu(cardQuantity.getCardFuQuantity());
            }
        }catch(Exception e){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("抽奖失败，执行出现异常");
            LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，更新抽奖结果时出错", e);
            return resultBean;
        }
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), WealthCardDefine.GET_USERCARD_COUNT);
        return resultBean;
    }
    
    /**
     * 
     * 点燃鞭炮抽奖
     * @author hsy
     * @param wealthCardBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WealthCardDefine.DO_PRIZE_DRAW)
    public synchronized WealthCardPrizeResultBean doNewYearPrizeDraw(@ModelAttribute WealthCardBean wealthCardBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "doNewYearPrizeDraw";
        LogUtil.startLog(this.getClass().getName(), WealthCardDefine.DO_PRIZE_DRAW);
        WealthCardPrizeResultBean resultBean = new WealthCardPrizeResultBean();
        try{
            //验证请求参数
            if (Validator.isNull(wealthCardBean.getUserId())) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("1");
                resultBean.setStatusDesc("请求参数非法");
                LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
                return resultBean;
            }
    
            //验签
            if(!this.checkSign(wealthCardBean, BaseDefine.METHOD_NEWYEAR_PRIZEDRAW)){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("1");
                resultBean.setStatusDesc("验签失败！");
                LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
                return resultBean;
            }
            
            if(GetDate.getNowTime10() < ACTIVITY_A_START_TIME){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("10");
                resultBean.setStatusDesc("活动未开始");
                LogUtil.errorLog(this.getClass().getName(), methodName, "活动未开始", null);
                return resultBean;
            }
            if(GetDate.getNowTime10() > ACTIVITY_A_END_TIME){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("11");
                resultBean.setStatusDesc("活动已结束");
                LogUtil.errorLog(this.getClass().getName(), methodName, "活动已结束", null);
                return resultBean;
            }

            NewyearCaishenCardQuantity cardQuantity = wealthCardService.getUserCardQuantity(String.valueOf(wealthCardBean.getUserId()));
            
            //校验是否可以点燃鞭炮
            if(cardQuantity == null || !wealthCardService.checkCanOpenPrize(String.valueOf(wealthCardBean.getUserId()))){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("2");
                resultBean.setStatusDesc("该用户没有燃放鞭炮抽奖资格");
                LogUtil.errorLog(this.getClass().getName(), methodName, "该用户没有燃放鞭炮抽奖资格", null);
                return resultBean;
            }
            
            int result = wealthCardService.generatePrize();
            
            if(result == 0){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("3");
                resultBean.setStatusDesc("抽奖失败，抽奖算法执行出错");
                LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，抽奖算法执行出错", null);
                return resultBean;
            }
            
            NewyearPrizeConfig prizeConfig = wealthCardService.getPrizeById(result);
            if(prizeConfig == null){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("3");
                resultBean.setStatusDesc("抽奖失败，根据中奖ID没有找到对应的奖品");
                LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，根据中奖ID没有找到对应的奖品", null);
                return resultBean;
            }
            
            boolean resultBoolean = wealthCardService.insertPrizeDraw(String.valueOf(wealthCardBean.getUserId()), prizeConfig, cardQuantity.getUpdateTime());
            if(!resultBoolean){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("3");
                resultBean.setStatusDesc("抽奖失败，更新抽奖结果时出错");
                LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，更新抽奖结果时出错", null);
                return resultBean;
            }
            
            resultBean.setPrizeType(prizeConfig.getPrizeOnline());
            resultBean.setPrizeName(prizeConfig.getPrizeName());
        }catch(Exception e){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("3");
            resultBean.setStatusDesc("抽奖失败，执行出现异常");
            LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，更新抽奖结果时出错", e);
            return resultBean;
        }
        
        resultBean.setErrCode("0");
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), WealthCardDefine.DO_PRIZE_DRAW);
        return resultBean;
    }

    /**
     * 
     * 手机号码校验
     * @author hsy
     * @param wealthCardBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WealthCardDefine.DO_PHONENUM_CHECK)
    public WealthCardPhoneCheckResultBean doPhoneNumCheck(@ModelAttribute WealthCardBean wealthCardBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "doPhoneNumCheck";
        LogUtil.startLog(this.getClass().getName(), WealthCardDefine.DO_PHONENUM_CHECK);
        WealthCardPhoneCheckResultBean resultBean = new WealthCardPhoneCheckResultBean();
        try{
            //验证请求参数
            if (Validator.isNull(wealthCardBean.getPhoneNum()) || !Validator.isPhoneNumber(wealthCardBean.getPhoneNum().trim())) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("请求参数非法");
                LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
                return resultBean;
            }
    
            //验签
            if(!this.checkSign(wealthCardBean, BaseDefine.METHOD_PHONENUM_CHECK)){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("验签失败！");
                LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
                return resultBean;
            }
           
            UsersInfo userInfo = wealthCardService.getUserInfoByPhone(wealthCardBean.getPhoneNum().trim());
            if(userInfo != null && StringUtils.isNotEmpty(userInfo.getTruename())){
                resultBean.setUserName(userInfo.getTruename());
                resultBean.setIsValidPhoneNum(0);
            }else if(userInfo != null && StringUtils.isEmpty(userInfo.getTruename())){
                resultBean.setUserName("");
                resultBean.setIsValidPhoneNum(2);
            }else{
                resultBean.setIsValidPhoneNum(1);
            }
            
            resultBean.setPhoneNum(wealthCardBean.getPhoneNum());
            
        }catch(Exception e){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("抽奖失败，执行出现异常");
            LogUtil.errorLog(this.getClass().getName(), methodName, "抽奖失败，更新抽奖结果时出错", e);
            return resultBean;
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), WealthCardDefine.DO_PHONENUM_CHECK);
        return resultBean;
    }

    /**
     * 
     * 发送财富卡给好友
     * @author hsy
     * @param wealthCardBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WealthCardDefine.DO_CARD_SEND)
    public synchronized WealthCardGiveResultBean doCardSend(@ModelAttribute WealthCardBean wealthCardBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName= "doCardSend";
        LogUtil.startLog(this.getClass().getName(), WealthCardDefine.DO_CARD_SEND);
        WealthCardGiveResultBean resultBean = new WealthCardGiveResultBean();
        
        try{
            //验证请求参数
            if (Validator.isNull(wealthCardBean.getUserId()) || Validator.isNull(wealthCardBean.getPhoneNum())
                            || Validator.isNull(wealthCardBean.getCardIdentifier()) || !Validator.isPhoneNumber(wealthCardBean.getPhoneNum().trim())) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("1");
                resultBean.setStatusDesc("请求参数非法");
                LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
                return resultBean;
            }
            
            // 校验财富卡类型
            if(wealthCardBean.getCardIdentifier() != 1 && wealthCardBean.getCardIdentifier() != 2 
                    && wealthCardBean.getCardIdentifier() != 3 && wealthCardBean.getCardIdentifier() != 4){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("1");
                resultBean.setStatusDesc("请求参数非法");
                LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
                return resultBean;
            }
    
            //验签
            if(!this.checkSign(wealthCardBean, BaseDefine.METHOD_CARD_SEND)){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("1");
                resultBean.setStatusDesc("验签失败！");
                LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
                return resultBean;
            }
            
            if(GetDate.getNowTime10() < ACTIVITY_A_START_TIME){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("10");
                resultBean.setStatusDesc("活动未开始");
                LogUtil.errorLog(this.getClass().getName(), methodName, "活动未开始", null);
                return resultBean;
            }
            if(GetDate.getNowTime10() > ACTIVITY_A_END_TIME){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("11");
                resultBean.setStatusDesc("活动已结束");
                LogUtil.errorLog(this.getClass().getName(), methodName, "活动已结束", null);
                return resultBean;
            }
            
            // 校验发送方
            Users userSend = wealthCardService.getUsers(wealthCardBean.getUserId());
            UsersInfo userInfoSend = wealthCardService.getUsersInfoByUserId(wealthCardBean.getUserId());
            if(userInfoSend == null || userSend == null){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("1");
                resultBean.setStatusDesc("请求参数非法");
                LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
                return resultBean;
            }
            
            // 校验手机号码
            UsersInfo userInfo = wealthCardService.getUserInfoByPhone(wealthCardBean.getPhoneNum().trim());
            if(wealthCardService.getUserInfoByPhone(wealthCardBean.getPhoneNum()) == null){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("2");
                resultBean.setStatusDesc("手机号码对应的用户不存在");
                LogUtil.errorLog(this.getClass().getName(), methodName, "手机号码对应的用户不存在", null);
                return resultBean;
            }
            
            // 校验是否是自己本人
            if(userInfo.getUserId().equals(wealthCardBean.getUserId())){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("3");
                resultBean.setStatusDesc("赠送的人不能是自己");
                LogUtil.errorLog(this.getClass().getName(), methodName, "赠送的人不能是自己", null);
                return resultBean;
            }
           
            NewyearCaishenCardQuantity cardQuantity = wealthCardService.getUserCardQuantity(String.valueOf(wealthCardBean.getUserId()));
            
            // 校验卡片数量
            if(cardQuantity == null || !wealthCardService.checkCanGive(String.valueOf(wealthCardBean.getUserId()), wealthCardBean.getCardIdentifier())){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("4");
                resultBean.setStatusDesc("用户该卡片数量不足");
                LogUtil.errorLog(this.getClass().getName(), methodName, "用户该卡片数量不足", null);
                return resultBean;
            }
            
            try {
                boolean result = wealthCardService.insertCardSend(String.valueOf(wealthCardBean.getUserId()), wealthCardBean.getCardIdentifier(), wealthCardBean.getPhoneNum().trim(), cardQuantity.getUpdateTime());
            
                if(!result){
                    resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                    resultBean.setErrCode("5");
                    resultBean.setStatusDesc("执行卡片发送业务处理时失败");
                    LogUtil.errorLog(this.getClass().getName(), methodName, "执行卡片发送业务处理时失败", null);
                    return resultBean;
                }
                
                // 发送短信通知用户
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("val_name", StringUtils.isEmpty(userInfoSend.getTruename())?userSend.getUsername():userInfoSend.getTruename());
                
                SmsMessage smsMessage =
                        new SmsMessage(null, paramMap, wealthCardBean.getPhoneNum(), null, MessageDefine.SMSSENDFORMOBILE, null,
                                WealthCardDefine.TPL_SMS_NEWYEAR_CAIFUKA, CustomConstants.CHANNEL_TYPE_NORMAL);
                smsProcesser.gather(smsMessage);

            } catch (Exception e) {
                e.printStackTrace();
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setErrCode("5");
                resultBean.setStatusDesc("执行卡片发送业务处理时失败");
                LogUtil.errorLog(this.getClass().getName(), methodName, "执行卡片发送业务处理时失败", null);
                return resultBean;
            }
            
            resultBean.setCardName(wealthCardService.getCardName(wealthCardBean.getCardIdentifier()));
            resultBean.setUserName(StringUtils.isEmpty(userInfo.getTruename())?"" : userInfo.getTruename());
            resultBean.setPhoneNum(wealthCardBean.getPhoneNum());
            
        }catch(Exception e){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setErrCode("5");
            resultBean.setStatusDesc("卡片发送失败，执行出现异常");
            LogUtil.errorLog(this.getClass().getName(), methodName, "卡片发送失败，执行出现异常", e);
            return resultBean;
        }
        resultBean.setErrCode("0");
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), WealthCardDefine.DO_CARD_SEND);
        return resultBean;
    }

}
