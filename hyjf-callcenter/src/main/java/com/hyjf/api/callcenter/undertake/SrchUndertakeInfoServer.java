package com.hyjf.api.callcenter.undertake;

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
import com.hyjf.callcenter.undertake.SrchUndertakeInfoBean;
import com.hyjf.callcenter.undertake.SrchUndertakeInfoDefine;
import com.hyjf.callcenter.undertake.SrchUndertakeInfoService;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterBorrowCreditCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询承接债权信息Controller
 * @author LIBIN
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = SrchUndertakeInfoDefine.REQUEST_MAPPING)
public class SrchUndertakeInfoServer extends CallcenterBaseController {
	@Autowired
	private SrchUndertakeInfoService srchUndertakeInfoService;
	
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SrchUndertakeInfoDefine.GET_CONTENT_INFO_MAPPING, method = RequestMethod.POST)
	public ResultListBean getContentOfUndertakeInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBean bean) {
		//初始化結果bean
		ResultListBean result = new ResultListBean();
		//初始化查詢bean
		CallCenterBorrowCreditCustomize callCenterBorrowCreditCustomize = new CallCenterBorrowCreditCustomize();
			//验签
//			if (!this.checkSign(bean)) {
//				result.statusMessage(result.STATUS_FAIL,"验签失败！");
//				return result;
//			}
			
			//根据用户名或手机号取得用户信息
			Users user = this.getUser(bean, result);
			if (user == null) {
				if (result.getStatus()!=BaseResultBean.STATUS_FAIL) {
					result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户不存在！");
				}
				return result;
			}
			//*************各自业务开始***************
			//此处是username必须是承接人的username，是用承接人的用户名查承接信息
			if (user.getUsername() != null && !"".equals(user.getUsername().trim())) {
				//查询条件使用“承接人”用户名
				callCenterBorrowCreditCustomize.setUsernameSrch(user.getUsername());
				callCenterBorrowCreditCustomize.setUserId(user.getUserId());
				// 分页开始结束
				callCenterBorrowCreditCustomize.setLimitStart(bean.getLimitStart());
				callCenterBorrowCreditCustomize.setLimitEnd(bean.getLimitSize());
				List<CallCenterBorrowCreditCustomize> recordList = this.srchUndertakeInfoService.selectBorrowCreditTenderList(callCenterBorrowCreditCustomize);
				//根據useranme如果沒有檢到記錄
				if (recordList == null) {
					result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户没有转让信息！");
					return result;
				}
				//编辑返回信息
				for (CallCenterBorrowCreditCustomize recordBean : recordList) {
					SrchUndertakeInfoBean returnBean = new SrchUndertakeInfoBean();
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
