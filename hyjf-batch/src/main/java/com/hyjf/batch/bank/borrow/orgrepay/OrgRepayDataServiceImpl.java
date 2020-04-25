package com.hyjf.batch.bank.borrow.orgrepay;

import com.hyjf.bank.service.user.repay.ProjectBean;
import com.hyjf.bank.service.user.repay.RepayService;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Service
public class OrgRepayDataServiceImpl extends BaseServiceImpl implements OrgRepayDataService {

    Logger _log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RepayService repayService;
    /**
     * 获取待还款标的
     * @return
     */
    @Override
    public List<Borrow> getOrgRepayBorrowList() {
        BorrowExample example = new BorrowExample();
        example.createCriteria().andIsRepayOrgFlagEqualTo(1).andRepayOrgUserIdIsNotNull().andStatusEqualTo(4);
        List<Borrow> borrows = this.borrowMapper.selectByExample(example);
        if(borrows != null && borrows.size() > 0){
            return  borrows;
        }
        return null;
    }

    /**
     * 每日维护垫付机构数据
     * @param borrow
     */
    @Override
    public void updateOrgRepayData(Borrow borrow) throws Exception {
        ProjectBean form = new ProjectBean();
        form.setUserId(borrow.getRepayOrgUserId().toString());
        form.setRoleId("3");
        //垫付机构总的还款信息

        form.setBorrowNid(borrow.getBorrowNid());

        ProjectBean repayProject = repayService.searchRepayProjectDetail(form,false);
        //标的总提请减息
        BigDecimal chargeInterest = new BigDecimal(repayProject.getChargeInterest() == null ? "0" : repayProject.getChargeInterest());
        //标的总延期利息
        BigDecimal delayInterest = new BigDecimal(repayProject.getDelayInterest() == null ? "0" : repayProject.getDelayInterest());
        //标的总逾期利息
        BigDecimal lateInterest = new BigDecimal(repayProject.getLateInterest() == null ? "0" : repayProject.getLateInterest());

        BorrowRepayExample example = new BorrowRepayExample();
        example.createCriteria().andBorrowNidEqualTo(borrow.getBorrowNid());
        List<BorrowRepay> borrowRepays = this.borrowRepayMapper.selectByExample(example);
        if (borrowRepays != null && borrowRepays.size() > 0){
            BorrowRepay borrowRepay = borrowRepays.get(0);
            borrowRepay.setOrgChargeInterest(chargeInterest);
            borrowRepay.setOrgDelayInterest(delayInterest);
            borrowRepay.setOrgLateInterest(lateInterest);
            int i = this.borrowRepayMapper.updateByPrimaryKey(borrowRepay);
            if(i == 0){
                throw new RuntimeException("变更垫付数据失败");
            }
        }

    }



}
