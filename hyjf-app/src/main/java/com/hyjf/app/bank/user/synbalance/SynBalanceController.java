package com.hyjf.app.bank.user.synbalance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.DateDistance;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 同步余额
 * @author Michael
 */
@Controller
@RequestMapping(value = SynBalanceDefine.REQUEST_MAPPING)
public class SynBalanceController extends BaseController {
	
	@Autowired
    AppSynBalanceService appSynBalanceService;

	/**
	 * 用户同步余额
	 */
	@ResponseBody
	@RequestMapping(value = SynBalanceDefine.INIT, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject synBalance(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT);
		
		JSONObject ret = new JSONObject();
		ret.put("request", SynBalanceDefine.REQUEST_HOME+SynBalanceDefine.REQUEST_MAPPING+SynBalanceDefine.INIT);
		String sign = request.getParameter("sign");
		// 获取sign缓存
		String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(token==null){
            ret.put("status",1);
            ret.put("statusDesc","用户未登录");
            LogUtil.endLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT);
            return ret; 
        }
        //判断用户是否登录
        Integer userId = SecretUtil.getUserId(sign);
        if(userId==null||userId<=0){
            ret.put("status",1);
            ret.put("statusDesc","用户未登录");
            LogUtil.endLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT);
            return ret; 
        }
        Users users= appSynBalanceService.getUsers(userId);
        /***********同步线下充值记录update  pcc  start***********/
        JSONObject status=new JSONObject();
        if(users.getBankOpenAccount()==1){
            status=CommonSoaUtils.synBalanceRetPost(users.getUserId());
            if("成功".equals(status.get("statusDesc").toString())){
                //成功
                ret.put("status",0);
                ret.put("statusDesc",status.get("bankBalance").toString());
                LogUtil.endLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT); 
            }else{
                //异常
                ret.put("status",1);
                ret.put("statusDesc",status.get("statusDesc").toString());
                LogUtil.endLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT);
            }
        }else{
            ret.put("status",1);
            ret.put("statusDesc","您尚未开通江西银行存管账户，请开户后重试。");
            LogUtil.endLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT);
        }
        /***********同步线下充值记录update  pcc  end***********/
		return ret;
	}
	
	public String getTxDate(Integer userId) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - 3);
        
        String startDate = dft.format(date.getTime());
        
        BankOpenAccount bankOpenAccount= appSynBalanceService.getBankOpenAccount(userId);
        if(bankOpenAccount==null){
            return GetOrderIdUtils.getTxDate();
        }
        Date createTime=bankOpenAccount.getCreateTime();
        String startDate1 ="";
        startDate1=dft.format(createTime);

        try {
            if((DateDistance.getDistanceDays2(GetOrderIdUtils.getTxDate(),startDate1))>=3){
                return startDate;
            }else{
                return startDate1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GetOrderIdUtils.getTxDate();
    }
}
