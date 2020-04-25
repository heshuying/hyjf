package com.hyjf.admin.finance.returncash;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.ReturncashCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface ReturncashService extends BaseService {

    /**
     * 获取待返现列表数量
     *
     * @param form
     * @return
     */
    public int getReturncashRecordCount(ReturncashBean form);

	/**
	 * 获取待返现列表
	 *
	 * @param form
	 * @return
	 */
	public List<ReturncashCustomize> getReturncashRecordList(ReturncashBean form);

    /**
     * 获取待已返现列表数量
     *
     * @param form
     * @return
     */
    public int getReturnedcashRecordCount(ReturncashBean form);

    /**
     * 获取已返现列表
     *
     * @param form
     * @return
     */
    public List<ReturncashCustomize> getReturnedcashRecordList(ReturncashBean form);

    /**
     * 返现处理
     *
     * @param form
     * @return
     */
    public int insertReturncashRecord(ReturncashCustomize returncashCustomize, ChinapnrBean obj);

}
