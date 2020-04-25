package com.hyjf.admin.manager.hjhplan.planlist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.admin.BaseService;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.PlanListCommonCustomize;

public interface PlanListService extends BaseService {
	
	/**
	 * 计划列表查询件数
	 * 
	 * @method: countPlan
	 * @return: int
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	 int countPlan(PlanListCommonCustomize planListCommonCustomize);
	
	/**
	 * 计划列表查询
	 * 
	 * @method: selectPlanList
	 * @return: List
	 * @mender: LIBIN
	 * @date: 2017年8月11日
	 */
	 List<HjhPlan> selectPlanList(PlanListCommonCustomize planListCommonCustomize);
	 
	/**
	 * 根据主键判断数据是否存在
	 * 
	 * @Title isExistsRecord
	 * @param planNid
	 * @return
	 */
	 boolean isExistsRecord(String planNid);
	 
	/**
	 * 获取计划数据
	 * 
	 * @Title getPlanInfo
	 * @param planListBean
	 * @return
	 */
	PlanListBean getPlanInfo(PlanListBean planListBean);

	/**
	 * 判断计划名称是否重复
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param request
	 * @return
	 */
	String isDebtPlanNameExist(HttpServletRequest request);
	
	/**
	 * 画面校验
	 * 
	 * @Title validatorFieldCheck
	 * @param mav
	 * @param planListBean
	 * @param isExistsRecord
	 */
	void validatorFieldCheck(ModelAndView mav, PlanListBean planListBean, boolean isExistsRecord);
	
	/**
	 * 获取还款方式
	 * 
	 * @Title getBorrowStyleList
	 * @return
	 */
	List<BorrowStyle> getBorrowStyleList();
	
	/**
	 * 更新操作
	 * 
	 * @Title updateRecord
	 * @param planListBean
	 * @throws Exception
	 */
	void updateRecord(PlanListBean planListBean) throws Exception;
	
	/**
	 * 插入操作
	 * 
	 * @param record
	 */
	void insertRecord(PlanListBean planListBean) throws Exception;
	
	/**
	 * 更新状态操作
	 * 
	 * @Title updatePlanRecord
	 * @param planListBean
	 * @throws Exception
	 */
	void updatePlanRecord(PlanListBean planListBean) throws Exception;
	
	/**
	 * 更新显示状态操作
	 * 
	 * @Title updatePlanRecord
	 * @param planListBean
	 * @throws Exception
	 */
	void updatePlanDisplayRecord(PlanListBean planListBean) throws Exception;
	/**
	 * 判断计划编号是否重复
	 * 
	 * @Title isDebtPlanTypeNidExist
	 * @param request
	 * @return
	 */
	public String isDebtPlanNidExist(HttpServletRequest request);

	/**
	 * 获取待还总额合计值
	 * 
	 * @Title sumHjhPlan
	 * @param planListCommonCustomize
	 * @return String
	 */
	String sumHjhPlan(PlanListCommonCustomize planListCommonCustomize);
	
	/**
	 * 获取开放额度合计值
	 * 
	 * @Title sumOpenAccount
	 * @param planListCommonCustomize
	 * @return String
	 */
	String sumOpenAccount(PlanListCommonCustomize planListCommonCustomize);
	
	/**
	 * 获取累计加入金额合计值
	 * 
	 * @Title sumJoinTotal
	 * @param planListCommonCustomize
	 * @return String
	 */
	String sumJoinTotal(PlanListCommonCustomize planListCommonCustomize);
}
