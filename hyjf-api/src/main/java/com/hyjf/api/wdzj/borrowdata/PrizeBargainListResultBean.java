package com.hyjf.api.wdzj.borrowdata;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.act.ActNovBargainCustomize;

public class PrizeBargainListResultBean extends BaseResultBean {
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -9174539916567994537L;
    
    
    private List<ActNovBargainCustomize> dataList;

    //分页信息
    Paginator paginator;

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

	public List<ActNovBargainCustomize> getDataList() {
		return dataList;
	}

	public void setDataList(List<ActNovBargainCustomize> dataList) {
		this.dataList = dataList;
	}

	@Override
	public String toString() {
		return super.toString() + "PrizeBargainListResultBean [dataList=" + dataList + ", paginator=" + paginator + "]";
	}

    
    
    
}
