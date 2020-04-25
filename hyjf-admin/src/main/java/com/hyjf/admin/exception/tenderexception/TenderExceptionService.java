package com.hyjf.admin.exception.tenderexception;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.AdminTenderExceptionCustomize;

public interface TenderExceptionService extends BaseService {

	/**
	 * 明细列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public List<AdminTenderExceptionCustomize> selectTenderExceptionList(
			AdminTenderExceptionCustomize TenderExceptionCustomize);

	/**
	 * 明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countTenderException(AdminTenderExceptionCustomize TenderExceptionCustomize);

	/**
	 * 金额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public String sumTenderExceptionAccount(AdminTenderExceptionCustomize TenderExceptionCustomize);

	/**
	 * 渠道下拉列表
	 * 
	 * @return
	 */
	public List<UtmPlat> getUtmPlatList();

	/**
	 * 出借爆标处理
	 * 
	 * @param nid
	 * @return
	 */
	public String updateBackTender(TenderExceptionBean form) throws Exception;

	/**
	 * 解冻订单号在本库中是否存在
	 * 
	 * @param trxId
	 * @return
	 */
	public boolean selsectNidIsExists(String nid);
}
