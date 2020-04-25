package com.hyjf.app.report;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;

import java.util.Map;

public interface OperationReportService extends BaseService {

    /**
     * 获取报表详情接口
     * @param paraMap 参数id 运营报告主键，必填。
     * @return
     */
    JSONObject getOperationReportInfo(Map<String, Object> paraMap);

    /**
     * 获取发布的列表接口
     * @param paraMap 参数isRelease 是否发布，必填。参数paginator  分页条目，选填
     * @return
     */
    JSONObject getRecordListByRelease(Map<String, Object> paraMap);

}
