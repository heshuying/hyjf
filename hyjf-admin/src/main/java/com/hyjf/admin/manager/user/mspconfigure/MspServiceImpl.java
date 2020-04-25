package com.hyjf.admin.manager.user.mspconfigure;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.MspConfigure;

@Service
public class MspServiceImpl extends BaseServiceImpl implements MspService {

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	@Override
	public List<MspConfigure> getRecordList(Map<String, Object> conditionMap,  int limitStart, int limitEnd) {
		
		if (limitStart == 0 || limitStart > 0) {
			conditionMap.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			conditionMap.put("limitEnd", limitEnd);
		}
		List<MspConfigure> list = mspConfigureMapper.selectAssetListList(conditionMap);
		return list;
	}
	/**
	 * 获取记录数
	 * @param form
	 * @return
	 * @author LiuBin
	 */
	@Override
	public Integer getRecordCount(Map<String, Object> conditionMap) {
		return mspConfigureMapper.countAssetList(conditionMap);
	}
	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	@Override
	public MspConfigure getRecord(String record) {
		MspConfigure utmPlat = mspConfigureMapper.selectByPrimaryKey(Integer.valueOf(record));
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

//		UtmPlatExample example = new UtmPlatExample();
//		UtmPlatExample.Criteria cra = example.createCriteria();
//		cra.andSourceIdEqualTo(Integer.valueOf(record));
//		List<UtmPlat> utmPlatList = this.utmPlatMapper.selectByExample(example);
//		if (utmPlatList != null && utmPlatList.size() > 0) {
//			return 1;
//		}
		return 0;
	}

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	@Override
	public int sourceNameIsExists(MspConfigure mspConfigure) {
		List<MspConfigure> utmPlatList = this.mspConfigureMapper.sourceNameIsExists(mspConfigure);
		if (utmPlatList != null && utmPlatList.size() > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	@Override
	public void insertRecord(MspConfigure mspConfigure) {
		long date=new Date().getTime();
		mspConfigure.setCreateTime((int)date);
		mspConfigure.setUpdateTime((int)date);
		mspConfigureMapper.insertSelective(mspConfigure);
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	@Override
	public void updateRecord(MspConfigure mspConfigure) {
		long date=new Date().getTime();
		mspConfigure.setUpdateTime((int)date);
		mspConfigureMapper.updateByPrimaryKeySelective(mspConfigure);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(String sourceId) {
		mspConfigureMapper.deleteByPrimaryKey(Integer.valueOf(sourceId));
	}
}
