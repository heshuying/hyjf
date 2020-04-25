/**
 * Description:项目列表查询service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.borrow;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.*;

import java.util.List;
import java.util.Map;

public interface BorrowService extends BaseService {

	/**
	 * 查询项目列表
	 * 
	 * @param params
	 * @return
	 */
	List<WebProjectListCustomize> searchProjectList(Map<String, Object> params);

	/**
	 * 统计项目列表总数
	 * 
	 * @param params
	 * @return
	 */
	int countProjectListRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的个人项目的项目详情
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectPersonDetailCustomize searchProjectPersonDetail(String borrowNid);


	/**
	 * 查询相应的企业项目的项目详情
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectCompanyDetailCustomize searchProjectCompanyDetail(String borrowNid);


	/**
	 * 查询项目的用户出借列表
	 * 
	 * @param params
	 * @return
	 */
	List<WebProjectInvestListCustomize> searchProjectInvestList(Map<String, Object> params);

	/**
	 * 统计用户出借的项目总数
	 * 
	 * @param params
	 * @return
	 */
	int countProjectInvestRecordTotal(Map<String, Object> params);

	/**
	 * 根据项目id获取项目信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectDetailCustomize selectProjectDetail(String borrowNid);

	/**
	 * 根据项目id获取项目信息（预览调用）
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebProjectDetailCustomize selectProjectPreview(String borrowNid);

	/**
	 * 
	 * 查询相应的汇资产信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebHzcProjectDetailCustomize searchHzcProjectDetail(String borrowNid);

	/**
	 * 查询相应的认证信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<WebProjectAuthenInfoCustomize> searchProjectAuthenInfos(String borrowNid);

	/**
	 * 计算获取还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<BorrowRepayPlanCustomBean> getRepayPlan(String borrowNid);

	/**
	 * 根据相应的borrowNid获取相应的项目的处置预案
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebHzcDisposalPlanCustomize searchDisposalPlan(String borrowNid);

	/**
	 * 根据borrowNid获取风控信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	WebRiskControlCustomize selectRiskControl(String borrowNid);

	/**
	 * 根据项目编号获取相应的房屋抵押信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<WebMortgageCustomize> selectMortgageList(String borrowNid);

	/**
	 * 根据项目编号获取相应的车辆抵押信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<WebVehiclePledgeCustomize> selectVehiclePledgeList(String borrowNid);

	/**
	 * 根据项目id获取文件
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<BorrowFileCustomBean> searchProjectFiles(String borrowNid, String url);

	/**
	 * 获取用户出借
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	int countUserInvest(Integer userId, String borrowNid);

	int countUserDebtInvest(Integer userId, String borrowNid);

	DebtBorrow selectDebtBorrowByNid(String borrowNid);

	int countDebtPlanProjectInvestRecordTotal(Map<String, Object> params);

	List<WebProjectInvestListCustomize> searchDeptPlanProjectInvestList(Map<String, Object> params);

	List<BorrowProjectType> getProjectTypeList();
	/**
	 * 
	 * 获取可出借项目列表记录条数（新）
	 * @author pcc
	 * @param params
	 * @return
	 */
    int countProjectListRecordTotalNew(Map<String, Object> params);
    /**
     * 
     * 获取可出借项目列表（新）
     * @author pcc
     * @param params
     * @return
     */
    List<WebProjectListCustomize> searchProjectListNew(Map<String, Object> params);
    /**
     * 获取借款企业信息
     * @param borrowNid
     * @return
     */
    BorrowUsers getBorrowUsersByNid(String borrowNid);
    
    /**
     * 获取借款人信息
     * @param borrowNid
     * @return
     */
    BorrowManinfo getBorrowManinfoByNid(String borrowNid);
    /**
     * 获取房产抵押信息
     * @param borrowNid
     * @return
     */
    List<BorrowHouses> getBorrowHousesByNid(String borrowNid);
    /**
     * 获取车辆抵押信息
     * @param borrowNid
     * @return
     */
    List<BorrowCarinfo> getBorrowCarinfoByNid(String borrowNid);

	List<WebProjectConsumeCustomize> searchProjectConsumeList(Map<String, Object> params);

	int countProjectConsumeRecordTotal(Map<String, Object> params);

	/**
	 * 获取资产项目类型
	 * 
	 * @return
	 */
	HjhAssetBorrowType selectAssetBorrowType(String instCode,Integer assetType);

	/**
     * 查询标的到达定时发标时间的标的编号
     * @param borrowNid 
     * @param ontime
     * @return
	 */
	Integer getOntimeIdByNid(String borrowNid, Integer ontime);
	
	/**
	 * 更新标的状态为预约状态
	 * 
	 * @param borrowId
	 * @return
	 */
	boolean updateOntimeSendBorrow(int borrowId);
	/**
	 * 根据borrowNid返回上市列表
	 * @param borrowNid
	 * @return
	 */
    List<ActdecListedOne> getActdecList(String borrowNid);

	BorrowRepay getBorrowRepay(String borrowNid);
	/**
	 * 根据borrowNid，userId查找承接记录
	 */
	int countCreditTender(Map<String, Object> params);

	/**
	 * 根据borrowNid查找债转记录列表
	 * @param params
	 * @return
	 */
    List<WebProjectUndertakeListCustomize> selectProjectUndertakeList(Map<String, Object> params);

    /**
     * 获取承接记录的总条数
     */
    int countCreditTenderByBorrowNid(String borrowNid);

    /**
     * 获取承接金额
     * @param borrowNid
     * @return
     */
    String sumUndertakeAccount(String borrowNid);

}
