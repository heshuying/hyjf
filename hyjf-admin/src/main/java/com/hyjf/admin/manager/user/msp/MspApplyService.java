package com.hyjf.admin.manager.user.msp;

import java.util.List;

import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.MspAbnormalCredit;
import com.hyjf.mybatis.model.auto.MspAbnormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspAnliInfos;
import com.hyjf.mybatis.model.auto.MspApply;
import com.hyjf.mybatis.model.auto.MspApplyDetails;
import com.hyjf.mybatis.model.auto.MspBlackData;
import com.hyjf.mybatis.model.auto.MspConfigure;
import com.hyjf.mybatis.model.auto.MspFqz;
import com.hyjf.mybatis.model.auto.MspNormalCreditDetail;
import com.hyjf.mybatis.model.auto.MspQueryDetail;
import com.hyjf.mybatis.model.auto.MspRegion;
import com.hyjf.mybatis.model.auto.MspShixinInfos;
import com.hyjf.mybatis.model.auto.MspTitle;
import com.hyjf.mybatis.model.auto.MspZhixingInfos;

public interface MspApplyService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<MspApply> getRecordList(MspApply mspApply, int limitStart, int limitEnd,int createStart,int createEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public MspApply getRecord(Integer record);

    /**
     * 根据主键判断手续费列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(MspApply record);

    /**
     * 手续费列表插入
     * 
     * @param record
     */
    public void insertRecord(MspApply record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(MspApply record);
    
    /**
     * 配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);



    /**
     * 获取城市列表
     * 
     * @return
     */
    public List<MspRegion> getRegionList();
    
    /**
     * 获取配置列表 
     * 
     * @return
     */
	public List<MspConfigure> getConfigureList();
    /**
     * 获取单个配置 
     * 
     * @return
     */
	
	public MspConfigure getConfigure(int id);

    /**
     * 获取反欺诈 
     * 
     * @return
     */
	
	public MspFqz getFqz(String applyId);
	
    /**
     * 获取反欺诈 
     * 
     * @return
     */
	
	public List<MspAnliInfos> getAnliInfos(String applyId);
	
	
	public List<MspShixinInfos> getShixinInfos(String applyId);
	
	
	public List<MspZhixingInfos> getZhixingInfos(String applyId);
	
	public MspTitle getTitle(String applyId);
	public List<MspNormalCreditDetail> getNormalCreditDetail(String applyId);
	public List<MspApplyDetails> getApplyDetails(String applyId);
	public List<MspQueryDetail> getQueryDetail(String applyId);
	public List<MspBlackData> getBlackData(String applyId);
	public List<MspAbnormalCreditDetail> getAbnormalCreditDetail(String applyId);
	public List<MspAbnormalCredit> getAbnormalCredit(String applyId);
	
	
	
	
	
	

    
}