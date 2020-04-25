package com.hyjf.admin.manager.allocationengine;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhRegion;

/**
 * 标的分配规则引擎实体bean
 * 
 * @author
 * 
 */
public class AllocationEngineBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8836278149259942537L;
	
    private List<HjhRegion> recordList;
    
    private List<HjhAllocationEngine> recordAllocationEngineList;
	
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;
    
    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;
	
	/**
	 * (计划专区)前台查询要素
	 */
	private String planNidSrch;
	
	private String planNameSrch;
	/**
	 * 添加时间查询
	 */
	private String startCreateSrch;
	private String endCreateSrch;
	/**
	 * 状态查询
	 */
	private Integer configStatusSrch;
	
	/**
	 * (计划专区)列表要素
	 */
	private String id;//序号ID
	
	private String planNid;//计划编号
	
	private String planName;//计划名称
	
	private String configAddTime;//添加时间
	
	private String configStatus;//状态
	
	/**
	 * (计划配置列表+详情)列表要素
	 */
	private String labelId;//标签编号
	
	private String labelName;//标签名称
	
	private String addTime;//标签添加时间
	
	private String labelStatus;//标签状态
	
	private String labelSort;//标签排序
	
	private String transferTimeSort;//债转时间排序 0：按转让时间降序 1：按转让时间升序
	
	private String transferTimeSortPriority;//债转时间排序优先级
	
	private String aprSort;//出借利率率排序 0：从低到高 1：从高到低
	
	private String aprSortPriority;//apr_sort_priority
	
	private String actulPaySort;//标的实际支付金额排序 0：从小到大 1：从大到小
	
	private String actulPaySortPriority;//出借利率率优先级

	private String investProgressSort;//出借进度排序 0：从小到大 1：从大到小
	
	private String investProgressSortPriority;//出借进度排序 0：从小到大 1：从大到小

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public String getPlanNidSrch() {
		return planNidSrch;
	}

	public void setPlanNidSrch(String planNidSrch) {
		this.planNidSrch = planNidSrch;
	}

	public String getPlanNameSrch() {
		return planNameSrch;
	}

	public void setPlanNameSrch(String planNameSrch) {
		this.planNameSrch = planNameSrch;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanNid() {
		return planNid;
	}

	public void setPlanNid(String planNid) {
		this.planNid = planNid;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getConfigAddTime() {
		return configAddTime;
	}

	public void setConfigAddTime(String configAddTime) {
		this.configAddTime = configAddTime;
	}

	public String getConfigStatus() {
		return configStatus;
	}

	public void setConfigStatus(String configStatus) {
		this.configStatus = configStatus;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getLabelStatus() {
		return labelStatus;
	}

	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
	}

	public String getLabelSort() {
		return labelSort;
	}

	public void setLabelSort(String labelSort) {
		this.labelSort = labelSort;
	}

	public String getTransferTimeSort() {
		return transferTimeSort;
	}

	public void setTransferTimeSort(String transferTimeSort) {
		this.transferTimeSort = transferTimeSort;
	}

	public String getTransferTimeSortPriority() {
		return transferTimeSortPriority;
	}

	public void setTransferTimeSortPriority(String transferTimeSortPriority) {
		this.transferTimeSortPriority = transferTimeSortPriority;
	}

	public String getAprSort() {
		return aprSort;
	}

	public void setAprSort(String aprSort) {
		this.aprSort = aprSort;
	}

	public String getActulPaySort() {
		return actulPaySort;
	}

	public void setActulPaySort(String actulPaySort) {
		this.actulPaySort = actulPaySort;
	}

	public String getActulPaySortPriority() {
		return actulPaySortPriority;
	}

	public void setActulPaySortPriority(String actulPaySortPriority) {
		this.actulPaySortPriority = actulPaySortPriority;
	}

	public String getInvestProgressSort() {
		return investProgressSort;
	}

	public void setInvestProgressSort(String investProgressSort) {
		this.investProgressSort = investProgressSort;
	}

	public String getInvestProgressSortPriority() {
		return investProgressSortPriority;
	}

	public void setInvestProgressSortPriority(String investProgressSortPriority) {
		this.investProgressSortPriority = investProgressSortPriority;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public List<HjhRegion> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<HjhRegion> recordList) {
		this.recordList = recordList;
	}

	public List<HjhAllocationEngine> getRecordAllocationEngineList() {
		return recordAllocationEngineList;
	}

	public void setRecordAllocationEngineList(List<HjhAllocationEngine> recordAllocationEngineList) {
		this.recordAllocationEngineList = recordAllocationEngineList;
	}

	public String getAprSortPriority() {
		return aprSortPriority;
	}

	public void setAprSortPriority(String aprSortPriority) {
		this.aprSortPriority = aprSortPriority;
	}

	public String getStartCreateSrch() {
		return startCreateSrch;
	}

	public void setStartCreateSrch(String startCreateSrch) {
		this.startCreateSrch = startCreateSrch;
	}

	public String getEndCreateSrch() {
		return endCreateSrch;
	}

	public void setEndCreateSrch(String endCreateSrch) {
		this.endCreateSrch = endCreateSrch;
	}

	public Integer getConfigStatusSrch() {
		return configStatusSrch;
	}

	public void setConfigStatusSrch(Integer configStatusSrch) {
		this.configStatusSrch = configStatusSrch;
	}
}
