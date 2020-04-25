package com.hyjf.admin.manager.config.evaluationconfig.evaluationchecklog;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/***
 * 测评配置-开关操作日志
* @author Zha Daojian
* @date 2018/11/27 15:17
* @param
* @return
**/
@Controller
@RequestMapping(value = EvaluationCheckLogDefine.REQUEST_MAPPING)
public class EvaluationCheckLogController extends BaseController {

	@Autowired
	private EvaluationCheckLogService evaluationCheckService;

	/**
	 * 初始化页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationCheckLogDefine.INIT)
	@RequiresPermissions(EvaluationCheckLogDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(EvaluationCheckLogDefine.EVALUATION_FORM) EvaluationCheckLogBean form) {
		LogUtil.startLog(EvaluationCheckLogController.class.toString(), EvaluationCheckLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(EvaluationCheckLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(modelAndView, form);
		LogUtil.endLog(EvaluationCheckLogController.class.toString(), EvaluationCheckLogDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能 页面初始化
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(ModelAndView modelAndView, EvaluationCheckLogBean form) {
		List<EvaluationConfigLog> recordList = this.evaluationCheckService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.evaluationCheckService.getRecordList(form, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(EvaluationCheckLogDefine.EVALUATION_FORM, form);
		}
	}

	/**
	 * 条件查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationCheckLogDefine.SEARCH_ACTION)
	@RequiresPermissions(EvaluationCheckLogDefine.PERMISSION_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
									 @ModelAttribute(EvaluationCheckLogDefine.EVALUATION_FORM) EvaluationCheckLogBean form) {
		LogUtil.startLog(EvaluationCheckLogController.class.toString(), EvaluationCheckLogDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(EvaluationCheckLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(modelAndView, form);
		LogUtil.endLog(EvaluationCheckLogController.class.toString(), EvaluationCheckLogDefine.SEARCH_ACTION);
		return modelAndView;
	}
}
