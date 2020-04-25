package com.hyjf.app.user.credit;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.DebtConfig;
import com.hyjf.mybatis.model.auto.DebtConfigExample;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("appDebtConfigService")
public class DebtConfigServiceImpl extends BaseServiceImpl implements DebtConfigService {

	@Override
	public List<DebtConfig> selectDebtConfigList(){
		DebtConfigExample example = new DebtConfigExample();
		return  debtConfigMapper.selectByExample(example);
	}

}
