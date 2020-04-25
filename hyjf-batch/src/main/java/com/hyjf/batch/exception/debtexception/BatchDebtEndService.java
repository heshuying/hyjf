package com.hyjf.batch.exception.debtexception;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminBankDebtEndCustomize;

import java.util.List;

/**
 * 结束债权异常处理Service
 *
 * @author liuyang
 */
public interface BatchDebtEndService extends BaseService {
    /**
     * 检索待结束的债权
     *
     * @return
     */
    List<AdminBankDebtEndCustomize> selectDebtEndList();

    /**
     * 结束债权
     *
     * @param borrowNid
     * @param tenderNid
     * @param userId
     * @return
     */
    public boolean requestDebtEnd(String borrowNid, String tenderNid, Integer userId) throws Exception;
}
