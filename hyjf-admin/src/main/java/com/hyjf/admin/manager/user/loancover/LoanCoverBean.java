package com.hyjf.admin.manager.user.loancover;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthority;
import com.hyjf.mybatis.model.auto.MspApply;
import com.hyjf.mybatis.model.auto.MspConfigure;
import com.hyjf.mybatis.model.auto.MspRegion;

/**
 * 文章管理实体类
 * 
 * @author 
 *
 */
public class LoanCoverBean extends LoanSubjectCertificateAuthority implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    /**
     * 前台时间接收
     */
    private String ids;
    
    private String startCreate;

    private String endCreate;

    public String getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(String startCreate) {
        this.startCreate = startCreate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(String endCreate) {
        this.endCreate = endCreate;
    }

    private List<LoanSubjectCertificateAuthority> recordList;
    
    private List<MspRegion> regionList;
    private List<MspConfigure> configureList;

    public List<MspConfigure> getConfigureList() {
		return configureList;
	}

	public void setConfigureList(List<MspConfigure> configureList) {
		this.configureList = configureList;
	}

	public List<MspRegion> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<MspRegion> regionList) {
		this.regionList = regionList;
	}

	public List<LoanSubjectCertificateAuthority> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<LoanSubjectCertificateAuthority> recordList) {
		this.recordList = recordList;
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

}
