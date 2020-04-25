package com.hyjf.admin.exception.openaccountexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.manager.user.manageruser.ManageUsersDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.admin.AdminOpenAccountExceptionCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package com.hyjf.admin.maintenance.Admin
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = OpenAccountDefine.REQUEST_MAPPING)
public class OpenAccountController extends BaseController {

	@Autowired
	private OpenAccountService openAccountService;

	/**
	 * 账户设置画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(OpenAccountDefine.INIT_ACTION)
	@RequiresPermissions(OpenAccountDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute OpenAccountBean form) {
		LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.INIT_ACTION);
		return modelAndView;
	}

	/**
	 * 创建账户设置分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, OpenAccountBean form) {

		// 开户状态
		List<ParamName> accountStatus = this.openAccountService.getParamNameList("ACCOUNT_STATUS");
		modelAndView.addObject("accountStatus", accountStatus);

		Map<String, Object> accountUser = new HashMap<String, Object>();
		accountUser.put("userName", form.getUserName());
		accountUser.put("recommendName", form.getRecommendName());
		accountUser.put("regTimeStart", form.getRegTimeStart());
		accountUser.put("regTimeEnd", form.getRegTimeEnd());
		int recordTotal = this.openAccountService.countRecordTotal(accountUser);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<AdminOpenAccountExceptionCustomize> recordList = this.openAccountService.getRecordList(accountUser,
					paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(OpenAccountDefine.ACCOUNT_LIST_FORM, form);
		}

	}

	/**
	 * 初始化用户开户画面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(OpenAccountDefine.INIT_UPDATE_ACCOUNT_ACTION)
	@RequiresPermissions(OpenAccountDefine.PERMISSION_CONFIRM_ACCOUNT)
	public ModelAndView initOpenAccount(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.INIT_UPDATE_ACCOUNT_ACTION);
		ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.UPDATE_ACCOUNT_PATH);
		String userId = request.getParameter("userId");
		Users user = this.openAccountService.getUserByUserId(userId);
		modelAndView.addObject(OpenAccountDefine.UPDATE_ACCOUNT_FORM, user);
		LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.INIT_UPDATE_ACCOUNT_ACTION);
		return modelAndView;
	}

	/**
	 * 初始化用户开户画面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(OpenAccountDefine.UPDATE_ACCOUNT_LOG_ACTION)
	@RequiresPermissions(OpenAccountDefine.PERMISSION_CONFIRM_ACCOUNT)
	public Map<String, Object> updateAccountLog(HttpServletRequest request, HttpServletResponse response) {
		
		LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_LOG_ACTION);
		ModelAndView modelAndView = new ModelAndView();
		String userId = request.getParameter("userId");
		// 画面验证
		if (Validator.isNull(userId) || !Validator.isNumber(userId)) {
			LogUtil.errorLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.MODIFY_RE_ACTION, "输入内容验证失败", null);
			modelAndView.addObject("success", "1");
			modelAndView.addObject("msg", "用户ID不正确");
			return modelAndView.getModel();
		}else{
			List<ChinapnrLog> openAccountLogs=this.openAccountService.getOpenAccountLog(userId);
			if(openAccountLogs!=null&&openAccountLogs.size()>0){
				String msgData= openAccountLogs.get(0).getMsgdata();
				JSONObject data=JSONObject.parseObject(msgData);
				String userName= data.getString("UsrName");//用户真实姓名
				String usrCustId = data.getString("UsrCustId");//客户号
				String usrId = data.getString("UsrId");//帐号
				String idCard = data.getString("IdNo");//身份证号码
				String mobile= data.getString("UsrMp");//用户手机号码
				// 是否包含平台的user_id
				int index = usrId.indexOf(userId);
				if (index + userId.length() == usrId.length()) {
					ChinapnrBean chinaPnr = new ChinapnrBean();
					chinaPnr.setLogUserId(Integer.parseInt(userId));
					String ip = GetCilentIP.getIpAddr(request);
					chinaPnr.setLogIp(ip);
					chinaPnr.setCertId(idCard);
					chinaPnr.setUsrName(userName);
					chinaPnr.setUsrMp(mobile);
					chinaPnr.setUsrId(usrId);
					chinaPnr.setUsrCustId(usrCustId);
					this.openAccountService.insertOpenAccountRecord(chinaPnr);
					LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_LOG_ACTION);
					modelAndView.addObject("success", 0);
					return modelAndView.getModel();
				}else{
					throw new RuntimeException("更新失败");
				}
			}else{
				LogUtil.errorLog(ManageUsersDefine.THIS_CLASS, ManageUsersDefine.MODIFY_RE_ACTION, "输入内容验证失败", null);
				modelAndView.addObject("success", "2");
				return modelAndView.getModel();
			}
		}
		
	}

	/**
	 * 用户开户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(OpenAccountDefine.UPDATE_ACCOUNT_ACTION)
	@RequiresPermissions(OpenAccountDefine.PERMISSION_CONFIRM_ACCOUNT)
	public ModelAndView openAccount(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION);
		ModelAndView modelAndView = new ModelAndView(OpenAccountDefine.UPDATE_ACCOUNT_PATH);
		String userId = request.getParameter("userId");
		String selecttype = request.getParameter("selecttype");
		String selectno = request.getParameter("selectno");
		// 画面验证
		BankCallBean retBean  =  null;
		if(selecttype.equals("2")){
			// 身份证号码格式以及长度的校验
//			ValidatorFieldCheckUtil.validateIdCard(modelAndView, "idCard", selectno, 18, 18, true);
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				Users user = this.openAccountService.getUserByUserId(userId);
				modelAndView.addObject(OpenAccountDefine.UPDATE_ACCOUNT_FORM, user);
				modelAndView.addObject("selectno", selectno);
				LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION, "输入内容验证失败", null);
				return modelAndView;
			}
			
			// IP地址
			String ip = CustomUtil.getIpAddr(request);
			BankCallBean bean = new BankCallBean();
			bean.setVersion(ChinaPnrConstant.VERSION_10);
			bean.setTxCode(BankCallMethodConstant.TXCODE_ACCOUNTID_QUERY);
			bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
			bean.setTxDate(GetOrderIdUtils.getTxDate());
			bean.setTxTime(GetOrderIdUtils.getTxTime());
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
			bean.setIdType("01");
			bean.setIdNo(selectno);
			bean.setLogIp(ip); // IP地址
			// 调用银行接口
			retBean = BankCallUtils.callApiBg(bean);

        }else{
			// 手机号码格式以及长度的校验
			ValidatorFieldCheckUtil.validateMobile(modelAndView, "mobile", selectno, true);
			if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
				Users user = this.openAccountService.getUserByUserId(userId);
				modelAndView.addObject(OpenAccountDefine.UPDATE_ACCOUNT_FORM, user);
				modelAndView.addObject("selectno", selectno);
				LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION, "输入内容验证失败", null);
				return modelAndView;
			}
			
			// IP地址
			String ip = CustomUtil.getIpAddr(request);
			BankCallBean bean = new BankCallBean();
			bean.setVersion(ChinaPnrConstant.VERSION_10);
			bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
			bean.setTxDate(GetOrderIdUtils.getTxDate());
			bean.setTxTime(GetOrderIdUtils.getTxTime());
			bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
			bean.setTxCode(BankCallMethodConstant.TXCODE_ACCOUNT_QUERY_BY_MOBILE);
			bean.setMobile(selectno);
			bean.setLogIp(ip); // IP地址
			// 调用银行接口
			retBean = BankCallUtils.callApiBg(bean);
		}

		if (retBean == null) {
			LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION,
					new Exception("调用银行接口发生错误"));
			// 未查询到相应的身份证的开户信息
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "idCard", "idCard.null");
			Users user = this.openAccountService.getUserByUserId(userId);
			modelAndView.addObject(OpenAccountDefine.UPDATE_ACCOUNT_FORM, user);
            modelAndView.addObject("errormsg", "调用银行接口发生错误");
			modelAndView.addObject("selectno", selectno);
			return modelAndView;
		}
		int cnt = 0;
		// 接口返回正常时,执行更新操作
		if (BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())) {
			try {
                if(selecttype.equals("2")) {
                    UsersInfo user = openAccountService.getUsersInfoByUserId(Integer.valueOf(userId));
                    if (!selectno.equals(user.getIdcard())) {
                        modelAndView.addObject("errormsg", "身份证信息有误!");
                        return modelAndView;
                    }
                } else {
                    Users user = openAccountService.getUserByUserId(userId);
                    if (!selectno.equals(user.getMobile())) {
                        modelAndView.addObject("errormsg", "输入号码有误!");
                        return modelAndView;
                    }
                }

				if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
					Users user = this.openAccountService.getUserByUserId(userId);
					modelAndView.addObject(OpenAccountDefine.UPDATE_ACCOUNT_FORM, user);
                    modelAndView.addObject("errormsg", "更新用户开户信息失败");
					modelAndView.addObject("selectno", selectno);
					return modelAndView;
				}


			} catch (Exception e) {
				LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION, e);
			}
		} else {
			LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION,
					new Exception("调用银行接口发生错误"));
			// 未查询到相应的身份证的开户信息
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "idCard", "idCard.search.fail");
			Users user = this.openAccountService.getUserByUserId(userId);
			modelAndView.addObject(OpenAccountDefine.UPDATE_ACCOUNT_FORM, user);
            modelAndView.addObject("errormsg", "更新用户开户信息失败");
			modelAndView.addObject("selectno", selectno);
			return modelAndView;
		}

        ChinapnrBean chinapnrBean = new ChinapnrBean();
		chinapnrBean.setUsrId(userId);
		chinapnrBean.setLogUserId(Integer.valueOf(userId));
		chinapnrBean.setCertId(retBean.getIdNo());
		chinapnrBean.setUsrName(retBean.getName());
		chinapnrBean.setUsrMp(retBean.getMobile());
		// 更新
        cnt = openAccountService.insertOpenAccountRecord(chinapnrBean, retBean.getAccountId());

        // 更新用户开户信息失败
		if (cnt < 0) {
			LogUtil.errorLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION,
					new Exception("更新用户开户信息失败"));
			// 更新相应用户的开户信息失败
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "idCard", "idCard.fail");
			Users user = this.openAccountService.getUserByUserId(userId);
			modelAndView.addObject(OpenAccountDefine.UPDATE_ACCOUNT_FORM, user);
			modelAndView.addObject("errormsg", "更新用户开户信息失败");
			modelAndView.addObject("selectno", selectno);
			return modelAndView;
		}
		modelAndView.addObject("success", "true");
		LogUtil.endLog(OpenAccountDefine.THIS_CLASS, OpenAccountDefine.UPDATE_ACCOUNT_ACTION);
		return modelAndView;
	}

}
