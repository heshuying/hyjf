package com.hyjf.admin.manager.borrow.borrowlog;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class BorrowLoBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;
	
	/**
     * 检索条件 时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 时间结束
     */
    private String timeEndSrch;
    /**

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
	 * 检索条件 项目编号
	 */
	private String borrowNidSrch;

	/**
	 * 检索条件 状态
	 */
	private String statusSrch;

	/**
	 * 检索条件 修改类型
	 */
	private String typeSrch;

	
	/**
	 * 操作时间
	 */
	private String ontime;
	/**
	 * 操作人
	 */
	private String usernameSrch;
	
	
	/**
     * timeStartSrch
     * 
     * @return the timeStartSrch
     */

    public String getTimeStartSrch() {
        return timeStartSrch;
    }

    /**
     * @param timeStartSrch
     *            the timeStartSrch to set
     */

    public void setTimeStartSrch(String timeStartSrch) {
        this.timeStartSrch = timeStartSrch;
    }

    /**
     * timeEndSrch
     * 
     * @return the timeEndSrch
     */

    public String getTimeEndSrch() {
        return timeEndSrch;
    }

    /**
     * @param timeEndSrch
     *            the timeEndSrch to set
     */

    public void setTimeEndSrch(String timeEndSrch) {
        this.timeEndSrch = timeEndSrch;
    }
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
    public String getBorrowNidSrch() {
        return borrowNidSrch;
    }
    public void setBorrowNidSrch(String borrowNidSrch) {
        this.borrowNidSrch = borrowNidSrch;
    }
    public String getStatusSrch() {
        return statusSrch;
    }
    public void setStatusSrch(String statusSrch) {
        this.statusSrch = statusSrch;
    }
    public String getTypeSrch() {
        return typeSrch;
    }
    public void setTypeSrch(String typeSrch) {
        this.typeSrch = typeSrch;
    }
    public String getOntime() {
        return ontime;
    }
    public void setOntime(String ontime) {
        this.ontime = ontime;
    }
    public String getUsernameSrch() {
        return usernameSrch;
    }
    public void setUsernameSrch(String usernameSrch) {
        this.usernameSrch = usernameSrch;
    }

	
	
}
