package com.hyjf.admin.exception.repayexception;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.RepayExceptionCustomize;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class RepayExceptionBean extends RepayExceptionCustomize implements Serializable {

	private static final long serialVersionUID = 1L;

	private String afterDay;

	private String password;

	private String borrowNidHidden;// 项目编号

	private Integer periodNowHidden; // 当前期数

    private String borrowNidParam;// 项目编号

    private Integer borrowPeriodParam; // 当前期数

    private String monthTypeHidden; // 是否分期

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

    public Integer getPeriodNowHidden() {
        return periodNowHidden;
    }

    public void setPeriodNowHidden(Integer periodNowHidden) {
        this.periodNowHidden = periodNowHidden;
    }

    public String getBorrowNidParam() {
        return borrowNidParam;
    }

    public void setBorrowNidParam(String borrowNidParam) {
        this.borrowNidParam = borrowNidParam;
    }

    public Integer getBorrowPeriodParam() {
        return borrowPeriodParam;
    }

    public void setBorrowPeriodParam(Integer borrowPeriodParam) {
        this.borrowPeriodParam = borrowPeriodParam;
    }

    public String getMonthTypeHidden() {
        return monthTypeHidden;
    }

    public void setMonthTypeHidden(String monthTypeHidden) {
        this.monthTypeHidden = monthTypeHidden;
    }
}
