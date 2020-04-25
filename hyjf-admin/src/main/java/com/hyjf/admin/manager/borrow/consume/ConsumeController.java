package com.hyjf.admin.manager.borrow.consume;

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

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.borrow.borrow.BorrowBean;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonBean;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonDefine;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonNameAccount;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.borrow.borrowfirst.BorrowFirstBean;
import com.hyjf.admin.manager.huixiaofei.HuixiaofeiBean;
import com.hyjf.admin.manager.huixiaofei.HuixiaofeiDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectType;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ConsumeDefine.REQUEST_MAPPING)
public class ConsumeController extends BaseController {

	@Autowired
	private ConsumeService consumeService;

	@Autowired
	private BorrowCommonService borrowCommonService;

	/**
	 * 迁移到详细画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ConsumeDefine.INFO_ACTION)
	public ModelAndView moveToInfoAction(HttpServletRequest request, BorrowCommonBean form) {
		LogUtil.startLog(ConsumeController.class.toString(), ConsumeDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(ConsumeDefine.INFO_PATH);

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList(CustomConstants.HXF);
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowProjectRepay> borrowStyleList = this.borrowCommonService.borrowProjectRepayList();
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 房屋类型
		modelAndView.addObject("housesTypeList", this.borrowCommonService.getParamNameList(CustomConstants.HOUSES_TYPE));

		// 公司规模
		modelAndView.addObject("companySizeList", this.borrowCommonService.getParamNameList(CustomConstants.COMPANY_SIZE));

		// 合作机构
		modelAndView.addObject("links", this.borrowCommonService.getLinks());

		// 借款预编码
		form.setBorrowPreNid(this.borrowCommonService.getBorrowPreNid());

		// 获取数据
		this.consumeService.getBorrow(form);

		modelAndView.addObject(ConsumeDefine.BORROW_FORM, form);
		LogUtil.endLog(ConsumeController.class.toString(), ConsumeDefine.INFO_ACTION);
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
	@RequestMapping(value = ConsumeDefine.INSERT_ACTION, method = RequestMethod.POST)
	public ModelAndView insertAction(HttpServletRequest request, RedirectAttributes attr, BorrowCommonBean form) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), ConsumeDefine.INSERT_ACTION);

		ModelAndView modelAndView = new ModelAndView(ConsumeDefine.INFO_PATH);

		// 借款编码
		String borrowNid = form.getBorrowNid();

		// 借款编码是否存在
		boolean isExistsRecord = StringUtils.isNotEmpty(borrowNid) && this.borrowCommonService.isExistsRecord(borrowNid, StringUtils.EMPTY);

		// 画面的值放到Bean中
		this.borrowCommonService.setPageListInfo(modelAndView, form, isExistsRecord);

		// 画面验证
		this.borrowCommonService.validatorFieldCheck(modelAndView, form, isExistsRecord, CustomConstants.HXF);

		String isChaibiao = form.getIsChaibiao();
		if ("yes".equals(isChaibiao)) {
			String cunsumeCount = form.getCunsumeCount();
			if (StringUtils.isEmpty(cunsumeCount)) {
				cunsumeCount = "1";
			}
			String cunsumeCountDB = String.valueOf(this.consumeService.getConsumeMaxNumber());
			if (!cunsumeCountDB.equals(cunsumeCount)) {
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "borrowname", "borrowname.repeat");
			}
		}

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {

			// 项目类型
			List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList(CustomConstants.HXF);
			modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

			// 还款方式
			List<BorrowProjectRepay> borrowStyleList = this.borrowCommonService.borrowProjectRepayList();
			modelAndView.addObject("borrowStyleList", borrowStyleList);

			// 房屋类型
			modelAndView.addObject("housesTypeList", this.borrowCommonService.getParamNameList(CustomConstants.HOUSES_TYPE));

			// 公司规模
			modelAndView.addObject("companySizeList", this.borrowCommonService.getParamNameList(CustomConstants.COMPANY_SIZE));

			// 合作机构
			modelAndView.addObject("links", this.borrowCommonService.getLinks());

			modelAndView.addObject(ConsumeDefine.BORROW_FORM, form);
			return modelAndView;
		}

		if (isExistsRecord) {
			List<BorrowCommonNameAccount> borrowCommonNameAccountList = new ArrayList<BorrowCommonNameAccount>();
			BorrowCommonNameAccount borrowCommonNameAccount = new BorrowCommonNameAccount();
			borrowCommonNameAccount.setNames(form.getName());
			borrowCommonNameAccount.setAccounts(form.getAccount());
			borrowCommonNameAccountList.add(borrowCommonNameAccount);
			form.setBorrowCommonNameAccountList(borrowCommonNameAccountList);
		}

		if (isExistsRecord) {
			this.consumeService.updateRecord(form);
		} else {
			this.consumeService.insertRecord(form);
		}
		// 列表迁移
		modelAndView = this.backActionModelAndView(request, modelAndView, attr, form);

		LogUtil.endLog(ConsumeController.class.toString(), ConsumeDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 返回列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = ConsumeDefine.BACK_ACTION, method = RequestMethod.POST)
	public ModelAndView backAction(HttpServletRequest request, RedirectAttributes attr, BorrowCommonBean form) {
		LogUtil.startLog(ConsumeController.class.toString(), ConsumeDefine.BACK_ACTION);
		ModelAndView modelAndView = new ModelAndView("redirect:/manager/borrow/borrow/init");
		// 列表迁移
		modelAndView = this.backActionModelAndView(request, modelAndView, attr, form);
		LogUtil.endLog(ConsumeController.class.toString(), ConsumeDefine.BACK_ACTION);
		return modelAndView;
	}

	/**
	 * 返回列表
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	public ModelAndView backActionModelAndView(HttpServletRequest request, ModelAndView modelAndView, RedirectAttributes attr, BorrowCommonBean form) {
		// 全部借款列表迁移
		if ("BORROW_LIST".equals(form.getMoveFlag())) {

			BorrowBean borrowBean = new BorrowBean();
			// 借款编码
			borrowBean.setBorrowNidSrch(form.getBorrowNidSrch());
			// 项目名称
			borrowBean.setBorrowNameSrch(form.getBorrowNameSrch());
			// 借 款 人
			borrowBean.setUsernameSrch(form.getUsernameSrch());
			// 项目状态
			borrowBean.setStatusSrch(form.getStatusSrch());
			// 项目类型
			borrowBean.setProjectTypeSrch(form.getProjectTypeSrch());
			// 还款方式
			borrowBean.setBorrowStyleSrch(form.getBorrowStyleSrch());
			// 添加时间
			borrowBean.setTimeStartSrch(form.getTimeStartSrch());
			// 添加时间
			borrowBean.setTimeEndSrch(form.getTimeEndSrch());
			attr.addFlashAttribute("borrowBean", borrowBean);
			modelAndView = new ModelAndView("redirect:/manager/borrow/borrow/init");
			// 借款初审列表迁移
		} else if ("BORROW_FIRST".equals(form.getMoveFlag())) {
			BorrowFirstBean borrowFirstBean = new BorrowFirstBean();
			// 借款编码
			borrowFirstBean.setBorrowNidSrch(form.getBorrowNidSrch());
			// 项目名称
			borrowFirstBean.setBorrowNameSrch(form.getBorrowNameSrch());
			// 是否交保证金
			borrowFirstBean.setIsBailSrch(form.getIsBailSrch());
			// 借 款 人
			borrowFirstBean.setUsernameSrch(form.getUsernameSrch());
			// 添加时间
			borrowFirstBean.setTimeStartSrch(form.getTimeStartSrch());
			// 添加时间
			borrowFirstBean.setTimeEndSrch(form.getTimeEndSrch());
			attr.addFlashAttribute("borrowfirstForm", borrowFirstBean);
			modelAndView = new ModelAndView("redirect:/manager/borrow/borrowfirst/init");
		} else if ("BORROW_HXF".equals(form.getMoveFlag())) {
			HuixiaofeiBean huixiaofeiForm = new HuixiaofeiBean();
			// 借款编码
			huixiaofeiForm.setStartCreate(form.getStatus());
			// 项目名称
			huixiaofeiForm.setStartCreate(form.getTimeStartSrch());
			// 是否交保证金
			huixiaofeiForm.setEndCreate(form.getTimeEndSrch());
			attr.addFlashAttribute("huixiaofeiForm", huixiaofeiForm);
			modelAndView = new ModelAndView(HuixiaofeiDefine.RE_LIST_PATH);
		}
		return modelAndView;
	}

	/**
	 * 用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = BorrowCommonDefine.ISEXISTSUSER_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String isExistsUser(HttpServletRequest request) {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.ISEXISTSUSER_ACTION);
		String message = this.borrowCommonService.isExistsUser(request);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.ISEXISTSUSER_ACTION);
		return message;
	}

	/**
	 * 借款预编码是否存在
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = BorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD, method = RequestMethod.POST)
	@ResponseBody
	public String isExistsBorrowPreNidRecord(HttpServletRequest request) {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD);
		String message = this.borrowCommonService.isExistsBorrowPreNidRecord(request);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD);
		return message;
	}

	/**
	 * 获取最新的借款预编码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = BorrowCommonDefine.GETBORROWPRENID_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String getBorrowPreNid(HttpServletRequest request) {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
		String borrowPreNid = this.borrowCommonService.getBorrowPreNid();
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
		return borrowPreNid;
	}

	/**
	 * 获取放款服务费率 & 还款服务费率
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = BorrowCommonDefine.GETSCALE_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String getBorrowServiceScale(HttpServletRequest request) {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.GETSCALE_ACTION);
		String scale = this.consumeService.getBorrowServiceScale(request);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.GETSCALE_ACTION);
		return scale;
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BorrowCommonDefine.UPLOAD_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_FILE);
		String files = this.borrowCommonService.uploadFile(request, response);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_FILE);
		return files;
	}

	/**
	 * 获取项目名称
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BorrowCommonDefine.GET_BORROW_NAME_ACCOUNT_LIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getBorrowNameAccountList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.GET_BORROW_NAME_ACCOUNT_LIST);

		// 项目名称
		String name = request.getParameter("name");
		if (StringUtils.isEmpty(name)) {
			return "";
		}
		// 资产包编号
		String consumeId = request.getParameter("consumeId");
		if (StringUtils.isEmpty(consumeId)) {
			return "";
		}
		// 拆标行数
		String nameCount = request.getParameter("nameCount");
		if (StringUtils.isEmpty(nameCount)) {
			return "";
		}

		JSONObject obj = this.consumeService.countUserAccount(name, consumeId, Integer.valueOf(nameCount));
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.GET_BORROW_NAME_ACCOUNT_LIST);
		return obj.toJSONString();
	}

	/**
	 * 项目名称的个数
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BorrowCommonDefine.GET_CUNSUME_COUNT, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getCunsumeCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.GET_CUNSUME_COUNT);
		int cunsumeCount = this.consumeService.getConsumeMaxNumber();
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.GET_CUNSUME_COUNT);
		return String.valueOf(cunsumeCount);
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(value = BorrowCommonDefine.DOWNLOAD_CAR_ACTION, method = RequestMethod.POST)
	public void downloadCarAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.DOWNLOAD_CAR_ACTION);
		this.borrowCommonService.downloadCar(request, response, form);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.DOWNLOAD_CAR_ACTION);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BorrowCommonDefine.UPLOAD_CAR_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String uploadCarAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_CAR_ACTION);
		String result = this.borrowCommonService.uploadCar(request, response);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_CAR_ACTION);
		return result;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(value = BorrowCommonDefine.DOWNLOAD_HOUSE_ACTION, method = RequestMethod.POST)
	public void downloadHouseAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.DOWNLOAD_HOUSE_ACTION);
		this.borrowCommonService.downloadHouse(request, response, form);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.DOWNLOAD_HOUSE_ACTION);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BorrowCommonDefine.UPLOAD_HOUSE_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String uploadHouseAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_HOUSE_ACTION);
		String result = this.borrowCommonService.uploadHouse(request, response);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_HOUSE_ACTION);
		return result;
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(value = BorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION, method = RequestMethod.POST)
	public void downloadAuthenAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION);
		this.borrowCommonService.downloadAuthen(request, response, form);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION);
	}

	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = BorrowCommonDefine.UPLOAD_AUTHEN_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String uploadAuthenAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogUtil.startLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_AUTHEN_ACTION);
		String result = this.borrowCommonService.uploadAuthen(request, response);
		LogUtil.endLog(ConsumeController.class.toString(), BorrowCommonDefine.UPLOAD_AUTHEN_ACTION);
		return result;
	}
}
