package com.hyjf.admin.finance.pushMoney;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.TenderCommission;
import com.hyjf.mybatis.model.customize.PushMoneyCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface PushMoneyManageService extends BaseService {

	/**
	 * 提成管理 （数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryPushMoneyCount(PushMoneyCustomize pushMoneyCustomize) ;

	/**
	 * 提成管理 （列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<PushMoneyCustomize> queryPushMoneyList(PushMoneyCustomize pushMoneyCustomize) ;

    /**
     * 提成明细 （数量）
     * @param accountManageBean
     * @return
     */
    public Integer queryPushMoneyDetailCount(PushMoneyCustomize pushMoneyCustomize) ;

    /**
     * 提成明细 （列表）
     * @param accountManageBean
     * @return
     */
    public List<PushMoneyCustomize> queryPushMoneyDetail(PushMoneyCustomize pushMoneyCustomize) ;

	/**
	 * 根据主键查找待提成数据
	 */
	public TenderCommission queryTenderCommissionByPrimaryKey(Integer id);

    /**
     * 提成处理
     *
     * @param form
     * @return
     */
    public int insertTenderCommissionRecord(Integer apicornId, PushMoneyManageBean form);

    /**
     * 发提成处理
     *
     * @param form
     * @return
     */
    public int updateTenderCommissionRecord(TenderCommission commission, BankCallBean resultBean);

    /**
     * 取得借款API表
     * @param borrowNid
     * @return
     */
    public BorrowApicron getBorrowApicronBorrowNid(String borrowNid);
    
    /**
     * 根据用户id查询其在crm中的员工属性
     * @param id
     * @return
     */
    public Integer queryCrmCuttype(Integer userid);


    /**
     * 查询金额总计 
     * @param id
     * @return
     */
	public Map<String, Object> queryPushMoneyTotle(
			PushMoneyCustomize pushMoneyCustomize);
    
    

}

