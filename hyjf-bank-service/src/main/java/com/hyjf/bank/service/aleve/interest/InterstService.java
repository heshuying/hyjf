package com.hyjf.bank.service.aleve.interest;

import java.util.List;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;

public interface InterstService extends BaseService {

	/**
	 * 检查导入的ALEVE数据中是否含有利息相关记录
	 * 
	 * @return
	 */
	public List<AleveLogCustomize> selectAleveInterstList(List<String> tranStype); 
	
	/**
	 * 网站收支中插入对应入账记录
	 * 
	 * @return
	 */
	public boolean insertAccountWebList(AleveLogCustomize aleveLogCustomize);
	
	/**
	 * 插入资产明细
	 * 
	 * @param aleveLog
	 * @return
	 */
	public boolean insertAccountList(AleveLogCustomize aleveLogCustomize);
	
	/**
	 * 更新公司账户余额
	 * 
	 * @param aleveLog
	 * @return
	 */
	public boolean updateMerchantAccount(AleveLogCustomize aleveLogCustomize);
	
	/**
	 * 获取Admin信息
	 * 
	 * @return
	 */
	public Admin getAdminId();
}
