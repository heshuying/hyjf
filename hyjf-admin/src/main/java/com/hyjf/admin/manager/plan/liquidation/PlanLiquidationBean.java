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

package com.hyjf.admin.manager.plan.liquidation;

import com.hyjf.common.paginator.Paginator;

/**
 * 计划清算列表查询表单实体
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年11月1日
 * @see 下午5:22:19
 */
public class PlanLiquidationBean {

    /**
     * 排序
     */
    private String sort;

    /**
     * 排序列
     */
    private String col;

    /**
     * 检索条件 画面迁移标识
     */
    private String moveFlag;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    /**
     * 检索条件 计划编号
     */
    private String planNidSrch;

    /**
     * 检索条件 状态 0 发起中；1
     * 待审核；2审核不通过；3待开放；4募集中；5募集完成；6锁定中；7清算中；8清算完成，9还款，10还款中，11还款完成
     */
    private String planStatusSrch;

    /**
     * 检索条件 计划应清算起始时间
     */
    private String liquidateShouldTimeStartSrch;

    /**
     * 检索条件 计划应清算结束时间
     */
    private String liquidateShouldTimeEndSrch;

    /**
     * 检索条件 计划实际清算起始时间
     */
    private String liquidateFactTimeStartSrch;

    /**
     * 检索条件 计划实际清算结束时间
     */
    private String liquidateFactTimeEndSrch;
    /**
     * 检索条件 订单号
     */
    private String planOrderIdSrh;
    /**
     * 检索条件 用户名
     */
    private String userName;

    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;

    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getMoveFlag() {
        return moveFlag;
    }

    public void setMoveFlag(String moveFlag) {
        this.moveFlag = moveFlag;
    }

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public String getPlanNidSrch() {
        return planNidSrch;
    }

    public void setPlanNidSrch(String planNidSrch) {
        this.planNidSrch = planNidSrch;
    }

    public String getPlanStatusSrch() {
        return planStatusSrch;
    }

    public void setPlanStatusSrch(String planStatusSrch) {
        this.planStatusSrch = planStatusSrch;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getLiquidateShouldTimeStartSrch() {
        return liquidateShouldTimeStartSrch;
    }

    public void setLiquidateShouldTimeStartSrch(String liquidateShouldTimeStartSrch) {
        this.liquidateShouldTimeStartSrch = liquidateShouldTimeStartSrch;
    }

    public String getLiquidateShouldTimeEndSrch() {
        return liquidateShouldTimeEndSrch;
    }

    public void setLiquidateShouldTimeEndSrch(String liquidateShouldTimeEndSrch) {
        this.liquidateShouldTimeEndSrch = liquidateShouldTimeEndSrch;
    }

    public String getLiquidateFactTimeStartSrch() {
        return liquidateFactTimeStartSrch;
    }

    public void setLiquidateFactTimeStartSrch(String liquidateFactTimeStartSrch) {
        this.liquidateFactTimeStartSrch = liquidateFactTimeStartSrch;
    }

    public String getLiquidateFactTimeEndSrch() {
        return liquidateFactTimeEndSrch;
    }

    public void setLiquidateFactTimeEndSrch(String liquidateFactTimeEndSrch) {
        this.liquidateFactTimeEndSrch = liquidateFactTimeEndSrch;
    }

	public String getPlanOrderIdSrh() {
		return planOrderIdSrh;
	}

	public void setPlanOrderIdSrh(String planOrderIdSrh) {
		this.planOrderIdSrh = planOrderIdSrh;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
