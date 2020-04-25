package com.hyjf.admin.maintenance.paramname;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.ParamNameExample;

@Service
public class ParamNameServiceImpl extends BaseServiceImpl implements ParamNameService {
	
	/**
	 * 获取列表size
	 * 
	 * @return
	 */
	public Integer getRecordListSize(ParamNameBean paramNameBean) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		if (paramNameBean.getNameCdSrch() != null && !"".equals(paramNameBean.getNameCdSrch().trim())) {
			cra.andNameCdEqualTo(paramNameBean.getNameCdSrch());
		}
		if (paramNameBean.getNameClassSrch() != null && !"".equals(paramNameBean.getNameClassSrch().trim())) {
			cra.andNameClassEqualTo(paramNameBean.getNameClassSrch());
		}
		if (paramNameBean.getNameSrch() != null && !"".equals(paramNameBean.getNameSrch().trim())) {
			cra.andNameEqualTo(paramNameBean.getNameSrch());
		}

		Integer count = paramNameMapper.countByExample(example);
		return count;
	}

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	public List<ParamName> getRecordList(ParamNameBean paramNameBean, int limitStart, int limitEnd) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		if (paramNameBean.getNameCdSrch() != null && !"".equals(paramNameBean.getNameCdSrch().trim())) {
			cra.andNameCdEqualTo(paramNameBean.getNameCdSrch());
		}
		if (paramNameBean.getNameClassSrch() != null && !"".equals(paramNameBean.getNameClassSrch().trim())) {
			cra.andNameClassEqualTo(paramNameBean.getNameClassSrch());
		}
		if (paramNameBean.getNameSrch() != null && !"".equals(paramNameBean.getNameSrch().trim())) {
			cra.andNameEqualTo(paramNameBean.getNameSrch());
		}

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}

		return paramNameMapper.selectByExample(example);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	public ParamName getRecord(ParamName record) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		cra.andNameCdEqualTo(record.getNameCd());
		cra.andNameClassEqualTo(record.getNameClass());
//		cra.andNameCdLike(record.getName());

		List<ParamName> list = paramNameMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(ParamNameBean record) {
		int nowTime = GetDate.getNowTime10();
		ParamName paramName = new ParamName();
		paramName.setNameClass(record.getNameClass());
		paramName.setNameCd(record.getNameCd());
		paramName.setName(record.getName());
		if (StringUtils.isEmpty(record.getOther1())) {
			paramName.setOther1(StringUtils.EMPTY);
		} else {
			paramName.setOther1(record.getOther1());
		}
		if (StringUtils.isEmpty(record.getOther2())) {
			paramName.setOther2(StringUtils.EMPTY);
		} else {
			paramName.setOther2(record.getOther2());
		}
		if (StringUtils.isEmpty(record.getOther3())) {
			paramName.setOther3(StringUtils.EMPTY);
		} else {
			paramName.setOther3(record.getOther3());
		}
		if (StringUtils.isNotEmpty(record.getSort())) {
			paramName.setSort(Integer.valueOf(record.getSort()));
		}
		paramName.setCreatetime(String.valueOf(nowTime));
		paramName.setUpdatetime(String.valueOf(nowTime));
		paramName.setDelFlag(CustomConstants.FLAG_NORMAL);
		paramNameMapper.insertSelective(paramName);
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(ParamNameBean record) {
		int nowTime = GetDate.getNowTime10();
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		cra.andNameCdEqualTo(record.getNameCd());
		cra.andNameClassEqualTo(record.getNameClass());

		ParamName paramName = new ParamName();
		paramName.setNameClass(record.getNameClass());
		paramName.setNameCd(record.getNameCd());
		paramName.setName(record.getName());
		if (StringUtils.isEmpty(record.getOther1())) {
			paramName.setOther1(StringUtils.EMPTY);
		} else {
			paramName.setOther1(record.getOther1());
		}
		if (StringUtils.isEmpty(record.getOther2())) {
			paramName.setOther2(StringUtils.EMPTY);
		} else {
			paramName.setOther2(record.getOther2());
		}
		if (StringUtils.isEmpty(record.getOther3())) {
			paramName.setOther3(StringUtils.EMPTY);
		} else {
			paramName.setOther3(record.getOther3());
		}
		if (StringUtils.isNotEmpty(record.getSort())) {
			paramName.setSort(Integer.valueOf(record.getSort()));
		}
		paramName.setCreatetime(String.valueOf(nowTime));
		paramName.setUpdatetime(String.valueOf(nowTime));

		paramNameMapper.updateByExampleSelective(paramName, example);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(ParamName record) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		cra.andNameCdEqualTo(record.getNameCd());
		cra.andNameClassEqualTo(record.getNameClass());
		paramNameMapper.deleteByExample(example);
	}

	/**
	 * 数据是否存在
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */
	@Override
	public boolean isExists(ParamNameBean record) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		cra.andNameCdEqualTo(record.getNameCd());
		cra.andNameClassEqualTo(record.getNameClass());

		List<ParamName> list = paramNameMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
}
