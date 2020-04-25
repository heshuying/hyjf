/**
 * Description:项目列表查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.user.credit;

import java.io.Serializable;
import java.util.List;

import com.hyjf.bank.service.BaseAjaxResultBean;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedCustomize;
import com.hyjf.mybatis.model.customize.web.TenderCreditAssignedStatisticCustomize;

public class MyCreditDetailAJaxBean extends BaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -7415746042913857105L;

	private List<TenderCreditAssignedCustomize> recordList;

	private TenderCreditAssignedStatisticCustomize assignedStatistic;
	
	private BorrowCredit borrowCredit;
	
	public TenderCreditAssignedStatisticCustomize getAssignedStatistic() {
		return assignedStatistic;
	}

	public void setAssignedStatistic(TenderCreditAssignedStatisticCustomize assignedStatistic) {
		this.assignedStatistic = assignedStatistic;
	}

	public List<TenderCreditAssignedCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<TenderCreditAssignedCustomize> recordList) {
		this.recordList = recordList;
	}

    public BorrowCredit getBorrowCredit() {
        return borrowCredit;
    }

    public void setBorrowCredit(BorrowCredit borrowCredit) {
        this.borrowCredit = borrowCredit;
    }
	
	


}
