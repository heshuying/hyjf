package com.hyjf.web.activity.activity518;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActdecFinancing;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.web.BaseController;
import com.hyjf.web.activity.activity201612wangdai.ActivityWangdaiController;
import com.hyjf.web.activity.activity201612wangdai.ActivityWangdaiDefine;
import com.hyjf.web.util.WebUtils;
@Controller
@RequestMapping(value = Act518Define.REQUEST_MAPPING)
public class Act518ServerController extends BaseController {
	@Autowired
	private Act518Service act518Service;
	@Autowired
	private UserCouponService userCouponService;
	final String actid = PropUtils.getSystem("hyjf.act.dec.2018.518.id");
	/*
	 * 查询排行
	 * @author ddddzs
	 * 
	 * @param form
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 		String sign = request.getParameter("sign");
		Integer userId = SecretUtil.getUserIdNoException(sign);
	 */
	@ResponseBody
	@RequestMapping(value = Act518Define.GET_ACTIVITY)
	public synchronized JSONObject actOne(HttpServletRequest request, HttpServletResponse response) {
		String methodName = "select1";
		LogUtil.startLog(this.getClass().getName(), methodName);
		ActivityList ayl = act518Service.getActivityDate(Integer.valueOf(actid));
		JSONObject info = new JSONObject();
		JSONObject info2 = new JSONObject();
		Integer userId = null;
		userId = WebUtils.getUserId(request);
		Integer money=0;
		if(userId==null) {
			info.put("status", "000");
			info.put("msg", "");
					info2.put("IsLogined",false);
					info.put("data", info2);
					return info;
		
		}
		info2.put("IsLogined",true);
		if(!StringUtils.isEmpty(request.getParameter("money"))) {
			money=Integer.valueOf(request.getParameter("money"));
		}
		
		String type=request.getParameter("type");


		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			info.put("status", "99");
			info.put("msg", "活动未开始");
			return info;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			info.put("status", "99");
			info.put("msg", "活动已结束");
			return info;

		}
		List<Users> user = act518Service.getUser(userId);
		List<UsersInfo> uio = act518Service.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			info.put("status", "99");
			info.put("msg", "未知错误");
			return info;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			info.put("status", "99");
			info.put("msg", "内部员工不得参与!");
			return info;
		}
	
		List<ActdecFinancing> a1 = act518Service.getAct518(String.valueOf(userId));


		//查询模式
		if(type.equals("2")) {
			info.put("status", "000");
			info.put("msg", "");
			if(a1==null) {
				info2.put("number",0);
			}else {
				info2.put("number", a1.size());
				for (int i = 0; i < a1.size(); i++) {
					if(a1.get(i).getType().equals(0)) {
						info2.put("cashCoupon", a1.get(i).getFaceValue());
						info2.put("termOfValidity", 3);
						info2.put("notUsed",true);
						info2.put("threshold", a1.get(i).getThreshold());
						info.put("data", info2);
						return info;
					}
					
				}
			}
			

			info2.put("notUsed",false);
			info.put("data", info2);
			return info;
		}
		if(a1!=null) {
			if(a1.size()==3) {
				info2.put("number", a1.size());
				info.put("data", info2);
				info.put("status", "00");
				info.put("msg", "已用完3个红包");
				return info;
			}
			for (int i = 0; i < a1.size(); i++) {
				if(a1.get(i).getType().equals(0)) {
					info.put("status", "00");
					info.put("msg", "还有红包未使用");
					info2.put("cashCoupon", a1.get(i).getFaceValue());
					info2.put("termOfValidity", 3);
					info2.put("notUsed",true);
					info2.put("threshold", a1.get(i).getThreshold());
					info.put("data", info2);
					return info;
				}
				
			}
		}


		JSONObject retResult = new JSONObject();
		UserCouponBean ucb = new UserCouponBean();
		ucb.setUserId(String.valueOf(userId));
		ucb.setActivityId(Integer.valueOf(actid));
		ActdecFinancing af=new ActdecFinancing();
		af.setUserId(String.valueOf(userId));
		af.setMobile(user.get(0).getMobile());
		af.setUserName(user.get(0).getUsername());
		af.setCreateTime(GetDate.getNowTime10());
		ucb.setSendFlg(Integer.valueOf(actid));
		if(money==200) {
			ucb.setRemark("0");
			af.setThreshold("100000");
			af.setFaceValue("200");
		}else if(money==190) {
			af.setThreshold("90000");
			af.setFaceValue("190");
			ucb.setRemark("1");
		}else if(money==180) {
			af.setThreshold("80000");
			af.setFaceValue("180");
			ucb.setRemark("2");
		}else if(money==170) {
			af.setThreshold("75000");
			af.setFaceValue("170");
			ucb.setRemark("3");
		}else if(money==160) {
			af.setThreshold("70000");
			af.setFaceValue("160");
			ucb.setRemark("4");
		}else if(money==150) {
			af.setThreshold("65000");
			af.setFaceValue("150");
			ucb.setRemark("5");
		}else if(money==140) {
			af.setThreshold("60000");
			af.setFaceValue("140");
			ucb.setRemark("6");
		}else if(money==130) {
			af.setThreshold("55000");
			af.setFaceValue("130");
			ucb.setRemark("7");
		}else if(money==120) {
			af.setThreshold("50000");
			af.setFaceValue("120");
			ucb.setRemark("8");
		}else if(money==110) {
			af.setThreshold("45000");
			af.setFaceValue("110");
			ucb.setRemark("9");
		}else if(money==100) {
			af.setThreshold("40000");
			af.setFaceValue("100");
			ucb.setRemark("10");
		}else if(money==90) {
			af.setThreshold("35000");
			af.setFaceValue("90");
			ucb.setRemark("11");
		}else if(money==80) {
			af.setThreshold("30000");
			af.setFaceValue("80");
			ucb.setRemark("12");
		}else if(money==70) {
			af.setThreshold("25000");
			af.setFaceValue("70");
			ucb.setRemark("13");
		}else if(money==60) {
			af.setThreshold("22000");
			af.setFaceValue("60");
			ucb.setRemark("14");
		}else if(money==50) {
			af.setThreshold("18000");
			af.setFaceValue("50");
			ucb.setRemark("15");
		}else if(money==40) {
			af.setThreshold("13000");
			af.setFaceValue("40");
			ucb.setRemark("16");
		}else if(money==30) {
			af.setThreshold("10000");
			af.setFaceValue("30");
			ucb.setRemark("17");
		}else if(money==20) {
			af.setThreshold("6000");
			af.setFaceValue("20");
			ucb.setRemark("18");
		}else if(money==10) {
			af.setThreshold("3000");
			af.setFaceValue("10");
			ucb.setRemark("19");
		}else {
			info.put("status", "99");
			info.put("msg", "发放优惠券失败");
			return info;
		}
		
		try {
			retResult = userCouponService.insertUserCoupon(ucb);
			System.out.println(retResult);
		} catch (Exception e) {
			e.printStackTrace();
			info.put("status", "99");
			info.put("msg", "发放优惠券失败");
			return info;
		}
		if ((retResult.get("status")).equals("0")) {
			info.put("status", "9");
			info.put("msg", "发放优惠券失败");
			return info;
		}
		JSONArray family = retResult.getJSONArray("retCouponUserCodes");
		af.setCreateUser(family.getString(0));
		act518Service.insertAct(af);
		LogUtil.endLog(this.getClass().getName(), methodName);
		info2.put("cashCoupon", af.getFaceValue());
		info2.put("threshold", af.getThreshold());
		info2.put("termOfValidity", 3);
		info2.put("notUsed",true);
		info.put("data", info2);
		info.put("reward", "1");
		info.put("status", "000");
		return info;
	}
	@RequestMapping(value = Act518Define.INIT_REQUEST_MAPPING)
    public ModelAndView getUserStatus(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(Act518ServerController.class.toString(), Act518Define.INIT_REQUEST_MAPPING);
        ModelAndView modelAndView = new ModelAndView(Act518Define.ACTIVITY_PATH);

        return modelAndView;
    }
}
