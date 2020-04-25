package com.hyjf.admin.finance.poundagedetail;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageCustomize;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageDetailCustomize;

import javax.servlet.http.HttpServletResponse;

public interface PoundageDetailService extends BaseService {
    /**
     * 查询数量
     *
     * @param poundageDetailCustomize
     * @return
     */
    public Integer getPoundageDetailCount(PoundageDetailCustomize poundageDetailCustomize);

    /**
     * 查询信息
     *
     * @param poundageDetailCustomize
     * @return
     */
    public List<PoundageDetailCustomize> getPoundageDetailList(PoundageDetailCustomize poundageDetailCustomize);

    /**
     * 新增信息
     *
     * @param poundageDetailCustomize
     * @return
     */
    public void insertPoundageDetail(PoundageDetailCustomize poundageDetailCustomize);

    /**
     * 修改信息
     *
     * @param poundageDetailCustomize
     * @return
     */
    public void updatePoundageDetail(PoundageDetailCustomize poundageDetailCustomize);

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    public void deletePoundageDetail(int id);

    /**
     * 获取单条信息
     *
     * @param id
     * @return
     */
    public PoundageDetailCustomize getPoundageDetailById(int id);

    /**
     * 导出手续费分账明细
     *
     * @param response
     * @param poundageCustomize
     * @author wgx
     */
    public void exportPoundageDetail(HttpServletResponse response, PoundageCustomize poundageCustomize);

}
