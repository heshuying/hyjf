package com.hyjf.batch.hjh.borrow.issuerecover;

import com.hyjf.bank.service.borrow.issue.MQBorrow;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 发标修复
 * @author xiaojohn
 *
 */
public class AutoIssueRecoverTask {
	
	Logger _log = LoggerFactory.getLogger(AutoIssueRecoverTask.class);
	
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	AutoIssueRecoverService autoIssueRecoverService;

	/**
	 * 调用任务实际方法接口
	 */
	public void run() {
		process();
	}

	/**
	 * 汇计划发标修复
	 *
	 * @return
	 */
	private boolean process() {
		if (isRun == 0) {
			_log.info("自动发标/备案/初审修复开始... ");
			
			isRun = 1;
			try {

				List statusList = new ArrayList();
				statusList.add(0);
				statusList.add(1);
				// 查询待录标列表
				List<HjhPlanAsset> sendList = this.autoIssueRecoverService.selectAssetList(statusList);
				_log.info(" 待录标总数: "+sendList.size());
				
				for (HjhPlanAsset planAsset : sendList) {
					_log.info(planAsset.getAssetId()+" 开始录标修复 ");
					this.autoIssueRecoverService.sendToMQ(planAsset, RabbitMQConstants.ROUTINGKEY_BORROW_SEND);
					_log.info(planAsset.getAssetId()+" 结束录标修复");
				}
				
				// 查询待备案列表
				statusList = new ArrayList();
				statusList.add(3);
				List<HjhPlanAsset> recordList = this.autoIssueRecoverService.selectAssetList(statusList);
				_log.info(" 待备案总数: "+recordList.size());
				
				for (HjhPlanAsset planAsset : recordList) {
					_log.info(planAsset.getAssetId()+" 开始待备案修复 ");
					this.autoIssueRecoverService.sendToMQ(planAsset, RabbitMQConstants.ROUTINGKEY_BORROW_RECORD);
					_log.info(planAsset.getAssetId()+" 结束待备案修复");
				}
				
				// 查询待初审列表
				statusList = new ArrayList();
				statusList.add(5);
				List<HjhPlanAsset> preauditList = this.autoIssueRecoverService.selectAssetList(statusList);
				_log.info(" 待初审总数: "+preauditList.size());
				
				for (HjhPlanAsset planAsset : preauditList) {
					_log.info(planAsset.getAssetId()+" 开始待初审修复 ");
					this.autoIssueRecoverService.sendToMQ(planAsset, RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
					_log.info(planAsset.getAssetId()+" 结束待初审修复");
				}
				
				
				// =================================
				// 查询待关联计划资产列表
				List<HjhPlanAsset> issueList = this.autoIssueRecoverService.selectBorrowAssetList();
				_log.info(" 原始标待关联计划总数: "+issueList.size());
				
				for (HjhPlanAsset planAsset : issueList) {
					_log.info(planAsset.getAssetId()+" 开始待关联计划资产修复 ");
					MQBorrow mqBorrow = new MQBorrow();
					mqBorrow.setBorrowNid(planAsset.getBorrowNid());
					
					this.autoIssueRecoverService.sendToMQ(mqBorrow, RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
					_log.info(planAsset.getAssetId()+" 结束待关联计划资产修复");
				}
				
				// 查询待发标关联计划列表
				List<HjhDebtCredit> creditissueList = this.autoIssueRecoverService.selectCreditAssetList();
				_log.info(" 债转待关联计划总数: "+creditissueList.size());
				
				for (HjhDebtCredit credit : creditissueList) {
					_log.info(credit.getCreditNid()+" 开始债转待关联计划资产修复 ");
					MQBorrow mqBorrow = new MQBorrow();
					mqBorrow.setCreditNid(credit.getCreditNid());
					
					this.autoIssueRecoverService.sendToMQ(mqBorrow, RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
					_log.info(credit.getCreditNid()+" 结束债转待关联计划资产修复");
				}
				
				
				// =================================================
				// 查询散标关联计划资产列表
				List<Borrow> borrowList = this.autoIssueRecoverService.selectBorrowList();
				_log.info(" 散标待关联计划总数: "+borrowList.size());
				
				for (Borrow borrow : borrowList) {
					_log.info(borrow.getBorrowNid()+" 开始散标待关联计划资产修复 ");
					MQBorrow mqBorrow = new MQBorrow();
					mqBorrow.setBorrowNid(borrow.getBorrowNid());
					
					this.autoIssueRecoverService.sendToMQ(mqBorrow, RabbitMQConstants.ROUTINGKEY_BORROW_ISSUE);
					_log.info(borrow.getBorrowNid()+" 结束散标待关联计划资产修复");
				}
				
				// =====================汇盈手动录标-各状态的标的修复 add by liushouyi HJH3============================
				List<BorrowWithBLOBs> borrowWithBLOBsList = this.autoIssueRecoverService.selectAutoBorrowNidList();
				_log.info(" 手动录标的自动备案、初审的标的总数: "+borrowWithBLOBsList.size());
				
				for (Borrow borrow : borrowWithBLOBsList) {
					_log.info(borrow.getBorrowNid()+" 开始自动备案、初审标的自动审核 ");

					// 校验标的状态位
					if (null == borrow.getStatus()) {
						_log.error(borrow.getBorrowNid()+" 该标的的状态值为空 ");
						continue;
					}
					// 自动备案的标的
					if (borrow.getStatus() == 0) {
						_log.info(borrow.getBorrowNid()+" 发送自动备案消息到MQ ");
						this.autoIssueRecoverService.sendToMQ(borrow, RabbitMQConstants.ROUTINGKEY_BORROW_RECORD);
					}
					
					// 自动初审的标的校验发标状态位
					if (borrow.getStatus() == 1 && null == borrow.getVerifyStatus()) {
						_log.error(borrow.getBorrowNid()+" 该标的的发标状态值为空 ");
						continue;
					}
					// 自动审核保证金的标的
					if (borrow.getStatus() == 1 && borrow.getVerifyStatus() == 0) {
						_log.info(borrow.getBorrowNid()+" 发送自动审核保证金消息到MQ ");
						this.autoIssueRecoverService.sendToMQ(borrow, RabbitMQConstants.ROUTINGKEY_BORROW_BAIL);
					}
					// 已审核保证金的标的发送到初审发标队列
					if (borrow.getStatus() == 1 && borrow.getVerifyStatus() > 0) {
						_log.info(borrow.getBorrowNid()+" 发送自动初审消息到MQ ");
						this.autoIssueRecoverService.sendToMQ(borrow, RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
					}
					_log.info(borrow.getBorrowNid()+" 结束自动备案、初审标的自动审核");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			
			_log.info("汇计划发标修复 结束... ");
			
		}else{
			
			_log.info("汇计划发标修复  正在运行... ");
		}
		
		return false;
	}
	
}
