package com.hyjf.admin.manager.content.qualify;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.auto.ContentQualifyExample;
import com.hyjf.mybatis.model.customize.ContentQualifyCustomize;

@Service
public class ContentQualifyServiceImpl extends BaseServiceImpl implements ContentQualifyService {


	/**
	 * 获取配置列表
	 * 
	 * @return
	 */
	public List<ContentQualify> getRecordList(ContentQualify contentQualify, int limitStart, int limitEnd) {
		ContentQualifyExample example = new ContentQualifyExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return contentQualifyMapper.selectByExample(example);
	}

	/**
	 * 获取单个权限维护
	 * 
	 * @return
	 */
	public ContentQualify getRecord(Integer record) {
		ContentQualify contentQualify =contentQualifyMapper.selectByPrimaryKey(record);
		return  contentQualify;
	}

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(ContentQualify record) {
		if (record.getId()==null) {
			return false;
		}
		ContentQualifyExample example = new ContentQualifyExample();
		ContentQualifyExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<ContentQualify> contentQualify = contentQualifyMapper.selectByExample(example);
		if (contentQualify != null && contentQualify.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(ContentQualify record) {
//		record.setImgurl("1");
		record.setCreateTime(GetDate.getDate());
        record.setUpdateTime(GetDate.getDate());
		contentQualifyMapper.insertSelective(record);
	}

	/**
	 * 权限维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(ContentQualify record) {
//		record.setImgurl("1");
		record.setUpdateTime(GetDate.getDate());
		contentQualifyMapper.updateByPrimaryKeySelective(record);
	}
	
	/**
     * 根据主键删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            contentQualifyMapper.deleteByPrimaryKey(id);
        }
    }
    
    /**
     * 根据条件查询数据
     */
    public List<ContentQualify> selectRecordList(ContentQualifyBean form, int limitStart, int limitEnd) {
        ContentQualifyCustomize example = new ContentQualifyCustomize();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        if(form.getName()!=null){
        	example.setName(form.getName());
        }
        if (form.getStatus() != null) {
            example.setStatus(form.getStatus());
        }
        if (form.getStartCreate() != null) {
            example.setStartCreate(GetDate.str2Date(form.getStartCreate(),GetDate.date_sdf));
        }
        if (form.getEndCreate() != null) {
            example.setEndCreate(GetDate.str2Date(form.getEndCreate(),GetDate.date_sdf));
        }

        return contentQualifyCustomizeMapper.selectContentQualify(example);
    }
    

}
