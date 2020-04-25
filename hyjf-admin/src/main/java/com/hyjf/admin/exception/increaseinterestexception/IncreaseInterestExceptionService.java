package com.hyjf.admin.exception.increaseinterestexception;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminIncreaseInterestExceptionCustomize;

/**
 * 融通宝加息异常处理Service
 * 
 * @ClassName IncreaseInterestExceptionService
 * @author liuyang
 * @date 2017年1月5日 下午5:04:30
 */
public interface IncreaseInterestExceptionService extends BaseService {

	/**
	 * 检索列表件数
	 * 
	 * @Title countRecordList
	 * @param form
	 * @return
	 */
	public int countRecordList(IncreaseInterestExceptionBean form);

	/**
	 * 检索列表
	 * 
	 * @Title selectRecordList
	 * @param form
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<AdminIncreaseInterestExceptionCustomize> selectRecordList(IncreaseInterestExceptionBean form, int limitStart, int limitEnd);

	/**
	 * 更新borrowApri表
	 * 
	 * @Title updateBorrowApr
	 * @param form
	 */
	public void updateBorrowApr(IncreaseInterestExceptionBean form);

}
