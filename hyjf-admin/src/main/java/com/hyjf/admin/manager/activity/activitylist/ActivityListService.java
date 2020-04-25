package com.hyjf.admin.manager.activity.activitylist;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActivityF1;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface ActivityListService extends BaseService {

    /**
     * 获取活动列表列表
     * 
     * @return
     */
    public List<ActivityList> getRecordList(ActivityList borrowActivityList, int limitStart, int limitEnd);

    /**
     * 获取单个活动列表维护
     * 
     * @return
     */
    public ActivityList getRecord(Integer record);

    /**
     * 根据主键判断活动列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(ActivityList record);

    /**
     * 活动列表插入
     * 
     * @param record
     */
    public void insertRecord(ActivityList record);

    /**
     * 活动列表更新
     * 
     * @param record
     */
    public void updateRecord(ActivityList record);

    /**
     * 活动删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

    /**
     * 根据条件查询数据
     * 
     * @param activityList
     * @param i
     * @param j
     * @return
     */
    public List<ActivityList> selectRecordList(ActivityListBean form, int limitStart, int limitEnd);

	public List<ActivityListCustomize> selectRecordListValid(ActivityListBean activityListBean, int i, int j);


    /**
     * 
     * 根据活动Type获取活动详情列表
     * @author liuyang
     * @param activityType
     * @return
     */
    public List<ActivityF1> selectActivityF1ListByActivityType(String activityType, int limitStart, int limitEnd);

    /**
     * 
     * 根据用户id查询当前活动详情
     * @author liuyang
     * @param user_id
     * @return
     */
    public ActivityF1 selectActivityF1ByUserId(Integer user_id);

    /**
     * 根据用户id查询活动件数
     * @author liuyang
     * @param user_id
     * @return
     */
    public int getActivityCountByUserId(Integer user_id);

    /**
     * 
     * 根据用户id查询用户部门情报
     * @author liuyang
     * @param userId
     * @return
     */
    public UserInfoCustomize queryUserInfoByUserId(Integer userId);

    /**
     * 
     * 根据用户检索条件查询活动件数
     * @author liuyang
     * @param parm
     * @return
     */
    public int countActivityInfo(Map<String, Object> parm);

    /**
     * 
     * 根据检索条件检索活动列表
     * @author liuyang
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<ActivityF1> selectActivityInfoList(Map<String, Object> parm, int limitStart, int limitEnd);

    /**
     * 
     * 返现操作
     * @author liuyang
     * @param activityF1
     * @param bean
     * @return
     */
    public int insertReturncashRecord(ActivityF1 activityF1, ChinapnrBean bean,UserInfoCustomize userInfo);

    String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
