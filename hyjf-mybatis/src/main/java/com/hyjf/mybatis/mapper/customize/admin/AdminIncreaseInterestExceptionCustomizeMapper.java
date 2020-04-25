package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestExceptionCustomize;

/**
 * 融通宝加息异常处理CustomizeMapper
 * @ClassName AdminIncreaseInterestExceptionCustomizeMapper
 * @author liuyang
 * @date 2017年1月6日 上午10:49:23
 */
public interface AdminIncreaseInterestExceptionCustomizeMapper {

	/**
	 * 检索列表件数
	 * 
	 * @Title countRecordList
	 * @param param
	 * @return
	 */
	public int countRecordList(Map<String, Object> param);

	/**
	 * 检索列表
	 * 
	 * @Title selectRecordList
	 * @param param
	 * @return
	 */
	public List<AdminIncreaseInterestExceptionCustomize> selectRecordList(Map<String, Object> param);

}
