package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.FddTempletCustomize;

import java.util.List;

public interface FddTempletCustomizeMapper {
    /**
     * 
     * 件数
     * @author liubin
     * @param fddTempletCustomize
     * @return
     */
    public int countRecord();

    /**
     * 
     * 列表
     * @author liubin
     * @param fddTempletCustomize
     * @return
     */
    public List<FddTempletCustomize> getRecordList(FddTempletCustomize fddTempletCustomize);

    /**
     *
     * 列表
     * @author liubin
     * @param fddTempletCustomize
     * @return
     */
    public List<FddTempletCustomize> getMaxTempletId(FddTempletCustomize fddTempletCustomize);

    /**
     * 查找不再合同模版约定条款表里的协议模板号
     * @return
     */
    public List<FddTempletCustomize> selectContractTempId();
}
