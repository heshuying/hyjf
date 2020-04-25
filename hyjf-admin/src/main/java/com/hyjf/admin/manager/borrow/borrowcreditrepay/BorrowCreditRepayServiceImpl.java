package com.hyjf.admin.manager.borrow.borrowcreditrepay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditRepayCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowCreditTenderCustomize;

@Service
public class BorrowCreditRepayServiceImpl extends BaseServiceImpl implements BorrowCreditRepayService {
    
    /**
     * 管理后台   汇转让   还款计划  已承接债转  数目
     * 
     * @return
     */
    @Override
    public Integer countCreditTender(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize) {
        Integer count = this.adminBorrowCreditRepayCustomizeMapper.countCreditTender(adminBorrowCreditTenderCustomize);
        return count;
    }

    /**
     * 管理后台   汇转让   还款计划  已承接债转 列表
     * 
     * @return
     */
    @Override
    public List<AdminBorrowCreditTenderCustomize> selectCreditTender(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize) {
        List<AdminBorrowCreditTenderCustomize> list = this.adminBorrowCreditRepayCustomizeMapper.selectCreditTender(adminBorrowCreditTenderCustomize);
        return list;
    }

    /**
     * 管理后台   债转还款计划   详细画面  数目
     * 
     * @return
     */
    @Override
    public Integer countBorrowCreditRepayInfoList(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize) {
        //获取债转还款计划信息
        CreditRepayExample creditRepayExample = new CreditRepayExample();
        CreditRepayExample.Criteria creditRepayCra = creditRepayExample.createCriteria();
        creditRepayCra.andAssignNidEqualTo(adminBorrowCreditTenderCustomize.getAssignNidSrch());
        Integer count = this.creditRepayMapper.countByExample(creditRepayExample);
        return count;
    }

    /**
     * 管理后台   债转还款计划   详细画面  列表
     * 
     * @return
     */
    @Override
    public List<AdminBorrowCreditRepayCustomize> selectBorrowCreditRepayInfoList(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize) {
        List<AdminBorrowCreditRepayCustomize> creditRepayList = new ArrayList<AdminBorrowCreditRepayCustomize>();
        AdminBorrowCreditRepayCustomize adminBorrowCreditRepayCustomize = null;
        //获取债转还款计划信息
        CreditRepayExample creditRepayExample = new CreditRepayExample();
        CreditRepayExample.Criteria creditRepayCra = creditRepayExample.createCriteria();
        creditRepayExample.setLimitStart(adminBorrowCreditTenderCustomize.getLimitStart());
        creditRepayExample.setLimitEnd(adminBorrowCreditTenderCustomize.getLimitEnd());
        creditRepayCra.andAssignNidEqualTo(adminBorrowCreditTenderCustomize.getAssignNidSrch());
        List<CreditRepay> list = this.creditRepayMapper.selectByExample(creditRepayExample);
        if(list!=null && list.size()>0){
            for(CreditRepay creditRepay : list){
                adminBorrowCreditRepayCustomize = new AdminBorrowCreditRepayCustomize();
                if (creditRepay.getAssignRepayNextTime()!=null) {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    creditRepay.setAddip(sdf.format(creditRepay.getAssignRepayNextTime()*1000l));
                }
                adminBorrowCreditRepayCustomize.setCreditRepay(creditRepay);
                //获取借款信息
                BorrowExample borrowExample = new BorrowExample();
                BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
                borrowCra.andBorrowNidEqualTo(creditRepay.getBidNid());
                List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
                if(borrowList!=null && borrowList.size()>0){
                    adminBorrowCreditRepayCustomize.setBorrow(borrowList.get(0));
                }
                creditRepayList.add(adminBorrowCreditRepayCustomize);
            }
        }
        return creditRepayList;
    }
	
	/**
	 * 获取用户名
	 * 
	 * @return
	 */
    @Override
	public Users getUsers(Integer userId) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);

		return this.usersMapper.selectByExample(example).get(0);
	}
    
    /**
     * 获取用户名
     * 
     * @return
     */
    @Override
    public Users getUsers(String userName) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria cra = example.createCriteria();
        cra.andUsernameEqualTo(userName);
        List<Users> list = this.usersMapper.selectByExample(example);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

	/**
	 * 金额合计值获取
	 * @param params
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public AdminBorrowCreditTenderCustomize sumCreditTender(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize) {
		
		return this.adminBorrowCreditRepayCustomizeMapper.sumCreditTender(adminBorrowCreditTenderCustomize);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param params
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public AdminBorrowCreditRepayCustomize sumCreditRepay(AdminBorrowCreditTenderCustomize adminBorrowCreditTenderCustomize) {
        //获取债转还款计划信息
        CreditRepayExample creditRepayExample = new CreditRepayExample();
        CreditRepayExample.Criteria creditRepayCra = creditRepayExample.createCriteria();
        creditRepayExample.setLimitStart(adminBorrowCreditTenderCustomize.getLimitStart());
        creditRepayExample.setLimitEnd(adminBorrowCreditTenderCustomize.getLimitEnd());
        creditRepayCra.andAssignNidEqualTo(adminBorrowCreditTenderCustomize.getAssignNidSrch());
        AdminBorrowCreditRepayCustomize record = this.creditRepayCustomizeMapper.sumCreditRepay(creditRepayExample);

        return record;
	}

}
