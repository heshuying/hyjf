package com.hyjf.admin.manager.config.subconfig;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;

public interface SubConfigService extends BaseService {

    /**
     * 项目类型
     *
     * @return
     */
//    public List<SubCommissionListConfigCustomize> borrowProjectTypeList(String borrowTypeCd);

    /**
     *
     * 检索融资管理费列表件数
     * @author liuyang
     * @param form
     * @return
     */
    public int getRecordCount(Map<String, Object> conditionMap);

    /**
     *
     * 获取融资管理费列表
     * @author liuyang
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<SubCommissionListConfigCustomize> getRecordList(Map<String, Object> conditionMap, int limitStart, int limitEnd);

    /**
     * 获取分账名单配置中的所有用户的用户名、真实姓名、银行账号
     * @return
     * @author wgx
     */
    public List<SubCommissionListConfigCustomize> getSimpleList();

    /**
     *
     * 根据主键获取融资管理费详情
     * @author liuyang
     * @param manChargeCd
     * @return
     */
    public SubCommissionListConfigCustomize getRecordInfo(Integer manChargeCd);

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
    public int insertRecord(SubConfigBean form);

    /**
     *
     * 更新操作
     * @author liuyang
     * @param form
     * @return
     */
    public int updateRecord(SubConfigBean form);

    /**
     *
     * 删除操作
     * @author liuyang
     * @param form
     * @return
     */
    public int deleteRecord(SubConfigBean form);

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
    public int insertLogRecord(SubConfigBean form);

    /**
     *
     * 更新操作
     * @author liuyang
     * @param form
     * @return
     */
    public int updateLogRecord(SubConfigBean form);

    /**
     *
     * 删除操作
     * @author liuyang
     * @param form
     * @return
     */
    public int deleteLogRecord(SubConfigBean form);
    /**
     *
     * 查询用户名信息
     * @author liuyang
     * @param form
     * @return
     */
    public Map<String, Object> userMap(SubConfigBean form);

    /**
     *
     * 根据用户id查询真是姓名，电子账号
     * @author liuyang
     * @param form
     * @return
     */
    public Map<String, Object> getUserInfo(SubConfigBean form);
    /**
     * 根据用户名查询分账名单是否存在
     */
    public List<SubCommissionListConfigCustomize> subconfig(String username);
}
