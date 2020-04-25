package com.hyjf.api.web.activity.actdec2017.corps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hyjf.activity.actdoubleeleven.bargain.BargainService;
import com.hyjf.activity.corps.ActCorpsDefine;
import com.hyjf.activity.corps.ActCorpsResultBean;
import com.hyjf.activity.corps.ActCorpsService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;

import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.ActdecCorps;
import com.hyjf.mybatis.model.auto.ActdecWinning;
import com.hyjf.mybatis.model.auto.ActivityList;

import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;


@Controller
@RequestMapping(value=ActCorpsDefine.REQUEST_MAPPING)
public class ActCorpsServer extends BaseController{

    @Autowired
    private ActCorpsService actCorpsService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
    @Autowired
    private BargainService bargainService;

	Logger _log = LoggerFactory.getLogger(ActCorpsServer.class);
    final int [] arr = {4,5,6,7,8,9,10};
    final String actid = PropUtils.getSystem("hyjf.act.dec.2017.balloon.id");
	
    /**
     * 
     * 
     * @author ddddzs
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActCorpsDefine.GET)
    public ActCorpsResultBean get(@ModelAttribute BaseBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActListData";
        LogUtil.startLog(this.getClass().getName(), methodName);

       
        ActivityList ayl=actCorpsService.getActivityDate(Integer.valueOf(actid));
        
        ActCorpsResultBean resultBean = new ActCorpsResultBean();
        
        // 验签
        if(!this.checkSign(requestBean, ActCorpsDefine.REQUEST_MAPPING)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        //检测活动id
       	if(StringUtils.isEmpty(actid)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动活动id没有配置");
            return resultBean;
    	}
        Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -3);  
        date = calendar.getTime();  
       	
        //活动时间检测
        if(ayl.getTimeStart()>GetDate.getNowTime10()) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动暂未开始，请稍后再来");
            return resultBean;
        } 
        //活动时间检测
        if(ayl.getTimeEnd()<(int)(date.getTime()/1000)) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
            return resultBean;
        }
        String opId = request.getParameter("opId");
        String cId = request.getParameter("corpsId");
        //验证请求参数
        if (Validator.isNull(opId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        if(!Validator.isNull(cId)) {
        	resultBean.setActdecWinning(actCorpsService.getActdecWinning(cId));
        	List<ActdecCorps> acs = new ArrayList<ActdecCorps>();
        	acs.add(actCorpsService.getActdecCorpsOne(cId));
            resultBean.setActdecCorps(acs);
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        }else {
        	List<ActdecCorps> acs = new ArrayList<ActdecCorps>();
        	List<ActdecCorps> acs2 = new ArrayList<ActdecCorps>();
        	List<ActdecCorps> acs3 = new ArrayList<ActdecCorps>();
        	List<ActdecCorps> acs4 = new ArrayList<ActdecCorps>();
        	for (ActdecCorps i : actCorpsService.getActdecCorps(opId)) {
        		if(i.getPrizeType()==2) {
        			acs4.add(i);
        		}else if(i.getPrizeType()==1) {
        			acs3.add(i);
        		}else if(!opId.equals(i.getCaptainOpid())) {
        			acs2.add(i);
        		}else {
        			acs.add(i);
        		}
				
			}
        	resultBean.setActdecCorps(acs);
        	resultBean.setActdecCorps2(acs2);
        	resultBean.setActdecCorps3(acs3);
        	resultBean.setActdecCorps4(acs4);
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        }

        return resultBean;
    }  
    /**
     * 
     * 
     * @author ddddzs
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActCorpsDefine.ADD)
    public ActCorpsResultBean add(@ModelAttribute BaseBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActListData";
        LogUtil.startLog(this.getClass().getName(), methodName);

        ActivityList ayl=actCorpsService.getActivityDate(Integer.valueOf(actid));
        
        ActCorpsResultBean resultBean = new ActCorpsResultBean();
        
        // 验签
        if(!this.checkSign(requestBean, ActCorpsDefine.REQUEST_MAPPING)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        //检测活动id
       	if(StringUtils.isEmpty(actid)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动活动id没有配置");
            return resultBean;
    	}
//        //活动时间检测
//        if(ayl.getTimeStart()>GetDate.getNowTime10() || ayl.getTimeEnd()<GetDate.getNowTime10()) {
//    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("活动未开始或已结束");
//            return resultBean;
//        } 
        Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -3);  
        date = calendar.getTime();  
        //活动时间检测
        if(ayl.getTimeStart()>GetDate.getNowTime10()) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动暂未开始，请稍后再来");
            return resultBean;
        } 
        //活动时间检测
        if(ayl.getTimeEnd()<(int)(date.getTime()/1000)) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
            return resultBean;
        }
        String opId = request.getParameter("opId");
        String name = request.getParameter("name");
        String head = request.getParameter("head");
        //验证请求参数
        if (Validator.isNull(opId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        int result=actCorpsService.addActdecCorps(opId, name, head);
        if(!(result==-1)) {
        	resultBean.setCorpsId(result);
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        }else {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("每人只能创建10个战队");
        }
        return resultBean;
    }
    /**
     * 
     * 
     * @author ddddzs
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActCorpsDefine.JOIN)
    public ActCorpsResultBean join(@ModelAttribute BaseBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActListData";
        LogUtil.startLog(this.getClass().getName(), methodName);

        ActivityList ayl=actCorpsService.getActivityDate(Integer.valueOf(actid));
        
        ActCorpsResultBean resultBean = new ActCorpsResultBean();
        
        // 验签
        if(!this.checkSign(requestBean, ActCorpsDefine.REQUEST_MAPPING)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        //检测活动id
       	if(StringUtils.isEmpty(actid)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动活动id没有配置");
            return resultBean;
    	}
//        //活动时间检测
//        if(ayl.getTimeStart()>GetDate.getNowTime10() || ayl.getTimeEnd()<GetDate.getNowTime10()) {
//    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("活动未开始或已结束");
//            return resultBean;
//        } 
        Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -3);  
        date = calendar.getTime();  
        //活动时间检测
        if(ayl.getTimeStart()>GetDate.getNowTime10()) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动暂未开始，请稍后再来");
            return resultBean;
        } 
        //活动时间检测
        if(ayl.getTimeEnd()<(int)(date.getTime()/1000)) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
            return resultBean;
        }
        String cid = request.getParameter("corpsId");
        String opId = request.getParameter("opId");
        String name = request.getParameter("name");
        String head = request.getParameter("head");
        //验证请求参数
        if (Validator.isNull(opId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
         ActdecCorps result = actCorpsService.joinActdecCorps(cid,opId, name, head);
        if(result.getId()==-1) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("每个人只能参加1个战队");
        }else if (result.getId()==-2) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("该战队已满");
        }
        else {
        	resultBean.setCaptainName(result.getCaptainName());
        	resultBean.setCaptainOpid(result.getCaptainOpid());
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        }
        return resultBean;
    }
    /**
     * 
     * 
     * @author ddddzs
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActCorpsDefine.DRAW)
    public ActCorpsResultBean draw(@ModelAttribute BaseBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActListData";
        LogUtil.startLog(this.getClass().getName(), methodName);

        ActivityList ayl=actCorpsService.getActivityDate(Integer.valueOf(actid));
        
        ActCorpsResultBean resultBean = new ActCorpsResultBean();
        
        // 验签
        if(!this.checkSign(requestBean, ActCorpsDefine.REQUEST_MAPPING)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        //检测活动id
       	if(StringUtils.isEmpty(actid)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动活动id没有配置");
            return resultBean;
    	}
//        //活动时间检测
//        if(ayl.getTimeStart()>GetDate.getNowTime10() || ayl.getTimeEnd()<GetDate.getNowTime10()) {
//    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("活动未开始或已结束");
//            return resultBean;
//        } 
        //活动时间检测
        Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -3);  
        date = calendar.getTime();  
        if(ayl.getTimeStart()> GetDate.getNowTime10()) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动暂未开始，请稍后再来");
            return resultBean;
        } 
        //活动时间检测
        if(ayl.getTimeEnd()<(int)(date.getTime()/1000)) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
            return resultBean;
        }
        String opId = request.getParameter("opId");
        String mobile = request.getParameter("mobile");
        String SmsCode = request.getParameter("smsCode");
        String cid = request.getParameter("corpsId");
        //验证请求参数
        if (Validator.isNull(opId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        ActdecCorps aco = actCorpsService.getActdecCorpsOne(cid);
        String uo=this.userOne(opId, aco);
        if(!"".equals(uo)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc(uo);
            return resultBean;
        }
        List<Users> aur = actCorpsService.getuser(mobile);
        if(aur.isEmpty()) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("手机号未注册，请先去注册");
            return resultBean;
        }
        if(!(aur.get(0).getBankOpenAccount()==1)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("该用户未开户,请先去开户");
            return resultBean;
        }
       List<UsersInfo> uio = actCorpsService.getUserInfo(aur.get(0).getUserId());
        
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
        int checkResult = bargainService.updateCheckMobileCode(mobile, SmsCode, "act", "0", 8, 9);
        //校验短信验证码
        if(checkResult != 1){
        	resultBean.setStatus("9");
            resultBean.setStatusDesc("短信验证码错误");
            LogUtil.errorLog(this.getClass().getName(), methodName, "短信验证码错误", null);
            _log.info("doBargainDouble接口返回：" + resultBean);
            return resultBean;
        }
        ActdecWinning aw=new ActdecWinning();
        aw.setCorpsId(Integer.valueOf(cid));
        aw.setUserId(String.valueOf(aur.get(0).getUserId()));
        aw.setUserName(aur.get(0).getUsername());
        aw.setMobile(mobile);
        aw.setWinningOpid(opId);
		int index=(int)(Math.random()*arr.length);
		int rand = arr[index];
		resultBean.setAmount(rand);
		aw.setAmount(rand);
        if(opId.equals(aco.getCaptainOpid())) {
        	aw.setWinningName(aco.getCaptainName());
        	aw.setWinningHead(aco.getCaptainHead());
            if(aco.getPrizeType()==2) {
            	resultBean.setAmount(-1);
            	aw.setAmount(0);
            }
    		
        }
        if(opId.equals(aco.getMember1Opid())) {
        	aw.setWinningName(aco.getMember1Name());
        	aw.setWinningHead(aco.getMember1Head());
            if(aco.getPrizeType()==2) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("队员不能领取苹果奖励");
                return resultBean;
            }
        }
        if(opId.equals(aco.getMember2Opid())) {
        	aw.setWinningName(aco.getMember2Name());
        	aw.setWinningHead(aco.getMember2Head());
            if(aco.getPrizeType()==2) {
                resultBean.setStatus(BaseResultBean.STATUS_FAIL);
                resultBean.setStatusDesc("队员不能领取苹果奖励");
                return resultBean;
            }
        }

        aw.setCorpsName(aco.getCaptainName()+","+aco.getMember1Name()+","+aco.getMember2Name());
        aw.setCaptainName(aco.getCaptainName());
        aw.setWinningType(aco.getPrizeType());

		
		aw.setType(1);
		aw.setCreateTime(GetDate.getNowTime10());
		String result=actCorpsService.addActdecWinning(aw,aur.get(0).getUserId(),uio.get(0));
        if("".equals(result)) {
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        }else {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc(result);
        }
        
	   
        
        return resultBean;
    }
    /**
     * 
     * 
     * @author ddddzs
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ActCorpsDefine.SEND)
    public ActCorpsResultBean send(@ModelAttribute BaseBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getActListData";
        LogUtil.startLog(this.getClass().getName(), methodName);

        ActivityList ayl=actCorpsService.getActivityDate(Integer.valueOf(actid));
        
        ActCorpsResultBean resultBean = new ActCorpsResultBean();
        
        // 验签
        if(!this.checkSign(requestBean, ActCorpsDefine.REQUEST_MAPPING)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        //检测活动id
       	if(StringUtils.isEmpty(actid)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动活动id没有配置");
            return resultBean;
    	}
//        //活动时间检测
//        if(ayl.getTimeStart()>GetDate.getNowTime10() || ayl.getTimeEnd()<GetDate.getNowTime10()) {
//    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc("活动未开始或已结束");
//            return resultBean;
//        } 
        Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -3);  
        date = calendar.getTime();  
        //活动时间检测
        if(ayl.getTimeStart()> GetDate.getNowTime10()) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动暂未开始，请稍后再来");
            return resultBean;
        } 
        //活动时间检测
        if(ayl.getTimeEnd()<(int)(date.getTime()/1000)) {
    		resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
            return resultBean;
        }
        String opId = request.getParameter("opId");
        String mob = request.getParameter("mobile");
        String validCodeType=request.getParameter("validCodeType");
        String cid = request.getParameter("corpsId");
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
        //验证请求参数
        if (Validator.isNull(opId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
//        ActdecWinning aw=new ActdecWinning();
//		String aaw=actCorpsService.addActdecWinning(aw);
//        if("".equals(aaw)) {
//            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
//            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
//        }else {
//            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
//            resultBean.setStatusDesc(aaw);
//        }
        ActdecCorps aco = actCorpsService.getActdecCorpsOne(cid);
        String uo=this.userOne(opId, aco);
        if(!"".equals(uo)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc(uo);
            return resultBean;
        }
		if (StringUtils.isBlank(mob)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("请填写手机号!");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "请填写手机号!", null);
	        return resultBean;
		}
		if (!isMobile(mob)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("手机号格式不正确!");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "手机号格式不正确!", null);
	        return resultBean;
		}
        List<Users> aur = actCorpsService.getuser(mob);
        if(aur.isEmpty()) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("手机号未注册，请先去注册");
            return resultBean;
        }
        if(!(aur.get(0).getBankOpenAccount()==1)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("该用户未开户,请先去开户");
            return resultBean;
        }
        

		SmsConfig smsConfig = bargainService.getSmsConfig();
		System.out.println("smsConfig---------------------------------------" + JSON.toJSONString(smsConfig));
		// smsConfig.getMaxIpCount();

		// 判断发送间隔时间
		String intervalTime = RedisUtils.get(mob + ":" + validCodeType + ":IntervalTime");
		System.out.println(mob + ":" + validCodeType + "----------IntervalTime-----------" + intervalTime);
		if (StringUtils.isNotBlank(intervalTime)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
	    	resultBean.setStatusDesc("请求验证码操作过快");
	    	LogUtil.errorLog(this.getClass().getName(), methodName, "手机号格式不正确!", null);
	        return resultBean;
		}


		// 判断最大发送数max_phone_count
		String count = RedisUtils.get(mob + ":MaxPhoneCount");
		if (StringUtils.isBlank(count) || !Validator.isNumber(count)) {
			count = "0";
			RedisUtils.set(mob + ":MaxPhoneCount", "0");
		}
		//System.out.println(mob + "----------MaxPhoneCount-----------" + count);
		if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
			if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
				try {
					bargainService.sendSms(mob, "手机验证码发送次数超限");
				} catch (Exception e) {
					LogUtil.errorLog(this.getClass().getName(), BargainDefine.DO_SMSCODE, e);
				}

				RedisUtils.set(mob + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
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
		SmsMessage smsMessage = new SmsMessage(null, param, mob, null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
		Integer result = (smsProcesser.gather(smsMessage) == 1) ? 0 : 1;

		// 短信发送成功后处理
		if (result != null && result == 0) {

			// 累加手机次数
			String currentMaxPhoneCount = RedisUtils.get(mob + ":MaxPhoneCount");
			if (StringUtils.isBlank(currentMaxPhoneCount)) {
				currentMaxPhoneCount = "0";
			}
			RedisUtils.set(mob + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
		}
		System.out.println("验证码:" + checkCode);
		// 保存短信验证码
		bargainService.saveSmsCode(mob, checkCode, validCodeType, 0, CustomConstants.CLIENT_PC);
		// 发送checkCode最大时间间隔，默认60秒
		RedisUtils.set(mob + ":" + validCodeType + ":IntervalTime", mob, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());
		
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
    	resultBean.setStatusDesc("短信验证码已发送");
    	_log.info("sendSmsCode接口返回：" + resultBean);
    	
    	LogUtil.endLog(this.getClass().getName(), BargainDefine.DO_SMSCODE);

        return resultBean;
    }  
	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	private static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
	/**
	 * 该用户中奖检测
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	private  String userOne(String opId,ActdecCorps aco) {
		if(aco==null) {
			return "未知战队";
		}
		if(aco.getPrizeType()==0) {
			return "该战队该用户未中奖";
		}
		if(aco.getPrizeType()==2&&!opId.equals(aco.getCaptainOpid())) {
			return "该用户未未中奖苹果多多";
		}
		if(aco.getPrizeType()==1) {
			
			if(opId.equals(aco.getCaptainOpid())||opId.equals(aco.getMember1Opid())||opId.equals(aco.getMember2Opid())) {
				
			}else {
				return "该用户未未中奖红包多多";
			}
			
		}
		return "";
	}
}
