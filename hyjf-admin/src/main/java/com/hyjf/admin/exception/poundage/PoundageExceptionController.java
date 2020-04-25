package com.hyjf.admin.exception.poundage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.jpush.api.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.finance.bankaccountmanage.BankAccountManageServiceImpl;
import com.hyjf.admin.finance.bankaccountmanage.ResultBean;
import com.hyjf.admin.finance.poundage.PoundageBean;
import com.hyjf.admin.finance.poundage.PoundageService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.PoundageExample;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageExceptionCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;

/**
 * 页面数据：
 * 1、列表数据类型
 * 银行返回手续费分账失败和异常、处理中的的数据在异常处理中显示
 * 2、列表排序
 * 按异常信息生成时间倒序排序，即最新的在前面
 * 3、查询字段
 * （1）订单号：输入框，模糊匹配；
 * （2）流水号：输入框，模糊匹配；
 * （7）收款人用户名：输入框，模糊匹配；
 * （8）分账状态：下拉列表，单选，选项：全部/未分账/已分账，默认：全部；
 * （10）分账时间段：时间控件，具体到天；结束值必须大于等于开始值，且时间段最大1个月；默认为空，即表示不限；
 *
 * @author wgx
 * @date 2017/12/15
 */
@Controller
@RequestMapping(value = PoundageExceptionDefine.REQUEST_MAPPING)
public class PoundageExceptionController extends BaseController {

    @Autowired
    private PoundageExceptionService poundageExceptionService;
    @Autowired
    private PoundageService poundageService;
    @Autowired
    private BankAccountManageServiceImpl bankAccountManageServiceImpl;

    Logger _log = LoggerFactory.getLogger(PoundageExceptionController.class);

    /**
     * 查询列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PoundageExceptionDefine.POUNDAGE_EXCEPTION_LIST)
    @RequiresPermissions(PoundageExceptionDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, PoundageExceptionBean form) {
        LogUtil.startLog(PoundageExceptionController.class.toString(), PoundageExceptionDefine.POUNDAGE_EXCEPTION_LIST);
        ModelAndView modelAndView = new ModelAndView(PoundageExceptionDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(PoundageExceptionController.class.toString(), PoundageExceptionDefine.POUNDAGE_EXCEPTION_LIST);
        return modelAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, PoundageExceptionBean form) {
        PoundageExceptionCustomize poundageexceptionCustomize = new PoundageExceptionCustomize();
        BeanUtils.copyProperties(form, poundageexceptionCustomize);
        Integer count = this.poundageExceptionService.getPoundageExceptionCount(poundageexceptionCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            poundageexceptionCustomize.setLimitStart(paginator.getOffset());
            poundageexceptionCustomize.setLimitEnd(paginator.getLimit());

            List<PoundageExceptionCustomize> customers = this.poundageExceptionService.getPoundageExceptionList(poundageexceptionCustomize);
            form.setPaginator(paginator);
            form.setRecordList(customers);
        }
        modelAndView.addObject(PoundageExceptionDefine.POUNDAGE_EXCEPTION_FORM, form);

    }

    /**
     * 添加按钮,跳转添加详情画面
     *
     * @param request
     * @param form
     * @return
     * @author wgx
     */
    @RequestMapping(PoundageExceptionDefine.DETAIL_ACTION)
    @RequiresPermissions(PoundageExceptionDefine.PERMISSIONS_ADD)
    public ModelAndView detailAction(HttpServletRequest request, PoundageExceptionBean form) {
        LogUtil.startLog(PoundageExceptionController.class.getName(), PoundageExceptionDefine.DETAIL_ACTION);
        ModelAndView modelAndView = new ModelAndView(PoundageExceptionDefine.DETAIL_PATH);
        String id = request.getParameter("id");
        if (id != null && id != "") {
            PoundageExceptionCustomize poundageException = this.poundageExceptionService.getPoundageExceptionById(Integer.valueOf(id));
            // 转出方用户电子账户号
            poundageException.setAccountId(CustomConstants.HYJF_BANK_MERS_ACCOUNT);
            // 余额
            BigDecimal balance = poundageService.getBankBalance(Integer.parseInt(ShiroUtil.getLoginUserId()), CustomConstants.HYJF_BANK_MERS_ACCOUNT);
            if (balance == null) {
                modelAndView.addObject(PoundageExceptionDefine.SUCCESS, PoundageExceptionDefine.JSON_STATUS_NG);
            } else {
                poundageException.setBalance(balance.toString());
            }
            modelAndView.addObject(PoundageExceptionDefine.POUNDAGE_EXCEPTION_FORM, poundageException);
        }
        LogUtil.endLog(PoundageExceptionController.class.getName(), PoundageExceptionDefine.DETAIL_ACTION);
        return modelAndView;
    }

    /**
     * 佣金分账
     * 1.查询银行平台还款服务费账户余额
     * 2.查询江西银行这笔转账当前状态是否成功
     * (1)若已成功（返回状态"S"），则提示已分账成功，不再进行转账流程，只是在资金明细中插入数据;
     * (2)若失败，则再次发起转账,需要走转账流程，输入交易密码;
     * (3)若还在处理中（返回状态"D"），则不允许再次发起转账.
     * 3.调用江西银行接口分佣
     * 成功:更新手续费分账信息--更新分账时间和分账状态（分账成功）;
     * 更新手续费异常信息--更新分账状态（分账成功）;
     * 更新转入用户账户信息--更新转入用户江西银行总资产和江西银行可用余额;
     * 插入交易明细（资金中心-资金明细）--交易类型:分账,收支类型：收入;
     * 插入手续费账户明细（资金中心-银行平台用户-手续费账户明细）--交易类型:分账,收支类型:支出.
     * 失败:返回错误信息
     *
     * @param request
     * @param expForm
     * @return
     * @author wgx
     */
    @RequestMapping(value = PoundageExceptionDefine.TRANSFER_ACTION)
    @RequiresPermissions(PoundageExceptionDefine.PERMISSIONS_ADD)
    public ModelAndView transferAction(HttpServletRequest request, PoundageExceptionBean expForm) {
        LogUtil.startLog(PoundageExceptionController.class.getName(), PoundageExceptionDefine.TRANSFER_ACTION);
        ModelAndView modelAndView = new ModelAndView(PoundageExceptionDefine.DETAIL_PATH);
        // 登陆用户ID
        Integer loginUserId = Integer.parseInt(ShiroUtil.getLoginUserId());
        // 转出用户电子账户号
        String accountId = expForm.getAccountId();
        // 余额
        BigDecimal balance = poundageService.getBankBalance(loginUserId, accountId);
        expForm.setBalance(balance == null ? BigDecimal.ZERO.toString() : balance.toString());// 账户余额
        expForm.setUpdater(loginUserId);
        expForm.setUpdateTime(GetDate.getNowTime10());
        PoundageBean form = createPoundageBean(expForm);
        poundageService.checkTransferParam(modelAndView, form);
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            modelAndView.addObject(PoundageExceptionDefine.POUNDAGE_EXCEPTION_FORM, expForm);
            return modelAndView;
        } else {
            // 首先查询江西银行这笔转账当前状态是否成功
            BankCallBean callBean = new BankCallBean();
            callBean.setOrderId(expForm.getNid());
            callBean.setAccountId(expForm.getAccountId());
            callBean.setLogUserId(loginUserId.toString());
            callBean.setLogOrderId(GetOrderIdUtils.getOrderId2(loginUserId));// 订单号
            BankCallBean bankCallBean = poundageService.checkLedgerResult(callBean);
            boolean isSuccess = checkLedgerStatus(loginUserId, form);
            if (isSuccess) {
                // 更新订单,用户账户等信息
                boolean updateFlag = poundageService.updateAfterLedgerSuccess(bankCallBean, form);
                if (!updateFlag) {
                    _log.info("调用银行成功后,更新数据失败");
                    // 转账成功，更新状态失败
                    return getModelAndView(expForm, modelAndView, bankCallBean, "feeshare.transfer.success", "调用银行成功后,更新数据失败");
                }
                //分账成功添加分账时间
                int time = poundageService.getAddTime(bankCallBean);
                return getSuccessModelAndView(expForm, modelAndView, form, time);
            }
            /*if (bankCallBean != null && bankCallBean.getTxState() != null) {
                if ("S".equals(bankCallBean.getTxState())) {// 已成功
                    // 更新订单,用户账户等信息
                    boolean updateFlag = poundageService.updateAfterLedgerSuccess(bankCallBean, form);
                    if (!updateFlag) {
                        _log.info("调用银行成功后,更新数据失败");
                        // 转账成功，更新状态失败
                        return getModelAndView(expForm, modelAndView, bankCallBean, "feeshare.transfer.success", "调用银行成功后,更新数据失败");
                    }
                    //分账成功添加分账时间
                    int time = poundageService.getAddTime(bankCallBean);
                    return getSuccessModelAndView(expForm, modelAndView, form, time);
                } else if ("D".equals(bankCallBean.getTxState())) {// 待处理
                    return getModelAndView(expForm, modelAndView, null, "feeshare.transfer.success", "交易处理中，请稍后查询明细");
                }
            }*/
            // 调用江西银行接口分佣
            try {
                BankCallBean resultBean = poundageService.getLedgerResult(form);
                if (resultBean == null || resultBean.getRetCode() == null) {
                    String logOrderId = resultBean.getLogOrderId() == null ? "" : resultBean.getLogOrderId();
                    _log.info("调用银行接口失败,银行返回空.订单号:[" + logOrderId + "].");
                    return getModelAndView(expForm, modelAndView, null, "feeshare.transfer.error", "调用银行接口失败");
                }
                // 银行返回响应代码
                String retCode = resultBean == null ? "" : resultBean.getRetCode();
                if ("CA51".equals(retCode)) {
                    return getModelAndView(expForm, modelAndView, resultBean, "feeshare.transfer.txamount.error", "账户余额不足");
                }
                // 调用银行接口失败
                if (!BankCallStatusConstant.RESPCODE_SUCCESS.equals(retCode)) {
                    return getModelAndView(expForm, modelAndView, resultBean, "feeshare.transfer.error", "调用银行接口失败(" + retCode + ")");
                }
                // 银行返回成功
                // 更新订单,用户账户等信息
                boolean updateFlag = poundageService.updateAfterLedgerSuccess(resultBean, form);
                if (!updateFlag) {
                    _log.info("调用银行成功后,更新数据失败");
                    // 转账成功，更新状态失败
                    return getModelAndView(expForm, modelAndView, resultBean, "feeshare.transfer.success", "调用银行成功后,更新数据失败");
                }
                poundageService.addBankCall(form, resultBean);
            } catch (Exception e) {
                _log.info("转账发生异常:异常信息:[" + e.getMessage() + "].");
                return getModelAndView(expForm, modelAndView, null, "feeshare.transfer.exception", "转账发生异常");
            }
        }
        return getSuccessModelAndView(expForm, modelAndView, form, null);
    }

    /**
     * 根据转出交易明细查询交易是否存在
     *
     * @return
     */
    private boolean checkLedgerStatus(Integer userId, PoundageBean form) {
        List<ResultBean> resultBeanList = queryAllAccountDetails(userId, form);
        if (resultBeanList == null) {
            return false;
        }
        for (ResultBean resultBean : resultBeanList) {
            if (StringUtils.isNotEmpty(form.getSeqNo()) && Integer.parseInt(form.getSeqNo()) == resultBean.getTraceNo()) {
                if (resultBean.getForAccountId().equals(form.getAccount())) {// 转入账户
                    if (resultBean.getTxAmount().compareTo(form.getAmount()) == 0) {//分账金额
                        if ("O".equals(resultBean.getOrFlag())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * 获得接口返回分账所有转出交易明细
     *
     * @param
     * @param
     * @return
     */
    private List<ResultBean> queryAllAccountDetails(Integer userId, PoundageBean form) {
        // 获得接口返回的所有交易明细
        // 分页数据
        String date = form.getTxDate() != null ? form.getTxDate().toString() : "";
        List<ResultBean> recordList = new ArrayList<ResultBean>();
        List<ResultBean> list = new ArrayList<ResultBean>();
        String inpDate = null;
        String inpTime = null;
        String relDate = null;
        String traceNo = null;
        do {
            // 调用查询明细接口 查所有交易明细
            BankCallBean bean = queryAccountDetails(userId, form.getAccountId(), date, date, "9", "2849", inpDate, inpTime, relDate, traceNo);
            if (bean == null) {
                LogUtil.endLog(this.getClass().getName(), "获取明细失败");
                return null;
            }
            //返回失败
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
                LogUtil.endLog(this.getClass().getName(), "-------------------调用查询接口失败，失败原因：" + bankAccountManageServiceImpl.getBankRetMsg(bean.getRetCode()) + "--------------------");
                return null;
            }
            //解析返回数据(记录为空)
            String content = bean.getSubPacks();
            if (org.apache.commons.lang3.StringUtils.isEmpty(content)) {
                return recordList;
            }
            list = JSONArray.parseArray(bean.getSubPacks(), ResultBean.class);
            recordList.addAll(list);
            //inpDate = list.get(list.size() - 1).getInpDate();
            //inpTime = list.get(list.size() - 1).getInpTime();
            //relDate = list.get(list.size() - 1).getRelDate();
            //traceNo = String.valueOf(list.get(list.size() - 1).getTraceNo());
        } while (list.size() == 10);

        LogUtil.infoLog(this.getClass().getName(), "-------------------" + recordList.size() + "获取明细总条数--------------------");
        return recordList;
    }

    /**
     * 调用交易明细查询接口获得交易明细
     *
     * @param userId
     * @param accountId
     * @param startDate
     * @param endDate
     * @param type
     * @param transType
     * @return
     */
    public BankCallBean queryAccountDetails(Integer userId, String accountId, String startDate, String endDate, String type, String transType, String inpDate, String inpTime, String relDate, String traceNo) {
        // 参数不正确
        if (org.apache.commons.lang3.StringUtils.isEmpty(accountId) || org.apache.commons.lang3.StringUtils.isEmpty(startDate)
                || org.apache.commons.lang3.StringUtils.isEmpty(endDate) || org.apache.commons.lang3.StringUtils.isEmpty(type)) {
            return null;
        }
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_DETAILS_QUERY + 2);// 消息类型
        // 修改手机号增强
        // accountDetailsQuery
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_PC);
        bean.setAccountId(accountId);// 电子账号
        bean.setStartDate(startDate);// 起始日期
        bean.setEndDate(endDate);// 结束日期
        bean.setType(type);// 交易种类 0-所有交易 1-转入交易 2-转出交易 9-指定交易类型
        if ("9".equals(type)) {
            bean.setTranType(transType);// 交易类型
        }
        if (StringUtils.isNotEmpty(inpDate)) {
            //bean.setRtnInd("1");
            //bean.setInpDate(inpDate);
            //bean.setInpTime(inpTime);
            //bean.setRelDate(relDate);
            //bean.setTraceNo(traceNo);
        }
        // 操作者ID
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
        // 调用接口
        return BankCallUtils.callApiBg(bean);
    }

    /**
     * 分账成功后返回前台页面
     *
     * @param expForm
     * @param modelAndView
     * @param form
     * @param successTime
     * @return
     */
    private ModelAndView getSuccessModelAndView(PoundageExceptionBean expForm, ModelAndView modelAndView, PoundageBean form, Integer successTime) {
        if (successTime != null) {
            form.setAddTime(successTime);
        }
        form.setStatus(PoundageBean.STATUS_SUCCESS);// 分账状态:2.分账成功
        expForm.setLedgerStatus(PoundageExceptionBean.STATUS_SUCCESS);// 分账状态:1.已分账
        modelAndView.addObject(PoundageExceptionDefine.POUNDAGE_EXCEPTION_FORM, expForm);
        modelAndView.addObject(PoundageExceptionDefine.SUCCESS, PoundageExceptionDefine.SUCCESS);
        poundageExceptionService.updateStatus(form, expForm);
        LogUtil.endLog(PoundageExceptionController.class.getName(), PoundageExceptionDefine.TRANSFER_ACTION);
        return modelAndView;
    }

    /**
     * 根据错误信息返回前台页面
     *
     * @param expForm
     * @param modelAndView
     * @param resultBean
     * @param errorId
     * @param error
     * @return
     */
    private ModelAndView getModelAndView(PoundageExceptionBean expForm, ModelAndView modelAndView, BankCallBean resultBean, String errorId, String error) {
        ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "error", errorId, error);
        PoundageBean poundageBean = createPoundageBean(expForm);
        poundageService.addBankCall(poundageBean, resultBean);
        poundageService.updatePoundage(poundageBean);
        expForm.setTxDate(poundageBean.getTxDate());
        expForm.setTxTime(poundageBean.getTxTime());
        modelAndView.addObject(PoundageExceptionDefine.POUNDAGE_EXCEPTION_FORM, expForm);
        return modelAndView;
    }

    /**
     * 根据异常信息创建手续费分账实体类
     *
     * @param form
     * @return
     */
    private PoundageBean createPoundageBean(PoundageExceptionBean form) {
        PoundageBean poundageBean = new PoundageBean();
        poundageBean.setId(form.getPoundageId());// 手续费分账id
        poundageBean.setUserId(form.getUserId());// 收款方用户id
        poundageBean.setUserName(form.getUserName());// 收款方用户名
        poundageBean.setRealName(form.getRealName());// 收款方姓名
        poundageBean.setAmount(form.getLedgerAmount());// 分账总金额
        poundageBean.setAccount(form.getAccount());// 收款方江西银行电子账号
        poundageBean.setAccountId(form.getAccountId());// 转出方用户电子账户号
        poundageBean.setBalance(form.getBalance());// 余额
        poundageBean.setPassword(form.getPassword());// 密码
        poundageBean.setNid(form.getNid());// 订单号
        poundageBean.setSeqNo(form.getSeqNo());// 请求流水号
        poundageBean.setTxDate(form.getTxDate());
        poundageBean.setTxTime(form.getTxTime());
        poundageBean.setAddTime(form.getUpdateTime());//分账时间
        poundageBean.setUpdater(form.getUpdater());
        poundageBean.setUpdateTime(form.getUpdateTime());
        return poundageBean;
    }

}
