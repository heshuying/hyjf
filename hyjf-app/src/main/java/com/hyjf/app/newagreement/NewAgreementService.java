package com.hyjf.app.newagreement;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;

public interface NewAgreementService extends BaseService {
	
	/**
	 * 用户中心债转被出借的协议
	 * @param userId 
	 * 
	 * @return
	 */
	public JSONObject selectUserCreditContract(NewCreditAssignedBean tenderCreditAssignedBean, Integer userId);
	
	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId);
	
    /**
     * 
     * 查询用户汇计划出借明细
     * @author pcc
     * @param params
     * @return
     */
    UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params);

    List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize);

	Integer selectBorrowerByBorrowNid(String borrowNid);
	BigDecimal getAccedeAccount(String accedeOrderId);

	/**
	 * 获取债转承接信息
	 * @param nid
	 * @return
	 */
    HjhDebtCreditTender getHjhDebtCreditTender(Integer nid);

	/**
	 * 获取债转信息
	 * @param creditNid
	 * @return
	 */
	HjhDebtCredit getHjhDebtCreditByCreditNid(String creditNid);
	
	/**
	 * 
	 * 新版获取居间服务借款协议展示内容信息
	 * @author pcc
	 * @param userId
	 * @param type
	 * @param tenderNid
	 * @param borrowNid
	 * @return
	 */
    public JSONObject interServiceLoanAgreement(Integer userId, String tenderNid, String borrowNid);
    /**
     * 
     * 根据出借订单号获取债转承接信息
     * @author pcc
     * @param nid
     * @return
     */
    public HjhDebtCreditTender getHjhDebtCreditTenderByCreditNid(String assignOrderId);
    /**
     * 
     * 根据出借订单号查询出借信息
     * @author pcc
     * @param investOrderId
     * @return
     */
    public BorrowTender getBorrowTenderByNid(String investOrderId);
    /**
     * 获取债转信息
     * @param creditNid
     * @return
     */
    public CreditTender getCreditTenderByCreditNid(String nid);

	/**
	 * 查询协议图片
	 *
	 * @param protocolId 协议模版的ID
	 * @return
	 */
	public List<String> getImgUrlList(String protocolId) throws  Exception;

	/**
	 * 获得对应的协议模板pdf路径
	 * @param protocolId 协议模版的ID
	 * @return
	 */
	public String getAgreementPdf(String protocolId) throws  Exception;

	/**
	 * 往Redis中放入协议模板内容
	 *
	 * @param displayName
	 * @return
	 */
	public boolean setRedisProtocolTemplate(String displayName);

	/**
	 * 获得协议模板图片
	 * @param aliasName
	 * @return
	 */
	public NewAgreementResultBean setProtocolImg(String aliasName);

	/**
	 * 协议名称 动态获得
	 *
	 * @return
	 */
	List<ProtocolTemplate> getdisplayNameDynamic();
}
