
package com.hyjf.web.platdata;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.web.BaseServiceImpl;

@Service
public class PlatDataServiceImpl extends BaseServiceImpl implements PlatDataService {

    @Override
    public Map<String, Object> selectDataInfo(String day) {
        return dataCustomizeMapper.selectDataInfo(day);
    }

    @Override
    public List<Map<String, Object>> selectDataTenderList() {
        return  dataCustomizeMapper.selectDataTenderInfo();
    }

    @Override
    public List<Map<String, Object>> selectDataCreditList() {
        return dataCustomizeMapper.selectDataCreditInfo();
    }

    @Override
    public Map<String, Object> selectPeriodInfo() {
        return  dataCustomizeMapper.selectPeriodInfo();
    }

    @Override
    public Map<String, Object> selectTendMoneyInfo() {
        return dataCustomizeMapper.selectTendMoneyInfo();
    }

	@Override
	public Map<String, Object> selectData() {
		  return dataCustomizeMapper.selectData();
	}

	@Override
	public List<Map<String, Object>> selectTenderListMap(int type) {
		return dataCustomizeMapper.selectTenderListMap(type);
	}
	
}