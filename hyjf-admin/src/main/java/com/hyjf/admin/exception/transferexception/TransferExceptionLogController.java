package com.hyjf.admin.exception.transferexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.TransferExceptionLog;
import com.hyjf.mybatis.model.customize.admin.AdminTransferExceptionLogCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 转账异常
 */
@Controller
@RequestMapping(value = TransferExceptionLogDefine.REQUEST_MAPPING)
public class TransferExceptionLogController extends BaseController {

    @Autowired
    private TransferExceptionLogService transferLogService;
    
	/**
	 * 画面初始化
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TransferExceptionLogDefine.INIT)
	@RequiresPermissions(TransferExceptionLogDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(TransferExceptionLogDefine.FORM) TransferExceptionLogBean form) {
		LogUtil.startLog(TransferExceptionLogController.class.toString(), TransferExceptionLogDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(TransferExceptionLogDefine.LIST_PATH);
		// 创建分页
		createPage(request, modelAndView, form);
		LogUtil.endLog(TransferExceptionLogController.class.toString(), TransferExceptionLogDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页机能
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, TransferExceptionLogBean form) {
	    Map<String, Object> paraMap = new HashMap<String, Object>();
	    if(StringUtils.isNotEmpty(form.getOrderId())){
            paraMap.put("orderId", form.getOrderId());
        }
        if(StringUtils.isNotEmpty(form.getUsername())){
            paraMap.put("username", form.getUsername());
        }
        if(form.getType() != null){
            paraMap.put("type", String.valueOf(form.getType()));
        }
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
            paraMap.put("timeStartSrch", form.getTimeStartSrch());
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            paraMap.put("timeEndSrch", form.getTimeEndSrch());
        }   
	    Integer count = transferLogService.countRecord(paraMap);
	    if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            paraMap.put("limitStart", paginator.getOffset());
            paraMap.put("limitEnd", paginator.getLimit());
            List<AdminTransferExceptionLogCustomize> recordList  = transferLogService.getRecordList(paraMap);
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(TransferExceptionLogDefine.FORM, form);
	}
	
	/**
	 * 转账确认
	 */
	@ResponseBody
	@RequestMapping(TransferExceptionLogDefine.CONFIRM_ACTION)
    @RequiresPermissions(TransferExceptionLogDefine.PERMISSIONS_RECOVER)
    public String confirmAction(HttpServletRequest request, @ModelAttribute(TransferExceptionLogDefine.FORM) TransferExceptionLogBean form) {
	    LogUtil.startLog(TransferExceptionLogController.class.toString(), TransferExceptionLogDefine.CONFIRM_ACTION);
	    if(StringUtils.isEmpty(form.getUuid()) || form.getStatus() == null){
	        return "0";
	    }
	    
	    int result = transferLogService.updateRecordByUUID(form);
	    LogUtil.endLog(TransferExceptionLogController.class.toString(), TransferExceptionLogDefine.CONFIRM_ACTION);
	    return result + "";
	}
	
	/**
	 * 重新执行转账
	 * @throws Exception 
	 */
	@ResponseBody
    @RequestMapping(TransferExceptionLogDefine.TRANSFER_AGAIN_ACTION)
    @RequiresPermissions(TransferExceptionLogDefine.PERMISSIONS_RECOVER)
    public String transferAgainAction(HttpServletRequest request, @ModelAttribute(TransferExceptionLogDefine.FORM) TransferExceptionLogBean form) {
	    LogUtil.startLog(TransferExceptionLogController.class.toString(), TransferExceptionLogDefine.TRANSFER_AGAIN_ACTION);
        
        if(StringUtils.isEmpty(form.getUuid())){
            return "0:参数错误";
        }

        //同步块避免重复转账
        synchronized (this.getClass()) {
            TransferExceptionLog transfer = transferLogService.getRecordByUUID(form.getUuid());
            if(transfer == null){
                return "0:data not found";
            }
            if(transfer.getStatus() != 1){
                return "0:status has updated";
            }
            if(StringUtils.isEmpty(transfer.getOrderId())){
                String orderId = GetOrderIdUtils.getOrderId2(transfer.getUserId()); 
                transfer.setOrderId(orderId);
                transferLogService.updateRecordByUUID(transfer);
            }
            String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
            
            BankCallBean bean = new BankCallBean();
            bean.setVersion(BankCallConstant.VERSION_10);// 版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
            bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
            bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
            bean.setAccountId(merrpAccount);// 电子账号
            bean.setTxAmount(transfer.getTransAmt().toString());
            bean.setForAccountId(transfer.getAccountId());
            bean.setDesLineFlag("1");
            bean.setDesLine(transfer.getOrderId());// add by cwyang 用于红包发放的银行对账依据,对应accountList 表的Nid
            bean.setLogOrderId(transfer.getOrderId());// 订单号
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            bean.setLogUserId(String.valueOf(transfer.getUserId()));
            bean.setLogClient(0);// 平台
            // 调用银行接口
            BankCallBean resultBean = BankCallUtils.callApiBg(bean);
            if (resultBean == null || !BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                System.out.println("[转账异常] 重新发起转账失败，失败原因：" + (resultBean !=null ? resultBean.getRetMsg() : ""));
                // 转账失败
               return "0:bank api call failed";
            }
            LogUtil.infoLog(this.getClass().getName(), "transferAgainAction", "respcode: " + resultBean.getRetCode());
            
            boolean result;
            try {
                result = transferLogService.transferAfter(transfer, resultBean);
            } catch (Exception e) {
                e.printStackTrace();
                return "0:update not success"; 
            }
            LogUtil.infoLog(this.getClass().getName(), "transferAgainAction", "transferAfter result: " + resultBean.getRetCode());
            // 重新转账成功更新异常表状态
            if (result) {
                //更新转账异常表状态
                form.setStatus(CustomConstants.TRANSFER_EXCEPTION_STATUS_YES);
                transferLogService.updateRecordByUUID(form);
                LogUtil.endLog(TransferExceptionLogController.class.toString(), TransferExceptionLogDefine.TRANSFER_AGAIN_ACTION);
                return "1:success";
            } else {
                LogUtil.endLog(TransferExceptionLogController.class.toString(), TransferExceptionLogDefine.TRANSFER_AGAIN_ACTION);
                return "0:update not success";
            }
        }
	}
}
