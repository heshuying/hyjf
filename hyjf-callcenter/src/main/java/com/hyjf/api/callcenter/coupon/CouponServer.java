package com.hyjf.api.callcenter.coupon;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.callcenter.base.CallcenterBaseController;
import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.coupom.CouponBackMoneyBean;
import com.hyjf.callcenter.coupom.CouponBean;
import com.hyjf.callcenter.coupom.CouponDefine;
import com.hyjf.callcenter.coupom.CouponService;
import com.hyjf.callcenter.coupom.CouponTenderBean;
import com.hyjf.callcenter.result.bean.ResultListBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponBackMoneyCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponTenderCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponUserCustomize;

/**
 * 呼叫中心:查询用户优惠券信息Controller
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 */
@Controller
@RequestMapping(value = CouponDefine.REQUEST_MAPPING)
public class CouponServer extends CallcenterBaseController {
	
	@Autowired
	private CouponService couponService;
	
	/**
	 * 按照用户名/手机号查询优惠券
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = CouponDefine.GET_USER_COUPON_INFO_LIST_ACTION, method = RequestMethod.POST)
	public ResultListBean getUserCouponInfoList(HttpServletRequest request, HttpServletResponse response,
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
		List<CallCenterCouponUserCustomize> recordList = this.couponService.selectCouponUserList(
				user,bean.getLimitStart(),bean.getLimitSize());

		//编辑返回信息
		for (CallCenterCouponUserCustomize recordBean : recordList) {
		    CouponBean returnBean = new CouponBean();
		    setUpCouponBean(recordBean);
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
     * 按照用户名/手机号查询优惠券使用（直投产品）
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.GET_USER_COUPON_TENDER_LIST_ACTION, method = RequestMethod.POST)
    public ResultListBean getUserCouponTenderList(HttpServletRequest request, HttpServletResponse response,
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
        List<CallCenterCouponTenderCustomize> recordList = this.couponService.selectCouponTenderList(
                user,bean.getLimitStart(),bean.getLimitSize());

        //编辑返回信息
        for (CallCenterCouponTenderCustomize recordBean : recordList) {
            CouponTenderBean returnBean = new CouponTenderBean();
            setUpCouponTenderBean(recordBean);
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
     * 十五、按照用户名/手机号查询优惠券回款（直投产品）
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = CouponDefine.GET_USER_COUPON_BACK_MONEY_LIST_ACTION, method = RequestMethod.POST)
    public ResultListBean getUserCouponBackMoneyList(HttpServletRequest request, HttpServletResponse response,
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
        List<CallCenterCouponBackMoneyCustomize> recordList = this.couponService.selectCouponBackMoneyList(
                user,bean.getLimitStart(),bean.getLimitSize());

        //编辑返回信息
        for (CallCenterCouponBackMoneyCustomize recordBean : recordList) {
            CouponBackMoneyBean returnBean = new CouponBackMoneyBean();
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
    
    
	
	

    private void setUpCouponBean(CallCenterCouponUserCustomize recordBean) {
        // 操作平台
        List<ParamName> clients = couponService.getParamNameList("CLIENT");
        recordBean.setProjectType(createProjectTypeString(recordBean.getProjectType()));
        recordBean.setCouponSystem(createCouponSystemString(recordBean.getCouponSystem(),clients));
    }
    
    private void setUpCouponTenderBean(CallCenterCouponTenderCustomize recordBean) {
        // 操作平台
        List<ParamName> clients = couponService.getParamNameList("CLIENT");
        recordBean.setProjectType(createProjectTypeString(recordBean.getProjectType()));
        recordBean.setCouponSystem(createCouponSystemString(recordBean.getCouponSystem(),clients));
    }
    
    
    private String createProjectTypeString(String projectType) {
        String projectString = "";
        if (projectType.indexOf("-1") != -1) {
            projectString = "不限";
        } else {
            //勾选汇直投，尊享汇，融通宝
            if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("6")!=-1) {
                projectString = projectString + "散标,";
            }
            //勾选汇直投  未勾选尊享汇，融通宝
            if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("6")==-1) {
                projectString = projectString + "散标,";
            }
            //勾选汇直投，融通宝  未勾选尊享汇
            if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("6")!=-1){
                projectString = projectString + "散标,";
            }
            //勾选汇直投，选尊享汇 未勾选融通宝
            if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("6")==-1){
                projectString = projectString + "散标,";
            }
            //勾选尊享汇，融通宝  未勾选直投
            if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("6")!=-1){
                projectString = projectString + "散标,";
            }
            //勾选尊享汇  未勾选直投，融通宝
            if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("6")==-1){
                projectString = projectString + "散标,";
            }
            //勾选尊享汇  未勾选直投，融通宝
            if(projectType.indexOf("1")==-1&&projectType.indexOf("4")==-1&&projectType.indexOf("6")!=-1){
                projectString = projectString + "散标,";
            }
            
            if (projectType.indexOf("3")!=-1) {
                projectString = projectString + "新手,";
            }
            if (projectType.indexOf("5")!=-1) {
                projectString = projectString + "计划,";
            }
        }
        projectString = StringUtils.removeEnd(projectString, ",");
        return projectString;
    }
    
    private String createCouponSystemString(String couponSystem, List<ParamName> clients) {
        String clientString = "";

        // 被选中操作平台
        String clientSed[] = StringUtils.split(couponSystem, ",");
        for (int i = 0; i < clientSed.length; i++) {
            if ("-1".equals(clientSed[i])) {
                clientString = clientString + "全部平台";
                break;
            } else {
                for (ParamName paramName : clients) {
                    if (clientSed[i].equals(paramName.getNameCd())) {
                        if (i != 0 && clientString.length() != 0) {
                            clientString = clientString + "、";
                        }
                        clientString = clientString + paramName.getName();

                    }
                }
            }
        }
        return clientString.replace("Android、iOS", "APP");
    }
}