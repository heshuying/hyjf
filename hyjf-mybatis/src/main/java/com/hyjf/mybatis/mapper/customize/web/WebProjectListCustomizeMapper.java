/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.web;

import com.hyjf.mybatis.model.customize.web.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WebProjectListCustomizeMapper {

	List<WebProjectListCustomize> selectProjectList(Map<String, Object> params);

	int countProjectListRecordTotal(Map<String, Object> params);

	WebProjectPersonDetailCustomize selectProjectPersonDetail(@Param("borrowNid") String borrowNid);
	
	WebProjectPersonDetailCustomize selectHTJProjectPersonDetail(@Param("borrowNid") String borrowNid);

	WebProjectCompanyDetailCustomize selectProjectCompanyDetail(@Param("borrowNid") String borrowNid);
	
	WebProjectCompanyDetailCustomize selectHTJProjectCompanyDetail(@Param("borrowNid") String borrowNid);

	WebProjectDetailCustomize selectProjectDetail(@Param("borrowNid") String borrowNid);
	//预览新增
	WebProjectDetailCustomize selectProjectPreview(@Param("borrowNid") String borrowNid);

	WebHzcProjectDetailCustomize searchHzcProjectDetail(@Param("borrowNid") String borrowNid);
	
	WebHzcProjectDetailCustomize searchHTJHzcProjectDetail(@Param("borrowNid") String borrowNid);

	List<WebProjectAuthenInfoCustomize> searchProjectAuthenInfos(@Param("borrowNid") String borrowNid);
	
	List<WebProjectAuthenInfoCustomize> searchHTJProjectAuthenInfos(@Param("borrowNid") String borrowNid);

	WebHzcDisposalPlanCustomize searchDisposalPlan(@Param("borrowNid") String borrowNid);
	
	WebHzcDisposalPlanCustomize searchHTJDisposalPlan(@Param("borrowNid") String borrowNid);

	WebRiskControlCustomize selectRiskControl(@Param("borrowNid") String borrowNid);
	
	WebRiskControlCustomize selectHTJRiskControl(@Param("borrowNid") String borrowNid);

	List<WebVehiclePledgeCustomize> selectVehiclePledgeList(@Param("borrowNid") String borrowNid);
	
	List<WebVehiclePledgeCustomize> selectHTJVehiclePledgeList(@Param("borrowNid") String borrowNid);

	List<WebMortgageCustomize> selectMortgageList(@Param("borrowNid") String borrowNid);
	
	List<WebMortgageCustomize> selectHTJMortgageList(@Param("borrowNid") String borrowNid);

	List<WebProjectConsumeCustomize> selectProjectConsumeList(Map<String, Object> params);

	int countProjectConsumeListRecordTotal(Map<String, Object> params);

	List<WebProjectInvestListCustomize> selectProjectInvestList(Map<String, Object> params);

	int countProjectInvestRecordTotal(Map<String, Object> params);

	String searchProjectFiles(@Param("borrowNid")String borrowNid);
	
	
	/**
	 * 根据项目id获取汇添金项目信息(预览调用)
	 * @Title selectHTJProjectPreview
	 * @param borrowNid
	 * @return
	 */
	WebProjectDetailCustomize selectHTJProjectPreview(@Param("borrowNid") String borrowNid);
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
    List<WebProjectListCustomize> selectProjectListNew(Map<String, Object> params);
    /**
	 * 根据borrowNid，userId查找承接记录
	 */
    int countCreditTender(Map<String, Object> params);

    /**
    * 获取borrowNid查找债转列表
    * @param params
    * @return
    */
	List<WebProjectUndertakeListCustomize> selectProjectUndertakeList(Map<String, Object> params);

	/**
	 * 根据borrowNid获取承接总金额
	 * @param borrowNid
	 * @return
	 */
   String sumUndertakeAccount(@Param("borrowNid") String borrowNid);

   /**
    * 根据borrowNid获取承接总数
    * @param borrowNid
    * @return
    */
   int countCreditTenderByBorrowNid(@Param("borrowNid") String borrowNid);

}
