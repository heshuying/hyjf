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

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.web.WebHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.WebHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectConsumeCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebVehiclePledgeCustomize;

public interface WebDebtPlanCustomizeMapper {

	List<WebProjectListCustomize> selectProjectList(Map<String, Object> params);

	int countProjectListRecordTotal(Map<String, Object> params);

	WebProjectPersonDetailCustomize selectProjectPersonDetail(@Param("borrowNid") String borrowNid);

	WebProjectCompanyDetailCustomize selectProjectCompanyDetail(@Param("borrowNid") String borrowNid);

	WebProjectDetailCustomize selectProjectDetail(@Param("borrowNid") String borrowNid);
	//预览新增
	WebProjectDetailCustomize selectProjectPreview(@Param("borrowNid") String borrowNid);

	WebHzcProjectDetailCustomize searchHzcProjectDetail(@Param("borrowNid") String borrowNid);

	List<WebProjectAuthenInfoCustomize> searchProjectAuthenInfos(@Param("borrowNid") String borrowNid);

	WebHzcDisposalPlanCustomize searchDisposalPlan(@Param("borrowNid") String borrowNid);

	WebRiskControlCustomize selectRiskControl(@Param("borrowNid") String borrowNid);

	List<WebVehiclePledgeCustomize> selectVehiclePledgeList(@Param("borrowNid") String borrowNid);

	List<WebMortgageCustomize> selectMortgageList(@Param("borrowNid") String borrowNid);

	List<WebProjectConsumeCustomize> selectProjectConsumeList(Map<String, Object> params);

	int countProjectConsumeListRecordTotal(Map<String, Object> params);

	List<WebProjectInvestListCustomize> selectProjectInvestList(Map<String, Object> params);

	int countProjectInvestRecordTotal(Map<String, Object> params);

	String searchProjectFiles(@Param("borrowNid")String borrowNid);

}
