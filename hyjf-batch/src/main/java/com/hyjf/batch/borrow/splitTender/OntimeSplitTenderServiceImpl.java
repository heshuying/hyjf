package com.hyjf.batch.borrow.splitTender;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowBail;
import com.hyjf.mybatis.model.auto.BorrowBailExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

@Service
public class OntimeSplitTenderServiceImpl extends BaseServiceImpl implements OntimeSplitTenderService {
	/**
	 * 查询符合条件的定时投标 数量
	 * 
	 * @param ontime
	 * @return
	 */
	@Override
	public Integer queryOntimeTenderCount(Integer ontime) {
		Integer count = this.ontimeTenderCustomizeMapper.queryOntimeTenderCount(ontime);
		return count;
	}

	/**
	 * 资金明细（列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<Borrow> queryOntimeTenderList(Integer ontime) {
		List<Borrow> list = this.ontimeTenderCustomizeMapper.queryOntimeTenderList(ontime);
		return list;
	}

	/**
	 * 修改投标信息
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKey(Borrow record) {
		int result = this.borrowMapper.updateByPrimaryKey(record);
		return result;
	}

	/**
	 * 修改投标信息
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(BorrowWithBLOBs record) {
		int result = this.borrowMapper.updateByPrimaryKeySelective(record);
		return result;
	}

	/**
	 * 查询所有未复审的标
	 * 
	 * @return
	 */
	public List<Borrow> queryAllunrecheckTenders() {
		List<Borrow> list = this.ontimeTenderCustomizeMapper.queryAllunrecheckTenders();
		return list;
	}

	/**
	 * 查询分期发标的标的拆分总期数
	 * 
	 * @param borrowPreNid
	 * @return
	 */
	public Integer querySplitTenderCount(@Param("borrowPreNid") Integer borrowPreNid) {
		Integer count = this.ontimeTenderCustomizeMapper.querySplitTenderCount(borrowPreNid);
		return count;
	}

	/**
	 * 根据borrowNid查询Borrow(可以自动发标的！)
	 * 
	 * @param borrowNid
	 * @return
	 */
	public Borrow queryBorrowByNextNid(String borrowNid) {
		// Borrow borrow = new Borrow();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowExample borrowExample = new BorrowExample();
			BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			// 该标的还没有发标
			borrowCra.andStatusEqualTo(0);
			// 立即发标  或者 batch发标后   时 BorrowStatus 为 1
			borrowCra.andBorrowStatusEqualTo(0);
			// 初审时间不为0，即已进行初审。
			borrowCra.andVerifyOverTimeNotEqualTo(0);
			//没有设置定时 （已定时的标不再自动发标！）
			borrowCra.andOntimeEqualTo(0);

			List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
			if (borrowList != null && borrowList.size() == 1) {
				return borrowList.get(0);
			}
		}
		// return borrow;
		return null;
	}

	/**
	 * 查询是否交纳保证金
	 * 
	 * @param borrowNid
	 * @return
	 */
	public BorrowBail queryBorrowBailByNid(String borrowNid) {
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowBailExample borrowBailExample = new BorrowBailExample();
			BorrowBailExample.Criteria borrowBailCra = borrowBailExample.createCriteria();
			borrowBailCra.andBorrowNidEqualTo(borrowNid);

			List<BorrowBail> borrowList = this.borrowBailMapper.selectByExample(borrowBailExample);
			if (borrowList != null && borrowList.size() == 1) {
				return borrowList.get(0);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param tplname
	 * @return
	 */
	public String queryMobiles(String tplname){
		String mobiles= this.ontimeTenderCustomizeMapper.queryMobiles(tplname);
		return mobiles;
	}
	
	
}
