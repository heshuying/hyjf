package com.hyjf.admin.manager.config.evaluationconfig.eveluationmoney;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.config.evaluationconfig.evaluationchecklog.EvaluationCheckLogService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
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
 * 测评配置，限额配置
* @author Zha Daojian
* @date 2018/11/27 15:17
* @param
* @return
**/
@Controller
@RequestMapping(value = EvaluationMoneyDefine.REQUEST_MAPPING)
public class EvaluationMoneyController extends BaseController {

	@Autowired
	private EvaluationMoneyService evaluationCheckService;
	@Autowired
	private EvaluationCheckLogService evaluationCheckLogService;

		/**
	 * 限额配置画面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationMoneyDefine.INIT)
	@RequiresPermissions(EvaluationMoneyDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(EvaluationMoneyDefine.EVALUATION_FORM) EvaluationMoneyBean form) {
		LogUtil.startLog(EvaluationMoneyController.class.toString(), EvaluationMoneyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(EvaluationMoneyDefine.LIST_PATH);
		// 创建分页
		this.createPage(modelAndView, form);
		LogUtil.endLog(EvaluationMoneyController.class.toString(), EvaluationMoneyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能 页面初始化
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(ModelAndView modelAndView, EvaluationMoneyBean form) {
		List<EvaluationConfig> recordList = this.evaluationCheckService.getRecordList();
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(EvaluationMoneyDefine.EVALUATION_FORM, form);
		}
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationMoneyDefine.INFO_ACTION)
	@RequiresPermissions(value = { EvaluationMoneyDefine.PERMISSIONS_INFO, EvaluationMoneyDefine.PERMISSIONS_MODIFY })
	public ModelAndView info(HttpServletRequest request,EvaluationMoneyBean form) {
		LogUtil.startLog(EvaluationMoneyController.class.toString(), EvaluationMoneyDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(EvaluationMoneyDefine.INFO_PATH);
		if (form.getId() != null) {
			Integer id = Integer.valueOf(form.getId());
			EvaluationConfig record = this.evaluationCheckService.getRecord(id);
			modelAndView.addObject(EvaluationMoneyDefine.EVALUATION_FORM, record);
		}
		LogUtil.endLog(EvaluationMoneyController.class.toString(), EvaluationMoneyDefine.INIT);
		return modelAndView;
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = EvaluationMoneyDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(EvaluationMoneyDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request, EvaluationMoneyBean form) {
		LogUtil.startLog(EvaluationMoneyController.class.toString(), EvaluationMoneyDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(EvaluationMoneyDefine.INFO_PATH);
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

			//测评到期时间
			record.setValidityEvaluationDate(form.getValidityEvaluationDate());
			//成长型单笔投资限额金额
			record.setGrowupEvaluationSingleMoney(form.getGrowupEvaluationSingleMoney());
			//稳健型单笔投资限额金额
			record.setSteadyEvaluationSingleMoney(form.getSteadyEvaluationSingleMoney());
			//进取型单笔投资限额金额
			record.setEnterprisingEvaluationSinglMoney(form.getEnterprisingEvaluationSinglMoney());
			//保守型单笔投资限额金额
			record.setConservativeEvaluationSingleMoney(form.getConservativeEvaluationSingleMoney());
			//成长型代收本金限额金额
			record.setGrowupEvaluationPrincipalMoney(form.getGrowupEvaluationPrincipalMoney());
			//稳健型代收本金限额金额
			record.setSteadyEvaluationPrincipalMoney(form.getSteadyEvaluationPrincipalMoney());
			//进取型代收本金限额金额
			record.setEnterprisingEvaluationPrincipalMoney(form.getEnterprisingEvaluationPrincipalMoney());
			//保守型代收本金限额金额
			record.setConservativeEvaluationPrincipalMoney(form.getConservativeEvaluationPrincipalMoney());

			/**********************Redis处理***********************************/
			//测评到期时间
			if (RedisUtils.exists(RedisConstants.REVALUATION_EXPIRED_DAY)) {
				//测评到期时间
				RedisUtils.set(RedisConstants.REVALUATION_EXPIRED_DAY, form.getValidityEvaluationDate()+"");
			}

			if (RedisUtils.exists(RedisConstants.REVALUATION_CONSERVATIVE)) {
				//保守型单笔投资限额金额
				RedisUtils.set(RedisConstants.REVALUATION_CONSERVATIVE, form.getConservativeEvaluationSingleMoney()+"");
			}
			if (RedisUtils.exists(RedisConstants.REVALUATION_ROBUSTNESS)) {
				//稳健型单笔投资限额金额
				RedisUtils.set(RedisConstants.REVALUATION_ROBUSTNESS, form.getSteadyEvaluationSingleMoney()+"");
			}
			if (RedisUtils.exists(RedisConstants.REVALUATION_GROWTH)) {
				//成长型单笔投资限额金额
				RedisUtils.set(RedisConstants.REVALUATION_GROWTH, form.getGrowupEvaluationSingleMoney()+"");
			}
			if (RedisUtils.exists(RedisConstants.REVALUATION_AGGRESSIVE)) {
				//保进取型单笔投资限额金额
				RedisUtils.set(RedisConstants.REVALUATION_AGGRESSIVE, form.getEnterprisingEvaluationSinglMoney()+"");
			}

			if (RedisUtils.exists(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL)) {
				//保守型代收本金限额金额
				RedisUtils.set(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL, form.getConservativeEvaluationPrincipalMoney()+"");
			}
			if (RedisUtils.exists(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL)) {
				//稳健型代收本金限额金额
				RedisUtils.set(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL, form.getSteadyEvaluationPrincipalMoney()+"");
			}
			if (RedisUtils.exists(RedisConstants.REVALUATION_GROWTH_PRINCIPAL)) {
				//成长型代收本金限额金额
				RedisUtils.set(RedisConstants.REVALUATION_GROWTH_PRINCIPAL, form.getGrowupEvaluationPrincipalMoney()+"");
			}
			if (RedisUtils.exists(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL)) {
				//保进取型代收本金限额金额
				RedisUtils.set(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL, form.getEnterprisingEvaluationPrincipalMoney()+"");
			}
			//新增日志表
			EvaluationConfigLog log =  new EvaluationConfigLog();
			BeanUtils.copyProperties(record,log);
			// IP地址
			String ip = CustomUtil.getIpAddr(request);
			// 当前登录用户id
			String username =ShiroUtil.getLoginUsername();
			//操作人
			log.setUpdateUser(username);
			log.setIp(ip);
			log.setStatus(2);
			log.setUpdateTime(new Date());
			log.setCreateTime(new Date());
			evaluationCheckLogService.insetRecord(log);
		}
		// 跳转页面用（info里面有）
		modelAndView.addObject(EvaluationMoneyDefine.SUCCESS, EvaluationMoneyDefine.SUCCESS);
		LogUtil.endLog(EvaluationMoneyController.class.toString(), EvaluationMoneyDefine.UPDATE_ACTION);
		return modelAndView;
	}
}
