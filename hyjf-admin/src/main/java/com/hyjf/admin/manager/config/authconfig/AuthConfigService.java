package com.hyjf.admin.manager.config.authconfig;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfig;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfigCustomize;
import com.hyjf.mybatis.model.auto.HjhUserAuthConfigLogCustomize;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 授权配置service
 */
public interface AuthConfigService extends BaseService{
    /**
     * 获取授权配置列表
     * @return
     */
	List<HjhUserAuthConfigCustomize> getAuthConfigList(int limitStart, int limitEnd);

    /**
     * 获取授权配置详情
     * @param id
     * @return
     */
    HjhUserAuthConfig getAuthConfigById(Integer id);

    /**
     * 获取总记录数
     * @return
     */
    int getAuthConfigCount();

    /**
     * 更新授权配置表 新增授权配置操作记录
     * @param form
     * @return
     */
    int updateRecord(HjhUserAuthConfig form,HttpServletRequest request);

    /**
     * 获取操作记录总数
     * @return
     */
    int getAuthConfigLogCount();

    /**
     * 获取操作记录列表
     * @param offset
     * @param limit
     * @return
     */
    List<HjhUserAuthConfigLogCustomize> getAuthConfigLogList(int limitStart, int limitEnd);
}
