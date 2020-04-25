package com.hyjf.api.callcenter.recharge;

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
import com.hyjf.callcenter.recharge.SrchRechargeInfoBean;
import com.hyjf.callcenter.recharge.SrchRechargeInfoDefine;
import com.hyjf.callcenter.recharge.SrchRechargeInfoService;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterRechargeCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询充值明细Controller
 * @author LIBIN
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = SrchRechargeInfoDefine.REQUEST_MAPPING)
public class SrchRechargeInfoServer extends CallcenterBaseController {
	@Autowired
	private SrchRechargeInfoService srchRechargeInfoService;
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SrchRechargeInfoDefine.GET_CONTENT_INFO_MAPPING, method = RequestMethod.POST)
	public ResultListBean getContentOfRechargeInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBean bean) {
		//初始化結果bean
		ResultListBean result = new ResultListBean();
		//初始化查詢bean
		CallCenterRechargeCustomize callCenterRechargeCustomize = new CallCenterRechargeCustomize();
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
			callCenterRechargeCustomize.setUserId(user.getUserId());
			//callCenterRechargeCustomize.setUsername(user.getUsername());
			// 分页开始结束
			callCenterRechargeCustomize.setLimitStart(bean.getLimitStart());
			callCenterRechargeCustomize.setLimitEnd(bean.getLimitSize());
			List<CallCenterRechargeCustomize> rechargeCustomize = this.srchRechargeInfoService.queryRechargeList(callCenterRechargeCustomize);
			if (rechargeCustomize == null) {
				result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户没有转让信息！");
				return result;
			}
			//编辑返回信息
			for (CallCenterRechargeCustomize recordBean : rechargeCustomize) {
				SrchRechargeInfoBean returnBean = new SrchRechargeInfoBean();
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
