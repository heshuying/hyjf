package com.hyjf.admin.app.maintenance.borrowimage;

import java.util.List;

import com.hyjf.mybatis.model.auto.AppBorrowImage;

public interface AppBorrowImageService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<AppBorrowImage> getRecordList(AppBorrowImage config, int limitStart, int limitEnd);

	/**
	 * 获取单个
	 * 
	 * @return
	 */
	public AppBorrowImage getRecord(Integer integer);

    /**
     * 根据产品编号获取
     * 
     * @return
     */
    public AppBorrowImage getRecords(String borrowImage);

	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(AppBorrowImage record) throws Exception;

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(AppBorrowImage record) throws Exception;

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(Integer id);
}
