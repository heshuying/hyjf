package com.hyjf.batch.datarecover.creditrec;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.UsersChangeLog;
import com.hyjf.mybatis.model.auto.UsersChangeLogExample;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;

/**
 * 债转承接时推荐人信息恢复保存
 * @author hsy
 *
 */
@Service
public class CreditTenderRecRecoverServiceImpl extends BaseServiceImpl implements CreditTenderRecRecoverService {

    Logger _log = LoggerFactory.getLogger(CreditTenderRecRecoverServiceImpl.class);
    
    /**
     * 检索待修复承接记录
     * @param idStart
     * @param idEnd
     * @return
     */
    @Override
	public List<CreditTender> selectTenderList(Integer idStart, Integer idEnd){
    	if(Validator.isNull(idStart)){
    		idStart = 0;
    	}
    	
    	if(Validator.isNull(idEnd)){
    		idEnd = 1000;
    	}
    	
    	CreditTenderExample example = new CreditTenderExample();
    	example.createCriteria().andAssignIdGreaterThanOrEqualTo(idStart).andAssignIdLessThan(idEnd);
    	
    	List<CreditTender> tenderList = creditTenderMapper.selectByExample(example);
    	
    	if(tenderList == null){
    		return new ArrayList<CreditTender>();
    	}
    	
    	return tenderList;
    }
    
    /**
     * 查询最大承接记录id
     * @return
     */
	@Override
	public int getMaxTenderId(){
    	CreditTenderExample example = new CreditTenderExample();
    	example.setOrderByClause("assign_id desc");
    	example.setLimitStart(0);
    	example.setLimitEnd(1);
    	List<CreditTender> tenderList = creditTenderMapper.selectByExample(example);
    	if(tenderList == null || tenderList.isEmpty()){
    		return 0;
    	}
    	
    	return tenderList.get(0).getAssignId();
    }
    
    /**
     * 查询用户的推荐人修改历史
     * @param userId
     * @return
     */
    @Override
	public List<UsersChangeLog>  selectRecChangeLog(Integer userId){
    	if(Validator.isNull(userId)){
    		return new ArrayList<UsersChangeLog>();
    	}
    	
    	UsersChangeLogExample example = new UsersChangeLogExample();
    	example.createCriteria().andUserIdEqualTo(userId).andChangeTypeEqualTo(1);
    	example.setOrderByClause("change_time desc");
    	List<UsersChangeLog> changeLogs = usersChangeLogMapper.selectByExample(example);
    	
    	if(changeLogs == null){
    		return new ArrayList<UsersChangeLog>();
    	}
    	
    	return changeLogs;
    }
    
    /**
     * 查询用户的部门信息等
     * @param userId
     * @return
     */
    @Override
	public UserInfoCustomize selectUserDepartmentInfo(Integer userId){
    	UserInfoCustomize userInfoCustomize = userInfoCustomizeMapper.queryUserInfoByUserId(userId);
    	return userInfoCustomize;
    }
    
    /**
     * 更新承接时推荐人信息主业务处理
     * @param record
     */
    @Override
	public void updateCreditTender(CreditTender record){
    	if(record == null){
    		return;
    	}
    	
    	// 出让人的推荐人信息修复-start
    	Integer creditUserId = record.getCreditUserId();
    	if(Validator.isNull(creditUserId)){
    		return;
    	}
    	
    	
    	UserInfoCustomize recUserInfo = getRecUserInfo(record, creditUserId);
    	
    	if(recUserInfo != null){
    		record.setInviteUserCreditName(recUserInfo.getUserName());
    		record.setInviteUserCreditAttribute(recUserInfo.getAttribute());
    		record.setInviteUserCreditRegionname(recUserInfo.getRegionName());
    		record.setInviteUserCreditBranchname(recUserInfo.getBranchName());
    		record.setInviteUserCreditDepartmentname(recUserInfo.getDepartmentName());
		}
    	
    	boolean updateFlag =  creditTenderMapper.updateByPrimaryKeySelective(record) > 0 ? true : false;
    	if(!updateFlag){
    		_log.info("更新出让人承接时推荐人信息失败，assign_nid：" + record.getAssignNid());
    	}
    	
    	// 承接人的推荐人信息修复-start
    	Integer tenderUserId = record.getUserId();
    	if(Validator.isNull(tenderUserId)){
    		return;
    	}
    	
    	recUserInfo = null;
    	recUserInfo = getRecUserInfo(record, tenderUserId);
    	
    	if(recUserInfo != null){
			record.setInviteUserName(recUserInfo.getUserName());
			record.setInviteUserAttribute(recUserInfo.getAttribute());
			record.setInviteUserRegionname(recUserInfo.getRegionName());
			record.setInviteUserBranchname(recUserInfo.getBranchName());
			record.setInviteUserDepartmentname(recUserInfo.getDepartmentName());
		}
    	
    	updateFlag =  creditTenderMapper.updateByPrimaryKeySelective(record) > 0 ? true : false;
    	if(!updateFlag){
    		_log.info("更新承接人承接时推荐人信息失败，assign_nid：" + record.getAssignNid());
    	}
    	
    }

    /**
     * 分析获取用户承接时的推荐人信息
     * @param record
     * @param userId
     * @return
     */
	private UserInfoCustomize getRecUserInfo(CreditTender record, Integer userId) {
		String recUserName = null;
    	UserInfoCustomize recUserInfo = null;
    	
		List<UsersChangeLog> changeList = selectRecChangeLog(userId);
    	if(changeList == null || changeList.isEmpty()){ //如果没有修改过推荐人
    		SpreadsUsers spreadUser = this.getSpreadsUsersByUserId(userId);
    		if(spreadUser != null){
    			recUserInfo = userInfoCustomizeMapper.queryUserInfoByUserId(spreadUser.getSpreadsUserid());
    		}
    		
    	}else{ // 曾经修改过推荐人
    		for(UsersChangeLog changeLog : changeList){
    			if(changeLog.getChangeTime() < Integer.parseInt(record.getAddTime())){
    				recUserName = changeLog.getRecommendUser();
    				break;
    			}
    		}
    		
    		if(StringUtils.isNotEmpty(recUserName)){
    			recUserInfo = userInfoCustomizeMapper.queryUserDepartmentInfoByUserName(recUserName);
    		}
    	}
		return recUserInfo;
	}
}
