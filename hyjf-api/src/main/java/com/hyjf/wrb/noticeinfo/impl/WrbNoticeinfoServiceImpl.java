package com.hyjf.wrb.noticeinfo.impl;

import java.util.List;

import com.hyjf.mybatis.model.auto.MessagePushTemplate;
import com.hyjf.mybatis.model.auto.MessagePushTemplateExample;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistoryExample;
import com.hyjf.wrb.noticeinfo.WrbNoticeinfoSerice;

@Service
public class WrbNoticeinfoServiceImpl extends BaseServiceImpl implements WrbNoticeinfoSerice {


    /**
     * 查询平台的公告信息
     *
     * @param limit
     * @param page
     * @return
     */
    @Override
    public List<MessagePushMsgHistory> getNoticeinfoDetail(Integer limit,
                                                           Integer page) {
        MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
        MessagePushMsgHistoryExample.Criteria criteria = example.createCriteria();

        Integer limitStart = limit * (page - 1);
        //偏移量
        if (limit > 0) {
            example.setLimitEnd(limit);
        }
        //开始条数
        if (limitStart >= 0) {
            example.setLimitStart(limitStart);
        }
        criteria.andMsgUserIdEqualTo(0);

        List<MessagePushMsgHistory> messagePushMsgHistoryList = messagePushMsgHistoryMapper.selectByExample(example);

        return messagePushMsgHistoryList;
    }

    /**
     * 查询平台的公告信息-新
     *
     * @param limit
     * @param page
     * @return
     */
    @Override
    public List<MessagePushTemplate> getNoticeinfoDetailNew(Integer limit, Integer page) {
        MessagePushTemplateExample example = new MessagePushTemplateExample();
        MessagePushTemplateExample.Criteria criteria = example.createCriteria();
        Integer limitStart = limit * (page - 1);
        //偏移量
        if (limit > 0) {
            example.setLimitEnd(limit);
        }
        //开始条数
        if (limitStart >= 0) {
            example.setLimitStart(limitStart);
        }
        criteria.andStatusEqualTo(1);//启用的
        criteria.andTemplateActionEqualTo(3);//只查打开微信的

        List<MessagePushTemplate> list = messagePushTemplateMapper.selectByExample(example);
        return list;
    }

}
