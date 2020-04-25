package com.hyjf.admin.manager.config.evaluationconfig.evaluationcheck;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.evaluationconfig.evaluationchecklog.EvaluationCheckLogService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.EvaluationConfig;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
/***
 * 测评配置
* @author Zha Daojian
* @date 2018/11/27 15:17
* @param
* @return
**/
@Controller
@RequestMapping(value = EvaluationCheckDefine.REQUEST_MAPPING)
public class EvaluationCheckController extends BaseController {

	@Autowired
	private EvaluationCheckService evaluationCheckService;
	@Autowired
	private EvaluationCheckLogService evaluationCheckLogService;

		/**
	 * 开关画面迁移(含有id更新，不含有id添加)
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationCheckDefine.INIT)
	@RequiresPermissions(EvaluationCheckDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(EvaluationCheckDefine.EVALUATION_FORM) EvaluationCheckBean form) {
		LogUtil.startLog(EvaluationCheckController.class.toString(), EvaluationCheckDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(EvaluationCheckDefine.LIST_PATH);
		// 创建分页
		this.createPage(modelAndView, form);
		LogUtil.endLog(EvaluationCheckController.class.toString(), EvaluationCheckDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能 页面初始化
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(ModelAndView modelAndView, EvaluationCheckBean form) {
		List<EvaluationConfig> recordList = this.evaluationCheckService.getRecordList();
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(EvaluationCheckDefine.EVALUATION_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationCheckDefine.INFO_ACTION)
	@RequiresPermissions(value = { EvaluationCheckDefine.PERMISSIONS_INFO, EvaluationCheckDefine.PERMISSIONS_MODIFY })
	public ModelAndView info(HttpServletRequest request,EvaluationCheckBean form) {
		LogUtil.startLog(EvaluationCheckController.class.toString(), EvaluationCheckDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(EvaluationCheckDefine.INFO_PATH);
		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			EvaluationConfig record = this.evaluationCheckService.getRecord(id);
			modelAndView.addObject(EvaluationCheckDefine.EVALUATION_FORM, record);
		}
		LogUtil.endLog(EvaluationCheckController.class.toString(), EvaluationCheckDefine.INIT);
		return modelAndView;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = EvaluationCheckDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(EvaluationCheckDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, EvaluationCheckBean form) {
		LogUtil.startLog(EvaluationCheckController.class.toString(), EvaluationCheckDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(EvaluationCheckDefine.INFO_PATH);
		// // 根据id更新
		if (!ValidatorFieldCheckUtil.validateRequired(modelAndView, "id", form.getId().toString())) {
			return modelAndView;
		}
		// 更新
		int re = this.evaluationCheckService.updateRecord(form);
		if(re>0){
			//获取操作之后的测评配置，保存操作日志
			EvaluationConfig record = this.evaluationCheckService.getRecord(form.getId());
			//填充操作内容
			record.setDebtEvaluationTypeCheck(form.getDebtEvaluationTypeCheck());
			record.setIntellectualCollectionEvaluationCheck(form.getIntellectualCollectionEvaluationCheck());
			record.setDeptEvaluationMoneyCheck(form.getDeptEvaluationMoneyCheck());
			record.setIntellectualEvaluationMoneyCheck(form.getIntellectualEvaluationMoneyCheck());
			record.setDeptCollectionEvaluationCheck(form.getDeptCollectionEvaluationCheck());
			record.setIntellectualCollectionEvaluationCheck(form.getIntellectualCollectionEvaluationCheck());
			record.setInvestmentEvaluationCheck(form.getInvestmentEvaluationCheck());
			//新增日志表
			EvaluationConfigLog log =  new EvaluationConfigLog();
			BeanUtils.copyProperties(record,log);
			// IP地址
			String ip = CustomUtil.getIpAddr(request);
			log.setIp(ip);
			// 当前登录用户id
			String username =ShiroUtil.getLoginUsername();
			//操作人
			log.setUpdateUser(username);
			//1开关配置 2限额配置 3信用等级
			log.setStatus(1);
			log.setUpdateTime(new Date());
			log.setCreateTime(new Date());
			evaluationCheckLogService.insetRecord(log);
		}
		// 跳转页面用（info里面有）
		modelAndView.addObject(EvaluationCheckDefine.SUCCESS, EvaluationCheckDefine.SUCCESS);
		LogUtil.endLog(EvaluationCheckController.class.toString(), EvaluationCheckDefine.UPDATE_ACTION);
		return modelAndView;
	}
}
