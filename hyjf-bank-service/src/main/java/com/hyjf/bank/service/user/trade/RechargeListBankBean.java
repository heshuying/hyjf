/**
 * Description：用户充值前台所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.user.trade;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.WebUserRechargeListCustomize;

public class RechargeListBankBean extends WebUserRechargeListCustomize implements Serializable {

	/**
	 * serialVersionUID:TODO 这个变量是干什么的
	 */
		
	private static final long serialVersionUID = -204409531346911817L;
	// 用户id
	public String userId;
	// 收益结束值
	public String startDate;
	// 期限开始值
	public String endDate;
	// 请求处理是否成功
    private boolean status = false;
    // 充值明细
    private List<WebUserRechargeListCustomize> rechargeList;
    // 数组类型
    public String listType;
    

    
    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public void success() {
        this.status = true;
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

	public RechargeListBankBean() {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<WebUserRechargeListCustomize> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<WebUserRechargeListCustomize> rechargeList) {
        this.rechargeList = rechargeList;
    }

}
