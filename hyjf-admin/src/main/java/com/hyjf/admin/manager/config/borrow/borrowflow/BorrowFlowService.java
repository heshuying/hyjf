package com.hyjf.admin.manager.config.borrow.borrowflow;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.HjhAssetBorrowTypeCustomize;

public interface BorrowFlowService extends BaseService {

    /**
     * 项目类型
     * 
     * @return
     */
    public List<BorrowProjectType> borrowProjectTypeList(String borrowTypeCd);

    /**
     * 
     * 件数
     * @author liubin
     * @param form
     * @return
     */
    public int countRecord(BorrowFlowBean form);

    /**
     * 
     * 列表
     * @author liubin
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<HjhAssetBorrowTypeCustomize> getRecordList(BorrowFlowBean form, int limitStart, int limitEnd);

    /**
     * 
     * 根据主键获取详情
     * @author liubin
     * @param Integer id
     * @return
     */
    public HjhAssetBorrowType getRecordInfo(Integer id);

    /**
     * 
     * 根据表的类型,期数,项目类型检索管理费件数
     * @author liubin
     * @param manChargeType
     * @param manChargeTime
     * @param projectType
     * @return
     */
    public int countRecordByPK(String instCode, Integer assetType);

    /**
     * 
     * 插入操作
     * @author liubin
     * @param form
     * @return
     */
    public int insertRecord(BorrowFlowBean form);

    /**
     * 
     * 更新操作
     * @author liubin
     * @param form
     * @return
     */
    public int updateRecord(BorrowFlowBean form);

    /**
     * 
     * 删除操作
     * @author liubin
     * @param form
     * @return
     */
    public int deleteRecord(BorrowFlowBean form);

}
