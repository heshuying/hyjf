package com.hyjf.wechat.controller.activity.list2018;



import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.wechat.base.BaseBean;
import com.hyjf.wechat.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping(value = ListActDefine.REQUEST_MAPPING)
public class ListActServer extends BaseController {
	@Autowired
	private ListActService listActService;
	@Autowired
	private UserCouponService userCouponService;
	final String actid = PropUtils.getSystem("hyjf.act.dec.2018.list.id");
	Logger _log = LoggerFactory.getLogger(ListActServer.class);

	/*
	 * 查询排行
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
	@RequestMapping(value = ListActDefine.GET_ACTIVITY)
	public ListActResultBean select(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "select";
		_log.info("上市活动开始中,正在查询排名:");
		LogUtil.startLog(this.getClass().getName(), methodName);
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		ListActResultBean resultBean = new ListActResultBean();


		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动活动id没有配置");
			return resultBean;
		}

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动未开始");
			return resultBean;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动已结束");
			return resultBean;
		}

		String type = request.getParameter("type");
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
				if (actdecListedFour != null) {
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
				if (actdecListedFour != null && actdecListedFour.getCoverUserName() != null) {
					ActdecListedFour acl = new ActdecListedFour();
					acl.setUserName(actdecListedFour.getCoverUserName().substring(0, 1) + "**");
					acl.setUserMobile(String.valueOf(actdecListedFour.getUserId()));
					alf.add(acl);
				}

			}
		}
		resultBean.setAlf(alf);

		resultBean.setStatus("000");
		resultBean.setStatusDesc("成功");
		LogUtil.endLog(this.getClass().getName(), methodName);
		return resultBean;
	}

	/*
	 * 出借
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
	@RequestMapping(value = ListActDefine.INVESTMENT_ACTIVITY)
	public ListActResultBean investment(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			return null;
		}

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			return null;
		}
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");
		String money = request.getParameter("money");
		String num = request.getParameter("num");

		// 验证请求参数
		if (Validator.isNull(userId)) {
			return null;

		}

		// 验证请求参数
		if (Validator.isNull(type)) {
			return null;

		}
		// 验证请求参数
		if (Validator.isNull(money)) {
			return null;

		}
		// 验证请求参数
		if (Validator.isNull(num)) {
			return null;

		}
		_log.info("上市活动开始中,正在出借userId:" + userId + "**type:" + type + "**金钱:" + money + "**期限:" + num);
		List<UsersInfo> uio = listActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
//		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
//			return null;
//		}
		int newMoney = 0;

		// 天标
		if (type.equals("endday")) {
			newMoney = (int) Float.parseFloat(money) * Integer.valueOf(num) / 365;
		} else {
			newMoney = (int) Float.parseFloat(money) * Integer.valueOf(num) / 12;
		}
		ActdecListedThree las = listActService.getAct3(Integer.valueOf(userId));
		if (las == null) {
			List<Users> user = listActService.getUser(Integer.valueOf(userId));
			las = new ActdecListedThree();
			las.setUserId(Integer.valueOf(userId));
			las.setUserMobile(user.get(0).getMobile());
			las.setUserName(user.get(0).getUsername());
			las.setUserTureName(uio.get(0).getTruename());
			las.setSingle(newMoney);
			las.setCumulative(newMoney);
			las.setRegistrationTime(user.get(0).getRegTime());
			las.setCreateTime(GetDate.getNowTime10());
			las.setUpdateTime(GetDate.getNowTime10());
			listActService.insertAct3(las);
		} else {
			if (las.getSingle() < newMoney) {
				las.setSingle(newMoney);
			}
			las.setCumulative(las.getCumulative() + newMoney);
			las.setUpdateTime(GetDate.getNowTime10());
			listActService.updateAct3(las);
		}
		ActdecListedFour alf = listActService.getAct4(Integer.valueOf(userId));
		if (alf != null) {
			alf.setCumulative(alf.getCumulative() + newMoney);
			alf.setUpdateTime(GetDate.getNowTime10());
			listActService.updateAct4(alf);
		}
		return null;
	}

	/*
	 * 开户
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
	@RequestMapping(value = ListActDefine.OPEN_ACTIVITY)
	public ListActResultBean open(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			return null;
		}

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			return null;
		}
		String userId = request.getParameter("userId");

		// 验证请求参数
		if (Validator.isNull(userId)) {
			return null;

		}
		_log.info("上市活动开始中,正在开户userId:" + userId);
		List<UsersInfo> uio = listActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			return null;
		}
		List<Users> user = listActService.getUser(Integer.valueOf(userId));
		if (user.isEmpty()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeStart() > user.get(0).getRegTime()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < user.get(0).getRegTime()) {
			return null;
		}
		ActdecListedFour alf = new ActdecListedFour();
		alf.setUserId(Integer.valueOf(userId));
		alf.setUserMobile(user.get(0).getMobile());
		alf.setUserName(user.get(0).getUsername());
		alf.setUserTureName(uio.get(0).getTruename());
		alf.setCumulative(0);
		alf.setRegistrationTime(user.get(0).getRegTime());
		alf.setOpenTime(GetDate.getNowTime10());
		alf.setCreateTime(GetDate.getNowTime10());
		alf.setWhether(1);
		alf.setUpdateTime(GetDate.getNowTime10());
		int useridd = 0;
		useridd = listActService.getSpreads(Integer.valueOf(userId));
		_log.info("上市活动开始中,正在开户推荐人的是userId:" + useridd);
		if (useridd != 0) {
			List<Users> user2 = listActService.getUser(useridd);
			alf.setCoverUserId(useridd);
			alf.setCoverUserMobile(user2.get(0).getMobile());
			alf.setCoverUserName(user2.get(0).getUsername());
			List<UsersInfo> uio2 = listActService.getUserInfo(useridd);
			alf.setCoverUserTureName(uio2.get(0).getTruename());
		}
		listActService.insertAct4(alf);
		return null;
	}

	/*
	 * 查询中奖列表
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
	@RequestMapping(value = ListActDefine.GET_ACTIVITY1)
	public ListActResultBean select1(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "select1";
		LogUtil.startLog(this.getClass().getName(), methodName);
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		ListActResultBean resultBean = new ListActResultBean();
		//验证请求参数
		String userId = String.valueOf(requestUtil.getRequestUserId(request));
		if (Validator.isNull(userId)) {
			resultBean.setStatus("996");
			resultBean.setStatusDesc("未登录");
			return resultBean;
		}

		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动活动id没有配置");
			return resultBean;
		}

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动未开始");
			return resultBean;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动已结束");
			return resultBean;
		}

		_log.info("上市活动开始中,正在查询中奖列表userId:" + userId);
		if (listActService.getAct1(Integer.valueOf(userId)) != null) {
			resultBean.setNum("1");
		} else {
			resultBean.setNum("0");
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
		resultBean.setAlo(a2);

		resultBean.setStatus("000");
		resultBean.setStatusDesc("成功");
		LogUtil.endLog(this.getClass().getName(), methodName);
		return resultBean;
	}

	/*
	 * 领取礼盒
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
	public synchronized ListActResultBean actOne(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "select1";
		LogUtil.startLog(this.getClass().getName(), methodName);
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		ListActResultBean resultBean = new ListActResultBean();

		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动活动id没有配置");
			return resultBean;
		}

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动暂未开始，请稍后再来");
			return resultBean;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
			return resultBean;
		}
        String userId = String.valueOf(requestUtil.getRequestUserId(request));
        
        //验证请求参数
        if (Validator.isNull(userId)) {
            resultBean.setStatus("99");
            resultBean.setStatusDesc("未登录");
            return resultBean;
        }
		List<UsersInfo> uio = listActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("未知错误");
			return resultBean;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("内部员工不可参与！");
			return resultBean;
		}
		_log.info("上市活动开始中,正在领取礼盒userId:" + userId);
		ActdecListedOne a1 = listActService.getAct1(Integer.valueOf(userId));
		if (a1 == null) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("你未获得红包");
			return resultBean;
		}
		a1.setWhether(1);
		a1.setUpdateTime(GetDate.getNowTime10());
		JSONObject retResult = new JSONObject();
		UserCouponBean ucb = new UserCouponBean();
		ucb.setUserId(userId);
		int re = (int) (a1.getAnnual() * 0.01);
		if (re < 500) {
			ucb.setSendFlg(29);
			a1.setReward(5);
		} else if (re < 2100) {
			a1.setReward(8);
			ucb.setSendFlg(30);
		} else if (re < 3100) {
			a1.setReward(18);
			ucb.setSendFlg(31);
		} else if (re < 4100) {
			a1.setReward(28);
			ucb.setSendFlg(32);
		} else if (re < 6100) {
			a1.setReward(38);
			ucb.setSendFlg(33);
		} else if (re < 7100) {
			a1.setReward(58);
			ucb.setSendFlg(34);
		} else if (re < 8100) {
			a1.setReward(68);
			ucb.setSendFlg(35);
		} else if (re < 9100) {
			a1.setReward(78);
			ucb.setSendFlg(36);
		} else if (re < 10100) {
			a1.setReward(88);
			ucb.setSendFlg(37);
		} else {
			a1.setReward(100);
			ucb.setSendFlg(38);
		}
		listActService.updateAct1(a1);
		try {
			retResult = userCouponService.insertUserCoupon(ucb);
		} catch (Exception e) {
			e.printStackTrace();
			resultBean.setStatus("000");
			resultBean.setStatusDesc("发放优惠券失败");
			return resultBean;
		}
		if ((retResult.get("status")).equals("0")) {
			resultBean.setStatus("000");
			resultBean.setStatusDesc("发放优惠券失败");
			return resultBean;
		}
		
		resultBean.setReward(String.valueOf(a1.getReward()));
		LogUtil.endLog(this.getClass().getName(), methodName);
		resultBean.setStatus("000");
		resultBean.setStatusDesc(String.valueOf(a1.getReward()));
		return resultBean;
	}

	/*
	 * 放款
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
	@RequestMapping(value = ListActDefine.GET_ACTONEFANG)
	public ListActResultBean fang(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			return null;
		}

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < GetDate.getNowTime10()) {
			return null;
		}
		String nid = request.getParameter("borrowNid");
		_log.info("正在放款,上市活动开始中,标的号:" + nid);
		// 验证请求参数
		if (Validator.isNull(nid)) {
			return null;

		}
		int count = listActService.getAct1Count(nid);
		if (count != 0) {
			_log.info("正在放款,上市活动开始中,标的号:" + nid + "重复放款不进行记录");
			return null;
		}

		Borrow brw = listActService.getBorrow(nid);
		List<BorrowTender> bt = listActService.getBorrowTender(nid);
		_log.info("正在放款,上市活动开始中,出借人数:" + bt.size());
		BorrowTender bt1 = bt.get(0);// 首笔
		List<UsersInfo> uio = listActService.getUserInfo(bt1.getUserId());
		List<Users> user = listActService.getUser(bt1.getUserId());
		ActdecListedOne one = new ActdecListedOne();
		one.setUserId(bt1.getUserId());
		one.setUserTureName(uio.get(0).getTruename());
		one.setUserMobile(user.get(0).getMobile());
		one.setUserName(user.get(0).getUsername());
		one.setInvestment((bt1.getAccount().multiply(new BigDecimal("100"))).intValue());
		if (brw.getBorrowStyle().equals("endday")) {
			one.setAnnual((bt1.getAccount().multiply(new BigDecimal("100"))).intValue()
					* Integer.valueOf(brw.getBorrowPeriod()) / 365);
		} else {
			one.setAnnual((bt1.getAccount().multiply(new BigDecimal("100"))).intValue()
					* Integer.valueOf(brw.getBorrowPeriod()) / 12);
		}
		one.setNumber(nid);
		one.setOrderNumber(bt1.getNid());
		one.setType(0);
		one.setWhether(0);
		one.setCreateTime(GetDate.getNowTime10());
		listActService.insertAct1(one);

		if (bt.size() == 1) {
			return null;
		}
		_log.info(
				"正在放款,上市活动开始中,首个人userId:" + bt.get(0).getUserId() + "最后一个人userid" + bt.get(bt.size() - 1).getUserId());
		BorrowTender bt2 = bt.get(bt.size() - 1);// 最后一笔
		List<UsersInfo> uio2 = listActService.getUserInfo(bt2.getUserId());
		List<Users> user2 = listActService.getUser(bt2.getUserId());
		ActdecListedOne two = new ActdecListedOne();
		two.setUserId(bt2.getUserId());
		two.setUserTureName(uio2.get(0).getTruename());
		two.setUserMobile(user2.get(0).getMobile());
		two.setUserName(user2.get(0).getUsername());
		two.setInvestment((bt2.getAccount().multiply(new BigDecimal("100"))).intValue());
		if (brw.getBorrowStyle().equals("endday")) {
			two.setAnnual((bt2.getAccount().multiply(new BigDecimal("100"))).intValue()
					* Integer.valueOf(brw.getBorrowPeriod()) / 365);
		} else {
			two.setAnnual((bt2.getAccount().multiply(new BigDecimal("100"))).intValue()
					* Integer.valueOf(brw.getBorrowPeriod()) / 12);
		}
		two.setNumber(nid);
		two.setOrderNumber(bt2.getNid());
		two.setType(1);
		two.setWhether(0);
		two.setCreateTime(GetDate.getNowTime10());
		listActService.insertAct1(two);
		return null;
	}

	/*
	 * 获取活动时间
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
	@RequestMapping(value = ListActDefine.GET_TIME)
	public ListActResultBean actTime(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = listActService.getActivityDate(Integer.valueOf(actid));
		ListActResultBean resultBean = new ListActResultBean();
		resultBean.setTimeStart(ayl.getTimeStart());
		resultBean.setTimeEnd(ayl.getTimeEnd());
		resultBean.setStatus("000");
		resultBean.setStatusDesc("成功");
		return resultBean;

	}
}