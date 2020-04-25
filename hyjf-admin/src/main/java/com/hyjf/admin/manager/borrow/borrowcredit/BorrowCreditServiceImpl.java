package com.hyjf.admin.manager.borrow.borrowcredit;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.BorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;

@Service
public class BorrowCreditServiceImpl extends BaseServiceImpl implements BorrowCreditService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countBorrowCredit(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.countBorrowCredit(borrowCreditCustomize);
	}

	/**
	 * 汇转让列表
	 * 
	 * @return
	 */
	public List<BorrowCreditCustomize> selectBorrowCreditList(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.selectBorrowCreditList(borrowCreditCustomize);
	}

	/**
	 * 导出汇转让列表
	 * 
	 * @return
	 */
	public List<BorrowCreditCustomize> exportBorrowCreditList(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.exportBorrowCreditList(borrowCreditCustomize);
	}

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countBorrowCreditInfoList(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.countBorrowCreditInfoList(borrowCreditCustomize);
	}

	/**
	 * 汇转让详细列表
	 * 
	 * @return
	 */
	public List<BorrowCreditCustomize> selectBorrowCreditInfoList(BorrowCreditCustomize borrowCreditCustomize) {
		return this.borrowCreditCustomizeMapper.selectBorrowCreditInfoList(borrowCreditCustomize);
	}

	/**
	 * 汇转让详细
	 * 
	 * @return
	 */
	public BorrowCredit getBorrowCredit(BorrowCredit borrowCredit) {
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria cra = example.createCriteria();
		cra.andCreditNidEqualTo(borrowCredit.getCreditNid());
		List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(example);
		return borrowCreditList.get(0);
	}

	/**
	 * 汇转让更新
	 * 
	 * @return
	 */
	public void updateBorrowCredit(BorrowCredit borrowCredit) {
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria cra = example.createCriteria();
		cra.andCreditNidEqualTo(borrowCredit.getCreditNid());

		BorrowCredit record = new BorrowCredit();
		record.setCreditStatus(borrowCredit.getCreditStatus());

		this.borrowCreditMapper.updateByExampleSelective(record, example);
	}

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public Users getUsers(Integer userId) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);

		return this.usersMapper.selectByExample(example).get(0);
	}
	
	/**
     * 
     * 根据债转编号获取转让记录详情
     * @author liuyang
     * @param params
     * @return
     * @see com.hyjf.app.tender.credit.AppTenderCreditService#selectTenderCreditRecordDetail(java.lang.String)
     */
    @Override
    public AppTenderCreditRecordDetailCustomize selectTenderCreditRecordDetail(Map<String, Object> params) {

        return appTenderCreditCustomizeMapper.selectTenderCreditRecordDetail(params);
    }

	/**
	 * 获取金额合计
	 * @param borrowCreditCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public BorrowCreditCustomize sumBorrowCredit(BorrowCreditCustomize borrowCreditCustomize) {
		
		return this.borrowCreditCustomizeMapper.sumBorrowCredit(borrowCreditCustomize);
			
	}

	/**
	 * dialog获取金额合计
	 * @param borrowCreditCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public BorrowCreditCustomize sumBorrowCreditInfo(BorrowCreditCustomize borrowCreditCustomize) {

		return this.borrowCreditCustomizeMapper.sumBorrowCreditInfo(borrowCreditCustomize);
			
	}
}
