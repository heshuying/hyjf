package com.hyjf.admin.manager.borrow.borrowfirst;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.coupon.config.CouponConfigDefine;
import com.hyjf.admin.interceptor.TokenInterceptor;
import com.hyjf.admin.interceptor.TokenInterceptor;
import com.hyjf.bank.service.mq.MqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFirstCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowFirstDefine.REQUEST_MAPPING)
public class BorrowFirstController extends BaseController {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MqService mqService;

	@Autowired
	private BorrowFirstService borrowFirstService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	Logger _log = LoggerFactory.getLogger(BorrowFirstController.class);

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowFirstDefine.INIT)
	@RequiresPermissions(BorrowFirstDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("borrowfirstForm") BorrowFirstBean form) {

		LogUtil.startLog(BorrowFirstController.class.toString(), BorrowFirstDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(BorrowFirstDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowFirstDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(save = true)
	@RequestMapping(BorrowFirstDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowFirstDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowFirstBean form) {

		LogUtil.startLog(BorrowFirstController.class.toString(), BorrowFirstDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(BorrowFirstDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowFirstDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowFirstBean borrowFirstBean) {
		// 资金来源
		List<HjhInstConfig> hjhInstConfigList = this.borrowCommonService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		// 初审状态
		List<ParamName> borrowStatusList = this.borrowCommonService.getParamNameList(CustomConstants.VERIFY_STATUS);
		modelAndView.addObject("verifyStatusList", borrowStatusList);

		BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		borrowCommonCustomize.setBorrowNidSrch(borrowFirstBean.getBorrowNidSrch());
		// 项目名称
		borrowCommonCustomize.setBorrowNameSrch(borrowFirstBean.getBorrowNameSrch());
		// 用户名
		borrowCommonCustomize.setUsernameSrch(borrowFirstBean.getUsernameSrch());
		// 保证金状态
		borrowCommonCustomize.setIsBailSrch(borrowFirstBean.getIsBailSrch());
		// 添加时间
		borrowCommonCustomize.setTimeStartSrch(borrowFirstBean.getTimeStartSrch());
		// 添加时间
		borrowCommonCustomize.setTimeEndSrch(borrowFirstBean.getTimeEndSrch());
		// 项目期限
		borrowCommonCustomize.setBorrowPeriod(borrowFirstBean.getBorrowPeriod());
		// 资金来源编号
		borrowCommonCustomize.setInstCodeSrch(borrowFirstBean.getInstCodeSrch());
		// 初审状态
		borrowCommonCustomize.setVerifyStatusSrch(borrowFirstBean.getVerifyStatusSrch());

		Integer count = this.borrowFirstService.countBorrowFirst(borrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(borrowFirstBean.getPaginatorPage(), count);
			borrowCommonCustomize.setLimitStart(paginator.getOffset());
			borrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowFirstCustomize> recordList = this.borrowFirstService
					.selectBorrowFirstList(borrowCommonCustomize);
			borrowFirstBean.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			String sumAccount = this.borrowFirstService.sumBorrowFirstAccount(borrowCommonCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
		}
		modelAndView.addObject("pageUrl", request.getRequestURL() + "?" + request.getQueryString());
		modelAndView.addObject(BorrowFirstDefine.BORROW_FORM, borrowFirstBean);
	}

	/**
	 * 已交保证金详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(save = true)
	@RequestMapping(value = BorrowFirstDefine.BAIL_INFO_ACTION)
	@RequiresPermissions(BorrowFirstDefine.PERMISSIONS_BORROW_BAIL)
	public ModelAndView bailInfoAction(HttpServletRequest request, BorrowFirstBean form) {

		LogUtil.startLog(BorrowFirstController.class.toString(), BorrowFirstDefine.BAIL_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFirstDefine.BAIL_PATH);
		String borrowNid = form.getBorrowNid();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowWithBLOBs borrowWithBLOBs = this.borrowCommonService.getBorrowWithBLOBs(borrowNid);
			if (borrowWithBLOBs != null) {
				modelAndView.addObject("fireInfo", borrowWithBLOBs);
				BigDecimal account = borrowWithBLOBs.getAccount();
				modelAndView.addObject("userName", this.borrowFirstService.getUserName(borrowWithBLOBs.getUserId()));
				BigDecimal bailPercent = new BigDecimal(
						this.borrowFirstService.getBorrowConfig(CustomConstants.BORROW_BAIL_RATE));// 计算公式：保证金金额=借款金额×3％
				double accountBail = (account.multiply(bailPercent)).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
				modelAndView.addObject("accountBail", accountBail);
			}
		}
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowFirstDefine.BAIL_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 交保证金
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(check = true, forward = BorrowFirstDefine.TOKEN_INIT_PATH)
	@RequestMapping(value = BorrowFirstDefine.BAIL_ACTION)
	@RequiresPermissions(BorrowFirstDefine.PERMISSIONS_BORROW_BAIL)
	public ModelAndView bailAction(HttpServletRequest request, BorrowFirstBean form) {

		LogUtil.startLog(BorrowFirstController.class.toString(), BorrowFirstDefine.BAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFirstDefine.BAIL_PATH);
		String borrowNid = form.getBorrowNid();
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "borrowNid", borrowNid);

		/*
		 * HttpSession session = request.getSession(); String sessionToken
		 * =String.valueOf(session.getAttribute(TokenInterceptor.RESUBMIT_TOKEN));//
		 * 生成的令牌 String pageToken = form.getPageToken();//页面令牌
		 */
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			return modelAndView;
		} else {
			// 交保证金
			boolean borrowBailFlag = this.borrowFirstService.saveBailRecord(borrowNid);
			if (borrowBailFlag) {
				modelAndView.addObject(BorrowFirstDefine.SUCCESS, BorrowFirstDefine.SUCCESS);
				modelAndView.addObject(BorrowFirstDefine.BORROW_FORM, form);
				/*---------------upd by liushouyi HJH3 Start------------------------------*/
				// 保证金审核通过后发送MQ到自动初审队列
				HjhAssetBorrowType hjhAssetBorrowType = this.borrowFirstService.selectAssetBorrowType(borrowNid);
				if (null != hjhAssetBorrowType && null != hjhAssetBorrowType.getAutoAudit()
						&& hjhAssetBorrowType.getAutoAudit() == 1) {
					this.borrowFirstService.sendToMQ(borrowNid, RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
				}
				/*---------------upd by liushouyi HJH3 End------------------------------*/
			} else {
				modelAndView.addObject(BorrowFirstDefine.BORROW_FORM, form);
			}
			LogUtil.endLog(BorrowFirstController.class.toString(), BorrowFirstDefine.BAIL_PATH);
			return modelAndView;
		}
	}

	/**
	 * 发标（用来弹出发标的dialog框）
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = BorrowFirstDefine.FIRE_ACTION)
	@RequiresPermissions(BorrowFirstDefine.PERMISSIONS_BORROW_FIRE)
	public ModelAndView fireAction(HttpServletRequest request, BorrowFirstBean form) {
		LogUtil.startLog(BorrowFirstController.class.toString(), BorrowFirstDefine.FIRE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFirstDefine.FIRE_PATH);
		String borrowNid = form.getBorrowNid();
		// 是否使用过引擎：
		int engineFlag = 0;
		if (StringUtils.isNotEmpty(borrowNid)) {
			engineFlag = this.borrowCommonService.isEngineUsed(borrowNid); // 0 未使用引擎 ; 1使用引擎
			modelAndView.addObject("engineFlag", engineFlag);
		}
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowWithBLOBs borrowWithBLOBs = borrowCommonService.getBorrowWithBLOBs(borrowNid);
			if (borrowWithBLOBs != null) {
				modelAndView.addObject("projectType", borrowWithBLOBs.getProjectType());
				modelAndView.addObject("fireInfo", borrowWithBLOBs);
				if (borrowWithBLOBs.getOntime() != null && borrowWithBLOBs.getOntime() != 0) {
					modelAndView.addObject("ontime",
							GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(borrowWithBLOBs.getOntime())));
				}
				modelAndView.addObject("addtime", GetDate.timestamptoStrYYYYMMDDHHMMSS(borrowWithBLOBs.getAddtime()));
				boolean borrowBailFlag = this.borrowFirstService.getBorrowBail(borrowNid);
				modelAndView.addObject("borrowBailFlag", borrowBailFlag);
			}
		}

		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowFirstDefine.FIRE_ACTION);
		return modelAndView;
	}

	/**
	 * 发标（发标弹出框的按钮提交到此方法）
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	// @Token(check = true, forward = BorrowFirstDefine.TOKEN_INIT_PATH)
	@RequestMapping(value = BorrowFirstDefine.FIRE_UPDATE_ACTION)
	@RequiresPermissions(BorrowFirstDefine.PERMISSIONS_BORROW_FIRE)
	public ModelAndView fireUpdateAction(HttpServletRequest request, BorrowFirstBean form) {
		LogUtil.startLog(BorrowFirstController.class.toString(), BorrowFirstDefine.FIRE_UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFirstDefine.FIRE_PATH);
		/*
		 * //令牌验证 HttpSession session = request.getSession(); String sessionToken
		 * =String.valueOf(session.getAttribute(TokenInterceptor.RESUBMIT_TOKEN));//
		 * 生成的令牌 String pageToken = form.getPageToken();//页面令牌
		 * if(!sessionToken.equals(pageToken)){
		 * ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "pagetoken",
		 * "pagetoken"); }
		 */
		String borrowNid = form.getBorrowNid();
		String verifyStatus = form.getVerifyStatus();
		String ontime = form.getOntime();
		// 如果是 0 未使用引擎， 如果是1则使用过引擎
		Integer count = borrowCommonService.isEngineUsed(borrowNid);
		Borrow borrow = borrowCommonService.getBorrowByNid(borrowNid);
		// 发标方式
		boolean verifyStatusFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "verifyStatus", verifyStatus);
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "borrowNid", borrowNid);
		if (verifyStatusFlag) {
			// 定时发标
			if (StringUtils.equals(verifyStatus, "3")) {
				verifyStatusFlag = ValidatorFieldCheckUtil.validateDateYYYMMDDHH24MI(modelAndView, "ontime", ontime,
						true);
				if (verifyStatusFlag) {
					String systeDate = GetDate.getServerDateTime(14, new Date());
					if (systeDate.compareTo(ontime) >= 0) {
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "ontime", "ontimeltsystemdate");
					}
				}
			}
		}

		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowWithBLOBs borrowWithBLOBs = borrowCommonService.getBorrowWithBLOBs(borrowNid);
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				if (borrowWithBLOBs != null) {
					modelAndView.addObject("projectType", borrowWithBLOBs.getProjectType());
					borrowWithBLOBs.setVerifyStatus(Integer.parseInt(verifyStatus));
					modelAndView.addObject("fireInfo", borrowWithBLOBs);
					modelAndView.addObject("addtime",
							GetDate.timestamptoStrYYYYMMDDHHMMSS(borrowWithBLOBs.getAddtime()));
					boolean borrowBailFlag = this.borrowFirstService.getBorrowBail(borrowNid);
					modelAndView.addObject("ontime", ontime);
					modelAndView.addObject("borrowBailFlag", borrowBailFlag);
					modelAndView.addObject(BorrowFirstDefine.BORROW_FORM, form);
					return modelAndView;
				}
				modelAndView.addObject(BorrowFirstDefine.BORROW_FORM, form);
				return modelAndView;
			}
		}

		if (StringUtils.isNotEmpty(borrowNid)) {
			this.borrowFirstService.updateOntimeRecord(form, count);

			if (form.getVerifyStatus() != null && StringUtils.isNotEmpty(form.getVerifyStatus())) {
				// upa by liushouyi nifa2 start
				if(Integer.valueOf(form.getVerifyStatus()) == 4){
					if (borrow.getIsEngineUsed().equals(1)) {
						// 成功后到关联计划队列
						this.sendToMQ(borrow, RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
						_log.info(borrowNid + "已发送至MQ");
					}
					// 发送发标成功的消息队列到互金上报数据
					Map<String, String> params = new HashMap<String, String>();
					params.put("borrowNid", borrowNid);
					params.put("userId",borrow.getUserId() + "");
					this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTING_DELAY_KEY, JSONObject.toJSONString(params));
				}
				// upa by liushouyi nifa2 end
			}
		}

		modelAndView.addObject(BorrowFirstDefine.SUCCESS, BorrowFirstDefine.SUCCESS);
		modelAndView.addObject(BorrowFirstDefine.BORROW_FORM, form);
		LogUtil.endLog(BorrowFirstController.class.toString(), BorrowFirstDefine.FIRE_UPDATE_ACTION);
		return modelAndView;

	}

	public void sendToMQ(Borrow borrow, String routingKey) {
		// 加入到消息队列
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("borrowNid", borrow.getBorrowNid());
		params.put("instCode", borrow.getInstCode());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
	}
}
