package com.hyjf.admin.manager.config.evaluationconfig.evaluationmoneylog;

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
@RequestMapping(value = EvaluationMoneyLogDefine.REQUEST_MAPPING)
public class EvaluationMoneyLogController extends BaseController {

	@Autowired
	private EvaluationMoneyLogService evaluationCheckService;

	/**
	 * 初始化页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationMoneyLogDefine.INIT)
	@RequiresPermissions(EvaluationMoneyLogDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,@ModelAttribute(EvaluationMoneyLogDefine.EVALUATION_FORM) EvaluationMoneyLogBean form) {
		LogUtil.startLog(EvaluationMoneyLogController.class.toString(), EvaluationMoneyLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(EvaluationMoneyLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(modelAndView, form);
		LogUtil.endLog(EvaluationMoneyLogController.class.toString(), EvaluationMoneyLogDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能 页面初始化
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(ModelAndView modelAndView, EvaluationMoneyLogBean form) {
		List<EvaluationConfigLog> recordList = this.evaluationCheckService.getRecordList(form, -1, -1);
		if (recordList != null) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
			recordList = this.evaluationCheckService.getRecordList(form, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(EvaluationMoneyLogDefine.EVALUATION_FORM, form);
		}
	}

	/**
	 * 条件查询
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(EvaluationMoneyLogDefine.SEARCH_ACTION)
	@RequiresPermissions(EvaluationMoneyLogDefine.PERMISSION_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request, RedirectAttributes attr,
									 @ModelAttribute(EvaluationMoneyLogDefine.EVALUATION_FORM) EvaluationMoneyLogBean form) {
		LogUtil.startLog(EvaluationMoneyLogController.class.toString(), EvaluationMoneyLogDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(EvaluationMoneyLogDefine.LIST_PATH);
		// 创建分页
		this.createPage(modelAndView, form);
		LogUtil.endLog(EvaluationMoneyLogController.class.toString(), EvaluationMoneyLogDefine.SEARCH_ACTION);
		return modelAndView;
	}
}
