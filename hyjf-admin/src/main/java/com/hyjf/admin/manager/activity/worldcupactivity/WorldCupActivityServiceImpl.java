package com.hyjf.admin.manager.activity.worldcupactivity;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.worldcup.GuessingActivitieCustomize;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/6/13.
 */
@Service
public class WorldCupActivityServiceImpl extends BaseServiceImpl implements WorldCupActivityService{


    /**
     * 按条件查询竞猜输赢个数
     *
     * @return
     */
    @Override
    public Integer countRecordBySearchCon(WorldCupActivityBean form){
        Map<String, Object> map = new HashMap<String, Object>();
        if(form ==null ){
            return null;
        }
        String userName = form.getUsername();
        String mobile = form.getMobile();
        if(StringUtils.isNotBlank(userName)){
            map.put("userName",userName);
        }
        if(StringUtils.isNotBlank(mobile)){
            map.put("mobile",mobile);
        }
        return worldCupActivityCustomizeMapper.countRecordBySearchCon(map);
    }

    /**
     * 获取竞猜输赢活动列表
     *
     * @return
     */
    @Override
    public List<GuessingActivitieCustomize>  getRecordList(WorldCupActivityBean form, int limitStart, int limitEnd){
        Map<String, Object> map = new HashMap<String, Object>();
        if(form ==null ){
            return null;
        }
        String userName = form.getUsername();
        String mobile = form.getMobile();
        if(StringUtils.isNotBlank(userName)){
            map.put("userName",userName);
        }
        if(StringUtils.isNotBlank(mobile)){
            map.put("mobile",mobile);
        }
        map.put("limitStart",limitStart);
        map.put("limitEnd",limitEnd);
        return worldCupActivityCustomizeMapper.getRecordList(map);

    }

    /**
     * 按条件查询竞猜冠军个数
     *
     * @return
     */
    @Override
    public Integer countChampionRecordBySearchCon(WorldCupActivityBean form){
        Map<String, Object> map = new HashMap<String, Object>();
        if(form ==null ){
            return null;
        }
        String userName = form.getUsername();
        String mobile = form.getMobile();
        if(StringUtils.isNotBlank(userName)){
            map.put("userName",userName);
        }
        if(StringUtils.isNotBlank(mobile)){
            map.put("mobile",mobile);
        }
        return worldCupActivityCustomizeMapper.countChampionRecordBySearchCon(map);
    }

    /**
     * 获取竞猜冠军活动列表
     *
     * @return
     */
    @Override
    public List<GuessingActivitieCustomize>  getChampionRecordList(WorldCupActivityBean form, int limitStart, int limitEnd){
        Map<String, Object> map = new HashMap<String, Object>();
        if(form ==null ){
            return null;
        }
        String userName = form.getUsername();
        String mobile = form.getMobile();
        if(StringUtils.isNotBlank(userName)){
            map.put("userName",userName);
        }
        if(StringUtils.isNotBlank(mobile)){
            map.put("mobile",mobile);
        }
        map.put("limitStart",limitStart);
        map.put("limitEnd",limitEnd);
        return worldCupActivityCustomizeMapper.getChampionRecordList(map);
    }


}
