/**
 * Description:会员管理
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.admin.exception.userparamexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.tenderexception.TenderExceptionBean;
import com.hyjf.admin.exception.tenderexception.TenderExceptionController;
import com.hyjf.admin.manager.content.article.ContentArticleDefine;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.AdminUserListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserRecommendCustomize;

@Controller
@RequestMapping(value = UserparamExceptionDefine.REQUEST_MAPPING)
public class UserparamExceptionController extends BaseController {

	@Resource
	private ManageUsersService usersService;
	
	@Autowired
	private UserTenderExceptionService tenderExceptionService;

	/**
	 * 会员管理列表
	 * 
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(UserparamExceptionDefine.USERS_LIST_ACTION)
	@RequiresPermissions(UserparamExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView searchUserList(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(UserparamExceptionDefine.USERS_LIST_FORM) UserparamExceptionBean searchUser) {
		LogUtil.startLog(UserparamExceptionDefine.THIS_CLASS, UserparamExceptionDefine.USERS_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(UserparamExceptionDefine.USERS_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, searchUser);
		LogUtil.endLog(UserparamExceptionDefine.THIS_CLASS, UserparamExceptionDefine.USERS_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, UserparamExceptionBean form) {

		// 用户角色
		List<ParamName> userRoles = this.usersService.getParamNameList("USER_ROLE");
		modelAndView.addObject("userRoles", userRoles);
		// 用户属性
		List<ParamName> userPropertys = this.usersService.getParamNameList("USER_PROPERTY");
		modelAndView.addObject("userPropertys", userPropertys);
		// 开户状态
		List<ParamName> accountStatus = this.usersService.getParamNameList("ACCOUNT_STATUS");
		modelAndView.addObject("accountStatus", accountStatus);
		// 用户状态
		List<ParamName> userStatus = this.usersService.getParamNameList("USER_STATUS");
		modelAndView.addObject("userStatus", userStatus);
		// 注册平台
		List<ParamName> registPlat = this.usersService.getParamNameList("CLIENT");
		modelAndView.addObject("registPlat", registPlat);
		// 51老用户
		List<ParamName> is51 = this.usersService.getParamNameList("IS_51");
		modelAndView.addObject("is51", is51);
		// 封装查询条件
		Map<String, Object> user = new HashMap<String, Object>();
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		String realName = StringUtils.isNotEmpty(form.getRealName()) ? form.getRealName() : null;
		String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
		String recommendName = StringUtils.isNotEmpty(form.getRecommendName()) ? form.getRecommendName() : null;
		String userRole = StringUtils.isNotEmpty(form.getUserRole()) ? form.getUserRole() : null;
		String userProperty = StringUtils.isNotEmpty(form.getUserProperty()) ? form.getUserProperty() : null;
		String accountStatusStr = StringUtils.isNotEmpty(form.getAccountStatus()) ? form.getAccountStatus() : null;
		String userStatusStr = StringUtils.isNotEmpty(form.getUserStatus()) ? form.getUserStatus() : null;
		String registPlatStr = StringUtils.isNotEmpty(form.getRegistPlat()) ? form.getRegistPlat() : null;
		String is51Str = StringUtils.isNotEmpty(form.getIs51()) ? form.getIs51() : null;
		String regTimeStart = StringUtils.isNotEmpty(form.getRegTimeStart()) ? form.getRegTimeStart() : null;
		String regTimeEnd = StringUtils.isNotEmpty(form.getRegTimeEnd()) ? form.getRegTimeEnd() : null;
		if (form.getRegTimeStart() != null) {
			user.put("regTimeStart", regTimeStart);
		}
		if (form.getRegTimeEnd() != null) {
			user.put("regTimeEnd", regTimeEnd);
		}
		user.put("userName", userName);
		user.put("realName", realName);
		user.put("mobile", mobile);
		user.put("recommendName", recommendName);
		user.put("userRole", userRole);
		user.put("userProperty", userProperty);
		user.put("accountStatus", accountStatusStr);
		user.put("userStatus", userStatusStr);
		user.put("registPlat", registPlatStr);
		user.put("is51", is51Str);
		int recordTotal = this.usersService.countRecordTotal(user);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminUserListCustomize> recordList = this.usersService.getRecordList(user, paginator.getOffset(),
					paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(UserparamExceptionDefine.USERS_LIST_FORM, form);
		}
	}

	/**
	 * 更新用户属性Attribute
	 * 
	 * @param request
	 * @return 进入用户详情页面
	 */
	@ResponseBody
	@RequestMapping(value = UserparamExceptionDefine.UPDATE_USERPARAM_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(UserparamExceptionDefine.PERMISSIONS_MODIFY)
	public Map<String, Object> updateUserParam(@ModelAttribute AdminUserRecommendCustomize form, HttpServletRequest request,
			HttpServletResponse response) {
		LogUtil.startLog(UserparamExceptionDefine.THIS_CLASS, UserparamExceptionDefine.UPDATE_USERPARAM_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("success", true);
		// 画面验证
		if (Validator.isNull(form.getUserId()) || !Validator.isNumber(form.getUserId())) {
			modelAndView.addObject(UserparamExceptionDefine.USERS_UPDATE_FORM, form);
			LogUtil.errorLog(UserparamExceptionDefine.THIS_CLASS, UserparamExceptionDefine.UPDATE_USERPARAM_ACTION,
					"输入内容验证失败", null);
			modelAndView.addObject("success", false);
			modelAndView.addObject("msg", "用户ID不正确");
			return modelAndView.getModel();
		}
		usersService.updateUserParam(Integer.parseInt(form.getUserId()));
		LogUtil.endLog(UserparamExceptionDefine.THIS_CLASS, UserparamExceptionDefine.UPDATE_USERPARAM_ACTION);
		return modelAndView.getModel();
	}

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	/**
	 * 更新全部用户属性Attribute
	 * 
	 * @param request
	 * @return 进入用户详情页面
	 */
	@ResponseBody
	@RequestMapping(value = UserparamExceptionDefine.UPDATE_ALLUSERPARAM_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(UserparamExceptionDefine.PERMISSIONS_MODIFYALL)
	public Map<String, Object> batchUpdateUserParam(@ModelAttribute AdminUserRecommendCustomize form,
			HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(UserparamExceptionDefine.THIS_CLASS, UserparamExceptionDefine.UPDATE_ALLUSERPARAM_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		if (isOver) {
			isOver = false;
			{
				usersService.updateAllUserParam();
				modelAndView.getModel().put(ContentArticleDefine.SUCCESS, true);
				modelAndView.getModel().put(ContentArticleDefine.JSON_VALID_INFO_KEY, "操作成功");
			}
			isOver = true;
		} else {
			modelAndView.getModel().put(ContentArticleDefine.SUCCESS, false);
			modelAndView.getModel().put(ContentArticleDefine.JSON_VALID_INFO_KEY, "已经在进行处理,请勿重复操作");
		}
		LogUtil.endLog(UserparamExceptionDefine.THIS_CLASS, UserparamExceptionDefine.UPDATE_ALLUSERPARAM_ACTION);
		modelAndView.addObject("success", true);
		return modelAndView.getModel();
	}

	
	/**
	 * 出借数据修复页面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = UserparamExceptionDefine.INIT_TENDER_REPAIR_ACTION)
	@RequiresPermissions(UserparamExceptionDefine.PERMISSION_REPAIR_TENDER)
	public ModelAndView initTenderRepairAction(HttpServletRequest request, TenderExceptionBean form) {
		LogUtil.startLog(TenderExceptionController.class.toString(), UserparamExceptionDefine.INIT_TENDER_REPAIR_ACTION);
		ModelAndView modelAndView = new ModelAndView(UserparamExceptionDefine.INIT_TENDER_REPAIR_PATH);
		LogUtil.endLog(TenderExceptionController.class.toString(), UserparamExceptionDefine.INIT_TENDER_REPAIR_ACTION);
		return modelAndView;

	}
	/**
	 * 出借数据修复
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = UserparamExceptionDefine.TENDER_REPAIR_ACTION)
	@RequiresPermissions(UserparamExceptionDefine.PERMISSION_REPAIR_TENDER)
	public ModelAndView tenderRepairAction(HttpServletRequest request, TenderExceptionBean form) {
		LogUtil.startLog(TenderExceptionController.class.toString(), UserparamExceptionDefine.TENDER_REPAIR_ACTION);
		ModelAndView modelAndView = new ModelAndView(UserparamExceptionDefine.INIT_TENDER_REPAIR_PATH);
		//获取需要修复的数据的时间的
		String repairStartDate = request.getParameter("repairStartDate");
		String repairEndDate = request.getParameter("repairEndDate");
		//获取此段时间的出借数据
		List<BorrowTender> borrowTenderList = this.tenderExceptionService.selectBorrowTenderList(repairStartDate,repairEndDate);
		if(borrowTenderList!=null&&borrowTenderList.size()>0){
			for(BorrowTender borrowTender:borrowTenderList){
				this.tenderExceptionService.updateUserTender(borrowTender,repairStartDate,repairEndDate);
			}
		}
		modelAndView.addObject(ManageUsersDefine.SUCCESS, ManageUsersDefine.SUCCESS);
		LogUtil.endLog(TenderExceptionController.class.toString(), UserparamExceptionDefine.TENDER_REPAIR_ACTION);
		return modelAndView;

	}
}
