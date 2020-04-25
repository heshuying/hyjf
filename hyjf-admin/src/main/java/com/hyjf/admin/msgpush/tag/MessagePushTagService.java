package com.hyjf.admin.msgpush.tag;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushTag;

public interface MessagePushTagService extends BaseService{

    /**
     * 获取列表
     * 
     * @return
     */
    public List<MessagePushTag> getRecordList(MessagePushTagBean bean, int limitStart, int limitEnd);

    /**
     * 获取列表记录数
     * 
     * @return
     */
    public Integer getRecordCount(MessagePushTagBean form);

    
    /**
     * 获取单个
     * 
     * @return
     */
    public MessagePushTag getRecord(Integer record);

    /**
     * 根据主键判断列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(MessagePushTag record);

    /**
     * 插入
     * 
     * @param record
     */
    public void insertRecord(MessagePushTag record);

    /**
     * 更新
     * 
     * @param record
     */
    public void updateRecord(MessagePushTag record);

    /**
     * 删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

    /**
     * 检查标签编码是否唯一
     * 
     * @param id
     * @param tagCode
     */
    public int countByTagCode(Integer id, String tagCode);
    
	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
    
	/**
	 * 获取消息推送标签列表
	 * @return
	 */
	public List<MessagePushTag> getAllPushTagList();
}