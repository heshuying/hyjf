package com.hyjf.api.callcenter.userinfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.callcenter.base.CallcenterBaseController;
import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.JsonBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.callcenter.userinfo.UserInfoBean;
import com.hyjf.callcenter.userinfo.UserInfoDefine;
import com.hyjf.callcenter.userinfo.UserInfoService;
import com.hyjf.mybatis.model.auto.CallcenterServiceUsers;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserBaseCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterUserDetailCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询会员资料Controller
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = UserInfoDefine.REQUEST_MAPPING)
public class UserInfoServer extends CallcenterBaseController {
	
	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserInfoDefine.INIT_USER_INFO_ACTION, method = RequestMethod.POST)
	public ResultListBean getUserInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UserBean bean) {
		ResultListBean result = new ResultListBean();

		//根据用户名或手机号取得用户信息
		Users user = this.getUser(bean, result);
		if (user == null) {
			if (result.getStatus()!=BaseResultBean.STATUS_FAIL) {
				result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户不存在！");
			}
			return result;
		}

		//*************各自业务开始***************
		UserInfoBean returnBean = new UserInfoBean();
		//取得用户基本信息
		List<CallcenterUserBaseCustomize> userBaseList = this.userInfoService.getUserBaseList(user);
		if (userBaseList == null || userBaseList.size()<=0) {
			result.statusMessage(BaseResultBean.STATUS_FAIL,"未查询到该用户基本信息！");
			return result;
		}
		
		//取得用户详细信息
		List<CallcenterUserDetailCustomize> userDetailList = this.userInfoService.getUserDetailList(user);
		if (userDetailList == null) {
			result.statusMessage(BaseResultBean.STATUS_FAIL,"未查询到该用户详细信息！");
			return result;
		}
		
		//编辑返回结果
		//用户详细信息
		if (userDetailList.size()==1) {
			BeanUtils.copyProperties(userDetailList.get(0), returnBean);
		}
		
		//用户基本信息
		if (userBaseList.size()==1) {
			//分公司
			returnBean.setRegionName(userBaseList.get(0).getRegionName());
			//分部
			returnBean.setBranchName(userBaseList.get(0).getBranchName());
			//团队
			returnBean.setDepartmentName(userBaseList.get(0).getDepartmentName());
			//汇付开户状态
			returnBean.setOpenAccount(userBaseList.get(0).getOpenAccount());
			//江西银行开户状态
			returnBean.setBankOpenAccount(userBaseList.get(0).getBankOpenAccount());
			//江西银行电子账号
			returnBean.setBankAccount(userBaseList.get(0).getBankAccount());
			//江西银行开户时间
			returnBean.setBankOpenTime(userBaseList.get(0).getBankOpenTime());
		}

		//用户名
		returnBean.setUserName(user.getUsername());
		//手机号
		returnBean.setMobile(user.getMobile());	
		
		result.getDataList().add(returnBean);
		
		result.statusMessage(BaseResultBean.STATUS_SUCCESS, BaseResultBean.STATUS_DESC_SUCCESS);
		return result;
	}
	
	/**
	 * 查询呼叫中心未分配客服的用户名/手机号调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = UserInfoDefine.INIT_NO_SERVIC_USERS_ACTION, method = RequestMethod.POST)
	public ResultListBean getNewUsers(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UserBean bean) {
		ResultListBean result = new ResultListBean();

		//唯一识别号验证
		if (!checkUniqueNo(bean, result)) {
			return result;
		}
		
		//分页验证
		if (!checkLimit(bean, result)) {
			return result;
		}

		//取得分配客服的用户名和手机号
		List<CallcenterUserBaseCustomize> userList = this.userInfoService.getNoServiceUsersList(bean);
		if (userList == null) {
			result.statusMessage(BaseResultBean.STATUS_FAIL,"未查询到该用户基本信息！");
			return result;
		}
		
		//编辑返回结果
		for (CallcenterUserBaseCustomize recordBean : userList) {
			UserInfoBean returnBean = new UserInfoBean();
			//用户名
			returnBean.setUserName(recordBean.getUserName());
			//手机号
			returnBean.setMobile(recordBean.getMobile());		
			
			result.getDataList().add(returnBean);
		}
		
		result.statusMessage(BaseResultBean.STATUS_SUCCESS, BaseResultBean.STATUS_DESC_SUCCESS);
		return result;
	}
	
	/**
	 * 更新呼叫中心用户分配客服的状态调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = UserInfoDefine.INIT_SERVED_USERS_ACTION, method = RequestMethod.POST)
	public BaseResultBean setServedUsers(HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonBean bean) {
		BaseResultBean result = new BaseResultBean();
		
		//参数非空判断
		if (bean == null || bean.getUserJsonArray() == null) {
			result.statusMessage(BaseResultBean.STATUS_FAIL,"传入参数为空！");
			return result;
		}
		
		//唯一识别号验证
		if (!this.checkUniqueNo(bean, result)) {
			return result;
		}
		
		//解析json到list
		List<CallcenterServiceUsers> userList = bean.getUserJsonArray();
//		try {
//			userList = JSONObject.parseArray(bean.getUserJsonArray(),CallcenterServiceUsers.class);
//		} catch (Exception e) {
//			result.statusMessage(BaseResultBean.STATUS_FAIL, "传入的用户信息不是正确Json格式！");
//			return result;
//		}

		//更新呼叫中心用户分配客服的状态
		Integer rowCount = this.userInfoService.executeRecord(userList);
		if (rowCount == null) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, "用户分配结果操作失败！");
			return result;
		}
		result.statusMessage(BaseResultBean.STATUS_SUCCESS, BaseResultBean.STATUS_DESC_SUCCESS+"操作记录数："+rowCount);
		return result;
	}
}