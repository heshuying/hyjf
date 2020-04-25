package com.hyjf.web.other;

import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.auto.SmsCodeMapper;
import com.hyjf.mybatis.mapper.auto.UsersMapper;
import com.hyjf.mybatis.mapper.customize.DataSearchCustomizeMapper;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.DataSearchCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.web.user.rechargefee.RechargeFeeBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lisheng
 * @version DataSearchServiceImpl, v0.1 2018/7/4 11:50
 */
@Service
public class DataSearchServiceImpl implements DataSearchService {

    @Autowired
    DataSearchCustomizeMapper dataSearchCustomizeMapper;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    protected SmsCodeMapper smsCodeMapper;


    /**
     * 查询数据
     *
     * @param dataSearchBean
     * @return
     */
    @Override
    public List<DataSearchCustomize> findDataList(DataSearchBean dataSearchBean) {
        HashMap<String, Object> req = new HashMap<>();
        List<DataSearchCustomize> dataSearchCustomizes=null;
        String flag = dataSearchBean.getType();
        toDataMap(dataSearchBean,req);
        if (dataSearchBean.getLimitStart() != null && dataSearchBean.getLimitEnd() != null) {
            req.put("limitStart", dataSearchBean.getLimitStart());
            req.put("limitEnd", dataSearchBean.getLimitEnd());
        }
        if (StringUtils.isNotBlank(flag)) {
            if (StringUtils.equals(flag, "1")) {//查询全部
                dataSearchCustomizes= dataSearchCustomizeMapper.queryList(req);
            } else if (StringUtils.equals(flag, "2")) {//计划
                dataSearchCustomizes = dataSearchCustomizeMapper.queryPlanList(req);
            } else if (StringUtils.equals(flag, "3")) {//散标
                dataSearchCustomizes = dataSearchCustomizeMapper.querySanList(req);
            }
        }
        if (dataSearchCustomizes!=null&&!dataSearchCustomizes.isEmpty()){
            for (DataSearchCustomize map : dataSearchCustomizes) {
                Integer userId = map.getUserId();
                HashMap<String, Object> req1 = new HashMap<>();
                req1.put("userId", userId);
                Map<String, Object> stringObjectMap = dataSearchCustomizeMapper.queryFirstTender(req1);
                String nid = stringObjectMap.get("nid") + "";
                if (StringUtils.equals(map.getNid(), nid)) {
                    map.setFirst("是");
                } else {
                    map.setFirst("否");
                }
            }
        }

        return dataSearchCustomizes;
    }

    /**
     * 查询导出的数据
     * @param dataSearchBean
     * @return
     */
    @Override
    public List<DataSearchCustomize>  findExportDataList(DataSearchBean dataSearchBean) {
        HashMap<String, Object> req = new HashMap<>();
        List<DataSearchCustomize> dataSearchCustomizes=null;
        String flag = dataSearchBean.getType();
        toDataMap(dataSearchBean,req);
        if (StringUtils.isNotBlank(flag)) {
            if (StringUtils.equals(flag, "1")) {//查询全部
                dataSearchCustomizes= dataSearchCustomizeMapper.queryList(req);
            } else if (StringUtils.equals(flag, "2")) {//计划
                dataSearchCustomizes = dataSearchCustomizeMapper.queryPlanList(req);
            } else if (StringUtils.equals(flag, "3")) {//散标
                dataSearchCustomizes = dataSearchCustomizeMapper.querySanList(req);
            }
        }
        return dataSearchCustomizes;
    }

    /**
     * 查询数据数量
     * @param dataSearchBean
     * @return
     */
    @Override
    public Integer findDataTotal(DataSearchBean dataSearchBean) {
        HashMap<String, Object> req = new HashMap<>();
        List<DataSearchCustomize> dataSearchCustomizes=null;
        String flag = dataSearchBean.getType();
        toDataMap(dataSearchBean,req);
        if (StringUtils.isNotBlank(flag)) {
            //查询全部
            if (StringUtils.equals(flag, "1")) {
                return dataSearchCustomizeMapper.queryCount(req);
                //计划
            } else if (StringUtils.equals(flag, "2")) {
                return dataSearchCustomizeMapper.queryPlanCount(req);
                //散标
            } else if (StringUtils.equals(flag, "3")) {
                return dataSearchCustomizeMapper.querySanCount(req);
            }
        }
        return 0;
        }

    /**
     * 查询金额
     *
     * @param dataSearchBean
     * @return
     */
    @Override
    public Map<String, Object> findMoney(DataSearchBean dataSearchBean) {
        HashMap<String, Object> req = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> maps = null;
        Map<String, Object> maps2 = null;
        String flag = dataSearchBean.getType();
        BigDecimal summoney = new BigDecimal(0);
        BigDecimal summoney1 = new BigDecimal(0);
        BigDecimal yearmoney = new BigDecimal(0);
        BigDecimal yearmoney1 = new BigDecimal(0);
        BigDecimal commission = new BigDecimal(0);
        BigDecimal commission1 = new BigDecimal(0);

        toDataMap(dataSearchBean,req);
        if (StringUtils.isNotBlank(flag)) {
            //查询全部
            if (StringUtils.equals(flag, "1")) {
                maps = dataSearchCustomizeMapper.queryPlanMoney(req);
                maps2 = dataSearchCustomizeMapper.querySanMoney(req);
                if (maps != null) {
                    summoney = (BigDecimal) (maps.get("summoney"));
                    yearmoney = (BigDecimal) maps.get("yearmoney");
                    commission = (BigDecimal) maps.get("commission");
                }
                if (maps2 != null) {
                    summoney1 = (BigDecimal) maps2.get("summoney");
                    yearmoney1 = (BigDecimal) maps2.get("yearmoney");
                    commission1 = (BigDecimal) maps2.get("commission");
                }
                BigDecimal sum = summoney.add(summoney1);
                BigDecimal year = yearmoney.add(yearmoney1);
                BigDecimal com = commission.add(commission1);
                res.put("summoney", sum);
                res.put("yearmoney", year);
                res.put("commission", com);
                //计划
            } else if (StringUtils.equals(flag, "2")) {
                res = dataSearchCustomizeMapper.queryPlanMoney(req);
                //散标
            } else if (StringUtils.equals(flag, "3")) {
                res = dataSearchCustomizeMapper.querySanMoney(req);
            }
        }
        if (res == null) {
            res=new HashMap<>();
            res.put("summoney", summoney);
            res.put("yearmoney", yearmoney);
            res.put("commission", commission);
        }
        return res;
    }

    /**
     * 拼接查询参数
     * @param dataSearchBean
     * @param req
     */
    public void toDataMap(DataSearchBean dataSearchBean,HashMap<String, Object> req) {
        if (StringUtils.isNotBlank(dataSearchBean.getAddTimeEnd()) && StringUtils.isNotBlank(dataSearchBean.getAddTimeStart())) {
            req.put("addTimeStart", dataSearchBean.getAddTimeStart());
            req.put("addTimeEnd", dataSearchBean.getAddTimeEnd());
        }
        if (StringUtils.isNotBlank(dataSearchBean.getRegTimeStart()) && StringUtils.isNotBlank(dataSearchBean.getRegTimeEnd())) {
            req.put("regTimeStart", dataSearchBean.getRegTimeStart());
            req.put("regTimeEnd", dataSearchBean.getRegTimeEnd());
        }
        if (StringUtils.isNotBlank(dataSearchBean.getTruename())) {
            req.put("truename", dataSearchBean.getTruename());
        }

        if (StringUtils.isNotBlank(dataSearchBean.getReffername())) {
            req.put("reffername", dataSearchBean.getReffername());
        }
        if (StringUtils.isNotBlank(dataSearchBean.getUsername())) {
            req.put("username", dataSearchBean.getUsername());
        }
        String source_id = PropUtils.getSystem(DataSearchDefine.SOURCE_ID);
        if (StringUtils.isNotBlank(source_id)) {
            req.put("sourceId", source_id);
        }
    }
    /**
     * 验证手机号
     * @param mobile
     * @return
     */
    @Override
    public boolean checkMobile(String mobile)
    {
        String mobileList = PropUtils.getSystem(DataSearchDefine.MOBILE_LIST);
        if(StringUtils.isNotBlank(mobileList)){
            String[] split = mobileList.split(",");
            for (String s : split) {
                if (StringUtils.equals(s,mobile)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查短信验证码
     */
    @Override
    public int checkMobileCode(String phone, String code) {
        int time = GetDate.getNowTime10();
        int timeAfter = time - 10*60;
        SmsCodeExample example = new SmsCodeExample();
        SmsCodeExample.Criteria cra = example.createCriteria();
        cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
        cra.andPosttimeLessThanOrEqualTo(time);
        cra.andMobileEqualTo(phone);
        cra.andCheckcodeEqualTo(code);
        return smsCodeMapper.countByExample(example);
    }
}