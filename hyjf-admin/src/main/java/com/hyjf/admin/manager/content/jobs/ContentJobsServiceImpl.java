package com.hyjf.admin.manager.content.jobs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Jobs;
import com.hyjf.mybatis.model.auto.JobsExample;
import com.hyjf.mybatis.model.customize.ContentJobsCustomize;

@Service
public class ContentJobsServiceImpl extends BaseServiceImpl implements ContentJobsService {

	/**
	 * 获取文章列表列表
	 * 
	 * @return
	 */
	public List<Jobs> getRecordList(Jobs jobs, int limitStart, int limitEnd) {
		JobsExample example = new JobsExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return jobsMapper.selectByExample(example);
	}

	/**
	 * 获取单个文章维护
	 * 
	 * @return
	 */
	public Jobs getRecord(Integer record) {
		Jobs team = jobsMapper.selectByPrimaryKey(record);
		return team;
	}

	/**
	 * 根据主键判断文章维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Jobs record) {
		if (record.getId() == null) {
			return false;
		}
		JobsExample example = new JobsExample();
		JobsExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<Jobs> teams = jobsMapper.selectByExample(example);
		if (teams != null && teams.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 文章维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(Jobs record) {
		record.setCreateTime(GetDate.getNowTime10());
		record.setUpdateTime(GetDate.getNowTime10());
		jobsMapper.insertSelective(record);
	}

	/**
	 * 文章维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(Jobs record) {
		record.setUpdateTime(GetDate.getNowTime10());
		jobsMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据主键删除文章
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			jobsMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 根据条件查询数据
	 */
	public List<Jobs> selectRecordList(ContentJobsBean form, int limitStart, int limitEnd) {
		ContentJobsCustomize example = new ContentJobsCustomize();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		if (StringUtils.isNotEmpty(form.getOfficeName())) {
			example.setOfficeName(form.getOfficeName());
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

		return contentJobsCustomizeMapper.selectContentJobs(example);
	}

}
