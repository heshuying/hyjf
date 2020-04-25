package com.hyjf.admin.promotion.keycount;

import java.util.List;

import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.KeyCountCustomize;

public interface KeyCountService {
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public Integer countList(KeyCountCustomize keyCountCustomize);

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	public List<KeyCountCustomize> getRecordList(KeyCountCustomize keyCountCustomize);

	/**
	 * 渠道
	 * 
	 * @return
	 */
	public List<UtmPlat> getUtmPlat();

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<KeyCountCustomize> exportList(KeyCountCustomize keyCountCustomize);
}
