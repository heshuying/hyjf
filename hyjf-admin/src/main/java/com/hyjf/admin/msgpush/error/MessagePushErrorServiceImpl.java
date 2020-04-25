package com.hyjf.admin.msgpush.error;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistoryExample;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTagExample;

@Service
public class MessagePushErrorServiceImpl extends BaseServiceImpl implements MessagePushErrorService {

    /**
     * 获取列表
     * 
     * @return
     */
    @Override
	public List<MessagePushMsgHistory> getRecordList(MessagePushErrorBean bean, int limitStart, int limitEnd) {
        MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
        MessagePushMsgHistoryExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (StringUtils.isNotEmpty(bean.getMsgTitleSrch())) {
            criteria.andMsgTitleLike("%" + bean.getMsgTitleSrch() + "%");
        }
        if(StringUtils.isNotEmpty(bean.getTagIdSrch())){
        	criteria.andTagIdEqualTo(Integer.valueOf(bean.getTagIdSrch()));
        }
        if(StringUtils.isNotEmpty(bean.getMsgCodeSrch())){
        	criteria.andMsgCodeEqualTo(bean.getMsgCodeSrch());
        }
        if(StringUtils.isNotEmpty(bean.getStartDateSrch())){
        	criteria.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(bean.getStartDateSrch())));
        }
        if(StringUtils.isNotEmpty(bean.getEndDateSrch())){
        	criteria.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(bean.getEndDateSrch())));
        }
        
        criteria.andMsgSendStatusEqualTo(2);//发送失败
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("create_time desc");
        return this.messagePushMsgHistoryMapper.selectByExample(example);
    }

	/**
	 * 获取列表记录数
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer getRecordCount(MessagePushErrorBean bean) {
        MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
        MessagePushMsgHistoryExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (StringUtils.isNotEmpty(bean.getMsgTitleSrch())) {
            criteria.andMsgTitleLike("%" + bean.getMsgTitleSrch() + "%");
        }
        if(StringUtils.isNotEmpty(bean.getTagIdSrch())){
        	criteria.andTagIdEqualTo(Integer.valueOf(bean.getTagIdSrch()));
        }
        if(StringUtils.isNotEmpty(bean.getMsgCodeSrch())){
        	criteria.andMsgCodeEqualTo(bean.getMsgCodeSrch());
        }
        if(StringUtils.isNotEmpty(bean.getStartDateSrch())){
        	criteria.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(bean.getStartDateSrch())));
        }
        if(StringUtils.isNotEmpty(bean.getEndDateSrch())){
        	criteria.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(bean.getEndDateSrch())));
        }
 
        criteria.andMsgSendStatusEqualTo(2);//发送失败
        return this.messagePushMsgHistoryMapper.countByExample(example);
	}
    
    
    /**
     * 获取单个
     * 
     * @return
     */
    @Override
	public MessagePushMsgHistory getRecord(Integer record) {
        MessagePushMsgHistory page = messagePushMsgHistoryMapper.selectByPrimaryKey(record);
        return page;
    }

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<MessagePushTag> getTagList() {
        MessagePushTagExample example = new MessagePushTagExample();
        MessagePushTagExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);//启用状态
        example.setOrderByClause("sort asc");
        return this.messagePushTagMapper.selectByExample(example);
			
	}

}
