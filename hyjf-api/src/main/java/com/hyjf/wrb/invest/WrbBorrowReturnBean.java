package com.hyjf.wrb.invest;

import java.util.List;

import com.hyjf.mybatis.model.customize.wrb.WrbBorrowListCustomize;
import com.hyjf.wrb.WrbResponse;

public class WrbBorrowReturnBean extends WrbResponse{

	private List<WrbBorrowListCustomize> invest_list;

	public List<WrbBorrowListCustomize> getInvest_list() {
		return invest_list;
	}

	public void setInvest_list(List<WrbBorrowListCustomize> invest_list) {
		this.invest_list = invest_list;
	}
	
	
}
