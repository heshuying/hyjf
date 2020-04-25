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

import java.text.ParseException;

import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.web.BaseService;
import com.hyjf.web.user.repay.UserRepayProjectBean;

/**
 * 计划专属标的借款用户还款接口
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月10日
 * @see 上午10:13:22
 */
public interface PlanUserRepayService extends BaseService {

    /**
     * 
     * 计算实际还款信息
     * @author renxingchen
     * @param userId
     * @param borrow
     * @return
     */
    DebtRepayByTermBean calculateRepay(Integer userId, DebtBorrow borrow) throws ParseException;

    /**
     * 
     * 查询实际还款项目
     * @author renxingchen
     * @param userId
     * @param borrowNid
     * @return
     */
    DebtBorrow searchDebtBorrowProject(Integer userId, String borrowNid);

    /**
     * 
     * 更新还款信息
     * @author renxingchen
     * @param repay
     * @return
     */
    boolean updateRepayMoney(DebtRepayByTermBean repay);

    /**
     * 
     * 查询用户还款详情
     * @author renxingchen
     * @param form
     * @return
     */
    UserRepayProjectBean searchRepayProjectDetail(UserRepayProjectBean form) throws NumberFormatException,
        ParseException;


}
