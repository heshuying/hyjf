package com.hyjf.web.bank.web.user.synbalance;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.synbalance.SynBalanceAjaxBean;
import com.hyjf.bank.service.user.synbalance.SynBalanceService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.DateDistance;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.wechat.user.synbalance.WeChatSynBalanceDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;

/**
 * 同步余额
 * @author Michael
 */
@Controller
@RequestMapping(value = SynBalanceDefine.REQUEST_MAPPING)
public class SynBalanceController extends BaseController {
	
	@Autowired
	SynBalanceService synBalanceService;

	/**
	 * 用户同步余额
	 */
	@ResponseBody
	@RequestMapping(value = SynBalanceDefine.INIT, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public SynBalanceAjaxBean synBalance(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT);
		
		
		SynBalanceAjaxBean ret = new SynBalanceAjaxBean();
		WebViewUser user = WebUtils.getUser(request);//用户ID
		
		
		if(user == null){
			ret.setStatus(SynBalanceDefine.STATUS_FALSE);
			ret.setMessage("用户未登陆");
			return ret;
		}
		/***********同步线下充值记录  pcc  start***********/
		JSONObject status=new JSONObject();
        if(user.isBankOpenAccount()){
            status=CommonSoaUtils.synBalanceRetPost(user.getUserId());
            if("成功".equals(status.get("statusDesc").toString())){
              //成功
                ret.setStatus(SynBalanceDefine.STATUS_TRUE);
                ret.setInfo(status.get("bankBalance").toString());//余额数据
                LogUtil.endLog(SynBalanceDefine.THIS_CLASS, SynBalanceDefine.INIT);
            }else{
                ret.setStatus(SynBalanceDefine.STATUS_FALSE);
                ret.setMessage(status.get("statusDesc").toString());
                LogUtil.endLog(WeChatSynBalanceDefine.THIS_CLASS, WeChatSynBalanceDefine.INIT);
            }
        }else{
            ret.setStatus(SynBalanceDefine.STATUS_FALSE);
            ret.setMessage("用户未开户");
            LogUtil.endLog(WeChatSynBalanceDefine.THIS_CLASS, WeChatSynBalanceDefine.INIT);
            return ret;
        }
        /***********同步线下充值记录  pcc  end***********/
		
		
		return ret;
	}

	public String getTxDate(Integer userId) {
	    SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - 3);
        
        String startDate = dft.format(date.getTime());
        
        BankOpenAccount bankOpenAccount=synBalanceService.getBankOpenAccount(userId);
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
