/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2018
 * Company: HYJF Corporation
 * @author: PC-LIUSHOUYI
 * @version: 1.0
 * Created at: 2018年1月19日 上午9:30:11
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.ManualReverseCustomize;

/**
 * @author PC-LIUSHOUYI
 */

public interface ManualReverseCustomizeMapper {
	/**
	 * 检索手动冲正数量
	 * 
	 * @param param
	 * @return
	 */
	public Integer countManualReverse(Map<String, Object> param);
	/**
	 * 检索手动冲正数据
	 * 
	 * @param param
	 * @return
	 */
	public List<ManualReverseCustomize> selectManualReverseList(Map<String, Object> param);
	/**
	 * 添加手动冲正数据
	 * 
	 * @param param
	 * @return
	 */
	public void insertManualReverse(ManualReverseCustomize manualReverseCustomize);
	/**
	 * 通过用户账户信息查询用户信息
	 * 
	 * @param param
	 * @return
	 */
	public List<String> selectUserIdsByAccount(Map<String, Object> param);
	/**
	 * 通过用户账户信息查询用户信息
	 * 
	 * @param param
	 * @return
	 */
	public String selectUserNamebyAccountId(Map<String, Object> param);
	/**
	 * 查询原体现交易数据
	 * 
	 * @param param
	 * @turn
	 */
	public Integer countAccountList(Map<String, Object> param);
}

	