package com.hyjf.admin.manager.config.protocol;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.ProtocolVersion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by DELL on 2018/5/25.
 */
public interface ProtocolService {

    /**
     * 获取全部列表
     *
     * @return
     */
    public List<ProtocolTemplateCommon> getRecordList(ProtocolTemplateBean form, int limitStart, int limitEnd);

    /**
     * 统计全部个数
     *
     * @return
     */
    public Integer countRecord(int limitStart, int limitEnd);


    /**
     * 根据协议id查询协议和版本
     *
     * @return
     */
    public ProtocolTemplateCommon getProtocolTemplateById(Integer id);


    /**
     * 添加协议模板
     *
     * @return
     */
    public void insertProtocolTemplate(ProtocolTemplateCommon form);

    /**
     * 修改协议模板
     *
     * @return
     */
    public void updateProtocolTemplate(ProtocolTemplateCommon form);

    /**
     * 删除协议模板
     */
    public void deleteProtocolTemplate(Integer id);

    /**
     * 校验字段是否为唯一
     *
     * @return
     */
    public JSONObject validatorFieldCheck(String protocolName, String versionNumber, String displayName,String protocolUrl,String protocolType,String oldDisplayName,String flag);


    /**
     * 图片上传
     *
     * @param request
     * @return
     */
    public String uploadFile(HttpServletRequest request, HttpServletResponse response);

    /**
     * 获得最新协议模版 前台展示信息
     *
     * @return
     */
    List<ProtocolTemplate> getNewInfo();

    /**
     * 修改已经存在的协议模板
     *
     * @return
     */
    void updateExistAction(ProtocolVersion form);
}
