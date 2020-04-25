package com.hyjf.admin.coupon.config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hyjf.admin.interceptor.TokenInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigExoportCustomize;

/**
 * 优惠券发行
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = CouponConfigDefine.REQUEST_MAPPING)
public class CouponConfigController extends BaseController {

	@Autowired
	private CouponConfigService couponConfigService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value=CouponConfigDefine.INIT,produces = "text/html;charset=UTF-8")
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request,
			RedirectAttributes attr,
			@ModelAttribute(CouponConfigDefine.FORM) CouponConfigBean form) throws UnsupportedEncodingException {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.INIT);
		return modelAndView;
	}

	/**
	 * 查询
	 * 
	 * @param request
	 * @param form
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value=CouponConfigDefine.SEARCH_ACTION,produces = "text/html;charset=UTF-8")
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchAction(HttpServletRequest request,
			RedirectAttributes attr,
			@ModelAttribute(CouponConfigDefine.FORM) CouponConfigBean form) throws UnsupportedEncodingException {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @throws UnsupportedEncodingException 
	 */
	private void createPage(HttpServletRequest request,
			ModelAndView modelAndView, CouponConfigBean form) throws UnsupportedEncodingException {
		CouponConfigCustomize couponConfigCustomize = new CouponConfigCustomize();
		String couponNameSrch = form.getCouponNameSrch();
		if(request.getMethod().equalsIgnoreCase("GET") && StringUtils.isNotEmpty(couponNameSrch)){
		    couponNameSrch = new String(couponNameSrch.getBytes("iso8859-1"),"utf-8");
		    form.setCouponNameSrch(couponNameSrch);
        }
		
		if (StringUtils.isNotEmpty(couponNameSrch)) {
			couponConfigCustomize.setCouponName(couponNameSrch);
		}
		if (StringUtils.isNotEmpty(form.getCouponCodeSrch())) {
			couponConfigCustomize.setCouponCode(form.getCouponCodeSrch());
		}
		if (StringUtils.isNotEmpty(form.getCouponTypeSrch())) {
			couponConfigCustomize.setCouponType(form.getCouponTypeSrch());
		}
		if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
			couponConfigCustomize.setTimeStartSrch(form.getTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
			couponConfigCustomize.setTimeEndSrch(form.getTimeEndSrch());
		}
		
		if(form.getStatus()!=null){
		    couponConfigCustomize.setStatus(form.getStatus());
		}
        
		Integer count = this.couponConfigService
				.countRecord(couponConfigCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			couponConfigCustomize.setLimitStart(paginator.getOffset());
			couponConfigCustomize.setLimitEnd(paginator.getLimit());
			List<CouponConfigCustomize> recordList = this.couponConfigService
					.getRecordList(couponConfigCustomize);
			form.setPaginator(paginator);
			form.setRecordList(recordList);
		}
		modelAndView.addObject(CouponConfigDefine.FORM, form);
	}

	/**
	 * 画面迁移(含有id更新，不含有id添加)
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(save = true)
	@RequestMapping(CouponConfigDefine.INFO_ACTION)
	@RequiresPermissions(value = { CouponConfigDefine.PERMISSIONS_ADD,
			CouponConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public ModelAndView infoAction(HttpServletRequest request,
			RedirectAttributes attr,
			@ModelAttribute(CouponConfigDefine.FORM) CouponConfigBean form) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.INFO_PATH);
		this.initDetail(modelAndView, form);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 初始化详细画面
	 * 
	 * @param modelAndView
	 * @param form
	 * @return
	 */
	private ModelAndView initDetail(ModelAndView modelAndView,
			CouponConfigBean form) {

		// 操作平台
		List<ParamName> clients = this.couponConfigService
				.getParamNameList("CLIENT");
		modelAndView.addObject("clients", clients);
		// 有效期类型
		List<ParamName> expType = this.couponConfigService
                .getParamNameList("COUPON_EXP_TYPE");
        modelAndView.addObject("expType", expType);
        
		// 项目类型
		/*List<BorrowProjectType> projectTypes = couponConfigService
				.getProjectTypeList();*/
		List<BorrowProjectType> couponProjectTypes = couponConfigService
                .getCouponProjectTypeList();
		/*modelAndView.addObject("projectTypes", projectTypes);*/
		modelAndView.addObject("couponProjectTypes", couponProjectTypes);
		// 被选中操作平台
		List<String> selectedClientList = new ArrayList<String>();
		// 被选中项目类型
		List<String> selectedProjectList = new ArrayList<String>();
		// 被选中操作平台表示用
		StringBuffer selectedClientDisplayBuffer = new StringBuffer();
		// 被选中项目类别表示用
		StringBuffer selectedProjectDisplayBuffer = new StringBuffer();
		if (form.getId() != null) {
			String recrodId = String.valueOf(form.getId());
			CouponConfig record = couponConfigService.getRecord(recrodId);
			BeanUtils.copyProperties(record, form);
			// 如果审核人不为空
			if (StringUtils.isNotEmpty(record.getAuditUser())) {
				// 取得审核人的真实姓名
				Admin userInfo = couponConfigService
						.getAdminInfoByUserId(Integer.valueOf(record
								.getAuditUser()));
				form.setAuditUserRealname(userInfo.getTruename());
			}

			// 如果审核时间不为空
			if (record.getAuditTime() != null) {
				// 转换时间戳
				form.setAuditTimeDisplay(GetDate
						.timestamptoStrYYYYMMDDHHMM(record.getAuditTime()
								.toString()));
			}

			if (null != record.getExpirationDate()) {
				form.setExpirationDateStr(GetDate.getDateMyTimeInMillis(record
						.getExpirationDate()));
			}

			modelAndView.addObject(CouponConfigDefine.FORM, form);
			// 被选中操作平台
			String clientSed[] = StringUtils.split(record.getCouponSystem(),
					",");
			 for(int i=0 ; i< clientSed.length;i++){
			         selectedClientList.add(clientSed[i]);
		            if("-1".equals(clientSed[i])){
		                selectedClientDisplayBuffer.append("全部平台");
		                break;
		            }else{
		                for (ParamName paramName : clients) {
		                    if(clientSed[i].equals(paramName.getNameCd())){
		                        if(i!=0&&selectedClientDisplayBuffer.length()!=0){
		                            selectedClientDisplayBuffer.append("/");
		                        }
		                        selectedClientDisplayBuffer.append(paramName.getName());
		                        
		                    }
		                }
		            }
		        }
			
			
			
			modelAndView.addObject("selectedClientList", selectedClientList);
			String selectedClientDisplay = selectedClientDisplayBuffer
					.toString();
			selectedClientDisplay = StringUtils.removeEnd(
					selectedClientDisplay, "/");
			modelAndView.addObject("selectedClientDisplay",
					selectedClientDisplay);
			
			// 被选中项目类型    新逻辑 pcc20160715
			String projectSed[] = StringUtils.split(record.getProjectType(),
					",");
			String selectedProjectDisplay="";
			 if(record.getProjectType().indexOf("-1")!=-1){
			     selectedProjectDisplay="所有散标/新手/智投项目";
	           }else{
	               selectedProjectDisplayBuffer.append("所有");
	               for (String project : projectSed) {
	                    if("1".equals(project)){
	                        selectedProjectDisplayBuffer.append("散标/");
	                    }  
	                    if("2".equals(project)){
                            selectedProjectDisplayBuffer.append("");
                        } 
	                    if("3".equals(project)){
                            selectedProjectDisplayBuffer.append("新手/");
                        } 
	                    if("4".equals(project)){
                            selectedProjectDisplayBuffer.append("");
                        } 
	                    if("5".equals(project)){
                            selectedProjectDisplayBuffer.append("");
                        } 
	                    if("6".equals(project)){
	                        selectedProjectDisplayBuffer.append("智投/");
	                    }
	                    
	               }
	               selectedProjectDisplay= selectedProjectDisplayBuffer
	                       .toString();
	               selectedProjectDisplay = StringUtils.removeEnd(
                           selectedProjectDisplay, "/");
	               selectedProjectDisplay=selectedProjectDisplay+"项目";
			}
			modelAndView.addObject("selectedProjectList", selectedProjectList);
			modelAndView.addObject("selectedProjectDisplay",
			        selectedProjectDisplay);
			return modelAndView;
		}else{
		    form.setExpirationDateStr(GetDate.getDateMyTimeInMillis(GetDate.getNowTime10()));
		    modelAndView.addObject(CouponConfigDefine.FORM, form);
		}
		return modelAndView;
	}

	/**
	 * 添加信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(check = true, forward = CouponConfigDefine.TOKEN_INIT_PATH)
	@RequestMapping(value = CouponConfigDefine.INSERT_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_ADD)
	public ModelAndView insertAction(HttpServletRequest request,
			CouponConfigBean form) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.INFO_PATH);
		// 画面验证
		this.validatorFieldCheckInsert(modelAndView, form);//验证令牌
		// 画面验证
		this.validatorFieldCheckInsert(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
		 // 操作平台
	        List<ParamName> clients = this.couponConfigService
	                .getParamNameList("CLIENT");
	        modelAndView.addObject("clients", clients);
	        // 项目类型
	        List<BorrowProjectType> projectTypes = couponConfigService
	                .getProjectTypeList();
	        modelAndView.addObject("projectTypes", projectTypes);
	        
	        String couponSystem=form.getCouponSystem();
	        if(couponSystem!=null&&couponSystem.length()>0){
	            String[] couponSystemArr=couponSystem.split(",");
	            // 被选中操作平台
	            List<String> selectedClientList = new ArrayList<String>();
	            for (String system : couponSystemArr) {
	                selectedClientList.add(system);
                }
	            modelAndView.addObject("selectedClientList", selectedClientList);
	        }
	        
	        String projectType=form.getProjectType();
	        if(projectType!=null&&projectType.length()>0){
                String[] projectTypeArr=projectType.split(",");
             // 被选中项目类型
                List<String> selectedProjectList = new ArrayList<String>();
                for (String type : projectTypeArr) {
                    selectedProjectList.add(type);
                }
                modelAndView.addObject("selectedProjectList", selectedProjectList);
            }
	        
	        
			modelAndView.addObject(CouponConfigDefine.FORM, form);
			return modelAndView;
		}
		// 数据插入
		this.couponConfigService.insertRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(CouponConfigDefine.SUCCESS,
				CouponConfigDefine.SUCCESS);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.INSERT_ACTION);
		return modelAndView;
	}

	/**
	 * 更新页面跳转
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(save = true)
	@RequestMapping(value = CouponConfigDefine.UPDATE_INFO_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateInfoAction(HttpServletRequest request,
			CouponConfigBean form) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.UPDATE_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.MODIFY_PATH);

		this.initDetail(modelAndView, form);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.UPDATE_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 修改信息
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(check = true, forward = CouponConfigDefine.TOKEN_INIT_PATH)
	@RequestMapping(value = CouponConfigDefine.UPDATE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateAction(HttpServletRequest request,
			CouponConfigBean form) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.MODIFY_PATH);
		// 画面验证
		this.validatorFieldCheckUpdate(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(CouponConfigDefine.FORM, form);
			return modelAndView;
		}
		
		// 更新
		this.couponConfigService.updateRecord(form);
		// 跳转页面用（info里面有）
		modelAndView.addObject(CouponConfigDefine.SUCCESS,
				CouponConfigDefine.SUCCESS);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.UPDATE_ACTION);
		return modelAndView;
	}
	
	
	

	/**
	 * 画面校验(新增数据)
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheckInsert(ModelAndView modelAndView,
			CouponConfigBean form) {
		// 优惠券名称
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "couponName",
				form.getCouponName(), 50, true);
		// 优惠券面值
		// 半角数字 必填项 长度最长11位，整数8位，小数2位
		ValidatorFieldCheckUtil.validateNumLength(
				modelAndView,
				"couponQuota",
				form.getCouponQuota() == null ? StringUtils.EMPTY : String
						.valueOf(form.getCouponQuota()), 8, 2, true);
		// 发行数量
		// 半角数字 必填项 不大于10位
		ValidatorFieldCheckUtil.validateNum(
				modelAndView,
				"couponQuantity",
				form.getCouponQuantity() == null ? StringUtils.EMPTY : String
						.valueOf(form.getCouponQuantity()), 10, true);
		if (form.getExpirationType() == 1) {
			// 截止日 检查日期格式(yyyy-mm-dd)
			ValidatorFieldCheckUtil.validateDate(modelAndView,
					"expirationDateStr", form.getExpirationDateStr(), true);
		} else if (form.getExpirationType() == 2){
			// 半角数字 必填项 不大于3位
			ValidatorFieldCheckUtil.validateNum(modelAndView,
					"expirationLength",
					form.getExpirationLength() == null ? StringUtils.EMPTY
							: String.valueOf(form.getExpirationLength()), 3,
					true);
		} else if (form.getExpirationType() == 3){
            // 半角数字 必填项 不大于3位
            ValidatorFieldCheckUtil.validateNum(modelAndView,
                    "expirationLengthDay",
                    form.getExpirationLengthDay() == null ? StringUtils.EMPTY
                            : String.valueOf(form.getExpirationLengthDay()), 5,
                    true);
        }
		// 备注 最大长度300字符
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "content", form.getContent(), 300, false);

		// 操作平台
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "couponSystem",
				form.getCouponSystem());

		// 项目类型
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "projectType",
				form.getProjectType());

		// 项目期限
		if(form.getProjectExpirationType() != 0){
			if (form.getProjectExpirationType() == 2) {
				// 时间段
				// 半角数字 必填项 不大于3位
				ValidatorFieldCheckUtil
						.validateNum(
								modelAndView,
								"projectExpirationLengthMin",
								form.getProjectExpirationLengthMin() == null ? StringUtils.EMPTY
										: String.valueOf(form
												.getProjectExpirationLengthMin()),
								3, true);
				// 半角数字 必填项 不大于3位
				ValidatorFieldCheckUtil
						.validateNum(
								modelAndView,
								"projectExpirationLengthMax",
								form.getProjectExpirationLengthMax() == null ? StringUtils.EMPTY
										: String.valueOf(form
												.getProjectExpirationLengthMax()),
								3, true);

			} else {
				// 时长
				// 半角数字 必填项 不大于3位
				ValidatorFieldCheckUtil
						.validateNum(
								modelAndView,
								"projectExpirationLength",
								form.getProjectExpirationLength() == null ? StringUtils.EMPTY
										: String.valueOf(form
												.getProjectExpirationLength()), 3,
								true);
			}
		}
		
		// 出借金额
		if (form.getTenderQuotaType() == 1) {
			// 金额范围
			// 半角数字 不带小数 不大于11位
			boolean minCheck = ValidatorFieldCheckUtil.validateNum(
					modelAndView, "tenderQuotaMin",
					form.getTenderQuotaMin() == null ? StringUtils.EMPTY
							: String.valueOf(form.getTenderQuotaMin()), 11,
					true);
			// 半角数字 不带小数 不大于11位
			boolean maxCheck = ValidatorFieldCheckUtil.validateNum(
					modelAndView, "tenderQuotaMax",
					form.getTenderQuotaMax() == null ? StringUtils.EMPTY
							: String.valueOf(form.getTenderQuotaMax()), 11,
					true);
			if (minCheck && maxCheck) {
				// 最大金额须大于最小金额
				ValidatorFieldCheckUtil.validateNumMainGreaterSub(modelAndView,
						"tenderQuotaMax", form.getTenderQuotaMax(),
						form.getTenderQuotaMin(), true);
			}

		} else if(form.getTenderQuotaType() == 2) {
			// 金额
			// 半角数字 不带小数 不大于11位
			ValidatorFieldCheckUtil.validateNum(
					modelAndView,
					"tenderQuota",
					form.getTenderQuota() == null ? StringUtils.EMPTY : String
							.valueOf(form.getTenderQuota()), 11, true);
		}
	}

	/**
	 * 画面校验（修改数据）
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheckUpdate(ModelAndView modelAndView,
			CouponConfigBean form) {
		// 优惠券名称 必填项
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "couponName",
				form.getCouponName(), 50, true);

		// 发行数量
        // 半角数字 必填项 不大于10位
        ValidatorFieldCheckUtil.validateNum(
                modelAndView,
                "couponQuantity",
                form.getCouponQuantity() == null ? StringUtils.EMPTY : String
                        .valueOf(form.getCouponQuantity()), 10, true);
		// 已发放数量
		int givedCount = this.couponConfigService.getCouponUsedCount(form
				.getId());
		// 发行数量不能小于已发放到用户手中的数量
		ValidatorFieldCheckUtil.validateCouponCount(modelAndView,
				"couponQuantity", form.getCouponQuantity(), givedCount, true);
	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheckAudit(ModelAndView modelAndView,
			CouponConfigBean form, CouponConfig record) {
		// 审核状态 必填项
		ValidatorFieldCheckUtil.validateRequired(
				modelAndView,
				"status",
				form.getStatus() == null ? StringUtils.EMPTY : String
						.valueOf(form.getStatus()));

		// 审核备注 必填项 不能大于300字符
		ValidatorFieldCheckUtil.validateMaxLength(
				modelAndView,
				"auditContent",
				form.getAuditContent() == null ? StringUtils.EMPTY : String
						.valueOf(form.getAuditContent()), 300, true);
		// 排他校验
		ValidatorFieldCheckUtil.validateSynOperation(modelAndView,
				"synOperation", form.getUpdateTime(), record.getUpdateTime());
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponConfigDefine.DELETE_ACTION)
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_DELETE)
	public String deleteAction(HttpServletRequest request,
			RedirectAttributes attr, CouponConfigBean form) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.DELETE_ACTION);
		this.couponConfigService.deleteRecord(String.valueOf(form.getId()));
		attr.addFlashAttribute(CouponConfigDefine.FORM, form);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.DELETE_ACTION);
		return "redirect:" + CouponConfigDefine.REQUEST_MAPPING + "/"
				+ CouponConfigDefine.INIT;
	}

	/**
	 * 审核画面
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponConfigDefine.AUDIT_ACTION)
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_AUDIT)
	public ModelAndView auditAction(HttpServletRequest request,
			CouponConfigBean form) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.AUDIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.AUDIT_PATH);
		this.initDetail(modelAndView, form);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.AUDIT_ACTION);
		return modelAndView;
	}

	/**
	 * 审核
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(CouponConfigDefine.AUDIT_UPDATE_ACTION)
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_AUDIT)
	public ModelAndView auditUpdateAction(HttpServletRequest request,
			CouponConfigBean form) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.AUDIT_UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(
				CouponConfigDefine.AUDIT_PATH);
		CouponConfig record = couponConfigService.getRecord(String.valueOf(form
				.getId()));
		this.validatorFieldCheckAudit(modelAndView, form, record);

		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(CouponConfigDefine.FORM, form);
			return modelAndView;
		} else {
			// 更新
			record.setStatus(form.getStatus());
			record.setAuditContent(form.getAuditContent());
			this.couponConfigService.auditRecord(record);
		}
		// 跳转页面用（info里面有）
		modelAndView.addObject(CouponConfigDefine.SUCCESS,
				CouponConfigDefine.SUCCESS);
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.AUDIT_UPDATE_ACTION);
		return modelAndView;
	}

	/**
	 * 检查编号唯一性
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CouponConfigDefine.CHECK_ACTION)
	@RequiresPermissions(value = { CouponConfigDefine.PERMISSIONS_ADD,
			CouponConfigDefine.PERMISSIONS_MODIFY }, logical = Logical.OR)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.CHECK_ACTION);
		String param = request.getParameter("param");
		// 优惠券编号
		String couponCode = request.getParameter("code");
		int issuNum = 0;
		JSONObject ret = new JSONObject();
		if (StringUtils.isNotEmpty(couponCode)) {
			issuNum = couponConfigService.getIssueNumber(couponCode);
		}
		if (StringUtils.isNotEmpty(param)) {
			int count = Integer.parseInt(param);
			if (issuNum > count) {
				String message = ValidatorFieldCheckUtil.getErrorMessage(
						"coupon.quantity", "");
				if("errors.coupon.quantity".equals(message)){
				    message="修改数量不能小于已发放数量，已发放"+issuNum+"张";
				}
//				message = message.replace("{label}", "发行数量");
				ret.put(CouponConfigDefine.JSON_VALID_INFO_KEY, message);
			}
		}
		// 没有错误时,返回y
		if (!ret.containsKey(CouponConfigDefine.JSON_VALID_INFO_KEY)) {
			ret.put(CouponConfigDefine.JSON_VALID_STATUS_KEY,
					CouponConfigDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 导出功能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	@RequestMapping(CouponConfigDefine.EXPORT_ACTION)
	@RequiresPermissions(CouponConfigDefine.PERMISSIONS_EXPORT)
	public void exportAction(HttpServletRequest request,
			HttpServletResponse response, CouponConfigBean form)
			throws Exception {
		LogUtil.startLog(CouponConfigController.class.toString(),
				CouponConfigDefine.EXPORT_ACTION);
		// 表格sheet名称
		String sheetName = "优惠券发行列表";
		CouponConfigCustomize couponConfigCustomize = new CouponConfigCustomize();
        String couponNameSrch = form.getCouponNameSrch();
        if(request.getMethod().equalsIgnoreCase("GET") && StringUtils.isNotEmpty(couponNameSrch)){
            couponNameSrch = new String(couponNameSrch.getBytes("iso8859-1"),"utf-8");
            form.setCouponNameSrch(couponNameSrch);
        }
        
        if (StringUtils.isNotEmpty(couponNameSrch)) {
            couponConfigCustomize.setCouponName(couponNameSrch);
        }
        if (StringUtils.isNotEmpty(form.getCouponCodeSrch())) {
            couponConfigCustomize.setCouponCode(form.getCouponCodeSrch());
        }
        if (StringUtils.isNotEmpty(form.getCouponTypeSrch())) {
            couponConfigCustomize.setCouponType(form.getCouponTypeSrch());
        }
        if (StringUtils.isNotEmpty(form.getTimeStartSrch())) {
            couponConfigCustomize.setTimeStartSrch(form.getTimeStartSrch());
        }
        if (StringUtils.isNotEmpty(form.getTimeEndSrch())) {
            couponConfigCustomize.setTimeEndSrch(form.getTimeEndSrch());
        }
        
        if(form.getStatus()!=null){
            couponConfigCustomize.setStatus(form.getStatus());
        }
        
        
		List<CouponConfigExoportCustomize> resultList = this.couponConfigService
				.exoportRecordList(couponConfigCustomize);
		
		/*List<CouponConfigCustomize> resultList = this.couponConfigService
                .exoportRecordList(couponConfigCustomize);*/
		String fileName = sheetName + StringPool.UNDERLINE
				+ GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;
		String[] titles = new String[] { "序号", "优惠券名称", "优惠券编号", "优惠券类型", "优惠券面值",
				"发行数量", "已发放数量", "有效期", "使用范围-操作平台", "使用范围-项目类型",
                "使用范围-出借金额","使用范围-项目期限","状态","发行时间" };
		
		//操作平台
        List<ParamName> clients=this.couponConfigService.getParamNameList("CLIENT");
        //modelAndView.addObject("clients", clients);
		
//		序号、优惠券名称、优惠券类型、优惠券面值、发行数量、已发放数量、有效期、使用范围-操作平台、使用范围-项目类型、使用范围-项目期限、使用范围-出借金额
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
				sheetName + "_第1页");

		if (resultList != null && resultList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < resultList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook,
							titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}
				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
				    CouponConfigExoportCustomize pInfo = resultList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					// 序号
					if (celLength == 0) {
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(pInfo.getCouponName());
					}  else if (celLength == 2) {
                        cell.setCellValue(pInfo.getCouponCode());
                    } else if (celLength == 3) {
						cell.setCellValue(pInfo.getCouponType());
					} else if (celLength == 4) {
					    if("加息券".equals(pInfo.getCouponType())){
					        cell.setCellValue(pInfo.getCouponQuota()+"%");
					    }else if("体验金".equals(pInfo.getCouponType()) || "代金券".equals(pInfo.getCouponType())){
					        cell.setCellValue(pInfo.getCouponQuota()+"元");
					    }
						
					} else if (celLength == 5) {
						cell.setCellValue(pInfo.getCouponQuantity());
					} else if (celLength == 6) {
						cell.setCellValue(pInfo.getIssueNumber());
					} else if (celLength == 7) {
						cell.setCellValue(pInfo.getExpirationDate());
					} else if (celLength == 8) {
					    //被选中操作平台
				        String clientString = "";

				      //被选中操作平台
				        String clientSed[] = StringUtils.split(pInfo.getCouponSystem(), ",");
				        for(int j=0 ; j< clientSed.length;j++){
				            if("-1".equals(clientSed[j])){
				                clientString=clientString+"不限";
				                break;
				            }else{
				                for (ParamName paramName : clients) {
				                    if(clientSed[j].equals(paramName.getNameCd())){
				                        if(j!=0&&clientString.length()!=0){
				                            clientString=clientString+"、";
				                        }
				                        clientString=clientString+paramName.getName();
				                        
				                    }
				                }
				            }
				        }
					    
						cell.setCellValue(clientString);
					} else if (celLength == 9) {
					    // 被选中项目类别表示用
				        StringBuffer selectedProjectDisplayBuffer = new StringBuffer();
				        // 被选中项目类型   新逻辑 pcc20160715
			            String projectSed[] = StringUtils.split(pInfo.getProjectType(),
			                    ",");
			            String selectedProjectDisplay="";
			             if(pInfo.getProjectType().indexOf("-1")!=-1){
			                 selectedProjectDisplay="所有散标/新手/智投项目";
			               }else{
			                   selectedProjectDisplayBuffer.append("所有");
			                   for (String project : projectSed) {
			                        if("1".equals(project)){
			                            selectedProjectDisplayBuffer.append("散标/");
			                        }  
			                        if("2".equals(project)){
			                            selectedProjectDisplayBuffer.append("");
			                        } 
			                        if("3".equals(project)){
			                            selectedProjectDisplayBuffer.append("新手/");
			                        } 
			                        if("4".equals(project)){
			                            selectedProjectDisplayBuffer.append("");
			                        } 
			                        if("5".equals(project)){
                                        selectedProjectDisplayBuffer.append("");
                                    } 
			                        if("6".equals(project)){
                                        selectedProjectDisplayBuffer.append("智投/");
                                    }
                                    
			                   }
			                   selectedProjectDisplay= selectedProjectDisplayBuffer
			                           .toString();
			                   selectedProjectDisplay = StringUtils.removeEnd(
			                           selectedProjectDisplay, "/");
			                   selectedProjectDisplay=selectedProjectDisplay+"项目";
			            }
						cell.setCellValue(selectedProjectDisplay);
					} else if (celLength == 10) {
                        cell.setCellValue(pInfo.getTenderQuota());
                    } else if (celLength == 11) {
                        cell.setCellValue(pInfo.getProjectExpirationType());
                    } else if (celLength == 12) {
                        cell.setCellValue(pInfo.getStatus());
                    }else if (celLength == 13) {
                        cell.setCellValue(pInfo.getAddTime());
                    }
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);

		LogUtil.endLog(CouponConfigController.class.toString(),
				CouponConfigDefine.EXPORT_ACTION);
	}

}
