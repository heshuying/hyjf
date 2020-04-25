package com.hyjf.admin.promotion.tenderdetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CreateUUID;
import com.hyjf.mybatis.model.customize.admin.UserTenderDetailCustomize;

/**
 * 投之家用户出借明细
 * @author zhangjinpeng
 *
 */
public class UserTenderDetailBean extends UserTenderDetailCustomize implements Serializable {

	/**
     * serialVersionUID
     */
	private static final long serialVersionUID = 1790601499725213969L;

	private String primaryKey = CreateUUID.getUUID();
    
    private BigDecimal tenderAccountTotal;
    
    private List<UserTenderDetailCustomize> recordList;

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

    public List<UserTenderDetailCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<UserTenderDetailCustomize> recordList) {
        this.recordList = recordList;
    }

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public BigDecimal getTenderAccountTotal() {
		return tenderAccountTotal;
	}

	public void setTenderAccountTotal(BigDecimal tenderAccountTotal) {
		this.tenderAccountTotal = tenderAccountTotal;
	}

}
