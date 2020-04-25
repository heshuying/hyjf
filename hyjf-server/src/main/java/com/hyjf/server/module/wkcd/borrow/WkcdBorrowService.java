package com.hyjf.server.module.wkcd.borrow;

public interface WkcdBorrowService {
	
	/**
	 * 插入微可车贷资产记录
	 * @param userId
	 * @param wkcdId
	 * @param userName
	 * @param trueName
	 * @param mobile
	 * @param borrowAmount
	 * @param car_no
	 * @param car_type
	 * @param car_shop
	 * @param wkcdStatus
	 * @param wkcdRepayType
	 * @param wkcdBorrowPeriod
	 * @throws Exception
	 */
	public void insertRecord(Integer userId,String wkcdId,String userName,String trueName,String mobile,String rate,String borrowAmount,String car_no,String car_type,String car_shop,String wkcdStatus,String wkcdRepayType,Integer wkcdBorrowPeriod)throws Exception;
    /**
     * 检查是否已经存在资产包
     * @param wkcdId
     * @return  true 已存在
     */
    public boolean checkWkcdId(String wkcdId);
}
