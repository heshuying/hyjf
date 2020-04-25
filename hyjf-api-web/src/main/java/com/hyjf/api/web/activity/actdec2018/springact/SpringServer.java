package com.hyjf.api.web.activity.actdec2018.springact;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.activity.actdec2018.springact.SpringActDefine;
import com.hyjf.activity.actdec2018.springact.SpringActResultBean;
import com.hyjf.activity.actdec2018.springact.SpringActService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.ActdecSpringList;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller
@RequestMapping(value = SpringActDefine.REQUEST_MAPPING)
public class SpringServer extends BaseController {
	@Autowired
	private SpringActService springActService;
	@Autowired
	private UserCouponService userCouponService;

	final String actid = PropUtils.getSystem("hyjf.act.dec.2018.spring.id");

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
	public SpringActResultBean select(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "select";
		LogUtil.startLog(this.getClass().getName(), methodName);
		ActivityList ayl = springActService.getActivityDate(Integer.valueOf(actid));
		SpringActResultBean resultBean = new SpringActResultBean();

		// 验签
		if (!this.checkSign(requestBean, SpringActDefine.REQUEST_MAPPING)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动活动id没有配置");
			return resultBean;
		}
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// calendar.add(Calendar.DAY_OF_MONTH, -3);
		date = calendar.getTime();

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动暂未开始，请稍后再来");
			return resultBean;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < (int) (date.getTime() / 1000)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
			return resultBean;
		}
		String userId = request.getParameter("userId");
		// 验证请求参数
		if (Validator.isNull(userId)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("请求参数非法");
			return resultBean;

		}
		List<UsersInfo> uio = springActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("未知错误");
			return resultBean;
		}
		ActdecSpringList act = springActService.getActdecSpringList(userId);
		if(!(act==null)) {
			resultBean.setAvailableNumber(act.getAvailableNumber());
			resultBean.setTotalNumber(act.getTotalNumber());
			resultBean.setNewRecharge(act.getNewRecharge());
			resultBean.setNewInvestment(act.getNewInvestment());
		}

		List<ActdecSpringList> acts = springActService.getActdecSpringLists(userId);
		if (acts.isEmpty()) {
			resultBean.setQuan("");
		} else {
			String i = "";
			for (ActdecSpringList actdecSpringList : acts) {

				i = i + actdecSpringList.getOperType() + "|";
			}
			resultBean.setQuan(i);

		}
		Account at=springActService.getAccount(Integer.valueOf(userId));
		resultBean.setZhongjiang(springActService.getZhongJiang(userId));
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
		resultBean.setBalance(String.valueOf(at.getBankBalance()));
		LogUtil.endLog(this.getClass().getName(), methodName);
		return resultBean;
	}

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
	@RequestMapping(value = SpringActDefine.EXCHANGE_ACTIVITY)
	public SpringActResultBean exchange(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "exchange";
		LogUtil.startLog(this.getClass().getName(), methodName);

		ActivityList ayl = springActService.getActivityDate(Integer.valueOf(actid));
		SpringActResultBean resultBean = new SpringActResultBean();

		// 验签
		if (!this.checkSign(requestBean, SpringActDefine.REQUEST_MAPPING)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动活动id没有配置");
			return resultBean;
		}
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// calendar.add(Calendar.DAY_OF_MONTH, -3);
		date = calendar.getTime();

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动暂未开始，请稍后再来");
			return resultBean;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < (int) (date.getTime() / 1000)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
			return resultBean;
		}
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");
		// 验证请求参数
		if (Validator.isNull(userId)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("请求参数非法");
			return resultBean;

		}
		List<UsersInfo> uio = springActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("未知错误");
			return resultBean;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("内部员工不可参与！");
			return resultBean;
		}
		ActdecSpringList act = springActService.getActdecSpringList(userId);
		if (act == null) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("碎片不足可刷新重试");
			return resultBean;
		}
		JSONObject retResult = new JSONObject();
		boolean stt = false;
		UserCouponBean ucb = new UserCouponBean();
		ucb.setUserId(userId);
		act.setCreateTime( GetDate.getNowTime10());
		act.setOperAmount(0);
		if (type.equals("1")) {
			// 1碎片不足可刷新重试
			if (act.getAvailableNumber() < 0) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("碎片不足可刷新重试");
				return resultBean;
			}
			act.setNumber(-1);
			act.setTotalNumber(act.getTotalNumber() );
			act.setAvailableNumber(act.getAvailableNumber() - 1);
			act.setId(null);
			act.setOperType(1);
			act.setReward("0.5%加息券兑换");
			stt = springActService.addActdecSpringList(act);
			ucb.setSendFlg(21);
		} else if (type.equals("2")) {
			// 2
			if (act.getAvailableNumber() < 2) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("碎片不足可刷新重试");
				return resultBean;
			}
			act.setNumber(-2);
			act.setTotalNumber(act.getTotalNumber() );
			act.setAvailableNumber(act.getAvailableNumber() - 2);
			act.setId(null);
			act.setOperType(2);
			act.setReward("1.0%加息券兑换");
			stt = springActService.addActdecSpringList(act);
			ucb.setSendFlg(22);
		} else if (type.equals("3")) {
			// 3
			if (act.getAvailableNumber() < 3) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("碎片不足可刷新重试");
				return resultBean;
			}
			act.setNumber(-3);
			act.setTotalNumber(act.getTotalNumber() );
			act.setAvailableNumber(act.getAvailableNumber() - 3);
			act.setId(null);
			act.setOperType(3);
			act.setReward("1.5%加息券兑换");
			stt = springActService.addActdecSpringList(act);
			ucb.setSendFlg(23);
		} else if (type.equals("4")) {
			// 5
			if (act.getAvailableNumber() < 5) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("碎片不足可刷新重试");
				return resultBean;
			}
			act.setNumber(-5);
			act.setTotalNumber(act.getTotalNumber() );
			act.setAvailableNumber(act.getAvailableNumber() - 5);
			act.setId(null);
			act.setOperType(4);
			act.setReward("2%加息券兑换");
			stt = springActService.addActdecSpringList(act);
			ucb.setSendFlg(24);
		}
		if (stt) {
			try {
				retResult = userCouponService.insertUserCoupon(ucb);
			} catch (Exception e) {
				e.printStackTrace();
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("发放优惠券失败");
				return resultBean;
			}
			if (!(retResult.get("status")).equals("0")) {
				// actSigninService.updateActRewardList(userId, new Integer(type),
				// ((List<String>)retResult.get("couponCode")).get(0), 1);
			} else {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("发放优惠券失败");
				return resultBean;
			}

		} else {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("发放优惠券失败");
			return resultBean;
		}
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);

		LogUtil.endLog(this.getClass().getName(), methodName);
		return resultBean;
	}

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
	@RequestMapping(value = SpringActDefine.BANDIT_ACTIVITY)
	public SpringActResultBean bandit(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "bandit";
		LogUtil.startLog(this.getClass().getName(), methodName);

		ActivityList ayl = springActService.getActivityDate(Integer.valueOf(actid));
		SpringActResultBean resultBean = new SpringActResultBean();

		// 验签
		if (!this.checkSign(requestBean, SpringActDefine.REQUEST_MAPPING)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动活动id没有配置");
			return resultBean;
		}
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// calendar.add(Calendar.DAY_OF_MONTH, -3);
		date = calendar.getTime();

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动暂未开始，请稍后再来");
			return resultBean;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < (int) (date.getTime() / 1000)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动已结束，下次要趁早哦！~");
			return resultBean;
		}
		String userId = request.getParameter("userId");
		// 验证请求参数
		if (Validator.isNull(userId)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("请求参数非法");
			return resultBean;

		}
		List<UsersInfo> uio = springActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("未知错误");
			return resultBean;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("内部员工不可参与！");
			return resultBean;
		}

		ActdecSpringList act = springActService.getActdecSpringList(userId);
		if (act == null) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("碎片不足可刷新重试");
			return resultBean;
		}
		act.setCreateTime( GetDate.getNowTime10());
		if (act.getAvailableNumber() > 0) {
			int random = new Random().nextInt(10000);
			act.setTotalNumber(act.getTotalNumber() );
			act.setAvailableNumber(act.getAvailableNumber() - 1);
			act.setNumber(-1);
			act.setOperAmount(0);
			act.setId(null);
			if (random == 1) {
				if (!RedisUtils.exists("ACTSpring1")) {
					RedisUtils.set("ACTSpring1", "1");
				}else {
					if(16<Integer.valueOf(RedisUtils.get("ACTSpring1"))) {
						resultBean.setStatus(BaseResultBean.STATUS_FAIL);
						resultBean.setStatusDesc("发放优惠券失败");
						return resultBean;
					}
					RedisUtils.set("ACTSpring1", String.valueOf(Integer.valueOf(RedisUtils.get("ACTSpring1"))+1));
				}
				act.setOperType(5);
				act.setReward("1000元京东E卡");
				springActService.addActdecSpringList(act);
				resultBean.setStatusDesc("5");
			}else
			if (random < 4) {
				if (!RedisUtils.exists("ACTSpring2")) {
					RedisUtils.set("ACTSpring2", "1");
				}else {
					if(32<Integer.valueOf(RedisUtils.get("ACTSpring2"))) {
						resultBean.setStatus(BaseResultBean.STATUS_FAIL);
						resultBean.setStatusDesc("发放优惠券失败");
						return resultBean;
					}
					RedisUtils.set("ACTSpring2", String.valueOf(Integer.valueOf(RedisUtils.get("ACTSpring2"))+1));
				}
				act.setOperType(6);
				act.setReward("500元京东E卡");
				springActService.addActdecSpringList(act);
				resultBean.setStatusDesc("6");
			}else
			if (random < 9) {
				if (!RedisUtils.exists("ACTSpring3")) {
					RedisUtils.set("ACTSpring3", "1");
				}else {
					if(80<Integer.valueOf(RedisUtils.get("ACTSpring3"))) {
						resultBean.setStatus(BaseResultBean.STATUS_FAIL);
						resultBean.setStatusDesc("发放优惠券失败");
						return resultBean;
					}
					RedisUtils.set("ACTSpring3", String.valueOf(Integer.valueOf(RedisUtils.get("ACTSpring3"))+1));
				}
				act.setOperType(7);
				act.setReward("200元京东E卡");
				springActService.addActdecSpringList(act);
				resultBean.setStatusDesc("7");
			}else
			if (random < 501) {
				act.setOperType(8);
				act.setReward("1%加息券");
				springActService.addActdecSpringList(act);
				resultBean.setStatusDesc("8");
				JSONObject retResult = new JSONObject();
				UserCouponBean ucb = new UserCouponBean();
				ucb.setUserId(userId);
				ucb.setSendFlg(22);
				try {
					retResult = userCouponService.insertUserCoupon(ucb);
				} catch (Exception e) {
					e.printStackTrace();
					resultBean.setStatus(BaseResultBean.STATUS_FAIL);
					resultBean.setStatusDesc("发放优惠券失败");
					return resultBean;
				}
				if (!(retResult.get("status")).equals("0")) {
					// actSigninService.updateActRewardList(userId, new Integer(type),
					// ((List<String>)retResult.get("couponCode")).get(0), 1);
				} else {
					resultBean.setStatus(BaseResultBean.STATUS_FAIL);
					resultBean.setStatusDesc("发放优惠券失败");
					return resultBean;
				}
			}else
			if (random < 1001) {
				act.setOperType(9);
				act.setReward("10张金彩碎片");
				act.setTotalNumber(act.getTotalNumber() + 10);
				act.setAvailableNumber(act.getAvailableNumber() + 10);
				springActService.addActdecSpringList(act);
				resultBean.setStatusDesc("9");
			}else 
			if (random > 1000) {
				 act.setOperType(10);
				 act.setReward("抽奖未中奖");
				springActService.addActdecSpringList(act);
				resultBean.setStatusDesc("10");
			}
		} else {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("碎片不足可刷新重试");
			return resultBean;
		}
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		

		LogUtil.endLog(this.getClass().getName(), methodName);
		return resultBean;
	}
	/*
	 * 充值
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
	@RequestMapping(value = SpringActDefine.RECHARGE_ACTIVITY)
	public SpringActResultBean recharge(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = springActService.getActivityDate(Integer.valueOf(actid));
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			return null;
		}
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// calendar.add(Calendar.DAY_OF_MONTH, -3);
		date = calendar.getTime();

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < (int) (date.getTime() / 1000)) {
			return null;
		}
		String userId = request.getParameter("userId");
		String money = request.getParameter("money");

		// 验证请求参数
		if (Validator.isNull(userId)) {
			return null;

		}
		List<UsersInfo> uio = springActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			return null;
		}
		ActdecSpringList act = springActService.getActdecSpringList(userId);
		if (act == null) {
			act=new ActdecSpringList();
			act.setCreateTime( GetDate.getNowTime10());
			List<Users> user = springActService.getUser(Integer.valueOf(userId));
			act.setUserName(user.get(0).getUsername());
			act.setUserId(userId);
			act.setUserMobile(user.get(0).getMobile());
			act.setOperType(11);
			act.setReward("充值");
			act.setTotalNumber(0);
			act.setAvailableNumber(0);
			act.setNewRecharge(Integer.valueOf(money));
			act.setNewInvestment(0);
			act.setOperAmount(Integer.valueOf(money));
			act.setNumber(0);
		}else {
			act.setOperAmount(Integer.valueOf(money));
			act.setCreateTime( GetDate.getNowTime10());
			act.setNewRecharge(act.getNewRecharge()+Integer.valueOf(money));
			act.setId(null);
			act.setOperType(11);
			act.setReward("充值");
			act.setNumber(0);
		}
		springActService.addActdecSpringList(act);
		return null;
	}
	/*提现
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
	@RequestMapping(value = SpringActDefine.WITHDRAW_ACTIVITY)
	public SpringActResultBean withdraw(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = springActService.getActivityDate(Integer.valueOf(actid));
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			return null;
		}
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// calendar.add(Calendar.DAY_OF_MONTH, -3);
		date = calendar.getTime();

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < (int) (date.getTime() / 1000)) {
			return null;
		}
		String userId = request.getParameter("userId");
		String money = request.getParameter("money");

		// 验证请求参数
		if (Validator.isNull(userId)) {
			return null;

		}
		List<UsersInfo> uio = springActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			return null;
		}
		ActdecSpringList act = springActService.getActdecSpringList(userId);
		if (act == null) {
			act=new ActdecSpringList();
			act.setCreateTime( GetDate.getNowTime10());
			List<Users> user = springActService.getUser(Integer.valueOf(userId));
			act.setUserName(user.get(0).getUsername());
			act.setUserId(userId);
			act.setUserMobile(user.get(0).getMobile());
			act.setOperType(12);
			act.setNumber(0);
			act.setReward("提现");
			act.setTotalNumber(0);
			act.setAvailableNumber(0);
			act.setNewRecharge(Integer.valueOf(money)*(-1));
			act.setNewInvestment(0);
			act.setOperAmount(Integer.valueOf(money)*(-1));
		}else {
			act.setOperAmount(Integer.valueOf(money)*(-1));
			act.setCreateTime( GetDate.getNowTime10());
			act.setNewRecharge(act.getNewRecharge()-Integer.valueOf(money));
			act.setId(null);
			act.setOperType(12);
			act.setReward("提现");
			act.setNumber(0);
		}
		springActService.addActdecSpringList(act);
		return null;
	}
	/*出借
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
	@RequestMapping(value = SpringActDefine.INVESTMENT_ACTIVITY)
	public SpringActResultBean investment(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = springActService.getActivityDate(Integer.valueOf(actid));
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			return null;
		}
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// calendar.add(Calendar.DAY_OF_MONTH, -3);
		date = calendar.getTime();

		// 活动时间检测
		if (ayl.getTimeStart() > GetDate.getNowTime10()) {
			return null;
		}
		// 活动时间检测
		if (ayl.getTimeEnd() < (int) (date.getTime() / 1000)) {
			return null;
		}
		String userId = request.getParameter("userId");
		String money = request.getParameter("money");

		// 验证请求参数
		if (Validator.isNull(userId)) {
			return null;

		}
		List<UsersInfo> uio = springActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			return null;
		}
		ActdecSpringList act = springActService.getActdecSpringList(userId);
		if (act == null) {
			act=new ActdecSpringList();
			act.setCreateTime( GetDate.getNowTime10());
			List<Users> user = springActService.getUser(Integer.valueOf(userId));
			act.setUserName(user.get(0).getUsername());
			act.setUserId(userId);
			act.setUserMobile(user.get(0).getMobile());
			act.setOperType(13);
			act.setNumber(Integer.valueOf(money)/10000);
			act.setReward("出借");
			act.setTotalNumber(Integer.valueOf(money)/10000);
			act.setAvailableNumber(Integer.valueOf(money)/10000);
			act.setNewRecharge(0);
			act.setNewInvestment(0);
			act.setOperAmount(Integer.valueOf(money));
		}else {
			act.setOperAmount(Integer.valueOf(money));
			act.setCreateTime( GetDate.getNowTime10());
			act.setReward("出借");
			act.setOperType(13);
			act.setId(null);
			int chongtou=0;
			int suipian=0;
			suipian=suipian+(Integer.valueOf(money)/10000);
			//判断冲投金额
			if(Integer.valueOf(money)<=act.getNewRecharge()) {
				chongtou=Integer.valueOf(money);
			}else {
				chongtou=act.getNewRecharge();
			}
			//判断冲投获得碎片和出借余数
			if(chongtou+act.getNewInvestment()>=10000) {
				suipian=suipian+((chongtou+act.getNewInvestment())/10000);
				act.setNewInvestment((chongtou+act.getNewInvestment())-(10000*((chongtou+act.getNewInvestment())/10000)));
			}else {
				act.setNewInvestment(chongtou+act.getNewInvestment());
			}
			act.setTotalNumber(act.getTotalNumber()+suipian);
			act.setAvailableNumber(act.getAvailableNumber()+suipian);
			act.setNumber(suipian);
			//判断冲投后新增充值是否为负数
			if(act.getNewRecharge()-Integer.valueOf(money)<1) {
				act.setNewRecharge(0);

			}else {
				act.setNewRecharge(act.getNewRecharge()-Integer.valueOf(money));
			}
		}
		springActService.addActdecSpringList(act);
		return null;
	}
}
