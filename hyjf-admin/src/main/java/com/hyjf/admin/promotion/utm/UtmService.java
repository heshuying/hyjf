package com.hyjf.admin.promotion.utm;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;

public interface UtmService extends BaseService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<UtmPlat> getRecordList(UtmBean utmBean, int limitStart, int limitEnd);

	/**
	 * 获取单表
	 * 
	 * @return
	 */
	public UtmPlat getRecord(String id);

	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(UtmBean record);

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	public int sourceIdIsExists(String record);

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	public int sourceNameIsExists(String sourceName, String sourceId);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(UtmBean record);

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String sendCd);

	/**
	 * 获取渠道列表
	 * @return
	 */
	List<UtmPlat> getUtm();
}