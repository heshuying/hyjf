package com.hyjf.admin.promotion.utm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;

@Service
public class UtmServiceImpl extends BaseServiceImpl implements UtmService {

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	@Override
	public List<UtmPlat> getRecordList(UtmBean utmBean, int limitStart, int limitEnd) {

		UtmPlatExample example = new UtmPlatExample();
		UtmPlatExample.Criteria cra = example.createCriteria();

		if (StringUtils.isNotEmpty(utmBean.getTimeStartSrch())) {
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.getDayStart(utmBean.getTimeStartSrch()));
		}
		if (StringUtils.isNotEmpty(utmBean.getTimeEndSrch())) {
			cra.andCreateTimeLessThanOrEqualTo(GetDate.getDayEnd(utmBean.getTimeEndSrch()));
		}
		if(StringUtils.isNotEmpty(utmBean.getSourceTypeSrch())){
			cra.andSourceTypeEqualTo(Integer.parseInt(utmBean.getSourceTypeSrch()));
		}
		if (utmBean.getAttornFlag() != null) {
			cra.andAttornFlagEqualTo(utmBean.getAttornFlag());
		}
		if (StringUtils.isNotEmpty(utmBean.getSourceId())) {
			cra.andSourceIdEqualTo(Integer.parseInt(utmBean.getSourceId()));
		}
		if (StringUtils.isNotEmpty(utmBean.getSourceIdSrch())) {
			cra.andSourceIdEqualTo(Integer.parseInt(utmBean.getSourceIdSrch()));
		}

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}

		return utmPlatMapper.selectByExample(example);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	@Override
	public UtmPlat getRecord(String record) {
		UtmPlat utmPlat = utmPlatMapper.selectByPrimaryKey(Integer.valueOf(record));
		return utmPlat;
	}

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	@Override
	public int sourceIdIsExists(String record) {
		if (!GenericValidator.isInt(record) || !NumberUtils.isNumber(record) || Integer.valueOf(record) < 0 || StringUtils.isEmpty(record)) {
			return 1;
		}

		UtmPlatExample example = new UtmPlatExample();
		UtmPlatExample.Criteria cra = example.createCriteria();
		cra.andSourceIdEqualTo(Integer.valueOf(record));
		List<UtmPlat> utmPlatList = this.utmPlatMapper.selectByExample(example);
		if (utmPlatList != null && utmPlatList.size() > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	@Override
	public int sourceNameIsExists(String sourceName, String sourceId) {
		UtmPlatExample example = new UtmPlatExample();
		UtmPlatExample.Criteria cra = example.createCriteria();
		cra.andSourceNameEqualTo(sourceName);
		if (StringUtils.isNotEmpty(sourceId)) {
			cra.andSourceIdNotEqualTo(Integer.valueOf(sourceId));
		}
		List<UtmPlat> utmPlatList = this.utmPlatMapper.selectByExample(example);
		if (utmPlatList != null && utmPlatList.size() > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 维护插入
	 * 
	 * @param utmBean
	 */
	@Override
	public void insertRecord(UtmBean utmBean) {
		String nowDate = GetDate.getServerDateTime(6, new Date());
		UtmPlat record = new UtmPlat();
		record.setSourceId(Integer.valueOf(utmBean.getSourceId()));
		record.setSourceName(utmBean.getSourceName());
		record.setDelFlag(utmBean.getDelFlag());
		record.setSourceType(utmBean.getSourceType());
		record.setAttornFlag(utmBean.getAttornFlag());
		if (StringUtils.isNotEmpty(utmBean.getRemark())) {
			record.setRemark(utmBean.getRemark());
		} else {
			record.setRemark(StringUtils.EMPTY);
		}

		record.setCreateTime(nowDate);
		record.setUpdateTime(nowDate);
		utmPlatMapper.insertSelective(record);
	}

	/**
	 * 维护更新
	 * 
	 * @param utmBean
	 */
	@Override
	public void updateRecord(UtmBean utmBean) {
		String nowDate = GetDate.getServerDateTime(6, new Date());
		UtmPlat record = new UtmPlat();
		record.setId(Integer.valueOf(utmBean.getId()));
		record.setSourceId(Integer.valueOf(utmBean.getSourceId()));
		record.setSourceName(utmBean.getSourceName());
		record.setDelFlag(utmBean.getDelFlag());
		record.setSourceType(utmBean.getSourceType());
		record.setAttornFlag(utmBean.getAttornFlag());
		if (StringUtils.isNotEmpty(utmBean.getRemark())) {
			record.setRemark(utmBean.getRemark());
		} else {
			record.setRemark(StringUtils.EMPTY);
		}

		record.setUpdateTime(nowDate);
		utmPlatMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param sourceId
	 */
	@Override
	public void deleteRecord(String sourceId) {
		utmPlatMapper.deleteByPrimaryKey(Integer.valueOf(sourceId));
	}

	/**
	 * 获取渠道列表
	 * @return
	 */
	@Override
	public List<UtmPlat> getUtm() {
		UtmPlatExample utmPlat = new UtmPlatExample();
		List<UtmPlat> list = utmPlatMapper.selectByExample(utmPlat);
		if(list == null){
			list = new ArrayList<UtmPlat>();
		}
		return list;
	}
}
