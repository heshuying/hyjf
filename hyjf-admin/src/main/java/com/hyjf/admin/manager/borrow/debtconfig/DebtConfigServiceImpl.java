package com.hyjf.admin.manager.borrow.debtconfig;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.DebtConfig;
import com.hyjf.mybatis.model.auto.DebtConfigExample;
import com.hyjf.mybatis.model.auto.DebtConfigLog;
import com.hyjf.mybatis.model.auto.DebtConfigLogExample;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtConfigServiceImpl extends BaseServiceImpl implements DebtConfigService {

	@Override
	public void insertRecord(DebtConfig record){
		debtConfigMapper.insertSelective(record);
	}
	@Override
	public void updateRecord(DebtConfig record, DebtConfigLog debtConfigLog){
		debtConfigLogMapper.insertSelective(debtConfigLog);
		debtConfigMapper.updateByPrimaryKeySelective(record);

	}
	@Override
	public List<DebtConfig> selectDebtConfigList(){
		DebtConfigExample example = new DebtConfigExample();
		return  debtConfigMapper.selectByExample(example);
	}
	@Override
	public 	int countDebtConfigLogTotal(){
		DebtConfigLogExample example = new DebtConfigLogExample();
		return debtConfigLogMapper.countByExample(example);
	}
	@Override
	public List<DebtConfigLog> getDebtConfigLogList(DebtConfigBean form, int limitStart, int limitEnd){
		DebtConfigLogExample example = new DebtConfigLogExample();
		if (limitStart !=-1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		example.setOrderByClause("`update_time` desc");
		return  debtConfigLogMapper.selectByExample(example);
	}

}
