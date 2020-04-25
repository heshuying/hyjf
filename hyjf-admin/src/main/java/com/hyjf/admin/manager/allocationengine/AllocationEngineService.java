package com.hyjf.admin.manager.allocationengine;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 标的分配规则引擎服务层
 * 
 * @author
 * 
 */
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhRegion;

/**
 * @author Administrator
 *
 */
public interface AllocationEngineService extends BaseService {
	
    /**
     * 获取计划配置列表
     * 
     * @return
     */
    List<HjhRegion> getRecordList(AllocationEngineBean bean, int limitStart, int limitEnd);
    
    /**
     * 获取单个计划
     * 
     * @return
     */
    HjhRegion getRecord(Integer record);
    
    /**
     * 单个计划插入
     * 
     * @param record
     */
    void insertRecord(HjhRegion record);
    
    /**
     * 查询汇计划名称
     * 
     * @param planName
     */
    String getPlanName(String planNid);
    
	/**
	 * 校验入力的计划编号
	 * @param request
	 * @return
	 */
	String checkInputPlanNidSrch(HttpServletRequest request);
	
    /**
     * 列表状态更新
     * 
     * @param record
     */
    void updateRecord(HjhRegion record);
    
	/**
	 * 总数COUNT
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	Integer countPlanConfig(AllocationEngineBean allocationEngineBean);
	
    /**
     * 获取计划配置列表
     * 
     * @return
     */
    List<HjhAllocationEngine> getAllocationEngineRecordList(AllocationEngineBean bean, int limitStart, int limitEnd);
    
    /**
     * 获取单个配置
     * 
     * @return
     */
    HjhAllocationEngine getPlanConfigRecord(Integer record);
    
    /**
     * 列表状态更新
     * 
     * @param record
     */
    void updatePlanConfigRecord(HjhAllocationEngine record);
    
    /**
     * 查询汇计划名称
     * 
     * @param planName
     */
    String getPlanNameById(String id);
   
	 /**
     * 查询标签信息
     * 
     * @param planName
     */
	HjhLabel getHjhLabelRecord(String labelName);
	
    /**
     * 查询汇计划名称
     * 
     * @param planName
     */
    String getPlanBorrowStyle(String planNid);
    
    /**
     * 验证重复
     * 
     * @param planName
     */
    boolean checkRepeat(String labelName,String planNid);
     
    /**
	 * 获取计划配置列表
	 * 
	 * @return
	 */
	List<HjhAllocationEngine> getHjhAllocationEngineListByPlan(String planNid);
	
	 /**
     * 查询计划专区
     * 
     * @param planName
     */
	HjhRegion getHjhRegionRecord(String planNid);
	
    /**
     * 单个计划插入
     * 
     * @param record
     */
    void insertHjhAllocationEngineRecord(HjhAllocationEngine record);
    

    /**
     * 列表状态更新
     * 
     * @param record
     */
    void updateAllocationEngineRecord(String planNid,Integer configStatus);
	
    /**
     * 获取单个计划
     * 
     * @return
     */
    HjhAllocationEngine getRecordBylabelId(String planNid,String labelId);
    
    /**
     * 获取单个计划
     * 
     * @return
     */
    HjhAllocationEngine getRecordBylabelName(String planNid,String labelName);
    /**
     * 获取单个计划
     * 
     * @return
     */
    HjhAllocationEngine getRecordBy(String planName,String labelName);
	
    /**
     * 单个计划插入
     * 
     * @param record
     */
    void updateHjhAllocationEngineRecord(HjhAllocationEngine record);
    
	 /**
     * 查询计划专区
     * 
     * @param planName
     */
	HjhRegion getHjhRegionRecordByName(String planName);
	HjhAllocationEngine selectByPrimaryKey(Integer id);

	/**
	 * 换绑,删除原来的标签
	 * @param planName
	 * @param labelName
	 */
	void deleteEngine(String planName, String labelName);

	/**
	 * 换绑以后插入新的计划配置
	 * @param record
	 */
	void insertEngine(HjhAllocationEngine record);

	/**校验计划编号是不是存在
	 * @param request
	 * @return
	 */
	String isExistsPlanNumber(HttpServletRequest request);
	
	/**查询后台数据库
	 * @param planNid
	 * @return
	 */
	int isExistsPlanNumber(String planNid);
}
