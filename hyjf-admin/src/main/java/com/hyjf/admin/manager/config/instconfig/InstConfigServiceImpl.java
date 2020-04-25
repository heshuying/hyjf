package com.hyjf.admin.manager.config.instconfig;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.maintenance.admin.AdminDefine;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class InstConfigServiceImpl extends BaseServiceImpl implements InstConfigService {

    /**
     * 获取取惠天利配置列表
     *
     * @return
     */
    public List<HjhInstConfig> getRecordList(int limitStart, int limitEnd) {
        HjhInstConfigExample example = new HjhInstConfigExample();
        example.createCriteria().andDelFlgEqualTo(0);
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        return hjhInstConfigMapper.selectByExample(example);
    }

    /**
     * 获取单个取惠天利配置维护
     *
     * @return
     */
    public HjhInstConfig getRecord(Integer record) {
        HjhInstConfig pushMoney = hjhInstConfigMapper.selectByPrimaryKey(record);
        return pushMoney;
    }

    /**
     * 根据主键判断取惠天利配置中数据是否存在
     *
     * @return
     */
    public boolean isExistsRecord(HjhInstConfig record) {
        HjhInstConfigExample example = new HjhInstConfigExample();
        HjhInstConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<HjhInstConfig> HjhInstConfigList = hjhInstConfigMapper.selectByExample(example);
        if (HjhInstConfigList != null && HjhInstConfigList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据主键判断取惠天利配置中数据是否存在
     *
     * @return
     */
    public boolean isExistsPermission(HjhInstConfig record) {
        HjhInstConfigExample example = new HjhInstConfigExample();
        HjhInstConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        if (record.getId() != null) {
            cra.andIdEqualTo(record.getId());
        }
        List<HjhInstConfig> HjhInstConfigList = hjhInstConfigMapper.selectByExample(example);
        if (HjhInstConfigList != null && HjhInstConfigList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 取惠天利配置插入
     *
     * @param record
     */
    public int insertRecord(HjhInstConfig record, String instCode) {
        record.setCreateTime(GetDate.getNowTime10());
        record.setUpdateTime(GetDate.getNowTime10());
        record.setInstCode(instCode);
        return hjhInstConfigMapper.insertSelective(record);
    }

    /**
     * 取惠天利配置更新
     *
     * @param record
     */
    public int updateRecord(HjhInstConfig record) {
        record.setUpdateTime(GetDate.getNowTime10());
        return hjhInstConfigMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 取惠天利配置维护删除
     *
     * @param recordList
     */
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            HjhInstConfig record = this.getRecord(id);
            record.setDelFlg(1);
            int result = hjhInstConfigMapper.updateByPrimaryKeySelective(record);

//            if(result > 0 && RedisUtils.exists(RedisConstants.CAPITAL_TOPLIMIT_+record.getInstCode())){
//            	RedisUtils.del(RedisConstants.CAPITAL_TOPLIMIT_+record.getInstCode());
//            }
        }
    }

    /**
     * 根据机构属性获取机构配置
     *
     * @param instType
     * @return
     */
    @Override
    public List<HjhInstConfig> getInstConfigByType(int instType) {
        HjhInstConfigExample example = new HjhInstConfigExample();

        HjhInstConfigExample.Criteria criteria1 = example.createCriteria();
        criteria1.andDelFlgEqualTo(0).andInstTypeEqualTo(instType);

        HjhInstConfigExample.Criteria criteria2 = example.createCriteria();
        criteria2.andDelFlgEqualTo(0).andInstTypeEqualTo(2);

        example.or(criteria2);

        example.setLimitStart(-1);
        example.setLimitEnd(-1);
        return hjhInstConfigMapper.selectByExample(example);
    }

    @Override
    public String isExists(HttpServletRequest request) {
        JSONObject ret = new JSONObject();
        String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
        message = message.replace("{label}", "资产来源编号");

        String param = request.getParameter("param");
        if (StringUtils.isEmpty(param)) {
            ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }

        HjhInstConfigExample example = new HjhInstConfigExample();
        example.createCriteria().andInstCodeEqualTo(param);
        int count = this.hjhInstConfigMapper.countByExample(example);
        if (count > 0) {
            message = ValidatorFieldCheckUtil.getErrorMessage("instcode.exists");
            ret.put(AdminDefine.JSON_VALID_INFO_KEY, message);
            return ret.toString();
        }
        ret.put(AdminDefine.JSON_VALID_STATUS_KEY, AdminDefine.JSON_VALID_STATUS_OK);
        return ret.toJSONString();
    }
}
