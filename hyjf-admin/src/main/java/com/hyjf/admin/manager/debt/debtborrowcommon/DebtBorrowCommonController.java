package com.hyjf.admin.manager.debt.debtborrowcommon;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.debt.debtborrow.DebtBorrowBean;
import com.hyjf.admin.manager.debt.debtborrowfirst.DebtBorrowFirstBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DebtBorrowCommonDefine.REQUEST_MAPPING)
public class DebtBorrowCommonController extends BaseController {

	@Autowired
	private DebtBorrowCommonService debtBorrowCommonService;

	/**
	 * 迁移到详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.INFO_ACTION)
	public ModelAndView moveToInfoAction(HttpServletRequest request, DebtBorrowCommonBean form) {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(DebtBorrowCommonDefine.INFO_PATH);

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.debtBorrowCommonService
				.borrowProjectTypeList(CustomConstants.HZT);

		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowProjectRepay> borrowStyleList = this.debtBorrowCommonService.borrowProjectRepayList();
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 房屋类型
		modelAndView.addObject("housesTypeList",
				this.debtBorrowCommonService.getParamNameList(CustomConstants.HOUSES_TYPE));

		// 公司规模
		modelAndView.addObject("companySizeList",
				this.debtBorrowCommonService.getParamNameList(CustomConstants.COMPANY_SIZE));

		// 借款人评级
		modelAndView
				.addObject("levelList", this.debtBorrowCommonService.getParamNameList(CustomConstants.BORROW_LEVEL));

		// 合作机构
		modelAndView.addObject("links", this.debtBorrowCommonService.getLinks());

		// 借款预编码
		form.setBorrowPreNid(this.debtBorrowCommonService.getBorrowPreNid());

		// 借款编码
		String borrowNid = form.getBorrowNid();

		if (StringUtils.isNotEmpty(borrowNid)) {
			// 借款编码是否存在
			boolean isExistsRecord = this.debtBorrowCommonService.isExistsRecord(borrowNid, StringUtils.EMPTY);

			if (isExistsRecord) {
				this.debtBorrowCommonService.getBorrow(form);
			} else {
				form.setBorrowValidTime(this.debtBorrowCommonService.getBorrowConfig("BORROW_VALID_TIME"));
			}
		} else {
			form.setBorrowValidTime(this.debtBorrowCommonService.getBorrowConfig("BORROW_VALID_TIME"));
		}

		DebtBorrowWithBLOBs bo = this.debtBorrowCommonService.getRecordById(form);
		form.setBorrowIncreaseMoney(bo.getBorrowIncreaseMoney());
		form.setBorrowInterestCoupon(bo.getBorrowInterestCoupon());
		form.setBorrowTasteMoney(bo.getBorrowTasteMoney());

		modelAndView.addObject(DebtBorrowCommonDefine.BORROW_FORM, form);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.INSERT_ACTION, method = RequestMethod.POST)
	public ModelAndView insertAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowCommonBean form)
			throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.INSERT_ACTION);

		ModelAndView modelAndView = new ModelAndView(DebtBorrowCommonDefine.INFO_PATH);

		// 借款编码
		String borrowNid = form.getBorrowNid();

		// 借款编码是否存在
		boolean isExistsRecord = StringUtils.isNotEmpty(borrowNid)
				&& this.debtBorrowCommonService.isExistsRecord(borrowNid, StringUtils.EMPTY);

		// 画面的值放到Bean中
		this.debtBorrowCommonService.setPageListInfo(modelAndView, form, isExistsRecord);

		// 画面验证
		this.debtBorrowCommonService.validatorFieldCheck(modelAndView, form, isExistsRecord, CustomConstants.HZT);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {

			// 项目类型
			List<BorrowProjectType> borrowProjectTypeList = this.debtBorrowCommonService
					.borrowProjectTypeList(CustomConstants.HZT);
			modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

			// 还款方式
			List<BorrowProjectRepay> borrowStyleList = this.debtBorrowCommonService.borrowProjectRepayList();
			modelAndView.addObject("borrowStyleList", borrowStyleList);

			// 房屋类型
			modelAndView.addObject("housesTypeList",
					this.debtBorrowCommonService.getParamNameList(CustomConstants.HOUSES_TYPE));

			// 公司规模
			modelAndView.addObject("companySizeList",
					this.debtBorrowCommonService.getParamNameList(CustomConstants.COMPANY_SIZE));

			// 借款人评级
			modelAndView.addObject("levelList",
					this.debtBorrowCommonService.getParamNameList(CustomConstants.BORROW_LEVEL));

			// 合作机构
			modelAndView.addObject("links", this.debtBorrowCommonService.getLinks());

			modelAndView.addObject(DebtBorrowCommonDefine.BORROW_FORM, form);
			return modelAndView;
		}

		if (isExistsRecord) {
			List<DebtBorrowCommonNameAccount> borrowCommonNameAccountList = new ArrayList<DebtBorrowCommonNameAccount>();
			DebtBorrowCommonNameAccount borrowCommonNameAccount = new DebtBorrowCommonNameAccount();
			borrowCommonNameAccount.setNames(form.getName());
			borrowCommonNameAccount.setAccounts(form.getAccount());
			borrowCommonNameAccountList.add(borrowCommonNameAccount);
			form.setBorrowCommonNameAccountList(borrowCommonNameAccountList);
		}

		if (isExistsRecord) {
			this.debtBorrowCommonService.updateRecord(form);
		} else {
			this.debtBorrowCommonService.insertRecord(form);
		}
		// 列表迁移
		modelAndView = this.backActionModelAndView(request, modelAndView, attr, form);

		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 返回列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.BACK_ACTION, method = RequestMethod.POST)
	public ModelAndView backAction(HttpServletRequest request, RedirectAttributes attr, DebtBorrowCommonBean form) {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.BACK_ACTION);
		ModelAndView modelAndView = new ModelAndView("redirect:/manager/debt/debtborrow/init");
		// 列表迁移
		modelAndView = this.backActionModelAndView(request, modelAndView, attr, form);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.BACK_ACTION);
		return modelAndView;
	}

	/**
	 * 返回列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	public ModelAndView backActionModelAndView(HttpServletRequest request, ModelAndView modelAndView,
			RedirectAttributes attr, DebtBorrowCommonBean form) {
		// 全部借款列表迁移
		if ("BORROW_LIST".equals(form.getMoveFlag())) {

			DebtBorrowBean debtBorrowBean = new DebtBorrowBean();
			// 借款编码
			debtBorrowBean.setBorrowNidSrch(form.getBorrowNidSrch());
			// 项目名称
			debtBorrowBean.setBorrowNameSrch(form.getBorrowNameSrch());
			// 借 款 人
			debtBorrowBean.setUsernameSrch(form.getUsernameSrch());
			// 项目状态
			debtBorrowBean.setStatusSrch(form.getStatusSrch());
			// 项目类型
			debtBorrowBean.setProjectTypeSrch(form.getProjectTypeSrch());
			// 还款方式
			debtBorrowBean.setBorrowStyleSrch(form.getBorrowStyleSrch());
			// 添加时间
			debtBorrowBean.setTimeStartSrch(form.getTimeStartSrch());
			// 添加时间
			debtBorrowBean.setTimeEndSrch(form.getTimeEndSrch());
			attr.addFlashAttribute("borrowBean", debtBorrowBean);
			modelAndView = new ModelAndView("redirect:/manager/debt/debtborrow/init");
			// 借款初审列表迁移
		} else if ("BORROW_FIRST".equals(form.getMoveFlag())) {
			DebtBorrowFirstBean debtBorrowFirstBean = new DebtBorrowFirstBean();
			// 借款编码
			debtBorrowFirstBean.setBorrowNidSrch(form.getBorrowNidSrch());
			// 项目名称
			debtBorrowFirstBean.setBorrowNameSrch(form.getBorrowNameSrch());
			// 是否交保证金
			debtBorrowFirstBean.setIsBailSrch(form.getIsBailSrch());
			// 借 款 人
			debtBorrowFirstBean.setUsernameSrch(form.getUsernameSrch());
			// 添加时间
			debtBorrowFirstBean.setTimeStartSrch(form.getTimeStartSrch());
			// 添加时间
			debtBorrowFirstBean.setTimeEndSrch(form.getTimeEndSrch());
			attr.addFlashAttribute("borrowfirstForm", debtBorrowFirstBean);
			modelAndView = new ModelAndView("redirect:/manager/debt/debtborrowfirst/init");
		}
		return modelAndView;
	}

	/**
	 * 用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.ISEXISTSUSER_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String isExistsUser(HttpServletRequest request) {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.ISEXISTSUSER_ACTION);
		String message = this.debtBorrowCommonService.isExistsUser(request);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.ISEXISTSUSER_ACTION);
		return message;
	}

	/**
	 * 项目申请人是否存在
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.ISEXISTSAPPLICANT_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String isExistsApplicant(HttpServletRequest request) {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.ISEXISTSAPPLICANT_ACTION);
		String message = this.debtBorrowCommonService.isExistsApplicant(request);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.ISEXISTSAPPLICANT_ACTION);
		return message;
	}

	/**
	 * 获取最新的借款预编码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.GETBORROWPRENID_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String getBorrowPreNid(HttpServletRequest request) {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.GETBORROWPRENID_ACTION);
		String borrowPreNid = this.debtBorrowCommonService.getBorrowPreNid();
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.GETBORROWPRENID_ACTION);
		return borrowPreNid;
	}

	/**
	 * 借款预编码是否存在
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD, method = RequestMethod.POST)
	@ResponseBody
	public String isExistsBorrowPreNidRecord(HttpServletRequest request) {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD);
		String message = this.debtBorrowCommonService.isExistsBorrowPreNidRecord(request);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD);
		return message;
	}

	/**
	 * 获取放款服务费率 & 还款服务费率
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.GETSCALE_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String getBorrowServiceScale(HttpServletRequest request) {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.GETSCALE_ACTION);
		String scale = this.debtBorrowCommonService.getBorrowServiceScale(request);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.GETSCALE_ACTION);
		return scale;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_FILE);
		String files = this.debtBorrowCommonService.uploadFile(request, response);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_FILE);
		return files;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.DOWNLOAD_CAR_ACTION, method = RequestMethod.POST)
	public void downloadCarAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form)
			throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.DOWNLOAD_CAR_ACTION);
		this.debtBorrowCommonService.downloadCar(request, response, form);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.DOWNLOAD_CAR_ACTION);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.UPLOAD_CAR_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String uploadCarAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_CAR_ACTION);
		String result = this.debtBorrowCommonService.uploadCar(request, response);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_CAR_ACTION);
		return result;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.DOWNLOAD_HOUSE_ACTION, method = RequestMethod.POST)
	public void downloadHouseAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form)
			throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.DOWNLOAD_HOUSE_ACTION);
		this.debtBorrowCommonService.downloadHouse(request, response, form);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.DOWNLOAD_HOUSE_ACTION);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.UPLOAD_HOUSE_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String uploadHouseAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_HOUSE_ACTION);
		String result = this.debtBorrowCommonService.uploadHouse(request, response);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_HOUSE_ACTION);
		return result;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION, method = RequestMethod.POST)
	public void downloadAuthenAction(HttpServletRequest request, HttpServletResponse response, DebtBorrowBean form)
			throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION);
		this.debtBorrowCommonService.downloadAuthen(request, response, form);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = DebtBorrowCommonDefine.UPLOAD_AUTHEN_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String uploadAuthenAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_AUTHEN_ACTION);
		String result = this.debtBorrowCommonService.uploadAuthen(request, response);
		LogUtil.endLog(DebtBorrowCommonController.class.toString(), DebtBorrowCommonDefine.UPLOAD_AUTHEN_ACTION);
		return result;
	}
}
