package com.hyjf.batch.bail;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.HjhBailConfig;
import com.hyjf.mybatis.model.auto.HjhBailConfigExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日发标额度累计
 *
 * @author liushouyi
 */
@Service
public class DayMarkLineTotalServiceImpl extends BaseServiceImpl implements DayMarkLineTotalService {

    Logger _log = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取所有打开日累计的数据
     *
     * @return
     */
    @Override
    public List<HjhBailConfig> selectAccumulateListAll() {
        HjhBailConfigExample example = new HjhBailConfigExample();
        List<HjhBailConfig> hjhBailConfigList = this.hjhBailConfigMapper.selectByExample(example);
        if (null != hjhBailConfigList && hjhBailConfigList.size() > 0) {
            return hjhBailConfigList;
        }
        return null;
    }

    /**
     * 获取所有打开日累计的数据
     *
     * @return
     */
    @Override
    public List<HjhBailConfig> selectAccumulateList() {
        HjhBailConfigExample example = new HjhBailConfigExample();
        example.createCriteria().andDelFlgEqualTo(0);
        List<HjhBailConfig> hjhBailConfigList = this.hjhBailConfigMapper.selectByExample(example);
        if (null != hjhBailConfigList && hjhBailConfigList.size() > 0) {
            return hjhBailConfigList;
        }
        return null;
    }
}
