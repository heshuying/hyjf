/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.nifareportlog;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.NifaReportLog;
import com.hyjf.mybatis.model.auto.NifaReportLogExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 */
@Service
public class NifaReportLogServiceImple extends BaseServiceImpl implements NifaReportLogService {

    /**
     * 根据分页查找数据
     *
     * @param limtStart
     * @param limtEnd
     * @return
     */
    @Override
    public List<NifaReportLog> selectNifaReportLog(int limtStart, int limtEnd, NifaReportLogBean form) {
        NifaReportLogExample example = new NifaReportLogExample();
        NifaReportLogExample.Criteria creteria = example.createCriteria();
        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        example.setOrderByClause(" history_data desc");
        Date dateStart = null;
        Date dateEnd = null;
        if (null != form) {
            try {
                if (StringUtils.isNotEmpty(form.getUploadImeStart())) {
                    dateStart = smp.parse(form.getUploadImeStart());
                    creteria.andCreateTimeGreaterThanOrEqualTo(dateStart);
                }
                if (StringUtils.isNotEmpty(form.getUploadImeEnd())) {
                    dateEnd = smp.parse(form.getUploadImeEnd());
                    creteria.andCreateTimeLessThanOrEqualTo(dateEnd);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(form.getFileUploadStatus())) {
                int intUploadStatus = Integer.parseInt(form.getFileUploadStatus());
                creteria.andFileUploadStatusEqualTo(intUploadStatus);
            }
            if (StringUtils.isNotBlank(form.getFeedbackResult())) {
                int intFeedBackResult = Integer.parseInt(form.getFeedbackResult());
                creteria.andFeedbackResultEqualTo(intFeedBackResult);
            }
            if (StringUtils.isNotBlank(form.getHistoryData())) {
                creteria.andHistoryDataEqualTo(form.getHistoryData());
            }
        }
        if (limtStart != -1) {
            example.setLimitStart(limtStart);
            example.setLimitEnd(limtEnd);
        }
        List<NifaReportLog> nifaReportLogList = nifaReportLogMapper.selectByExample(example);
        return nifaReportLogList;
    }

    /**
     * 根据id查找日志信息
     * @param recordId
     * @return
     */
    @Override
    public NifaReportLog getNifaReportLogById (int recordId){
        NifaReportLogExample example = new NifaReportLogExample();
        NifaReportLogExample.Criteria creteria = example.createCriteria();
        creteria.andIdEqualTo(recordId);
        List<NifaReportLog> nifaReportLogList = nifaReportLogMapper.selectByExample(example);
        if(null!=nifaReportLogList&&nifaReportLogList.size()>0){
            return nifaReportLogList.get(0);
        }
        return null;
    }

}
