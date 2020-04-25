package com.hyjf.admin.manager.debt.debtborrowapply;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.Loan;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class DebtBorrowApplyBean implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名 检索条件
	 */
	private String nameSrch;
	/**
	 * 审核状态 检索条件
	 */
	private String stateSrch;
	/**
	 * 申请时间 检索条件
	 */
	private String timeStartSrch;
	/**
	 * 申请时间 检索条件
	 */
	private String timeEndSrch;
	/**
	 * ID
	 */
	private String id;
	/**
	 * 审核状态
	 */
	private String state;
	/**
	 * 审核时间
	 */
	private String addtime;
	/**
	 * 审核备注
	 */
	private String remark;
	/**
	 * 申请借款信息表
	 */
	private Loan loan;

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
	 * addtime
	 * @return the addtime
	 */
	
	public String getAddtime() {
		return addtime;
	}

	/**
	 * @param addtime the addtime to set
	 */
	
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	/**
	 * remark
	 * 
	 * @return the remark
	 */

	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * loan
	 * 
	 * @return the loan
	 */

	public Loan getLoan() {
		return loan;
	}

	/**
	 * @param loan
	 *            the loan to set
	 */

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	/**
	 * nameSrch
	 * 
	 * @return the nameSrch
	 */

	public String getNameSrch() {
		return nameSrch;
	}

	/**
	 * @param nameSrch
	 *            the nameSrch to set
	 */

	public void setNameSrch(String nameSrch) {
		this.nameSrch = nameSrch;
	}

	/**
	 * stateSrch
	 * 
	 * @return the stateSrch
	 */

	public String getStateSrch() {
		return stateSrch;
	}

	/**
	 * @param stateSrch
	 *            the stateSrch to set
	 */

	public void setStateSrch(String stateSrch) {
		this.stateSrch = stateSrch;
	}

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

	/**
	 * id
	 * 
	 * @return the id
	 */

	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * state
	 * 
	 * @return the state
	 */

	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */

	public void setState(String state) {
		this.state = state;
	}
}
