package com.hyjf.api.callcenter.repaymentdetail;

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
import com.hyjf.callcenter.repaymentdetail.RepaymentDetailDefine;
import com.hyjf.callcenter.repaymentdetail.RepaymentDetailHtjBean;
import com.hyjf.callcenter.repaymentdetail.RepaymentDetailHztBean;
import com.hyjf.callcenter.repaymentdetail.RepaymentDetailService;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterHtjRepaymentDetailCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterHztRepaymentDetailCustomize;

/**
 * 呼叫中心:查询还款明细信息Controller
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = RepaymentDetailDefine.REQUEST_MAPPING)
public class RepaymentDetailServer extends CallcenterBaseController {
	
	@Autowired
	private RepaymentDetailService repaymentDetailService;
	
	/**
	 * 按照用户名/手机号查询还款明细（直投产品，含承接的债权）
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepaymentDetailDefine.GET_HZT_REPAYMENT_DETAIL_LIST_ACTION, method = RequestMethod.POST)
	public ResultListBean getHztRepaymentDetailList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UserBean bean) {
		ResultListBean result = new ResultListBean();

		//根据用户名或手机号取得用户信息
		Users user = this.getUser(bean, result);
		if (user == null) {
		    result.setStatus(BaseResultBean.STATUS_FAIL); 
            result.statusMessage(ResultListBean.STATUS_FAIL,"该用户不存在！");
			return result;
		}

		//*************各自业务开始***************
		//根据用户信息查询用户优惠券信息
		List<CallCenterHztRepaymentDetailCustomize> recordList = this.repaymentDetailService.getHztRepaymentDetailList(
				user,bean.getLimitStart(),bean.getLimitSize());

		//编辑返回信息
		for (CallCenterHztRepaymentDetailCustomize recordBean : recordList) {
		    RepaymentDetailHztBean returnBean = new RepaymentDetailHztBean();
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
	
	
	/**
     * 按照用户名/手机号查询还款明细（汇添金）
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = RepaymentDetailDefine.GET_HTJ_REPAYMENT_DETAIL_LIST_ACTION, method = RequestMethod.POST)
    public ResultListBean getHtjRepaymentDetailList(HttpServletRequest request, HttpServletResponse response,
    		@RequestBody UserBean bean) {
        ResultListBean result = new ResultListBean();

        //根据用户名或手机号取得用户信息
        Users user = this.getUser(bean, result);
        if (user == null) {
            result.setStatus(BaseResultBean.STATUS_FAIL); 
            result.statusMessage(ResultListBean.STATUS_FAIL,"该用户不存在！");
            return result;
        }

        //*************各自业务开始***************
        //根据用户信息查询用户优惠券信息
        List<CallCenterHtjRepaymentDetailCustomize> recordList = this.repaymentDetailService.getHtjRepaymentDetailList(
                user,bean.getLimitStart(),bean.getLimitSize());

        //编辑返回信息
        for (CallCenterHtjRepaymentDetailCustomize recordBean : recordList) {
            RepaymentDetailHtjBean returnBean = new RepaymentDetailHtjBean();
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