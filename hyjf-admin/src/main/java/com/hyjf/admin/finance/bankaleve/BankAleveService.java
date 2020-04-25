package com.hyjf.admin.finance.bankaleve;

import com.hyjf.mybatis.model.customize.AleveLogCustomize;

import java.util.List;

/**
 * Created by cuigq on 2018/1/22.
 */
public interface BankAleveService {

    /**
     * 查询银行账务明细
     * @param form
     */
    void queryList(BankAleveBean form);

    /**
     * 导出文件查询银行账务明细
     *
     * @param form
     * @return
     */
    List<AleveLogCustomize> queryAleveLogList(BankAleveBean form);

    /**
     * 导出文件查询银行账务明细总条数
     *
     * @param form
     * @return
     */
    Integer queryAleveLogCount(BankAleveBean form);
}
