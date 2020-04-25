package com.hyjf.admin.manager.borrow.borrowfull;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowFullCustomize;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowFullDefine.REQUEST_MAPPING)
public class BorrowFullController extends BaseController {

	@Autowired
	private BorrowFullService borrowFullService;

	/**
	 * 复审记录
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(save = true)
	@RequestMapping(BorrowFullDefine.INIT)
	@RequiresPermissions(BorrowFullDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute("borrowFullBean") BorrowFullBean form) {
		LogUtil.startLog(BorrowFullController.class.toString(), BorrowFullDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BorrowFullDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowFullController.class.toString(), BorrowFullDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查找
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowFullDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowFullDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute("borrowFullBean") BorrowFullBean form) {
		LogUtil.startLog(BorrowFullController.class.toString(), BorrowFullDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFullDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BorrowFullController.class.toString(), BorrowFullDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowFullBean form) {

		BorrowCommonCustomize borrowCustomize = new BorrowCommonCustomize();
		// 借款编码
		borrowCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		borrowCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 用户名
		borrowCustomize.setUsernameSrch(form.getUsernameSrch());
		// 满标时间
		borrowCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 满标时间
		borrowCustomize.setTimeEndSrch(form.getTimeEndSrch());

		Long count = this.borrowFullService.countBorrowFull(borrowCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));
			borrowCustomize.setLimitStart(paginator.getOffset());
			borrowCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowFullCustomize> recordList = this.borrowFullService.selectBorrowFullList(borrowCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			/*-------add by LSY START-------------*/
			BorrowFullCustomize sumAccount = this.borrowFullService.sumAccount(borrowCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
			/*-------add by LSY END-------------*/
		}
		modelAndView.addObject(BorrowFullDefine.BORROW_FORM, form);
	}

	/**
	 * 复审的详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowFullDefine.FULL_INFO_ACTION)
	@RequiresPermissions(BorrowFullDefine.PERMISSIONS_BORROW_FULL)
	public ModelAndView fullAction(HttpServletRequest request, BorrowFullBean form) {
		LogUtil.startLog(BorrowFullController.class.toString(), BorrowFullDefine.FULL_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFullDefine.REVERIFY_PATH);

		// 复审的详细
		this.getFullInfo(modelAndView, form);

		modelAndView.addObject(BorrowFullDefine.BORROW_FORM, form);

		LogUtil.endLog(BorrowFullController.class.toString(), BorrowFullDefine.FULL_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 复审的详细
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void getFullInfo(ModelAndView modelAndView, BorrowFullBean form) {

		String borrowNid = form.getBorrowNid();// 项目编号
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowFullCustomize reverifyInfo = this.borrowFullService.selectFullInfo(borrowNid);
			if (reverifyInfo != null) {
				modelAndView.addObject("reverifyInfo", reverifyInfo);
				// 合规删除 2018/11/15
				//List<BorrowFullCustomize> fullList = this.borrowFullService.selectFullList(borrowNid, -1, -1);
				//if (fullList != null && fullList.size() > 0) {
				//	Paginator paginator = new Paginator(form.getPaginatorPage(), fullList.size());
				//	fullList = this.borrowFullService.selectFullList(borrowNid, paginator.getOffset(), paginator.getLimit());
				//	form.setPaginator(paginator);
				//	modelAndView.addObject("fullList", fullList);
				//	BorrowFullCustomize sumAmount = this.borrowFullService.sumAmount(borrowNid);
				//	modelAndView.addObject("sumAmount", sumAmount);
				//}
			}
		}
	}

	/**
	 * 复审的详细画面确定按钮
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(check = true, forward = BorrowFullDefine.REQUEST_MAPPING + "/" + BorrowFullDefine.INIT)
	@RequestMapping(BorrowFullDefine.FULL_ACTION)
	@RequiresPermissions(BorrowFullDefine.PERMISSIONS_BORROW_FULL)
	public ModelAndView fullInfoAction(HttpServletRequest request, RedirectAttributes attr, BorrowFullBean form) {

		LogUtil.startLog(BorrowFullController.class.toString(), BorrowFullDefine.FULL_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFullDefine.REVERIFY_PATH);
		this.validatorFieldCheck(modelAndView, form);// 校验参数
       /* HttpSession session = request.getSession();
        String sessionToken =String.valueOf(session.getAttribute(TokenInterceptor.RESUBMIT_TOKEN));//生成的令牌
        String pageToken = form.getPageToken();//页面令牌*/
		if (!ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			boolean flag = false;
			String msg = "";
			try {
				flag = this.borrowFullService.updateReverifyRecord(form,msg);
			} catch (Exception e) {
				e.printStackTrace();
				//modelAndView.addObject("errMsg", e.getMessage());
			}
			if (flag) {
				attr.addFlashAttribute("borrowFullBean", form);
				modelAndView = new ModelAndView("redirect:/manager/borrow/borrowfull/init");
				LogUtil.endLog(BorrowFullController.class.toString(), BorrowFullDefine.FULL_ACTION);
				return modelAndView;
			}else{
				modelAndView.addObject("errMsg", msg);
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "statusError", "borrow.status.tender.error");
			}
		}
		// 获取复审的详细
		this.getFullInfo(modelAndView, form);
		modelAndView.addObject("reverifyRemark", form.getReverifyRemark());
		modelAndView.addObject(BorrowFullDefine.BORROW_FORM, form);
		LogUtil.endLog(BorrowFullController.class.toString(), BorrowFullDefine.FULL_ACTION);
		return modelAndView;

	}

	/**
	 * 复审的详细画面确定按钮
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowFullDefine.REPEAT_ACTION)
	public ModelAndView repeatAction(HttpServletRequest request, RedirectAttributes attr, BorrowFullBean form) {
		LogUtil.startLog(BorrowFullController.class.toString(), BorrowFullDefine.REPEAT_ACTION);
		ModelAndView modelAndView = new ModelAndView(BorrowFullDefine.REVERIFY_PATH);
		boolean flag = this.borrowFullService.updateBorrowApicronRecord(form);
		if (flag) {
			attr.addFlashAttribute("borrowFullBean", form);
			modelAndView = new ModelAndView("redirect:/manager/borrow/borrowfull/init");
		}
		LogUtil.endLog(BorrowFullController.class.toString(), BorrowFullDefine.REPEAT_ACTION);
		return modelAndView;
	}

	/**
	 * 验证
	 * 
	 * @param mav
	 * @param form
	 */
	private void validatorFieldCheck(ModelAndView mav, BorrowFullBean form) {
		// 审核备注
		ValidatorFieldCheckUtil.validateMaxLength(mav, "reverifyRemark", form.getReverifyRemark(), 255, true);
		// 是否已经被审核
		boolean flag = this.borrowFullService.isBorrowStatus16(form);
		if (flag) {
			ValidatorFieldCheckUtil.validateSpecialError(mav, "statusError", "borrow.status.not.16");
		}
	}

	/**
	 * 流标
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BorrowFullDefine.OVER_ACTION)
	@RequiresPermissions(BorrowFullDefine.PERMISSIONS_BORROW_OVER)
	public ModelAndView overAction(HttpServletRequest request, RedirectAttributes attr, BorrowFullBean form) {
		LogUtil.startLog(BorrowFullController.class.toString(), BorrowFullDefine.OVER_ACTION);
		ModelAndView modelAndView = new ModelAndView("redirect:/manager/borrow/borrowfull/init");
		attr.addFlashAttribute("borrowFullBean", form);
		modelAndView = new ModelAndView("redirect:/manager/borrow/borrowfull/init");
		this.borrowFullService.updateBorrowRecordOver(form);

		LogUtil.endLog(BorrowFullController.class.toString(), BorrowFullDefine.OVER_ACTION);
		return modelAndView;
	}
}
