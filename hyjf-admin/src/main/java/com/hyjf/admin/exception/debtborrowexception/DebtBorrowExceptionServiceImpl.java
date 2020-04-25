package com.hyjf.admin.exception.debtborrowexception;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.DebtBail;
import com.hyjf.mybatis.model.auto.DebtBailExample;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtCarInfoExample;
import com.hyjf.mybatis.model.auto.DebtCompanyAuthenExample;
import com.hyjf.mybatis.model.auto.DebtCompanyInfoExample;
import com.hyjf.mybatis.model.auto.DebtHouseInfoExample;
import com.hyjf.mybatis.model.auto.DebtUsersInfoExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteBean;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowExceptionDeleteSrchBean;

@Service
public class DebtBorrowExceptionServiceImpl extends BaseServiceImpl implements DebtBorrowExceptionService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrow(DebtBorrowCommonCustomize borrowCommonCustomize) {
		return this.debtAdminBorrowExceptionMapper.countBorrow(borrowCommonCustomize);
	}

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(DebtBorrowCommonCustomize borrowCommonCustomize) {
		return this.debtAdminBorrowExceptionMapper.sumAccount(borrowCommonCustomize);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize borrowCommonCustomize) {
		return this.debtAdminBorrowExceptionMapper.selectBorrowList(borrowCommonCustomize);
	}

	/**
	 * 根据bnid获取borrow信息
	 * @param nid
	 * @return
	 * @author zhuxiaodong
	 */
		
	@Override
	public List<DebtBorrowCustomize> selectBorrowByNid(String nid) {
		// TODO Auto-generated method stub
		return this.debtAdminBorrowExceptionMapper.selectBorrowByNid(nid);	
	}

	/**
	 * 根据bnid删除borrow信息
	 * @param nid
	 * @author zhuxiaodong
	 */
		
	@Override
	public void deleteBorrowByNid(String nid) {
		List<DebtBorrowCustomize> recordList = this.debtAdminBorrowExceptionMapper.selectBorrowByNid(nid);
		if(recordList!=null && recordList.size()>0){
			DebtBorrowExceptionDeleteBean bed = null;
			AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
			for(DebtBorrowCustomize borrowCustomize : recordList){
				bed = new DebtBorrowExceptionDeleteBean();
                String bnid = borrowCustomize.getBorrowNid();
				// 判断该项目编号有没有交过保证金
				DebtBailExample exampleBail = new DebtBailExample();
				DebtBailExample.Criteria craBail = exampleBail.createCriteria();
				craBail.andBorrowNidEqualTo(bnid);
				List<DebtBail> borrowBailList = this.debtBailMapper.selectByExample(exampleBail);
				if(borrowBailList!=null && borrowBailList.size()>0){
					bed.setBail_num(borrowBailList.get(0).getBailNum());
				}
				bed.setBorrow_nid(borrowCustomize.getBorrowNid());
				bed.setBorrow_name(borrowCustomize.getBorrowName());
				bed.setUsername(borrowCustomize.getUsername());
				bed.setAccount(borrowCustomize.getAccount());
				bed.setBorrow_account_yes(borrowCustomize.getBorrowAccountYes());
				bed.setBorrow_account_wait(borrowCustomize.getBorrowAccountWait());
				bed.setBorrow_account_scale(borrowCustomize.getBorrowAccountScale());
				bed.setBorrow_style(borrowCustomize.getBorrowStyle());
				bed.setBorrow_style_name(borrowCustomize.getBorrowStyleName());
				bed.setProject_type(Integer.parseInt(borrowCustomize.getProjectType()));
				bed.setProject_type_name(borrowCustomize.getProjectTypeName());
				bed.setBorrow_period(borrowCustomize.getBorrowPeriod());
				bed.setBorrow_apr(borrowCustomize.getBorrowApr());
				bed.setStatus(borrowCustomize.getStatus());;
				bed.setAddtime(borrowCustomize.getAddtime());
				bed.setBorrow_full_time(borrowCustomize.getBorrowFullTime());
				bed.setRecover_last_time(borrowCustomize.getRecoverLastTime());
				bed.setOperater_uid(Integer.valueOf(adminSystem.getId()));
				bed.setOperater_user(adminSystem.getUsername());
				bed.setOperater_time(GetDate.getNowTime10());
				try{
					/* 首先先保存日志,然后删除borrow辅助信息,最后删除borrow信息*/
					this.debtAdminBorrowExceptionMapper.insert(bed);
					//1.删除个人信息
					DebtUsersInfoExample borrowManinfoExample = new DebtUsersInfoExample();
					DebtUsersInfoExample.Criteria borrowManinfoCra = borrowManinfoExample.createCriteria();
					borrowManinfoCra.andBorrowNidEqualTo(bnid);
					this.debtUsersInfoMapper.deleteByExample(borrowManinfoExample);
					//2.删除公司信息
					DebtCompanyInfoExample borrowUsersExample = new DebtCompanyInfoExample();
					DebtCompanyInfoExample.Criteria borrowUsersCra = borrowUsersExample.createCriteria();
					borrowUsersCra.andBorrowNidEqualTo(bnid);
					this.debtCompanyInfoMapper.deleteByExample(borrowUsersExample);
					//3.删除车辆信息
					DebtCarInfoExample borrowCarinfoExample = new DebtCarInfoExample();
					DebtCarInfoExample.Criteria borrowCarinfoCra = borrowCarinfoExample.createCriteria();
					borrowCarinfoCra.andBorrowNidEqualTo(bnid);
					this.debtCarInfoMapper.deleteByExample(borrowCarinfoExample);
					//4.删除房产信息
					DebtHouseInfoExample borrowHousesExample = new DebtHouseInfoExample();
					DebtHouseInfoExample.Criteria borrowHousesCra = borrowHousesExample.createCriteria();
					borrowHousesCra.andBorrowNidEqualTo(bnid);
					this.debtHouseInfoMapper.deleteByExample(borrowHousesExample);
					//5.删除认证信息
					DebtCompanyAuthenExample borrowCompanyAuthenExample = new DebtCompanyAuthenExample();
					DebtCompanyAuthenExample.Criteria borrowCompanyAuthenCra = borrowCompanyAuthenExample.createCriteria();
					borrowCompanyAuthenCra.andBorrowNidEqualTo(bnid);
					this.debtCompanyAuthenMapper.deleteByExample(borrowCompanyAuthenExample);
					//6.删除保证金信息
					DebtBailExample borrowBailExample = new DebtBailExample();
					DebtBailExample.Criteria borrowBailCra = borrowBailExample.createCriteria();
					borrowBailCra.andBorrowNidEqualTo(bnid);
					this.debtBailMapper.deleteByExample(borrowBailExample);
					//7.删除borrow数据
					DebtBorrowExample borrowExample = new DebtBorrowExample();
					DebtBorrowExample.Criteria borrowCra = borrowExample.createCriteria();
					borrowCra.andBorrowNidEqualTo(bnid);
					this.debtBorrowMapper.deleteByExample(borrowExample);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}

	}

	@Override
	public Long countBorrowDelete(DebtBorrowExceptionDeleteSrchBean form) {
		return this.debtAdminBorrowExceptionMapper.countBorrowDelete(form);
	}

	@Override
	public List<DebtBorrowExceptionDeleteBean> selectBorrowDeleteList(DebtBorrowExceptionDeleteSrchBean form) {
		return this.debtAdminBorrowExceptionMapper.selectBorrowDeleteList(form);
	}
}
