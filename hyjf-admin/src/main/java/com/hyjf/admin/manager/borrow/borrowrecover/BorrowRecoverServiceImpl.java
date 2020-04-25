package com.hyjf.admin.manager.borrow.borrowrecover;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.customize.BorrowRecoverCustomize;

@Service
public class BorrowRecoverServiceImpl extends BaseServiceImpl implements BorrowRecoverService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowRecoverCustomize> selectBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize) {
		return this.borrowRecoverCustomizeMapper.selectBorrowRecoverList(borrowRecoverCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRecover(BorrowRecoverCustomize borrowRecoverCustomize) {
		return this.borrowRecoverCustomizeMapper.countBorrowRecover(borrowRecoverCustomize);
	}

	/**
	 * 放款明细记录 合计
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public BorrowRecoverCustomize sumBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize) {
		return this.borrowRecoverCustomizeMapper.sumBorrowRecoverList(borrowRecoverCustomize);

	}

	/**
	 * 导出明细列表
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowRecoverCustomize> selectExportBorrowRecoverList(BorrowRecoverCustomize borrowRecoverCustomize) {
		return this.borrowRecoverCustomizeMapper.exportBorrowRecoverList(borrowRecoverCustomize);

	}
	
	/**
     * 检索BorrowRecover
     * @param userId
     * @param borrowNid
     * @param nid
     * @return
     */
    @Override
    public List<BorrowRecover> selectBorrowRecover(String borrowNid) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowRecover> borrowRecovers = this.borrowRecoverMapper.selectByExample(example);
        if (borrowRecovers != null && borrowRecovers.size()> 0){
            return borrowRecovers;
        }
        return null;
    }
    /**
     * 检索BorrowRecoverPlan
     * @param userId
     * @param borrowNid
     * @param nid
     * @return
     */
    @Override
    public List<BorrowRecoverPlan> selectBorrowRecoverPlan(String borrowNid,int repayPeriod) {
        BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
        BorrowRecoverPlanExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        cra.andRecoverPeriodEqualTo(repayPeriod);
        List<BorrowRecoverPlan> borrowRecovers = this.borrowRecoverPlanMapper.selectByExample(example);
        if (borrowRecovers != null && borrowRecovers.size()> 0){
            return borrowRecovers;
        }
        return null;
    }
    
    /**
     * 检索BorrowRecoverPlan
     * @param userId
     * @param borrowNid
     * @param nid
     * @return
     */
    @Override
    public List<BorrowRecoverPlan> selectBorrowRecoverPlanByNid(String nid,int repayPeriod) {
        BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
        BorrowRecoverPlanExample.Criteria cra = example.createCriteria();
        cra.andNidEqualTo(nid);
        cra.andRecoverPeriodEqualTo(repayPeriod);
        List<BorrowRecoverPlan> borrowRecovers = this.borrowRecoverPlanMapper.selectByExample(example);
        if (borrowRecovers != null && borrowRecovers.size()> 0){
            return borrowRecovers;
        }
        return null;
    }
}
