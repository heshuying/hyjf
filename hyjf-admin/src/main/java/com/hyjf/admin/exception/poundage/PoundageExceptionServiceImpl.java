package com.hyjf.admin.exception.poundage;

import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageExceptionCustomize;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;

import java.util.List;

/**
 * com.hyjf.admin.exception.poundage
 *
 * @author wgx
 * @date 2017/12/15
 */
@Service
public class PoundageExceptionServiceImpl extends BaseServiceImpl implements PoundageExceptionService {

    /**
     * 查询数量
     *
     * @param poundageExceptionCustomize
     * @return
     */
    @Override
    public Integer getPoundageExceptionCount(PoundageExceptionCustomize poundageExceptionCustomize) {
        Integer count = this.poundageExceptionCustomizeMapper.getPoundageExceptionCount(poundageExceptionCustomize);
        return count;
    }

    /**
     * 查询信息
     *
     * @param poundageExceptionCustomize
     * @return
     */
    @Override
    public List<PoundageExceptionCustomize> getPoundageExceptionList(PoundageExceptionCustomize poundageExceptionCustomize) {
        List<PoundageExceptionCustomize> list = this.poundageExceptionCustomizeMapper.getPoundageExceptionList(poundageExceptionCustomize);
        return list;
    }

    /**
     * 新增信息
     *
     * @param poundageExceptionCustomize
     * @return
     */
    @Override
    public void insertPoundageException(PoundageExceptionCustomize poundageExceptionCustomize) {
        this.poundageExceptionCustomizeMapper.insertPoundageException(poundageExceptionCustomize);
    }

    /**
     * 修改信息
     *
     * @param poundageExceptionCustomize
     * @return
     */
    @Override
    public void updatePoundageException(PoundageExceptionCustomize poundageExceptionCustomize) {
        this.poundageExceptionCustomizeMapper.updatePoundageException(poundageExceptionCustomize);
    }

    /**
     * 修改状态
     *
     * @param poundageExceptionCustomize
     * @return
     */
    @Override
    public void updatePoundageExceptionStatus(PoundageExceptionCustomize poundageExceptionCustomize) {
        this.poundageExceptionCustomizeMapper.updatePoundageExceptionStatus(poundageExceptionCustomize);
    }
    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    @Override
    public void deletePoundageException(int id) {
        this.poundageExceptionCustomizeMapper.deletePoundageException(id);
    }

    /**
     * 获取单条信息
     *
     * @param id
     * @return
     */
    @Override
    public PoundageExceptionCustomize getPoundageExceptionById(int id) {
        return this.poundageExceptionCustomizeMapper.getPoundageExceptionById(id);
    }

    /**
     * 修改信息状态
     *
     * @param poundageCustomize
     * @param poundageExceptionCustomize
     * @author wgx
     */
    @Override
    public void updateStatus(PoundageCustomize poundageCustomize, PoundageExceptionCustomize poundageExceptionCustomize) {
        this.poundageCustomizeMapper.updatePoundage(poundageCustomize);
        updatePoundageExceptionStatus(poundageExceptionCustomize);
    }

}
