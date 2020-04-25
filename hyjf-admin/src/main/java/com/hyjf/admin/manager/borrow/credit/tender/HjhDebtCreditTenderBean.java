package com.hyjf.admin.manager.borrow.credit.tender;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditTenderCustomize;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class HjhDebtCreditTenderBean extends HjhDebtCreditTenderCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 387630498860089653L;
	
	/**
	 * 检索条件 项目还款方式
	 */
	private String repayStyle;
	
	
	/**
	 * 检索条件 债权承接方式
	 */
	private String assignType;

	/**
	 * 检索条件 承接时间开始
	 */
	private String assignTimeStart;
	
	/**
	 * 检索条件 承接时间开始
	 */
	private String assignTimeEnd;
	
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
	
	/**
     * 是否从加入明细列表跳转 1:是 0:否
     */
    private int isAccedelist = 0;

	/**
	 * 是否从债转标的页面调转(1:是)
	 */
	private String isOptFlag;

	/**
	 * 传参数用
	 */
	private  String planNidTemp;

	private  String isSearch;

	/**
	 * 构造方法
	 */
		
	public HjhDebtCreditTenderBean() {
		super();
	}
	
	public int getIsAccedelist() {
        return isAccedelist;
    }

    public void setIsAccedelist(int isAccedelist) {
        this.isAccedelist = isAccedelist;
    }

    /**
	 * limitStart
	 * 
	 * @return the limitStart
	 */
	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */

	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */

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

	public String getAssignTimeStart() {
		return assignTimeStart;
	}

	public void setAssignTimeStart(String assignTimeStart) {
		this.assignTimeStart = assignTimeStart;
	}

	public String getAssignTimeEnd() {
		return assignTimeEnd;
	}

	public void setAssignTimeEnd(String assignTimeEnd) {
		this.assignTimeEnd = assignTimeEnd;
	}

	public String getRepayStyle() {
		return repayStyle;
	}

	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}

	public String getAssignType() {
		return assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getIsOptFlag() {
		return isOptFlag;
	}

	public void setIsOptFlag(String isOptFlag) {
		this.isOptFlag = isOptFlag;
	}

	public String getPlanNidTemp() {
		return planNidTemp;
	}

	public void setPlanNidTemp(String planNidTemp) {
		this.planNidTemp = planNidTemp;
	}

	public String getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}
}
