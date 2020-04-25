package com.hyjf.mybatis.model.customize.admin.htj;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class DebtBorrowExceptionDeleteSrchBean implements Serializable {

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
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;
	
	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

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

	/**
	 * moveFlag
	 * 
	 * @return the moveFlag
	 */

	public String getMoveFlag() {
		return moveFlag;
	}

	/**
	 * @param moveFlag
	 *            the moveFlag to set
	 */

	public void setMoveFlag(String moveFlag) {
		this.moveFlag = moveFlag;
	}

	/**
	 * serialversionuid
	 * 
	 * @return the serialversionuid
	 */

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * sort
	 * 
	 * @return the sort
	 */

	public String getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * col
	 * 
	 * @return the col
	 */

	public String getCol() {
		return col;
	}

	/**
	 * @param col
	 *            the col to set
	 */

	public void setCol(String col) {
		this.col = col;
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


	/*借款的识别名*/
	private String borrow_nidSrch;
	/*标题*/
	private String borrow_nameSrch;
	/*操作人名称*/
	private String operater_userSrch;
	/*删除开始时间*/
	private String operater_time_startSrch;
	/*删除结束时间*/
	private String operater_time_endSrch;

	public String getBorrow_nidSrch() {
		return borrow_nidSrch;
	}

	public void setBorrow_nidSrch(String borrow_nidSrch) {
		this.borrow_nidSrch = borrow_nidSrch;
	}

	public String getBorrow_nameSrch() {
		return borrow_nameSrch;
	}

	public void setBorrow_nameSrch(String borrow_nameSrch) {
		this.borrow_nameSrch = borrow_nameSrch;
	}

	public String getOperater_userSrch() {
		return operater_userSrch;
	}

	public void setOperater_userSrch(String operater_userSrch) {
		this.operater_userSrch = operater_userSrch;
	}

	public String getOperater_time_startSrch() {
		return operater_time_startSrch;
	}

	public void setOperater_time_startSrch(String operater_time_startSrch) {
		this.operater_time_startSrch = operater_time_startSrch;
	}

	public String getOperater_time_endSrch() {
		return operater_time_endSrch;
	}

	public void setOperater_time_endSrch(String operater_time_endSrch) {
		this.operater_time_endSrch = operater_time_endSrch;
	}
}
