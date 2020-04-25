package com.hyjf.web.coupon;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;

public class CouponListBean extends CouponUserListCustomize implements Serializable {

	private static final long serialVersionUID = 3278149257478770256L;

	// 用户id
	public String userId;
    // 请求处理是否成功
    private boolean status = false;
    //时间戳 加密用
    public String timestamp;
    
    private List<CouponUserListCustomize> couponUserList;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public CouponListBean() {
		super();
	}

    public void success() {
        this.status = true;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}



    public List<CouponUserListCustomize> getCouponUserList() {
        return couponUserList;
    }

    public void setCouponUserList(List<CouponUserListCustomize> couponUserList) {
        this.couponUserList = couponUserList;
    }



}
