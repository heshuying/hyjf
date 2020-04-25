package com.hyjf.api.web.activity.actten;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.activity.actten.act1.ActSigninDefine;
import com.hyjf.activity.actten.act1.ActSigninResultBean;
import com.hyjf.activity.actten.act1.ActSigninService;
import com.hyjf.activity.corps.ActCorpsService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActRewardList;
import com.hyjf.mybatis.model.auto.ActSignin;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller
@RequestMapping(value=ActSigninDefine.REQUEST_MAPPING)
public class ActSigninServer extends BaseController{

    @Autowired
    private ActSigninService actSigninService;
    
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private ActCorpsService actCorpsService;
	SimpleDateFormat df2 = new SimpleDateFormat("dd");
    /**
     * 
     * 获取活动列表数据
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActSigninDefine.GET_ACTIVITY_LIST)
    public ActSigninResultBean getActListData(@ModelAttribute BaseBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActListData";
        LogUtil.startLog(this.getClass().getName(), methodName);

        String actid = PropUtils.getSystem("hyjf.actten2017.id");
        ActivityList ayl=actSigninService.getActivityDate(Integer.valueOf(actid));
        
        ActSigninResultBean resultBean = new ActSigninResultBean();
        resultBean.setSysDate(GetDate.getNowTime10());
       
        // 验签
        if(!this.checkSign(requestBean, ActSigninDefine.GET_ACTIVITY_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String mobile = request.getParameter("mobile");
        String type = request.getParameter("type");
       	if(StringUtils.isEmpty(actid)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("2018元旦活动id没有配置");
            return resultBean;
    	}else{
            resultBean.setTimeStart(ayl.getTimeStart());
            resultBean.setTimeEnd(ayl.getTimeEnd());
            

    	}
        //验证请求参数
        if (Validator.isNull(userId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }

        if (Validator.isNull(userName)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        if (Validator.isNull(mobile)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        if (Validator.isNull(type)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
    	
    	UserCouponBean ucb=new UserCouponBean();
    	ucb.setUserId(userId);
    	JSONObject retResult = new JSONObject();
    	boolean stt;
    	
    	List<ActRewardList> asl=actSigninService.getActRewardList(userId);
    	String receiveSum="";
    	for (ActRewardList actRewardList : asl) {
    		receiveSum=receiveSum+String.valueOf(actRewardList.getAct1RewardType())+"|";
		}
    	resultBean.setReceiveSum(receiveSum);
    	
    	int typeNum=0;
        if(type.equals("1")){
            List<UsersInfo> uio = actCorpsService.getUserInfo(new Integer(userId));
            if(uio.isEmpty()) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("未知错误");
                return resultBean;
            }
            if(uio.get(0).getAttribute()==2||uio.get(0).getAttribute()==3){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("内部员工不可参与！");
                return resultBean;
            }
        	
            Random r = new Random();
		    int n = r.nextInt(10);
		    if (n < 2) {
		    	typeNum=15;
		    	resultBean.setStatusDesc("28元代金券");
		    } else if (n >= 2 && n < 5) {
		    	typeNum=16;
		    	resultBean.setStatusDesc("66元代金券");
		    } else {
		    	typeNum=17;
		    	resultBean.setStatusDesc("0.5%加息券");
		    }
		    ucb.setSendFlg(typeNum);
        	//领取优惠券1
        	stt=actSigninService.setActRewardList(userId, userName, mobile,new Integer(type));
        	if(stt){
            	try {
            		retResult=userCouponService.insertUserCoupon(ucb);
    			} catch (Exception e) {
    				e.printStackTrace();
    	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
    	             resultBean.setStatusDesc("发放优惠券失败");
    	             return resultBean;
    			}
            	if(!( retResult.get("status")).equals("0")){
            		actSigninService.updateActRewardList(userId, new Integer(type), ((List<String>)retResult.get("couponCode")).get(0), 1);
            	}else{
   	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	             resultBean.setStatusDesc("发放优惠券失败");
	             return resultBean;
            	}
            	
        	}else{
	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	             resultBean.setStatusDesc("发放优惠券失败");
	             return resultBean;
        	}
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            LogUtil.endLog(this.getClass().getName(), methodName);
            return resultBean;
        }else if (type.equals("2")){//领取优惠券2
            List<UsersInfo> uio = actCorpsService.getUserInfo(new Integer(userId));
            if(uio.isEmpty()) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("未知错误");
                return resultBean;
            }
            if(uio.get(0).getAttribute()==2||uio.get(0).getAttribute()==3){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("内部员工不可参与！");
                return resultBean;
            }
        	
            Random r = new Random();
		    int n = r.nextInt(10);
		    if (n < 3) {
		    	typeNum=18;
		    	resultBean.setStatusDesc("88元代金券");
		    } else {
		    	typeNum=19;
		    	resultBean.setStatusDesc("1%加息券");
		    }
		    stt=actSigninService.setActRewardList(userId, userName, mobile,new Integer(type));
		    ucb.setSendFlg(typeNum);
        	if(stt){
            	try {
            		retResult=userCouponService.insertUserCoupon(ucb);
    			} catch (Exception e) {
    				e.printStackTrace();
    	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
    	             resultBean.setStatusDesc("发放优惠券失败");
    	             return resultBean;
    			}
            	if(!( retResult.get("status")).equals("0")){
            		actSigninService.updateActRewardList(userId, new Integer(type), ((List<String>)retResult.get("couponCode")).get(0), 1);
            	}else{
   	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	             resultBean.setStatusDesc("发放优惠券失败");
	             return resultBean;
            	}
        	}else{
	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	             resultBean.setStatusDesc("发放优惠券失败");
	             return resultBean;
        	}
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            LogUtil.endLog(this.getClass().getName(), methodName);
            return resultBean;
        }else if (type.equals("3")){
            List<UsersInfo> uio = actCorpsService.getUserInfo(new Integer(userId));
            if(uio.isEmpty()) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("未知错误");
                return resultBean;
            }
            if(uio.get(0).getAttribute()==2||uio.get(0).getAttribute()==3){
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("内部员工不可参与！");
                return resultBean;
            }
        	//领取优惠券3
        	stt=actSigninService.setActRewardList(userId, userName, mobile,new Integer(type));
        	ucb.setSendFlg(20);
        	if(stt){
            	try {
            		retResult=userCouponService.insertUserCoupon(ucb);
            		resultBean.setStatusDesc("2%加息券");
    			} catch (Exception e) {
    				e.printStackTrace();
    	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
    	             resultBean.setStatusDesc("发放优惠券失败");
    	             return resultBean;
    			}
            	if(!( retResult.get("status")).equals("0")){
            		actSigninService.updateActRewardList(userId, new Integer(type), ((List<String>)retResult.get("couponCode")).get(0), 1);
            	}else{
   	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	             resultBean.setStatusDesc("发放优惠券失败");
	             return resultBean;
            	}
        	}else{
	        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	             resultBean.setStatusDesc("发放优惠券失败");
	             return resultBean;
        	}
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            LogUtil.endLog(this.getClass().getName(), methodName);
            return resultBean;
        }else if (type.equals("4")){
        	//查询签到
        	List<ActSignin> asn=actSigninService.getActSignin(userId);
        
        	resultBean.setSignSum(asn.size());
        	String signDate="";
        
        	String signnow="";
        	for (ActSignin actSignin : asn) {
        		signDate=signDate+String.valueOf(actSignin.getSignTime())+"|";
        		//检查今天是否签到
        		if(actSignin.getSignTime().toString().equals(df2.format(new Date()))){
        			signnow=df2.format(new Date());
        		}
			}
        	resultBean.setSignDate(signDate);
        	resultBean.setSignNow(signnow);

        }else if (type.equals("5")){
            if(ayl.getTimeStart()>GetDate.getNowTime10() || ayl.getTimeEnd()<GetDate.getNowTime10()) {
        		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	            resultBean.setStatusDesc("活动未开始或已结束");
	            return resultBean;
            }
        	//签到
        	//需要插入月份，测试改为9月，上生产需要改成10月
//    		if(!df.format(new Date()).equals("09")){
//        		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//	             resultBean.setStatusDesc("该月无法进行签到");
//	             return resultBean;
//    		}
        	stt=actSigninService.setActSignin(userId, userName, mobile);
        	if(!stt){
        		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	             resultBean.setStatusDesc("签到失败");
	             return resultBean;
        	}
        }else{
        	 resultBean.setStatus(BaseResultBean.STATUS_FAIL);
             resultBean.setStatusDesc("请求参数非法");
             return resultBean;
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }

}
