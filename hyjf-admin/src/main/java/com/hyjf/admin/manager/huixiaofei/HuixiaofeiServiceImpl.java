package com.hyjf.admin.manager.huixiaofei;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.htl.dafei.ConsumeInfo;
import com.hyjf.admin.htl.dafei.DaFeiHttpRequest;
import com.hyjf.admin.htl.dafei.DaFeiService;
import com.hyjf.admin.htl.dafei.RefuseInfo;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Consume;
import com.hyjf.mybatis.model.auto.ConsumeExample;
import com.hyjf.mybatis.model.auto.ConsumeList;
import com.hyjf.mybatis.model.auto.ConsumeListExample;

/**
 * 汇消费Service
 *
 * @author 孙亮
 * @since 2015年12月14日 上午9:15:25
 */
@Service
public class HuixiaofeiServiceImpl extends BaseServiceImpl implements HuixiaofeiService {

	@Override
	public List<Consume> getConsumeRecordList(HuixiaofeiBean bean, int limitStart, int limitEnd) {
		ConsumeExample example = new ConsumeExample();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		example.setOrderByClause("insert_time DESC,id DESC");
		com.hyjf.mybatis.model.auto.ConsumeExample.Criteria criteria = example.createCriteria();
		// 条件查询
		// if (StringUtils.isNotEmpty(bean.getName())) {
		// criteria.andNameLike("%" + bean.getName() + "%");
		// }
		if (bean.getStatus() != null) {
			criteria.andStatusEqualTo(bean.getStatus());
		}
		if (bean.getRelease() != null) {
			criteria.andReleaseEqualTo(bean.getRelease());
		}
		if (StringUtils.isNotEmpty(bean.getStartCreate()) && StringUtils.isNotEmpty(bean.getEndCreate())) {
			criteria.andInsertTimeBetween(GetDate.get10Time(bean.getStartCreate()),
					GetDate.get10Time(bean.getEndCreate()));
		}
		return consumeMapper.selectByExample(example);
	}

	@Override
	public Consume getRecordByID(Integer id) {
		Consume consume = consumeMapper.selectByPrimaryKey(id);
		return consume;

	}

	@Override
	public List<ConsumeList> getConsumeListByCondition(HuixiaofeiListBean bean, int limitStart, int limitEnd) {
		ConsumeListExample example = new ConsumeListExample();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		example.setOrderByClause("insert_time DESC,id DESC");
		com.hyjf.mybatis.model.auto.ConsumeListExample.Criteria criteria = example.createCriteria();
		// 条件查询
		// if (StringUtils.isNotEmpty(bean.getName())) {
		// criteria.andNameLike("%" + bean.getName() + "%");
		// }
		// 此处的逻辑是如果有编号按编号来,否则按时间来
		if (StringUtils.isNotEmpty(bean.getConsumeId())) {
			criteria.andConsumeIdEqualTo(bean.getConsumeId());
		} else {
			if (StringUtils.isNotEmpty(bean.getInsertTime())) {
				criteria.andInsertTimeEqualTo(bean.getInsertTime());
			}
		}
		if (bean.getStatus() != null) {
			criteria.andStatusEqualTo(bean.getStatus());
		}
		return consumeListMapper.selectByExample(example);
	}

	@Override
	public void downLoadDataAction() throws Exception {
		List<ConsumeInfo> ConsumeInfoList = null;
		List<ConsumeList> list = null;
		Consume consume = null;
		String insertDay = GetDate.formatDate();
		String datetimestr = GetDate.formatTime2();
		Integer timestr = GetDate.getMyTimeInMillis();
//		System.out.println(GetDate.getDateTimeMyTimeInMillis(timestr));
		String consumeId = "";
		{
			ConsumeExample example = new ConsumeExample();
			com.hyjf.mybatis.model.auto.ConsumeExample.Criteria criteria = example.createCriteria();
			criteria.andInsertDayEqualTo(GetDate.formatDate());
			Integer size = consumeMapper.countByExample(example);
			if (size == null || size == 0) {
				consumeId = "DF" + GetDate.getDate("yyyyMMdd") + "01";
			} else {
				if (size + 1 < 10) {
					consumeId = "DF" + GetDate.getDate("yyyyMMdd") + "0" + (size + 1);
				} else {
					consumeId = "DF" + GetDate.getDate("yyyyMMdd") + (size + 1);
				}
			}
		}
		// 第一步:下载,并转换成ConsumeList
		{
			DaFeiService dafeiService = new DaFeiService();
			try {
				ConsumeInfoList = dafeiService.getConsumeInfo(datetimestr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("连接达飞数据失败");
			}
			if (ConsumeInfoList != null && ConsumeInfoList.size() > 0) {
				list = new ArrayList<ConsumeList>();
				for (int i = 0; i < ConsumeInfoList.size(); i++) {
					ConsumeList obj = new ConsumeList();
					obj.setAccountNo(ConsumeInfoList.get(i).getAccount_No());
					obj.setAddress(ConsumeInfoList.get(i).getAddress());
					obj.setBankName(ConsumeInfoList.get(i).getBank_Name());
					obj.setCompany(ConsumeInfoList.get(i).getCompany());
					obj.setConsumeId(consumeId);
					obj.setContractNo(ConsumeInfoList.get(i).getContract_No());
					obj.setCreditAmount(BigDecimal.valueOf(ConsumeInfoList.get(i).getCredit_Amount()));
					obj.setIdent(ConsumeInfoList.get(i).getIdent());
					obj.setIdentAuth(ConsumeInfoList.get(i).getIdent_Auth());
					obj.setIdentExp(ConsumeInfoList.get(i).getIdent_Exp());
					obj.setIncome(BigDecimal.valueOf(ConsumeInfoList.get(i).getIncome()));
					obj.setInitPay(ConsumeInfoList.get(i).getInit_Pay());
					obj.setInsertDay(insertDay);
					obj.setInsertTime(timestr + "");
					obj.setInstalmentNum(ConsumeInfoList.get(i).getInstalment_Num() + "");
					obj.setLoanDate(ConsumeInfoList.get(i).getLoan_Date());
					obj.setMobile(ConsumeInfoList.get(i).getMobile());
					obj.setPersonName(ConsumeInfoList.get(i).getPerson_Name());
					obj.setRelease(0);
					obj.setSex(ConsumeInfoList.get(i).getSex());
					obj.setStatus(HuixiaofeiDefine.CONSUMELIST_STATUS_2);
					list.add(obj);
				}
			}else{
				throw new Exception("当天没有数据可下载");
			}
		}
		// 第二步:计算汇总数据和详细数据
		{
			if (list != null && list.size() > 0) {
				consume = new Consume();
				consume.setConsumeId(consumeId);
				consume.setInsertDay(insertDay);
				consume.setInsertTime(timestr + "");
				consume.setTime(insertDay);
				consume.setRelease(HuixiaofeiDefine.CONSUME_RELEASE_0);
				consume.setStatus(HuixiaofeiDefine.CONSUME_STATUS_0);
				consume.setAmount(BigDecimal.ZERO);
				consume.setAmountReal(BigDecimal.ZERO);
				consume.setPersonNum(0);
				consume.setPersonReal(0);
				for (int i = 0; i < list.size(); i++) {
					consume.setAmount(consume.getAmount().add(list.get(i).getCreditAmount()));
					consume.setPersonNum(consume.getPersonNum() + 1);
				}
			}
		}
		// 第三步:将汇总数据和详表数据保存到数据库
		{
			if (list != null && list.size() > 0) {
				consumeMapper.insertSelective(consume);
				for (int i = 0; i < list.size(); i++) {
					consumeListMapper.insertSelective(list.get(i));
				}
			}

		}
	}

	@Override
	public void shenhe(List<Integer> ids, Integer updatestatus) {
		ConsumeListExample example = new ConsumeListExample();
		com.hyjf.mybatis.model.auto.ConsumeListExample.Criteria criteria = example.createCriteria();
		criteria.andIdIn(ids);
		List<ConsumeList> consumeListList = consumeListMapper.selectByExample(example);
		if (consumeListList != null && consumeListList.size() != 0) {
			// 第一步:查出Consume
			ConsumeExample consumeExample = new ConsumeExample();
			com.hyjf.mybatis.model.auto.ConsumeExample.Criteria consumeCriteria = consumeExample.createCriteria();
			if (StringUtils.isNotEmpty(consumeListList.get(0).getConsumeId())) {
				consumeCriteria.andConsumeIdEqualTo(consumeListList.get(0).getConsumeId());
			} else {
				consumeCriteria.andInsertDayEqualTo(consumeListList.get(0).getInsertDay());
			}
			List<Consume> consumes = consumeMapper.selectByExample(consumeExample);
			Consume consume = consumes.get(0);
			// 第二步:更新ConsumeList
			{
				for (ConsumeList consumeList : consumeListList) {
					consumeList.setStatus(updatestatus);
					consumeListMapper.updateByPrimaryKey(consumeList);
				}
				//第三步:更新Consume
				{

					ConsumeListExample allListExample = new ConsumeListExample();
					com.hyjf.mybatis.model.auto.ConsumeListExample.Criteria allListExampleCriteria = allListExample.createCriteria();
					if (StringUtils.isNotEmpty(consume.getConsumeId())) {
						allListExampleCriteria.andConsumeIdEqualTo(consume.getConsumeId());
					} else {
						allListExampleCriteria.andInsertDayEqualTo(consume.getInsertDay());
					}
					consume.setAmountReal(BigDecimal.ZERO);
					consume.setPersonReal(0);
					List<ConsumeList> allConsumeList = consumeListMapper.selectByExample(allListExample);
					for(ConsumeList consumeList:allConsumeList){
						if (consumeList.getStatus() == HuixiaofeiDefine.CONSUMELIST_STATUS_0) {
							consume.setAmountReal(consume.getAmountReal().add(consumeList.getCreditAmount()));
							consume.setPersonReal(consume.getPersonReal() + 1);
						}
					}
					// 查出所有与该包有关的数据,若都审核过了,则改包的状态(包含有未审核的不可以打包)
					if (isAllOver(consume.getConsumeId(), consume.getInsertDay())) {
						consume.setStatus(HuixiaofeiDefine.CONSUME_STATUS_1);
					}
					consumeMapper.updateByPrimaryKey(consume);
				}
			}
		}
	}

	@Override
	public Boolean isAllOver(String consumeID, String insertDay) {
		ConsumeListExample example2 = new ConsumeListExample();
		com.hyjf.mybatis.model.auto.ConsumeListExample.Criteria criteria2 = example2.createCriteria();
		if (StringUtils.isNotEmpty(consumeID)) {
			criteria2.andConsumeIdEqualTo(consumeID);
		} else {
			criteria2.andInsertDayEqualTo(insertDay);
		}
		criteria2.andStatusEqualTo(HuixiaofeiDefine.CONSUMELIST_STATUS_2);
		int weitongguosize = consumeListMapper.countByExample(example2);
		if (weitongguosize == 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public String chuli(Integer id) throws Exception {
		String resultMsg = "处理成功";
		// 第一步,根据id查出Consume
		Consume consume = getRecordByID(id);
		// 第二步,根据consume查询出需要处理的ConsumeList
		ConsumeListExample example = new ConsumeListExample();
		com.hyjf.mybatis.model.auto.ConsumeListExample.Criteria criteria2 = example.createCriteria();
		if (StringUtils.isNotEmpty(consume.getConsumeId())) {
			criteria2.andConsumeIdEqualTo(consume.getConsumeId());
		} else {
			criteria2.andInsertDayEqualTo(consume.getInsertDay());
		}
		criteria2.andStatusEqualTo(HuixiaofeiDefine.CONSUMELIST_STATUS_1);
		List<ConsumeList> consumeList = consumeListMapper.selectByExample(example);
		if (consumeList != null && consumeList.size() != 0) {
			List<RefuseInfo> refuseList = new ArrayList<RefuseInfo>();
			for (int i = 0; i < consumeList.size(); i++) {
				RefuseInfo refuseInfo = new RefuseInfo();
				refuseInfo.setContractNo(consumeList.get(i).getContractNo());
				refuseInfo.setContractDate(consumeList.get(i).getLoanDate());
				refuseInfo.setCreditAmount(consumeList.get(i).getCreditAmount() + "");
				refuseInfo.setCreditModel(DaFeiHttpRequest.CREDITMODEL);
				refuseInfo.setReason("");
				refuseInfo.setCustomerName(consumeList.get(i).getPersonName());
				refuseList.add(refuseInfo);
				consumeList.get(i).setStatus(HuixiaofeiDefine.CONSUMELIST_STATUS_3);
			}
			// 执行达飞接口,回传数据
			DaFeiService dafeiService = new DaFeiService();
			try {
				resultMsg = dafeiService.refuseContract(refuseList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("调用达飞数据接口回传数据失败");
			}
			switch (resultMsg) {
			case "000000":
				consume.setStatus(HuixiaofeiDefine.CONSUME_STATUS_2);
				consumeMapper.updateByPrimaryKey(consume);
				// 更新数据库
				for (int i = 0; i < consumeList.size(); i++) {
					consumeListMapper.updateByPrimaryKey(consumeList.get(i));
				}
				resultMsg = "共处理"+consumeList.size()+"条数据,处理成功";
				break;
			case "999999":
				resultMsg = "失败";
				break;
			case "100001":
				resultMsg = "数据校验失败";
				break;
			case "100002":
				resultMsg = "超出抽查率";
				break;
			case "100003":
				resultMsg = "参数不匹配";
				break;
			case "100005":
				resultMsg = "找不到对应的接口";
				break;
			}
		} else {
			resultMsg = "没有需要回传的数据,处理成功";
			consume.setStatus(HuixiaofeiDefine.CONSUME_STATUS_2);
			consumeMapper.updateByPrimaryKey(consume);
		}
		return resultMsg;
	}

}
