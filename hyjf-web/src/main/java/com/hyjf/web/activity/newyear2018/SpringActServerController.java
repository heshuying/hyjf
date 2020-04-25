package com.hyjf.web.activity.newyear2018;

import java.util.Calendar;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.ActdecSpringList;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.web.BaseController;

import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = SpringActDefine.REQUEST_MAPPING)
public class SpringActServerController extends BaseController {
	@Autowired
	private SpringActService springActService;


	/*
	 * 
	 * @author ddddzs
	 * 
	 * @param form
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SpringActDefine.GET_ACTIVITY)
	public String select(HttpServletRequest request, HttpServletResponse response) {
		String methodName = "select";
		LogUtil.startLog(this.getClass().getName(), methodName);
	//	String sign = request.getParameter("sign");
        Integer userId = null;
		userId = WebUtils.getUserId(request);
		JSONObject info = new JSONObject();
		if(userId==null) {
			info.put("status", "false");
			return JSONObject.toJSONString(info);
		}
		List<UsersInfo> uio = springActService.getUserInfo(userId);
		if (uio.isEmpty()) {
			info.put("status", "false");
			return JSONObject.toJSONString(info);
		}
		ActdecSpringList act = springActService.getActdecSpringList(String.valueOf(userId));
		if(!(act==null)) {

			info.put("availableNumber", act.getAvailableNumber());
			info.put("totalNumber", act.getTotalNumber());
			info.put("newRecharge", act.getNewRecharge());
			info.put("newInvestment", act.getNewInvestment());
		}else {
			info.put("availableNumber", "0");
			info.put("totalNumber", "0");
			info.put("newRecharge", "0");
			info.put("newInvestment", "0");
		}

		Account at=springActService.getAccount(Integer.valueOf(userId));
	
		info.put("balance", String.valueOf(at.getBankBalance()));
		info.put("status", "true");
		LogUtil.endLog(this.getClass().getName(), methodName);
		return JSONObject.toJSONString(info);
	}
	
    @RequestMapping(value = SpringActDefine.INIT)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(SpringActServerController.class.toString(), SpringActDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(SpringActDefine.INIT_PATH);

        LogUtil.endLog(SpringActServerController.class.toString(), SpringActDefine.INIT);
        return modelAndView;
        
    }

	
}
