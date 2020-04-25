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

package com.hyjf.admin.manager.user.manageruser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.changelog.ChangeLogBean;
import com.hyjf.admin.manager.user.changelog.ChangeLogDefine;
import com.hyjf.admin.manager.user.changelog.ChangeLogService;
import com.hyjf.bank.service.user.transpassword.TransPasswordAjaxBean;
import com.hyjf.bank.service.user.transpassword.TransPasswordService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.customize.ChangeLogCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserBankOpenAccountCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserInfosUpdCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserRecommendCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserUpdateCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Controller
@RequestMapping(value = ManageUsersDefine.REQUEST_MAPPING)
public class ManageUsersController extends BaseController {

	@Resource
	private ManageUsersService usersService;
	@Autowired
	private ChangeLogService changeLogService;


	/**
	 * 会员管理列表
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(ManageUsersDefine.USERS_LIST_ACTION)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_VIEW)
	public ModelAndView searchUserList(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(ManageUsersDefine.USERS_LIST_FORM) UserListCustomizeBean form) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.USERS_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.USERS_LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.USERS_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 创建权限维护分页机能
	 *
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, UserListCustomizeBean form) {

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
		// 用户类型
		List<ParamName> userTypes = this.usersService.getParamNameList("USER_TYPE");
		modelAndView.addObject("userTypes", userTypes);
		// 借款人类型
		List<ParamName> borrowTypes = this.usersService.getParamNameList("BORROWER_TYPE");
		modelAndView.addObject("borrowTypes", borrowTypes);
		
		// 用户来源 new added by libin
		List<HjhInstConfig> hjhInstConfigList = this.usersService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				list = new String[] { form.getCombotreeSrch() };
				form.setCombotreeListSrch(list);
			}
		}

		// 封装查询条件
		Map<String, Object> user = new HashMap<String, Object>();
		String userName = StringUtils.isNotEmpty(form.getUserName()) ? form.getUserName() : null;
		String realName = StringUtils.isNotEmpty(form.getRealName()) ? form.getRealName() : null;
		String mobile = StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : null;
		String recommendName = StringUtils.isNotEmpty(form.getRecommendName()) ? form.getRecommendName() : null;
		String userRole = StringUtils.isNotEmpty(form.getUserRole()) ? form.getUserRole() : null;
		String userType = StringUtils.isNotEmpty(form.getUserType()) ? form.getUserType() : null;
		String userProperty = StringUtils.isNotEmpty(form.getUserProperty()) ? form.getUserProperty() : null;
		String accountStatusStr = StringUtils.isNotEmpty(form.getAccountStatus()) ? form.getAccountStatus() : null;
		String userStatusStr = StringUtils.isNotEmpty(form.getUserStatus()) ? form.getUserStatus() : null;
		String registPlatStr = StringUtils.isNotEmpty(form.getRegistPlat()) ? form.getRegistPlat() : null;
		String is51Str = StringUtils.isNotEmpty(form.getIs51()) ? form.getIs51() : null;
		String vipType = StringUtils.isNotEmpty(form.getVipType()) ? form.getVipType() : null;
		String regTimeStart = StringUtils.isNotEmpty(form.getRegTimeStart()) ? form.getRegTimeStart() : null;
		String regTimeEnd = StringUtils.isNotEmpty(form.getRegTimeEnd()) ? form.getRegTimeEnd() : null;
		String borrowerType = StringUtils.isNotEmpty(form.getBorrowerType()) ? form.getBorrowerType() : null;
		String customerId = StringUtils.isNotEmpty(form.getCustomerId()) ? form.getCustomerId() : null;
		
		String instCodeSrch = StringUtils.isNotEmpty(form.getInstCodeSrch()) ? form.getInstCodeSrch() : null;
		
		String[] combotreeListSrchStr = form.getCombotreeListSrch();
		user.put("regTimeStart", regTimeStart);
		user.put("regTimeEnd", regTimeEnd);
		user.put("userName", userName);
		user.put("realName", realName);
		user.put("mobile", mobile);
		user.put("recommendName", recommendName);
		user.put("userRole", userRole);
		user.put("userType", userType);
		user.put("userProperty", userProperty);
		user.put("accountStatus", accountStatusStr);
		user.put("userStatus", userStatusStr);
		user.put("registPlat", registPlatStr);
		user.put("borrowerType", borrowerType);
		user.put("is51", is51Str);
		user.put("vipType", vipType);
		user.put("combotreeListSrch", combotreeListSrchStr);
		user.put("customerId", customerId);
		
		user.put("instCodeSrch", instCodeSrch);
		
		int recordTotal = this.usersService.countRecordTotal(user);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminUserListCustomize> recordList = this.usersService.getRecordList(user, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(ManageUsersDefine.USERS_LIST_FORM, form);
		}
	}

	/**
	 * 用户详情
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(ManageUsersDefine.USER_DETAIL_ACTION)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_INFO)
	public ModelAndView searchUserDetail(HttpServletRequest request) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.USER_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.USER_DETAIL_PATH);
		// 获取用户id
		String userIdStr = request.getParameter("userId");
		int userId = Integer.parseInt(userIdStr);
		// 根据用户id查询用户详情信息
		AdminUserDetailCustomize user = usersService.searchUserDetailById(userId);
		modelAndView.addObject(ManageUsersDefine.USERS_DETAIL_FORM, user);

		// 通过当前用户ID 查询用户所在一级分部,从而关联用户所属渠道
		// 合规自查添加
		// 20181205 产品需求, 屏蔽渠道,只保留用户ID
//		AdminUserDetailCustomize userUtmInfo = usersService.selectUserUtmInfo(userId);

		// 判断是否关联渠道标识(用于区分已打印的二维码的url和从网站新生成的url)
//        if (userUtmInfo != null){
//            // 二维码用
//            modelAndView.addObject("wechatQRUrl", PropUtils.getSystem("hyjf.admin.qrcode.url") + "refferUserId=" +userId+ "&utmId=" + userUtmInfo.getSourceId() + "&utmSource=" + this.toUnicode(userUtmInfo.getSourceName()));
//            // 邀请链接使用
//            modelAndView.addObject("webPageUrl", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/landingpage/init.do?refferUserId="+userId+ "&utmId=" + userUtmInfo.getSourceId() + "&utmSource=" + userUtmInfo.getSourceName());
//        }else {
            // 二维码用
            modelAndView.addObject("wechatQRUrl", PropUtils.getSystem("hyjf.admin.qrcode.url") + "refferUserId=" +userId);
            // 邀请链接使用
            modelAndView.addObject("webPageUrl", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/landingpage/init.do?refferUserId="+userId);
//        }

		// 获取vip信息
		if (user.getVipId() == null || user.getVipId() == 0) {
			modelAndView.addObject("vipInfo", null);
		} else {
			VipInfo vipInfo = usersService.getVipInfoById(user.getVipId());
			modelAndView.addObject("vipInfo", vipInfo);
		}
		// 获取测评信息
		UserEvalationResultCustomize userEvalationResultCustomize = usersService.getEvalationResultByUserId(userId);
		if (null != userEvalationResultCustomize && null != userEvalationResultCustomize.getCreatetime()) {
			// 获取用户信息
			Users users = this.usersService.getUsersByUserId(Integer.valueOf(userId));
			//获取评测时间加一年的毫秒数18.2.2评测 19.2.2
			Long lCreate = users.getEvaluationExpiredTime().getTime();
			//获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
			Long lNow = System.currentTimeMillis();
			if (lCreate <= lNow) {
				//已过期需要重新评测2已过期、1有效
				modelAndView.addObject("isEvalation", "2");
			} else {
				modelAndView.addObject("isEvalation", "1");
			}
		}
		modelAndView.addObject("userEvalationResult", userEvalationResultCustomize);
		/**********************************************银行存管 修改  pcc start****************************************************/
		//用户开户信息
		AdminUserBankOpenAccountCustomize userBankOpenAccount =usersService.selectBankOpenAccountById(userId);
        modelAndView.addObject("userBankOpenAccount", userBankOpenAccount);
        
        CompanyInfoBean bean = usersService.selectCompanyInfoByUserId(userId);
        //查询企业信息  
        modelAndView.addObject("enterpriseInformation", bean);
        /**********************************************银行存管 修改  pcc  end****************************************************/
        //第三方平台绑定信息
        BindUsers bindUsers = usersService.queryBindUsers(userId,CustomConstants.PLATFORM_ID_HJS);
        modelAndView.addObject("bindUsers", bindUsers);
        
        //电子签章
        CertificateAuthority certificateAuthority = usersService.selectCertificateAuthorityByUserId(userId);
        modelAndView.addObject("certificateAuthority", certificateAuthority);
        
		// 文件服务地址
		String hostUrl = CustomConstants.WEB_DOMAIN_HOST;
		modelAndView.addObject("hostUrl", hostUrl);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.USER_DETAIL_ACTION);
		return modelAndView;

	}

    /**
     * 对二维码中的汉字进行 Unicode 编码
     * @param str
     * @return
     */
	private String toUnicode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

    /**
	 * 获取用户编辑信息
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(ManageUsersDefine.INIT_USER_UPDATE_ACTION)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_MODIFY)
	public ModelAndView searchUpdateUser(HttpServletRequest request) {

		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INIT_USER_UPDATE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.UPDATE_USER_PATH);
		// 用户状态
		List<ParamName> userStatus = this.usersService.getParamNameList("USER_STATUS");
		modelAndView.addObject("userStatus", userStatus);
		// 用户角色
		List<ParamName> userRoles = this.usersService.getParamNameList("USER_ROLE");
		modelAndView.addObject("userRoles", userRoles);
		// 借款人类型
		List<ParamName> borrowTypes = this.usersService.getParamNameList("BORROWER_TYPE");
		modelAndView.addObject("borrowTypes", borrowTypes);
		// 获取用户id
		String userIdStr = request.getParameter("userId");
		int userId = Integer.parseInt(userIdStr);
		// 根据用户id查询用户详情信息
		AdminUserUpdateCustomize user = usersService.searchUserUpdateById(userId);
		modelAndView.addObject(ManageUsersDefine.USERS_UPDATE_FORM, user);
		// 加载修改日志
		ChangeLogBean logBean = new ChangeLogBean();
		logBean.setUserId(userId);
		logBean.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_USERINFO);
		List<ChangeLogCustomize> recordList = this.changeLogService.getUserRecordList(logBean, -1, -1);
		modelAndView.addObject(ManageUsersDefine.USERS_CHANGELOG_FORM, recordList);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INIT_USER_UPDATE_ACTION);
		return modelAndView;

	}

	/**
	 * 更新用户信息
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(value = ManageUsersDefine.UPDATE_USER_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_MODIFY)
	public ModelAndView updateUser(@ModelAttribute AdminUserUpdateCustomize form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_USER_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.UPDATE_USER_PATH);
		// 画面验证
		this.validatorFieldCheckUpdate(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			// 用户状态
			List<ParamName> userStatus = this.usersService.getParamNameList("USER_STATUS");
			modelAndView.addObject("userStatus", userStatus);
			// 用户角色
			List<ParamName> userRoles = this.usersService.getParamNameList("USER_ROLE");
			modelAndView.addObject("userRoles", userRoles);
			// 借款人类型
			List<ParamName> borrowTypes = this.usersService.getParamNameList("BORROWER_TYPE");
			modelAndView.addObject("borrowTypes", borrowTypes);
			modelAndView.addObject(ManageUsersDefine.USERS_UPDATE_FORM, form);
			LogUtil.errorLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_USER_ACTION, "输入内容验证失败", null);
			return modelAndView;
		}
		usersService.updateUser(form);
		this.usersService.sendCAChangeMQ(form);
		modelAndView.addObject(ManageUsersDefine.SUCCESS, ManageUsersDefine.SUCCESS);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_USER_ACTION);
		return modelAndView;
	}

	// /**
	// * 更新用户属性Attribute
	// *
	// * @param request
	// * @return 进入用户详情页面
	// */
	// @ResponseBody
	// @RequestMapping(value = ManageUsersDefine.UPDATE_USERPARAM_ACTION, method
	// = RequestMethod.POST)
	// @RequiresPermissions(ManageUsersDefine.PERMISSIONS_UPDATE)
	// public Map<String, Object> updateUserParam(@ModelAttribute
	// UserRecommendCustomize form, HttpServletRequest request,
	// HttpServletResponse response) {
	// LogUtil.startLog(ManageUsersDefine.THIS_CLASS,
	// ManageUsersDefine.UPDATE_USERPARAM_ACTION);
	// ModelAndView modelAndView = new ModelAndView();
	// modelAndView.addObject("success", true);
	// // 画面验证
	// if (Validator.isNull(form.getUserId()) ||
	// !Validator.isNumber(form.getUserId())) {
	// modelAndView.addObject(ManageUsersDefine.USERS_UPDATE_FORM, form);
	// LogUtil.errorLog(ManageUsersDefine.THIS_CLASS,
	// ManageUsersDefine.MODIFY_RE_ACTION, "输入内容验证失败", null);
	// modelAndView.addObject("success", false);
	// modelAndView.addObject("msg", "用户ID不正确");
	// return modelAndView.getModel();
	// }
	// usersService.updateUserParam(Integer.parseInt(form.getUserId()));
	// LogUtil.endLog(ManageUsersDefine.THIS_CLASS,
	// ManageUsersDefine.UPDATE_USERPARAM_ACTION);
	// return modelAndView.getModel();
	// }
	//
	// /**
	// * 更新全部用户属性Attribute
	// *
	// * @param request
	// * @return 进入用户详情页面
	// */
	// @ResponseBody
	// @RequestMapping(value = ManageUsersDefine.UPDATE_ALLUSERPARAM_ACTION,
	// method = RequestMethod.POST)
	// @RequiresPermissions(ManageUsersDefine.PERMISSIONS_UPDATE)
	// public Map<String, Object> batchUpdateUserParam(@ModelAttribute
	// UserRecommendCustomize form,
	// HttpServletRequest request, HttpServletResponse response) {
	// LogUtil.startLog(ManageUsersDefine.THIS_CLASS,
	// ManageUsersDefine.UPDATE_ALLUSERPARAM_ACTION);
	// ModelAndView modelAndView = new ModelAndView();
	// usersService.updateAllUserParam();
	// LogUtil.endLog(ManageUsersDefine.THIS_CLASS,
	// ManageUsersDefine.UPDATE_ALLUSERPARAM_ACTION);
	// modelAndView.addObject("success", true);
	// return modelAndView.getModel();
	// }

	/**
	 * 获取用户推荐人信息
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(ManageUsersDefine.INIT_MODIFY_RE_ACTION)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_MODIFYRE)
	public ModelAndView searchUserRe(HttpServletRequest request) {

		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INIT_MODIFY_RE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.MODIFY_RE_PATH);
		// 获取用户id
		String userIdStr = request.getParameter("userId");
		int userId = Integer.parseInt(userIdStr);
		// 根据用户id查询用户详情信息
		AdminUserRecommendCustomize userRe = usersService.searchUserRecommend(userId);
		modelAndView.addObject(ManageUsersDefine.MODIFY_RE__FORM, userRe);
		// 加载修改日志
		ChangeLogBean logBean = new ChangeLogBean();
		logBean.setUserId(userId);
		logBean.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_RECOMMEND);
		List<ChangeLogCustomize> recordList = this.changeLogService.getUserRecordList(logBean, -1, -1);
		modelAndView.addObject(ManageUsersDefine.USERS_CHANGELOG_FORM, recordList);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INIT_MODIFY_RE_ACTION);
		return modelAndView;

	}

	/**
	 * 修改用户推荐人
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(value = ManageUsersDefine.MODIFY_RE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_MODIFYRE)
	public ModelAndView modifyRec(@ModelAttribute AdminUserRecommendCustomize form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.MODIFY_RE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.MODIFY_RE_PATH);
		// 画面验证
		this.validatorFieldCheckRe(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			modelAndView.addObject(ManageUsersDefine.USERS_UPDATE_FORM, form);
			LogUtil.errorLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.MODIFY_RE_ACTION, "输入内容验证失败", null);
			return modelAndView;
		}
		String ip = GetCilentIP.getIpAddr(request);
		form.setIp(ip);
		usersService.updateUserRe(form);
		modelAndView.addObject(ManageUsersDefine.SUCCESS, ManageUsersDefine.SUCCESS);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_USER_ACTION);
		return modelAndView;
	}

	/**
	 * 获取用户身份证信息
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(ManageUsersDefine.INIT_MODIFY_IDCARD_ACTION)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_MODIFYRE)
	public ModelAndView searchIdCard(HttpServletRequest request) {

		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INIT_MODIFY_IDCARD_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.MODIFY_IDCARD_PATH);
		// 获取用户id
		String userIdStr = request.getParameter("userId");
		int userId = Integer.parseInt(userIdStr);
		// 根据用户id查询用户详情信息
		AdminUserRecommendCustomize userRe = usersService.searchUserRecommend(userId);
		modelAndView.addObject(ManageUsersDefine.MODIFY_IDCARD_FORM, userRe);
		// 加载修改日志
		ChangeLogBean logBean = new ChangeLogBean();
		logBean.setUserId(userId);
		logBean.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_IDCARD);
		List<ChangeLogCustomize> recordList = this.changeLogService.getUserRecordList(logBean, -1, -1);
		modelAndView.addObject(ManageUsersDefine.IC_CHANGELOG_FORM, recordList);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INIT_MODIFY_IDCARD_ACTION);
		return modelAndView;

	}

	/**
	 * 修改用户身份证
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(value = ManageUsersDefine.MODIFY_IC_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_MODIFYIDCARD)
	public ModelAndView modifyIdCard(@ModelAttribute AdminUserRecommendCustomize form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.MODIFY_IC_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.MODIFY_IDCARD_PATH);
		usersService.updateUserIdCard(form);
		this.usersService.sendCAChangeMQ(form);
		modelAndView.addObject(ManageUsersDefine.SUCCESS, ManageUsersDefine.SUCCESS);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.MODIFY_IC_ACTION);
		return modelAndView;
	}

	/**
	 * 更新推荐人信息,CRM平台调用
	 * 
	 * @param userId
	 * @param SpreadsUserId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ManageUsersDefine.UPDATE_REC_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String updateRec(@RequestParam("userId") String userId, @RequestParam("spreadsUserId") String spreadsUserId, @RequestParam("operationName") String operationName, HttpServletRequest request,
			HttpServletResponse response) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_REC_ACTION);

		JSONObject ret = new JSONObject();
		// 根据userId检索用户是否存在
		int count = this.usersService.countUserById(userId);
		if (count == 0) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("null", "");
			message = message.replace("{label}", "用户id");
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, "N");
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		// 根据推荐人ID检索推荐人是否存在
		count = this.usersService.countUserById(spreadsUserId);
		if (count == 0) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("null", "");
			message = message.replace("{label}", "推荐人ID");
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, "N");
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		// 更新推荐人
		String ip = GetCilentIP.getIpAddr(request);
		int updateCount = this.usersService.updateSpreadsUsers(userId, spreadsUserId, operationName, ip);
		if (updateCount == 0) {
			String message = "更新推荐人失败";
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, "N");
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		// 没有错误时,返回y
		if (!ret.containsKey(ManageUsersDefine.JSON_VALID_INFO_KEY)) {
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, ManageUsersDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_REC_ACTION);
		return ret.toString();
	}

	/**
	 * 检查手机号码或用户名唯一性
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ManageUsersDefine.CHECK_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = ManageUsersDefine.PERMISSIONS_MODIFY)
	public String checkAction(HttpServletRequest request) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.CHECK_ACTION);

		String userId = request.getParameter("id");
		String name = request.getParameter("name");
		String param = request.getParameter("param");

		JSONObject ret = new JSONObject();
		// 检查手机号码唯一性
		if ("mobile".equals(name)) {
			int cnt = usersService.countUserByMobile(GetterUtil.getInteger(userId), param);
			if (cnt > 0) {
				String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
				message = message.replace("{label}", "手机号");
				ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, message);
			}
		}
		// 没有错误时,返回y
		if (!ret.containsKey(ManageUsersDefine.JSON_VALID_INFO_KEY)) {
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, ManageUsersDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 检查手机号码或用户名唯一性
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ManageUsersDefine.CHECK_RE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(value = ManageUsersDefine.PERMISSIONS_MODIFYRE)
	public String checkReAction(HttpServletRequest request) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.CHECK_ACTION);

		String userId = request.getParameter("id");
		String name = request.getParameter("name");
		String param = request.getParameter("param");

		JSONObject ret = new JSONObject();
		// 检查手机号码唯一性
		if ("recommendName".equals(name)) {
			// 检查用户名唯一性
			if (Validator.isNotNull(userId)) {
				if (StringUtils.isNotEmpty(param)) {
					int recomFlag = this.usersService.checkRecommend(userId, param);
					if (recomFlag == 2) {// 推荐人不能是自己
						String message = "不能将推荐人设置为自己";
						ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, message);
					} else if (recomFlag == 1) {// 推荐人不存在
						String message = "推荐人不存在";
						ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, message);
					}
				}
			}
		}
		// 没有错误时,返回y
		if (!ret.containsKey(ManageUsersDefine.JSON_VALID_INFO_KEY)) {
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, ManageUsersDefine.JSON_VALID_STATUS_OK);
		}
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.CHECK_ACTION);
		return ret.toString();
	}

	/**
	 * 具有   组织机构查看权限  的导出, 可以导出更多的字段
	 * 根据业务需求导出相应的表格 此处暂时为可用情况 缺陷： 1.无法指定相应的列的顺序， 2.无法配置，excel文件名，excel sheet名称
	 * 3.目前只能导出一个sheet 4.列的宽度的自适应，中文存在一定问题
	 * 5.根据导出的业务需求最好可以在导出的时候输入起止页码，因为在大数据量的情况下容易造成卡顿
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(ManageUsersDefine.ENHANCE_EXPORT_USERS_ACTION)
	@RequiresPermissions(value = {ManageUsersDefine.PERMISSIONS_EXPORT, ManageUsersDefine.ENHANCE_PERMISSIONS_EXPORT}, logical = Logical.AND)
	public void enhanceExportExcel(@ModelAttribute UserListCustomizeBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
		// 表格sheet名称
		String sheetName = "会员列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		// 需要输出的结果列表
		// 封装查询条件
		Map<String, Object> userMap = new HashMap<String, Object>();
		String regionName = StringUtils.isNotEmpty(form.getRegionName()) ? form.getRegionName() : null;
		String branchName = StringUtils.isNotEmpty(form.getBranchName()) ? form.getBranchName() : null;
		String departmentName = StringUtils.isNotEmpty(form.getDepartmentName()) ? form.getDepartmentName() : null;
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
		String vipType = StringUtils.isNotEmpty(form.getVipType()) ? form.getVipType() : null;
		String regTimeStart = StringUtils.isNotEmpty(form.getRegTimeStart()) ? form.getRegTimeStart() : null;
		String regTimeEnd = StringUtils.isNotEmpty(form.getRegTimeEnd()) ? form.getRegTimeEnd() : null;
		
		String instCodeSrch = StringUtils.isNotEmpty(form.getInstCodeSrch()) ? form.getInstCodeSrch() : null;
		
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				list = new String[] { form.getCombotreeSrch() };
				form.setCombotreeListSrch(list);
			}
		}
		String[] combotreeListSrchStr = form.getCombotreeListSrch();
		if (form.getRegTimeStart() != null) {
			userMap.put("regTimeStart", regTimeStart);
		}
		if (form.getRegTimeEnd() != null) {
			userMap.put("regTimeEnd", regTimeEnd);
		}
		userMap.put("regionName", regionName);
		userMap.put("branchName", branchName);
		userMap.put("departmentName", departmentName);
		userMap.put("userName", userName);
		userMap.put("realName", realName);
		userMap.put("mobile", mobile);
		userMap.put("recommendName", recommendName);
		userMap.put("userRole", userRole);
		userMap.put("userProperty", userProperty);
		userMap.put("accountStatus", accountStatusStr);
		userMap.put("userStatus", userStatusStr);
		userMap.put("registPlat", registPlatStr);
		userMap.put("is51", is51Str);
		userMap.put("vipType", vipType);
		
		userMap.put("instCodeSrch", instCodeSrch);
		
		userMap.put("combotreeListSrch", combotreeListSrchStr);
		List<AdminUserListCustomize> recordList = this.usersService.getRecordList(userMap, -1, -1);
		String[] titles = new String[] { "序号", "分公司", "分部", "团队", "用户来源", "用户名", "姓名", "性别", "年龄", "生日","身份证号", "户籍所在地", "手机号码", "会员类型", "用户角色", "用户属性", "推荐人", "51老用户", "用户状态","银行开户状态","银行开户时间","汇付开户状态", "注册平台", "注册时间", "注册所在地" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					AdminUserListCustomize user = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) { // 大区
						cell.setCellValue(user.getRegionName());
					} else if (celLength == 2) { // 分公司
						cell.setCellValue(user.getBranchName());
					} else if (celLength == 3) { // 团队
						cell.setCellValue(user.getDepartmentName());	
					} else if (celLength == 4) { // 用户来源
						cell.setCellValue(user.getInstName());	
					} else if (celLength == 5) {// 用户名
						cell.setCellValue(user.getUserName());
					} else if (celLength == 6) {// 姓名
						cell.setCellValue(user.getRealName());
					} else if (celLength == 7) {// 性别
						if ("1".equals(user.getSex())) {
							cell.setCellValue("男");
						} else if ("2".equals(user.getSex())) {
							cell.setCellValue("女");
						} else {
							cell.setCellValue("未知");
						}
					} else if (celLength == 8) {// 年龄
						cell.setCellValue(this.getAge(user.getBirthday()));
					} else if (celLength == 9) {// 生日
						cell.setCellValue(user.getBirthday());
					} else if (celLength == 10) {// 身份证号
						cell.setCellValue(AsteriskProcessUtil.getAsteriskedValue(user.getIdcard(), ManageUsersDefine.PERMISSION_HIDE_SHOW));
					} else if (celLength == 11) {// 户籍所在地
						cell.setCellValue(usersService.getAreaByIdCard(user.getIdcard()));
					} else if (celLength == 12) {// 手机号码
						cell.setCellValue(AsteriskProcessUtil.getAsteriskedValue(user.getMobile(), ManageUsersDefine.PERMISSION_HIDE_SHOW));
					} else if (celLength == 13) {// 会员类型
						cell.setCellValue(user.getVipType());
					} else if (celLength == 14) {// 用户角色
						cell.setCellValue(user.getUserRole());
					} else if (celLength == 15) {// 用户属性
						cell.setCellValue(user.getUserProperty());
					} else if (celLength == 16) {// 推荐人
						cell.setCellValue(user.getRecommendName());
					} else if (celLength == 17) {// 51老用户
						cell.setCellValue(user.getIs51());
					} else if (celLength == 18) {// 用户状态
						cell.setCellValue(user.getUserStatus());
					} else if (celLength == 19) {// 银行开户状态
                        cell.setCellValue("1".equals(user.getBankOpenAccount())?"已开户":"未开户");
                    } else if (celLength == 20) {// 银行开户时间
                        cell.setCellValue(user.getBankOpenTime());
                    } else if (celLength == 21) {// 开户状态
                        cell.setCellValue("1".equals(user.getOpenAccount())?"已开户":"未开户");
                    } else if (celLength == 22) {// 注册平台
						cell.setCellValue(user.getRegistPlat());
					} else if (celLength == 23) {// 注册时间
						cell.setCellValue(user.getRegTime());
					} else if (celLength == 24) {// 注册所在地
						cell.setCellValue(user.getRegistArea());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}

	/**
	 * 不具有   组织机构查看权限  的导出
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(ManageUsersDefine.EXPORT_USERS_ACTION)
	@RequiresPermissions(value = ManageUsersDefine.PERMISSIONS_EXPORT)
	public void exportExcel(@ModelAttribute UserListCustomizeBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
		// 表格sheet名称
		String sheetName = "会员列表";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date()) + CustomConstants.EXCEL_EXT;
		// 需要输出的结果列表
		// 封装查询条件
		Map<String, Object> userMap = new HashMap<String, Object>();
		String regionName = StringUtils.isNotEmpty(form.getRegionName()) ? form.getRegionName() : null;
		String branchName = StringUtils.isNotEmpty(form.getBranchName()) ? form.getBranchName() : null;
		String departmentName = StringUtils.isNotEmpty(form.getDepartmentName()) ? form.getDepartmentName() : null;
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
		String vipType = StringUtils.isNotEmpty(form.getVipType()) ? form.getVipType() : null;
		String regTimeStart = StringUtils.isNotEmpty(form.getRegTimeStart()) ? form.getRegTimeStart() : null;
		String regTimeEnd = StringUtils.isNotEmpty(form.getRegTimeEnd()) ? form.getRegTimeEnd() : null;

		String instCodeSrch = StringUtils.isNotEmpty(form.getInstCodeSrch()) ? form.getInstCodeSrch() : null;

		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getCombotreeSrch())) {
			if (form.getCombotreeSrch().contains(StringPool.COMMA)) {
				list = form.getCombotreeSrch().split(StringPool.COMMA);
				form.setCombotreeListSrch(list);
			} else {
				list = new String[] { form.getCombotreeSrch() };
				form.setCombotreeListSrch(list);
			}
		}
		String[] combotreeListSrchStr = form.getCombotreeListSrch();
		if (form.getRegTimeStart() != null) {
			userMap.put("regTimeStart", regTimeStart);
		}
		if (form.getRegTimeEnd() != null) {
			userMap.put("regTimeEnd", regTimeEnd);
		}
		userMap.put("regionName", regionName);
		userMap.put("branchName", branchName);
		userMap.put("departmentName", departmentName);
		userMap.put("userName", userName);
		userMap.put("realName", realName);
		userMap.put("mobile", mobile);
		userMap.put("recommendName", recommendName);
		userMap.put("userRole", userRole);
		userMap.put("userProperty", userProperty);
		userMap.put("accountStatus", accountStatusStr);
		userMap.put("userStatus", userStatusStr);
		userMap.put("registPlat", registPlatStr);
		userMap.put("is51", is51Str);
		userMap.put("vipType", vipType);

		userMap.put("instCodeSrch", instCodeSrch);

		userMap.put("combotreeListSrch", combotreeListSrchStr);
		List<AdminUserListCustomize> recordList = this.usersService.getRecordList(userMap, -1, -1);
		String[] titles = new String[] { "序号", "用户来源", "用户名", "姓名", "性别", "年龄", "生日","身份证号", "户籍所在地", "手机号码", "会员类型", "用户角色", "用户属性", "推荐人", "51老用户", "用户状态","银行开户状态","银行开户时间","汇付开户状态", "注册平台", "注册时间", "注册所在地" };
//		String[] titles = new String[] { "序号", "分公司", "分部", "团队", "用户来源", "用户名", "姓名", "性别", "年龄", "生日","身份证号", "户籍所在地", "手机号码", "会员类型", "用户角色", "用户属性", "推荐人", "51老用户", "用户状态","银行开户状态","银行开户时间","汇付开户状态", "注册平台", "注册时间", "注册所在地" };
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, (sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					AdminUserListCustomize user = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else
//					if (celLength == 1) { // 大区
//						cell.setCellValue(user.getRegionName());
//					} else if (celLength == 2) { // 分公司
//						cell.setCellValue(user.getBranchName());
//					} else if (celLength == 3) { // 团队
//						cell.setCellValue(user.getDepartmentName());
//					} else
					if (celLength == 1) { // 用户来源
						cell.setCellValue(user.getInstName());
					} else if (celLength == 2) {// 用户名
						cell.setCellValue(user.getUserName());
					} else if (celLength == 3) {// 姓名
						cell.setCellValue(user.getRealName());
					} else if (celLength == 4) {// 性别
						if ("1".equals(user.getSex())) {
							cell.setCellValue("男");
						} else if ("2".equals(user.getSex())) {
							cell.setCellValue("女");
						} else {
							cell.setCellValue("未知");
						}
					} else if (celLength == 5) {// 年龄
						cell.setCellValue(this.getAge(user.getBirthday()));
					} else if (celLength == 6) {// 生日
						cell.setCellValue(user.getBirthday());
					} else if (celLength == 7) {// 身份证号
						cell.setCellValue(AsteriskProcessUtil.getAsteriskedValue(user.getIdcard(), ManageUsersDefine.PERMISSION_HIDE_SHOW));
					} else if (celLength == 8) {// 户籍所在地
						cell.setCellValue(usersService.getAreaByIdCard(user.getIdcard()));
					} else if (celLength == 9) {// 手机号码
						cell.setCellValue(AsteriskProcessUtil.getAsteriskedValue(user.getMobile(), ManageUsersDefine.PERMISSION_HIDE_SHOW));
					} else if (celLength == 10) {// 会员类型
						cell.setCellValue(user.getVipType());
					} else if (celLength == 11) {// 用户角色
						cell.setCellValue(user.getUserRole());
					} else if (celLength == 12) {// 用户属性
						cell.setCellValue(user.getUserProperty());
					} else if (celLength == 13) {// 推荐人
						cell.setCellValue(user.getRecommendName());
					} else if (celLength == 14) {
						// 51老用户
						cell.setCellValue(user.getIs51());
					} else if (celLength == 15) {
						// 用户状态
						cell.setCellValue(user.getUserStatus());
					} else if (celLength == 16) {
						// 银行开户状态
						cell.setCellValue("1".equals(user.getBankOpenAccount())?"已开户":"未开户");
					} else if (celLength == 17) {
						// 银行开户时间
						cell.setCellValue(user.getBankOpenTime());
					} else if (celLength == 18) {
						// 开户状态
						cell.setCellValue("1".equals(user.getOpenAccount())?"已开户":"未开户");
					} else if (celLength == 19) {
						// 注册平台
						cell.setCellValue(user.getRegistPlat());
					} else if (celLength == 20) {
						// 注册时间
						cell.setCellValue(user.getRegTime());
					} else if (celLength == 21) {
						// 注册所在地
						cell.setCellValue(user.getRegistArea());
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.EXPORT_USERS_ACTION);
	}
	/**
	 * 画面校验
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheckUpdate(ModelAndView modelAndView, AdminUserUpdateCustomize form) {
		// 手机号码(必须,数字,最大长度)
		boolean mobileFlag = ValidatorFieldCheckUtil.validateNumJustLength(modelAndView, "mobile", form.getMobile(), 11, true);
		if (mobileFlag) {
			int cnt = usersService.countUserByMobile(GetterUtil.getInteger(form.getUserId()), form.getMobile());
			if (cnt > 0) {
				// 用户手机号码
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "mobile", "repeat");
			}
		}
		// 用户角色
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "userRole", form.getUserRole());
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 60, true);
		// 用户状态
		ValidatorFieldCheckUtil.validateRequired(modelAndView, "status", form.getStatus());
	}

	/**
	 * 画面校验
	 *
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheckRe(ModelAndView modelAndView, AdminUserRecommendCustomize form) {
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRecommendName())) {
			boolean reFlag = ValidatorFieldCheckUtil.validateRequired(modelAndView, "recommendName", form.getRecommendName());
			// 检查用户名唯一性
			if (reFlag) {
				int recomFlag = this.usersService.checkRecommend(form.getUserId(), form.getRecommendName());
				if (recomFlag == 2) {
					// 推荐人
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "recommendName", "recommend.same");
				} else if (recomFlag == 1) {
					// 推荐人
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "recommendName", "null");
				}
			}
		}
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 60, true);
	}

	/**
	 * 根据修改履历修改推荐人
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(value = ManageUsersDefine.UPDATE_INVITE_INFO_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_UPDATEINVITE)
	public ModelAndView updateTenderUserReferrerInit(AdminUserRecommendCustomize form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_INVITE_INFO_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.UPDATE_INVITE_PATH);

		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_INVITE_INFO_ACTION);
		return modelAndView;
	}

	/**
	 * 根据修改履历修改推荐人
	 *
	 * @param request
	 * @return 进入用户详情页面
	 */
	@RequestMapping(value = ManageUsersDefine.UPDATE_INVITE_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_UPDATEINVITE)
	public ModelAndView updateTenderUserReferrer(AdminUserRecommendCustomize form, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_INVITE_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.UPDATE_INVITE_PATH);

		AdminUserRecommendCustomize userRecommendCustomize = new AdminUserRecommendCustomize();
		// 2015-08-19 16:38:01 1439973481
		userRecommendCustomize.setStartTime(form.getStartTime());
		// 2016/01/18 12:00:00 1453089600
		userRecommendCustomize.setEndTime(form.getEndTime());

		this.usersService.querySpreadsUsersLog(userRecommendCustomize);

		modelAndView.addObject(ManageUsersDefine.SUCCESS, ManageUsersDefine.SUCCESS);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_INVITE_ACTION);
		return modelAndView;
	}

	/**
	 * 取得部门信息
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping("getCrmDepartmentList")
	@ResponseBody
	public String getCrmDepartmentListAction(@RequestBody UserListCustomizeBean form) {
		// 部门
		String[] list = new String[] {};
		if (Validator.isNotNull(form.getIds())) {
			if (form.getIds().contains(StringPool.COMMA)) {
				list = form.getIds().split(StringPool.COMMA);
			} else {
				list = new String[] { form.getIds() };
			}
		}

		JSONArray ja = this.usersService.getCrmDepartmentList(list);
		if (ja != null) {
			return ja.toString();
		}

		return StringUtils.EMPTY;
	}

	/**
	 * 
	 * @Description:获取年龄
	 * @param birthday
	 * @return String
	 * @exception:
	 * @author: xulijie
	 * @time:2017年5月3日 下午4:00:48
	 */
	public String getAge(String birthday) {
		if (StringUtils.isBlank(birthday)) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String formatDate = sdf.format(date);
		int age = Integer.parseInt(formatDate) - Integer.parseInt(birthday.substring(0, 4));
		return String.valueOf(age);
	}
	
	/**
	 * 企业用户信息补录 add by cwyang 2017-7-13
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ManageUsersDefine.INSERT_COMPANY_ACTION, method = RequestMethod.POST)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_INSERT_CONPANYINFO)
	public ModelAndView insertCompanyInfo(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INSERT_COMPANY_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.INSERT_COMPANY_PATH);
		String userId = request.getParameter("userId");
		if (StringUtils.isNotBlank(userId)) {
			Users user = usersService.getUsersByUserId(Integer.parseInt(userId));
			AdminUserBankOpenAccountCustomize bankOpenAccount = usersService.selectBankOpenAccountById(Integer.parseInt(userId));
			CompanyInfoBean companyInfo = usersService.selectCompanyInfoByUserId(Integer.parseInt(userId));
			modelAndView.addObject("bankOpenAccount", bankOpenAccount);
			modelAndView.addObject("user", user);
			if (companyInfo != null) {
				modelAndView.addObject("companyInfo",companyInfo);
			}
		}else{
			System.out.println("==========用户userId is null!");
		}
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.INSERT_COMPANY_ACTION);
		return modelAndView;
	}


	/**
	 * 同步用户角色
	 */
	@ResponseBody
	@RequestMapping(value="/syncRoleAction",method = RequestMethod.POST)
	@RequiresPermissions(ManageUsersDefine.PERMISSIONS_MODIFYRE)
	public JSONObject syncRoleAction(HttpServletRequest request, HttpServletResponse response){
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, "syncRoleAction");
		String userId=request.getParameter("userId");
		if (StringUtils.isEmpty(userId)){
			JSONObject res = new JSONObject();
			res.put("code",BankCallStatusConstant.STATUS_FAIL);
			res.put("msg","获取用户userId失败!");
			return res;
		}
		JSONObject result=usersService.updateUserRoleId(Integer.parseInt(userId));
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, "syncRoleAction");
		return result;
	}



	/**
	 * 修改手机号 add by cwyang 2017-816
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "modifyMoileAction", method = RequestMethod.POST)
	public ModelAndView modifyMoileAction(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("manager/users/userlist/updateUserMobile");
		String userId = request.getParameter("userId");
		if (StringUtils.isNotBlank(userId)) {
			Users user = usersService.getUsersByUserId(Integer.parseInt(userId));
			String mobile = user.getMobile();
			modelAndView.addObject("mobile",mobile);
			modelAndView.addObject("userId", userId);
		}else{
			System.out.println("==========用户userId is null!");
		}
		return modelAndView;
	}

    @Autowired
    private TransPasswordService transPasswordService;

	/**
     * 修改手机号保存 add by cwyang 2017-816
     * @param form
     * @return
     */
	@ResponseBody
    @RequestMapping(value = "savemodifyUser", method = RequestMethod.POST)
    public String savemodifyUser(@RequestBody CompanyInfoBean form) {
        String userId =form.getUserId();
        String mobile = form.getMobile();


        JSONObject result = new JSONObject();
        TransPasswordAjaxBean ret = new TransPasswordAjaxBean();
        if (StringUtils.isNotBlank(userId)) {
            Users user = usersService.getUsersByUserId(Integer.parseInt(userId));
            BankCallBean bean2 = this.transPasswordService.sendSms(user.getUserId(),
                    BankCallMethodConstant.TXCODE_MOBILE_MODIFY_PLUS, mobile, BankCallConstant.CHANNEL_PC);
            if (bean2 == null) {
                result.put("status", "error");
                result.put("result", "银行接口异常!");
                return ret.toString();

            }
            // 返回失败
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean2.getRetCode())) {
                if(!"JX900651".equals(bean2.getRetCode())){
                    result.put("status", "error");
                    result.put("result", "短信验证码失败!");
                    return ret.toString();
                }

            }
            // 成功返回业务授权码
            String srvAuthCode = bean2.getSrvAuthCode();
            BankOpenAccount bankOpenAccount = usersService.getBankOpenAccount(Integer.valueOf(userId));
            // 调用电子账号手机号修改增强
            BankCallBean bean = new BankCallBean();
            bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bean.setTxCode(BankCallConstant.TXCODE_MOBILE_MODIFY_PLUS);// 消息类型
            // 修改手机号增强
            // mobileModifyPlus
            bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
            bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
            bean.setTxDate(GetOrderIdUtils.getTxDate());
            bean.setTxTime(GetOrderIdUtils.getTxTime());
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
            bean.setChannel(BankCallConstant.CHANNEL_PC);
            bean.setAccountId(String.valueOf(bankOpenAccount.getAccount()));// 电子账号
            bean.setOption(BankCallConstant.OPTION_1);// 修改
            bean.setMobile(mobile);// 新手机号
            bean.setLastSrvAuthCode(srvAuthCode);// 业务授权码
            bean.setSmsCode("111111");// 短信验证码
            // 商户私有域，存放开户平台,用户userId
            LogAcqResBean acqRes = new LogAcqResBean();
            acqRes.setUserId(Integer.valueOf(userId));
            bean.setLogAcqResBean(acqRes);
            // 操作者ID
            bean.setLogUserId(String.valueOf(userId));
            bean.setLogOrderId(GetOrderIdUtils.getUsrId(Integer.valueOf(userId)));
            // 返回参数
            BankCallBean retBean = null;
            try {
                // 调用接口
                retBean = BankCallUtils.callApiBg(bean);
            } catch (Exception e) {
                result.put("status", "error");
                result.put("result", "短信验证码失败!");
                return ret.toString();
            }
            if (retBean == null) {
                result.put("status", "error");
                result.put("result", "短信验证码失败!");
                return ret.toString();
            }
            // 返回失败
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
                result.put("status", "error");
                result.put("result", "短信验证码失败!");
                return ret.toString();
            }

            result.put("status", "success");
            result.put("result", "成功!");

        }else{
            System.out.println("==========用户userId is null!");
        }
        return result.toString();
    }

	/**
	 * 查询企业开户信息
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("serchCompanyInfo")
	public String serchCompanyInfo(@RequestBody UserListCustomizeBean form) {
		JSONObject ret = new JSONObject();
		String accountId = form.getAccountId();
		String userId = form.getUserId();
		if (StringUtils.isBlank(userId)) {
			ret.put("status", "error");
			ret.put("result", "请先选择用户再进行操作!");
			return ret.toString();
		}
		if (StringUtils.isBlank(accountId)) {
			ret.put("status", "error");
			ret.put("result", "请输入正确的电子账号!");
			return ret.toString();
		}
		//根据accountid调用接口查找企业信息
		CompanyInfoBean info = usersService.queryCompanyInfoByAccoutnId(accountId,ret,userId);
		if (info!=null) {
			ret.put("company", info);
		}
		//查询用户是否开户
		Users user = usersService.getUsersByUserId(Integer.parseInt(userId));
		Integer bankFlag = user.getBankOpenAccount();
		ret.put("isOpenAccount", bankFlag);
		return ret.toString();
	}
	
	/**
	 * 保存企业开户信息
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("saveCompanyInfo")
	public String saveCompanyInfo(@RequestBody CompanyInfoBean form) {
		JSONObject ret = new JSONObject();
		String accountId = form.getAccountId();
		String userId = form.getUserId();
		if (StringUtils.isBlank(userId)) {
			ret.put("status", "error");
			ret.put("result", "请先选择用户再进行操作!");
			return ret.toString();
		}
		if (StringUtils.isBlank(accountId)) {
			ret.put("status", "error");
			ret.put("result", "请输入正确的电子账号!");
			return ret.toString();
		}
		//存入企业开户信息
		usersService.updateCompanyInfo(form,ret);
		return ret.toString();
	}
	/**
	 * 保存用户基本信息
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateUserBaseInfo")
	public ModelAndView updateMobile(@ModelAttribute AdminUserInfosUpdCustomize form) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS,"updateUserBaseInfo");
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.UPDATE_USERINFOS_PATH);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
			// 用户角色
			List<ParamName> userRoles = this.usersService.getParamNameList("USER_ROLE");
			String updType =form.getUpdFlg();
			modelAndView.addObject("updType", updType);
			modelAndView.addObject("userRoles", userRoles);
			modelAndView.addObject(ManageUsersDefine.USERS_UPDATE_FORM, form);
			LogUtil.errorLog(ManageUsersDefine.THIS_CLASS, "updateUserBaseInfo", "输入内容验证失败", null);
			return modelAndView;
		}
		if(form.getUpdFlg().equals("bankCard")){
			//修改银行卡
			usersService.updateUserBankInfo(form);
		}else{
			//修改用户基本信息(电话,邮箱,用户角色)
			usersService.updateUserInfos(form);
		}
		modelAndView.addObject(ManageUsersDefine.SUCCESS, ManageUsersDefine.SUCCESS);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, "updateUserBaseInfo");
		return modelAndView;
	}
	/**
	 * 验证手机号
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("isPhone")
	public String isPhone(@ModelAttribute AdminUserUpdateCustomize form,HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String param = request.getParameter("param");
		String userId = request.getParameter("id");
		int cnt = usersService.countUserByMobile(GetterUtil.getInteger(userId), param);
		if (cnt > 0) {
			String message = ValidatorFieldCheckUtil.getErrorMessage("repeat", "");
			message = message.replace("{label}", "手机号");
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		if(param.length()!=11){
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "手机号应为11位数");
			return ret.toString();
		}
		if(Validator.isMobile(param)){
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, ManageUsersDefine.JSON_VALID_STATUS_OK);
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "手机号验证成功!");
		}else{
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "请填入正确的手机号");
		}
		return ret.toString();
		/*
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(param);
		boolean isMatch = m.matches();
		if (!isMatch) {
			ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "请填入正确的手机号");
			return ret.toString();
		}
		if (!ret.containsKey(ManageUsersDefine.JSON_VALID_INFO_KEY)) {
			ret.put(ManageUsersDefine.JSON_VALID_STATUS_KEY, ManageUsersDefine.JSON_VALID_STATUS_OK);
		}		ret.put(ManageUsersDefine.JSON_VALID_INFO_KEY, "手机号验证成功!");
		return ret.toString();*/
	}

	/**
	 * 修改用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ManageUsersDefine.UPDATE_USERINFOS_ACTION)
	@RequiresPermissions(value = {ManageUsersDefine.PERMISSIONS_MODIFYPHONE,
			ManageUsersDefine.PERMISSIONS_MODIFYEMAIL,
			ManageUsersDefine.PERMISSIONS_MODIFYUSERROLE,
			ManageUsersDefine.PERMISSIONS_MODIFYBANKCARD},logical= Logical.OR)
	public ModelAndView initUpdateUserInfos(HttpServletRequest request) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_USERINFOS_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManageUsersDefine.UPDATE_USERINFOS_PATH);
		// 获取用户id
		String userIdStr = request.getParameter("userId");
		int userId = Integer.parseInt(userIdStr);
		// 根据用户id查询用户详情信息
		AdminUserUpdateCustomize user = usersService.searchUserUpdateById(userId);
		modelAndView.addObject("userInfos", user);

		//用户开户信息
		BankCard bankCard = usersService.selectBankCardByUserId(userId);
		modelAndView.addObject("bankInfo",bankCard);
		// 用户角色
		List<ParamName> userRoles = this.usersService.getParamNameList("USER_ROLE");
		//查找
		// 加载修改日志
		ChangeLogBean logBean = new ChangeLogBean();
		logBean.setUserId(userId);
		Integer changeType = null;
		String updType = request.getParameter("updType");
		switch(updType){
			case "mobile":
				changeType = ChangeLogDefine.CHANGELOG_TYPE_MOBILE;
				break;
			case "email":
				changeType = ChangeLogDefine.CHANGELOG_TYPE_EMAIL;
				break;
			case "userRole":
				changeType = ChangeLogDefine.CHANGELOG_TYPE_USERROLE;
				break;
			case "bankCard":
				changeType = ChangeLogDefine.CHANGELOG_TYPE_CARDCHANGE;
				break;
		}
		modelAndView.addObject("updType", updType);
		String remak = null;
		logBean.setChangeType(changeType);
		List<ChangeLogCustomize> recordList = this.changeLogService.getUserRecordList(logBean, -1, -1);
		if(null!=recordList&&recordList.size()>0){
			ChangeLogCustomize changeLogCustomize = recordList.get(0);
			remak = changeLogCustomize.getRemark();
		}
		modelAndView.addObject(ManageUsersDefine.USERS_CHANGELOG_FORM, recordList);
		modelAndView.addObject("remak", remak);
		modelAndView.addObject("userRoles", userRoles);
		LogUtil.endLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.UPDATE_USERINFOS_ACTION);
		return modelAndView;
	}
	/**
	 * 查找联行号
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("searchPayAllianceCode")
	public String searchPayAllianceCode(@RequestBody AdminUserInfosUpdCustomize form,HttpServletRequest request) {
		LogUtil.startLog(ManageUsersDefine.THIS_CLASS, "searchPayAllianceCode");
		JSONObject ret = new JSONObject();
		ret = usersService.searchPayAllianceCode(form);
		return ret.toString();
	}

}
