package com.hyjf.admin.manager.content.team;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Team;
import com.hyjf.mybatis.model.auto.TeamExample;
import com.hyjf.mybatis.model.customize.ContentTeamCustomize;

@Service
public class ContentTeamServiceImpl extends BaseServiceImpl implements ContentTeamService {


    /**
     * 获取列表
     * 
     * @return
     */
    public List<Team> getRecordList(Team team, int limitStart, int limitEnd) {
    	TeamExample example = new TeamExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        return teamMapper.selectByExample(example);
    }

    /**
     * 获取单个
     * 
     * @return
     */
    public Team getRecord(Integer record) {
        Team team = teamMapper.selectByPrimaryKey(record);
        return team;
    }

    /**
     * 根据主键判断数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(Team record) {
        if (record.getId() == null) {
            return false;
        }
        TeamExample example = new TeamExample();
        TeamExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<Team> teams = teamMapper.selectByExample(example);
        if (teams != null && teams.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 插入
     * 
     * @param record
     */
    public void insertRecord(Team record) {
        record.setCreateTime(GetDate.getNowTime10());
        record.setImgappurl(record.getImgurl());
        record.setUpdateTime(GetDate.getNowTime10());
        teamMapper.insertSelective(record);
    }

    /**
     * 更新
     * 
     * @param record
     */
    public void updateRecord(Team record) {
        record.setImgappurl(record.getImgurl());
        record.setCreateTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
        teamMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    /**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
        	teamMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据条件查询数据
     */
    public List<Team> selectRecordList(ContentTeamBean form, int limitStart, int limitEnd) {
        ContentTeamCustomize example = new ContentTeamCustomize();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        if(StringUtils.isNotEmpty(form.getName())){
        	example.setName(form.getName());
        }
        if(StringUtils.isNotEmpty(form.getPosition())){
        	example.setPosition(form.getPosition());
        }
        if (form.getStatus() != null) {
            example.setStatus(form.getStatus());
        }
        if (StringUtils.isNotEmpty(form.getStartCreate())) {
            example.setStartCreate(Integer.valueOf(GetDate.get10Time(form.getStartCreate())));
        }
        if (StringUtils.isNotEmpty(form.getEndCreate())) {
            example.setEndCreate(Integer.valueOf(GetDate.get10Time(form.getEndCreate())));
        }
        return contentTeamCustomizeMapper.selectContentTeam(example);
    }

}
