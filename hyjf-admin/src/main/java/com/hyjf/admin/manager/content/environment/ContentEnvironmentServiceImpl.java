package com.hyjf.admin.manager.content.environment;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ContentEnvironment;
import com.hyjf.mybatis.model.auto.ContentEnvironmentExample;

@Service
public class ContentEnvironmentServiceImpl extends BaseServiceImpl implements ContentEnvironmentService {

    /**
     * 获取活动列表列表
     * 
     * @return
     */
    public List<ContentEnvironment> getRecordList(ContentEnvironmentBean bean, int limitStart, int limitEnd) {
        ContentEnvironmentExample example = new ContentEnvironmentExample();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("update_time");
        com.hyjf.mybatis.model.auto.ContentEnvironmentExample.Criteria criteria = example.createCriteria();
        // 条件查询
        if (StringUtils.isNotEmpty(bean.getName())) {
            criteria.andNameLike("%" + bean.getName() + "%");
        }
        if (bean.getStatus() != null) {
            criteria.andStatusEqualTo(bean.getStatus());
        }
        if (bean.getImgtype() != null) {
            criteria.andImgtypeEqualTo(bean.getImgtype());
        }
        if (StringUtils.isNotEmpty(bean.getStartCreate()) && StringUtils.isNotEmpty(bean.getEndCreate())) {
            criteria.andCreateTimeBetween(GetDate.str2Timestamp(bean.getStartCreate()),
                    GetDate.str2Timestamp(bean.getEndCreate()));
        }
        return contentEnvironmentMapper.selectByExample(example);
    }

    /**
     * 获取单个权限维护
     * 
     * @return
     */
    public ContentEnvironment getRecord(Integer record) {
        ContentEnvironment environment = contentEnvironmentMapper.selectByPrimaryKey(record);
        return environment;
    }

    /**
     * 根据主键判断权限维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(ContentEnvironment record) {
        if (record.getId() == null) {
            return false;
        }
        ContentEnvironmentExample example = new ContentEnvironmentExample();
        ContentEnvironmentExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<ContentEnvironment> ContentEnvironmentList = contentEnvironmentMapper.selectByExample(example);
        if (ContentEnvironmentList != null && ContentEnvironmentList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 权限维护插入
     * 
     * @param record
     */
    public void insertRecord(ContentEnvironment record) {
        record.setCreateTime(GetDate.getDate());
        record.setUpdateTime(GetDate.getDate());
        contentEnvironmentMapper.insertSelective(record);
    }

    /**
     * 权限维护更新
     * 
     * @param record
     */
    public void updateRecord(ContentEnvironment record) {
        record.setUpdateTime(GetDate.getDate());
        contentEnvironmentMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除环境
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            contentEnvironmentMapper.deleteByPrimaryKey(id);
        }
    }

}
