package com.hyjf.activity.actdec.listed2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.activity.actdec.listed2.ListedTwoActDefine;
import com.hyjf.activity.actdec.listed2.ListedTwoActResultBean;
import com.hyjf.activity.actdec.listed2.ListedTwoActService;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.coupon.UserCouponBean;
import com.hyjf.bank.service.coupon.UserCouponService;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActdecListedTwo;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller
@RequestMapping(value = ListedTwoActDefine.REQUEST_MAPPING)
public class ListedTwoActServer extends BaseController {
	@Autowired
	private ListedTwoActService listedTwoActService;
	@Autowired
	private UserCouponService userCouponService;

	final String actid = PropUtils.getSystem("hyjf.act.dec.2018.list.id");
	
	Logger _log = LoggerFactory.getLogger(ListedTwoActServer.class);

	/**
	 * 页面查询数据接口
	 * 
	 * @author liushouyi
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ListedTwoActDefine.GET_ACTIVITY)
	public ListedTwoActResultBean select(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "select";
		LogUtil.startLog(this.getClass().getName(), methodName);
		ListedTwoActResultBean resultBean = new ListedTwoActResultBean();

		// 验签
		if (!this.checkSign(requestBean, ListedTwoActDefine.REQUEST_MAPPING)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		// 检测活动id
		if (StringUtils.isBlank(actid)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动活动id没有配置");
			return resultBean;
		}
		// 根据活动id获取活动信息
		ActivityList ayl = listedTwoActService.getActivityDate(Integer.valueOf(actid));
		if (null == ayl) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("未查询到活动信息");
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
		//获取用户信息
		List<UsersInfo> uio = listedTwoActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("用户详细信息不存在，请联系客服");
			return resultBean;
		}
		
		//查询该用户的最新冲投信息（领奖金额、当前冲投金额）
		//领奖金额有值：说明该用户已领取奖励、已领取奖励的加息券显示已领取、其余显示无机会
		//领奖金额空：获取当前冲投金额、根据冲投金额显示页面加息券可领取与未解锁
		ActdecListedTwo act = listedTwoActService.getActdecListedTwoList(userId);
		if(!(act==null)) {
			//返回领奖金额、当前冲投金额
			resultBean.setAcceptPrize((null == act.getAcceptPrize()) ? 0 : act.getAcceptPrize());
			resultBean.setCumulativeInvest((null == act.getCumulativeInvest()) ? 0 : act.getCumulativeInvest()/100);
		} else {
			//从未有冲投记录的场合、返回冲投金额0
			resultBean.setAcceptPrize(0);
			resultBean.setCumulativeInvest(0);
		}

		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
		LogUtil.endLog(this.getClass().getName(), methodName);
		return resultBean;
	}

	/**
	 * 领取优惠券
	 * 
	 * @author liushouyi
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ListedTwoActDefine.EXCHANGE_ACTIVITY)
	public ListedTwoActResultBean exchange(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		String methodName = "exchange";
		LogUtil.startLog(this.getClass().getName(), methodName);

		ListedTwoActResultBean resultBean = new ListedTwoActResultBean();

		// 验签
		if (!this.checkSign(requestBean, ListedTwoActDefine.REQUEST_MAPPING)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}
		// 检测活动id
		if (StringUtils.isBlank(actid)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动id没有配置");
			return resultBean;
		}
		//获取活动时间
		ActivityList ayl = listedTwoActService.getActivityDate(Integer.valueOf(actid));
		if (null == ayl) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("未查询到活动信息");
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
		//券类型
		String type = request.getParameter("type");
		// 验证请求参数
		if (Validator.isNull(userId)) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("请求参数非法");
			return resultBean;

		}
		List<UsersInfo> uio = listedTwoActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("用户详细信息不存在，请联系客服");
			return resultBean;
		}
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("内部员工不可参与！");
			return resultBean;
		}
		//获取用户冲投金额
		ActdecListedTwo act = listedTwoActService.getActdecListedTwoList(userId);
		if (null == act) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("未达活动条件请刷新重试");
			return resultBean;
		}
		if (act.getAcceptPrize() != null && act.getAcceptPrize() != 0) {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("活动期间仅能领取一次加息券");
			return resultBean;
		}
		JSONObject retResult = new JSONObject();
		boolean stt = false;
		UserCouponBean ucb = new UserCouponBean();
		ucb.setUserId(userId);
		act.setCreateTime(GetDate.getNowTime10());
		act.setAcceptTime(GetDate.getNowTime10());
		act.setInvestedAmount(act.getCumulativeInvest());
		//操作金额归零
		act.setAmount(0);
		if (type.equals("20000")) {
			// 冲投2万 1张1% 加息券
			if (act.getCumulativeInvest()/100 < 20000) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("未达活动条件请刷新重试");
				return resultBean;
			}
			act.setAcceptPrize(20000);
			act.setTrade(0);
			stt = listedTwoActService.addActdecSpringList(act);
			ucb.setSendFlg(25);
		} else if (type.equals("50000")) {
			// 冲投5万 1张2% 加息券
			if (act.getCumulativeInvest()/100 < 50000) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("未达活动条件请刷新重试");
				return resultBean;
			}
			act.setAcceptPrize(50000);
			act.setTrade(0);
			stt = listedTwoActService.addActdecSpringList(act);
			ucb.setSendFlg(26);
		} else if (type.equals("100000")) {
			// 冲投10万 1张3% 加息券
			if (act.getCumulativeInvest()/100 < 100000) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("未达活动条件请刷新重试");
				return resultBean;
			}
			act.setAcceptPrize(100000);
			act.setTrade(0);
			stt = listedTwoActService.addActdecSpringList(act);
			ucb.setSendFlg(27);
		} else if (type.equals("200000")) {
			// 冲投20万 2张3% 加息券
			if (act.getCumulativeInvest()/100 < 200000) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("未达活动条件请刷新重试");
				return resultBean;
			}
			act.setAcceptPrize(200000);
			act.setTrade(0);
			stt = listedTwoActService.addActdecSpringList(act);
			ucb.setSendFlg(28);
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
			if ((retResult.get("status")).equals("0")) {
				resultBean.setStatus(BaseResultBean.STATUS_FAIL);
				resultBean.setStatusDesc("发放优惠券失败");
				return resultBean;
			}

		} else {
			resultBean.setStatus(BaseResultBean.STATUS_FAIL);
			resultBean.setStatusDesc("发放优惠券失败");
			return resultBean;
		}
		resultBean.setAcceptPrize(Integer.valueOf(type));
		resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
		resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);

		LogUtil.endLog(this.getClass().getName(), methodName);
		return resultBean;
	}

	/**
	 * 充值
	 * 
	 * @author liushouyi
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ListedTwoActDefine.RECHARGE_ACTIVITY)
	public ListedTwoActResultBean recharge(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		// 检测活动id
		if (StringUtils.isEmpty(actid)) {
			return null;
		}
		//获取活动时间
		ActivityList ayl = listedTwoActService.getActivityDate(Integer.valueOf(actid));
		if (null == ayl) {
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
		int iMoney = (int) (Float.parseFloat(money) * 100);

		// 验证请求参数
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		List<UsersInfo> uio = listedTwoActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
		// 2线下员工 3线上员工
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			return null;
		}
		ActdecListedTwo act = listedTwoActService.getActdecListedTwoList(userId);
		
		if (act == null) {
			//第一次充值
			act=new ActdecListedTwo();
			act.setCreateTime( GetDate.getNowTime10());
			//获取用户信息
			List<Users> user = listedTwoActService.getUser(Integer.valueOf(userId));
			//获取用户详细信息
			List<UsersInfo> userInfo = listedTwoActService.getUserInfo(Integer.valueOf(userId));
			act.setUsername(user.get(0).getUsername());
			act.setTruename(userInfo.get(0).getTruename());
			act.setUserId(Integer.valueOf(userId));
			act.setMobile(user.get(0).getMobile());
			//操作(0领奖、1充值、2出借、3提现)
			act.setTrade(1);
			act.setAmount(iMoney);
			act.setCumulativeCharge(iMoney);
			act.setCreateUser(userId);
			act.setDelFlg(0);
		}else {
			//已领奖的用户不再记录
			if (act.getTrade().equals(0)) {
				return null;
			}
			act.setCreateTime(GetDate.getNowTime10());
			act.setAmount(iMoney);
			act.setCumulativeCharge(act.getCumulativeCharge() + iMoney);
			//操作(0领奖、1充值、2出借、3提现)
			act.setTrade(1);
			act.setCreateUser(userId);
		}
		listedTwoActService.addActdecSpringList(act);
		return null;
	}
	/**
	 * 提现
	 * 
	 * @author liushouyi
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ListedTwoActDefine.WITHDRAW_ACTIVITY)
	public ListedTwoActResultBean withdraw(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = listedTwoActService.getActivityDate(Integer.valueOf(actid));
		if (null == ayl) {
			return null;
		}
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
		int iMoney = (int) (Float.parseFloat(money) * 100);

		// 验证请求参数
		if (Validator.isNull(userId)) {
			return null;

		}
		List<UsersInfo> uio = listedTwoActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
		// 2线下员工 3线上员工
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			return null;
		}
		ActdecListedTwo act = listedTwoActService.getActdecListedTwoList(userId);

		if (act == null) {
			//未充值先提现
			act=new ActdecListedTwo();
			act.setCreateTime( GetDate.getNowTime10());
			//获取用户信息
			List<Users> user = listedTwoActService.getUser(Integer.valueOf(userId));
			//获取用户详细信息
			List<UsersInfo> userInfo = listedTwoActService.getUserInfo(Integer.valueOf(userId));
			act.setUsername(user.get(0).getUsername());
			act.setTruename(userInfo.get(0).getTruename());
			act.setUserId(Integer.valueOf(userId));
			act.setMobile(user.get(0).getMobile());
			//操作(0领奖、1充值、2出借、3提现)
			act.setTrade(3);
			act.setAmount(iMoney);
			act.setCumulativeCharge(iMoney * -1);
			act.setCreateUser(userId);
			act.setDelFlg(0);
		}else {
			//已领奖的用户不再记录
			if (act.getTrade().equals(0)) {
				return null;
			}
			act.setCreateTime(GetDate.getNowTime10());
			act.setAmount(iMoney);
			act.setCumulativeCharge(act.getCumulativeCharge() - iMoney);
			//操作(0领奖、1充值、2出借、3提现)
			act.setTrade(3);
			act.setCreateUser(userId);
		}
		listedTwoActService.addActdecSpringList(act);
		return null;
	}
	/**
	 * 出借
	 * 
	 * @author liushouyi
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ListedTwoActDefine.INVESTMENT_ACTIVITY)
	public ListedTwoActResultBean investment(@ModelAttribute BaseBean requestBean, HttpServletRequest request,
			HttpServletResponse response) {
		ActivityList ayl = listedTwoActService.getActivityDate(Integer.valueOf(actid));
		if (null == ayl) {
			return null;
		}
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
		List<UsersInfo> uio = listedTwoActService.getUserInfo(Integer.valueOf(userId));

		if (uio.isEmpty()) {
			return null;
		}
		// 2线下员工 3线上员工
		if (uio.get(0).getAttribute() == 2 || uio.get(0).getAttribute() == 3) {
			return null;
		}
		ActdecListedTwo act = listedTwoActService.getActdecListedTwoList(userId);
		if (act == null) {
			//未充值先出借、非冲投不做记录
			return null;
		}else {
			//已领奖的用户不再记录
			if (act.getTrade().equals(0)) {
				return null;
			}
			//当前累计充值金额小于等于0、不做记录(活动期间未充值或已全部提现、出借不算为冲投)
			if (act.getCumulativeCharge() <= 0) {
				return null;
			}
			act.setCreateTime(GetDate.getNowTime10());
			//操作(0领奖、1充值、2出借、3提现)
			act.setTrade(2);
			act.setCreateUser(userId);

			//未出借的场合、出借金额设为0
			if (null == act.getCumulativeInvest()) {
				act.setCumulativeInvest(0);
			}
			//冲投有效金额
			int chgAndInv=0;
			int iMoney = (int) (Float.parseFloat(money) * 100);
			//判断冲投金额
			if(iMoney<=act.getCumulativeCharge()) {
				//出借小于充值、取当前出借的金额
				chgAndInv = iMoney;
				//冲值金额减少
				act.setCumulativeCharge(act.getCumulativeCharge() - chgAndInv);
			}else {
				//出借多于充值、取当前充值的金额
				chgAndInv=act.getCumulativeCharge();
				//冲值额度用完归零
				act.setCumulativeCharge(0);
			}
			//操作金额
			act.setAmount(iMoney);
			//冲投金额增加 
			chgAndInv = chgAndInv + act.getCumulativeInvest();
			act.setCumulativeInvest(chgAndInv);
		}
		listedTwoActService.addActdecSpringList(act);
		return null;
	}
}
