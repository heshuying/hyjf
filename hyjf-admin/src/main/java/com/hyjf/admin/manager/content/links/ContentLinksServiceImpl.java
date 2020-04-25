package com.hyjf.admin.manager.content.links;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Links;
import com.hyjf.mybatis.model.auto.LinksExample;
import com.hyjf.mybatis.model.customize.LinksCustomize;

@Service
public class ContentLinksServiceImpl extends BaseServiceImpl implements ContentLinksService {

	/**
	 * 获取文章列表列表
	 * 
	 * @return
	 */
	public List<Links> getRecordList(ContentLinksBean bean, int limitStart, int limitEnd) {
		LinksExample example = new LinksExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		LinksExample.Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(bean.getType());
		// 条件查询
		if (StringUtils.isNotEmpty(bean.getSearchWebname())) {
			criteria.andWebnameLike("%" + bean.getSearchWebname() + "%");
		}
		if (bean.getStatus() != null) {
			criteria.andStatusEqualTo(bean.getStatus());
		}
		if (StringUtils.isNotEmpty(bean.getStartCreate())) {
			criteria.andCreateTimeGreaterThanOrEqualTo(Integer.valueOf(GetDate.get10Time(bean.getStartCreate())));
		}
		if (StringUtils.isNotEmpty(bean.getEndCreate())) {
			criteria.andCreateTimeLessThanOrEqualTo(Integer.valueOf(GetDate.get10Time(bean.getEndCreate())));
		}
		example.setOrderByClause("`partner_type` ASC,`order` Asc,`create_time` Desc");
		return linksMapper.selectByExample(example);
	}

	/**
	 * 获取单个文章维护
	 * 
	 * @return
	 */
	public Links getRecord(Short record) {
		Links links = linksMapper.selectByPrimaryKey(record);
		return links;
	}

	/**
	 * 根据主键判断文章维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Links record) {
		if (record.getId() == null) {
			return false;
		}
		LinksExample example = new LinksExample();
		LinksExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<Links> linksList = linksMapper.selectByExample(example);
		if (linksList != null && linksList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 文章维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(Links record) {
		record.setOrder(record.getOrder().shortValue());
		record.setStatus(record.getStatus().shortValue());
		record.setCreateTime(GetDate.getNowTime10());
		record.setUpdateTime(GetDate.getNowTime10());
		fillEmpty(record);
		linksMapper.insertSelective(record);
	}

	/**
	 * 文章维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(Links record) {
		record.setOrder(record.getOrder().shortValue());
		record.setStatus(record.getStatus().shortValue());
		// record.setCreateTime(record.getCreateTime());
		record.setUpdateTime(GetDate.getNowTime10());
		fillEmpty(record);
		linksMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 填充空值
	 * 
	 * @param record
	 */
	private void fillEmpty(Links record) {
		if (StringUtils.isEmpty(record.getPhone())) {
			record.setPhone("");
		}
		if (StringUtils.isEmpty(record.getAddress())) {
			record.setAddress("");
		}
		if (StringUtils.isEmpty(record.getSetupTime())) {
			record.setSetupTime("");
		}
		if (StringUtils.isEmpty(record.getCooperationTime())) {
			record.setCooperationTime("");
		}
		if (record.getPartnerType() == null) {
			record.setPartnerType(0);
		}
		if (record.getHits() == null) {
			record.setHits(0);
		}
		if (record.getIsindex() == null) {
			record.setIsindex(0);
		}
	}

	/**
	 * 根据主键删除文章
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			linksMapper.deleteByPrimaryKey(id.shortValue());
		}
	}

	/**
	 * 根据条件查询数据
	 */
	public List<Links> selectRecordList(ContentLinksBean form, int limitStart, int limitEnd) {
		LinksCustomize example = new LinksCustomize();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		if (form.getStatus() != null) {
			example.setStatus(form.getStatus());
		}
		if (form.getStartCreate() != null) {
			example.setStartCreate(Integer.valueOf(GetDate.get10Time(form.getStartCreate())));
		}
		if (form.getEndCreate() != null) {
			example.setStartCreate(Integer.valueOf(GetDate.get10Time(form.getEndCreate())));
		}
		// 获取活动名
		if (StringUtils.isNotEmpty(form.getSearchWebname())) {
			example.setWebname(form.getSearchWebname());
		}
		return linksCustomizeMapper.selectContentLinks(example);
	}

}
