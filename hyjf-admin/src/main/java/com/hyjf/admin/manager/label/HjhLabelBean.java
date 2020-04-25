package com.hyjf.admin.manager.label;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.customize.HjhLabelCustomize;

/**
 * 提成设置
 * @author qingbing
 *
 */
public class HjhLabelBean extends HjhLabel implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<HjhLabelCustomize> recordList;

	private String ids;
	 private Integer engineId;
	 
	
	public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
    private String pushTimeStartString;

    private String pushTimeEndString;
    // 标签名称
	 private String labelNameSrch;
	//状态
	 private Integer labelStateSrch;
	// 操作时间
	 private String createTimeStartSrch;
    // 操作时间
	 private String createTimeEndSrch;

    // 修改时间开始
    private String updateTimeStartSrch;
    // 修改时间结束
    private String updateTimeEndSrch;


    //机构编号
	 private String instCodeSrch;
	 //机构产品类型
	 private Integer assetTypeSrch;
	 //项目类型
	 private Integer projectTypeSrch;
	 //还款方式
	 private String borrowStyleSrch;
	 //使用状态
	 private Integer engineIdSrch;

	 //智投编号
	 private String planNidSrch;

    public String getBorrowStyleSrch() {
        return borrowStyleSrch;
    }

    public void setBorrowStyleSrch(String borrowStyleSrch) {
        this.borrowStyleSrch = borrowStyleSrch;
    }
    /**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

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

	public List<HjhLabelCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<HjhLabelCustomize> recordList) {
		this.recordList = recordList;
	}
	

    public String getPushTimeStartString() {
        return pushTimeStartString;
    }

    public void setPushTimeStartString(String pushTimeStartString) {
        this.pushTimeStartString = pushTimeStartString;
    }

    public String getPushTimeEndString() {
        return pushTimeEndString;
    }

    public void setPushTimeEndString(String pushTimeEndString) {
        this.pushTimeEndString = pushTimeEndString;
    }

    public String getLabelNameSrch() {
        return labelNameSrch;
    }

    public void setLabelNameSrch(String labelNameSrch) {
        this.labelNameSrch = labelNameSrch;
    }

    public Integer getLabelStateSrch() {
        return labelStateSrch;
    }

    public void setLabelStateSrch(Integer labelStateSrch) {
        this.labelStateSrch = labelStateSrch;
    }

   
    public String getCreateTimeStartSrch() {
        return createTimeStartSrch;
    }

    public void setCreateTimeStartSrch(String createTimeStartSrch) {
        this.createTimeStartSrch = createTimeStartSrch;
    }

    public String getCreateTimeEndSrch() {
        return createTimeEndSrch;
    }

    public void setCreateTimeEndSrch(String createTimeEndSrch) {
        this.createTimeEndSrch = createTimeEndSrch;
    }

    public String getInstCodeSrch() {
        return instCodeSrch;
    }

    public void setInstCodeSrch(String instCodeSrch) {
        this.instCodeSrch = instCodeSrch;
    }

    public Integer getAssetTypeSrch() {
        return assetTypeSrch;
    }

    public void setAssetTypeSrch(Integer assetTypeSrch) {
        this.assetTypeSrch = assetTypeSrch;
    }

    public Integer getProjectTypeSrch() {
        return projectTypeSrch;
    }

    public void setProjectTypeSrch(Integer projectTypeSrch) {
        this.projectTypeSrch = projectTypeSrch;
    }

	public Integer getEngineIdSrch() {
		return engineIdSrch;
	}

	public void setEngineIdSrch(Integer engineIdSrch) {
		this.engineIdSrch = engineIdSrch;
	}


    public String getPlanNidSrch() {
        return planNidSrch;
    }

    public void setPlanNidSrch(String planNidSrch) {
        this.planNidSrch = planNidSrch;
    }

    public String getUpdateTimeStartSrch() {
        return updateTimeStartSrch;
    }

    public void setUpdateTimeStartSrch(String updateTimeStartSrch) {
        this.updateTimeStartSrch = updateTimeStartSrch;
    }

    public String getUpdateTimeEndSrch() {
        return updateTimeEndSrch;
    }

    public void setUpdateTimeEndSrch(String updateTimeEndSrch) {
        this.updateTimeEndSrch = updateTimeEndSrch;
    }
}
