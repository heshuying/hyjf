package com.hyjf.admin.exception.freezexception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrowrecover.BorrowRecoverController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.FreezeHistory;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = FreezExceptionDefine.REQUEST_MAPPING)
public class FreezExceptionController extends BaseController {

	@Autowired
	private FreezExceptionService freezExceptionService;

	/**
	 * 复审记录
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FreezExceptionDefine.INIT)
	@RequiresPermissions(FreezExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(FreezExceptionDefine.FORM) FreezExceptionBean form) {
		LogUtil.startLog(FreezExceptionController.class.toString(), FreezExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(FreezExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(FreezExceptionController.class.toString(), FreezExceptionDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查找
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FreezExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(FreezExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute(FreezExceptionDefine.FORM) FreezExceptionBean form) {
		LogUtil.startLog(FreezExceptionController.class.toString(), FreezExceptionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(FreezExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(FreezExceptionController.class.toString(), FreezExceptionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, FreezExceptionBean form) {
		int recordTotal = this.freezExceptionService.queryCount(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<FreezeHistory> recordList = this.freezExceptionService.queryRecordList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(FreezExceptionDefine.FORM, form);
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(FreezExceptionDefine.FREEZE_INFO_ACTION)
	public ModelAndView freezeInfoAction(HttpServletRequest request, FreezExceptionBean form) {
		LogUtil.startLog(BorrowRecoverController.class.toString(), FreezExceptionDefine.FREEZE_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(FreezExceptionDefine.INFO_PATH);
		LogUtil.endLog(BorrowRecoverController.class.toString(), FreezExceptionDefine.FREEZE_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 解冻异常修复
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = FreezExceptionDefine.FREEZE_ACTION)
	@ResponseBody
	public JSONObject freezeAction(HttpServletRequest request, FreezExceptionBean form) {
		LogUtil.startLog(FreezExceptionController.class.toString(), FreezExceptionDefine.FREEZE_ACTION);
		JSONObject object = new JSONObject();
		if (StringUtils.isEmpty(form.getTrxId())) {
			object.put("message", "解冻订单号不能为空！");
			return object;
		}

		if (StringUtils.isEmpty(form.getNotes())) {
			object.put("message", "解冻备注不能为空！");
			return object;
		} else {
			if (form.getNotes().length() > 255) {
				object.put("message", "解冻备注长度不能超过255位！");
				return object;
			}
		}

		boolean trxIdFlag = this.freezExceptionService.selsectTrxIdIsExists(form.getTrxId());

		if (trxIdFlag) {
			object.put("message", "解冻订单号在系统已经存在，只能解冻只在汇付存在的订单号！");
			return object;
		}

		String message = this.freezExceptionService.updateFreezRecord(form);
		if (StringUtils.isNotEmpty(message)) {
			object.put("message", message);
			return object;
		}

		LogUtil.endLog(FreezExceptionController.class.toString(), FreezExceptionDefine.FREEZE_ACTION);

		object.put("message", "");
		return object;

	}
}
