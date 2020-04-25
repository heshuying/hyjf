package com.hyjf.admin.manager.config.underlinerecharge;

import com.hyjf.mybatis.model.auto.UnderLineRecharge;

import java.util.List;

/**
 *
 * @author : huanghui
 */
public interface UnderLineRechargeService {

    /**
     * 插入
     * @param record
     * @return
     * @author :
     */
    int insertRecord(UnderLineRecharge record);

    /**
     * 查询指定数据的总条数
     * @param ids
     * @param code
     * @return
     */
    int underLineRechargeListInfoByCode(String ids, String code);

    /**
     * 现在充值类型总条数
     * @param form
     * @return
     */
    int countUnderLineRecharList(UnderLineRechargeBean form);

    /**
     * 检索现在充值类型列表
     * @param form
     * @return
     */
    List<UnderLineRecharge> searchUnderLineRechargeList(UnderLineRechargeBean form);

    /**
     * 获取数据表中所有数据
     * @param form
     * @return
     */
    List<UnderLineRecharge> selectUnderLineRechargeList(UnderLineRechargeBean form);

    /**
     * 删除数据
     * @param recordList
     */
    void deleteUnderLineRecharge(List<Integer> recordList);

    /**
     * 根据ID 获取单条记录
     * @param ids
     * @return
     */
    UnderLineRecharge getUnderLineRechargeListInfo(String ids);

    /**
     * 更新数据
     * @param form
     * @return
     */
    int updateUnderLineRechargeListInfo(UnderLineRechargeBean form);

}
