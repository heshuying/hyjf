package com.hyjf.admin.manager.debt.debtborrowrepayment;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRepaymentCustomize;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class DebtBorrowRepaymentBean extends DebtBorrowRepaymentCustomize implements Serializable {

	/**
	 * serialVersionUID:TODO 这个变量是干什么的
	 */

	private static final long serialVersionUID = 1L;

	private String afterDay;

	private String password;

	private String borrowNidHidden;// 项目编号
	
	private String borrowPeriod;// 借款期限

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * password
	 *
	 * @return the password
	 */

	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * afterDay
	 *
	 * @return the afterDay
	 */

	public String getAfterDay() {
		return afterDay;
	}

	/**
	 * @param afterDay
	 *            the afterDay to set
	 */

	public void setAfterDay(String afterDay) {
		this.afterDay = afterDay;
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

    public String getBorrowNidHidden() {
        return borrowNidHidden;
    }

    public void setBorrowNidHidden(String borrowNidHidden) {
        this.borrowNidHidden = borrowNidHidden;
    }

	public String getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(String borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}
    
}
