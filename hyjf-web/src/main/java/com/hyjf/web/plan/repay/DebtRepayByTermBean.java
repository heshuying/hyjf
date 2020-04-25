/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 * 
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑 永无BUG
 */

package com.hyjf.web.plan.repay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtRepay;

/**
 * 汇添金实际还款类
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月10日
 * @see 下午3:24:17
 */
public class DebtRepayByTermBean extends DebtRepay implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -5297562144743567019L;

    // 还款所需总额
    public BigDecimal repayTotal;

    // 当前还款期数
    public String borrowPeriod;

    // 还款用户的ip地址
    private String ip;

    // 放款列表
    private List<DebtLoan> loanList = new ArrayList<DebtLoan>();

    // 分期放款列表
    private List<DebtRepayDetailLoanDetailBean> repayDetailList = new ArrayList<DebtRepayDetailLoanDetailBean>();

    public DebtRepayByTermBean() {
        super();
    }

    public String getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(String borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<DebtLoan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<DebtLoan> loanList) {
        this.loanList = loanList;
    }

    public List<DebtRepayDetailLoanDetailBean> getRepayDetailList() {
        return repayDetailList;
    }

    public void setRepayDetailList(List<DebtRepayDetailLoanDetailBean> repayDetailList) {
        this.repayDetailList = repayDetailList;
    }

    public BigDecimal getRepayTotal() {
        return repayTotal;
    }

    public void setRepayTotal(BigDecimal repayTotal) {
        this.repayTotal = repayTotal;
    }

}
