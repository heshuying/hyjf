package com.hyjf.admin.msgpush.statics.plat;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.MessagePushPlatStatics;
import com.hyjf.mybatis.model.auto.MessagePushPlatStaticsExample;
import com.hyjf.mybatis.model.auto.MessagePushTag;
import com.hyjf.mybatis.model.auto.MessagePushTagExample;

@Service
public class MessagePushPlatStaticsServiceImpl extends BaseServiceImpl implements MessagePushPlatStaticsService {

    /**
     * 获取列表
     * 
     * @return
     */
    @Override
	public List<MessagePushPlatStatics> getRecordList(MessagePushPlatStaticsBean bean, int limitStart, int limitEnd) {
        MessagePushPlatStaticsExample example = new MessagePushPlatStaticsExample();
        MessagePushPlatStaticsExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if(StringUtils.isNotEmpty(bean.getTagIdSrch())){
        	criteria.andTagIdEqualTo(Integer.valueOf(bean.getTagIdSrch()));
        }
        if(StringUtils.isNotEmpty(bean.getStartDateSrch())){
        	criteria.andStaDateGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(bean.getStartDateSrch())));
        }
        if(StringUtils.isNotEmpty(bean.getEndDateSrch())){
        	criteria.andStaDateLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(bean.getEndDateSrch())));
        }
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("sta_date desc");
        return this.messagePushPlatStaticsMapper.selectByExample(example);
    }

	/**
	 * 获取列表记录数
	 * @param form
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer getRecordCount(MessagePushPlatStaticsBean bean) {
        MessagePushPlatStaticsExample example = new MessagePushPlatStaticsExample();
        MessagePushPlatStaticsExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if(StringUtils.isNotEmpty(bean.getTagIdSrch())){
        	criteria.andTagIdEqualTo(Integer.valueOf(bean.getTagIdSrch()));
        }
        if(StringUtils.isNotEmpty(bean.getStartDateSrch())){
        	criteria.andStaDateGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(bean.getStartDateSrch())));
        }
        if(StringUtils.isNotEmpty(bean.getEndDateSrch())){
        	criteria.andStaDateLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(bean.getEndDateSrch())));
        }
        int cnt = this.messagePushPlatStaticsMapper.countByExample(example);
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
	public MessagePushPlatStatics getRecord(Integer record) {
        MessagePushPlatStatics page = messagePushPlatStaticsMapper.selectByPrimaryKey(record);
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
