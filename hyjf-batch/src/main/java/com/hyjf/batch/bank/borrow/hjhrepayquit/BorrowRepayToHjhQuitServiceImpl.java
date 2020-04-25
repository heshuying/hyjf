package com.hyjf.batch.bank.borrow.hjhrepayquit;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;

/**
 * 
 *
 * @author Administrator
 *
 */
@Service
public class BorrowRepayToHjhQuitServiceImpl extends BaseServiceImpl implements BorrowRepayToHjhQuitService {

	Logger _log = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<HjhAccede> selectWaitQuitHjhList() {
		ArrayList<Integer> list = new ArrayList<>();
		list.add(5);//(退出中)准备退出计划
		list.add(2);//(自动投标成功)准备进入锁定期
		HjhAccedeExample accedeExample = new HjhAccedeExample();
		accedeExample.createCriteria().andOrderStatusIn(list);
		List<HjhAccede> accedeList = this.hjhAccedeMapper.selectByExample(accedeExample);
		if (accedeList != null && accedeList.size() > 0) {
			return accedeList;
		}
		return null;
	}


	
	
}
