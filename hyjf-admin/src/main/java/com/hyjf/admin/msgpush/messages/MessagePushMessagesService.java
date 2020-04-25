package com.hyjf.admin.msgpush.messages;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MessagePushMsg;

public interface MessagePushMessagesService extends BaseService{

    /**
     * 获取着落页列表记录数
     * 
     * @return
     */
    public Integer getRecordCount(MessagePushMessagesBean form);
    
    /**
     * 获取着落页列表
     * 
     * @return
     */
    public List<MessagePushMsg> getRecordList(MessagePushMessagesBean bean, int limitStart, int limitEnd);
    
    /**
     * 获取单个着落页信息
     * 
     * @return
     */
    public MessagePushMsg getRecord(Integer record);

    /**
     * 根据主键判断列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(MessagePushMsg record);

    /**
     * 插入
     * 
     * @param record
     */
    public void insertRecord(MessagePushMsg record);

    /**
     * 更新
     * 
     * @param record
     */
    public void updateRecord(MessagePushMsg record);

    /**
     * 删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

//    /**
//     * 检查标签编码是否唯一
//     * 
//     * @param id 主键
//     * @param tagId 标签类型id
//     * @param templateCode 标签编码code
//     */
//    public int countByTemplateCode(Integer id, Integer tagId,String templateCode);
    
	/**
	 * 资料上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
}