package com.hyjf.api.callcenter.transfer;

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
import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.transfer.SrchTransferInfoBean;
import com.hyjf.callcenter.transfer.SrchTransferInfoDefine;
import com.hyjf.callcenter.transfer.SrchTransferInfoService;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterBorrowCreditCustomize;

/**
 * 呼叫中心:按照用户名/手机号查询转让信息Controller
 * @author LIBIN
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = SrchTransferInfoDefine.REQUEST_MAPPING)
public class SrchTransferInfoServer extends CallcenterBaseController {
	
	@Autowired
	private SrchTransferInfoService srchTransferInfoService;
	/**
	 * 呼叫中心接口调用入口
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = SrchTransferInfoDefine.GET_CONTENT_INFO_MAPPING, method = RequestMethod.POST)
	public ResultListBean getContentOfTransferInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBean bean) {
		//初始化結果bean
		ResultListBean result = new ResultListBean();
		//初始化查詢bean
		CallCenterBorrowCreditCustomize callCenterBorrowCreditCustomize = new CallCenterBorrowCreditCustomize();
		/*try {*/
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
			if (user.getUsername() != null && !"".equals(user.getUsername().trim())) {
				// 用户名(查詢條件) 判空：当只给手机号时,查詢的記錄里username可能為空，這裡不是查全部，所以必須有username
				callCenterBorrowCreditCustomize.setUsernameSrch(user.getUsername());
				callCenterBorrowCreditCustomize.setUserId(user.getUserId());
				// 分页开始结束
				callCenterBorrowCreditCustomize.setLimitStart(bean.getLimitStart());
				callCenterBorrowCreditCustomize.setLimitEnd(bean.getLimitSize());
				List<CallCenterBorrowCreditCustomize> recordList = this.srchTransferInfoService.selectBorrowCreditList(callCenterBorrowCreditCustomize);
				//根據useranme如果沒有檢到記錄
				if (recordList == null) {
					result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户没有转让信息！");
					return result;
				}
				//编辑返回信息
				for (CallCenterBorrowCreditCustomize customize : recordList) {
					SrchTransferInfoBean srchTransferInfoBean = new SrchTransferInfoBean();
					srchTransferInfoBean.setUsername(user.getUsername());//用户名
					srchTransferInfoBean.setMobile(user.getMobile());//手机号
					srchTransferInfoBean.setCreditNid(customize.getCreditNid());//债转编号
					srchTransferInfoBean.setBidNid(customize.getBidNid());//原始项目编号
					srchTransferInfoBean.setCreditCapital(customize.getCreditCapital());//债权本金
					srchTransferInfoBean.setCreditCapitalPrice(customize.getCreditCapitalPrice());//转让本金
					srchTransferInfoBean.setCreditDiscount(customize.getCreditDiscount());//折让率
					srchTransferInfoBean.setCreditPrice(customize.getCreditPrice());//转让价格
					srchTransferInfoBean.setCreditCapitalAssigned(customize.getCreditCapitalAssigned());//已转让金额
					srchTransferInfoBean.setCreditStatus(customize.getCreditStatus());//转让状态
					srchTransferInfoBean.setClient(customize.getClient());//操作平台
					srchTransferInfoBean.setAddTime(customize.getAddTime());//转让时间
					result.getDataList().add(srchTransferInfoBean);
				}
			} else {
				result.statusMessage(BaseResultBean.STATUS_FAIL,"该用户无用户名！");
			}
			result.statusMessage(BaseResultBean.STATUS_SUCCESS, BaseResultBean.STATUS_DESC_SUCCESS);
			return result;
	}
}


