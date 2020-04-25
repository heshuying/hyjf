package com.hyjf.api.surong.user.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.surong.user.recharge.RdfRechargeService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.vip.apply.ApplyDefine;
@Controller
@RequestMapping(AccountDefine.REQUEST_MAPPING)
public class AccountServer extends BaseController{
	Logger _log = LoggerFactory.getLogger("hyjf账户查询Controller");
	
	@Autowired
	private RdfAccountService rdfAccountService;
	@Autowired
	private RdfRechargeService rdfRechargeService;
	
	@RequestMapping(AccountDefine.GET_BALANCE)
	@ResponseBody
	public Object getBalance(HttpServletRequest request){
		String mobile = request.getParameter("mobile");
		String sign = request.getParameter("sign");
		if(!checkSign(mobile,sign)){
			return null;
		}
		String balance=rdfAccountService.getBalance(mobile);
		Map<String, String> result = new HashMap<>();
		result.put("balance", balance);
		Users user = rdfRechargeService.findUserByMobile(mobile);
		Integer isSetPassword = user.getIsSetPassword();
		result.put("isSetPassword", isSetPassword==null?"0":isSetPassword.toString());
		_log.info("mobile:"+mobile+" -balance:"+result.get("balance")+" -isSetPassword:"+result.get("isSetPassword"));
		return result;
	}
	
	@RequestMapping(AccountDefine.GET_BANKCARD)
	@ResponseBody
	public Object getCard(HttpServletRequest request){
		String mobile = request.getParameter("mobile");
		String sign = request.getParameter("sign");
		if(!checkSign(mobile,sign)){
			return null;
		}
		BankCard bankCard = rdfAccountService.getBankCard(mobile);
		if(bankCard == null){
			return null;
		}
		Map<String, String> result = new HashMap<>();
		result.put("cardNo", bankCard.getCardNo());
		result.put("bankName", bankCard.getBank());
		return result;
	}
	
	/**
	 * 线下充值信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(AccountDefine.OFFLINERECHAGEINFO_ACTION)
	@ResponseBody
	public Object offLineRechageInfo(HttpServletRequest request, HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String sign = request.getParameter("sign");
		if(!checkSign(mobile,sign)){
			return null;
		}
		Map<String, String> result = new HashMap<>();
		Users user = rdfRechargeService.findUserByMobile(mobile);
		// 根据用户Id查询用户卡户信息
		BankOpenAccount bankOpenAccount = this.rdfAccountService.getBankOpenAccount(user.getUserId());
		if (bankOpenAccount != null && StringUtils.isNotEmpty(bankOpenAccount.getAccount())) {
			result.put("account", bankOpenAccount.getAccount());
		}
		// 根据用户Id获取用户信息
		UsersInfo usersInfo = this.rdfAccountService.getUsersInfoByUserId(user.getUserId());
		if (usersInfo != null) {
			result.put("userName", usersInfo.getTruename());
		}
		return result;
	}
	
	/**
	 * 融东风余额同步 
	 * @param request
	 * @return
	 */
	@RequestMapping(AccountDefine.BALANCE_SYNC)
	@ResponseBody
	public Object balanceSync(HttpServletRequest request){
		String sign = request.getParameter("sign");
		String ids = request.getParameter("ids");
		String random = request.getParameter("random");
		if(!checkSign(random,sign)){
			_log.info("融东风同步余额sign非法");
			return null;
		}
		_log.info("融东风同步余额:ids="+ids);
		List<Integer> userIds = null;
		try {
			userIds = (List<Integer>) JSONArray.parseArray(ids,Integer.class);
		} catch (Exception e) {
			_log.info("json集合转换出错! ids="+ids);
		}
		
		return rdfAccountService.balanceSync(userIds);
	}
		
	
	private boolean checkSign(String mobile,String sign){
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		String miwen =  MD5.toMD5Code(accessKey + mobile + accessKey);
		if(!miwen.equals(sign)){
			return false;
		}
		return true;
	}
	
	
	/**
     * 融东风余额同步 （新 实时同步江西银行）
     * @param request
     * @return
     */
    @RequestMapping(AccountDefine.BALANCE_ACTUAL_SYNC)
    @ResponseBody
    public Object balanceActualSync(HttpServletRequest request){
        LogUtil.startLog(AccountServer.class.getName(), AccountDefine.BALANCE_ACTUAL_SYNC);
        JSONObject ret = new JSONObject();
        //判断用户是否登录
        Integer userId = null;
        if(request.getParameter("userId") != null){
            userId = Integer.valueOf(request.getParameter("userId"));
        }
        if(userId == null || userId <= 0){
            ret.put("status",1);
            ret.put("statusDesc","用户未登录");
            LogUtil.endLog(AccountServer.class.getName(), AccountDefine.BALANCE_ACTUAL_SYNC);
            return ret; 
        }
        Users users=rdfAccountService.getUsers(userId);
        /***********同步线下充值记录update  pcc  start***********/
        JSONObject status=new JSONObject();
        if(users.getBankOpenAccount()==1){
            status=CommonSoaUtils.synBalanceRetPost(users.getUserId());
            if("成功".equals(status.get("statusDesc").toString())){
                //成功
                ret.put("status",0);
                ret.put("statusDesc",status.get("bankBalance").toString());
                LogUtil.endLog(AccountServer.class.getName(), AccountDefine.BALANCE_ACTUAL_SYNC);
            }else{
                //异常
                ret.put("status",1);
                ret.put("statusDesc",status.get("statusDesc").toString());
                LogUtil.endLog(AccountServer.class.getName(), AccountDefine.BALANCE_ACTUAL_SYNC);
            }
        }else{
            ret.put("status",1);
            ret.put("statusDesc","您尚未开通江西银行存管账户，请开户后重试。");
            LogUtil.endLog(AccountServer.class.getName(), AccountDefine.BALANCE_ACTUAL_SYNC);
        }
        /***********同步线下充值记录update  pcc  end***********/
        return ret;
    }
        
    
}
