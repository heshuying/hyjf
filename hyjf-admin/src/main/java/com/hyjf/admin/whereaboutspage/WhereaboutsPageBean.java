package com.hyjf.admin.whereaboutspage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.WhereaboutsPageConfig;

/**
 * 
 * @author Michael
 */
public class WhereaboutsPageBean extends WhereaboutsPageConfig  implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	
	private String utmName;
	/**
	 * 检索条件 订单id
	 */
	private String referrerName;
	/**
     * 检索条件 用户名
     */
    private String titleName;
    /**
     * 参数项 获取style样式
     */
    private ParamName pageStyle;
    
    /**
     * 项目资料
     */
    private List<BorrowCommonImage> whereaboutsPagePictures1 = new ArrayList<BorrowCommonImage>();
    /**
     * 项目资料
     */
    private List<BorrowCommonImage> whereaboutsPagePictures2 = new ArrayList<BorrowCommonImage>();
    /**
     * 项目资料
     */
    private List<BorrowCommonImage> whereaboutsPagePictures3 = new ArrayList<BorrowCommonImage>();
	
    /**
     * 图片
     */
    private String imageJson1;
    
    /**
     * 图片
     */
    private String imageJson2;
    
    /**
     * 图片
     */
    private String imageJson3;
    
    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;
    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;
	


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

    public String getUtmName() {
        return utmName;
    }

    public void setUtmName(String utmName) {
        this.utmName = utmName;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
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

    

   
    public List<BorrowCommonImage> getWhereaboutsPagePictures1() {
        return whereaboutsPagePictures1;
    }

    public void setWhereaboutsPagePictures1(List<BorrowCommonImage> whereaboutsPagePictures1) {
        this.whereaboutsPagePictures1 = whereaboutsPagePictures1;
    }

    public List<BorrowCommonImage> getWhereaboutsPagePictures2() {
        return whereaboutsPagePictures2;
    }

    public void setWhereaboutsPagePictures2(List<BorrowCommonImage> whereaboutsPagePictures2) {
        this.whereaboutsPagePictures2 = whereaboutsPagePictures2;
    }

    public List<BorrowCommonImage> getWhereaboutsPagePictures3() {
        return whereaboutsPagePictures3;
    }

    public void setWhereaboutsPagePictures3(List<BorrowCommonImage> whereaboutsPagePictures3) {
        this.whereaboutsPagePictures3 = whereaboutsPagePictures3;
    }

    public String getImageJson1() {
        return imageJson1;
    }

    public void setImageJson1(String imageJson1) {
        this.imageJson1 = imageJson1;
    }

    public String getImageJson2() {
        return imageJson2;
    }

    public void setImageJson2(String imageJson2) {
        this.imageJson2 = imageJson2;
    }

    public String getImageJson3() {
        return imageJson3;
    }

    public void setImageJson3(String imageJson3) {
        this.imageJson3 = imageJson3;
    }

	public ParamName getPageStyle() {
		return pageStyle;
	}

	public void setPageStyle(ParamName pageStyle) {
		this.pageStyle = pageStyle;
	}



}
