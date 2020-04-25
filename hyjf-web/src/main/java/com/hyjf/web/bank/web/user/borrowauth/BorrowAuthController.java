/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
package com.hyjf.web.bank.web.user.borrowauth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.borrowauth.BorrowAuthRequestBean;
import com.hyjf.bank.service.user.borrowauth.BorrowAuthService;
import com.hyjf.bank.service.user.borrowauth.BorrowAuthServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.STZHWhiteList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.web.WebBorrowAuthCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;

import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping(value = BorrowAuthDefine.REQUEST_MAPPING)
public class BorrowAuthController extends BaseController {

	public static JedisPool pool = RedisUtils.getPool();

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private BorrowAuthService borrowAuthService;
	
	// 获取用户待授权列表
	@ResponseBody
    @RequestMapping(value = BorrowAuthDefine.NEED_AUTH_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public BorrowAuthAjaxResult searchUserNeedAuthList(@ModelAttribute BorrowAuthRequestBean userRepay, HttpServletRequest request, HttpServletResponse response) {

        BorrowAuthAjaxResult result = new BorrowAuthAjaxResult();
        // 用户ID
        WebViewUser user = WebUtils.getUser(request);
        if (user != null) {
            userRepay.setUserId(user.getUserId().toString());
            userRepay.setRoleId(user.getRoleId());// 角色分借款人、垫付机构
            result.setRoleId(user.getRoleId());
            this.createBorrowNeedAuthListPage(request, result, userRepay);
        }
        return result;
    }
	
	/**
	 * 
	 * 分页查询
	 * @author sunss
	 * @param request
	 * @param result 结果
	 * @param form 查询参数
	 */
	private void createBorrowNeedAuthListPage(HttpServletRequest request, BorrowAuthAjaxResult result, BorrowAuthRequestBean form) {

        int recordTotal = this.borrowAuthService.countBorrowNeedAuthRecordTotal(form);// 查询记录总数（个人和机构）
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
            List<WebBorrowAuthCustomize> recordList = borrowAuthService.searchBorrowNeedAuthList(form, paginator.getOffset(), paginator.getLimit());
            result.success();
            result.setPaginator(paginator);
            result.setborrowList(recordList);
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
            result.success();
            result.setPaginator(paginator);
        }
    }
	
	// 获取用户已授权列表
	@ResponseBody
    @RequestMapping(value = BorrowAuthDefine.AUTHED_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public BorrowAuthAjaxResult searchUserAuthedList(@ModelAttribute BorrowAuthRequestBean userRepay, HttpServletRequest request, HttpServletResponse response) {

        BorrowAuthAjaxResult result = new BorrowAuthAjaxResult();
        // 用户ID
        WebViewUser user = WebUtils.getUser(request);
        if (user != null) {
            userRepay.setUserId(user.getUserId().toString());
            userRepay.setRoleId(user.getRoleId());// 角色分借款人、垫付机构
            result.setRoleId(user.getRoleId());
            this.createBorrowAuthedListPage(request, result, userRepay);
        }
        return result;
    }

    private void createBorrowAuthedListPage(HttpServletRequest request, BorrowAuthAjaxResult result,
        BorrowAuthRequestBean form) {
        
        int recordTotal = this.borrowAuthService.countBorrowAuthedRecordTotal(form);// 查询记录总数（个人和机构）
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
            List<WebBorrowAuthCustomize> recordList = borrowAuthService.searchBorrowAuthedList(form, paginator.getOffset(), paginator.getLimit());
            result.success();
            result.setPaginator(paginator);
            result.setborrowList(recordList);
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
            result.success();
            result.setPaginator(paginator);
        }
    }
    
    // 受托支付授权
    @RequestMapping(BorrowAuthDefine.AUTH_PAGE_ACTION)
    public ModelAndView trusteePay(HttpServletRequest request,String borrowId, HttpServletResponse response) {

        LogUtil.startLog(BorrowAuthController.class.toString(), BorrowAuthDefine.AUTH_PAGE_ACTION);
        ModelAndView modelAndView = new ModelAndView();
        // 用户id
        WebViewUser user = WebUtils.getUser(request);
       
        if (user == null) {
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "登录失效，请重新登陆");
            return modelAndView;
        }
        Users users=borrowAuthService.getUsers(user.getUserId());
        UsersInfo usersInfo = borrowAuthService.getUsersInfoByUserId(user.getUserId());
        
        if (users.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未开户！");
            return modelAndView;
        }
        // 判断用户是否设置过交易密码
        if (users.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "用户未设置交易密码");
            return modelAndView;
        }
        
        // 检查标的是否存在
        Borrow borrow = this.borrowAuthService.selectBorrowByProductId(borrowId);
        if(borrow==null){
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "标的不存在");
            return modelAndView;
        }
        
        // 检查标的状态 // 待授权状态才可以
        if(!borrow.getStatus().equals(BorrowAuthServiceImpl.BORROW_STATUS_WITE_AUTHORIZATION)){
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "标的状态错误");
            return modelAndView;
        }
        
        // 查询受托支付用户ID
        Users stzfUser=borrowAuthService.getUsers(borrow.getEntrustedUserId());
        if (stzfUser == null) {
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "受托支付用户不存在");
            return modelAndView;
        }
        if (stzfUser.getBankOpenAccount()==0) {// 未开户
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "受托支付用户未开户！");
            return modelAndView;
        }
        // 判断用户是否设置过交易密码
        if (stzfUser.getIsSetPassword() == 0) {// 未设置交易密码
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "受托支付用户未设置交易密码");
            return modelAndView;
        }
        
        // 查询是否在白名单里面
        STZHWhiteList whiteList = borrowAuthService.getSTZHWhiteListByUserID(user.getUserId(),stzfUser.getUserId());
        if(whiteList==null){
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "未在受托支付白名单中");
            return modelAndView;
        }
        
        if(whiteList.getState()-0==0){
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "受托支付白名单状态错误");
            return modelAndView;
        }
        
        int userId = user.getUserId();
        
        
        // 同步调用路径
        String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) 
                + BorrowAuthDefine.REQUEST_MAPPING + BorrowAuthDefine.RETURL_SYN_ACTION + ".do?"+ BankCallConstant.PARAM_PRODUCTID + "=" + borrowId;        
        // 异步调用路
        String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)
                + BorrowAuthDefine.REQUEST_MAPPING + BorrowAuthDefine.RETURL_ASY_ACTION + ".do";
        // 银行成功后跳转
        String successfulUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) 
                + BorrowAuthDefine.REQUEST_MAPPING + BorrowAuthDefine.RETURL_SYN_ACTION + ".do?&isSuccess=1&"+ BankCallConstant.PARAM_PRODUCTID + "=" + borrowId;
        
        // 调用受托支付授权接口
        BankCallBean bean = new BankCallBean();
        // 设置共同参数
        setCommonCall(bean);
        
        bean.setTxCode(BankCallConstant.TXCODE_TRUSTEE_PAY);// 消息类型(用户开户)
        bean.setChannel("000002");// 渠道APP:000001,  渠道PC:000002 渠道Wechat:000003
        bean.setAccountId(whiteList.getAccountid());// 电子账号
        bean.setProductId(borrowId); //标的编号
        bean.setReceiptAccountId(whiteList.getStAccountid()); // 收款人电子帐户
        // 取用户类型 如果企业用户 上送不同
        if (user.getUserType() == 1) { // 企业用户 传组织机构代码
            CorpOpenAccountRecord record = borrowAuthService.getCorpOpenAccountRecord(userId);
            bean.setIdType(record.getCardType() != null ? String.valueOf(record.getCardType()) : BankCallConstant.ID_TYPE_COMCODE);// 证件类型 20：其他证件（组织机构代码）25：社会信用号
            bean.setIdNo(record.getBusiCode());
        }else{
            bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);
            bean.setIdNo(usersInfo.getIdcard());
        }
        
        // 检查证件号是否为空
        if (Validator.isNull(bean.getIdNo())) {
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", "证件号为空");
            return modelAndView;
        }
        
        bean.setForgotPwdUrl(CustomConstants.FORGET_PASSWORD_URL); // 忘记密码连接
        
        bean.setRetUrl(retUrl);// 页面同步返回 URL
        bean.setNotifyUrl(bgRetUrl);// 页面异步返回URL(必须)
        bean.setSuccessfulUrl(successfulUrl);
        // 操作者ID
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_TRUSTEE_PAY);
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogRemark("平台借款人受托支付申请");
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        
        // 跳转到汇付天下画面
        try {
            modelAndView = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.endLog(BorrowAuthController.class.toString(), BorrowAuthDefine.AUTH_PAGE_ACTION);
        return modelAndView;
    }

    private void setCommonCall(BankCallBean bean) {
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
    }
    
    private BankCallBean queryIsSuccess(String accountId,String productId,Integer userId) {
        // 调用查询接口 查询是否成功授权
        BankCallBean selectbean = new BankCallBean();
        // 设置共通参数
        setCommonCall(selectbean);
        selectbean.setTxCode(BankCallConstant.TXCODE_TRUSTEE_PAY_QUERY);
        selectbean.setAccountId(accountId);// 电子账号
        selectbean.setProductId(productId); // 标的编号

        // 操作者ID
        selectbean.setLogUserId(String.valueOf(userId));
        selectbean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        selectbean.setLogClient(0);
        selectbean.setLogRemark("受托支付申请查询");
        selectbean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        // 返回参数
        BankCallBean retBean = null;
        // 调用接口
        retBean = BankCallUtils.callApiBg(selectbean);
        return retBean;
    }
    
    /**
     * 受托支付同步回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(BorrowAuthDefine.RETURL_SYN_ACTION)
    public ModelAndView userAuthInvesReturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {

        LogUtil.startLog(this.getClass().toString(),BorrowAuthDefine.RETURL_SYN_ACTION, "[受托支付同步回调]");
        ModelAndView modelAndView = new ModelAndView();
        bean.convert();
        String isSuccess = request.getParameter("isSuccess");
        // 调用成功了
        if (isSuccess != null && "1".equals(isSuccess)) {
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_SUCCESS_PATH);
            modelAndView.addObject("message", "受托支付申请成功");
            return modelAndView;
        }
        // 调用查询接口 查询是否成功授权
        int userId = Integer.parseInt(bean.getLogUserId());
        String productId = request.getParameter("productId");
        BankOpenAccount bankOpenAccount = this.borrowAuthService.getBankOpenAccount(userId);
        
        BankCallBean retBean = queryIsSuccess(bankOpenAccount.getAccount(),productId,bankOpenAccount.getUserId() );
        
        bean = retBean;
        // 查询是否成功
        if (bean!=null&&BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) && "1".equals(bean.getState())) {
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_SUCCESS_PATH);
            modelAndView.addObject("message", "受托支付申请成功");
            /*-----------upd by liushouyi HJH3 Start-------------------*/
        	// 审核保证金的标的发送MQ到消息队列
            Borrow borrow = this.borrowAuthService.selectBorrowByProductId(productId);
			HjhAssetBorrowType hjhAssetBorrowType = this.borrowAuthService.selectAssetBorrowType(borrow);
			if (null != hjhAssetBorrowType && null != hjhAssetBorrowType.getAutoBail() && hjhAssetBorrowType.getAutoBail() == 1) {
				// 加入到消息队列
				this.borrowAuthService.sendToMQ(borrow, RabbitMQConstants.ROUTINGKEY_BORROW_BAIL);
			}
            /*-----------upd by liushouyi HJH3 End-------------------*/
        } else {
            modelAndView = new ModelAndView(BorrowAuthDefine.BORROW_AUTH_ERROR_PATH);
            modelAndView.addObject("message", borrowAuthService.getBankRetMsg(bean.getRetCode()));
        }
        
        LogUtil.endLog(this.getClass().toString(),BorrowAuthDefine.RETURL_SYN_ACTION, "[受托支付同步回调结束]");
        return modelAndView;
    }

    /**
     * 受托支付异步回调
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(BorrowAuthDefine.RETURL_ASY_ACTION)
    public String userAuthInvesBgreturn(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute BankCallBean bean) {
        BankCallResult result = new BankCallResult();
        LogUtil.startLog(this.getClass().toString(),BorrowAuthDefine.RETURL_SYN_ACTION, "[受托支付异步回调结束]");
        bean.convert();

        // 查询是否成功
        if (bean!=null&&BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) && "1".equals(bean.getState())) {
            try {
                // 修改对应数据
                // 修改huiyingdai_borrow表状态  根据返回值productId对应表的id   status改为2
                // 修改hyjf_hjh_plan_asset status改为
                boolean ok = borrowAuthService.updateTrusteePaySuccess(bean);
                if (ok) {
                    result.setMessage("受托支付申请成功");
                    result.setStatus(true);
                } else {
                    result.setMessage("受托支付申请操作失败");
                    result.setStatus(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           
        } else{
            result.setMessage("受托支付申请失败");
            result.setStatus(true);
        }
        LogUtil.endLog(this.getClass().toString(),BorrowAuthDefine.RETURL_SYN_ACTION, "[受托支付异步回调结束]");
       
        return JSONObject.toJSONString(result, true);
    }
}
