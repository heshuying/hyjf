package com.hyjf.admin.finance.poundage;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.hyjf.admin.finance.web.WebService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageExceptionCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;
import org.springframework.web.servlet.ModelAndView;

@Service
public class PoundageServiceImpl extends BaseServiceImpl implements PoundageService {

    Logger _log = LoggerFactory.getLogger(PoundageServiceImpl.class);

    @Autowired
    private WebService webService;

    /**
     * 查询数量
     *
     * @param poundageCustomize
     * @return
     */
    @Override
    public Integer getPoundageCount(PoundageCustomize poundageCustomize) {
        Integer count = this.poundageCustomizeMapper.getPoundageCount(poundageCustomize);
        return count;
    }

    /**
     * 查询信息
     *
     * @param poundageCustomize
     * @return
     */
    @Override
    public List<PoundageCustomize> getPoundageList(PoundageCustomize poundageCustomize) {
        List<PoundageCustomize> list = this.poundageCustomizeMapper.getPoundageList(poundageCustomize);
        return list;
    }

    /**
     * 修改信息
     *
     * @param poundageCustomize
     * @return
     */
    public void updatePoundage(PoundageCustomize poundageCustomize) {
        this.poundageCustomizeMapper.updatePoundage(poundageCustomize);
    }

    /**
     * 获取单条信息
     *
     * @param id
     * @return
     */
    @Override
    public PoundageCustomize getPoundageById(int id) {
        return this.poundageCustomizeMapper.getPoundageById(id);
    }

    /**
     * 检验参数
     * 优先显示账户余额不足,其次显示交易密码错误
     *
     * @param modelAndView
     * @param form
     * @author wgx
     */
    @Override
    public void checkTransferParam(ModelAndView modelAndView, PoundageBean form) {
        if (new BigDecimal(form.getBalance()).compareTo(form.getAmount()) < 0) {
            ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "error", "feeshare.transfer.amount.error", "账户余额不足");
        } else {
            String password = CustomConstants.SUB_COMMISSION_PASSWORD;
            if (!password.equals(MD5Utils.MD5(form.getPassword()))) {
                _log.info("交易密码错误");
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "error", "feeshare.transfer.password.error", "交易密码错误");
            }
        }
    }

    /**
     * 进行分账，获取结果
     *
     * @param form
     * @return
     * @author wgx
     */
    @Override
    public BankCallBean getLedgerResult(PoundageBean form) {
        // 调用江西银行接口分佣
        Integer loginUserId = Integer.parseInt(ShiroUtil.getLoginUserId());// 登陆用户ID
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 版本号
        bean.setTxCode(BankCallConstant.TXCODE_FEE_SHARE);// 交易代码
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
        bean.setSeqNo(StringUtils.isEmpty(form.getSeqNo()) ? GetOrderIdUtils.getSeqNo(6) : form.getSeqNo());// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(form.getAccountId());// 电子账号
        bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB);// 币种
        bean.setTxAmount(CustomUtil.formatAmount(form.getAmount().toString())); // 分账金额
        bean.setForAccountId(form.getAccount());// 对手电子账号
        bean.setDescription("手续费分账");
        bean.setLogOrderId(StringUtils.isEmpty(form.getNid()) ? GetOrderIdUtils.getOrderId2(loginUserId) : form.getNid());
        bean.setLogUserId(String.valueOf(loginUserId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        bean.setLogRemark("手续费分账");
        BankCallBean resultBean = BankCallUtils.callApiBg(bean);
        if (resultBean == null) {
            resultBean = new BankCallBean();
            resultBean.setLogOrderId(bean.getLogOrderId());
            return resultBean;
        }
        return resultBean;
    }

    /**
     * 分账失败后,后续账户信息操作
     *
     * @param resultBean
     * @param form
     * @return
     * @author wgx
     */
    @Override
    public boolean updateAfterLedgerFail(BankCallBean resultBean, PoundageBean form) {
        // 修改分账状态
        updatePoundage(form);
        // 插入分账失败信息
        insertPoundageLedgerException(resultBean, form);
        return true;
    }

    /**
     * 插入分账失败信息 项目编号和放款/还款时间应该是明细的字段
     *
     * @param resultBean
     * @param form
     * @author wgx
     */
    public void insertPoundageLedgerException(BankCallBean resultBean, PoundageBean form) {
        if (!isExist(resultBean)) {
            PoundageExceptionCustomize poundageExceptionCustomize = new PoundageExceptionCustomize();
            poundageExceptionCustomize.setLedgerAmount(form.getAmount());//分账金额
            poundageExceptionCustomize.setLedgerId(form.getLedgerId());//手续费分账配置id
            poundageExceptionCustomize.setPoundageId(form.getId());//手续费分账id
            poundageExceptionCustomize.setPayeeName(form.getUserName());//收款人用户名
            poundageExceptionCustomize.setLedgerStatus(0);//分账状态:0.未分账;1.已分账
            poundageExceptionCustomize.setCreateTime(GetDate.getNowTime10());//创建时间
            poundageExceptionCustomizeMapper.insertPoundageException(poundageExceptionCustomize);
        }
    }

    /**
     * 根据订单号查询失败信息是否存在
     *
     * @param resultBean
     * @return
     */
    private boolean isExist(BankCallBean resultBean) {
        PoundageExceptionCustomize poundageExceptionCustomize = new PoundageExceptionCustomize();
        poundageExceptionCustomize.setNidSer(resultBean.getLogOrderId());
        return poundageExceptionCustomizeMapper.getPoundageExceptionCount(poundageExceptionCustomize) > 0;
    }

    /**
     * 分账成功后,后续账户信息操作
     *
     * @param resultBean
     * @param form
     * @return
     * @author wgx
     */
    @Override
    public boolean updateAfterLedgerSuccess(BankCallBean resultBean, PoundageBean form) {
        Integer nowTime = GetDate.getNowTime10();
        // 转入用户ID
        Integer receiveUserId = form.getUserId();
        // 更新转入用户账户信息
        updateOfLedgerIn(resultBean, form);
        // 插入交易明细
        insertAccountList(resultBean, nowTime, receiveUserId);
        //插入网站收支记录
        insertAccountWebList(resultBean, nowTime, receiveUserId);
        // 插入手续费账户明细
        insertMerchantAccountList(resultBean, receiveUserId);
        return true;
    }

    /**
     * 插入手续费账户明细
     *
     * @param resultBean
     * @param receiveUserId
     * @author wgx
     */
    private void insertMerchantAccountList(BankCallBean resultBean, Integer receiveUserId) {
        try {
            // 根据用户id查询用户情报
            UserInfoCustomize userInfoCustomize = userInfoCustomizeMapper.queryUserInfoByUserId(Integer.parseInt(resultBean.getLogUserId()));
            // 创建手续费账户明细
            BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
            bankMerchantAccountList.setOrderId(resultBean.getLogOrderId());// 订单号
            bankMerchantAccountList.setBorrowNid("");// 项目编号
            bankMerchantAccountList.setUserId(receiveUserId);// 对方用户ID
            bankMerchantAccountList.setAccountId(resultBean.getForAccountId());// 对方电子账号
            bankMerchantAccountList.setAmount(new BigDecimal(resultBean.getTxAmount()));// 操作金额
            bankMerchantAccountList.setBankAccountCode(resultBean.getAccountId());// 商户子账户电子账号(转出账号)
            //bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAvailableBalance());// 银行账户可用金额
            //bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());// 银行账户冻结金额
            bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_MANUAL_LEDGER);// 交易类型 2分账
            bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);// 收支类型 1支出
            bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);// 交易状态0: 失败 1：成功 2:处理中
            bankMerchantAccountList.setTxDate(Integer.parseInt(resultBean.getTxDate())); // 交易日期
            bankMerchantAccountList.setTxTime(Integer.parseInt(resultBean.getTxTime()));// 交易时间
            bankMerchantAccountList.setSeqNo(resultBean.getSeqNo() + "");// 流水号
            bankMerchantAccountList.setCreateTime(new Date());// 添加时间
            //bankMerchantAccountList.setUpdateTime(new Date());// 更新时间
            bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());// 分公司
            bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());// 分部
            bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());// 团队
            bankMerchantAccountList.setCreateUserId(Integer.parseInt(resultBean.getLogUserId()));// 创建用户id
            //bankMerchantAccountList.setUpdateUserId(bankOpenAccount.getUserId());// 更新用户id
            bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());// 创建用户名
            //bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());// 更新用户名
            bankMerchantAccountList.setRemark("");// 备注字段
            // 生成交易明细
            this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        } catch (Exception e) {
            throw new RuntimeException("手续费账户明细(hyjf_bank_merchant_account_list)插入失败！" + "[订单号：" + resultBean.getLogOrderId() + "]");
        }
    }

    /**
     * 插入交易明细
     *
     * @param resultBean
     * @param nowTime
     * @param receiveUserId
     * @author wgx
     */
    private void insertAccountList(BankCallBean resultBean, Integer nowTime, Integer receiveUserId) {
        // 转账订单号
        String orderId = resultBean.getLogOrderId();
        // 交易金额
        String txAmount = resultBean.getTxAmount();
        // 交易日期
        String txDate = resultBean.getTxDate();
        // 交易时间
        String txTime = resultBean.getTxTime();
        // 交易流水号
        String seqNo = resultBean.getSeqNo();
        Account receiveUserAccount = this.getAccountByUserId(receiveUserId);
        AccountList receiveUserList = new AccountList();
        receiveUserList.setNid(orderId); // 订单号
        receiveUserList.setUserId(receiveUserId); // 转入人用户ID
        receiveUserList.setAmount(new BigDecimal(txAmount)); // 操作金额
        /** 银行相关 */
        receiveUserList.setAccountId(resultBean.getForAccountId());
        receiveUserList.setBankAwait(receiveUserAccount.getBankAwait());
        receiveUserList.setBankAwaitCapital(receiveUserAccount.getBankAwaitCapital());
        receiveUserList.setBankAwaitInterest(receiveUserAccount.getBankAwaitInterest());
        receiveUserList.setBankBalance(receiveUserAccount.getBankBalance());
        receiveUserList.setBankFrost(receiveUserAccount.getBankFrost());
        receiveUserList.setBankInterestSum(receiveUserAccount.getBankInterestSum());
        receiveUserList.setBankInvestSum(receiveUserAccount.getBankInvestSum());
        receiveUserList.setBankTotal(receiveUserAccount.getBankTotal());
        receiveUserList.setBankWaitCapital(receiveUserAccount.getBankWaitCapital());
        receiveUserList.setBankWaitInterest(receiveUserAccount.getBankWaitInterest());
        receiveUserList.setBankWaitRepay(receiveUserAccount.getBankWaitRepay());
        receiveUserList.setCheckStatus(0);
        receiveUserList.setTradeStatus(1);// 交易状态 0:失败 1:成功
        receiveUserList.setIsBank(1);
        receiveUserList.setTxDate(Integer.parseInt(txDate));
        receiveUserList.setTxTime(Integer.parseInt(txTime));
        receiveUserList.setSeqNo(seqNo);
        receiveUserList.setBankSeqNo(txDate + txTime + seqNo);
        /** 非银行相关 */
        receiveUserList.setType(1); // 1收入
        receiveUserList.setTrade(CustomConstants.TRADE_LEDGER_IN); // 手续费分账转入
        receiveUserList.setTradeCode("balance"); // 余额操作
        receiveUserList.setTotal(receiveUserAccount.getTotal()); // 出借人资金总额
        receiveUserList.setBalance(receiveUserAccount.getBalance()); // 出借人可用金额
        receiveUserList.setPlanFrost(receiveUserAccount.getPlanFrost());// 汇添金冻结金额
        receiveUserList.setPlanBalance(receiveUserAccount.getPlanBalance());// 汇添金可用金额
        receiveUserList.setFrost(receiveUserAccount.getFrost()); // 出借人冻结金额
        receiveUserList.setAwait(receiveUserAccount.getAwait()); // 出借人待收金额
        receiveUserList.setCreateTime(nowTime); // 创建时间
        receiveUserList.setBaseUpdate(nowTime); // 更新时间
        receiveUserList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
        receiveUserList.setRemark(CustomConstants.TRADE_LEDGER_REMARK);
        receiveUserList.setIp(""); // 操作IP
        receiveUserList.setIsUpdate(0);
        receiveUserList.setBaseUpdate(0);
        receiveUserList.setInterest(BigDecimal.ZERO); // 利息
        receiveUserList.setWeb(0); // PC
        boolean receiveUserListFlag = this.accountListMapper.insertSelective(receiveUserList) > 0 ? true : false;
        if (!receiveUserListFlag) {
            _log.info("插入转入用户交易记录失败,用户ID:[" + receiveUserId + "],订单号:[" + orderId + "].");
            throw new RuntimeException("插入转出用户交易记录失败,用户ID:[" + receiveUserId + "],订单号:[" + orderId + "].");
        }
    }

    /**
     * 插入网站收支记录
     */
    private void insertAccountWebList(BankCallBean resultBean, Integer nowTime, Integer receiveUserId) {
        // 订单号
        String orderId = resultBean.getLogOrderId();
        // 插入网站收支明细记录
        AccountWebList accountWebList = new AccountWebList();
        accountWebList.setOrdid(orderId);// 订单号
        accountWebList.setBorrowNid(""); // 出借编号
        accountWebList.setUserId(receiveUserId); //
        accountWebList.setAmount(new BigDecimal(resultBean.getTxAmount())); // 管理费
        accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入,2支出
        accountWebList.setTrade(CustomConstants.TRADE_LEDGER_OUT); // 交易类型值
        accountWebList.setTradeType(CustomConstants.TRADE_LEDGER_REMARK); // 交易类型
        accountWebList.setRemark(""); // 备注
        accountWebList.setCreateTime(nowTime);
        accountWebList.setOperator(ShiroUtil.getLoginUsername());
        int result = webService.insertAccountWebList(accountWebList);
        if (result == -1) {
            throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)已存在!" + "[出借订单号：" + orderId + "]");
        } else if (result < 0) {
            throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[订单号：" + orderId + "]");
        }
    }

    /**
     * 更新转入用户账户信息
     *
     * @param resultBean
     * @param form
     * @return
     * @author wgx
     */
    private void updateOfLedgerIn(BankCallBean resultBean, PoundageBean form) {
        // 转账订单号
        String orderId = resultBean.getLogOrderId();
        // 转入用户ID
        Integer receiveUserId = form.getUserId();
        Account receiveUserAccount = new Account();
        receiveUserAccount.setUserId(form.getUserId());
        receiveUserAccount.setBankTotal(new BigDecimal(resultBean.getTxAmount()));
        receiveUserAccount.setBankBalance(new BigDecimal(resultBean.getTxAmount()));
        boolean isUpdateFlag = this.adminAccountCustomizeMapper.updateOfSubCommissionTransferIn(receiveUserAccount) > 0 ? true : false;
        if (!isUpdateFlag) {
            _log.info("更新转入用户的账户信息失败,用户ID:[" + receiveUserId + "].订单号:[" + orderId + "].");
            throw new RuntimeException("更新转入用户的账户信息失败,用户ID:[" + receiveUserId + "].订单号:[" + orderId + "].");
        }
    }

    /**
     * 根据用户ID查询用户账户信息
     *
     * @param userId
     * @return
     */
    private Account getAccountByUserId(Integer userId) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andUserIdEqualTo(userId);
        List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
        if (listAccount != null && listAccount.size() > 0) {
            return listAccount.get(0);
        }
        return null;
    }

    /**
     * 获取分账状态
     *
     * @param callBean
     * @return bankCallBean.getTxState() S-成功 F-失败 N-交易不存在 Z-未知 D-待处理
     * bankCallBean.getFailMsg() 失败描述 txState为F时有效
     * @author wgx
     */
    @Override
    public BankCallBean checkLedgerResult(BankCallBean callBean) {
        if (callBean == null || Validator.isNull(callBean.getAccountId())
                || Validator.isNull(callBean.getOrderId())) {
            return null;
        }
        try {
            BankCallBean bean = new BankCallBean();
            bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_TRANSACTION_STATUS_QUERY);// 交易代码
            bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
            bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
            bean.setTxDate(GetOrderIdUtils.getOrderDate());// 交易日期
            bean.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
            bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
            bean.setAccountId(callBean.getAccountId());// 电子账号 若原交易有电子账号，必填
            bean.setReqType("2");// 查询类别 2-按订单号查询
            bean.setReqOrderId(callBean.getOrderId());// 查询订单号 reqType=2，必填
            bean.setReqTxCode(BankCallConstant.TXCODE_FEE_SHARE);
            bean.setLogOrderId(callBean.getLogOrderId());// 订单号
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            // 调用接口
            BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
            return bankCallBean;
        } catch (Exception e) {
            _log.info("获取分账状态发生异常:异常信息:[" + e.getMessage() + "].");
            return null;
        }
    }

    /**
     * 获取交易时间
     *
     * @param bankCallBean
     * @return
     * @author wgx
     */
    @Override
    public int getAddTime(BankCallBean bankCallBean) {
        try {
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String date = bankCallBean.getOrgTxDate();
            String time = bankCallBean.getOrgTxTime();
            String add = "";
            for (int i = 0; i < 6 - time.length(); i++) {
                add += "0";
            }
            Date addTime = format.parse(date + add + time);
            return (int) (addTime.getTime() / 1000);
        } catch (Exception e) {
            return GetDate.getNowTime10();
        }
    }

    /**
     * 统计总分账金额和总分账笔数
     *
     * @return
     * @author wgx
     */
    @Override
    public PoundageCustomize getPoundageSum(PoundageCustomize entity) {
        return this.poundageCustomizeMapper.getPoundageSum(entity);
    }

    /**
     * 更新银行返回信息
     *
     * @param form
     * @param resultBean
     */
    @Override
    public void addBankCall(PoundageBean form, BankCallBean resultBean) {
        form.setNid(resultBean.getLogOrderId());//交易凭证号
        form.setSeqNo(resultBean.getSeqNo());//请求流水号
        // 交易日期
        String txDate = resultBean.getTxDate();
        // 交易时间
        String txTime = resultBean.getTxTime();
        form.setTxDate(Integer.parseInt(txDate));
        form.setTxTime(Integer.parseInt(txTime));
    }

    /**
     * 重写根据电子账号查询用户在江西银行的可用余额方法
     * 返回为null的时候表示请求银行信息失败
     *
     * @param userId
     * @param accountId
     * @return
     */
    @Override
    public BigDecimal getBankBalance(Integer userId, String accountId) {
        // 账户可用余额
        BigDecimal balance = null;
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
        bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
        bean.setAccountId(accountId);// 电子账号
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)));// 订单号
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogClient(0);// 平台
        try {
            BankCallBean resultBean = BankCallUtils.callApiBg(bean);
            if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return balance;
    }
}
