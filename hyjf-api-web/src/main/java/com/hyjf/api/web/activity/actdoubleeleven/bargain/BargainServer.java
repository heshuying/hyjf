package com.hyjf.api.web.activity.actdoubleeleven.bargain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hyjf.activity.actdoubleeleven.bargain.BargainDefine;
import com.hyjf.activity.actdoubleeleven.bargain.BargainDoubleRequestBean;
import com.hyjf.activity.actdoubleeleven.bargain.BargainDoubleResultBean;
import com.hyjf.activity.actdoubleeleven.bargain.BargainRequestBean;
import com.hyjf.activity.actdoubleeleven.bargain.BargainResultBean;
import com.hyjf.activity.actdoubleeleven.bargain.BargainService;
import com.hyjf.activity.actdoubleeleven.bargain.PrizeBargainListResultBean;
import com.hyjf.activity.actdoubleeleven.bargain.PrizeBuyRequestBean;
import com.hyjf.activity.actdoubleeleven.bargain.PrizeBuyResultBean;
import com.hyjf.activity.actdoubleeleven.bargain.PrizeDetailResultBean;
import com.hyjf.activity.actdoubleeleven.bargain.PrizeListResultBean;
import com.hyjf.activity.actdoubleeleven.bargain.SmsCodeRequestBean;
import com.hyjf.activity.actdoubleeleven.bargain.SmsCodeResultBean;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.ActJanPrize;
import com.hyjf.mybatis.model.auto.ActivityInviteSeven;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.customize.act.ActNovBargainCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeDetailCustomize;

/**
 * 砍价活动
 * @author hesy
 *
 */
@Controller
@RequestMapping(value = BargainDefine.REQUEST_MAPPING)
public class BargainServer extends BaseController{

    @Autowired
    private BargainService bargainService;
    
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	Logger _log = LoggerFactory.getLogger(BargainServer.class);
    
    String actTime = PropUtils.getSystem("hyjf.act.nov.2017.bargain.time");
    
    /**
     * 获取砍价活动奖品列表
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BargainDefine.GET_PRIZELIST)
    public PrizeListResultBean getActPrizeList(HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActPrizeList";
        LogUtil.startLog(this.getClass().getName(), methodName);
        
        PrizeListResultBean resultBean = new PrizeListResultBean();
        
        String actStartTime = actTime.split(",")[0];
        String actEndTime = actTime.split(",")[1];
        
        resultBean.setActStartTime(actStartTime);
        resultBean.setActEndtime(actEndTime);
        resultBean.setNowTime(String.valueOf(GetDate.getNowTime10()));
        
        String wechatId = request.getParameter("wechatId");
        
        _log.info("getActPrizeList接口请求参数：wechatId=" + wechatId);
        
        //验证请求参数
        if (Validator.isNull(wechatId)) {
            wechatId = null;
        }
        
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("wechatId", wechatId);
        List<ActNovPrizeCustomize> prizeList = bargainService.getPrizeList(paraMap);
        
        resultBean.setDataList(prizeList);
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        _log.info("getActPrizeList接口返回：" + resultBean);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    /**
     * 奖品详情接口
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BargainDefine.GET_PRIZEDETAIL)
    public PrizeDetailResultBean getActPrizeDetail(HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActPrizeDetail";
        LogUtil.startLog(this.getClass().getName(), methodName);

        PrizeDetailResultBean resultBean = new PrizeDetailResultBean();
        
        String actStartTime = actTime.split(",")[0];
        String actEndTime = actTime.split(",")[1];
        
        String wechatId = request.getParameter("wechatId");
        String wechatIdHelp = request.getParameter("wechatIdHelp");
        String prizeId = request.getParameter("prizeId");
        
        _log.info("getActPrizeDetail接口请求参数：wechatId=" + wechatId + " wechatIdHelp=" + wechatIdHelp + " prizeId=" + prizeId);
        
        resultBean.setActStartTime(actStartTime);
        resultBean.setActEndtime(actEndTime);
        resultBean.setNowTime(String.valueOf(GetDate.getNowTime10()));
        
        if (Validator.isNull(wechatId) || Validator.isNull(wechatIdHelp)) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            _log.info("getActPrizeDetail接口返回：" + resultBean);
            return resultBean;
        }
        
        if (Validator.isNull(prizeId)) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            _log.info("getActPrizeDetail接口返回：" + resultBean);
            return resultBean;
        }
        
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("wechatId", wechatId);
        paraMap.put("wechatIdHelp", wechatIdHelp);
        paraMap.put("prizeId", prizeId);
        ActNovPrizeDetailCustomize prizeDetail = bargainService.getPrizeDetail(paraMap);
        if (prizeDetail == null) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("奖品不存在");
            _log.info("getActPrizeDetail接口返回：" + resultBean);
            return resultBean;
        }
        
        try {
			BeanUtils.copyProperties(resultBean, prizeDetail);
		} catch (Exception e) {
			e.printStackTrace();
			
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("发生异常");
            _log.info("getActPrizeDetail接口返回：" + resultBean);
            return resultBean;
		}        
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        _log.info("getActPrizeDetail接口返回：" + resultBean);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    /**
     * 砍价列表数据
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BargainDefine.GET_PRIZE_BARGAIN_LIST)
    public PrizeBargainListResultBean getActPrizeBargainList(HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActPrizeBargainList";
        LogUtil.startLog(this.getClass().getName(), methodName);

        PrizeBargainListResultBean resultBean = new PrizeBargainListResultBean();
        
        String wechatId = request.getParameter("wechatId");
        String prizeId = request.getParameter("prizeId");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");
        
        _log.info("getActPrizeBargainList接口请求参数：wechatId=" + wechatId + " prizeId=" + prizeId + " page=" + page + " pageSize=" + pageSize);
        
        if (Validator.isNull(wechatId)) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            _log.info("getActPrizeBargainList接口返回：" + resultBean);
            return resultBean;
        }
        
        if (Validator.isNull(prizeId)) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            _log.info("getActPrizeBargainList接口返回：" + resultBean);
            return resultBean;
        }
        
        if(StringUtils.isEmpty(page)){
        	page = "1";
        }
        
        if(StringUtils.isEmpty(pageSize)){
        	pageSize = "10";
        }
        
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("wechatId", wechatId);
        paraMap.put("prizeId", prizeId);
        
        int countTotal = bargainService.selectBargainCount(paraMap);
        if(countTotal > 0){
            Paginator paginator = new Paginator(Integer.parseInt(page), countTotal, Integer.parseInt(pageSize));
            List<ActNovBargainCustomize> bargainList = bargainService.selectBargainList(paraMap);
            resultBean.setPaginator(paginator);
            resultBean.setDataList(bargainList);
        }else{
            Paginator paginator = new Paginator(Integer.parseInt(page), countTotal, Integer.parseInt(pageSize));
            resultBean.setPaginator(paginator);
            resultBean.setData(new ArrayList<ActivityInviteSeven>());
        }
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        _log.info("getActPrizeBargainList接口返回：" + resultBean);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    /**
     * 砍价接口
     * @param bargainRequestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BargainDefine.DO_BARGAIN)
    public BargainResultBean doBargain( @ModelAttribute BargainRequestBean bargainRequestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "doBargain";
        LogUtil.startLog(this.getClass().getName(), methodName);

        _log.info("doBargain接口接收到的请求参数：" + bargainRequestBean);
        BargainResultBean resultBean = new BargainResultBean();
        
        String actStartTime = actTime.split(",")[0];
        String actEndTime = actTime.split(",")[1];
        if(GetDate.getNowTime10() < GetDate.getDayStart10(actStartTime)){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动未开始");
            _log.info("doBargain接口返回：" + resultBean);
            return resultBean;
        }
        
        if(GetDate.getNowTime10() > GetDate.getDayEnd10(actEndTime)){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束");
            _log.info("doBargain接口返回：" + resultBean);
            return resultBean;
        }
        
        if (Validator.isNull(bargainRequestBean.getWechatId()) || Validator.isNull(bargainRequestBean.getWechatIdHelp()) 
        		|| Validator.isNull(bargainRequestBean.getPrizeId()) || Validator.isNull(bargainRequestBean.getChkValue()) || Validator.isNull(bargainRequestBean.getTimestamp())) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            _log.info("doBargain接口返回：" + resultBean);
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(bargainRequestBean, BaseDefine.METHOD_DO_BARGAIN)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            _log.info("doBargain接口返回：" + resultBean);
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            return resultBean;
        }
        
        ActJanPrize prize = bargainService.getPrizeById(bargainRequestBean.getPrizeId());
        
        if(prize.getRemainCount() <= 0){
        	resultBean.setStatus("2");
            resultBean.setStatusDesc("砍价失败，奖品剩余份数为0");
            _log.info("doBargain接口返回：" + resultBean);
            LogUtil.errorLog(this.getClass().getName(), methodName, "砍价失败，奖品剩余份数为0", null);
            return resultBean;
        }

        BigDecimal curBargainMoney = bargainService.getCurrentBargainMoney(bargainRequestBean.getPrizeId(), bargainRequestBean.getWechatId());
        if(curBargainMoney.compareTo(prize.getPrice()) >= 0){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("砍价失败，奖品价格已经砍到目标价");
            _log.info("doBargain接口返回：" + resultBean);
            LogUtil.errorLog(this.getClass().getName(), methodName, "砍价失败，奖品价格已经看到目标价", null);
            return resultBean;
        }
        
        // iphone x 不让中奖
        if(bargainRequestBean.getPrizeId() == 1 && curBargainMoney.compareTo(new BigDecimal(7000))>=0){
        	PrizeBuyRequestBean bean = new PrizeBuyRequestBean();
        	bean.setPrizeId(bargainRequestBean.getPrizeId());
        	bean.setWechatId("喵里个喵");
        	bean.setWechatNickName("喵里个喵");
        	bargainService.updatePrizeBuy(bean);
        	
        	resultBean.setStatus("2");
            resultBean.setStatusDesc("砍价失败，奖品剩余份数为0");
            _log.info("doBargain接口返回：" + resultBean);
            LogUtil.errorLog(this.getClass().getName(), methodName, "砍价失败，奖品价格已经看到目标价", null);
            return resultBean;
        }
        
        boolean hasBargain = bargainService.checkHaveHelpedBargain(bargainRequestBean.getPrizeId(), bargainRequestBean.getWechatId(), bargainRequestBean.getWechatIdHelp());
        if(hasBargain){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("您的砍价次数已用完");
            _log.info("doBargain接口返回：" + resultBean);
            LogUtil.errorLog(this.getClass().getName(), methodName, "您的砍价次数已用完", null);
            return resultBean;
        }
        
        BigDecimal bargainMoney = bargainService.getRandomBargainMoney(bargainService.getBargainLevel(bargainRequestBean.getPrizeId(), prize.getPrice().doubleValue(), curBargainMoney.doubleValue()));
        if(bargainMoney.compareTo(prize.getPrice().subtract(curBargainMoney)) > 0){
        	// 如果砍价金额大于剩余可砍金额则砍价金额等于剩余金额
        	bargainMoney = prize.getPrice().subtract(curBargainMoney);
        }
        
        String clientIp = GetCilentIP.getIpAddr(request);
        
        Integer result = bargainService.insertBargainRecord(bargainRequestBean, bargainMoney, clientIp, prize);
        
        if(result !=null && result > 0){
        	resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        	resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        	resultBean.setIdBargain(result);
        }else{
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
        	resultBean.setStatusDesc("砍价失败，插入砍价记录失败");
        	LogUtil.errorLog(this.getClass().getName(), methodName, null);
        }
        
        resultBean.setPrizeBargain(bargainMoney);
        resultBean.setPrizeId(prize.getId());
        resultBean.setPrizeName(prize.getPrizeName());
        _log.info("doBargain接口返回：" + resultBean);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    /**
     * 砍价翻倍接口
     * @param bargainRequestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BargainDefine.DO_BARGAIN_DOUBLE)
    public BargainDoubleResultBean doBargainDouble(@ModelAttribute BargainDoubleRequestBean bargainRequestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "doBargainDouble";
        LogUtil.startLog(this.getClass().getName(), methodName);

        _log.info("doBargainDouble接口接收到的请求参数：" + bargainRequestBean);
        
        BargainDoubleResultBean resultBean = new BargainDoubleResultBean();
        String actStartTime = actTime.split(",")[0];
        String actEndTime = actTime.split(",")[1];
        if(GetDate.getNowTime10() < GetDate.getDayStart10(actStartTime)){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动未开始");
            LogUtil.errorLog(this.getClass().getName(), methodName, "活动未开始", null);
            _log.info("doBargainDouble接口返回：" + resultBean);
            return resultBean;
        }
        
        if(GetDate.getNowTime10() > GetDate.getDayEnd10(actEndTime)){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束");
            LogUtil.errorLog(this.getClass().getName(), methodName, "活动已结束", null);
            _log.info("doBargainDouble接口返回：" + resultBean);
            return resultBean;
        }
        
        if (Validator.isNull(bargainRequestBean.getIdBargain()) || Validator.isNull(bargainRequestBean.getPhoneNum()) 
        		|| Validator.isNull(bargainRequestBean.getSmsCode()) || Validator.isNull(bargainRequestBean.getChkValue()) || Validator.isNull(bargainRequestBean.getTimestamp())) {
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
            _log.info("doBargainDouble接口返回：" + resultBean);
            return resultBean;
        }

        //验签
        if(!this.checkSign(bargainRequestBean, BaseDefine.METHOD_DO_BARGAIN_DOUBLE)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            _log.info("doBargainDouble接口返回：" + resultBean);
            return resultBean;
        }
        
        int checkResult = bargainService.updateCheckMobileCode(bargainRequestBean.getPhoneNum(), bargainRequestBean.getSmsCode(), "act", "0", 8, 9);
        //校验短信验证码
        if(checkResult != 1){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("短信验证码错误");
            LogUtil.errorLog(this.getClass().getName(), methodName, "短信验证码错误", null);
            _log.info("doBargainDouble接口返回：" + resultBean);
            return resultBean;
        }
        
        BigDecimal doubleMoney = bargainService.updateBargainDouble(bargainRequestBean.getIdBargain(), bargainRequestBean.getPhoneNum());
        if(doubleMoney.compareTo(BigDecimal.ZERO) <= 0){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("砍价翻倍失败");
            LogUtil.errorLog(this.getClass().getName(), methodName, "砍价翻倍失败", null);
            _log.info("doBargainDouble接口返回：" + resultBean);
            return resultBean;
        }
        
        resultBean.setIdBargain(bargainRequestBean.getIdBargain());
        resultBean.setPrizeId(bargainRequestBean.getPrizeId());
        resultBean.setPrizeBargain(doubleMoney);
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
    	resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
    	_log.info("doBargainDouble接口返回：" + resultBean);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    /**
     * 奖品购买接口
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BargainDefine.DO_PRIZE_BUY)
    public PrizeBuyResultBean doPrizeBuy(@ModelAttribute PrizeBuyRequestBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "doPrizeBuy";
        LogUtil.startLog(this.getClass().getName(), methodName);

        _log.info("doPrizeBuy接口接收到的请求参数：" + requestBean);
        
        PrizeBuyResultBean resultBean = new PrizeBuyResultBean();
        String actStartTime = actTime.split(",")[0];
        String actEndTime = actTime.split(",")[1];
        if(GetDate.getNowTime10() < GetDate.getDayStart10(actStartTime)){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动未开始");
            LogUtil.errorLog(this.getClass().getName(), methodName, "活动未开始", null);
            _log.info("doPrizeBuy接口返回：" + resultBean);
            return resultBean;
        }
        
        if(GetDate.getNowTime10() > GetDate.getDayEnd10(actEndTime)){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束");
            LogUtil.errorLog(this.getClass().getName(), methodName, "活动已结束", null);
            _log.info("doPrizeBuy接口返回：" + resultBean);
            return resultBean;
        }
        
        //请求参数校验
        if (Validator.isNull(requestBean.getWechatId()) || Validator.isNull(requestBean.getPrizeId()) 
        		|| Validator.isNull(requestBean.getBookingName()) || Validator.isNull(requestBean.getBookingMobile()) 
        		|| Validator.isNull(requestBean.getBookingAddress()) || Validator.isNull(requestBean.getChkValue()) || Validator.isNull(requestBean.getTimestamp())) {
        	
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
            _log.info("doPrizeBuy接口返回：" + resultBean);
            return resultBean;
        }

        //验签
        if(!this.checkSign(requestBean, BaseDefine.METHOD_DO_PRIZE_BUY)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            _log.info("doPrizeBuy接口返回：" + resultBean);
            return resultBean;
        }
        
        ActJanPrize prize = null;
		try {
			prize = bargainService.getPrizeById(requestBean.getPrizeId());
			
			if(prize == null){
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			    resultBean.setStatusDesc("奖品不存在");
			    LogUtil.errorLog(this.getClass().getName(), methodName, "奖品不存在：prizeId：" + requestBean.getPrizeId(), null);
			    _log.info("doPrizeBuy接口返回：" + resultBean);
			    return resultBean;
			}
			
			if(prize.getRemainCount() <= 0){
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			    resultBean.setStatusDesc("购买失败，奖品剩余份数为0");
			    LogUtil.errorLog(this.getClass().getName(), methodName, "购买失败，奖品剩余份数为0", null);
			    _log.info("doPrizeBuy接口返回：" + resultBean);
			    return resultBean;
			}

			BigDecimal curBargainMoney = bargainService.getCurrentBargainMoney(requestBean.getPrizeId(), requestBean.getWechatId());
			if(curBargainMoney.compareTo(prize.getPrice()) < 0){
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			    resultBean.setStatusDesc("购买失败，奖品未砍到目标价格");
			    LogUtil.errorLog(this.getClass().getName(), methodName, "购买失败，奖品未砍到目标价格", null);
			    _log.info("doPrizeBuy接口返回：" + resultBean);
			    return resultBean;
			}
			
			boolean isHaveBought = bargainService.checkHaveBought(requestBean.getPrizeId(), requestBean.getWechatId());
			if(isHaveBought){
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			    resultBean.setStatusDesc("购买失败，您已经购买过改奖品");
			    LogUtil.errorLog(this.getClass().getName(), methodName, "购买失败，您已经购买过改奖品", null);
			    _log.info("doPrizeBuy接口返回：" + resultBean);
			    return resultBean;
			}
			
			int result = bargainService.updatePrizeBuy(requestBean);
			if(result > 0){
				resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
				resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
			}else{
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("购买失败");
				LogUtil.errorLog(this.getClass().getName(), methodName, "购买失败", null);
			}
		} catch (Exception e) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("购买失败");
			LogUtil.errorLog(this.getClass().getName(), methodName, "购买失败", e);
		}

        
        resultBean.setPrizeId(requestBean.getPrizeId());
        resultBean.setPrizeName(prize.getPrizeName());
        _log.info("doPrizeBuy接口返回：" + resultBean);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
  
    }
    
	/**
	 * 发送短信验证码（ajax请求） 短信验证码数据保存
	 */
	@ResponseBody
	@RequestMapping(value = BargainDefine.DO_SMSCODE, produces = "application/json; charset=utf-8")
	public SmsCodeResultBean sendSmsCode(@ModelAttribute SmsCodeRequestBean requestBean, HttpServletRequest request, HttpServletResponse response) {
		String methodName = "sendSmsCode";
		LogUtil.startLog(this.getClass().getName(), BargainDefine.DO_SMSCODE);

		_log.info("购买接口接收到的请求参数：" + requestBean);
		
		SmsCodeResultBean resultBean = new SmsCodeResultBean();
		
		String validCodeType = requestBean.getSmsCodeType();
		if (validCodeType == null) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
        	resultBean.setStatusDesc("请求参数非法");
        	LogUtil.errorLog(this.getClass().getName(), methodName, "请求参数非法", null);
            return resultBean;
		}
		if (!validCodeType.equals(BargainDefine.SMSCODE_TYPE_ACT)){
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("无效的验证码类型!");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "无效的验证码类型!", null);
	        return resultBean;
		}
		// 手机号码(必须,数字,最大长度)
		String mobile = requestBean.getPhoneNum();
		if (StringUtils.isBlank(mobile)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("请填写手机号!");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "请填写手机号!", null);
	        return resultBean;
		}
		if (!Validator.isMobile(mobile)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("手机号格式不正确!");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "手机号格式不正确!", null);
	        return resultBean;
		}
		
		//验签
        if(!this.checkSign(requestBean, BaseDefine.METHOD_DO_SMSCODE_SEND)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            LogUtil.errorLog(this.getClass().getName(), methodName, "验签失败！", null);
            return resultBean;
        }

		SmsConfig smsConfig = bargainService.getSmsConfig();
		System.out.println("smsConfig---------------------------------------" + JSON.toJSONString(smsConfig));
		// smsConfig.getMaxIpCount();

		// 判断发送间隔时间
		String intervalTime = RedisUtils.get(mobile + ":" + validCodeType + ":IntervalTime");
		System.out.println(mobile + ":" + validCodeType + "----------IntervalTime-----------" + intervalTime);
		if (StringUtils.isNotBlank(intervalTime)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("请求验证码操作过快");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "手机号格式不正确!", null);
	        return resultBean;
		}

//		String ip = GetCilentIP.getIpAddr(request);
//		String ipCount = RedisUtils.get(ip + ":MaxIpCount");
//		if (StringUtils.isBlank(ipCount) || !Validator.isNumber(ipCount)) {
//			ipCount = "0";
//			RedisUtils.set(ip + ":MaxIpCount", "0");
//		}
//		System.out.println(mobile + "------ip---" + ip + "----------MaxIpCount-----------" + ipCount);
//		if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
//			if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {
//				try {
//					bargainService.sendSms(mobile, "IP访问次数超限:" + ip);
//				} catch (Exception e) {
//					LogUtil.errorLog(this.getClass().getName(), BargainDefine.DO_SMSCODE, e);
//				}
//
//				RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
//
//			}
//			
//			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//	    	resultBean.setStatusDesc("该ip短信请求次数超限，请明日再试");
//	    	LogUtil.errorLog(this.getClass().getName(), methodName, "该ip短信请求次数超限，请明日再试", null);
//	        return resultBean;
//		}

		// 判断最大发送数max_phone_count
		String count = RedisUtils.get(mobile + ":MaxPhoneCount");
		if (StringUtils.isBlank(count) || !Validator.isNumber(count)) {
			count = "0";
			RedisUtils.set(mobile + ":MaxPhoneCount", "0");
		}
		System.out.println(mobile + "----------MaxPhoneCount-----------" + count);
		if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
			if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
				try {
					bargainService.sendSms(mobile, "手机验证码发送次数超限");
				} catch (Exception e) {
					LogUtil.errorLog(this.getClass().getName(), BargainDefine.DO_SMSCODE, e);
				}

				RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
			}
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("该手机号短信请求次数超限，请明日再试");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "该手机号短信请求次数超限，请明日再试", null);
	        return resultBean;
		}

		// 生成验证码
		String checkCode = GetCode.getRandomSMSCode(6);
		Map<String, String> param = new HashMap<String, String>();
		param.put("val_code", checkCode);
		// 发送短信验证码
		SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
		Integer result = (smsProcesser.gather(smsMessage) == 1) ? 0 : 1;

		// 短信发送成功后处理
		if (result != null && result == 0) {
			// 累计IP次数
//			String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
//			if (StringUtils.isBlank(currentMaxIpCount)) {
//				currentMaxIpCount = "0";
//			}
			// 累加手机次数
			String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
			if (StringUtils.isBlank(currentMaxPhoneCount)) {
				currentMaxPhoneCount = "0";
			}
//			RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
			RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
		}
		System.out.println("验证码:" + checkCode);
		// 保存短信验证码
		bargainService.saveSmsCode(mobile, checkCode, validCodeType, 0, CustomConstants.CLIENT_PC);
		// 发送checkCode最大时间间隔，默认60秒
		RedisUtils.set(mobile + ":" + validCodeType + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());
		
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
    	resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
    	_log.info("sendSmsCode接口返回：" + resultBean);
    	
    	LogUtil.endLog(this.getClass().getName(), BargainDefine.DO_SMSCODE);
    	return resultBean;
	}
	
}
