package com.hyjf.admin.manager.borrow.debtconfig;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.DebtConfig;
import com.hyjf.mybatis.model.auto.DebtConfigLog;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tanyy
 * @date 2018/08/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtConfigDefine.REQUEST_MAPPING)
public class DebtConfigController extends BaseController {


	@Autowired
	private DebtConfigService debtConfigService;


	/**
	 * 页面初始化
	 *
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(DebtConfigDefine.INIT)
	@RequiresPermissions(DebtConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init() {
		LogUtil.startLog(DebtConfigController.class.toString(), DebtConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(DebtConfigDefine.INFO_PATH);
		List<DebtConfig> list = debtConfigService.selectDebtConfigList();
		if(!CollectionUtils.isEmpty(list)){
			DebtConfig debtConfig = list.get(0);
			debtConfig.setConcessionRateDown(debtConfig.getConcessionRateDown().setScale(1, BigDecimal.ROUND_DOWN));
			debtConfig.setConcessionRateUp(debtConfig.getConcessionRateUp().setScale(1, BigDecimal.ROUND_DOWN));
			modelAndView.addObject("debtConfig",list.get(0));
		}else {
			modelAndView.addObject("debtConfig",new DebtConfig());
		}
		LogUtil.endLog(DebtConfigController.class.toString(), DebtConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 添加信息
	 *
	 * @param request
	 * @param record
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = DebtConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(DebtConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, DebtConfigBean record) throws Exception {
		LogUtil.startLog(DebtConfigController.class.toString(), DebtConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtConfigDefine.RE_LIST_PATH);
		// 调用校验
		if (validatorFieldCheck(modelAndView, record) != null) {
			// 失败返回
			return validatorFieldCheck(modelAndView, record);
		}
		AdminSystem user = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		DebtConfigLog debtConfigLog = new DebtConfigLog();
		DebtConfig debtConfig = new DebtConfig();
		debtConfigLog.setAttornRate(record.getAttornRate());
		debtConfigLog.setCloseDes(record.getCloseDes());
		debtConfigLog.setConcessionRateDown(record.getConcessionRateDown());
		debtConfigLog.setConcessionRateUp(record.getConcessionRateUp());
		debtConfigLog.setHyjfDebtConfigId(record.getId());
		debtConfigLog.setToggle(record.getToggle());
		debtConfigLog.setUpdateTime(new Date());
		debtConfigLog.setUpdateUser(Integer.valueOf(user.getId()));
		debtConfigLog.setUpdateUserName(user.getUsername());
		InetAddress ia=InetAddress.getLocalHost();
        logger.info("当前操作IP*******="+GetCilentIP.getIpAddr(request));
        String ip = GetCilentIP.getIpAddr(request);
        debtConfigLog.setIpAddress(ip);
		//debtConfigLog.setMacAddress("");
		BeanUtils.copyProperties(record,debtConfig);
		modelAndView.addObject("debtConfig",debtConfig);
		debtConfigService.updateRecord(debtConfig,debtConfigLog);
		LogUtil.endLog(DebtConfigController.class.toString(), DebtConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}


		/**
         * 调用校验表单方法
         *
         * @param modelAndView
         * @param form
         * @return
         */
	private ModelAndView validatorFieldCheck(ModelAndView modelAndView, DebtConfigBean form) {
		if (form.getAttornRate() == null)
		{
			ValidatorFieldCheckUtil.validateRequired(modelAndView,"attornRate","");
			return modelAndView;
		}
		if (form.getConcessionRateUp() == null)
		{
			ValidatorFieldCheckUtil.validateRequired(modelAndView,"concessionRateUp","");
			return modelAndView;
		}
		if (form.getConcessionRateDown() == null)
		{
			ValidatorFieldCheckUtil.validateRequired(modelAndView,"concessionRateDown","");
			return modelAndView;
		}
		return null;
	}
}
