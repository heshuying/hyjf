package com.hyjf.admin.manager.activity.twoeleven2018;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.TwoelevenRewardMapper;
import com.hyjf.mybatis.mapper.customize.TwoelevenCustomizeMapper;
import com.hyjf.mybatis.model.auto.TwoelevenReward;
import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/10/10.
 */
@Service
public class TwoelevenServiceImpl extends BaseServiceImpl implements TwoelevenService {


    @Autowired
    private TwoelevenCustomizeMapper twoelevenCustomizeMapper;

    @Autowired
    private TwoelevenRewardMapper twoelevenRewardMapper;


    /**
     * 查询秒杀条数
     * @param map
     * @return
     */
    @Override
    public Integer selectTwoelevenSeckillCount( Map<String, Object> map){
        return twoelevenCustomizeMapper.selectTwoelevenSeckillCount(map);
    }
    /**
     * 查询秒杀列表
     * @param map
     * @return
     */
    @Override
    public List<TwoelevenCustomize> selectTwoelevenSeckillList(Map<String, Object> map){
        return twoelevenCustomizeMapper.selectTwoelevenSeckillList(map);
    }


    /**
     * 年化出借金额 查询条数
     * @param map
     * @return
     */
    @Override
    public Integer selectTwoelevenInvestCount( Map<String, Object> map){
        return twoelevenCustomizeMapper.selectTwoelevenInvestCount(map);
    }
    /**
     * 年化出借金额 查询列表
     * @param map
     * @return
     */
    @Override
    public List<TwoelevenCustomize> selectTwoelevenInvestList(Map<String, Object> map){
        return twoelevenCustomizeMapper.selectTwoelevenInvestList(map);
    }

    /**
     * 奖励（新款iPhone）明细查询条数
     * @param map
     * @return
     */
    @Override
    public Integer selectTwoelevenRewardCount( Map<String, Object> map){
        return twoelevenCustomizeMapper.selectTwoelevenRewardCount(map);
    }
    /**
     * 奖励（新款iPhone）明细 查询列表
     * @param map
     * @return
     */
    @Override
    public List<TwoelevenCustomize> selectTwoelevenRewardList(Map<String, Object> map){
        return twoelevenCustomizeMapper.selectTwoelevenRewardList(map);
    }
    /**
     * 奖励（新款iPhone）明细修改
     * @param record
     * @return
     */
    @Override
    public int updateTwoelevenReward(TwoelevenReward record){
        return twoelevenRewardMapper.updateByPrimaryKeySelective(record);
    }
}
