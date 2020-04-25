package com.hyjf.api.aems.repayment;

import com.hyjf.base.bean.BaseBean;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class AemsBorrowRepaymentInfoBean extends BaseBean {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 借款编号 检索条件
	 */
	private String borrowNid;
	/**
	 * 资产编号 检索条件
	 */
	private String assetId;
	/**
     * 应还日期 检索条件
     */
    private String recoverTimeStart;
    /**
     * 应还日期 检索条件
     */
    private String recoverTimeEnd;
    public String getBorrowNid() {
        return borrowNid;
    }
    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
    public String getAssetId() {
        return assetId;
    }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
    public String getRecoverTimeStart() {
        return recoverTimeStart;
    }
    public void setRecoverTimeStart(String recoverTimeStart) {
        this.recoverTimeStart = recoverTimeStart;
    }
    public String getRecoverTimeEnd() {
        return recoverTimeEnd;
    }
    public void setRecoverTimeEnd(String recoverTimeEnd) {
        this.recoverTimeEnd = recoverTimeEnd;
    }
   
}
