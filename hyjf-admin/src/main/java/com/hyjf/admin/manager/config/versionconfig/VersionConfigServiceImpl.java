package com.hyjf.admin.manager.config.versionconfig;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Version;
import com.hyjf.mybatis.model.auto.VersionExample;

@Service
public class VersionConfigServiceImpl extends BaseServiceImpl implements VersionConfigService {

    /**
     * 获取列表
     *
     * @return
     */
    public List<Version> getRecordList(VersionConfigBean version, int limitStart, int limitEnd) {
    	VersionExample example = new VersionExample();
    	VersionExample.Criteria cra = example.createCriteria();
    	if(StringUtils.isNotEmpty(version.getNameSrh())){
    		cra.andTypeEqualTo(Integer.parseInt(version.getNameSrh()));
    	}
    	if(StringUtils.isNotEmpty(version.getVersionSrh())){
    		cra.andVersionLike(version.getVersionSrh());
    	}
		example.setOrderByClause("id desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return versionMapper.selectByExample(example);
    }

    /**
     * 获取记录数
     */
	public Integer countRecord(VersionConfigBean version) {
    	VersionExample example = new VersionExample();
    	VersionExample.Criteria cra = example.createCriteria();
    	if(StringUtils.isNotEmpty(version.getNameSrh())){
    		cra.andTypeEqualTo(Integer.parseInt(version.getNameSrh()));
    	}
    	if(StringUtils.isNotEmpty(version.getVersionSrh())){
    		cra.andVersionLike(version.getVersionSrh());
    	}
		return versionMapper.countByExample(example);
	}
    /**
     * 获取单个信息
     *
     * @return
     */
    public Version getRecord(Integer id) {
    	VersionExample example = new VersionExample();
    	VersionExample.Criteria cra = example.createCriteria();
    	cra.andIdEqualTo(id);
        List<Version> list = versionMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new Version();
    }

    /**
     * 根据主键判断账户中数据是否存在
     *
     * @return
     */
    public boolean isExistsRecord(Integer id) {
        if (Validator.isNull(id)) {
            return false;
        }
        VersionExample example = new VersionExample();
        VersionExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(id);
        int cnt = versionMapper.countByExample(example);
        return cnt > 0;
    }

	/**
	 * 插入记录
	 */
	public void insertRecord(VersionConfigBean form) {
        Version record = new Version();
        BeanUtils.copyProperties(form, record);
        record.setIsupdate(form.getIsupdate());
        record.setType(form.getType());
        record.setVersion(form.getVersion());
        record.setUrl(form.getUrl());
        record.setContent(form.getContent());
        versionMapper.insertSelective(record);
	}
	/**
	 * 更新记录
	 */
	public void updateRecord(VersionConfigBean form) {
        Version record = new Version();
        BeanUtils.copyProperties(form, record);
        versionMapper.updateByPrimaryKeySelective(record);
	}
	/**
	 * 批量删除
	 */
	public void deleteRecord(List<Integer> ids) {
		VersionExample example = new VersionExample();
		VersionExample.Criteria cra = example.createCriteria();
		cra.andIdIn(ids);
		versionMapper.deleteByExample(example);
	}

	/**
	 * 判断当前系统版本号是否唯一
	 * @param vid
	 * @param type
	 * @param version
	 * @return
	 * @author Michael
	 */
	@Override
	public Version getVersionByCode(Integer vid, Integer type, String version) {
		VersionExample example = new VersionExample();
		VersionExample.Criteria cra = example.createCriteria();
		if(vid != null){
			cra.andIdNotEqualTo(vid);
		}
		cra.andTypeEqualTo(type);
		cra.andVersionEqualTo(version);
		List<Version> list = versionMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
