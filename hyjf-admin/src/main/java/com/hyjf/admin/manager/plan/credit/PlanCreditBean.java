package com.hyjf.admin.manager.plan.credit;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class PlanCreditBean extends DebtCreditCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;
	
	/**
	 * 转让状态
	 */
	private String creditStatus;
	
	/**
	 * 还款状态
	 */
	private String repayStatus;
	
	/**
	 * 检索条件 项目还款方式
	 */
	private String repayStyle;
	
	/**
	 * 检索条件 清算时间开始
	 */
	private String liquidatesTimeStart;

	/**
	 * 检索条件 清算时间结束
	 */
	private String liquidatesTimeEnd;
	
	/**
	 * 检索条件 还款时间开始
	 */
	private String repayNextTimeStart;

	/**
	 * 检索条件 还款时间结束
	 */
	private String repayNextTimeEnd;
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;
	
	
	public String getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getLiquidatesTimeStart() {
		return liquidatesTimeStart;
	}

	public void setLiquidatesTimeStart(String liquidatesTimeStart) {
		this.liquidatesTimeStart = liquidatesTimeStart;
	}

	public String getLiquidatesTimeEnd() {
		return liquidatesTimeEnd;
	}

	public void setLiquidatesTimeEnd(String liquidatesTimeEnd) {
		this.liquidatesTimeEnd = liquidatesTimeEnd;
	}

	public String getRepayNextTimeStart() {
		return repayNextTimeStart;
	}

	public void setRepayNextTimeStart(String repayNextTimeStart) {
		this.repayNextTimeStart = repayNextTimeStart;
	}

	public String getRepayNextTimeEnd() {
		return repayNextTimeEnd;
	}

	public void setRepayNextTimeEnd(String repayNextTimeEnd) {
		this.repayNextTimeEnd = repayNextTimeEnd;
	}

	public String getRepayStyle() {
		return repayStyle;
	}

	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
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

}
