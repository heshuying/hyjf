package com.hyjf.web.user.trade;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.WebUserTradeListCustomize;

public class TradeListBean extends WebUserTradeListCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3458197418404401541L;
	
	/**
     * 交易明细
     */
	private List<WebUserTradeListCustomize> tradeList;
	// 请求处理是否成功
    private boolean status = false;
	// 收益结束值
	public String startDate;
	// 期限开始值
	public String endDate;
	// 用户id
	public String userId;
	// 数组类型
    public String listType;
    
	public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    /**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 翻页功能所用分页大小
	 */
	private int pageSize = 10;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	public void success() {
        this.status = true;
    }

	public TradeListBean() {
		super();
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

	public int getPageSize() {
		if (pageSize == 0) {
			pageSize = 10;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    public List<WebUserTradeListCustomize> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<WebUserTradeListCustomize> tradeList) {
        this.tradeList = tradeList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
