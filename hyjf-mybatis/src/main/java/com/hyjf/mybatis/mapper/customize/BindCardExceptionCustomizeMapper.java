package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.BindCardExceptionCustomize;

/**
 * 绑卡掉单异常处理CustomizeMapper
 * 
 * @author liuyang
 *
 */
public interface BindCardExceptionCustomizeMapper {

	/**
	 * 检索用户银行卡列表件数
	 * 
	 * @param param
	 * @return
	 */
	public int countBankCardList(Map<String, Object> param);

	/**
	 * 检索用户银行卡列表
	 * 
	 * @param param
	 * @return
	 */
	public List<BindCardExceptionCustomize> selectBankCardList(Map<String, Object> param);

}
