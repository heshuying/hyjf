package com.hyjf.bank.service.aleve.reverse;

import java.util.List;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;

public interface ReverseService extends BaseService {

	/**
	 * 检查导入的ALEVE数据中是否含有利息相关记录
	 * 
	 * @return
	 */
	public List<AleveLogCustomize> selectAleveReverseList(List<String> tranStype); 

	/**
	 * 检索手动冲正数量
	 * 
	 * @param aleveLog
	 * @return
	 */
	public int countManualReverse(AleveLogCustomize aleveLogCustomize);
	
	/**
	 * 自动冲正
	 * 
	 * @param aleveLog
	 * @return
	 */
	public boolean forReverse(AleveLogCustomize aleveLogCustomize);
}
