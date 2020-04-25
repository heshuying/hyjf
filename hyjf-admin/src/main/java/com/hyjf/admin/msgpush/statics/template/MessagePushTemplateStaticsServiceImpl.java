package com.hyjf.admin.msgpush.statics.template;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTagExample;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStatics;
import com.hyjf.mybatis.model.auto.MessagePushTemplateStaticsExample;

@Service
public class MessagePushTemplateStaticsServiceImpl extends BaseServiceImpl implements MessagePushTemplateStaticsService {

    /**
     * 获取列表
     * 
     * @return
     */
    @Override
	public List<MessagePushTemplateStatics> getRecordList(MessagePushTemplateStaticsBean bean, int limitStart, int limitEnd) {
        MessagePushTemplateStaticsExample example = new MessagePushTemplateStaticsExample();
        MessagePushTemplateStaticsExample.Criteria criteria = example.createCriteria();
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
        	criteria.andSendTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(bean.getStartDateSrch())));
        }
        if(StringUtils.isNotEmpty(bean.getEndDateSrch())){
        	criteria.andSendTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(bean.getEndDateSrch())));
        }
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("send_time desc");
        return this.messagePushTemplateStaticsMapper.selectByExample(example);
    }

	/**
	 * 获取列表记录数
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer getRecordCount(MessagePushTemplateStaticsBean bean) {
        MessagePushTemplateStaticsExample example = new MessagePushTemplateStaticsExample();
        MessagePushTemplateStaticsExample.Criteria criteria = example.createCriteria();
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
        	criteria.andSendTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(bean.getStartDateSrch())));
        }
        if(StringUtils.isNotEmpty(bean.getEndDateSrch())){
        	criteria.andSendTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(bean.getEndDateSrch())));
        }
        int cnt = this.messagePushTemplateStaticsMapper.countByExample(example);
        if(cnt > 0){
        	return cnt;
        }
        return 0;
	}
    
    
    /**
     * 获取单个
     * 
     * @return
     */
    @Override
	public MessagePushTemplateStatics getRecord(Integer record) {
        MessagePushTemplateStatics page = messagePushTemplateStaticsMapper.selectByPrimaryKey(record);
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
