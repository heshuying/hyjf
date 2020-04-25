package com.hyjf.api.callcenter.invest.hzt;

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
import com.hyjf.callcenter.invest.hzt.HztInvestBean;
import com.hyjf.callcenter.invest.hzt.HztInvestDefine;
import com.hyjf.callcenter.invest.hzt.HztInvestService;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterHztInvestCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询出借明细（直投产品）Controller
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = HztInvestDefine.REQUEST_MAPPING)
public class HztInvestServer extends CallcenterBaseController {
	
	@Autowired
	private HztInvestService hztInvestService;
	
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HztInvestDefine.INIT_INVEST_INFO_ACTION, method = RequestMethod.POST)
	public ResultListBean getContentOfHztInvest(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UserBean bean) {
		ResultListBean result = new ResultListBean();

		//根据用户名或手机号取得用户信息
		Users user = this.getUser(bean, result);
		if (user == null) {
			if (result.getStatus()!=BaseResultBean.STATUS_FAIL) {
				result.statusMessage(ResultListBean.STATUS_FAIL,"该用户不存在！");
			}
			return result;
		}

		//*************各自业务开始***************
		//根据用户信息查询江西银行绑卡关系
		List<CallcenterHztInvestCustomize> recordList = this.hztInvestService.getRecordList(
				user,bean.getLimitStart(),bean.getLimitSize());
		if (recordList == null) {
			result.statusMessage(ResultListBean.STATUS_FAIL,"该用户未出借汇直投！");
			return result;
		}

		//编辑返回信息
		for (CallcenterHztInvestCustomize recordBean : recordList) {
			HztInvestBean returnBean = new HztInvestBean();
			//*************各自业务结束***************
			//检索bean→返回bean
			BeanUtils.copyProperties(recordBean, returnBean);
			//用户名
			returnBean.setUserName(user.getUsername());
			//手机号
			returnBean.setMobile(user.getMobile());	
			
			result.getDataList().add(returnBean);
		}
		
		result.statusMessage(ResultListBean.STATUS_SUCCESS, ResultListBean.STATUS_DESC_SUCCESS);
		return result;
	}
}