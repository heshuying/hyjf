package com.hyjf.api.callcenter.account.huifu;

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
import com.hyjf.callcenter.account.huifu.AccountHuifuBean;
import com.hyjf.callcenter.account.huifu.AccountHuifuDefine;
import com.hyjf.callcenter.account.huifu.AccountHuifuService;
import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterAccountHuifuCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询汇付绑卡关系Controller
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = AccountHuifuDefine.REQUEST_MAPPING)
public class AccountHuifuServer extends CallcenterBaseController {
	
	@Autowired
	private AccountHuifuService accountHuifuService;
	
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AccountHuifuDefine.INIT_TIED_CARD_ACTION, method = RequestMethod.POST)
	public ResultListBean getContentOfAccountHuifu(HttpServletRequest request, HttpServletResponse response,
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
		//根据用户信息查询汇付绑卡关系
		List<CallcenterAccountHuifuCustomize> recordList = this.accountHuifuService.getRecordList(
				user,bean.getLimitStart(),bean.getLimitSize());
		if (recordList == null) {
			result.statusMessage(ResultListBean.STATUS_FAIL,"该用户在汇付未绑卡！");
			return result;
		}

		//编辑返回信息
		for (CallcenterAccountHuifuCustomize recordBean : recordList) {
			AccountHuifuBean returnBean = new AccountHuifuBean();
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