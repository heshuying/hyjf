package com.hyjf.admin.exception.poundage;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageExceptionCustomize;

/**
 * com.hyjf.admin.exception.poundage
 *
 * @author wgx
 * @date 2017/12/15
 */
public interface PoundageExceptionService extends BaseService {

    /**
     * 查询数量
     *
     * @param poundageExceptionCustomize
     * @return
     */
    public Integer getPoundageExceptionCount(PoundageExceptionCustomize poundageExceptionCustomize);

    /**
     * 查询信息
     *
     * @param poundageExceptionCustomize
     * @return
     */
    public List<PoundageExceptionCustomize> getPoundageExceptionList(PoundageExceptionCustomize poundageExceptionCustomize);

    /**
     * 新增信息
     *
     * @param poundageExceptionCustomize
     * @return
     */
    public void insertPoundageException(PoundageExceptionCustomize poundageExceptionCustomize);

    /**
     * 修改信息
     *
     * @param poundageExceptionCustomize
     * @return
     */
    public void updatePoundageException(PoundageExceptionCustomize poundageExceptionCustomize);

    /**
     * 修改状态
     *
     * @param poundageExceptionCustomize
     * @return
     */
    public void updatePoundageExceptionStatus(PoundageExceptionCustomize poundageExceptionCustomize);

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    public void deletePoundageException(int id);

    /**
     * 获取单条信息
     *
     * @param id
     * @return
     */
    public PoundageExceptionCustomize getPoundageExceptionById(int id);

    /**
     * 修改信息状态
     *
     * @param poundageCustomize
     * @param poundageExceptionCustomize
     * @author wgx
     */
    public void updateStatus(PoundageCustomize poundageCustomize, PoundageExceptionCustomize poundageExceptionCustomize);
}
