package com.hyjf.app.activity.list2018;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActdecListedFour;
import com.hyjf.mybatis.model.auto.ActdecListedOne;
import com.hyjf.mybatis.model.auto.ActdecListedThree;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.UsersInfo;
@Controller
@RequestMapping(value = ListActDefine.REQUEST_MAPPING)
public class ListActServerController extends BaseController {
	@Autowired
	private ListActService listActService;
	@Autowired
	private UserCouponService userCouponService;
	final String actid = PropUtils.getSystem("hyjf.act.dec.2018.list.id");
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
	public String select(HttpServletRequest request, HttpServletResponse response) {
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
				acl.setUserMobile(String.valueOf(((double) actdecListedThree.getSingle()) ));
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
	/*
	 * 查询中奖列表
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
	@RequestMapping(value = ListActDefine.GET_ACTIVITY1)
	public String select1(HttpServletRequest request, HttpServletResponse response) {
		String methodName = "select1";
		LogUtil.startLog(this.getClass().getName(), methodName);
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		String sign = request.getParameter("sign");
		JSONObject info = new JSONObject();
		int login=0;
		if(sign==null) {
			login=1;
		}
		Integer userId = SecretUtil.getUserIdNoException(sign);
		
		
		if(userId==null) {
			login=1;
		}
		info.put("login", login);
		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			info.put("status", "false");
			info.put("msg", "活动未开始");
			return JSONObject.toJSONString(info);
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			info.put("status", "false");
			info.put("msg", "活动已结束");
			return JSONObject.toJSONString(info);

		}
		if(login==1) {
			info.put("status", "true");
			info.put("msg", "未登录");
			return JSONObject.toJSONString(info);
		}

		if(listActService.getAct1(Integer.valueOf(userId))==null) {
			info.put("num", "0");
		}else {
			info.put("num", "1");
		}
		List<ActdecListedOne> a1 = listActService.getAct1List(Integer.valueOf(userId));
		List<ActdecListedOne> a2=new ArrayList<ActdecListedOne>();
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd ");
		for (int i = 0; i < a1.size(); i++) {
			ActdecListedOne ao=new ActdecListedOne();
			ao=a1.get(i);
			if(ao.getType()==0) {
				ao.setCreateUser("首");
			}else {
				ao.setCreateUser("尾");
			}
			long lt = new Long(ao.getUpdateTime());
	        Date date = new Date(lt*1000);
			ao.setUpdateUser( simpleDateFormat.format(date));
			ao.setOrderNumber(String.valueOf(ao.getReward())+"元代金券");
			ao.setInvestment(ao.getInvestment()/100);
			long ltt = new Long(ao.getCreateTime());
	        Date datee = new Date(ltt*1000);
			ao.setUserName( simpleDateFormat.format(datee));
			ao.setNumber(ao.getNumber().substring(0, 3)+"***"+ao.getNumber().substring(ao.getNumber().length()-2));
			a2.add(i, ao);
		}
		info.put("list", a2);
		info.put("status", "true");
		LogUtil.endLog(this.getClass().getName(), methodName);
		return JSONObject.toJSONString(info);
	}
	/*
	 * 领取礼盒
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
	public synchronized String actOne(HttpServletRequest request, HttpServletResponse response) {
		String methodName = "select1";
		LogUtil.startLog(this.getClass().getName(), methodName);
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		String sign = request.getParameter("sign");
		JSONObject info = new JSONObject();
		if(sign==null) {
			info.put("status", "99");
			return JSONObject.toJSONString(info);
		}
		Integer userId = SecretUtil.getUserIdNoException(sign);
		
	


		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			info.put("status", "false");
			info.put("msg", "活动未开始");
			return JSONObject.toJSONString(info);
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			info.put("status", "false");
			info.put("msg", "活动已结束");
			return JSONObject.toJSONString(info);

		}
		List<UsersInfo> uio = listActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			info.put("status", "false");
			info.put("msg", "未知错误");
			return JSONObject.toJSONString(info);
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			info.put("status", "false");
			info.put("msg", "内部员工不得参与!");
			return JSONObject.toJSONString(info);
		}
	
		ActdecListedOne a1 = listActService.getAct1(userId);
		if(a1==null) {
			info.put("status", "false");
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
			info.put("status", "false");
			info.put("msg", "发放优惠券失败");
			return JSONObject.toJSONString(info);
		}
		if ((retResult.get("status")).equals("0")) {
			info.put("status", "false");
			info.put("msg", "发放优惠券失败");
			return JSONObject.toJSONString(info);
		}
		LogUtil.endLog(this.getClass().getName(), methodName);
		info.put("reward", a1.getReward());
		info.put("status", "true");
		return JSONObject.toJSONString(info);
	}
	
}
