package com.hyjf.admin.finance.web;

import java.util.List;

import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.WebCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class WebServiceImpl extends BaseServiceImpl implements WebService {

	/**
	 * 查询符合条件的网站收支数量
	 * 
	 * @param webCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryWebCount(WebCustomize webCustomize) {
		Integer accountCount = this.webCustomizeMapper
				.queryWebCount(webCustomize);
		return accountCount;

	}

	/**
	 * 网站收支列表查询
	 * 
	 * @param webCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<WebCustomize> queryWebList(WebCustomize webCustomize) {
		List<WebCustomize> accountInfos = this.webCustomizeMapper
				.queryWebList(webCustomize);
		return accountInfos;

	}

	/**
	 * 查询公司余额
	 */
	@Override
	public double getCompanyYuE(String companyId) {
		// 得到接口API对象
		// PnrApi api = new ChinaPnrApiImpl();
		ChinapnrBean pnrBean = new ChinapnrBean();
		// 版本号
		pnrBean.setVersion(ChinaPnrConstant.VERSION_10);
		// 消息类型
		pnrBean.setCmdId(ChinaPnrConstant.CMDID_QUERY_ACCTS);
		// 商户客户号
		pnrBean.setMerCustId(companyId);
		// 签名
		pnrBean.setChkValue(ChinaPnrConstant.PARAM_CHKVALUE);
		
		// ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(pnrBean);
		/*
		 * String result = api.callChinaPnrApi(pnrBean); ChinapnrBean
		 * chinapnrBean = JSONObject.parseObject(result, ChinapnrBean.class);
		 */
		
		double acctBalsSum = 0.00;
		// 调用汇付天下API接口
		JSONObject jsonObject = ChinapnrUtil.callApiBgForYuE(pnrBean);
		// 取得所有子账户详细信息
		String acctDetails = jsonObject.getString("AcctDetails");
		if(StringUtils.isNotEmpty(acctDetails)){
			JSONArray acctDetailsList = JSONArray.parseArray(acctDetails);
			// 遍历所有子账户
			for(Object object:acctDetailsList){
				JSONObject acctObject = (JSONObject)object;
				// 取得公司账户余额(所有子账户余额相加)
				acctBalsSum += acctObject.getDoubleValue("AcctBal");
				
			}
		}
		
		return acctBalsSum;
	}

	/**
	 * 查询用户交易明细的交易类型
	 * @return
	 */
	@Override
	public List<AccountTrade> selectTradeTypes() {
		AccountTradeExample example = new AccountTradeExample();
		AccountTradeExample.Criteria crt=example.createCriteria();
		crt.andStatusEqualTo(1);
		List<AccountTrade> list=accountTradeMapper.selectByExample(example);
		return list;
	}

	/**
	 * 插入网站收支记录
	 *
	 * @param accountWebList
	 * @return
	 */
	@Override
	public int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门信息
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return -1;
	}

	/**
	 * 判断网站收支是否存在
	 *
	 * @param nid
	 * @return
	 */
	private int countAccountWebList(String nid, String trade) {
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
		return this.accountWebListMapper.countByExample(example);
	}

	/**
	 * 设置部门名称
	 *
	 * @param accountWebList
	 */
	private void setDepartments(AccountWebList accountWebList) {
		if (accountWebList != null) {
			Integer userId = accountWebList.getUserId();
			UsersInfo usersInfo = getUsersInfoByUserId(userId);
			if (usersInfo != null) {
				Integer attribute = usersInfo.getAttribute();
				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = getUsersByUserId(userId);
					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}
					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是无主单，全插
					else if (users != null && (attribute == 1)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是有主单
					else if (users != null && (attribute == 0)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
				}
				accountWebList.setTruename(usersInfo.getTruename());
				accountWebList.setFlag(1);
			}
		}
	}
	
	/**
	 * 交易金额合计
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	@Override
	public String selectBorrowInvestAccount(WebCustomize webCustomize) {
		return this.webCustomizeMapper.selectBorrowInvestAccount(webCustomize);
	}
}
