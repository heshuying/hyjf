package com.hyjf.api.callcenter.capital;

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
import com.hyjf.callcenter.capital.SrchCapitalInfoBean;
import com.hyjf.callcenter.capital.SrchCapitalInfoDefine;
import com.hyjf.callcenter.capital.SrchCapitalInfoService;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterAccountDetailCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询资金明细Controller
 * @author LIBIN
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = SrchCapitalInfoDefine.REQUEST_MAPPING)
public class SrchCapitalInfoServer extends CallcenterBaseController {
	@Autowired
	private SrchCapitalInfoService srchCapitalInfoService;
	
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SrchCapitalInfoDefine.GET_CONTENT_INFO_MAPPING, method = RequestMethod.POST)
	public ResultListBean getContentOfCapitalInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBean bean) {
		//初始化結果bean
		ResultListBean result = new ResultListBean();
		//初始化查詢bean
		CallCenterAccountDetailCustomize callCenterAccountDetailCustomize = new CallCenterAccountDetailCustomize();
//      验签
//		if (!this.checkSign(bean)) {
//			result.statusMessage(result.STATUS_FAIL,"验签失败！");
//			return result;
//		}
		//根据用户名或手机号取得用户信息
		Users user = this.getUser(bean, result);
		if (user == null) {
			if (result.getStatus()!=BaseResultBean.STATUS_FAIL) {
				result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户不存在！");
			}
			return result;
		}
		//*************各自业务开始***************
		if (user.getUsername() != null && !"".equals(user.getUsername().trim())) {
			//查询条件使用userId精确查询
			callCenterAccountDetailCustomize.setUserId(user.getUserId());
			callCenterAccountDetailCustomize.setUsername(user.getUsername());
			// 分页开始结束
			callCenterAccountDetailCustomize.setLimitStart(bean.getLimitStart());
			callCenterAccountDetailCustomize.setLimitEnd(bean.getLimitSize());
			
			List<CallCenterAccountDetailCustomize> accountDetails = this.srchCapitalInfoService.queryAccountDetails(callCenterAccountDetailCustomize);
			if (accountDetails == null) {
				result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户没有转让信息！");
				return result;
			}
			//编辑返回信息
			for (CallCenterAccountDetailCustomize recordBean : accountDetails) {
				SrchCapitalInfoBean returnBean = new SrchCapitalInfoBean();
				//查询bean赋值
				BeanUtils.copyProperties(recordBean, returnBean);
				//用户名
				returnBean.setUsername(user.getUsername());
				//手机号
				returnBean.setMobile(user.getMobile());
				result.getDataList().add(returnBean);
			}
		} else {
			result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户无用户名！");
		}
		result.statusMessage(BaseResultBean.STATUS_SUCCESS, BaseResultBean.STATUS_DESC_SUCCESS);
		return result;	
		
	}
}
