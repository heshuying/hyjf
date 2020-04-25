package com.hyjf.admin.manager.content.events;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.mybatis.model.auto.EventsExample;
import com.hyjf.mybatis.model.customize.ContentEventsCustomize;

@Service
public class ContentEventsServiceImpl extends BaseServiceImpl implements ContentEventsService {


    /**
     * 获取文章列表列表
     * 
     * @return
     */
    public List<Events> getRecordList(Events events, int limitStart, int limitEnd) {
    	EventsExample example = new EventsExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        return eventsMapper.selectByExample(example);
    }

    /**
     * 获取单个文章维护
     * 
     * @return
     */
    public Events getRecord(Integer record) {
    	Events events = eventsMapper.selectByPrimaryKey(record);
        return events;
    }

    /**
     * 根据主键判断文章维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(Events record) {
        if (record.getId() == null) {
            return false;
        }
        EventsExample example = new EventsExample();
        EventsExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<Events> events = eventsMapper.selectByExample(example);
        if (events != null && events.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 维护插入
     * 
     * @param record
     */
    public void insertRecord(Events record) {
        record.setCreateTime(GetDate.getNowTime10());
        record.setUpdateTime(record.getCreateTime());
        Subject currentUser = SecurityUtils.getSubject();
        record.setAddAdmin(currentUser.getPrincipal()+"");
        eventsMapper.insertSelective(record);
    }

    /**
     * 维护更新
     * 
     * @param record
     */
    public void updateRecord(Events record) {
        record.setUpdateTime(GetDate.getNowTime10());
        Subject currentUser = SecurityUtils.getSubject();
        record.setAddAdmin(currentUser.getPrincipal()+"");
        eventsMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
        	eventsMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据条件查询数据
     */
    public List<Events> selectRecordList(ContentEventsBean form, int limitStart, int limitEnd) {
    	ContentEventsCustomize example = new ContentEventsCustomize();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        if(form.getEventYear()!=null){
        	example.setEventYear(form.getEventYear());
        }
        if (StringUtils.isNotEmpty(form.getStartCreate())) {
            example.setStartCreate(Integer.valueOf(GetDate.get10Time(form.getStartCreate())));
        }
        if (StringUtils.isNotEmpty(form.getEndCreate())) {
            example.setEndCreate(Integer.valueOf(GetDate.get10Time(form.getEndCreate())));
        }
        return contentEventsCustomizeMapper.selectContentEvents(example);
    }

}
