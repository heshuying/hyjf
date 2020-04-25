package com.hyjf.web.activity.list2018;


import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.web.BaseController;
import com.hyjf.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = ListActDefine.REQUEST_MAPPING)
public class ListActServerController extends BaseController {
	final String actid = PropUtils.getSystem("hyjf.act.dec.2018.list.id");
	@Autowired
	private ListActService listActService;
	@Autowired
	private UserCouponService userCouponService;
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
	@RequestMapping(value = ListActDefine.GET_ACTONE)
	public synchronized String select(HttpServletRequest request, HttpServletResponse response) {
		String methodName = "select";
		LogUtil.startLog(this.getClass().getName(), methodName);
		// String sign = request.getParameter("sign");
		Integer userId = null;
		userId = WebUtils.getUserId(request);
		JSONObject info = new JSONObject();
		if (userId == null) {
			info.put("status", false);
			return JSONObject.toJSONString(info);
		}
		

		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			info.put("status", false);
			info.put("msg", "活动暂未开始，请稍后再来");
			return JSONObject.toJSONString(info);
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			info.put("status", false);
			info.put("msg", "活动已结束，下次要趁早哦！~");
			return JSONObject.toJSONString(info);

		}
		List<UsersInfo> uio = listActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			info.put("status",false);
			info.put("msg", "未知错误");
			return JSONObject.toJSONString(info);
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			info.put("status", false);
			info.put("msg", "内部员工不得参与!");
			return JSONObject.toJSONString(info);
		}
	
		ActdecListedOne a1 = listActService.getAct1(userId);
		if(a1==null) {
			info.put("status", false);
			info.put("msg", "无红包");
			return JSONObject.toJSONString(info);
		}
		a1.setWhether(1);
		a1.setUpdateTime(GetDate.getNowTime10());
		JSONObject retResult = new JSONObject();
		UserCouponBean ucb = new UserCouponBean();
		ucb.setUserId(String.valueOf(userId));
		int re=(int) (a1.getAnnual()*0.01);
		if(re<500) {
			ucb.setSendFlg(29);
			a1.setReward(5);
		}else if(re<2100) {
			a1.setReward(8);
			ucb.setSendFlg(30);
		}else if(re<3100) {
			a1.setReward(18);
			ucb.setSendFlg(31);
		}else if(re<4100) {
			a1.setReward(28);
			ucb.setSendFlg(32);
		}else if(re<6100) {
			a1.setReward(38);
			ucb.setSendFlg(33);
		}else if(re<7100) {
			a1.setReward(58);
			ucb.setSendFlg(34);
		}else if(re<8100) {
			a1.setReward(68);
			ucb.setSendFlg(35);
		}else if(re<9100) {
			a1.setReward(78);
			ucb.setSendFlg(36);
		}else if(re<10100) {
			a1.setReward(88);
			ucb.setSendFlg(37);
		}else {
			a1.setReward(100);
			ucb.setSendFlg(38);
		}
		listActService.insertAct1(a1);
		try {
			retResult = userCouponService.insertUserCoupon(ucb);
		} catch (Exception e) {
			e.printStackTrace();
			info.put("status", false);
			info.put("msg", "发放优惠券失败");
			return JSONObject.toJSONString(info);
		}
		if ((retResult.get("status")).equals("0")) {
			info.put("status", false);
			info.put("msg", "发放优惠券失败");
			return JSONObject.toJSONString(info);
		}
		
		LogUtil.endLog(this.getClass().getName(), methodName);
		info.put("reward", a1.getReward());
		info.put("status", true);
		LogUtil.endLog(this.getClass().getName(), methodName);
		return JSONObject.toJSONString(info);
	}

	@RequestMapping(value = ListActDefine.INIT_ACTION)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ListActServerController.class.toString(), ListActDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ListActDefine.INIT_PATH);
		Integer userId = null;
		userId = WebUtils.getUserId(request);
		//userId=4351;
		String userids="";
		if (userId != null) {
			 userids=String.valueOf(userId);
		}
		
		String msg = "1";
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			modelAndView.addObject("userId", userids);
			modelAndView.addObject("num",0);
			modelAndView.addObject("msg","0");
			return modelAndView;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			modelAndView.addObject("userId", userids);
			modelAndView.addObject("num",0);
			modelAndView.addObject("msg","2");
			return modelAndView;
		}
		if (userId == null) {
			modelAndView.addObject("userId", userids);
			modelAndView.addObject("num",0);
			modelAndView.addObject("msg","1");
			return modelAndView;
		}
		modelAndView.addObject("userId",userId);
		int num=0;
		if(listActService.getAct1(userId)!=null) {
			num=1;
		}
		modelAndView.addObject("num",num);
		modelAndView.addObject("msg",msg);
		List<ActdecListedOne> list = listActService.getAct1List(Integer.valueOf(userId));
		modelAndView.addObject("list",list);
		LogUtil.endLog(ListActServerController.class.toString(), ListActDefine.INIT_ACTION);
		return modelAndView;

	}
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
	@RequestMapping(value = ListActDefine.GET_ACTIVITY)
	public String select3(HttpServletRequest request, HttpServletResponse response) {
		String methodName = "select";
		LogUtil.startLog(this.getClass().getName(), methodName);
		String type = request.getParameter("type");
		JSONObject info = new JSONObject();
		if(type==null) {
			info.put("status", "false");
			return JSONObject.toJSONString(info);
		}
		List<ActdecListedFour> alf = new ArrayList<>();
		if (type.equals("2")) {// 单笔排名
			List<ActdecListedThree> act3 = listActService.getAct3List(1);

			for (ActdecListedThree actdecListedThree : act3) {
				ActdecListedFour acl = new ActdecListedFour();
				acl.setUserName(actdecListedThree.getUserName().substring(0, 1) + "**");
				acl.setUserMobile(String.valueOf(((double) actdecListedThree.getSingle())));
				alf.add(acl);
			}
		}
		if (type.equals("1")) {// 累计
			List<ActdecListedThree> act3 = listActService.getAct3List(2);

			for (ActdecListedThree actdecListedThree : act3) {
				ActdecListedFour acl = new ActdecListedFour();
				acl.setUserName(actdecListedThree.getUserName().substring(0, 1) + "**");
				acl.setUserMobile(String.valueOf(((double) actdecListedThree.getCumulative())));
				alf.add(acl);
			}
		}
		if (type.equals("3")) {// 新用户累计
			List<ActdecListedFour> a4 = listActService.getAct4List(1);
			for (ActdecListedFour actdecListedFour : a4) {
				ActdecListedFour acl = new ActdecListedFour();
				acl.setUserName(actdecListedFour.getUserName().substring(0, 1) + "**");
				acl.setUserMobile(String.valueOf(((double) actdecListedFour.getCumulative())));
				alf.add(acl);
			}
		}
		if (type.equals("5")) {// 推荐人累计
			List<ActdecListedFour> a4 = listActService.getAct4List(2);
			for (ActdecListedFour actdecListedFour : a4) {
				if(actdecListedFour!=null) {
					ActdecListedFour acl = new ActdecListedFour();
					acl.setUserName(actdecListedFour.getCoverUserName().substring(0, 1) + "**");
					acl.setUserMobile(String.valueOf(((double) actdecListedFour.getCumulative())));
					alf.add(acl);
				}
			}
		}
		if (type.equals("4")) {// 邀请好友数
			List<ActdecListedFour> a4 = listActService.getAct4List(3);
			for (ActdecListedFour actdecListedFour : a4) {
				if(actdecListedFour!=null&&actdecListedFour.getCoverUserName()!=null) {
					ActdecListedFour acl = new ActdecListedFour();
					acl.setUserName(actdecListedFour.getCoverUserName().substring(0, 1) + "**");
					acl.setUserMobile(String.valueOf(actdecListedFour.getUserId()));
					alf.add(acl);
				}

			}
		}
		info.put("list", alf);
		info.put("status", "true");
		LogUtil.endLog(this.getClass().getName(), methodName);
		return JSONObject.toJSONString(info);
	}

}
