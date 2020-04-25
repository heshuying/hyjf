package com.hyjf.admin.manager.user.loancover;

import java.util.List;

import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthority;
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

public interface LoanCoverService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<LoanSubjectCertificateAuthority> getRecordList(LoanSubjectCertificateAuthority lsca, int limitStart, int limitEnd,int createStart,int createEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public LoanSubjectCertificateAuthority getRecord(Integer record);

    /**
     * 根据主键判断手续费列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(String record);

    /**
     * 借贷
     * 
     * @param record
     */
    public void insertRecord(LoanSubjectCertificateAuthority record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(LoanSubjectCertificateAuthority record);
    
    /**
     * 配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

    public String isExistsAuthority(String idno,String tureName);


    

	
	
	
	
	

    
}