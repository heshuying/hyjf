package com.hyjf.admin.finance.poundage;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.admin.finance.subcommission.SubCommissionBean;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import org.springframework.web.servlet.ModelAndView;

public interface PoundageService extends BaseService {
    /**
     * 查询数量
     *
     * @param poundageCustomize
     * @return
     */
    public Integer getPoundageCount(PoundageCustomize poundageCustomize);

    /**
     * 查询信息
     *
     * @param poundageCustomize
     * @return
     */
    public List<PoundageCustomize> getPoundageList(PoundageCustomize poundageCustomize);

    /**
     * 修改信息
     *
     * @param poundageCustomize
     * @return
     */
    public void updatePoundage(PoundageCustomize poundageCustomize);

    /**
     * 获取单条信息
     *
     * @param id
     * @return
     */
    public PoundageCustomize getPoundageById(int id);

    /**
     * 检验参数
     *
     * @param modelAndView
     * @param form
     */
    void checkTransferParam(ModelAndView modelAndView, PoundageBean form);

    /**
     * 进行分账，获取结果
     *
     * @param form
     * @return
     * @author wgx
     */
    public BankCallBean getLedgerResult(PoundageBean form);

    /**
     * 分账失败后,后续账户信息操作
     *
     * @param resultBean
     * @param form
     * @return
     * @author wgx
     */
    public boolean updateAfterLedgerFail(BankCallBean resultBean, PoundageBean form);

    /**
     * 分账成功后,后续账户信息操作
     *
     * @param resultBean
     * @param form
     * @return
     * @author wgx
     */
    public boolean updateAfterLedgerSuccess(BankCallBean resultBean, PoundageBean form);

    /**
     * 获取分账状态
     *
     * @param bean
     * @return
     * @author wgx
     */
    public BankCallBean checkLedgerResult(BankCallBean bean);

    /**
     * 获取交易时间
     *
     * @param bankCallBean
     * @return
     * @author wgx
     */
    public int getAddTime(BankCallBean bankCallBean);

    /**
     * 统计总分账金额和总分账笔数
     * @return
     * @author wgx
     */
    public PoundageCustomize getPoundageSum(PoundageCustomize entity);

    /**
     * 更新银行返回信息
     * @param form
     * @param resultBean
     */
    public void addBankCall(PoundageBean form, BankCallBean resultBean);
}
