package com.hyjf.admin.manager.allocationengine;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.PublicImpl;
/**
 * 标的分配规则引擎服务实现层
 * 
 * @author
 * 
 */
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhAllocationEngineExample;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhLabelExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.HjhPlanExample.Criteria;
import com.hyjf.mybatis.model.auto.HjhRegion;
import com.hyjf.mybatis.model.auto.HjhRegionExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.thoughtworks.xstream.core.util.ThreadSafeSimpleDateFormat;
@Service
public class AllocationEngineServiceImpl extends BaseServiceImpl implements AllocationEngineService {

	@Override
	public List<HjhRegion> getRecordList(AllocationEngineBean bean, int limitStart, int limitEnd) {
		
		HjhRegionExample example = new HjhRegionExample();
		
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		HjhRegionExample.Criteria criteria = example.createCriteria();
		// 条件查询1--计划编号
		if (StringUtils.isNotEmpty(bean.getPlanNidSrch())) {
			//计划编号支持模糊查询
			criteria.andPlanNidLike("%" + bean.getPlanNidSrch() + "%");
			/*criteria.andPlanNidEqualTo(bean.getPlanNidSrch());*/	
		}
		// 条件查询2--计划名称
		if (StringUtils.isNotEmpty(bean.getPlanNameSrch())) {
			criteria.andPlanNameLike("%" + bean.getPlanNameSrch() + "%");
		}
		// 条件查询3--状态
		if (bean.getConfigStatusSrch()!=null) {
			criteria.andConfigStatusEqualTo(bean.getConfigStatusSrch());
		}
		// 条件查询4--传入查询添加时间
		if (StringUtils.isNotEmpty(bean.getStartCreateSrch())&&StringUtils.isNotEmpty(bean.getEndCreateSrch())) {		
			SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			long start = 0 ;
			long end = 0 ;
			try {
				start = formatter.parse(bean.getStartCreateSrch()).getTime()/1000;
				end = formatter.parse(bean.getEndCreateSrch()).getTime()/1000;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int starti=(int)start;
//			criteria.andConfigAddTimeBetween((int)start, (int)end);
			criteria.andConfigAddTimeLessThanOrEqualTo((int)end+86399);
			criteria.andConfigAddTimeGreaterThanOrEqualTo((int)start);
		}
		example.setOrderByClause("`config_add_time` Desc");
		return hjhRegionMapper.selectByExample(example);
	}
	
	/**
	 * 获取单个计划(更新状态用)
	 * 
	 * @return
	 */
	@Override
	public HjhRegion getRecord(Integer record) {
		HjhRegion hjhRegion = hjhRegionMapper.selectByPrimaryKey(record);
		return hjhRegion;
	}

	/**
	 * 单个计划插入
	 * 
	 * @param record
	 */
	@Override
	public void insertRecord(HjhRegion record) {
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		record.setCreateUser(Integer.parseInt(adminSystem.getId()));
		hjhRegionMapper.insertSelective(record);
	}

	@Override
	public String getPlanName(String planNid) {
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		cra.andPlanNidEqualTo(planNid);
		List<HjhPlan> result = this.hjhPlanMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0).getPlanName();
		}
		return null;
	}

	@Override
	public String checkInputPlanNidSrch(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		message = message.replace("{label}", "借款金额");
		String planNid = request.getParameter("param");
		//计划编号未入力
		if (StringUtils.isEmpty(planNid)) {
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		int errorFlag = this.checkInputPlanNidSrch(planNid);
		if (errorFlag == 1) {
			message = ValidatorFieldCheckUtil.getErrorMessage("planNid.not.exist", "");//username.not.exist 该计划编号"不存在"与汇计划表中
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} else if (errorFlag == 2) {
			message = ValidatorFieldCheckUtil.getErrorMessage("planNid.engine.repeat");//username.not.accounts 该计划编号已存在与计划专区表
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} 
		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
		
		return ret.toJSONString();
	}
	
	
	/**
	 * 校验入力的planNid
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	public int checkInputPlanNidSrch(String planNid) {
		if (StringUtils.isNotEmpty(planNid)) {
			HjhPlanExample example = new HjhPlanExample();
			HjhPlanExample.Criteria cra = example.createCriteria();
			cra.andPlanNidEqualTo(planNid);
			List<HjhPlan> planList = this.hjhPlanMapper.selectByExample(example);
			if (planList == null || planList.size() == 0) {
				// 该计划编号"不存在"与汇计划表中
				return 1;
			}
			
			HjhRegionExample example1 = new HjhRegionExample();
			HjhRegionExample.Criteria cra1 = example1.createCriteria();
			cra1.andPlanNidEqualTo(planNid);
			List<HjhRegion> planList1 = this.hjhRegionMapper.selectByExample(example1);
			if (planList1.size() >= 1) {//查出多条就是重复
				// 该计划编号已存在于计划专区表
				return 2;
			}	
		}
		return 0;
	}

	@Override
	public void updateRecord(HjhRegion record) {
		record.setUpdateTime(GetDate.getNowTime10());
		hjhRegionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Integer countPlanConfig(AllocationEngineBean allocationEngineBean) {
		int ret = 0;
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria cra = example.createCriteria();
		// 传入查询计划编号
		if (StringUtils.isNotEmpty(allocationEngineBean.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(allocationEngineBean.getPlanNidSrch());	
		}
		ret = hjhAllocationEngineMapper.countByExample(example);
		return ret;
	}

	@Override
	public List<HjhAllocationEngine> getAllocationEngineRecordList(AllocationEngineBean bean, int limitStart,
			int limitEnd) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		// 条件查询1--计划编号
		if (StringUtils.isNotEmpty(bean.getPlanNidSrch())) {
			criteria.andPlanNidEqualTo(bean.getPlanNidSrch());	
		}
		example.setOrderByClause("`add_time` Desc");
		return hjhAllocationEngineMapper.selectByExample(example);
	}

	@Override
	public HjhAllocationEngine getPlanConfigRecord(Integer record) {
		HjhAllocationEngine hjhAllocationEngine = hjhAllocationEngineMapper.selectByPrimaryKey(record);
		return hjhAllocationEngine;
	}

	@Override
	public void updatePlanConfigRecord(HjhAllocationEngine record) {
		record.setUpdateTime(GetDate.getNowTime10());
		hjhAllocationEngineMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public String getPlanNameById(String id) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(Integer.valueOf(id));
		List<HjhAllocationEngine> result = this.hjhAllocationEngineMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0).getPlanName();
		}
		return null;
	}

	@Override
	public HjhLabel getHjhLabelRecord(String labelName) {
		HjhLabelExample example = new HjhLabelExample(); 
		HjhLabelExample.Criteria cra = example.createCriteria();
		cra.andLabelNameEqualTo(labelName.trim());
		List<HjhLabel> result = this.hjhLabelMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public String getPlanBorrowStyle(String planNid) {
		HjhPlanExample example = new HjhPlanExample(); 
		HjhPlanExample.Criteria cra = example.createCriteria();
		cra.andPlanNidEqualTo(planNid);
		List<HjhPlan> result = this.hjhPlanMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0).getBorrowStyle();
		}
		return null;
	}

	@Override
	public boolean checkRepeat(String labelName, String planNid) {
		Integer labelId = 0;
		HjhLabelExample example1 = new HjhLabelExample(); 
		HjhLabelExample.Criteria cra = example1.createCriteria();
		cra.andLabelNameEqualTo(labelName.trim());
		List<HjhLabel> result1 = this.hjhLabelMapper.selectByExample(example1);
		if (result1 != null && result1.size() > 0) {
			 labelId = result1.get(0).getId();
		}
		//这个标签一旦存在引擎，那么新入力标签不只是在本计划添加不了，在其他计划也不能添加
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		criteria.andLabelNameEqualTo(labelName.trim());
		/*criteria.andPlanNidEqualTo(planNid);*/
		/*criteria.andLabelStatusEqualTo(1);*///标签状态 0：停用 1：启用
		criteria.andLabelIdEqualTo(labelId);
		List<HjhAllocationEngine> result = this.hjhAllocationEngineMapper.selectByExample(example);
		if (result != null && result.size() >= 1) {
			return false;
		}
		return true;
	}

	@Override
	public List<HjhAllocationEngine> getHjhAllocationEngineListByPlan(String planNid) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		// 条件查询1--计划编号
		if (StringUtils.isNotEmpty(planNid)) {
			criteria.andPlanNidEqualTo(planNid.trim());	
		}
		return hjhAllocationEngineMapper.selectByExample(example);
	}

	@Override
	public HjhRegion getHjhRegionRecord(String planNid) {
		HjhRegionExample example = new HjhRegionExample();
		HjhRegionExample.Criteria cra = example.createCriteria();
		cra.andPlanNidEqualTo(planNid.trim());
		List<HjhRegion> result = this.hjhRegionMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public void insertHjhAllocationEngineRecord(HjhAllocationEngine record) {
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		record.setCreateUser(Integer.parseInt(adminSystem.getId()));
		hjhAllocationEngineMapper.insertSelective(record);
	}

	@Override
	public void updateAllocationEngineRecord(String planNid,Integer configStatus) {
		if (StringUtils.isNotEmpty(planNid)) {
			HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
			HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
			criteria.andPlanNidEqualTo(planNid);
	
			List<HjhAllocationEngine> planList = this.hjhAllocationEngineMapper.selectByExample(example);
			if (planList != null && planList.size() > 0) {
				for( int i = 0 ; i < planList.size() ; i++) {
					HjhAllocationEngine hjhAllocationEngine = planList.get(i);
					//只有当计划专区的config_status状态更为0：停用时，将引擎表的计划状态和标签状态也置为0
					if (configStatus == 0) {
						hjhAllocationEngine.setConfigStatus(0);
						hjhAllocationEngine.setLabelStatus(0);
					} else {
						hjhAllocationEngine.setConfigStatus(1);//如果计划专区的计划改为启用，那么引擎的计划状态改为启用
					}
					this.hjhAllocationEngineMapper.updateByPrimaryKeySelective(hjhAllocationEngine);
					//可能會有else
					// 数据更新
				}
			}
		}
	}

	@Override
	public HjhAllocationEngine getRecordBylabelId(String planNid, String labelId) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		criteria.andPlanNidEqualTo(planNid);
		criteria.andLabelIdEqualTo(Integer.valueOf(labelId));
		List<HjhAllocationEngine> result = this.hjhAllocationEngineMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public HjhAllocationEngine getRecordBylabelName(String planNid, String labelName) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		criteria.andPlanNidEqualTo(planNid);
		criteria.andLabelNameEqualTo(labelName.trim());
		List<HjhAllocationEngine> result = this.hjhAllocationEngineMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public void updateHjhAllocationEngineRecord(HjhAllocationEngine record) {
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		record.setUpdateUser(Integer.parseInt(adminSystem.getId()));
		hjhAllocationEngineMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public HjhRegion getHjhRegionRecordByName(String planName) {
		HjhRegionExample example = new HjhRegionExample();
		HjhRegionExample.Criteria cra = example.createCriteria();
		cra.andPlanNameEqualTo(planName.trim());
		List<HjhRegion> result = this.hjhRegionMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	
	@Override
	public HjhAllocationEngine getRecordBy(String planName, String labelName) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		criteria.andPlanNameEqualTo(planName);
		criteria.andLabelNameEqualTo(labelName.trim());
		List<HjhAllocationEngine> result = this.hjhAllocationEngineMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public HjhAllocationEngine selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return hjhAllocationEngineMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 删除标签的方法
	 * wangxiaohui
	 * 
	 */
	@Override
	public void deleteEngine(String planName, String labelName) {
		HjhAllocationEngineExample example = new HjhAllocationEngineExample(); 
		HjhAllocationEngineExample.Criteria criteria = example.createCriteria();
		criteria.andPlanNameEqualTo(planName);
		criteria.andLabelNameEqualTo(labelName.trim());
		this.hjhAllocationEngineMapper.deleteByExample(example);
	}
	
	@Override
	public void insertEngine(HjhAllocationEngine record) {
		hjhAllocationEngineMapper.insert(record);
	}
	
	/**
	 * 计划编号必须是存在于计划表中的并且不能是delflag为1的
	 * 
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	@Override
	public String isExistsPlanNumber(HttpServletRequest request) {
		JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		message = message.replace("{label}", "智投编号");
		String param = request.getParameter("param");
		if (StringUtils.isEmpty(param)) {
			return ret.toJSONString();
		}
		int usersFlag = this.isExistsPlanNumber(param);
		if (usersFlag == 1) {
			message = ValidatorFieldCheckUtil.getErrorMessage("planid.not.exists", "");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} else if (usersFlag == 2) {
			message = ValidatorFieldCheckUtil.getErrorMessage("planid.not.account");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		} 
		// added by LIBIN 20180515 start
		else if (usersFlag == 3) {
			message = ValidatorFieldCheckUtil.getErrorMessage("planid.not.exists.region");
			ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
			return ret.toString();
		}
		// added by LIBIN 20180515 end
	
		ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
		return ret.toJSONString();
	}
	
	
	
	/**
	 * 去数据库查询计划编号是不是存在
	 * @param planNid
	 * @return
	 */
	@Override
	public int isExistsPlanNumber(String planNid) {
		if (StringUtils.isNotEmpty(planNid)) {
			HjhPlanExample example = new HjhPlanExample();
			Criteria criteria = example.createCriteria();
			criteria.andPlanNidEqualTo(planNid);
			List<HjhPlan> list = this.hjhPlanMapper.selectByExample(example);
			if(list == null || list.size()==0) {
				// 该计划不存在。
				return 1;
			}
			HjhPlan hjhPlan = list.get(0);
			if (hjhPlan.getDelFlg()==1) {
				//该计划已经被禁用
				return 2;
			}
			// added by LIBIN 20180515 start
			// 查询该计划编号是否存在于计划专区表
			HjhRegionExample example1 = new HjhRegionExample();
			HjhRegionExample.Criteria criteria1 = example1.createCriteria();
			criteria1.andPlanNidEqualTo(planNid);
			List<HjhRegion> list1 = this.hjhRegionMapper.selectByExample(example1);
			if(list1 == null || list1.size()==0) {
				// 该计划编号不存在与计划专区
				return 3;
			}
			// added by LIBIN 20180515 end
		}
		return 0;
	}

	

	

}
