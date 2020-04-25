package com.hyjf.admin.exception.withdrawexception;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.WithdrawCustomize;

public interface WithdrawExceptionService extends BaseService {

    /**
     * 获取提现列表数量
     *
     * @param form
     * @return
     */
    public int getWithdrawRecordCount(WithdrawExceptionBean form);

	/**
	 * 获取提现列表
	 *
	 * @param form
	 * @return
	 */
	public List<WithdrawCustomize> getWithdrawRecordList(WithdrawExceptionBean form);

    /**
     * 提现确认
     *
     * @param form
     * @return
     */
    public String updateWithdrawConfirm(WithdrawExceptionBean form, boolean success);

}
