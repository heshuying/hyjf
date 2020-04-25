package com.hyjf.api.callcenter.account.bank;

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
import com.hyjf.callcenter.account.bank.AccountBankBean;
import com.hyjf.callcenter.account.bank.AccountBankDefine;
import com.hyjf.callcenter.account.bank.AccountBankService;
import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.Users;

/**
 * 呼叫中心:按照用户名/手机号查询江西银行绑卡关系Controller
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = AccountBankDefine.REQUEST_MAPPING)
public class AccountBankServer extends CallcenterBaseController {
	
	@Autowired
	private AccountBankService accountBankService;
	
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AccountBankDefine.INIT_TIED_CARD_ACTION, method = RequestMethod.POST)
	public ResultListBean getContentOfAccountBank(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UserBean bean) {
		ResultListBean result = new ResultListBean();

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
		//根据用户信息查询江西银行绑卡关系
		List<BankCard> recordList = accountBankService.getTiedCardOfAccountBank(user);
		if (recordList == null) {
			result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户在江西银行未绑卡！");
			return result;
		}

		//编辑返回信息
		for (BankCard recordBean : recordList) {
			AccountBankBean returnBean = new AccountBankBean();

			//检索bean→返回bean
			BeanUtils.copyProperties(recordBean, returnBean);
			//用户名
			returnBean.setUserName(user.getUsername());
			//手机号
			returnBean.setMobile(user.getMobile());	
			//添加时间
			if (recordBean.getCreateTime()==null) {
				returnBean.setCreateTime(null);
			}
			returnBean.setCreateTime(GetDate.formatDateTime(recordBean.getCreateTime().getTime()));
			
			result.getDataList().add(returnBean);
		}
		//*************各自业务结束***************
		result.statusMessage(BaseResultBean.STATUS_SUCCESS, BaseResultBean.STATUS_DESC_SUCCESS);
		return result;
	}
}