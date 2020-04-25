package com.hyjf.admin.wkcd.wkcdborrow;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.WkcdBorrow;

public interface WkcdBorrowService {
	/**
	 * 获取总记录条数
	 * 
	 * @param param
	 * @return
	 */
	public int countRecordTotal(String mobile,String userName,Integer status);

	/**
	 * 获取所有注册记录
	 * 
	 * @param param
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<WkcdBorrow> searchRecord(String mobile,String userName,Integer status,int limitStart, int limitEnd);
	
	/**
	 * 根据ID获取
	 * @param id
	 * @return
	 */
	public WkcdBorrow findById(Integer id);
	
	/**
	 * 审核
	 * @param verify
	 * @param yijian
	 * @param id
	 */
	public Map<String, Object> verify(Integer verify,String yijian,Integer id,Integer userId);
	/**
	 * 微可车贷信息保存BorrowNid
	 * @return
	 */
	public String saveBorrowNid(Map<String, Object> map);
}
