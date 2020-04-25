package com.hyjf.admin.manager.activity.returncash;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminActivityReturncashCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface ActivityReturncashService extends BaseService {

    /**
     * 获取待返现列表数量
     *
     * @param form
     * @return
     */
    public int getReturncashRecordCount(Map<String, Object> paraMap);

	/**
	 * 获取待返现列表
	 *
	 * @param form
	 * @return
	 */
    public List<AdminActivityReturncashCustomize> getReturncashRecordList(Map<String, Object> paraMap);

    /**
     * 获取已返现列表
     *
     * @return
     */
	public List<AdminActivityReturncashCustomize> getReturnedcashRecordList(Map<String, Object> paraMap);
	
	 /**
     * 获取已返现列表数量
     *
     * @param form
     * @return
     */
	public int getReturnedcashRecordCount(Map<String, Object> paraMap);

	public int insertReturncashRecord(Integer userId, String ip, ChinapnrBean bean);

	boolean checkReturnCashStatus(Integer userId);

	BigDecimal getReturncashAmountTotal(Map<String, Object> paraMap);


}
