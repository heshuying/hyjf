package com.hyjf.admin.manager.plan;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllplanCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class PlanBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

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
	 * 检索条件 计划标题
	 */
	private String planNameSrch;
	/**
	 * 检索条件 计划类型 “配置中心-汇添金配置”中所有计划类型（不区分启用禁用）
	 */
	private String planTypeSrch;

	/**
	 * 检索条件 状态 0 发起中；1
	 * 待审核；2审核不通过；3待开放；4募集中；5募集完成；6锁定中；7清算中；8清算完成，9还款，10还款中，11还款完成
	 */
	private String planStatusSrch;

	/**
	 * 检索条件 发起时间
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 发起时间
	 */
	private String timeEndSrch;

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

	public String getPlanNameSrch() {
		return planNameSrch;
	}

	public void setPlanNameSrch(String planNameSrch) {
		this.planNameSrch = planNameSrch;
	}

	public String getPlanTypeSrch() {
		return planTypeSrch;
	}

	public void setPlanTypeSrch(String planTypeSrch) {
		this.planTypeSrch = planTypeSrch;
	}

	public String getPlanStatusSrch() {
		return planStatusSrch;
	}

	public void setPlanStatusSrch(String planStatusSrch) {
		this.planStatusSrch = planStatusSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
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

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

}
