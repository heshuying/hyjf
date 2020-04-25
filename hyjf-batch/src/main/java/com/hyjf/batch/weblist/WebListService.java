package com.hyjf.batch.weblist;

import com.hyjf.batch.BaseService;

public interface WebListService extends BaseService {

    /**
     * 债转服务费
     *
     * @return
     */
    public int creditFeeService() throws Exception;

    /**
     * 线下充值
     *
     * @return
     */
    public int outLineService() throws Exception;

    /**
     * 计算网站收到情况 （服务费）
     *
     * @return
     */
    public int incomeLoanService() throws Exception;

    /**
     * 计算网站收到情况 （管理费）
     *
     * @return
     */
    public int incomeFeeService() throws Exception;

    /**
     * 充值手续费返还
     *
     * @return
     */
    public int rechargeFeeService() throws Exception;

    /**
     * 出借推广提成
     *
     * @return
     */
    public int promoteCommissionService() throws Exception;

    /**
     * 其他
     *
     * @return
     */
    public int otherService() throws Exception;
}
