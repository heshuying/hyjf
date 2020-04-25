package com.hyjf.web.bank.wechat.user.synbalance;

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
import com.hyjf.bank.service.user.synbalance.SynBalanceService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.DateDistance;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;

/**
 * 同步余额
 * @author Michael
 */
@Controller
@RequestMapping(value = WeChatSynBalanceDefine.REQUEST_MAPPING)
public class WeChatSynBalanceController extends BaseController {
	
	@Autowired
	SynBalanceService synBalanceService;

	/**
	 * 用户同步余额
	 */
	@ResponseBody
	@RequestMapping(value = WeChatSynBalanceDefine.INIT, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject synBalance(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(WeChatSynBalanceDefine.THIS_CLASS, WeChatSynBalanceDefine.INIT);
		
		
		JSONObject ret = new JSONObject();
		
		// 获取登陆用户userId
        String userIdStr = request.getParameter("userId");
        if (Validator.isNull(userIdStr)) {
            ret.put(WeChatSynBalanceDefine.STATUS, 1);
            ret.put(WeChatSynBalanceDefine.MESSAGE, "用户不存在！");
            return ret;
        }
        int userId = Integer.parseInt(userIdStr);
        Users user = this.synBalanceService.getUsers(userId);
        if (Validator.isNull(user)) {
            ret.put(WeChatSynBalanceDefine.STATUS, 1);
            ret.put(WeChatSynBalanceDefine.MESSAGE, "用户不存在！");
            return ret;
        }
        /***********同步线下充值记录  pcc  start***********/
        JSONObject status=new JSONObject();
        if(user.getBankOpenAccount()==1){
            status=CommonSoaUtils.synBalanceRetPost(user.getUserId());
            if("成功".equals(status.get("statusDesc").toString())){
                //成功
                ret.put(WeChatSynBalanceDefine.STATUS, 0);
                ret.put(WeChatSynBalanceDefine.MESSAGE, "更新成功");
                ret.put("bankBalance", status.get("bankBalance").toString());//余额数据
                ret.put("bankTotal", status.get("bankTotal").toString());//账户总额数据
                LogUtil.endLog(WeChatSynBalanceDefine.THIS_CLASS, WeChatSynBalanceDefine.INIT);
            }else{
                //异常
                ret.put(WeChatSynBalanceDefine.STATUS, 1);
                ret.put(WeChatSynBalanceDefine.MESSAGE, status.get("statusDesc").toString());
                LogUtil.endLog(WeChatSynBalanceDefine.THIS_CLASS, WeChatSynBalanceDefine.INIT);
            }
        }else{
            ret.put(WeChatSynBalanceDefine.STATUS, 1);
            ret.put(WeChatSynBalanceDefine.MESSAGE, "用户未开户");
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
