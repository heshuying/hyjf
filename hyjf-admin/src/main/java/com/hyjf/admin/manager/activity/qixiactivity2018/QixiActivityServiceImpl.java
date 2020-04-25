package com.hyjf.admin.manager.activity.qixiactivity2018;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.ActivityQixiMapper;
import com.hyjf.mybatis.model.auto.ActivityQixi;
import com.hyjf.mybatis.model.customize.admin.QixiActivityCustomize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author by xiehuili on 2018/7/23.
 */
@Service
public class QixiActivityServiceImpl extends BaseServiceImpl implements QixiActivityService {

    @Autowired
    private ActivityQixiMapper activityQixiMapper;
    /**
     * 查询七夕活动单笔出借条数
     * @param map
     * @return
     */
    @Override
    public Integer selectQixiActivityCount( Map<String, Object> map){
        return qixiActivityCustomizeMapper.selectQixiActivityCount(map);
    }
    /**
     * 查询七夕活动单笔出借列表
     * @param map
     * @return
     */
    @Override
    public List<QixiActivityCustomize> selectQixiActivityList(Map<String, Object> map){
        return qixiActivityCustomizeMapper.selectQixiActivityList(map);
    }
    /**
     * 查询七夕活动累计出借条数
     * @param map
     * @return
     */
    @Override
    public Integer selectQixiActivityTotalCount( Map<String, Object> map){
        return qixiActivityCustomizeMapper.selectQixiActivityTotalCount(map);
    }
    /**
     * 查询七夕活动累计出借列表
     * @param map
     * @return
     */
    @Override
    public List<QixiActivityCustomize> selectQixiActivityTotalList(Map<String, Object> map){
        return qixiActivityCustomizeMapper.selectQixiActivityTotalList(map);
    }
    /**
     * 查询七夕活动奖励条数
     * @param map
     * @return
     */
    @Override
    public Integer selectQixiActivityAwardCount( Map<String, Object> map){
        return qixiActivityCustomizeMapper.selectQixiActivityAwardCount(map);
    }
    /**
     *查询七夕活动奖励明细列表
     * @param map
     * @return
     */
    @Override
    public List<QixiActivityCustomize> selectQixiActivityAwardList(Map<String, Object> map){
        return qixiActivityCustomizeMapper.selectQixiActivityAwardList(map);
    }
    /**
     * 查询七夕活动奖励转台页面
     * @param from
     * @return
     */
    @Override
    public void updateQixiActivityAward(ActivityQixi from){
       activityQixiMapper.updateByPrimaryKeySelective(from);
    }
}
