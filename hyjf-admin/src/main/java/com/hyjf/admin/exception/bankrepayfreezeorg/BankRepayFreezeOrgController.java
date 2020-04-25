package com.hyjf.admin.exception.bankrepayfreezeorg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.exception.bankaccountcheck.BankAccountCheckDefine;
import com.hyjf.admin.exception.tendercancelexception.TenderCancelExceptionDefine;
import com.hyjf.bank.service.user.repay.RepayBean;
import com.hyjf.bank.service.user.repay.RepayService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankRepayOrgFreezeLog;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.admin.BankRepayFreezeOrgCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 代偿冻结异常
 */
@Controller
@RequestMapping(value = BankRepayFreezeOrgDefine.REQUEST_MAPPING)
public class BankRepayFreezeOrgController extends BaseController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BankRepayFreezeOrgService bankRepayFreezeService;
    @Autowired
    private RepayService repayService;

	/**
	 * 列表初始化页面
	 *
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(BankRepayFreezeOrgDefine.INIT_ACTION)
	@RequiresPermissions(BankRepayFreezeOrgDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(BankRepayFreezeOrgDefine.BANKREPAY_FORM) BankRepayFreezeOrgBean form) {
		ModelAndView modeAndView = new ModelAndView(BankRepayFreezeOrgDefine.LIST_PATH);
		this.createPage(request, modeAndView, form);
		return modeAndView;
	}

    /**
     * 列表条件检索
     *
     * @param request
     * @param form
     * @return
     */
	@RequestMapping(BankRepayFreezeOrgDefine.SEARCH_ACTION)
	@RequiresPermissions(BankRepayFreezeOrgDefine.PERMISSION_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute(BankRepayFreezeOrgDefine.BANKREPAY_FORM) BankRepayFreezeOrgBean form) {
		ModelAndView modeAndView = new ModelAndView(BankRepayFreezeOrgDefine.LIST_PATH);
		this.createPage(request, modeAndView, form);
		return modeAndView;
	}

	/**
	 * 分页
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BankRepayFreezeOrgBean form) {
		// 资金来源   界面下拉框显示
		List<HjhInstConfig> hjhInstConfigList = this.bankRepayFreezeService.hjhInstConfigList("");
		modelAndView.addObject("hjhInstConfigList", hjhInstConfigList);
		// 还款状态
		List<ParamName> recoverStatusList = this.bankRepayFreezeService.getParamNameList("REPAY_STATUS");
		modelAndView.addObject("recoverStatusList", recoverStatusList);
		Integer count = this.bankRepayFreezeService.selectCount(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<BankRepayFreezeOrgCustomize> recordList = this.bankRepayFreezeService.selectList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(BankAccountCheckDefine.ACCOUNTCHECK_FORM, form);
	}

	/**
	 * 冻结异常情况处理
	 */
	@ResponseBody
	@RequestMapping(value = BankRepayFreezeOrgDefine.PROCESS, method = RequestMethod.POST)
	@RequiresPermissions(BankRepayFreezeOrgDefine.PERMISSIONS_MODIFY)
	public String bankAccountCheckAction(HttpServletRequest request, @RequestBody BankRepayFreezeOrgBean form) {
		JSONObject ret = new JSONObject();
        String orderId = form.getOrderId();
        if (StringUtils.isBlank(orderId)) {
            ret.put(BankRepayFreezeOrgDefine.JSON_STATUS_KEY, BankRepayFreezeOrgDefine.JSON_STATUS_NG);
            ret.put(BankRepayFreezeOrgDefine.JSON_RESULT_KEY, "参数错误，请稍后再试！");
            return ret.toString();
        }
        BankCallBean callApiBg = new BankCallBean();
        if (BankCallConstant.RESPCODE_SUCCESS.equals(form.getRetCode()) && "0".equals(form.getState())) {
            return updateRepayMoney(form, callApiBg);
        } else if (BankCallConstant.RESPCODE_SUCCESS.equals(form.getRetCode()) && !"0".equals(form.getState())) {
            logger.info("【代偿冻结异常处理】订单号：{},未冻结状态,解除冻结！",orderId);
            bankRepayFreezeService.deleteFreezeLogById(form.getId());
            RedisUtils.del("batchOrgRepayUserid_" + form.getRepayUserId());
        } else if (form.getCreateTime() != null && GetDate.getNowTime10() < form.getCreateTime() + 60 * 20) {
            logger.info("【代偿冻结异常处理】订单号：{},冻结时间不满20分钟，不予处理！",orderId);
            ret.put(BankRepayFreezeOrgDefine.JSON_STATUS_KEY, BankRepayFreezeOrgDefine.JSON_STATUS_NG);
            ret.put(BankRepayFreezeOrgDefine.JSON_RESULT_KEY, "处理失败，请稍后再试！");
            return ret.toString();
        } else {
            BankCallBean bean = new BankCallBean();
            bean.setTxCode(BankCallConstant.TXCODE_BALANCE_FREEZE_QUERY);// 消息类型
            bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
            bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
            bean.setTxDate(GetOrderIdUtils.getTxDate());
            bean.setTxTime(GetOrderIdUtils.getTxTime());
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
            bean.setChannel(BankCallConstant.CHANNEL_PC);
            bean.setAccountId(form.getAccount());// 电子账号
            bean.setOrgOrderId(form.getOrderId());
            bean.setLogOrderId(GetOrderIdUtils.getUsrId(form.getRepayUserId()));
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            bean.setLogUserId(String.valueOf(form.getRepayUserId())); // 操作者ID
            bean.setLogClient(0);
            // 调用接口
            callApiBg = BankCallUtils.callApiBg(bean);
            logger.info("单笔还款申请冻结查询银行返回：" + JSON.toJSONString(callApiBg));
            if (Validator.isNotNull(callApiBg)) {
                if (BankCallConstant.RESPCODE_SUCCESS.equals(callApiBg.getRetCode()) && !"0".equals(callApiBg.getState())) {
                    logger.info("【代偿冻结异常处理】订单号：{},未冻结状态,解除冻结！", orderId);
                    bankRepayFreezeService.deleteFreezeLogById(form.getId());
                    RedisUtils.del("batchOrgRepayUserid_" + form.getRepayUserId());
                } else if (BankCallConstant.RESPCODE_SUCCESS.equals(callApiBg.getRetCode()) && "0".equals(callApiBg.getState())) {
                    return updateRepayMoney(form, callApiBg);
                } else {
                    logger.info("【代偿冻结异常处理】订单号：{},未冻结或已解冻状态,解除冻结！", orderId);
                    bankRepayFreezeService.deleteFreezeLogById(form.getId());
                }
            } else {
                ret.put(BankRepayFreezeOrgDefine.JSON_STATUS_KEY, BankRepayFreezeOrgDefine.JSON_STATUS_NG);
                ret.put(BankRepayFreezeOrgDefine.JSON_RESULT_KEY, "处理失败，请稍后再试！");
                return ret.toString();
            }
            ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_OK);
            ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "处理成功!");
            return ret.toString();
        }
        ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_OK);
        ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "处理成功!");
        return ret.toString();
    }

    /**
     * 处理冻结成功的状态
     *
     * @return
     */
    private String updateRepayMoney(BankRepayFreezeOrgBean form, BankCallBean callApiBg) {
        JSONObject ret = new JSONObject();
        logger.info("【代偿冻结异常处理】已冻结，冻结订单号为:" + form.getOrderId());
        String borrowNid = form.getBorrowNid();
        Integer userId = form.getRepayUserId();
        String userName = form.getRepayUserName();
        String orderId = form.getOrderId();
        boolean isAllRepay = form.getAllRepayFlag() == 1;
        try {
            Borrow borrow = this.repayService.searchRepayProject(userId, userName, "3", borrowNid);
            // 担保机构的还款
            RepayBean repay = this.getRepayBean(userId, borrow, isAllRepay);
            if (repay != null) {
                // 还款后变更数据
                callApiBg.setOrderId(orderId);
                // 用于用户资金明细
                callApiBg.setTxDate(orderId.substring(0, 8));
                callApiBg.setTxTime(orderId.substring(8, 14));
                callApiBg.setSeqNo(orderId.substring(14));
                boolean flag = repayService.updateRepayMoney(repay, callApiBg, 3, Integer.valueOf(userId), userName, isAllRepay);
                if (flag) {
                    // 如果有正在出让的债权,先去把出让状态停止
                    bankRepayFreezeService.deleteFreezeLogById(form.getId());
                    repayService.updateBorrowCreditStautus(borrow);
                    //RedisUtils.del("batchOrgRepayUserid_" + form.getRepayUserId());
                    logger.info("【代偿冻结异常处理】担保机构:" + userId + "还款申请成功,标的号:" + borrowNid + ",订单号:" + orderId);
                } else {
                    logger.error("【代偿冻结异常处理】担保机构:" + userId + "还款更新数据失败,标的号:" + borrowNid + ",订单号:" + orderId);
                    ret.put(BankRepayFreezeOrgDefine.JSON_STATUS_KEY, BankRepayFreezeOrgDefine.JSON_STATUS_NG);
                    ret.put(BankRepayFreezeOrgDefine.JSON_RESULT_KEY, "还款更新数据失败,标的号:" + borrowNid + ",订单号:" + orderId);
                    return ret.toString();
                }
            } else {
                logger.info("【代偿冻结异常处理】获取担保机构:" + userId + "还款信息失败,标的号:" + borrowNid + ",订单号:" + orderId);
                ret.put(BankRepayFreezeOrgDefine.JSON_STATUS_KEY, BankRepayFreezeOrgDefine.JSON_STATUS_NG);
                ret.put(BankRepayFreezeOrgDefine.JSON_RESULT_KEY, "还款信息失败,标的号:" + borrowNid + ",订单号:" + orderId);
                return ret.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("【代偿冻结异常处理】担保机构:" + userId + "更新异常,标的号:" + borrowNid + ",订单号:" + orderId);
            ret.put(BankRepayFreezeOrgDefine.JSON_STATUS_KEY, BankRepayFreezeOrgDefine.JSON_STATUS_NG);
            ret.put(BankRepayFreezeOrgDefine.JSON_RESULT_KEY, "更新异常");
            return ret.toString();
        }
        ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_OK);
        ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "处理成功!");
        return ret.toString();
    }

    /**
     * 冻结异常情况查询
     */
    @ResponseBody
    @RequestMapping(value = BankRepayFreezeOrgDefine.CHECK, method = RequestMethod.POST)
    @RequiresPermissions(BankRepayFreezeOrgDefine.PERMISSIONS_MODIFY)
    public BankRepayFreezeOrgBean checkRepayFreezeOrgAction(HttpServletRequest request, @RequestBody BankRepayFreezeOrgBean form) {
        String orderId = form.getOrderId();
        String borrowNid = form.getBorrowNid();
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(borrowNid)) {
            form.setMsg("参数错误，请稍后再试！");
            return form;
        }
        BankRepayOrgFreezeLog repayFreezeFlog = this.bankRepayFreezeService.getFreezeLog(orderId,borrowNid);
        if (Validator.isNull(repayFreezeFlog)) {
            form.setMsg("处理失败，代偿冻结记录不存在");
            return form;
        }
        BeanUtils.copyProperties(repayFreezeFlog, form);
        BankCallBean bean = new BankCallBean();
        bean.setTxCode(BankCallConstant.TXCODE_BALANCE_FREEZE_QUERY);// 消息类型
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_PC);
        bean.setAccountId(repayFreezeFlog.getAccount());// 电子账号
        bean.setOrgOrderId(repayFreezeFlog.getOrderId());
        bean.setLogOrderId(GetOrderIdUtils.getUsrId(repayFreezeFlog.getRepayUserId()));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(String.valueOf(repayFreezeFlog.getRepayUserId())); // 操作者ID
        bean.setLogClient(0);
        // 调用接口
        BankCallBean callApiBg = BankCallUtils.callApiBg(bean);
        logger.info("【代偿冻结异常处理】单笔还款申请冻结查询银行返回：" + JSON.toJSONString(callApiBg));
        if (callApiBg != null) {
            form.setRetCode(callApiBg.getRetCode());
            form.setState(callApiBg.getState());
            form.setMsg(callApiBg.getRetMsg());
        }
        return form;
    }

    /**
     * 获取担保机构还款信息
     *
     * @param userId
     * @param borrow
     * @param isAllRepay
     * @return
     * @throws Exception
     */
    private RepayBean getRepayBean(int userId, Borrow borrow, boolean isAllRepay) throws Exception {
        RepayBean repayByTerm;
        String borrowStyle = StringUtils.isNotBlank(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
        BigDecimal borrowApr = borrow.getBorrowApr();
        Integer repayUserId = borrow.getUserId();
        // 一次性还款
        if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
            repayByTerm = this.repayService.searchRepayTotalV2(repayUserId, borrow);
            repayByTerm.setRepayUserId(userId);// 担保机构id
        } else {// 分期还款
            if (isAllRepay) {
                repayByTerm = this.repayService.searchRepayPlanTotal(repayUserId, borrow);
            } else {
                repayByTerm = this.repayService.searchRepayByTermTotalV2(repayUserId, borrow, borrowApr, borrowStyle, borrow.getBorrowPeriod());
            }
            repayByTerm.setRepayUserId(userId);// 担保机构id
        }
        return repayByTerm;
    }

}
