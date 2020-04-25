package com.hyjf.admin.exception.bankdebtend;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.bank.service.user.creditend.CreditEndService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCreditEnd;
import com.hyjf.mybatis.model.customize.admin.AdminBankDebtEndCustomize;

/**
 * 银行债权结束
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping(value = BankDebtEndDefine.REQUEST_MAPPING)
public class BankDebtEndController extends BaseController {
	@Autowired
	private BankDebtEndService bankDebtEndService;
	
	@Autowired
	private CreditEndService creditEndService;
	
	/**
	 * 
	 * 新债权结束列表请求(相同的权限)
	 * 
	 * @author LIBIN
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(BankDebtEndDefine.INIT)
	@RequiresPermissions(BankDebtEndDefine.PERMISSIONS_VIEW)
	public ModelAndView newInit(HttpServletRequest request, BankDebtEndBean form) throws Exception {
		LogUtil.startLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(BankDebtEndDefine.NEW_LIST_PATH);
		// 创建分页
		this.createNewPage(request, modelAndView, form);
		LogUtil.endLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.INIT);
		return modelAndView;
	}

	@RequestMapping(BankDebtEndDefine.OLD_INIT)
	@RequiresPermissions(BankDebtEndDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, BankDebtEndBean form) throws Exception {
		LogUtil.startLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.OLD_INIT);
		ModelAndView modelAndView = new ModelAndView(BankDebtEndDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.OLD_INIT);
		return modelAndView;
	}
	/**
	 * 
	 * 列表检索Action
	 * 
	 * @author liuyang
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(BankDebtEndDefine.SEARCH_ACTION)
	@RequiresPermissions(BankDebtEndDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BankDebtEndBean form) throws Exception {
		LogUtil.startLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankDebtEndDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.SEARCH_ACTION);
		return modelAndView;
	}
	
	/**
	 * 
	 * 列表检索Action(新)
	 * 
	 * @author liuyang
	 * @param request
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(BankDebtEndDefine.SEARCH_NEW_ACTION)
	@RequiresPermissions(BankDebtEndDefine.PERMISSIONS_SEARCH)
	public ModelAndView searchNew(HttpServletRequest request, BankDebtEndBean form) throws Exception {
		LogUtil.startLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.SEARCH_NEW_ACTION);
		ModelAndView modelAndView = new ModelAndView(BankDebtEndDefine.NEW_LIST_PATH);
		// 分页
		this.createNewPage(request, modelAndView, form);
		LogUtil.endLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.SEARCH_NEW_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能(新)
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @throws Exception
	 */
	private void createNewPage(HttpServletRequest request, ModelAndView modelAndView, BankDebtEndBean form) throws Exception {
		int recordTotal = this.bankDebtEndService.countNewBankDebtEndList(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<AdminBankDebtEndCustomize> recordList = this.bankDebtEndService.selectNewRecordList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(BankDebtEndDefine.FORM, form);
	}
	
	
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 * @throws Exception
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankDebtEndBean form) throws Exception {
		int recordTotal = this.bankDebtEndService.countBankDebtEndList(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<AdminBankDebtEndCustomize> recordList = this.bankDebtEndService.selectRecordList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(BankDebtEndDefine.FORM, form);
	}

	/**
	 * 结束债权
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = BankDebtEndDefine.MODIFY_ACTION, produces = "application/json; charset=UTF-8")
	@RequiresPermissions(BankDebtEndDefine.PERMISSIONS_MODIFY)
	public String updateDebtEndAction(HttpServletRequest request, @ModelAttribute BankDebtEndBean form) {
		LogUtil.startLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.MODIFY_ACTION);
		JSONObject ret = new JSONObject();
		// 出借订单号
		if (Validator.isNull(form.getTenderNid())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "出借订单号为空");
			return ret.toString();
		}
		if (Validator.isNull(form.getUserId())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "出借用户Id为空");
			return ret.toString();
		}
		if (Validator.isNull(form.getBorrowNid())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "项目编号为空");
			return ret.toString();
		}
		String tenderNid = form.getTenderNid();
		Integer tenderUserId = form.getUserId();
		String borrowNid = form.getBorrowNid();
		try {
			// 调用结束债权
			boolean debtEndFlag = this.bankDebtEndService.requestDebtEnd(borrowNid, tenderNid, tenderUserId);
			if (!debtEndFlag) {
				ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
				ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "债权结束失败");
				return ret.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_OK);
		ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "债权结束成功");
		LogUtil.endLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.MODIFY_ACTION);
		return ret.toJSONString();
	}
	
	/**
	 * 结束债权(新)同步
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = BankDebtEndDefine.MODIFY_NEW_ACTION, produces = "application/json; charset=UTF-8")
	@RequiresPermissions(BankDebtEndDefine.PERMISSIONS_MODIFY)
	public String updateNewDebtEndAction(HttpServletRequest request, @ModelAttribute BankDebtEndBean form) {
		LogUtil.startLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.MODIFY_NEW_ACTION);
		JSONObject ret = new JSONObject();
		
		if (Validator.isNull(form.getTxCounts())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次交易笔数为空");
			return ret.toString();
		}
		if (Validator.isNull(form.getTxDate())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次交易日期为空");
			return ret.toString();
		}
		if (Validator.isNull(form.getBatchNo())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次号为空");
			return ret.toString();
		}
		if (Validator.isNull(form.getStatus())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次状态为空");
			return ret.toString();
		}
		// 总笔数
		String txCounts = form.getTxCounts();
		// 交易日期
		String txDate = form.getTxDate();
		// 批次号
		String batchNo = form.getBatchNo();
		// 状态
		String status = form.getStatus();
		BankCreditEnd bankCreditEnd = new BankCreditEnd();
		// 皆不为空
		if(StringUtils.isNotBlank(txCounts) && StringUtils.isNotBlank(txDate) && StringUtils.isNotBlank(batchNo) ){
			bankCreditEnd.setTxCounts(Integer.valueOf(txCounts));
			bankCreditEnd.setTxDate(txDate);
			bankCreditEnd.setBatchNo(batchNo);
			bankCreditEnd.setStatus(Integer.valueOf(status));
		} else {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "缺少参数导致同步债权信息失败");
			return ret.toString();
		}
		try {
			boolean debtEndFlag = this.creditEndService.batchDetailsQuery(bankCreditEnd);
			if (!debtEndFlag) {
				ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
				ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "同步债权信息失败");
				return ret.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_OK);
		ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "同步债权信息成功");
		LogUtil.endLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.MODIFY_ACTION);
		return ret.toJSONString();
	}
	
	
	/**
	 * 结束债权(新)更新
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = BankDebtEndDefine.CHANGE_NEW_ACTION, produces = "application/json; charset=UTF-8")
	@RequiresPermissions(BankDebtEndDefine.PERMISSIONS_MODIFY)
	public String changeNewDebtEndAction(HttpServletRequest request, @ModelAttribute BankDebtEndBean form) {
		LogUtil.startLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.CHANGE_NEW_ACTION);
		JSONObject ret = new JSONObject();
	
		if (Validator.isNull(form.getTxDate())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次交易日期为空");
			return ret.toString();
		}
		if (Validator.isNull(form.getBatchNo())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次号为空");
			return ret.toString();
		}
		if (Validator.isNull(form.getStatus()) && Integer.parseInt(form.getStatus()) < 10) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "非错误批次不能恢复为初始状态");
			return ret.toString();
		}
		if (Validator.isNull(form.getOrderId())) {
			ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
			ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "订单号为空");
			return ret.toString();
		}

		// 状态
		String status = form.getStatus();
		// 订单号
		String orderId = form.getOrderId();
		
		BankCreditEnd bankCreditEnd = this.creditEndService.selectByOrderId(orderId);

		try {
			int debtEndFlag = this.creditEndService.updateCreditEndForInitial(bankCreditEnd);
			if (debtEndFlag <= 0) {
				ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_NG);
				ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次恢复为初始状态，操作失败");
				return ret.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put(BankDebtEndDefine.JSON_STATUS_KEY, BankDebtEndDefine.JSON_STATUS_OK);
		ret.put(BankDebtEndDefine.JSON_RESULT_KEY, "批次恢复为初始状态，操作成功");
		LogUtil.endLog(BankDebtEndDefine.THIS_CLASS, BankDebtEndDefine.MODIFY_ACTION);
		return ret.toJSONString();
	}

}
