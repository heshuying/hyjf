package com.hyjf.admin.manager.config.feefrom;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.sitesetting.EmailSettingController;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Config;
import com.hyjf.mybatis.util.getfeefrom.GetFeeFromUtil;

/**
 * 充值手续费收取方式配置
 * 
 * @author 孙亮
 *
 */
@Controller
@RequestMapping(value = FeeFromDefine.REQUEST_MAPPING)
public class FeeFromController extends BaseController {

	@Autowired
	private FeeFromService feeFromService;

	/**
	 * 网站邮件设置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FeeFromDefine.INIT)
	@RequiresPermissions(FeeFromDefine.PERMISSIONS_VIEW)
	public ModelAndView init() {
		LogUtil.startLog(EmailSettingController.class.toString(), FeeFromDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FeeFromDefine.LIST_PATH);
		Config record = feeFromService.getRecord();
		modelAndView.addObject(FeeFromDefine.FEEFROM_FORM, record);
		LogUtil.endLog(EmailSettingController.class.toString(), FeeFromDefine.INIT);
		return modelAndView;
	}

	/**
	 * 数据修改
	 * 
	 * @param modelAndView
	 * @param form
	 */
	@ResponseBody
	@RequestMapping(FeeFromDefine.UPDATE_ACTION)
	@RequiresPermissions(FeeFromDefine.PERMISSIONS_MODIFY)
	public Map<String, Object> update(Config config) {
		// 日志开始
		LogUtil.startLog(EmailSettingController.class.toString(), FeeFromDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(FeeFromDefine.RE_LIST_PATH);
		if (StringUtils.isBlank(config.getValue())) {
			modelAndView.getModel().put("success", false);
			modelAndView.getModel().put("info", "手续费收取方式不能为空");
			return modelAndView.getModel();
		}
		if (!(config.getValue().equals(GetFeeFromUtil.FROMUSER + "") || config.getValue().equals(GetFeeFromUtil.FROMCOMPANY + ""))) {
			modelAndView.getModel().put("success", false);
			modelAndView.getModel().put("info", "无效的类型");
			return modelAndView.getModel();
		}
		Config record = feeFromService.getRecord();
		record.setValue(config.getValue());
		// 成功修改
		feeFromService.updateRecord(record);
		LogUtil.errorLog(EmailSettingController.class.toString(),FeeFromDefine.UPDATE_ACTION,"用户user_id:"+ShiroUtil.getLoginUserId()+",时间:"+GetDate.getDate()+ ",更新充值手续费收取方式为:"+record.getValue()+",更新成功",null );
		modelAndView.getModel().put("success", true);
		modelAndView.getModel().put("info", "操作成功");
		// 日志结束
		LogUtil.endLog(EmailSettingController.class.toString(), FeeFromDefine.UPDATE_ACTION);
		return modelAndView.getModel();
	}

}
