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
import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;

/**
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月11日
 * @see 下午3:26:49
 */
public class DebtRepayDetailLoanDetailBean extends DebtRepayDetail implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1401705163289413099L;

    /** 用户还款详情 */
    private List<DebtLoanDetail> loanDetailList = new ArrayList<DebtLoanDetail>();

    public DebtRepayDetailLoanDetailBean() {
        super();
    }

    public List<DebtLoanDetail> getLoanDetailList() {
        return loanDetailList;
    }

    public void setLoanDetailList(List<DebtLoanDetail> loanDetailList) {
        this.loanDetailList = loanDetailList;
    }

}
