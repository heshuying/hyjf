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

package com.hyjf.mybatis.mapper.customize.web.htj;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.web.WebProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowHzcDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowMortgageCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowVehiclePledgeCustomize;

public interface DebtPlanBorrowCustomizeMapper {

	/**
	 * 查询相应的项目列表
	 * @param params
	 * @return
	 */
	List<DebtPlanBorrowCustomize> selectDebtPlanBorrowList(Map<String, Object> params);

	/**
	 * 查询相应的项目列表总数
	 * @param params
	 * @return
	 */
	int countDebtPlanBorrowListRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的项目标的的个人详情
	 * @param borrowNid
	 * @return
	 */
	DebtPlanBorrowPersonDetailCustomize selectDebtPlanBorrowPersonDetail(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的项目标的的企业详情
	 * @param borrowNid
	 * @return
	 */
	DebtPlanBorrowCompanyDetailCustomize selectDebtPlanBorrowCompanyDetail(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的项目详情
	 * @param borrowNid
	 * @return
	 */
	DebtPlanBorrowDetailCustomize selectDebtPlanBorrowDetail(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的项目详情的预览
	 * @param borrowNid
	 * @return
	 */
	DebtPlanBorrowDetailCustomize selectDebtPlanBorrowPreview(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的汇资产详情
	 * @param borrowNid
	 * @return
	 */
	DebtPlanBorrowHzcDetailCustomize searchDebtPlanBorrowHzcDetail(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的项目文件的认证信息
	 * @param borrowNid
	 * @return
	 */
	List<DebtPlanBorrowAuthenInfoCustomize> searchDebtPlanBorrowAuthenInfo(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的汇资产项目的风控信息
	 * @param borrowNid
	 * @return
	 */
	DebtPlanBorrowHzcDisposalPlanCustomize searchDebtPlanBorrowHzcDisposalPlan(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的项目的风控信息
	 * @param borrowNid
	 * @return
	 */
	DebtPlanBorrowRiskControlCustomize selectDebtPlanBorrowRiskControl(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应的车辆抵押信息
	 * @param borrowNid
	 * @return
	 */
	List<DebtPlanBorrowVehiclePledgeCustomize> selectDebtPlanBorrowVehiclePledgeList(@Param("borrowNid") String borrowNid);

	/***
	 * 查询相应的抵押信息
	 * @param borrowNid
	 * @return
	 */
	List<DebtPlanBorrowMortgageCustomize> selectDebtPlanBorrowMortgageList(@Param("borrowNid") String borrowNid);

	/**
	 * 查询相应标的的出借记录
	 * @param params
	 * @return
	 */
	List<WebProjectInvestListCustomize> selectDebtPlanBorrowInvestList(Map<String, Object> params);

	/**
	 * 统计相应的标的的出借总数
	 * @param params
	 * @return
	 */
	int countDebtPlanBorrowInvestRecordTotal(Map<String, Object> params);

	/**
	 * 查询相应的项目的文件信息
	 * @param borrowNid
	 * @return
	 */
	String searchDebtPlanBorrowFiles(@Param("borrowNid")String borrowNid);

}
