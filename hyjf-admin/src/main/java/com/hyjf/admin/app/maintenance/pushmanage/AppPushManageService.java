package com.hyjf.admin.app.maintenance.pushmanage;

import com.hyjf.mybatis.model.auto.AppPushManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AppPushManageService {

    /**
     * App推送管理总条数
     * @param form
     * @return
     */
    int countAppPushManageList(AppPushManageBean form);

    /**
     * App推送列表
     * @param form
     * @return
     */
    List<AppPushManage> searchAppPushManageList(AppPushManageBean form);

    /**
     * 写入数据库
     * @param record
     * @return
     */
    int insertRecord(AppPushManage record);

    /**
     * 根据ID获取单挑记录
     * @param id
     * @return
     */
    AppPushManage getAppPushManageInfo(Integer id);

    /**
     * 更新数据
     * @param record
     * @return
     */
    int updateRecord(AppPushManageBean record);

    /**
     * 更新数据
     * @param record
     */
    void updateRecordNoVoid(AppPushManage record);

    /**
     * 获取单条记录
     * @param record
     * @return
     */
    AppPushManage getRecord(Integer record);

    /**
     * 删除推送信息
     * @param recordList
     */
    void deleteRecord(List<Integer> recordList);

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 根据条件查询数据
     * @param record
     * @param limitStart
     * @param limitEnd
     * @return
     */
    List<AppPushManage> selectRecordList(AppPushManageBean record, int limitStart, int limitEnd);
}
