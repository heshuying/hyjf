package com.hyjf.admin.finance.pushmoneyhjh;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.admin.manager.hjhplan.accedelist.AccedeListBean;
import com.hyjf.mybatis.model.auto.TenderCommission;
import com.hyjf.mybatis.model.customize.PushMoneyCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface PushMoneyManageHjhService extends BaseService {

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
     * 发提成处理
     *
     * @param form
     * @return
     */
    public int updateTenderCommissionRecord(TenderCommission commission, BankCallBean resultBean, ChinapnrBean chinapnrBean);

    
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
	public Map<String, Object> queryPushMoneyTotle(PushMoneyCustomize pushMoneyCustomize);

	public PushMoneyManageHjhBean queryAccoundInfo(String planOrderId);
    
    

}

