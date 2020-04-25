package com.hyjf.admin.manager.plan.plancommon;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.DebtPlanConfig;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.customize.DebtPlanBorrowCustomize;

/**
 * 计划录入共通Service
 * 
 * @ClassName PlanCommonService
 * @author liuyang
 * @date 2016年9月18日 下午2:27:49
 */
public interface PlanCommonService extends BaseService {

	/**
	 * 获取计划配置信息
	 * 
	 * @Title getDebtPlanConfigList
	 * @return
	 */
	public List<DebtPlanConfig> getDebtPlanConfigList();

	/**
	 * 获取还款方式
	 * 
	 * @Title getBorrowStyleList
	 * @return
	 */
	public List<BorrowStyle> getBorrowStyleList();

	/**
	 * 获取计划的关键资产的件数
	 * 
	 * @Title countDebtPlanBorrowList
	 * @param param
	 * @return
	 */
	public int countDebtPlanBorrowList(Map<String, Object> param);

	/**
	 * 获取计划的关联资产信息
	 * 
	 * @Title getDebtPlanBorrowList
	 * @param param
	 * @return
	 */
	public List<DebtPlanBorrowCustomize> getDebtPlanBorrowList(Map<String, Object> param);

	/**
	 * 获取计划预编号
	 * 
	 * @Title getPlanPreNid
	 * @return
	 */
	public String getPlanPreNid();

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @Title isExistsRecord
	 * @param planNid
	 * @param planPreNid
	 * @return
	 */
	public boolean isExistsRecord(String planNid, String planPreNid);

	/**
	 * 获取计划数据
	 * 
	 * @Title getPlanInfo
	 * @param planCommonBean
	 * @return
	 */
	public PlanCommonBean getPlanInfo(PlanCommonBean planCommonBean);

	/**
	 * 获取计划信息
	 * 
	 * @Title getDebtPlanWithBLOBs
	 * @param planNid
	 * @return
	 */
	public DebtPlanWithBLOBs getDebtPlanWithBLOBs(String planNid);

	/**
	 * 计划数据获取
	 * 
	 * @Title getPlanCommonFiled
	 * @param planBean
	 * @param debtPlanWithBLOBs
	 */
	public void getPlanCommonFiled(PlanCommonBean planBean, DebtPlanWithBLOBs debtPlanWithBLOBs);

	/**
	 * 根据计划类型查询已经发布该计划类型的数量
	 * 
	 * @Title getPlanByDebtPlanType
	 * @param debtPlanType
	 * @return
	 */
	public int getPlanByDebtPlanType(String debtPlanType);

	/**
	 * 根据计划类型查询计划配置信息
	 * 
	 * @Title getPlanConfigByDebtPlanType
	 * @param debtPlanType
	 * @return
	 */
	public DebtPlanConfig getPlanConfigByDebtPlanType(String debtPlanType);

	/**
	 * 判断计划名称是否重复
	 * 
	 * @Title isDebtPlanTypeNameExist
	 * @param request
	 * @return
	 */
	public String isDebtPlanNameExist(HttpServletRequest request);

	/**
	 * 画面校验
	 * 
	 * @Title validatorFieldCheck
	 * @param mav
	 * @param plancCommonBean
	 * @param isExistsRecord
	 */
	public void validatorFieldCheck(ModelAndView mav, PlanCommonBean plancCommonBean, boolean isExistsRecord);
	
	
	/**
	 * 判断计划预编号是否重复
	 * @Title isExistsPlanPreNidRecord
	 * @param request
	 * @return
	 */
	public String isExistsPlanPreNidRecord(HttpServletRequest request);

	/**
	 * 插入操作
	 * 
	 * @param record
	 */
	public void insertRecord(PlanCommonBean planCommonBean) throws Exception;

	/**
	 * 更新操作
	 * 
	 * @Title updateRecord
	 * @param planCommonBean
	 * @throws Exception
	 */
	public void updateRecord(PlanCommonBean planCommonBean) throws Exception;

	/**
	 * 计划的资产配置信息插入
	 * 
	 * @Title insertDebtPlanBorrowNidRecord
	 * @param planCommonBean
	 * @param debtPlanBorrowNid
	 */
	public void insertDebtPlanBorrowNidRecord(PlanCommonBean planCommonBean, List<String> debtPlanBorrowNid);

	/**
	 * 根据汇添金专属标号查询已关联的计划编号
	 * 
	 * @Title getDebtPlanNidListByBorrowNid
	 * @param borrowNid
	 * @return
	 */
	public List<String> getDebtPlanNidListByBorrowNid(String borrowNid);

	/**
	 * 根据计划编号,专属标号查询该计划是否已经关联资产
	 * 
	 * @Title getPlanIsSelected
	 * @param debtPlanNid
	 * @param borrowNid
	 * @return
	 */
	public String getPlanIsSelected(String debtPlanNid, String borrowNid);
	
	
	/**
	 * 获取关联总计
	 * @Title countDebtPlanAmount
	 * @param param
	 * @return
	 */
	public Map<String,Object> countDebtPlanAmount(Map<String,Object> param);
}
