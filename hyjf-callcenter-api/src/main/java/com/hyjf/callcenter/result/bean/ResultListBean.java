/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.callcenter.result.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.callcenter.base.BaseResultBean;

/**
 * 呼叫中心用接口返回多条数据的通用类
 * @author 刘彬
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年07月15日 
 * @see 下午2:23:01
 */
public class ResultListBean extends BaseResultBean implements Serializable {
	
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3589570872364671096L;
    /**
     * 返回多条数据list
     */
    private List<Object> dataList;
    
    //实例化list
    {
    	dataList = new ArrayList<Object>();
    }

	public List<Object> getDataList() {
		return dataList;
	}

	public void setDataList(List<Object> dataList) {
		this.dataList = dataList;
	}
}
