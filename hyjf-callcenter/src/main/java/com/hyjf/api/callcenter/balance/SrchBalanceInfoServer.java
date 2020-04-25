package com.hyjf.api.callcenter.balance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.callcenter.base.CallcenterBaseController;
import com.hyjf.callcenter.balance.SrchBalanceInfoBean;
import com.hyjf.callcenter.balance.SrchBalanceInfoDefine;
import com.hyjf.callcenter.balance.SrchBalanceInfoService;
import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterAccountManageCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterBankAccountManageCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询账户余额Controller
 * @author LIBIN
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = SrchBalanceInfoDefine.REQUEST_MAPPING)
public class SrchBalanceInfoServer extends CallcenterBaseController {
	
	@Autowired
	private SrchBalanceInfoService srchBalanceInfoService;
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SrchBalanceInfoDefine.GET_CONTENT_INFO_MAPPING, method = RequestMethod.POST)
	public ResultListBean getContentOfBalanceInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBean bean) {
		//初始化結果bean
		ResultListBean result = new ResultListBean();
		//初始化查詢bean
		//银行beans
		CallCenterBankAccountManageCustomize callCenterBankAccountManageCustomize = new CallCenterBankAccountManageCustomize();
		//汇付bean
		CallCenterAccountManageCustomize callCenterAccountManageCustomize = new CallCenterAccountManageCustomize();
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
		if (user.getUsername() != null && !"".equals(user.getUsername().trim())) {
			//1.银行资金查询(一个用户只有一条记录)
			callCenterBankAccountManageCustomize.setUsername(user.getUsername());
			callCenterBankAccountManageCustomize.setUserId(String.valueOf(user.getUserId()));
			//分页开始结束
			callCenterBankAccountManageCustomize.setLimitStart(bean.getLimitStart());
			callCenterBankAccountManageCustomize.setLimitEnd(bean.getLimitSize());
			List<CallCenterBankAccountManageCustomize> bankAccountInfos = this.srchBalanceInfoService.queryBankAccountInfos(callCenterBankAccountManageCustomize);
			//编辑返回信息
			//初始化返回bean
			SrchBalanceInfoBean returnBean = new SrchBalanceInfoBean();
			for (CallCenterBankAccountManageCustomize customize : bankAccountInfos) {
				returnBean.setUsername(user.getUsername());//用户名
				returnBean.setMobile(user.getMobile());//手机号
				
				returnBean.setBankTotal(customize.getBankTotal());//资产总额
				returnBean.setBankAwait(customize.getBankAwait());//待收金额
				returnBean.setBankWaitRepay(customize.getBankWaitRepay());//待还金额
				//江西银行总可用金额
				returnBean.setBankBalance(customize.getBankBalance());//江西银行可用金额
				returnBean.setBankBalanceCash(customize.getBankBalanceCash());
				//江西银行总冻结金额
				returnBean.setBankFrost(customize.getBankFrost());//江西银行冻结金额
				returnBean.setBankFrostCash(customize.getBankFrostCash());
			}
			
			//2.汇付资金查询(一个用户只有一条记录)
			callCenterAccountManageCustomize.setUsername(user.getUsername());
			callCenterAccountManageCustomize.setUserId(String.valueOf(user.getUserId()));
			//分页开始结束
			callCenterAccountManageCustomize.setLimitStart(bean.getLimitStart());
			callCenterAccountManageCustomize.setLimitEnd(bean.getLimitSize());
			
			List<CallCenterAccountManageCustomize> accountInfos = this.srchBalanceInfoService.queryAccountInfos(callCenterAccountManageCustomize);
			for (CallCenterAccountManageCustomize customize : accountInfos) {
				returnBean.setUsername(user.getUsername());//用户名
				returnBean.setMobile(user.getMobile());//手机号
				returnBean.setBalanceTotal(customize.getBalanceTotal());//汇付可用金额
				returnBean.setFrostTotal(customize.getFrostTotal());//汇付冻结金额
				
				//将银行循环的bean再赋值后添加result
				result.getDataList().add(returnBean);
			}
		} else {
			result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户无用户名！");
		}
		result.statusMessage(BaseResultBean.STATUS_SUCCESS, BaseResultBean.STATUS_DESC_SUCCESS);
		return result;
	}

}













