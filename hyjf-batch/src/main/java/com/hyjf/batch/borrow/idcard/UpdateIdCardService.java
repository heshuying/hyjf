package com.hyjf.batch.borrow.idcard;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminChinapnrLogCustomize;

/**
 * 调用汇付接口,更新银行卡信息接口
 * 
 * @author wangkun
 * @since 2016年1月18日 上午8:42:33
 */
public interface UpdateIdCardService extends BaseService {

	/**
	 * @return
	 */
		
	List<AdminChinapnrLogCustomize> getAllOpenAccount();

	/**
	 * gem
	 * @param accountChinapnr
	 * @return
	 */
	int updateIdCard(AdminChinapnrLogCustomize accountChinapnr);
	
}
