package com.hyjf.wechat.controller.user.synbalance;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.model.user.synbalance.WxSynBalanceResultBean;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 同步余额
 * @author Michael
 */
@Controller
@RequestMapping(value = WxSynBalanceDefine.REQUEST_MAPPING)
public class WxSynBalanceController extends BaseController {
	
	@Autowired
	SynBalanceService synBalanceService;

	/**
	 * 用户同步余额
	 */
	
	@SignValidate
	@ResponseBody
	@RequestMapping(value = WxSynBalanceDefine.INIT, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BaseResultBean synBalance(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(WxSynBalanceDefine.THIS_CLASS, WxSynBalanceDefine.INIT);
		WxSynBalanceResultBean result = new WxSynBalanceResultBean();
		// 获取登陆用户userId
		Integer userId = requestUtil.getRequestUserId(request);
        Users user = this.synBalanceService.getUsers(userId);
        if (Validator.isNull(user)) {
            result.setEnum(ResultEnum.ERROR_001);
            LogUtil.endLog(WxSynBalanceDefine.THIS_CLASS, WxSynBalanceDefine.INIT);
            return result;
        }
        /***********同步线下充值记录  pcc  start***********/
        JSONObject status=new JSONObject();
        if(user.getBankOpenAccount()==1){
            status=CommonSoaUtils.synBalanceRetPost(user.getUserId());
            if("成功".equals(status.get("statusDesc").toString())){
                //成功
            	DecimalFormat df = CustomConstants.DF_FOR_VIEW_V1;
            	BigDecimal bankBalance = new BigDecimal(status.get("bankBalance").toString().replaceAll(",", ""));
            	BigDecimal bankTotal = new BigDecimal(status.get("bankTotal").toString().replaceAll(",", ""));
                result.setEnum(ResultEnum.SUCCESS4);
                result.setBankBalance(df.format(bankBalance));
                result.setBankTotal(df.format(bankTotal));
                LogUtil.endLog(WxSynBalanceDefine.THIS_CLASS, WxSynBalanceDefine.INIT);
            }else{
                //异常
                result.setEnum(ResultEnum.FAIL);
                LogUtil.endLog(WxSynBalanceDefine.THIS_CLASS, WxSynBalanceDefine.INIT);
            }
        }else{
            result.setEnum(ResultEnum.USER_ERROR_200);
            LogUtil.endLog(WxSynBalanceDefine.THIS_CLASS, WxSynBalanceDefine.INIT);
            return result;
        }
        /***********同步线下充值记录  pcc  end***********/
        return result;
	}
}
