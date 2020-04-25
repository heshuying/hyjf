package com.hyjf.admin.manager.config.borrow.finmanchargenew;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.BorrowFinmanNewChargeCustomize;

public interface FinmanChargeNewService extends BaseService {

    /**
     * 项目类型
     * 
     * @return
     */
    public List<BorrowProjectType> borrowProjectTypeList(String borrowTypeCd);

    /**
     * 
     * 检索融资管理费列表件数
     * @author liuyang
     * @param form
     * @return
     */
    public int countRecordTotal(FinmanChargeNewBean form);

    /**
     * 
     * 获取融资管理费列表
     * @author liuyang
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<BorrowFinmanNewChargeCustomize> getRecordList(FinmanChargeNewBean form, int limitStart, int limitEnd);

    /**
     * 
     * 根据主键获取融资管理费详情
     * @author liuyang
     * @param manChargeCd
     * @return
     */
    public BorrowFinmanNewCharge getRecordInfo(String manChargeCd);

    /**
     * 
     * 根据表的类型,期数,项目类型检索管理费件数
     * @author liuyang
     * @param manChargeType
     * @param manChargeTime
     * @param projectType
     * @return
     */
    public int countRecordByProjectType(String manChargeType, Integer manChargeTime, 
    		String instCode, Integer assetType);

    /**
     * 
     * 插入操作
     * @author liuyang
     * @param form
     * @return
     */
    public int insertRecord(FinmanChargeNewBean form);

    /**
     * 
     * 更新操作
     * @author liuyang
     * @param form
     * @return
     */
    public int updateRecord(FinmanChargeNewBean form);

    /**
     * 
     * 删除操作
     * @author liuyang
     * @param form
     * @return
     */
    public int deleteRecord(FinmanChargeNewBean form);
    
    /**
     * 
     * 根据表的期数,项目类型检索管理费件数
     * @author libin
     * @param manChargeTime
     * @param instCode
     * @param assetType
     * @return
     */
    public int countRecordByItems(Integer manChargeTime, 
    		String instCode, Integer assetType);
    
    /**
     * 
     * 插入操作
     * @author liuyang
     * @param form
     * @return
     */
    public int insertLogRecord(FinmanChargeNewBean form);
    
    /**
     * 
     * 更新操作
     * @author liuyang
     * @param form
     * @return
     */
    public int updateLogRecord(FinmanChargeNewBean form);
    
    /**
     * 
     * 删除操作
     * @author liuyang
     * @param form
     * @return
     */
    public int deleteLogRecord(FinmanChargeNewBean form);

}
