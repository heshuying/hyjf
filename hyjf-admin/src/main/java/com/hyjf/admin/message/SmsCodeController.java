package com.hyjf.admin.message;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.sdk.client.api.Client;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.message.log.SmsLogService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetSessionOrRequestUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.messageprocesser.impl.SmsUtil;
import com.hyjf.mybatis.model.customize.SmsCodeCustomize;
import com.hyjf.mybatis.model.customize.SmsLogCustomize;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = SmsCodeDefine.MESSAGE)
public class SmsCodeController extends BaseController {

	@Autowired
	private SmsCodeService msgService;

	@Autowired
	private SmsLogService logService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SmsCodeDefine.INIT)
	@RequiresPermissions(SmsCodeDefine.PERMISSIONS_MASS)
	public ModelAndView init(HttpServletRequest request, SmsCodeBean form) {
		LogUtil.startLog(SmsCodeController.class.toString(), SmsCodeDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(SmsCodeDefine.SEND_PATH);

		// 在筛选条件下查询出用户
		List<SmsCodeCustomize> msgs = msgService.queryUser(form);

		// 用户数
		modeAndView.addObject("user_number", msgs.size());

		BigDecimal remain_money = BigDecimal.ZERO;
		int remain_number = 0;
		try {
			if(RedisUtils.exists(RedisConstants.REMAIN_NUMBER) && RedisUtils.exists(RedisConstants.REMAIN_MONEY)){
				String remain_numberT = RedisUtils.get(RedisConstants.REMAIN_NUMBER);
				String remain_moneyT = RedisUtils.get(RedisConstants.REMAIN_MONEY);
				remain_number = Integer.valueOf(remain_numberT);
				remain_money = new BigDecimal(remain_moneyT);
			}else{
				Client c = SmsUtil.getClient();
				remain_number = (int) c.getBalance() * 10;
				remain_money = BigDecimal.valueOf(remain_number).multiply(BigDecimal.valueOf(0.04));
				RedisUtils.set(RedisConstants.REMAIN_NUMBER, remain_number + "", 5 * 60);
				RedisUtils.set(RedisConstants.REMAIN_MONEY, remain_money.toString(), 5 * 60);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 短信余额
		modeAndView.addObject("remain_money", remain_money.toPlainString());

		// 剩余短信条数
		modeAndView.addObject("remain_number", remain_number);

		// 上个月发的短信数量
		SmsLogCustomize smlogCustomize = new SmsLogCustomize();
		// 获取上个月第一天00:00:00
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		int begin = (int) (cal.getTimeInMillis() / 1000L);
		// 获取上个月最后一天23:59:59
		cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		int end = (int) (cal.getTimeInMillis() / 1000L);
		smlogCustomize.setPost_time_begin(begin);
		smlogCustomize.setPost_time_end(end);
		smlogCustomize.setStatus(0);
		Integer lastMonth_number = this.logService.queryLogCount(smlogCustomize);
		modeAndView.addObject("lastMonth_number", lastMonth_number);
		// 2017-1-18modify by 周小帅 去掉默认选择所有用户
		/*
		 * // 筛选条件 form.setOpen_account(3);
		 */
		modeAndView.addObject("smsCode", form);

		LogUtil.endLog(SmsCodeController.class.toString(), SmsCodeDefine.INIT);
		return modeAndView;
	}

	/**
	 * 筛选用户
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = SmsCodeDefine.QUERYUSER_ACTION)
	@RequiresPermissions(SmsCodeDefine.PERMISSIONS_SINGLE)
	public ModelAndView queryUser(HttpServletRequest request, SmsCodeBean form) {
		LogUtil.startLog(SmsCodeController.class.toString(), SmsCodeDefine.QUERYUSER_ACTION);
		ModelAndView modeAndView = new ModelAndView(SmsCodeDefine.SEND_PATH);

		// 在筛选条件下查询出用户
		List<SmsCodeCustomize> msgs = msgService.queryUser(form);

		// 用户数
		modeAndView.addObject("user_number", msgs.size());

		// 筛选条件
		modeAndView.addObject("smsCode", form);

		BigDecimal remain_money = BigDecimal.ZERO;
		int remain_number = 0;
		try {
			if(RedisUtils.exists(RedisConstants.REMAIN_NUMBER) && RedisUtils.exists(RedisConstants.REMAIN_MONEY)){
				String remain_numberT = RedisUtils.get(RedisConstants.REMAIN_NUMBER);
				String remain_moneyT = RedisUtils.get(RedisConstants.REMAIN_MONEY);
				remain_number = Integer.valueOf(remain_numberT);
				remain_money = new BigDecimal(remain_moneyT);
			}else{
				Client c = SmsUtil.getClient();
				remain_number = (int) c.getBalance() * 10;
				remain_money = BigDecimal.valueOf(remain_number).multiply(BigDecimal.valueOf(0.06));
				RedisUtils.set(RedisConstants.REMAIN_NUMBER, remain_number + "", 5 * 60);
				RedisUtils.set(RedisConstants.REMAIN_MONEY, remain_money.toString(), 5 * 60);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 短信余额
		modeAndView.addObject("remain_money", remain_money.toPlainString());

		// 剩余短信条数
		modeAndView.addObject("remain_number", remain_number);

		LogUtil.endLog(SmsCodeController.class.toString(), SmsCodeDefine.QUERYUSER_ACTION);
		return modeAndView;
	}

	/**
	 * PC后台发送短信
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(SmsCodeDefine.SENDMESSAGE_ACTION)
	@RequiresPermissions(value = { SmsCodeDefine.PERMISSIONS_SINGLE, SmsCodeDefine.PERMISSIONS_MASS }, logical = Logical.OR)
	public Map<String, Object> send(HttpServletRequest request, SmsCodeBean form) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		LogUtil.startLog(SmsCodeController.class.toString(), SmsCodeDefine.SENDMESSAGE_ACTION);
		// 获取用户输入的手机号码
		String mobile = form.getUser_phones();
		if (form.getMessage() == null) {
			result.put("msg", "发送消息不能为空");
			return result;
		}
		if (request.getHeader("Referer").contains("timeinit")) {
			if (mobile.contains(",")) {
				result.put("msg", "单发只能发送一条");
				return result;
			}
			boolean flag = msgService.getUserByMobile(mobile);
			if (!flag) {
				result.put("msg", "单发不能发送平台外的用户手机号");
				return result;
			}
		}
		String send_message = form.getMessage();
		String channelType = form.getChannelType();
		String sendType = form.getSendType();
		Integer isDisplay = form.getIsDisplay();
		form.setIp(GetCilentIP.getIpAddr(GetSessionOrRequestUtils.getRequest()));
		if (sendType == null) {
			result.put("msg", "请选择发送类型");
			return result;
		} else if (sendType.equals("ontime")) {
			if (form.getOn_time() == null || form.getOn_time().equals("")) {
				result.put("msg", "请选择发送时间");
				return result;
			}
			if (StringUtils.isEmpty(mobile)) {
				if (form.getOpen_account() == null) {
					result.put("msg", "请选择发送条件或者填写手机号");
					return result;
				}
			}
			// TODO 定时发短信插入新定时表
			boolean flag = msgService.sendSmsOntime(form);
			if (flag) {
				result.put("success", true);
				result.put("msg", "定时发送任务创建成功");
				return result;
			} else {
				result.put("msg", "定时发送任务创建失败");
				return result;
			}

		} else {
			if (StringUtils.isEmpty(mobile)) {
				if (form.getOpen_account() == null) {
					result.put("msg", "请选择发送条件或者填写手机号");
					return result;
				}
				// 在筛选条件下查询出用户
				List<SmsCodeCustomize> msgs = msgService.queryUser(form);
				// 用户数
				result.put("user_number", msgs.size());
				// 用户未手写手机号码
				int number = 200;// 分组每组数
				if (msgs != null && msgs.size() != 0) {
					int i = msgs.size() / number;
					for (int j = 0; j <= i; j++) {
						int tosize = (j + 1) * number;
						List<SmsCodeCustomize> smslist;
						if (tosize > msgs.size()) {
							smslist = msgs.subList(j * number, msgs.size());
						} else {
							smslist = msgs.subList(j * number, tosize);
						}
						String phones = "";
						for (int z = 0; z < smslist.size(); z++) {
							if (StringUtils.isNotEmpty(smslist.get(z).getUser_phones())
									&& Validator.isPhoneNumber(smslist.get(z).getUser_phones())) {
								if (z < smslist.size() - 1) {
									phones += smslist.get(z).getUser_phones() + ",";
								} else {
									phones += smslist.get(z).getUser_phones();
								}
							}
						}
						try {
							SmsMessage smsMessage = new SmsMessage(null, null, phones, send_message,
									MessageDefine.SMSSENDFORUSERSNOTPL, null, null, channelType, isDisplay);
							smsProcesser.gather(smsMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {

				// 发送短信
				try {
					String[] mobiles = mobile.split(",");
					for (int i = 0; i < (mobiles.length / 200) + 1; i++) {
						String mbl = "";
						for (int j = i * 200; j < ((i + 1) * 200) && j < mobiles.length; j++) {
							mbl += mobiles[j] + ",";
						}
						if (mbl.endsWith(",")) {
							mbl = mbl.substring(0, mbl.length() - 1);
						}
						SmsMessage smsMessage = new SmsMessage(null, null, mbl, send_message,
								MessageDefine.SMSSENDFORUSERSNOTPL, null, null, channelType, isDisplay);
						smsProcesser.gather(smsMessage);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 筛选条件
			if (form.getOpen_account() == null) {
				form.setOpen_account(3);
			}
			LogUtil.endLog(SmsCodeController.class.toString(), SmsCodeDefine.SENDMESSAGE_ACTION);
			{
				// 更新消息条数

				BigDecimal remain_money = BigDecimal.ZERO;
				int remain_number = 0;
				try {
					if(RedisUtils.exists(RedisConstants.REMAIN_NUMBER) && RedisUtils.exists(RedisConstants.REMAIN_MONEY)){
						String remain_numberT = RedisUtils.get(RedisConstants.REMAIN_NUMBER);
						String remain_moneyT = RedisUtils.get(RedisConstants.REMAIN_MONEY);
						remain_number = Integer.valueOf(remain_numberT);
						remain_money = new BigDecimal(remain_moneyT);
					}else{
						Client c = SmsUtil.getClient();
						remain_number = (int) c.getBalance() * 10;
						remain_money = BigDecimal.valueOf(remain_number).multiply(BigDecimal.valueOf(0.06));
						RedisUtils.set(RedisConstants.REMAIN_NUMBER, remain_number + "", 5 * 60);
						RedisUtils.set(RedisConstants.REMAIN_MONEY, remain_money.toString(), 5 * 60);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// 短信余额
				result.put("remain_money", remain_money.toPlainString());
				// 剩余短信条数
				result.put("remain_number", remain_number);
			}
			result.put("success", true);
			result.put("msg", "发送成功");
		}
		return result;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(SmsCodeDefine.TIME_INIT)
	@RequiresPermissions(SmsCodeDefine.PERMISSIONS_SINGLE)
	public ModelAndView timeinit(HttpServletRequest request, SmsCodeBean form) {
		LogUtil.startLog(SmsCodeController.class.toString(), SmsCodeDefine.TIME_INIT);
		ModelAndView modeAndView = new ModelAndView(SmsCodeDefine.SINGLE_SEND_PATH);
		LogUtil.endLog(SmsCodeController.class.toString(), SmsCodeDefine.TIME_INIT);
		return modeAndView;
	}
}
