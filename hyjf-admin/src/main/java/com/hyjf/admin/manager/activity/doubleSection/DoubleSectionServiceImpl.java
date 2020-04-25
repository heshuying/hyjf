package com.hyjf.admin.manager.activity.doubleSection;

import com.hyjf.mybatis.mapper.auto.ActivityMidauInfoMapper;
import com.hyjf.mybatis.mapper.customize.ActivityMidauInfoCustomizeMapper;
import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.customize.admin.DoubleSectionActivityCustomize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: walter.limeng
 * @Date: 2018/9/11 10:48
 * @Description: DoubleSectionServiceImpl
 */
@Service
public class DoubleSectionServiceImpl implements DoubleSectionService {

    @Resource
    private ActivityMidauInfoCustomizeMapper activityMidauInfoCustomizeMapper;
    @Resource
    private ActivityMidauInfoMapper activityMidauInfoMapper;
    @Override
    public Integer selectDouSectionActivityCount(Map<String, Object> paraMap) {
        return activityMidauInfoCustomizeMapper.selectDouSectionActivityCount(paraMap);
    }

    @Override
    public List<DoubleSectionActivityCustomize> selectDouSectionActivityList(Map<String, Object> paraMap) {
        return activityMidauInfoCustomizeMapper.selectDouSectionActivityList(paraMap);
    }

    @Override
    public Integer selectSectionActivityAwardCount(Map<String, Object> paraMap) {
        return activityMidauInfoCustomizeMapper.selectSectionActivityAwardCount(paraMap);
    }

    @Override
    public List<DoubleSectionActivityCustomize> selectSectionActivityAwardList(Map<String, Object> paraMap) {
        return activityMidauInfoCustomizeMapper.selectSectionActivityAwardList(paraMap);
    }

    @Override
    public void updateSectionActivityAward(ActivityMidauInfo from) {
        activityMidauInfoMapper.updateByPrimaryKeySelective(from);
    }
}
