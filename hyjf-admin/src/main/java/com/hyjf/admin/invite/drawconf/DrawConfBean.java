package com.hyjf.admin.invite.drawconf;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.InvitePrizeConf;

/**
 * 奖品配置 
 * 
 * @author qingbing
 *
 */
public class DrawConfBean extends InvitePrizeConf implements Serializable {

    /**
     * serialVersionUID:
     */
    private static final long serialVersionUID = 1L;
    
    private String ids;
    
    /**
     * 奖品状态
     */
    private String prizeStatusSrch;
    
    /**
     * 是否优惠券
     */
    private String prizeTypeSrch;
    
    /**
     * 奖品剩余数量
     */
    private Integer prizeUsed = 0;

    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;
    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;

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

    public String getPrizeStatusSrch() {
        return prizeStatusSrch;
    }

    public void setPrizeStatusSrch(String prizeStatusSrch) {
        this.prizeStatusSrch = prizeStatusSrch;
    }


    public String getPrizeTypeSrch() {
        return prizeTypeSrch;
    }

    public void setPrizeTypeSrch(String prizeTypeSrch) {
        this.prizeTypeSrch = prizeTypeSrch;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getPrizeUsed() {
        return prizeUsed;
    }

    public void setPrizeUsed(Integer prizeUsed) {
        this.prizeUsed = prizeUsed;
    }


}
